package ru.alimov.productservice.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.alimov.productservice.demo.dto.ProductDto;
import ru.alimov.productservice.demo.model.Product;
import ru.alimov.productservice.demo.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void addProduct(Long userId, ProductDto productDto) {
        if (Objects.isNull(userId) || Objects.isNull(productDto)) {
            return;
        }
        try {
            Product product = mapDtoToProduct(productDto);
            product.setUserId(userId);
            productRepository.save(product);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void updateProduct(Long userId, ProductDto productDto) {
        if (Objects.isNull(userId) || Objects.isNull(productDto)) {
            return;
        }
        try {
            Product product = mapDtoToProduct(productDto);
            product.setUserId(userId);
            productRepository.save(product);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteProduct(Long productId) {
        if (productId == null) {
            return;
        }
        try {
            productRepository.deleteById(productId);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteAllProducts() {
        try {
            productRepository.deleteAll();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    public ProductDto getProductById(Long id) {
        ProductDto productDto = null;
        if (id == null) {
            return null;
        }
        try {
            productDto = mapProductToDto(productRepository.findById(id).orElse(null));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return productDto;
    }

    @Override
    public List<ProductDto> getProductByUserId(Long userId) {
        List<ProductDto> productDtoList = new ArrayList<>();
        if (userId == null) {
            return productDtoList;
        }
        try {
            List<Product> productList = productRepository.findByUserId(userId);
            productDtoList = productList.stream().map(p -> mapProductToDto(p)).collect(Collectors.toList());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return productDtoList;
    }

    private ProductDto mapProductToDto(Product product) {
        if (product == null) {
            return null;
        }
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setAccountNumber(product.getAccountNumber());
        productDto.setBalance(product.getBalance());
        productDto.setType(product.getType());
        return productDto;
    }

    private Product mapDtoToProduct(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }
        Product product = new Product();
        product.setId(productDto.getId());
        product.setAccountNumber(productDto.getAccountNumber());
        product.setBalance(productDto.getBalance());
        product.setType(productDto.getType());
        return product;
    }
}
