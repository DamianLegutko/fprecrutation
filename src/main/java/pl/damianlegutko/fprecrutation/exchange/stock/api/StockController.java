package pl.damianlegutko.fprecrutation.exchange.stock.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.damianlegutko.fprecrutation.ResponseMessage;
import pl.damianlegutko.fprecrutation.exchange.stock.StockService;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockAlreadyExistsException;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockCodeOutsideEnumException;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockException;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockNotExistsException;

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
    ResponseEntity<Object> stockNotFound(StockNotExistsException exception) {
            return ResponseMessage.createResponseEntity(exception,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StockCodeOutsideEnumException.class)
    ResponseEntity<Object> stocCodekOutsideEnum(StockCodeOutsideEnumException exception) {
        return ResponseMessage.createResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StockAlreadyExistsException.class)
    ResponseEntity<Object> stockExists(StockAlreadyExistsException exception) {
        return ResponseMessage.createResponseEntity(exception,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(StockException.class)
    ResponseEntity<Object> unhandledStockError(StockException exception) {
        return ResponseMessage.createResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }
}