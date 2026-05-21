package pl.trinity.warehouse.warehouse_service.client;

import org.springframework.stereotype.Component;
import pl.trinity.warehouse.warehouse_service.dto.ProductDto;

@Component
public class ProductClientFallback implements ProductClient {

    @Override
    public Object getProductById(Long id) {
        return null;
    }

    @Override
    public ProductDto getProductBySku(String sku) {
        System.out.println("🚨 [Circuit Breaker] Awaria product-service! Uruchamiam fallback dla SKU: " + sku);
        return null;
    }
}
