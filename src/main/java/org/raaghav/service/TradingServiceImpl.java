package org.raaghav.service;

import org.raaghav.dao.TradingDao;
import org.raaghav.beans.Trade;
import org.raaghav.helper.TradeRejectedException;
import org.raaghav.validator.ValidatorExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TradingServiceImpl implements TradingService {

    @Autowired
    ValidatorExecutor validatorExecutor;

    @Autowired
    TradingDao tradingDao;

    @Override
    public Integer acceptTrade(Trade trade) throws TradeRejectedException {
        validatorExecutor.validateTrade(trade);
        return tradingDao.addTrade(trade);
    }

    @Override
    public List<Trade> getTrades() {
        return tradingDao.getTrades();
    }
}
