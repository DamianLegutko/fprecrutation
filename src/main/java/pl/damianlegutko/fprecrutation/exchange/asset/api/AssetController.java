package pl.damianlegutko.fprecrutation.exchange.asset.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.damianlegutko.fprecrutation.commonExceptions.EmptyFieldException;
import pl.damianlegutko.fprecrutation.responses.ExceptionMessage;
import pl.damianlegutko.fprecrutation.exchange.asset.AssetService;
import pl.damianlegutko.fprecrutation.exchange.asset.exceptions.AssetException;
import pl.damianlegutko.fprecrutation.exchange.asset.exceptions.UserHaveNotEnoughStocksException;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockCodeOutsideEnumException;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockHaveNotEnoughStocksException;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockNotExistsException;
import pl.damianlegutko.fprecrutation.responses.ValidationMessage;
import pl.damianlegutko.fprecrutation.user.exceptions.UserHaveNotEnoughMoneyException;
import pl.damianlegutko.fprecrutation.user.exceptions.UserNotExistsException;

import javax.validation.ValidationException;

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
    ResponseEntity userNotFound(UserNotExistsException exception) {
        return ExceptionMessage.createResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StockNotExistsException.class)
    ResponseEntity stockNotFound(StockNotExistsException exception) {
        return ExceptionMessage.createResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserHaveNotEnoughStocksException.class)
    ResponseEntity userHaveNotEnoughStocks(UserHaveNotEnoughStocksException exception) {
        return ExceptionMessage.createResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserHaveNotEnoughMoneyException.class)
    ResponseEntity userHaveNotEnaughMoney(UserHaveNotEnoughMoneyException exception) {
        return ExceptionMessage.createResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StockCodeOutsideEnumException.class)
    ResponseEntity stocCodekOutsideEnum(StockCodeOutsideEnumException exception) {
        return ExceptionMessage.createResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StockHaveNotEnoughStocksException.class)
    ResponseEntity stockHaveNotEnoughStocks(StockHaveNotEnoughStocksException exception) {
        return ExceptionMessage.createResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyFieldException.class)
    ResponseEntity stockHaveNotEnoughStocks(EmptyFieldException exception) {
        return ExceptionMessage.createResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AssetException.class)
    ResponseEntity unhandledAssetError(AssetException exception) {
        return ExceptionMessage.createResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    ResponseEntity handleValidation(ValidationException exception) {
        return ValidationMessage.createResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }
}