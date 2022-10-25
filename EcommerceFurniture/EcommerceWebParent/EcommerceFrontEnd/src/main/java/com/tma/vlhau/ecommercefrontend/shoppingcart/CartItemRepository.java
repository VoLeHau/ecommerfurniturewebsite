package com.tma.vlhau.ecommercefrontend.shoppingcart;

import com.tma.vlhau.ecommercecommon.entity.CartItem;
import com.tma.vlhau.ecommercecommon.entity.Customer;
import com.tma.vlhau.ecommercecommon.entity.product.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartItemRepository extends CrudRepository<CartItem, Integer> {
	@Query("select count(c) from CartItem c where c.customer=?1")
	int getamountProductByCustomer(Customer customer);
	List<CartItem> findByCustomer(Customer customer);
	
	CartItem findByCustomerAndProduct(Customer customer, Product product);
	
	@Modifying
	@Query("UPDATE CartItem c SET c.quantity = ?1 WHERE c.customer.id = ?2 AND c.product.id = ?3")
	void updateQuantity(Integer quantity, Integer customerId, Integer productId);

	@Modifying
	@Query("DELETE FROM CartItem c WHERE c.customer.id = ?1 AND c.product.id = ?2")
	void deleteByCustomerAndProduct(Integer customerId, Integer productId);

	@Modifying
	@Query("DELETE FROM CartItem c WHERE c.customer.id = ?1")
	void deleteByCustomer(Integer customerId);

}
