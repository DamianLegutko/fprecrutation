package pl.damianlegutko.fprecrutation.exchange.asset.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.damianlegutko.fprecrutation.exchange.Company;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockCodeOutsideEnumException;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssetDTO {
    String companyCode;

    Long stockAmount;

    BigDecimal stockPrice;

    String userName;

    public Company getCompany() throws StockCodeOutsideEnumException {
        return Company.parseStockCode(companyCode);
    }
}