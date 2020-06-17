package org.raaghav.controller;

import org.raaghav.beans.Trade;
import org.raaghav.helper.TradeRejectedException;
import org.raaghav.service.TradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TradingController {

    @Autowired
    TradingService tradingService;


    @GetMapping(path="/trades")
    public ResponseEntity<List<Trade>> getTrades() {
        return ResponseEntity.ok(tradingService.getTrades());
    }

    @PostMapping(path="/trades", consumes = "application/json")
    public ResponseEntity postTrade(@RequestBody Trade trade) throws TradeRejectedException {
        tradingService.acceptTrade(trade);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
