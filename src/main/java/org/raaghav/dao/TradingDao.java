package org.raaghav.dao;

import org.raaghav.beans.Trade;
import java.util.List;

public interface TradingDao {
    public Integer addTrade(Trade trade);
    // getMaxVersionOfTrade(String tradeId);
    public Trade getMaxVersionOfTrade(String tradeId);
    public Integer updateTradeExpiryStatus();
    //
    public List<Trade> getTrades();
}
