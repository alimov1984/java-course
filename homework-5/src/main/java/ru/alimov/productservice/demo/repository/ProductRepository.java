package ru.alimov.productservice.demo.repository;

import ru.alimov.productservice.demo.model.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductRepository {
    void insert(Product product) throws SQLException;

    void update(Product product) throws SQLException;

    void delete(long id) throws SQLException;

    void deleteAll() throws SQLException;

    Product getById(long productId) throws SQLException;

    List<Product> getByUserId(Long userId) throws SQLException;
}
