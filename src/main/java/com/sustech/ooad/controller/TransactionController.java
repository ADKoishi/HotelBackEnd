package com.sustech.ooad.controller;

import com.sustech.ooad.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TransactionController {
    @Autowired
    TransactionService transactionService;
    @PostMapping("/purchase")
    public Map<String, Object> orderPurchase(@RequestBody Map<String, String> requestInfo){
        Map<String, Object> orderPurchaseResult = new HashMap<>();
        transactionService.orderPurchase(requestInfo, orderPurchaseResult);
        return orderPurchaseResult;
    }
}
