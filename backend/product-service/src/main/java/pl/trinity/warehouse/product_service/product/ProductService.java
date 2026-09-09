package pl.trinity.warehouse.product_service.product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.trinity.warehouse.product_service.dto.ProductRequest;
import pl.trinity.warehouse.product_service.dto.ProductResponse;
import pl.trinity.warehouse.product_service.exception.ProductNotFoundException;
import pl.trinity.warehouse.product_service.exception.SkuAlreadyExistsException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse addProduct(ProductRequest request) {
        if (productRepository.existsBySku(request.sku())) {
            throw new SkuAlreadyExistsException(request.sku());
        }

        Product product = Product.builder()
                .name(request.name())
                .sku(request.sku())
                .price(request.price())
                .build();

        Product savedProduct = productRepository.save(product);
        return mapToResponse(savedProduct);
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return mapToResponse(product);
    }

    public ProductResponse getProductBySku(String sku) {
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException(sku));
        return mapToResponse(product);
    }

    public List<ProductResponse> getProducts(Optional<String> name) {
        List<Product> products = name
                .map(productRepository::findByNameContainingIgnoreCase)
                .orElseGet(productRepository::findAll);

        return products.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        // Jeśli SKU uległo zmianie, sprawdzamy czy nowe SKU nie jest zajęte
        if (!product.getSku().equals(request.sku()) && productRepository.existsBySku(request.sku())) {
            throw new SkuAlreadyExistsException(request.sku());
        }

        product.setName(request.name());
        product.setSku(request.sku());
        product.setPrice(request.price());

        Product updatedProduct = productRepository.save(product);
        return mapToResponse(updatedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.delete(product);
    }

    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getSku(),
                product.getPrice()
        );
    }
}