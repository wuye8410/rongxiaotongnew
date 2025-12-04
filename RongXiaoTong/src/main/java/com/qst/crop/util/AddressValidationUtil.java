package com.qst.crop.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 * 地址有效性校验工具类（对接第三方API）
 */
public class AddressValidationUtil {

    // 第三方地址校验API地址（示例，需替换为实际接口）
    private static final String VALIDATION_API_URL = "https://api.example.com/address/validate";
    // 第三方API密钥（示例）
    private static final String API_KEY = "your_api_key";

    /**
     * 校验地址有效性
     * @param addressDetail 详细地址
     * @return 校验结果（true=有效，false=无效）
     */
    public static boolean validateAddress(String addressDetail) {
        if (addressDetail == null || addressDetail.trim().isEmpty()) {
            return false;
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + API_KEY);

            // 构建请求参数（根据第三方API要求调整）
            JSONObject request = new JSONObject();
            request.put("address", addressDetail);
            HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);

            // 调用第三方API
            JSONObject response = restTemplate.postForObject(VALIDATION_API_URL, entity, JSONObject.class);

            // 根据API返回结果判断（示例逻辑，需根据实际响应调整）
            return response != null && response.getBoolean("valid");
        } catch (Exception e) {
            // API调用失败时默认返回true（避免因接口问题阻塞用户）
            e.printStackTrace();
            return true;
        }
    }
}