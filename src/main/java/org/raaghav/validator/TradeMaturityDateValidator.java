package org.raaghav.validator;

import org.raaghav.helper.TradingConstants;
import org.raaghav.beans.Trade;
import org.raaghav.helper.TradeRejectedException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TradeMaturityDateValidator implements Validator {
    @Override
    public void validate(Trade trade) throws TradeRejectedException {
        LocalDate todaysDate = LocalDate.now();
        if (trade.getMaturityDate().isBefore(todaysDate)) {
            throw new TradeRejectedException(TradingConstants.TRADE_MATURITY_DATE_VALIDATION_MSG);
        }
    }

}
