package com.study.springboot.domain.product.repository;

import com.study.springboot.domain.product.Product;
import com.study.springboot.enumeration.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findProductByProductCode(String code);

    // 카테고리 별로 상품 출력
    Page<Product> findByCategory(ProductCategory category, Pageable pageable);

    Page<Product> findAll(Pageable pageable);

    Page<Product> findProductByProductDelYn(Boolean delYn, Pageable pageable);

}
