package org.raaghav.validator;

import org.raaghav.beans.Trade;
import org.raaghav.helper.TradeRejectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidatorExecutor {

    @Autowired
    TradeMaturityDateValidator tradeMaturityDateValidator;

    @Autowired
    TradeVersionValidator tradeVersionValidator;



    public void validateTrade(Trade trade) throws TradeRejectedException {
        tradeMaturityDateValidator.validate(trade);
        tradeVersionValidator.validate(trade);

    }
}
