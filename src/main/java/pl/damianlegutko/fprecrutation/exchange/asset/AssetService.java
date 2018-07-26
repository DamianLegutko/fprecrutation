package pl.damianlegutko.fprecrutation.exchange.asset;

import pl.damianlegutko.fprecrutation.exchange.asset.api.AssetDTO;

public interface AssetService {
    void buyAssetByUser(AssetDTO asset);
    void sellAssetByUser(AssetDTO asset);
}
