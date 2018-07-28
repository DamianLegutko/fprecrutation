package pl.damianlegutko.fprecrutation.exchange.stock;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.damianlegutko.fprecrutation.exchange.Company;
import pl.damianlegutko.fprecrutation.exchange.stock.api.StockDTO;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockAlreadyExistsException;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockCodeOutsideEnumException;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockNotExistsException;

import javax.annotation.PostConstruct;

import static java.util.Objects.nonNull;

@Service("stockService")
@AllArgsConstructor
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;

    @SneakyThrows
    public StockDTO findStockByCompany(String companyCode) {
        try {
            Stock stock = stockRepository.findByCompany(Company.valueOf(companyCode));

            if (nonNull(stock)) {
                return mapStockToDto(stock);
            }
            else {
                throw new StockNotExistsException(companyCode);
            }
        }
        catch (IllegalArgumentException enumValueNotFound) {
            throw new StockCodeOutsideEnumException(companyCode);
        }
    }

    @SneakyThrows
    public void saveStock(StockDTO stock) {
        if (nonNull(stockRepository.findByCompany(stock.getCompany()))) throw new StockAlreadyExistsException(stock.getCompany().getCompanyName());

        stock.setCompanyCode(stock.getCompanyCode().toUpperCase());

        updateStock(stock);
    }

    @SneakyThrows
    public void updateStock(StockDTO stock) {
        stockRepository.save(mapDtoToStock(stock));
    }

    @PostConstruct
    @SneakyThrows
    @Transactional
    public void initializeStock() {
        Stock stock = Stock.builder()
                        .stockAmount(10000L)
                        .build();

        for(Company company : Company.values()) {
            stock.setCompany(company);
            stockRepository.save(stock);
        }
    }

    @SneakyThrows
    private Stock mapDtoToStock(StockDTO stock) {
        return Stock.builder()
                .company(stock.getCompany())
                .stockAmount(stock.getStockAmount())
                .build();
    }

    private StockDTO mapStockToDto(Stock stock) {
        return StockDTO.builder()
                .companyCode(stock.getCompany().name())
                .stockAmount(stock.getStockAmount())
                .build();
    }
}