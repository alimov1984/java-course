package ru.alimov.productservice.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alimov.productservice.demo.model.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUserId(Long userId);
}
