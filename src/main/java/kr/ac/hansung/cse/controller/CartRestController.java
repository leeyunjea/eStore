package kr.ac.hansung.cse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import kr.ac.hansung.cse.model.Cart;
import kr.ac.hansung.cse.model.CartItem;
import kr.ac.hansung.cse.model.Product;
import kr.ac.hansung.cse.model.User;
import kr.ac.hansung.cse.service.CartItemService;
import kr.ac.hansung.cse.service.CartService;
import kr.ac.hansung.cse.service.ProductService;
import kr.ac.hansung.cse.service.UserService;

@RestController // @Controller + @ResponseBody
@RequestMapping("/api/cart")
public class CartRestController {

	@Autowired
	private CartService cartService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProductService productService;

	@RequestMapping(value = "/{cartId}", method = RequestMethod.GET)
	public ResponseEntity<Cart> getCartById(@PathVariable(value = "cartId") int cartId) {
		Cart cart = cartService.getCartById(cartId);

		HttpHeaders headers = new HttpHeaders();
		headers.setCacheControl("max-age=10");

		return new ResponseEntity<Cart>(cart, headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/{cartId}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> clearCart(@PathVariable(value = "cartId") int cartId) {
		Cart cart = cartService.getCartById(cartId);
		cartItemService.removeAllCartItems(cart);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/add/{productId}", method = RequestMethod.PUT)
	public ResponseEntity<Void> addItem(@PathVariable(value = "productId") int productId) {
		Product product = productService.getProductById(productId);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // 현재 인증된 사용자의 이름을 얻어옴
		String username = authentication.getName();

		User user = userService.getUserByUserName(username);
		Cart cart = user.getCart(); // 현재 인증된 사용자의 cart를 가져옴

		// check if cartitem for a given product already exists
		List<CartItem> cartItems = cart.getCartItems();

		// product.setUnitInStock(product.getUnitInStock() - 1);
		// productService.updateProduct(product);

		for (int i = 0; i < cartItems.size(); i++) {
			if (product.getId() == cartItems.get(i).getProduct().getId()) {
				CartItem cartItem = cartItems.get(i);
				cartItem.setQuantity(cartItem.getQuantity() + 1);
				cartItem.setTotalPrice(product.getPrice() * cartItem.getQuantity());
				cartItemService.addCartItem(cartItem);

				return new ResponseEntity<>(HttpStatus.OK);
			}
		}

		// create new cartItem
		CartItem cartItem = new CartItem();
		cartItem.setQuantity(1);
		cartItem.setTotalPrice(product.getPrice() * cartItem.getQuantity());
		cartItem.setProduct(product);
		cartItem.setCart(cart);

		// bidirectional **********************
		cart.getCartItems().add(cartItem);

		cartItemService.addCartItem(cartItem);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/cartItem/{productId}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> removeItem(@PathVariable(value = "productId") int productId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		User user = userService.getUserByUserName(username);
		Cart cart = user.getCart();

		CartItem cartItem = cartItemService.getCartItemByProductId(cart.getId(), productId);
		cartItemService.removeCartItem(cartItem);

		// Product product = productService.getProductById(productId);
		// product.setUnitInStock(product.getUnitInStock() + cartItem.getQuantity());
		// productService.updateProduct(product);

		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

	}

	@RequestMapping(value = "/cartItem/plus/{productId}", method = RequestMethod.PUT)
	public ResponseEntity<Void> plusItem(@PathVariable(value = "productId") int productId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		User user = userService.getUserByUserName(username);
		Cart cart = user.getCart();

		CartItem cartItem = cartItemService.getCartItemByProductId(cart.getId(), productId);
		Product product = productService.getProductById(productId);

		if (product.getUnitInStock() == cartItem.getQuantity()) {
			return new ResponseEntity<Void>(HttpStatus.EXPECTATION_FAILED);
		}
		else if (product.getUnitInStock() > cartItem.getQuantity() && cartItem.getQuantity() >= 0) {
			cartItem.setQuantity(cartItem.getQuantity() + 1);
			cartItem.setTotalPrice(cartItem.getTotalPrice() + cartItem.getProduct().getPrice());
			cartItemService.updateCartItem(cartItem);
		} 

		return new ResponseEntity<Void>(HttpStatus.OK);

	}

	@RequestMapping(value = "/cartItem/minus/{productId}", method = RequestMethod.PUT)
	public ResponseEntity<Void> minusItem(@PathVariable(value = "productId") int productId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		User user = userService.getUserByUserName(username);
		Cart cart = user.getCart();

		CartItem cartItem = cartItemService.getCartItemByProductId(cart.getId(), productId);
		Product product = productService.getProductById(productId);

		if (product.getUnitInStock() >= cartItem.getQuantity() && cartItem.getQuantity() == 1) {
			cartItemService.removeCartItem(cartItem);
		} else if (product.getUnitInStock() >= cartItem.getQuantity() && cartItem.getQuantity() > 0) {
			cartItem.setQuantity(cartItem.getQuantity() - 1);
			cartItem.setTotalPrice(cartItem.getTotalPrice() - cartItem.getProduct().getPrice());
			cartItemService.updateCartItem(cartItem);
		}

		return new ResponseEntity<Void>(HttpStatus.OK);

	}

}