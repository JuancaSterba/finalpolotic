package com.polotic.taskmanager.service.impl;

import com.polotic.taskmanager.dto.response.RecaptchaResponse;
import com.polotic.taskmanager.service.RecaptchaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class RecaptchaServiceImpl implements RecaptchaService {

    private static final String GOOGLE_RECAPTCHA_ENDPOINT = "https://www.google.com/recaptcha/api/siteverify";

    @Value("${recaptcha.secretkey}")
    private String secretKey;

    @Override
    public boolean isValid(String captcha) {

        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("secret", secretKey);
        request.add("response", captcha);

        RecaptchaResponse response = restTemplate.postForObject(GOOGLE_RECAPTCHA_ENDPOINT, request, RecaptchaResponse.class);

        if (response == null) {
            return false;
        }

        return Boolean.TRUE.equals(response.isSuccess());

    }
}
