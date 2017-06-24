package com.lailife.service;

import com.lailife.service.util.CheckSumBuilder;
import com.lailife.service.util.Netease;
import com.lailife.service.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * Created by SterOtto on 2017/4/25.
 */
@Service
public class SmsService {
    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    private static final String APP_KEY = "f90a15f2d0c7ed0b87749df472a05a08";
    private static final String APP_SECRET = "c89fa5bee0ce";
    private static final String URL_SEND_CODE = "https://api.netease.im/sms/sendcode.action";
    private static final String URL_CHECK_CODE = "https://api.netease.im/sms/verifycode.action";

    @Autowired
    RestTemplate restTemplate;

    public Optional<Netease> sendCode(String phone){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("mobile", phone);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, this.buildHeader());
        ResponseEntity<Netease> responseEntity =  restTemplate.exchange(URL_SEND_CODE, HttpMethod.POST, entity, Netease.class);
        if(responseEntity.getStatusCode() == HttpStatus.OK){
            return Optional.of(responseEntity.getBody());
        }else {
            logger.error("failed to send code, got a response with code {}", responseEntity.getStatusCode());
        }
        return null;
    }
    public Optional<Netease> checkCode(String phone, String code){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("mobile", phone);
        params.add("code", code);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, this.buildHeader());
        ResponseEntity<Netease> responseEntity =  restTemplate.exchange(URL_CHECK_CODE, HttpMethod.POST, entity, Netease.class);
        if(responseEntity.getStatusCode() == HttpStatus.OK){
            return Optional.of(responseEntity.getBody());
        }else {
            logger.error("failed to send code, got a response with code {}", responseEntity.getStatusCode());
        }
        return null;
    }

    private static HttpHeaders buildHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("AppKey", APP_KEY);
        String curTimestamp = String.valueOf(System.currentTimeMillis());
        headers.add("CurTime", curTimestamp);
        String nonce = RandomUtil.generateNonce();
        headers.add("Nonce", nonce);
        headers.add("CheckSum", CheckSumBuilder.getCheckSum(APP_SECRET, nonce, curTimestamp));
        MediaType mediaType = MediaType.parseMediaType("application/x-www-form-urlencoded;charset=utf-8");
        headers.setContentType(mediaType);
        return headers;
    }
}
