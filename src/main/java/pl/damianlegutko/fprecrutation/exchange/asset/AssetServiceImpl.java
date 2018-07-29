package pl.damianlegutko.fprecrutation.exchange.asset;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pl.damianlegutko.fprecrutation.Validators;
import pl.damianlegutko.fprecrutation.exchange.asset.api.AssetDTO;
import pl.damianlegutko.fprecrutation.exchange.asset.api.UserAssetsDTO;
import pl.damianlegutko.fprecrutation.exchange.asset.api.UserStock;
import pl.damianlegutko.fprecrutation.exchange.asset.exceptions.UserHaveNotEnoughStocksException;
import pl.damianlegutko.fprecrutation.exchange.stock.StockService;
import pl.damianlegutko.fprecrutation.exchange.stock.api.StockDTO;
import pl.damianlegutko.fprecrutation.user.UserService;
import pl.damianlegutko.fprecrutation.user.api.UserDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Objects.isNull;

@Service("assetService")
@AllArgsConstructor
@Validated
public class AssetServiceImpl implements AssetService {
    private final AssetRepository assetRepository;
    private final StockService stockService;
    private final UserService userService;

    @SneakyThrows
    @Transactional(rollbackFor = Throwable.class)
    public void buyAssetByUser(AssetDTO assetDTO) {
        assetDTO.validateAllFields();

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
        assetDTO.validateAllFields();

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
    public UserAssetsDTO getUserAssetsByUserName(String userName) {
        Validators.objectIsNotNull(userName, "userName");

        //region get money amount from user
        UserDTO userDTO = userService.findUserByUsername(userName);
        //endregion

        //region get all stocks belong to user
        ArrayList<Asset> assetWallet = assetRepository.findAllByUserName(userName);
        //end

        UserAssetsDTO userAssetsDTO =  UserAssetsDTO.builder()
                .money(userDTO.getMoney())
                .assetWallet(mapAssetListToUserStocks(assetWallet))
                .build();

        return userAssetsDTO;
    }

    @SneakyThrows
    private Asset mapDtoToAsset(AssetDTO assetDTO) {
        assetDTO.validateAllFields();

        return Asset.builder()
                .company(assetDTO.getCompany())
                .userName(assetDTO.getUserName())
                .stockAmount(assetDTO.getStockAmount())
                .build();
    }

    @SneakyThrows
    private AssetDTO mapAssetToDto(Asset asset) {
        return AssetDTO.builder()
                .companyCode(asset.getCompany().toString())
                .userName(asset.getUserName())
                .stockAmount(asset.getStockAmount())
                .build();
    }

    @SneakyThrows
    private List<UserStock> mapAssetListToUserStocks(List<Asset> assets) {
        List<UserStock> userStocks = newArrayList();

        for (Asset asset : assets) userStocks.add(new UserStock(asset.getCompany().toString(), asset.getStockAmount()));

        return userStocks;
    }
}