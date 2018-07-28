package pl.damianlegutko.fprecrutation.exchange.stock;

import pl.damianlegutko.fprecrutation.exchange.stock.api.StockDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface StockService {
    StockDTO findStockByCompany(@NotBlank String companyCode);
    void saveStock(@NotNull StockDTO company);
    void updateStock(@NotNull StockDTO company);
    void initializeStock();
}