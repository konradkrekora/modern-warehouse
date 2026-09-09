package pl.trinity.warehouse.warehouse_service.client;

import org.springframework.stereotype.Component;
import pl.trinity.warehouse.warehouse_service.dto.ProductDto;

@Component
public class ProductClientFallback implements ProductClient {

    @Override
    public ProductDto getProductById(Long id) {
        return null;
    }

    @Override
    public ProductDto getProductBySku(String sku) {
        return null;
    }
}
