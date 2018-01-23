package com.liyang.domain.productstorefee;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.liyang.domain.product.Product;

public interface ProductstorefeeRepository extends JpaRepository<Productstorefee, Integer>{

    List<Productstorefee> findAllByProduct(Product product);
    
    @Modifying
	@Transactional
    @Query("delete from Productstorefee s where s.product.id = ?1")
    public void deleteByProduct(Integer productId);
}
