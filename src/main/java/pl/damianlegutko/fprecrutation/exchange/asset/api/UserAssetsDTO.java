package pl.damianlegutko.fprecrutation.exchange.asset.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAssetsDTO {
    BigDecimal money;
    List<UserStock> assetWallet;
}