package pl.trinity.warehouse.warehouse_service.stock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.trinity.warehouse.warehouse_service.client.ProductClient;
import pl.trinity.warehouse.warehouse_service.dto.ProductDto;
import pl.trinity.warehouse.warehouse_service.exception.ProductNotFoundException;
import pl.trinity.warehouse.warehouse_service.exception.StockNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private StockService stockService;

    private Stock stock;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        stock = new Stock();
        stock.setSku("SKU-123");
        stock.setQuantity(100);

        productDto = new ProductDto();
    }

    @Test
    @DisplayName("setStock: powinien utworzyć nowy stan magazynowy, gdy produkt istnieje w OpenFeign, a w bazie brak wpisu")
    void setStock_shouldCreateNewStock_whenProductExistsAndStockNotFoundInDb() {
        // given
        given(productClient.getProductBySku("SKU-123")).willReturn(productDto);
        given(stockRepository.findBySku("SKU-123")).willReturn(Optional.empty());
        given(stockRepository.save(stock)).willReturn(stock);

        // when
        Stock result = stockService.setStock(stock);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getSku()).isEqualTo("SKU-123");
        verify(stockRepository).save(stock);
    }

    @Test
    @DisplayName("setStock: powinien zaktualizować istniejący stan magazynowy, gdy produkt oraz wpis w bazie już istnieją")
    void setStock_shouldUpdateExistingStock_whenStockAlreadyExistsInDb() {
        // given
        Stock existingStock = new Stock();
        existingStock.setSku("SKU-123");
        existingStock.setQuantity(10);

        given(productClient.getProductBySku("SKU-123")).willReturn(productDto);
        given(stockRepository.findBySku("SKU-123")).willReturn(Optional.of(existingStock));
        given(stockRepository.save(existingStock)).willReturn(existingStock);

        // when
        Stock result = stockService.setStock(stock);

        // then
        assertThat(result.getQuantity()).isEqualTo(100);
        verify(stockRepository).save(existingStock);
    }

    @Test
    @DisplayName("setStock: powinien rzucić ProductNotFoundException, gdy productClient zwróci null")
    void setStock_shouldThrowProductNotFoundException_whenProductClientReturnsNull() {
        // given
        given(productClient.getProductBySku("SKU-123")).willReturn(null);

        // when & then
        assertThatThrownBy(() -> stockService.setStock(stock))
                .isInstanceOf(ProductNotFoundException.class);

        verify(stockRepository, never()).save(any());
    }

    @Test
    @DisplayName("getStockBySku: powinien zwrócić stan magazynowy, gdy istnieje w bazie")
    void getStockBySku_shouldReturnStock_whenFoundInDb() {
        // given
        given(stockRepository.findBySku("SKU-123")).willReturn(Optional.of(stock));

        // when
        Stock result = stockService.getStockBySku("SKU-123");

        // then
        assertThat(result).isNotNull();
        assertThat(result.getSku()).isEqualTo("SKU-123");
    }

    @Test
    @DisplayName("getStockBySku: powinien rzucić StockNotFoundException, gdy brak wpisu w bazie")
    void getStockBySku_shouldThrowStockNotFoundException_whenNotFoundInDb() {
        // given
        given(stockRepository.findBySku("SKU-999")).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> stockService.getStockBySku("SKU-999"))
                .isInstanceOf(StockNotFoundException.class);
    }
}