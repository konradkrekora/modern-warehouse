package pl.trinity.warehouse.warehouse_service.stock;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findBySku(String sku);
    boolean existsBySku(String sku);
//    List<Stock> findBySkuContainingIgnoreCase(String sku);
}
