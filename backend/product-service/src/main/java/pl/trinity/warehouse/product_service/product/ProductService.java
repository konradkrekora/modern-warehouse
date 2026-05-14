package pl.trinity.warehouse.product_service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.trinity.warehouse.product_service.exception.ProductNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public List<Product> getProducts(Optional<String> name) {
        return name
                .map(productRepository::findByNameContainingIgnoreCase)
                .orElseGet(productRepository::findAll);
    }
}
