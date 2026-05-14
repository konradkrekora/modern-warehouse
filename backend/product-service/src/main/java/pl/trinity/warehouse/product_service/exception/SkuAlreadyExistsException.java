package pl.trinity.warehouse.product_service.exception;

public class SkuAlreadyExistsException extends RuntimeException {
    public SkuAlreadyExistsException(String sku) {
        super("Product with SKU " + sku + " already exists");
    }
}
