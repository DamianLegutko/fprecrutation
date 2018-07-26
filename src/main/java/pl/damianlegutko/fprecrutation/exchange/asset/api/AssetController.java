package pl.damianlegutko.fprecrutation.exchange.asset.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.damianlegutko.fprecrutation.exchange.asset.AssetService;
import pl.damianlegutko.fprecrutation.exchange.asset.exceptions.AssetException;
import pl.damianlegutko.fprecrutation.exchange.asset.exceptions.UserHaveNotEnoughStocksException;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockCodeOutsideEnumException;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockNotExistsException;
import pl.damianlegutko.fprecrutation.user.exceptions.UserHaveNotEnoughMoneyException;
import pl.damianlegutko.fprecrutation.user.exceptions.UserNotExistsException;

@RestController
@RequestMapping("/api/asset")
@RequiredArgsConstructor
class AssetController {

    private final AssetService assetService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/buy")
    void buyAssets(@RequestBody AssetDTO asset) {
        assetService.buyAssetByUser(asset);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/sell")
    void sellAssets(@RequestBody AssetDTO asset) {
        assetService.sellAssetByUser(asset);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNotExistsException.class)
    void userNotFound() {}

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(StockNotExistsException.class)
    void stockNotFound() {}

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserHaveNotEnoughStocksException.class)
    void userHaveNotEnoughStocks() {}

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserHaveNotEnoughMoneyException.class)
    void userHaveNotEnaughMoney() {}

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(StockCodeOutsideEnumException.class)
    void stocCodekOutsideEnum() {}

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AssetException.class)
    void unhandledAssetError() {}
}