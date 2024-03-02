package ru.alimov.paymentservice.demo.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.alimov.paymentservice.demo.config.IntegrationConfig;
import ru.alimov.paymentservice.demo.exception.ProductProcessException;
import ru.alimov.paymentservice.demo.exception.ProductUnavailableException;
import ru.alimov.paymentservice.demo.integration.dto.ProductDto;
import ru.alimov.paymentservice.demo.integration.dto.ProductErrorDto;

import java.net.URI;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final IntegrationConfig integrationConfig;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public ProductServiceImpl(IntegrationConfig integrationConfig, RestClient restClient,
                              ObjectMapper objectMapper) {
        this.integrationConfig = integrationConfig;
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<ProductDto> getProductsByUserId(Long userId) {
        List<ProductDto> productDtoList = restClient.get()
                .uri(URI.create(integrationConfig.getProductServiceProperties().url() + "/by-user-id/" + userId))
                .header("Accept", "application/json")
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), (request, response) -> {
                    ProductErrorDto productErrorDto = objectMapper.readValue(response.getBody(), ProductErrorDto.class);
                    throw new ProductProcessException(
                            String.format("Process error in Product service:%s", productErrorDto.getCode()));
                })
                .onStatus(status -> status.is5xxServerError(), (request, response) -> {
                    throw new ProductUnavailableException("Product service is unavailable");
                })
                .body(new ParameterizedTypeReference<>() {
                });

        return productDtoList;
    }

    @Override
    public ProductDto getProductById(Long productId) {
        ProductDto productDto = restClient.get()
                .uri(URI.create(integrationConfig.getProductServiceProperties().url() + "/by-product-id/" + productId))
                .header("Accept", "application/json")
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), (request, response) -> {
                    ProductErrorDto productErrorDto = objectMapper.readValue(response.getBody(), ProductErrorDto.class);
                    throw new ProductProcessException(
                            String.format("Process error in Product service:%s", productErrorDto.getCode()));
                })
                .onStatus(status -> status.is5xxServerError(), (request, response) -> {
                    throw new ProductUnavailableException("Product service is unavailable");
                })
                .body(ProductDto.class);

        return productDto;
    }
}
