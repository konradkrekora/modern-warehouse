package pl.trinity.warehouse.product_service.product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<Product> getProducts(@RequestParam Optional<String> name) {
        return productService.getProducts(name);
    }

    @PostMapping
    public Product createProduct(@Valid @RequestBody Product product) {
        return productService.addProduct(product);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
}
