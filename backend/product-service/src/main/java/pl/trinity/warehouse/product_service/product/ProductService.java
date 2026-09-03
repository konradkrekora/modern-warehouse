package pl.trinity.warehouse.product_service.product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.trinity.warehouse.product_service.dto.ProductResponse;
import pl.trinity.warehouse.product_service.exception.ProductNotFoundException;
import pl.trinity.warehouse.product_service.exception.SkuAlreadyExistsException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product addProduct(Product product) {
        if (productRepository.existsBySku(product.getSku())) {
            throw new SkuAlreadyExistsException(product.getSku());
        }
        return productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product getProductBySku(String sku) {
        return productRepository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException(sku));
    }

    public List<Product> getProducts(Optional<String> name) {
        return name
                .map(productRepository::findByNameContainingIgnoreCase)
                .orElseGet(productRepository::findAll);
    }

    public Product updateProduct(Long id, @Valid Product product) {
        Product existingProduct = productRepository.getProductById(id);
        existingProduct.setName(product.getName());
        existingProduct.setSku(product.getSku());
        existingProduct.setPrice(product.getPrice());
        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        Product existingProduct = getProductById(id);
        productRepository.delete(existingProduct);
    }

    private Product findEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
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
