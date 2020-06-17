package org.raaghav.validator;

import org.raaghav.dao.TradingDao;
import org.raaghav.helper.TradingConstants;
import org.raaghav.beans.Trade;
import org.raaghav.helper.TradeRejectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Objects;

@Component
public class TradeVersionValidator implements Validator {

    @Autowired
    TradingDao tradingDao;

    public void validate(Trade newTrade) throws TradeRejectedException {

        Trade existingTrade = tradingDao.getMaxVersionOfTrade(newTrade.getId());

        if (Objects.nonNull(existingTrade)) {
            if (existingTrade.getVersion() > newTrade.getVersion()) {
                throw new TradeRejectedException(MessageFormat.format(TradingConstants.TRADE_VERSION_VALIDATION_MSG,
                        newTrade.getId(), newTrade.getVersion()));
            }

        }
    }
}