package pl.damianlegutko.fprecrutation.exchange.stock.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(StockNotExistsException.class)
    void stockNotFound() {}

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(StockCodeOutsideEnumException.class)
    void stocCodekOutsideEnum() {}

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(StockAlreadyExistsException.class)
    void stockExists() {}

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(StockException.class)
    void unhandledStockError() {}
}