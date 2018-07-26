package pl.damianlegutko.fprecrutation.exchange.stock;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import pl.damianlegutko.fprecrutation.exchange.Company;
import pl.damianlegutko.fprecrutation.exchange.stock.api.StockDTO;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockNotExistsException;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockAlreadyExistsException;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockCodeOutsideEnumException;

import static java.util.Objects.nonNull;

@Service("stockService")
@AllArgsConstructor
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;

    @SneakyThrows
    public StockDTO findStockByCompany(String companyCode) {
        try {
            Company company = Company.valueOf(companyCode);
            Stock stock = stockRepository.findByCompany(company);

            if (nonNull(stock)) {
                return mapStockToDto(stock);
            }
            else {
                throw new StockNotExistsException();
            }
        }
        catch (IllegalArgumentException enumValueNotFound) {
            throw new StockNotExistsException();
        }
    }

    @SneakyThrows
    public void saveStock(StockDTO stock) {
        Company companyToFind = parseStockCodeToEnum(stock.getCompanyCode());
        if(nonNull(stockRepository.findByCompany(companyToFind))) throw new StockAlreadyExistsException();

        updateStock(stock);
    }

    @SneakyThrows
    public void updateStock(StockDTO stock) {
        stockRepository.save(mapDtoToStock(stock));
    }

    @SneakyThrows
    private Stock mapDtoToStock(StockDTO stock) {
        return Stock.builder()
                .company(parseStockCodeToEnum(stock.getCompanyCode()))
                .amount(stock.getAmount())
                .build();
    }

    private StockDTO mapStockToDto(Stock stock) {
        return StockDTO.builder()
                .companyCode(stock.getCompany().name())
                .amount(stock.getAmount())
                .build();
    }

    private Company parseStockCodeToEnum(String stockCode) throws StockCodeOutsideEnumException {
        try {
            return Company.valueOf(stockCode);
        }
        catch (IllegalArgumentException enumWithThisCodeDontExists){
            throw new StockCodeOutsideEnumException();
        }
    }
}