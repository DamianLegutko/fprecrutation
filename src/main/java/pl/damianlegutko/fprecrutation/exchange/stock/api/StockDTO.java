package pl.damianlegutko.fprecrutation.exchange.stock.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockDTO {
    private String companyCode;
    private Long amount;
}