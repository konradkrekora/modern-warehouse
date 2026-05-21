package pl.trinity.warehouse.warehouse_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.trinity.warehouse.warehouse_service.dto.ProductDto;

@FeignClient(name = "product-service", fallback = ProductClientFallback.class)
public interface ProductClient {

    // Ta metoda musi idealnie odpowiadać endpointowi z ProductController
    @GetMapping("/api/products/{id}")
    Object getProductById(@PathVariable("id") Long id);

    // product-service ma endpoint: GET /api/products/search?sku=XYZ
    @GetMapping("/api/products/sku/{sku}")
    ProductDto getProductBySku(@PathVariable("sku") String sku);
}
