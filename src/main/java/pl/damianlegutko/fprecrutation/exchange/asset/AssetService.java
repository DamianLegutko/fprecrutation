package pl.damianlegutko.fprecrutation.exchange.asset;

import pl.damianlegutko.fprecrutation.exchange.asset.api.AssetDTO;

import javax.validation.constraints.NotNull;

public interface AssetService {
    void buyAssetByUser(@NotNull AssetDTO asset);
    void sellAssetByUser(@NotNull AssetDTO asset);
}
