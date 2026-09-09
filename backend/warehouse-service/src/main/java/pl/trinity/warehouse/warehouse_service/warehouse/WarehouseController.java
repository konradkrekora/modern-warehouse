package pl.trinity.warehouse.warehouse_service.warehouse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.trinity.warehouse.warehouse_service.stock.Stock;
import pl.trinity.warehouse.warehouse_service.stock.StockService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/warehouse")
public class WarehouseController {

    private final StockService stockService;

    @GetMapping
    public Stock getStocks(@RequestParam String sku) {
        return stockService.getStockBySku(sku);
    }

    @PostMapping
    public Stock updateStock(@Valid @RequestBody Stock stock) {
        return stockService.setStock(stock);
    }
}
