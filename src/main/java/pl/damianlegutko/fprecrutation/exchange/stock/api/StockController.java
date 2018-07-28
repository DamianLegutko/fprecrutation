package pl.damianlegutko.fprecrutation.exchange.stock.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.damianlegutko.fprecrutation.commonExceptions.EmptyFieldException;
import pl.damianlegutko.fprecrutation.responses.ExceptionMessage;
import pl.damianlegutko.fprecrutation.exchange.stock.StockService;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockAlreadyExistsException;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockCodeOutsideEnumException;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockException;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockNotExistsException;
import pl.damianlegutko.fprecrutation.responses.ValidationMessage;

import javax.validation.ValidationException;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
class StockController {

    private final StockService stockService;

    @GetMapping("/get/{companyCode}")
    ResponseEntity getStock(@PathVariable String companyCode) {
        StockDTO stockDTO = stockService.findStockByCompany(companyCode);
        return new ResponseEntity(stockDTO, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    void saveStock(@RequestBody StockDTO stock) {
        stockService.saveStock(stock);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/")
    void updateStock(@RequestBody StockDTO stock) {
        stockService.updateStock(stock);
    }

    @ExceptionHandler(StockNotExistsException.class)
    ResponseEntity stockNotFound(StockNotExistsException exception) {
            return ExceptionMessage.createResponseEntity(exception,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StockCodeOutsideEnumException.class)
    ResponseEntity stocCodekOutsideEnum(StockCodeOutsideEnumException exception) {
        return ExceptionMessage.createResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StockAlreadyExistsException.class)
    ResponseEntity stockExists(StockAlreadyExistsException exception) {
        return ExceptionMessage.createResponseEntity(exception,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmptyFieldException.class)
    ResponseEntity stockHaveNotEnoughStocks(EmptyFieldException exception) {
        return ExceptionMessage.createResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StockException.class)
    ResponseEntity unhandledStockError(StockException exception) {
        return ExceptionMessage.createResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    ResponseEntity handleValidation(ValidationException exception) {
        return ValidationMessage.createResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }
}