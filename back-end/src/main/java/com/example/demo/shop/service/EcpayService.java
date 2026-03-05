package com.example.demo.shop.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

@Service
public class EcpayService {

    private static final String MERCHANT_ID = "3002607";
    private static final String HASH_KEY = "pwFHCqoQZGmho4w6";
    private static final String HASH_IV = "EkRm7uy3DiqpYwyQ";
    private static final String ECPAY_URL = "https://payment-stage.ecpay.com.tw/Cashier/AioCheckOut/V5";
    private static final String NGROK_URL = "https://shily-untusked-yuri.ngrok-free.dev";

    public String generatePaymentForm(Integer orderId, int totalAmount, String itemName) {
        String merchantTradeNo = "ORD" + orderId + System.currentTimeMillis() % 10000;
        String merchantTradeDate = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        TreeMap<String, String> params = new TreeMap<>();
        params.put("MerchantID", MERCHANT_ID);
        params.put("MerchantTradeNo", merchantTradeNo);
        params.put("MerchantTradeDate", merchantTradeDate);
        params.put("PaymentType", "aio");
        params.put("TotalAmount", String.valueOf(totalAmount));
        params.put("TradeDesc", "購物訂單");
        params.put("ItemName", itemName);
        params.put("ReturnURL", NGROK_URL + "/api/ecpay/return");
        params.put("ClientBackURL", "http://localhost:5173/shopStore");
        params.put("OrderResultURL", "http://localhost:5173/shopStore");
        params.put("ChoosePayment", "ALL");
        params.put("EncryptType", "1");

        String checkMac = generateCheckMac(params);
        params.put("CheckMacValue", checkMac);

        // 產生 HTML form
        StringBuilder form = new StringBuilder();
        form.append("<form id='ecpayForm' method='post' action='").append(ECPAY_URL).append("'>");
        for (var entry : params.entrySet()) {
            form.append("<input type='hidden' name='").append(entry.getKey())
                .append("' value='").append(entry.getValue()).append("'/>");
        }
        form.append("</form>");
        form.append("<script>document.getElementById('ecpayForm').submit();</script>");

        return form.toString();
    }

    private String generateCheckMac(TreeMap<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("HashKey=").append(HASH_KEY);
        for (var entry : params.entrySet()) {
            sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }
        sb.append("&HashIV=").append(HASH_IV);

        String encoded = urlEncodeToLower(sb.toString());
        return DigestUtils.sha256Hex(encoded).toUpperCase();
    }

    private String urlEncodeToLower(String str) {
        try {
            return URLEncoder.encode(str, StandardCharsets.UTF_8)
                    .toLowerCase()
                    .replace("%2b", "+")
                    .replace("%20", "+");
        } catch (Exception e) {
            throw new RuntimeException("URL encode 失敗", e);
        }
    }
}