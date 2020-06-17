package org.raaghav.service;

import org.raaghav.beans.Trade;
import org.raaghav.helper.TradeRejectedException;

import java.util.List;


public interface TradingService {

    public Integer acceptTrade(Trade trade) throws TradeRejectedException;

    public List<Trade> getTrades();
}
