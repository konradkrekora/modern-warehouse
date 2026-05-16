package pl.trinity.warehouse.warehouse_service.stock;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "stocks")
@Getter
@Setter
@NoArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "SKU cannot be empty")
    @Column(unique = true, nullable = false)
    private String sku;

    @Min(value = 0, message = "Quantity cannot be less than zero")
    @NotNull(message = "Quantity is required")
    private Integer quantity;
}
