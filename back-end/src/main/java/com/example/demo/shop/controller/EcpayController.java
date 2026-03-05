package com.example.demo.shop.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.shop.entity.Orders;
import com.example.demo.shop.repository.OrdersRepository;
import com.example.demo.shop.service.EcpayService;

@RestController
@RequestMapping("/api/ecpay")
@CrossOrigin(origins = "http://localhost:5173")
public class EcpayController {

    @Autowired
    private EcpayService ecpayService;

    @Autowired
    private OrdersRepository ordersRepository;

    // 產生付款表單
    @GetMapping("/pay/{orderId}")
    public ResponseEntity<String> pay(@PathVariable Integer orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("找不到訂單"));

        // 取得訂單商品名稱（這裡簡化用訂單ID）
        String itemName = "訂單 ORD-" + orderId;
        int totalAmount = order.getTotalPrice().intValue();

        String form = ecpayService.generatePaymentForm(orderId, totalAmount, itemName);
        return ResponseEntity.ok(form);
    }

    // 綠界付款完成後回傳（ReturnURL）
    @PostMapping("/return")
    public ResponseEntity<String> returnUrl(@RequestParam Map<String, String> params) {
        String rtnCode = params.get("RtnCode");
        String merchantTradeNo = params.get("MerchantTradeNo");

        if ("1".equals(rtnCode)) {
            // 付款成功，更新訂單狀態
            // 從 merchantTradeNo 取出 orderId（ORD{orderId}xxxx）
            // 這裡先簡單 log，後續可以更新資料庫
            System.out.println("付款成功：" + merchantTradeNo);
        }

        return ResponseEntity.ok("1|OK"); // 綠界要求回傳 1|OK
    }
}