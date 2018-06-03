package kr.ac.hansung.cse.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kr.ac.hansung.cse.model.Cart;
import kr.ac.hansung.cse.model.CartItem;

@Repository
@Transactional
public class CartItemDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public void addCartItem(CartItem cartItem) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(cartItem);
		session.flush();
	}
	
	public void removeCartItem(CartItem cartItem) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(cartItem);
		session.flush();
	}
	//
	public void updateCartItem(CartItem cartItem) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(cartItem);
		session.flush();
	}
	//
	public void removeAllCartItems(Cart cart) {
		List<CartItem> cartItmes = cart.getCartItems(); //lazy로 줬으면 null값이 들어감. eagel로 줬기 때문에 다 가져옴
		
		for(CartItem item : cartItmes) {
			removeCartItem(item);
		}
	}
	
	@SuppressWarnings("unchecked")
	public CartItem getCartItemByProductId(int cartId, int productId) {
		Session session = sessionFactory.getCurrentSession();
		TypedQuery<CartItem> query = session.createQuery("from CartItem where cart.id=? and product.id = ?");
		query.setParameter(0, cartId);
		query.setParameter(1, productId);
		
		return (CartItem) query.getSingleResult();
	}
}
