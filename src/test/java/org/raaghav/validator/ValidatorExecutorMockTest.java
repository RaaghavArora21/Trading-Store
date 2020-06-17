package org.raaghav.validator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.raaghav.beans.Trade;
import org.raaghav.helper.TradeRejectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.MessageFormat;

import static org.mockito.ArgumentMatchers.any;
import static org.raaghav.helper.TradingConstants.TRADE_MATURITY_DATE_VALIDATION_MSG;
import static org.raaghav.helper.TradingConstants.TRADE_VERSION_VALIDATION_MSG;
import static org.raaghav.util.TradeUtils.getTrade;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ValidatorExecutorMockTest {

    @Autowired
    ValidatorExecutor validatorExecutor;

    @MockBean
    TradeMaturityDateValidator tradeMaturityDateValidator;

    @MockBean
    TradeVersionValidator tradeVersionValidator;

    private TradeRejectedException tradeRejectedExceptionMaturityDate;

    private TradeRejectedException tradeRejectedExceptionVersionNo;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        tradeRejectedExceptionMaturityDate = new TradeRejectedException(TRADE_MATURITY_DATE_VALIDATION_MSG);
    }


    @Test
    public void validateTrade_SuccessTest() throws TradeRejectedException {
        Mockito.doNothing().when(tradeMaturityDateValidator).validate(any(Trade.class));
        Mockito.doNothing().when(tradeVersionValidator).validate(any(Trade.class));

        Trade trade = getTrade();
        validatorExecutor.validateTrade(trade);

        Mockito.verify(tradeMaturityDateValidator, Mockito.times(1)).validate(trade);
        Mockito.verify(tradeVersionValidator, Mockito.times(1)).validate(trade);
    }

    @Test
    public void validateTrade_FailureTest_TradeMaturityDateValidationTest() throws TradeRejectedException {
        Mockito.doThrow(tradeRejectedExceptionMaturityDate).when(tradeMaturityDateValidator).validate(any(Trade.class));
        Mockito.doNothing().when(tradeVersionValidator).validate(any(Trade.class));
        thrown.expect(TradeRejectedException.class);
        thrown.expectMessage(TRADE_MATURITY_DATE_VALIDATION_MSG);

        Trade trade = getTrade();
        validatorExecutor.validateTrade(trade);
    }

    @Test
    public void validateTrade_FailureTest_TradeVersionValidationTest() throws TradeRejectedException {
        Mockito.doNothing().when(tradeMaturityDateValidator).validate(any(Trade.class));
        Trade newTrade = getTrade();
        newTrade.setId("T1"); newTrade.setVersion(1);
        String formattedExceptionMsg = MessageFormat.format(TRADE_VERSION_VALIDATION_MSG, newTrade.getId(), newTrade.getVersion());
        tradeRejectedExceptionVersionNo = new TradeRejectedException(formattedExceptionMsg);
        Mockito.doThrow(tradeRejectedExceptionVersionNo).when(tradeVersionValidator).validate(any(Trade.class));
        thrown.expect(TradeRejectedException.class);
        thrown.expectMessage(formattedExceptionMsg);

        Trade trade = getTrade();
        validatorExecutor.validateTrade(trade);
    }

    @Test
    public void validateTrade_FailureTest_TradeVersionValidationTest_TradeMaturityDateValidationTest() throws TradeRejectedException {
        Mockito.doThrow(tradeRejectedExceptionMaturityDate).when(tradeMaturityDateValidator).validate(any(Trade.class));
        Mockito.doThrow(new TradeRejectedException(TRADE_VERSION_VALIDATION_MSG)).when(tradeVersionValidator).validate(any(Trade.class));
        thrown.expect(TradeRejectedException.class);
        thrown.expectMessage(TRADE_MATURITY_DATE_VALIDATION_MSG);

        Trade trade = getTrade();
        validatorExecutor.validateTrade(trade);
    }
}
