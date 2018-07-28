package pl.damianlegutko.fprecrutation.exchange.asset;

import org.springframework.data.repository.CrudRepository;
import pl.damianlegutko.fprecrutation.exchange.Company;

import java.util.ArrayList;

interface AssetRepository  extends CrudRepository<Asset, Long> {
    Asset findByUserNameAndCompany(String userName, Company company);
    ArrayList<Asset> findAllByUserName(String userName);
}