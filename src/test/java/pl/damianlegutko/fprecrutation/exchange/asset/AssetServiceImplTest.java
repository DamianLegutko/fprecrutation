package pl.damianlegutko.fprecrutation.exchange.asset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.damianlegutko.fprecrutation.configuration.AppTestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppTestConfig.class)
public class AssetServiceImplTest {

    @Autowired
    private AssetService assetService;

    @Before
    public void setUp() throws Exception {
        assetService.buyAssetByUser(null);
    }

    @Test
    public void buyAssetByUser() {
    }

    @Test
    public void sellAssetByUser() {
    }
}