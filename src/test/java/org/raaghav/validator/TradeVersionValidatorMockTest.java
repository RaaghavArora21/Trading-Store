package org.raaghav.validator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.raaghav.beans.Trade;
import org.raaghav.dao.TradingDao;
import org.raaghav.helper.TradeRejectedException;
import org.raaghav.helper.TradingScheduler;
import org.raaghav.service.TradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.MessageFormat;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.raaghav.helper.TradingConstants.TRADE_MATURITY_DATE_VALIDATION_MSG;
import static org.raaghav.helper.TradingConstants.TRADE_VERSION_VALIDATION_MSG;
import static org.raaghav.util.TradeUtils.getTrade;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeVersionValidatorMockTest {

    @Autowired
    TradeVersionValidator tradeVersionValidator;

    @MockBean
    TradingDao tradingDao;

    private TradeRejectedException tradeRejectedExceptionVersionNo;


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        Mockito.when(tradingDao.getMaxVersionOfTrade(any(String.class))).thenReturn(getTradeMaxVersion());
    }

    @Test
    public void validateTrade_SuccessTest_NewTradeVerGreaterThanExistingTradeMaxId() throws TradeRejectedException {
        Trade newTrade = getTrade();
        newTrade.setVersion(3);
        tradeVersionValidator.validate(newTrade);

        Mockito.verify(tradingDao, Mockito.times(1)).getMaxVersionOfTrade(newTrade.getId());
    }

    @Test
    public void validateTrade_SuccessTest_NewTradeVerEqualsExistingTradeMaxId() throws TradeRejectedException {
        Trade newTrade = getTrade();
        newTrade.setVersion(2);
        tradeVersionValidator.validate(newTrade);

        Mockito.verify(tradingDao, Mockito.times(1)).getMaxVersionOfTrade(newTrade.getId());
    }


    @Test
    public void validateTrade_FailureTest_NewTradeVerLesserThanExistingTradeMaxId() throws TradeRejectedException {
        Trade newTrade = getTrade();
        newTrade.setId("T1"); newTrade.setVersion(1);
        String formattedExceptionMsg = MessageFormat.format(TRADE_VERSION_VALIDATION_MSG, newTrade.getId(), newTrade.getVersion());
        tradeRejectedExceptionVersionNo = new TradeRejectedException(formattedExceptionMsg);
        thrown.expect(TradeRejectedException.class);
        thrown.expectMessage(formattedExceptionMsg);

        tradeVersionValidator.validate(newTrade);

        Mockito.verify(tradingDao, Mockito.times(1)).getMaxVersionOfTrade(newTrade.getId());
    }

    public static Trade getTradeMaxVersion() {
        Trade trade = new Trade();
        trade.setId("T1");
        trade.setVersion(2);
        trade.setCounterPartyId("CP-1");
        trade.setBookId("B1");
        trade.setMaturityDate(LocalDate.of(2020,6, 3));
        trade.setCreatedDate(LocalDate.now());
        trade.setExpired(false);
        return trade;
    }

}
