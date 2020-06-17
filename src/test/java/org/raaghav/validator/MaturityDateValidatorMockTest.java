package org.raaghav.validator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.raaghav.beans.Trade;
import org.raaghav.dao.TradingDao;
import org.raaghav.helper.TradeRejectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.raaghav.helper.TradingConstants.TRADE_MATURITY_DATE_VALIDATION_MSG;
import static org.raaghav.helper.TradingConstants.TRADE_VERSION_VALIDATION_MSG;
import static org.raaghav.util.TradeUtils.getTrade;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MaturityDateValidatorMockTest {

    @Autowired
    TradeMaturityDateValidator tradeMaturityDateValidator;

    private TradeRejectedException tradeRejectedExceptionMaturityDate;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        tradeRejectedExceptionMaturityDate = new TradeRejectedException(TRADE_MATURITY_DATE_VALIDATION_MSG);
    }


    @Test
    public void validate_SuccessTest_TradeMaturityDateEqualsTodaysDate() throws TradeRejectedException {
        Trade trade = getTrade();
        trade.setMaturityDate(LocalDate.now());
        tradeMaturityDateValidator.validate(trade);
    }

    @Test
    public void validate_SuccessTest_TradeMaturityDateGreaterThanTodaysDate() throws TradeRejectedException {
        Trade trade = getTrade();
        trade.setMaturityDate(LocalDate.now().plusDays(1));
        tradeMaturityDateValidator.validate(trade);
    }

    @Test
    public void validate_FailureTest_TradeMaturityDateLesserThanTodaysDate() throws TradeRejectedException {
        thrown.expect(TradeRejectedException.class);
        thrown.expectMessage(TRADE_MATURITY_DATE_VALIDATION_MSG);

        Trade trade = getTrade();
        tradeMaturityDateValidator.validate(trade);
    }
}
