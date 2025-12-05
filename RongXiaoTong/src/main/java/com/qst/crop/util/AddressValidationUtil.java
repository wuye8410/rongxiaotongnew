package com.qst.crop.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

// 修改1：使用 jakarta.annotation 替代 javax.annotation
import jakarta.annotation.PostConstruct;
import java.util.Map;

/**
 * 第三方API连接工具类
 * 用于统一处理第三方服务的HTTP请求
 */
@Component
public class AddressValidationUtil {

    // 从配置文件中读取
    @Value("${thirdparty.address.api-url:https://api.example.com/address/validate}")
    private String validationApiUrl;

    @Value("${thirdparty.address.api-key:your_actual_api_key}")
    private String apiKey;

    @Value("${thirdparty.connect-timeout:5000}")
    private int connectTimeout;

    @Value("${thirdparty.read-timeout:10000}")
    private int readTimeout;

    @Value("${thirdparty.address.enabled:true}")
    private boolean validationEnabled;

    @Value("${thirdparty.address.fallback-on-failure:true}")
    private boolean fallbackOnFailure;

    private RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        // 初始化RestTemplate
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);
        this.restTemplate = new RestTemplate(requestFactory);
    }

    /**
     * 校验地址有效性
     * @param addressDetail 详细地址
     * @return 校验结果（true=有效，false=无效）
     */
    public boolean validateAddress(String addressDetail) {
        // 如果未启用地址校验，直接返回true
        if (!validationEnabled) {
            return true;
        }

        if (!StringUtils.hasText(addressDetail)) {
            return false;
        }

        // 基本格式校验：长度、字符等
        if (addressDetail.length() < 5 || addressDetail.length() > 100) {
            return false;
        }

        // 简单格式校验：至少包含中文或数字
        if (!addressDetail.matches(".*[\\u4e00-\\u9fa5\\d]+.*")) {
            return false;
        }

        // 如果配置了第三方API，则调用API校验
        if (StringUtils.hasText(validationApiUrl) && StringUtils.hasText(apiKey) &&
                !validationApiUrl.equals("https://api.example.com/address/validate")) {
            try {
                return validateAddressWithThirdPartyAPI(addressDetail);
            } catch (Exception e) {
                // 记录日志
                System.err.println("第三方地址校验API调用失败: " + e.getMessage());
                // 根据配置决定降级策略
                return fallbackOnFailure;
            }
        }

        // 如果没有配置第三方API，只进行基本校验
        return true;
    }

    /**
     * 调用第三方API校验地址
     */
    private boolean validateAddressWithThirdPartyAPI(String addressDetail) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            JSONObject request = new JSONObject();
            request.put("address", addressDetail);
            HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);

            JSONObject response = restTemplate.postForObject(validationApiUrl, entity, JSONObject.class);
            return response != null && Boolean.TRUE.equals(response.getBoolean("valid"));
        } catch (Exception e) {
            throw new RuntimeException("地址校验API调用失败", e);
        }
    }

    /**
     * 发送POST请求（JSON格式）的静态方法
     */
    public static <T> T postJson(String url, Map<String, String> headers, Object requestParam, Class<T> responseType) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(10000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(httpHeaders::set);
        }

        HttpEntity<String> requestEntity = new HttpEntity<>(
                JSON.toJSONString(requestParam),
                httpHeaders
        );

        try {
            ResponseEntity<T> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    responseType
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RuntimeException("第三方API请求失败，状态码：" + response.getStatusCodeValue());
            }
        } catch (RestClientException e) {
            throw new RuntimeException("第三方API调用异常：" + e.getMessage(), e);
        }
    }

    // 地址校验响应实体类
    public static class AddressValidationResponse {
        private boolean valid;
        private String message;

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}