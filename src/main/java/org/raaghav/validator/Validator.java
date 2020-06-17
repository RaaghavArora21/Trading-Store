package org.raaghav.validator;

import org.raaghav.beans.Trade;
import org.raaghav.helper.TradeRejectedException;

public interface Validator {
    public void validate(Trade trade) throws TradeRejectedException;
}
