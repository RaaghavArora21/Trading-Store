package org.raaghav.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.raaghav.dao.TradingDao;
import org.raaghav.helper.TradingConstants;
import org.raaghav.service.TradingService;
import org.raaghav.beans.Trade;
import org.raaghav.helper.TradeRejectedException;
import org.raaghav.helper.TradingScheduler;
import org.raaghav.validator.ValidatorExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.MessageFormat;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.raaghav.helper.TradingConstants.TRADE_MATURITY_DATE_VALIDATION_MSG;
import static org.raaghav.helper.TradingConstants.TRADE_VERSION_VALIDATION_MSG;
import static org.raaghav.util.TradeUtils.getTrade;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TradingServiceMockTest {

    @Autowired
    TradingService tradingService;

    @MockBean
    TradingDao tradingDao;

    @MockBean
    ValidatorExecutor validatorExecutor;

    private TradeRejectedException tradeRejectedExceptionMaturityDate;

    private TradeRejectedException tradeRejectedExceptionVersionNo;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        tradeRejectedExceptionMaturityDate = new TradeRejectedException(TRADE_MATURITY_DATE_VALIDATION_MSG);
        Mockito.doReturn(1).when(tradingDao).addTrade(any(Trade.class));
    }

    @Test
    public void acceptTrade_SuccessTest() throws TradeRejectedException {
        Mockito.doNothing().when(validatorExecutor).validateTrade(any(Trade.class));

        Trade newTrade = getTrade();
        Integer tradesAdded = tradingService.acceptTrade(newTrade);
        assert(tradesAdded.equals(1));

        Mockito.verify(tradingDao, Mockito.times(1)).addTrade(newTrade);
        Mockito.verify(validatorExecutor, Mockito.times(1)).validateTrade(newTrade);
    }

    @Test
    public void acceptTradeSuccess_MaturityDateValidationTest_MaturityDateEqualsTodaysDate() throws TradeRejectedException {
        Mockito.doNothing().when(validatorExecutor).validateTrade(any(Trade.class));

        //Test where Maturity Date is same as Today's Date
        Trade newTrade = getTrade();
        newTrade.setMaturityDate(LocalDate.now());

        Integer tradesAdded = tradingService.acceptTrade(newTrade);
        assertEquals(tradesAdded, new Integer(1));

        Mockito.verify(tradingDao, Mockito.times(1)).addTrade(newTrade);
        Mockito.verify(validatorExecutor, Mockito.times(1)).validateTrade(newTrade);
    }

    @Test
    public void acceptTradeSuccess_MaturityDateValidationTest_MaturityDateGreaterThanTodaysDate() throws TradeRejectedException {
        Mockito.doNothing().when(validatorExecutor).validateTrade(any(Trade.class));

        // Test where Maturity Date is 1 day after Today's Date
        Trade newTrade = getTrade();
        newTrade.setMaturityDate(LocalDate.now().plusDays(1));

        Integer tradesAdded = tradingService.acceptTrade(newTrade);
        assertEquals(tradesAdded, new Integer(1));

        Mockito.verify(tradingDao, Mockito.times(1)).addTrade(newTrade);
        Mockito.verify(validatorExecutor, Mockito.times(1)).validateTrade(newTrade);
    }

    @Test
    public void acceptTradeFailure_MaturityDateValidationTest_MaturityDateLessThanTodaysDate() throws TradeRejectedException {
        Mockito.doThrow(tradeRejectedExceptionMaturityDate).when(validatorExecutor).validateTrade(any(Trade.class));
        thrown.expect(TradeRejectedException.class);
        thrown.expectMessage(TRADE_MATURITY_DATE_VALIDATION_MSG);

        // Test when MaturityDate is less than Today's Date
        Trade trade = getTrade();
        trade.setMaturityDate(LocalDate.now().minusDays(2));

        Integer tradesAdded = tradingService.acceptTrade(trade);
    }

    @Test
    public void acceptTradeFailure_TradeVersionValidationTest_TradeVersionSmallerThanExistingTradeMaxVersion() throws TradeRejectedException{
        Trade newTrade = getTrade();
        newTrade.setId("T1"); newTrade.setVersion(1);
        String formattedExceptionMsg = MessageFormat.format(TRADE_VERSION_VALIDATION_MSG, newTrade.getId(), newTrade.getVersion());

        tradeRejectedExceptionVersionNo = new TradeRejectedException(formattedExceptionMsg);
        Mockito.doThrow(tradeRejectedExceptionVersionNo).when(validatorExecutor).validateTrade(any(Trade.class));
        thrown.expect(TradeRejectedException.class);
        thrown.expectMessage(formattedExceptionMsg);

        // Test where Trade Version is smaller than existing version
        Trade existingTrade = getTrade();
        existingTrade.setId("T1"); existingTrade.setVersion(2);
        Mockito.doReturn(existingTrade).when(tradingDao).getMaxVersionOfTrade(anyString());

        Integer tradesAdded = tradingService.acceptTrade(newTrade);
    }

    @Test
    public void acceptTradeSuccess_TradeVersionValidationTest_TradeVersionGreaterThanExistingTradeMaxVersion() throws TradeRejectedException{
        Mockito.doNothing().when(validatorExecutor).validateTrade(any(Trade.class));

        // Test where Trade Version is greater than existing version
        Trade existingTrade = getTrade();
        existingTrade.setId("T1"); existingTrade.setVersion(1);
        Mockito.doReturn(existingTrade).when(tradingDao).getMaxVersionOfTrade(anyString());

        Trade newTrade = getTrade();
        newTrade.setId("T1"); newTrade.setVersion(2);

        Integer tradesAdded = tradingService.acceptTrade(newTrade);
        assertEquals(tradesAdded.intValue(), 1);

        Mockito.verify(tradingDao, Mockito.times(1)).addTrade(newTrade);
        Mockito.verify(validatorExecutor, Mockito.times(1)).validateTrade(newTrade);
    }


    @Test
    public void acceptTradeSuccess_TradeVersionValidationTest_TradeVersionSameAsExistingTradeMaxVersion() throws TradeRejectedException{
        Mockito.doNothing().when(validatorExecutor).validateTrade(any(Trade.class));

        // Test where Trade Version is same as existing trade max version
        Trade existingTrade = getTrade();
        existingTrade.setId("T1"); existingTrade.setVersion(2);
        Mockito.doReturn(existingTrade).when(tradingDao).getMaxVersionOfTrade(anyString());

        Trade newTrade = getTrade();
        newTrade.setId("T1"); newTrade.setVersion(2);
        Integer tradesAdded = tradingService.acceptTrade(newTrade);

        Mockito.verify(tradingDao, Mockito.times(1)).addTrade(newTrade);
        Mockito.verify(validatorExecutor, Mockito.times(1)).validateTrade(newTrade);
    }

}
