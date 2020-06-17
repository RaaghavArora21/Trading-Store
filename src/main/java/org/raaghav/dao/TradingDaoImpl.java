package org.raaghav.dao;

import org.raaghav.beans.Trade;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TradingDaoImpl implements TradingDao {
    // We are using for this exercise, a Map for Trade Storage
    private Map<String, SortedMap<Integer, Trade>> tradingStore = new ConcurrentHashMap<>();

    @Override
    public Integer addTrade(Trade trade) {
        SortedMap<Integer, Trade> verWiseTradesMap = tradingStore.get(trade.getId());
        if (CollectionUtils.isEmpty(verWiseTradesMap)) {
            verWiseTradesMap = new TreeMap<>();
            tradingStore.put(trade.getId(), verWiseTradesMap);
        }
        verWiseTradesMap.put(trade.getVersion(), trade);
        return 1; //returning the no Of rows affected (mocking the behaviour here for exercise sake).
    }

    @Override
    public Trade getMaxVersionOfTrade(String tradeId) {
        SortedMap<Integer, Trade> verWiseTradesMap = tradingStore.get(tradeId);
        if (CollectionUtils.isEmpty(verWiseTradesMap)) {
            return null;
        } else {
            // return Max Version Trade of the tradeId
            return verWiseTradesMap.get(verWiseTradesMap.lastKey());
        }
    }

    @Override
    public Integer updateTradeExpiryStatus() {
        //total rows affected
        AtomicInteger rowsAffected = new AtomicInteger(0);
        tradingStore.forEach( (tradeId, verWiseTradesMap) -> {
            verWiseTradesMap.forEach( (tradeVer, trade) -> {
                if(trade.getMaturityDate().isBefore(LocalDate.now())) {
                    trade.setExpired(true);
                    rowsAffected.getAndIncrement();
                }
            });
        });
        return rowsAffected.get();
    }

    @Override
    public List<Trade> getTrades() {
        List<Trade> tradeList = new ArrayList<>();
        tradingStore.forEach( (tradeId, verWiseTradesMap) -> {
            verWiseTradesMap.forEach( (tradeVer, trade) -> {
               tradeList.add(trade);
            });
        });
        return tradeList;
    }
}
