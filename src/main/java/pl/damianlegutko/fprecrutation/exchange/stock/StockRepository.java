package pl.damianlegutko.fprecrutation.exchange.stock;

import org.springframework.data.repository.CrudRepository;
import pl.damianlegutko.fprecrutation.exchange.Company;

interface StockRepository extends CrudRepository<Stock, Long> {
    Stock findByCompany(Company company);
}