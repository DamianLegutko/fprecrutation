package pl.damianlegutko.fprecrutation.exchange.asset;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.damianlegutko.fprecrutation.exchange.asset.api.AssetDTO;
import pl.damianlegutko.fprecrutation.exchange.asset.exceptions.UserHaveNotEnoughStocksException;
import pl.damianlegutko.fprecrutation.exchange.stock.StockService;
import pl.damianlegutko.fprecrutation.exchange.stock.api.StockDTO;
import pl.damianlegutko.fprecrutation.user.UserService;
import pl.damianlegutko.fprecrutation.user.api.UserDTO;

import java.math.BigDecimal;

import static java.util.Objects.isNull;

@Service("assetService")
@AllArgsConstructor
public class AssetServiceImpl implements AssetService {
    private final AssetRepository assetRepository;
    private final StockService stockService;
    private final UserService userService;

    @SneakyThrows
    @Transactional(rollbackFor = Throwable.class)
    public void buyAssetByUser(AssetDTO assetDTO) {

        //region subtract money from user wallet
        UserDTO userDTO = userService.findUserByUsername(assetDTO.getUserName());
        BigDecimal totalStocksCost = assetDTO.getStockPrice().multiply(new BigDecimal(assetDTO.getStockAmount()));
        userService.takeMoneyFromUser(userDTO.getUsername(), totalStocksCost);
        //endregion

        //region subtract stocks from stock exchange
        StockDTO stockDTO = stockService.findStockByCompany(assetDTO.getCompanyCode());
        stockDTO.decreaseStockAmount(assetDTO.getStockAmount());
        stockService.updateStock(stockDTO);
        //endregion

        //region add stocks to user wallet
        Asset asset = assetRepository.findByUserNameAndCompany(assetDTO.getUserName(), assetDTO.getCompany());

        if (isNull(asset)) asset = mapDtoToAsset(assetDTO);
        else asset.increaseStockAmount(assetDTO.getStockAmount());

        assetRepository.save(asset);
        //endregion
    }

    @SneakyThrows
    @Transactional(rollbackFor = Throwable.class)
    public void sellAssetByUser(AssetDTO assetDTO) {

        //region add money to user wallet
        UserDTO userDTO = userService.findUserByUsername(assetDTO.getUserName());
        BigDecimal totalStocksCost = assetDTO.getStockPrice().multiply(new BigDecimal(assetDTO.getStockAmount()));
        userService.giveMoneyToUser(userDTO.getUsername(), totalStocksCost);
        //endregion

        //region add stocks to stock exchange
        StockDTO stockDTO = stockService.findStockByCompany(assetDTO.getCompanyCode());
        stockDTO.increaseStockAmount(assetDTO.getStockAmount());
        stockService.updateStock(stockDTO);
        //endregion

        //region subtract stocks from user wallet
        Asset asset = assetRepository.findByUserNameAndCompany(assetDTO.getUserName(), assetDTO.getCompany());

        if (isNull(asset))
            throw new UserHaveNotEnoughStocksException(stockDTO.getCompany().getCompanyName());
        if (asset.getStockAmount().compareTo(assetDTO.getStockAmount()) < 0)
            throw new UserHaveNotEnoughStocksException(stockDTO.getCompany().getCompanyName(), assetDTO.getStockAmount(), asset.getStockAmount());

        asset.decreaseStockAmount(assetDTO.getStockAmount());
        assetRepository.save(asset);
        //endregion
    }

    @SneakyThrows
    private Asset mapDtoToAsset(AssetDTO asset) {
        return Asset.builder()
                .company(asset.getCompany())
                .userName(asset.getUserName())
                .stockAmount(asset.getStockAmount())
                .build();
    }
}