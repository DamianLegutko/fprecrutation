package pl.damianlegutko.fprecrutation.exchange.stock;

import pl.damianlegutko.fprecrutation.exchange.stock.api.StockDTO;

interface StockService {
    StockDTO findStockByCompany(String companyCode);
    void saveStock(StockDTO company);
    void updateStock(StockDTO company);
}