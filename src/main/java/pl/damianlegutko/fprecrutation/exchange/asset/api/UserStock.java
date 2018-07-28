package pl.damianlegutko.fprecrutation.exchange.asset.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStock {
    private String companyCode;
    private Long stockAmount;
}