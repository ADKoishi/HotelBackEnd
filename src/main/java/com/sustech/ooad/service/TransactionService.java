package com.sustech.ooad.service;

import java.util.Map;

public interface TransactionService {
    void orderPurchase(Map<String, String> requestInfo, Map<String, Object> orderPurchaseResult);
}
