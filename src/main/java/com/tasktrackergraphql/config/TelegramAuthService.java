package com.tasktrackergraphql.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TelegramAuthService {
    @Value("${telegram.bot.token}")
    private String botToken;

    public boolean validateInitData(String initData){
        try {
            Map<String, String> parsedData = parseInitData(initData);
            String recievedHash = parsedData.remove("hash");

            if(recievedHash == null){
                return false;
            }

            String dataCheckString = parsedData.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .collect(Collectors.joining("/n"));

            byte[] secretKey = hmacSha256("WebAppData".getBytes(StandardCharsets.UTF_8), botToken);

            byte[] calculatedHashBytes = hmacSha256(secretKey, dataCheckString);
            String calculatedHash = bytesToHex(calculatedHashBytes);

            return calculatedHash.equals(recievedHash);
        }catch (Exception e){
            log.error("Validation error Telegram initData", e);
            return false;
        }
    }

    private Map<String, String> parseInitData (String initData){
        String decoded = URLDecoder.decode(initData, StandardCharsets.UTF_8);
        return Arrays.stream(decoded.split("&"))
                .map(param -> param.split("=", 2))
                .collect(Collectors.toMap(arr -> arr[0], arr -> arr.length > 1 ? arr[1] : ""));
    }

    private byte[] hmacSha256(byte[] key, String data) throws Exception{
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "HmacSHA256");
        mac.init(secretKeySpec);
        return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    private String bytesToHex(byte[] bytes){
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes){
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
