package pl.damianlegutko.fprecrutation.exchange.asset;

import pl.damianlegutko.fprecrutation.exchange.asset.api.AssetDTO;
import pl.damianlegutko.fprecrutation.exchange.asset.api.UserAssetsDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface AssetService {
    void buyAssetByUser(@NotNull AssetDTO asset);
    void sellAssetByUser(@NotNull AssetDTO asset);

    UserAssetsDTO getUserAssetsByUserName(@NotBlank @Size(min = 3, max = 256) String userName);
}
