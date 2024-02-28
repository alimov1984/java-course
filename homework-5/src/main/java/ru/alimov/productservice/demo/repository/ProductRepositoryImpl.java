package ru.alimov.productservice.demo.repository;

import org.springframework.stereotype.Repository;
import ru.alimov.productservice.demo.model.Product;
import ru.alimov.productservice.demo.model.ProductType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final DataSource dataSource;

    public ProductRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insert(Product product) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement("INSERT INTO products (account_number, balance, type, user_id) VALUES (?, ?, ?, ?)")) {
            pst.setString(1, product.getAccountNumber());
            pst.setBigDecimal(2, product.getBalance());
            pst.setString(3, product.getType().name());
            pst.setLong(4, product.getUserId());
            pst.executeUpdate();
        }
    }

    @Override
    public void update(Product product) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement("UPDATE products SET account_number=?, balance=?, type=?, user_id=? WHERE id=?")) {
            pst.setString(1, product.getAccountNumber());
            pst.setBigDecimal(2, product.getBalance());
            pst.setString(3, product.getType().name());
            pst.setLong(4, product.getUserId());
            pst.setLong(5, product.getId());
            pst.executeUpdate();
        }
    }

    @Override
    public void delete(long id) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement("DELETE FROM products WHERE id=?")) {
            pst.setLong(1, id);
            pst.executeUpdate();
        }
    }

    @Override
    public void deleteAll() throws SQLException {
        try (Connection con = dataSource.getConnection();
             Statement st = con.createStatement()) {
            st.execute("DELETE FROM products");
        }
    }

    @Override
    public List<Product> getByUserId(Long userId) throws SQLException {
        ResultSet rs = null;
        List<Product> productList = new LinkedList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT id, account_number, balance, type, user_id FROM products WHERE user_id = ?");) {
            pst.setLong(1, userId);
            rs = pst.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getLong(1));
                product.setAccountNumber(rs.getString(2));
                product.setBalance(rs.getBigDecimal(3));
                product.setType(ProductType.valueOf(rs.getString(4)));
                product.setUserId(rs.getLong(5));
                productList.add(product);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return productList;
    }

    @Override
    public Product getById(long productId) throws SQLException {
        Product product = null;
        ResultSet rs = null;
        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT id, account_number, balance, type, user_id FROM products WHERE id=?");) {
            pst.setLong(1, productId);
            rs = pst.executeQuery();
            if (rs.next()) {
                product = new Product();
                product.setId(rs.getLong(1));
                product.setAccountNumber(rs.getString(2));
                product.setBalance(rs.getBigDecimal(3));
                product.setType(ProductType.valueOf(rs.getString(4)));
                product.setUserId(rs.getLong(5));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return product;
    }

}
