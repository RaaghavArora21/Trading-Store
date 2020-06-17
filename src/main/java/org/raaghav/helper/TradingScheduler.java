package org.raaghav.helper;

import org.raaghav.dao.TradingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TradingScheduler {
    @Autowired
    TradingDao tradingDao;

    @Scheduled(cron="0 1 0 * * ?")
    public Integer updateTradeExpiryStatus() {
        return tradingDao.updateTradeExpiryStatus();
    }

}
