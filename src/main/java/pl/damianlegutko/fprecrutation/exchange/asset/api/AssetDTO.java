package pl.damianlegutko.fprecrutation.exchange.asset.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssetDTO {
    String companyCode;

    BigDecimal amount;

    String userName;
}