package pl.damianlegutko.fprecrutation.exchange.stock;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.damianlegutko.fprecrutation.exchange.Company;
import pl.damianlegutko.fprecrutation.exchange.stock.api.StockDTO;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockAlreadyExistsException;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockNotExistsException;

import javax.annotation.PostConstruct;

import static java.util.Objects.nonNull;

@Service("stockService")
@AllArgsConstructor
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;

    @SneakyThrows
    @Transactional(readOnly = true)
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
    @Transactional
    public void saveStock(StockDTO stock) {
        Company companyToFind = Company.parseStockCode(stock.getCompanyCode());
        if(nonNull(stockRepository.findByCompany(companyToFind))) throw new StockAlreadyExistsException();

        updateStock(stock);
    }

    @SneakyThrows
    @Transactional
    public void updateStock(StockDTO stock) {
        stockRepository.save(mapDtoToStock(stock));
    }

    @PostConstruct
    @SneakyThrows
    @Transactional
    public void initializeStock() {
        Stock stock = Stock.builder()
                        .amount(10000L)
                        .build();

        for(Company company : Company.values()) {
            stock.setCompany(company);
            stockRepository.save(stock);
        }
    }

    @SneakyThrows
    private Stock mapDtoToStock(StockDTO stock) {
        return Stock.builder()
                .company(Company.parseStockCode(stock.getCompanyCode()))
                .amount(stock.getAmount())
                .build();
    }

    private StockDTO mapStockToDto(Stock stock) {
        return StockDTO.builder()
                .companyCode(stock.getCompany().name())
                .amount(stock.getAmount())
                .build();
    }
}