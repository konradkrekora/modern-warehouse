package pl.trinity.warehouse.warehouse_service.exception;

public class StockNotFoundException extends RuntimeException {
    public StockNotFoundException(String sku) {
        super("Stock with sku " + sku + " does not exist");
    }
}
