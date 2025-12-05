package com.qst.crop.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 第三方API连接工具类
 * 用于统一处理第三方服务的HTTP请求
 */
// 注意：工具类若使用@Value注入，需加上@Component注解并确保Spring能扫描到
@Component
public class AddressValidationUtil {
    // 移除static修饰符，@Value无法直接注入静态变量
    @Value("${thirdparty.address.api-url}")
    private static String validationApiUrl;

    @Value("${thirdparty.address.api-key}")
    private static String apiKey;

    @Value("${thirdparty.connect-timeout:5000}") // 设置默认值
    private static int connectTimeout;

    @Value("${thirdparty.read-timeout:10000}") // 设置默认值
    private static int readTimeout;
    /**
     * 校验地址有效性（修改为静态方法）
     * @param addressDetail 详细地址
     * @return 校验结果（true=有效，false=无效）
     */

    // 1. 保留原校验方法（调整为非静态，因使用成员变量注入）
    public static boolean validateAddress(String addressDetail) { // 添加 static 关键字
        if (addressDetail == null || addressDetail.trim().isEmpty()) {
            return false;
        }

        try {
            // 构建带超时设置的RestTemplate
            RestTemplate restTemplate = new RestTemplate();
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout(connectTimeout);
            requestFactory.setReadTimeout(readTimeout);
            restTemplate.setRequestFactory(requestFactory);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            JSONObject request = new JSONObject();
            request.put("address", addressDetail);
            HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);

            JSONObject response = restTemplate.postForObject(validationApiUrl, entity, JSONObject.class);
            return response != null && response.getBoolean("valid");
        } catch (Exception e) {
            e.printStackTrace();
            return true; // 接口异常时默认通过校验
        }
    }

    // 默认超时时间（毫秒）
    private static final int DEFAULT_CONNECT_TIMEOUT = 5000;
    private static final int DEFAULT_READ_TIMEOUT = 10000;

    // 构建带超时设置的RestTemplate（静态方法用）
    private static RestTemplate getRestTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
        requestFactory.setReadTimeout(DEFAULT_READ_TIMEOUT);
        return new RestTemplate(requestFactory);
    }

    /**
     * 发送POST请求（JSON格式）
     * @param url 第三方API地址
     * @param headers 请求头信息
     * @param requestParam 请求参数（对象或Map）
     * @param responseType 响应数据类型
     * @return 第三方API返回结果
     * @param <T> 响应数据泛型
     */
    public static <T> T postJson(String url, Map<String, String> headers, Object requestParam, Class<T> responseType) {
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
            ResponseEntity<T> response = getRestTemplate().exchange(
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

    /**
     * 发送GET请求
     * @param url 第三方API地址（含参数）
     * @param headers 请求头信息
     * @param responseType 响应数据类型
     * @return 第三方API返回结果
     * @param <T> 响应数据泛型
     */
    public static <T> T get(String url, Map<String, String> headers, Class<T> responseType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(httpHeaders::set);
        }

        HttpEntity<Void> requestEntity = new HttpEntity<>(httpHeaders);

        try {
            ResponseEntity<T> response = getRestTemplate().exchange(
                    url,
                    HttpMethod.GET,
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

    /**
     * 2. 重命名重复的方法（示例命名为checkAddressValidity）
     * 示例：调用地址校验API（复用之前的地址校验逻辑）
     * @param address 地址信息
     * @return 校验结果（true=有效）
     */
    public static boolean checkAddressValidity(String address) {
        boolean result;
        if (address == null || address.trim().isEmpty()) {
            result = false;
        } else {
            try {
                // 第三方地址校验API配置（建议放到配置文件中）
                String apiUrl = "https://api.example.com/address/validate";
                String apiKey = "your_actual_api_key";

                // 构建请求参数
                Map<String, String> params = Map.of("address", address);
                // 构建请求头（含认证信息）
                Map<String, String> headers = Map.of("Authorization", "Bearer " + apiKey);

                // 调用POST请求，响应为JSON对象
                AddressValidationResponse response = postJson(
                        apiUrl,
                        headers,
                        params,
                        AddressValidationResponse.class
                );

                result = response != null && response.isValid();
            } catch (Exception e) {
                e.printStackTrace();
                result = true; // 接口调用失败时的降级策略
            }
        }

        return result;
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