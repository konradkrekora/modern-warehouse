package pl.trinity.warehouse.warehouse_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class ProductDto {
    private Long id;
    private String name;
    private String sku;
    private BigDecimal price;
}
