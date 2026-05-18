package pl.trinity.warehouse.warehouse_service.stock;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.trinity.warehouse.warehouse_service.client.ProductClient;
import pl.trinity.warehouse.warehouse_service.dto.ProductDto;
import pl.trinity.warehouse.warehouse_service.exception.StockNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final ProductClient productClient;

    @Transactional
    public Stock setStock(@Valid Stock stock) {
        try {
            ProductDto product = productClient.getProductBySku(stock.getSku());
            if (product == null) {
                throw new StockNotFoundException(stock.getSku());
            }
        } catch (feign.FeignException.NotFound e) {
            throw new StockNotFoundException(stock.getSku());
        }

        return stockRepository.findBySku(stock.getSku())
                .map(existingStock -> {
                    existingStock.setQuantity(stock.getQuantity());
                    return stockRepository.save(existingStock);
                })
                .orElseGet(() -> stockRepository.save(stock));
    }

    public Stock getStockBySku(String sku) {
        return stockRepository.findBySku(sku)
                .orElseThrow(() -> new StockNotFoundException(sku));
    }

//    public List<Stock> getStocks(Optional<String> sku) {
//        return sku
//                .map(stockRepository::findBySkuContainingIgnoreCase)
//                .orElseGet(stockRepository::findAll);
//    }
}
