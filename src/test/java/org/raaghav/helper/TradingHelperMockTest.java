package org.raaghav.helper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.raaghav.dao.TradingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TradingHelperMockTest {

    @Autowired
    TradingScheduler tradingScheduler;

    @MockBean
    TradingDao tradingDao;

    @Test
    public void updateTradeExpiryStatus_SuccessTest() {
        Mockito.doReturn(5).when(tradingDao).updateTradeExpiryStatus();

        Integer tradesUpdated = tradingScheduler.updateTradeExpiryStatus();
        assertEquals(tradesUpdated, new Integer(5));
        Mockito.verify(tradingDao, Mockito.times(1)).updateTradeExpiryStatus();
    }
}
