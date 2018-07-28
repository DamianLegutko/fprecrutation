package pl.damianlegutko.fprecrutation.exchange.asset.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.damianlegutko.fprecrutation.ResponseMessage;
import pl.damianlegutko.fprecrutation.exchange.asset.AssetService;
import pl.damianlegutko.fprecrutation.exchange.asset.exceptions.AssetException;
import pl.damianlegutko.fprecrutation.exchange.asset.exceptions.UserHaveNotEnoughStocksException;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockCodeOutsideEnumException;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockHaveNotEnoughStocksException;
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
    @PostMapping("/sell")
    void sellAssets(@RequestBody AssetDTO asset) {
        assetService.sellAssetByUser(asset);
    }

    @ExceptionHandler(UserNotExistsException.class)
    ResponseEntity<Object> userNotFound(UserNotExistsException exception) {
        return ResponseMessage.createResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StockNotExistsException.class)
    ResponseEntity<Object> stockNotFound(StockNotExistsException exception) {
        return ResponseMessage.createResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserHaveNotEnoughStocksException.class)
    ResponseEntity<Object> userHaveNotEnoughStocks(UserHaveNotEnoughStocksException exception) {
        return ResponseMessage.createResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserHaveNotEnoughMoneyException.class)
    ResponseEntity<Object> userHaveNotEnaughMoney(UserHaveNotEnoughMoneyException exception) {
        return ResponseMessage.createResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StockCodeOutsideEnumException.class)
    ResponseEntity<Object> stocCodekOutsideEnum(StockCodeOutsideEnumException exception) {
        return ResponseMessage.createResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StockHaveNotEnoughStocksException.class)
    ResponseEntity<Object> stockHaveNotEnoughStocks(StockHaveNotEnoughStocksException exception) {
        return ResponseMessage.createResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AssetException.class)
    ResponseEntity<Object> unhandledAssetError(AssetException exception) {
        return ResponseMessage.createResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }
}