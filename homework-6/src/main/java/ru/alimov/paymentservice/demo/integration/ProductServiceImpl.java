package ru.alimov.paymentservice.demo.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.alimov.paymentservice.demo.config.IntegrationProperties;
import ru.alimov.paymentservice.demo.exception.EntityNotFoundException;
import ru.alimov.paymentservice.demo.exception.ResourceException;
import ru.alimov.paymentservice.demo.integration.dto.ProductDto;
import ru.alimov.paymentservice.demo.integration.dto.ProductErrorDto;

import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {
    private final String USER_NOT_FOUND_CODE = "USER_NOT_FOUND";
    private final String PRODUCT_SERVICE_UNAVAILABLE_CODE = "PRODUCT_SERVICE_UNAVAILABLE";
    private final String PRODUCT_SERVICE_PROCESS_ERROR_CODE = "PRODUCT_SERVICE_PROCESS_ERROR";
    private final List<Long> userIdList = List.of(25L, 50L);
    private final IntegrationProperties integrationProperties;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public ProductServiceImpl(IntegrationProperties integrationProperties, RestClient restClient,
                              ObjectMapper objectMapper) {
        this.integrationProperties = integrationProperties;
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<ProductDto> getProductsByUserId(Long userId) {
        if (Objects.isNull(userId)) {
            throw new EntityNotFoundException(USER_NOT_FOUND_CODE, "USERID header is not defined");
        }
        if (!userIdList.contains(userId)) {
            throw new EntityNotFoundException(USER_NOT_FOUND_CODE, String.format("User with id=%d not found", userId));
        }

        List<ProductDto> productDtoList = restClient.get()
                .uri(integrationProperties.getProductServiceProperties().url() + "/by-user-id/{userId}", userId)
                .header("Accept", "application/json")
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), (request, response) -> {
                    ProductErrorDto productErrorDto = objectMapper.readValue(response.getBody(), ProductErrorDto.class);
                    throw new ResourceException(PRODUCT_SERVICE_PROCESS_ERROR_CODE,
                            String.format("Process error in Product service:%s", productErrorDto.getCode()));
                })
                .onStatus(status -> status.is5xxServerError(), (request, response) -> {
                    throw new ResourceException(PRODUCT_SERVICE_UNAVAILABLE_CODE, "Product service is unavailable");
                })
                .body(new ParameterizedTypeReference<>() {
                });

        return productDtoList;
    }

    @Override
    public ProductDto getProductById(Long productId) {
        ProductDto productDto = restClient.get()
                .uri(integrationProperties.getProductServiceProperties().url() + "/by-product-id/{productId}", productId)
                .header("Accept", "application/json")
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), (request, response) -> {
                    ProductErrorDto productErrorDto = objectMapper.readValue(response.getBody(), ProductErrorDto.class);
                    throw new ResourceException(PRODUCT_SERVICE_PROCESS_ERROR_CODE,
                            String.format("Process error in Product service:%s", productErrorDto.getCode()));
                })
                .onStatus(status -> status.is5xxServerError(), (request, response) -> {
                    throw new ResourceException(PRODUCT_SERVICE_UNAVAILABLE_CODE, "Product service is unavailable");
                })
                .body(ProductDto.class);

        return productDto;
    }
}
