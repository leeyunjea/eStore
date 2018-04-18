package kr.ac.hansung.cse.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kr.ac.hansung.cse.model.Product;

@Repository
@Transactional
public class ProductDao {

	/*
	 * private JdbcTemplate jdbcTemplate;
	 * 
	 * @Autowired public void setDataSource(DataSource dataSource) { jdbcTemplate =
	 * new JdbcTemplate(dataSource); }
	 */

	@Autowired
	private SessionFactory sessionFactory;

	public Product getProductById(int id) {

		Session session = sessionFactory.getCurrentSession();
		Product product = (Product) session.get(Product.class, id);

		return product;

		/*
		 * String sqlStatement = "select * from product where id=?";
		 * 
		 * return jdbcTemplate.queryForObject(sqlStatement, new Object[] { id }, new
		 * RowMapper<Product>() {
		 * 
		 * @Override public Product mapRow(ResultSet rs, int rowNum) throws SQLException
		 * {
		 * 
		 * Product product = new Product();
		 * 
		 * product.setId(rs.getInt("id")); product.setName(rs.getString("name"));
		 * product.setCategory(rs.getString("category"));
		 * product.setManufacturer(rs.getString("manufacturer"));
		 * product.setPrice(rs.getInt("price"));
		 * product.setUnitInStock(rs.getInt("unitInStock"));
		 * product.setDescription(rs.getString("description"));
		 * 
		 * return product; }
		 * 
		 * });
		 */

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Product> getProducts() {

		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Product");
		List<Product> productList = query.list();

		return productList;

		/*
		 * String sqlStatement = "select * from product";
		 * 
		 * return jdbcTemplate.query(sqlStatement, new RowMapper<Product>() {
		 * 
		 * @Override public Product mapRow(ResultSet rs, int rowNum) throws SQLException
		 * {
		 * 
		 * Product product = new Product();
		 * 
		 * product.setId(rs.getInt("id")); product.setName(rs.getString("name"));
		 * product.setCategory(rs.getString("category"));
		 * product.setManufacturer(rs.getString("manufacturer"));
		 * product.setPrice(rs.getInt("price"));
		 * product.setUnitInStock(rs.getInt("unitInStock"));
		 * product.setDescription(rs.getString("description"));
		 * 
		 * return product; }
		 * 
		 * });
		 */
	}

	public void addProduct(Product product) {

		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(product);
		session.flush();

		/*
		 * String name = product.getName(); String category = product.getCategory(); int
		 * price = product.getPrice(); String manufacturer = product.getManufacturer();
		 * int unitInStock = product.getUnitInStock(); String description =
		 * product.getDescription();
		 * 
		 * String sqlStatement =
		 * "insert into product (name, category, price, manufacturer, unitInStock, description) values(?,?,?,?,?,?)"
		 * ;
		 * 
		 * return (jdbcTemplate.update(sqlStatement, new Object[] { name, category,
		 * price, manufacturer, unitInStock, description }) == 1);
		 */

	}

	public void deleteProduct(Product product) {

		Session session = sessionFactory.getCurrentSession();
		session.delete(product);
		session.flush();

		/*
		 * String sqlStatement = "delete from product where id=?"; return
		 * (jdbcTemplate.update(sqlStatement, new Object[] { id }) == 1);
		 */

	}

	public void updateProduct(Product product) {

		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(product);
		session.flush();

		/*
		 * int id = product.getId(); String name = product.getName(); String category =
		 * product.getCategory(); int price = product.getPrice(); String manufacturer =
		 * product.getManufacturer(); int unitInStock = product.getUnitInStock(); String
		 * description = product.getDescription();
		 * 
		 * String sqlStatement =
		 * "update product set name=?, category=?, price=?, manufacturer=?, unitInStock=?, description=? where id=?"
		 * ;
		 * 
		 * return (jdbcTemplate.update(sqlStatement, new Object[] { name, category,
		 * price, manufacturer, unitInStock, description, id}) == 1);
		 */

	}

}
