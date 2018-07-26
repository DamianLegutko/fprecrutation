package pl.damianlegutko.fprecrutation.exchange.asset;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.damianlegutko.fprecrutation.exchange.Company;
import pl.damianlegutko.fprecrutation.exchange.asset.api.AssetDTO;
import pl.damianlegutko.fprecrutation.exchange.asset.exceptions.UserHaveNotEnoughStocksException;
import pl.damianlegutko.fprecrutation.exchange.stock.StockRepository;
import pl.damianlegutko.fprecrutation.exchange.stock.exceptions.StockNotExistsException;
import pl.damianlegutko.fprecrutation.user.UserRepository;
import pl.damianlegutko.fprecrutation.user.exceptions.UserNotExistsException;

import static java.util.Objects.nonNull;

@Service("assetService")
@AllArgsConstructor
public class AssetServiceImpl implements AssetService {
    private final AssetRepository assetRepository;
    private final StockRepository stockRepository;
    private final UserRepository userRepository;

    @SneakyThrows
    @Transactional
    public void buyAssetByUser(AssetDTO assetDTO) {
        Company company = Company.parseStockCode(assetDTO.getCompanyCode());

        if(!userRepository.existsByUsername(assetDTO.getUserName())) throw new UserNotExistsException();
        if(!stockRepository.existsByCompany(Company.parseStockCode(assetDTO.getCompanyCode()))) throw new StockNotExistsException();

        Asset asset = assetRepository.findByUserNameAndCompany(assetDTO.getUserName(), company);

        if(nonNull(asset)){
            asset.setAmount(asset.getAmount().add(assetDTO.getAmount()));
            //transakcja pieniezna
            //transakcja na gieldzie
        }
        else {
            asset = mapDtoToAsset(assetDTO);
        }

        assetRepository.save(asset);
    }

    @SneakyThrows
    @Transactional
    public void sellAssetByUser(AssetDTO assetDTO) {
        Company company = Company.parseStockCode(assetDTO.getCompanyCode());

        if(!userRepository.existsByUsername(assetDTO.getUserName())) throw new UserNotExistsException();
        if(!stockRepository.existsByCompany(Company.parseStockCode(assetDTO.getCompanyCode()))) throw new StockNotExistsException();

        Asset asset = assetRepository.findByUserNameAndCompany(assetDTO.getUserName(), company);

        if(nonNull(asset)){
            if (asset.getAmount().compareTo(assetDTO.getAmount()) > 0) {
                asset.setAmount(asset.getAmount().subtract(assetDTO.getAmount()));
                //transakcja pieniezna
                //transakcja na gieldzie
            }
            else {
                throw new UserHaveNotEnoughStocksException();
            }
        }
        else {
            asset = mapDtoToAsset(assetDTO);
        }

        assetRepository.save(asset);
    }

    @SneakyThrows
    private Asset mapDtoToAsset(AssetDTO asset) {
        return Asset.builder()
                .company(Company.parseStockCode(asset.getCompanyCode()))
                .userName(asset.getUserName())
                .amount(asset.getAmount())
                .build();
    }
}