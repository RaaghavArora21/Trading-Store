package org.raaghav.util;

import org.raaghav.beans.Trade;

import java.time.LocalDate;

public class TradeUtils {
    public static Trade getTrade() {
        Trade trade = new Trade();
        trade.setId("T1");
        trade.setVersion(1);
        trade.setCounterPartyId("CP-1");
        trade.setBookId("B1");
        trade.setMaturityDate(LocalDate.of(2020,6, 3));
        trade.setCreatedDate(LocalDate.now());
        trade.setExpired(false);
        return trade;
    }
}
