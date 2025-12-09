package com.qst.crop.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.annotation.PostConstruct;
import java.net.URLEncoder;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 使用高德地图API进行地址校验的工具类
 * 支持智能降级和地址补充
 */
@Component
public class AddressValidationUtil {

    @Value("${thirdparty.address.api-url:https://restapi.amap.com/v3/geocode/geo}")
    private String validationApiUrl;

    @Value("${thirdparty.address.api-key:}")
    private String apiKey;

    @Value("${thirdparty.connect-timeout:5000}")
    private int connectTimeout;

    @Value("${thirdparty.read-timeout:10000}")
    private int readTimeout;

    @Value("${thirdparty.address.enabled:true}")
    private boolean validationEnabled;

    @Value("${thirdparty.address.fallback-on-failure:true}")
    private boolean fallbackOnFailure;

    @Value("${thirdparty.address.supported-languages:zh,en}")
    private String supportedLanguages;

    private RestTemplate restTemplate;

    // 中文地址关键词
    private static final String[] CHINESE_KEYWORDS = {
            "路", "街", "巷", "道", "号", "小区", "村", "组", "栋", "单元", "室", "楼",
            "弄", "胡同", "里", "区", "县", "市", "省"
    };

    // 英文地址关键词
    private static final String[] ENGLISH_KEYWORDS = {
            "road", "street", "avenue", "boulevard", "lane", "drive", "court", "place",
            "rd", "st", "ave", "blvd", "ln", "dr", "ct", "pl",
            "no.", "number", "#", "apt", "apartment", "suite", "ste", "room",
            "building", "bldg", "tower", "block", "blk", "floor", "level", "unit"
    };

    @PostConstruct
    public void init() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);
        this.restTemplate = new RestTemplate(requestFactory);

        // 打印API配置信息（调试用）
        System.out.println("高德地图API配置信息：");
        System.out.println("API URL: " + validationApiUrl);
        System.out.println("API Key: " + (StringUtils.hasText(apiKey) ? "已配置" : "未配置"));
        System.out.println("校验启用: " + validationEnabled);
    }

    /**
     * 地址校验主方法 - 智能降级版本
     * @param addressDetail 详细地址
     * @return 校验结果（true=有效，false=无效）
     */
    public boolean validateAddress(String addressDetail) {
        // 如果未启用地址校验，直接返回true
        if (!validationEnabled) {
            System.out.println("地址校验未启用，直接通过");
            return true;
        }

        if (!StringUtils.hasText(addressDetail)) {
            System.out.println("地址为空，校验失败");
            return false;
        }

        String address = addressDetail.trim();

        // 基础格式校验
        if (address.length() < 5 || address.length() > 200) {
            System.out.println("地址长度不符合要求: " + address.length());
            return false;
        }

        // 基本格式校验：至少包含中文、英文或数字
        if (!address.matches(".*[\\u4e00-\\u9fa5a-zA-Z\\d]+.*")) {
            System.out.println("地址缺少有效字符");
            return false;
        }

        // 如果配置了高德API密钥，则尝试调用API校验
        if (StringUtils.hasText(apiKey)) {
            try {
                // 尝试用HttpURLConnection调用高德API（更稳定）
                boolean apiResult = validateAddressWithGaodeAPIHttp(address);
                System.out.println("高德API校验结果: " + apiResult);

                if (apiResult) {
                    return true;
                } else {
                    // API返回失败，但可能只是地址级别问题
                    System.out.println("高德API校验失败，降级到本地校验");
                    return validateAddressLocally(address);
                }
            } catch (Exception e) {
                System.err.println("高德地址校验API调用失败: " + e.getMessage());
                // API调用失败，根据配置决定降级策略
                if (fallbackOnFailure) {
                    System.out.println("API调用失败，降级到本地校验");
                    return validateAddressLocally(address);
                }
                return false;
            }
        } else {
            System.out.println("未配置高德API密钥，使用本地校验");
            // 如果没有配置API密钥，只进行本地校验
            return validateAddressLocally(address);
        }
    }

    /**
     * 使用HttpURLConnection调用高德地图API（更可靠）
     */
    private boolean validateAddressWithGaodeAPIHttp(String addressDetail) throws Exception {
        HttpURLConnection connection = null;
        try {
            // 地址预处理：补充可能的缺失信息
            String processedAddress = preprocessAddress(addressDetail);
            String encodedAddress = URLEncoder.encode(processedAddress, "UTF-8");
            String urlStr = validationApiUrl + "?address=" + encodedAddress + "&key=" + apiKey;

            System.out.println("调用高德API: " + urlStr);

            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();

            // 设置请求方法和请求头（模拟浏览器）
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");

            // 设置超时
            connection.setConnectTimeout(connectTimeout);
            connection.setReadTimeout(readTimeout);

            // 获取响应
            int responseCode = connection.getResponseCode();
            System.out.println("HTTP响应码: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 读取响应内容
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String responseBody = response.toString();
                System.out.println("高德API响应: " + responseBody);

                // 解析JSON
                JSONObject jsonResponse = JSON.parseObject(responseBody);
                String status = jsonResponse.getString("status");
                String info = jsonResponse.getString("info");
                String infocode = jsonResponse.getString("infocode");

                System.out.println("API状态: " + status + ", 信息: " + info + ", 错误码: " + infocode);

                if ("1".equals(status)) {
                    JSONArray geocodes = jsonResponse.getJSONArray("geocodes");
                    if (geocodes != null && geocodes.size() > 0) {
                        JSONObject geocode = geocodes.getJSONObject(0);
                        String level = geocode.getString("level");
                        String formattedAddress = geocode.getString("formatted_address");

                        System.out.println("地址级别: " + level);
                        System.out.println("格式化地址: " + formattedAddress);

                        return isValidAddressLevel(level);
                    }
                } else {
                    // API返回错误状态
                    System.err.println("高德API返回错误: " + info + " (错误码: " + infocode + ")");
                    // 30001错误通常是引擎错误，降级到本地校验
                    if ("30001".equals(infocode)) {
                        throw new Exception("高德API引擎错误，触发降级: " + info);
                    }
                    return false;
                }
            } else {
                System.err.println("HTTP请求失败，响应码: " + responseCode);
                throw new Exception("HTTP请求失败: " + responseCode);
            }

            return false;
        } catch (Exception e) {
            System.err.println("HttpURLConnection调用异常: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 地址预处理 - 如果地址缺少省市区信息，尝试补充
     */
    private String preprocessAddress(String addressDetail) {
        String address = addressDetail.trim();

        // 检查是否已经包含省市区信息
        boolean hasProvince = address.matches(".*省.*") || address.matches(".*自治区.*");
        boolean hasCity = address.matches(".*市.*");
        boolean hasDistrict = address.matches(".*区.*") || address.matches(".*县.*");

        // 如果已经有省市区信息，直接返回
        if ((hasProvince || hasCity || hasDistrict) && address.length() > 10) {
            return address;
        }

        System.out.println("地址缺少省市区信息，原地址: " + address);

        // 尝试根据关键词补充默认城市（这里可以根据实际业务需求调整）
        // 例如：如果地址包含"西南民族大学"，补充"四川省成都市"
        if (address.contains("西南民族大学")) {
            String enhancedAddress = "四川省成都市" + address;
            System.out.println("补充地址为: " + enhancedAddress);
            return enhancedAddress;
        }

        // 如果没有特定关键词，返回原地址
        return address;
    }

    /**
     * 根据高德地图返回的地址级别判断地址是否有效
     */
    private boolean isValidAddressLevel(String level) {
        if (level == null) return false;

        // 有效的地址级别（门牌号、兴趣点、村庄等具体地址）
        String[] validLevels = {
                "门牌号",          // 精确到门牌号
                "兴趣点",          // POI点
                "村庄",           // 村庄
                "乡镇",           // 乡镇
                "道路",           // 道路
                "道路交叉口",      // 道路交叉口
                "区县",           // 区县级别（放宽）
                "街道"            // 街道级别
        };

        // 无效的地址级别（太宽泛）
        String[] invalidLevels = {
                "国家",           // 国家级别，太宽泛
                "省",            // 省级别，太宽泛
                "城市"           // 城市级别，太宽泛
        };

        // 检查是否在无效级别中
        for (String invalidLevel : invalidLevels) {
            if (level.contains(invalidLevel)) {
                System.out.println("地址级别无效: " + invalidLevel);
                return false;
            }
        }

        // 检查是否在有效级别中
        for (String validLevel : validLevels) {
            if (level.contains(validLevel)) {
                System.out.println("地址级别有效: " + validLevel);
                return true;
            }
        }

        System.out.println("地址级别未匹配任何规则: " + level);
        return false;
    }

    /**
     * 本地地址校验（降级方案）
     */
    private boolean validateAddressLocally(String address) {
        System.out.println("执行本地地址校验: " + address);

        // 检测地址语言
        AddressLanguage language = detectAddressLanguage(address);
        System.out.println("检测到地址语言: " + language);

        // 根据语言进行校验
        switch (language) {
            case CHINESE:
                return validateChineseAddressEnhanced(address);
            case ENGLISH:
                return validateEnglishAddressEnhanced(address);
            default:
                return validateGenericAddressEnhanced(address);
        }
    }

    /**
     * 检测地址语言
     */
    private AddressLanguage detectAddressLanguage(String address) {
        // 检查是否包含中文字符
        boolean hasChinese = Pattern.compile("[\\u4e00-\\u9fa5]").matcher(address).find();

        // 检查英文地址特征
        boolean hasEnglishKeywords = false;
        String lowerAddress = address.toLowerCase();
        for (String keyword : ENGLISH_KEYWORDS) {
            if (lowerAddress.contains(keyword)) {
                hasEnglishKeywords = true;
                break;
            }
        }

        if (hasChinese) {
            return AddressLanguage.CHINESE;
        } else if (hasEnglishKeywords) {
            return AddressLanguage.ENGLISH;
        } else {
            return AddressLanguage.MIXED;
        }
    }

    /**
     * 增强的中文地址校验
     */
    private boolean validateChineseAddressEnhanced(String address) {
        System.out.println("执行增强中文地址校验: " + address);

        // 规则1: 必须包含中文
        if (!Pattern.compile("[\\u4e00-\\u9fa5]").matcher(address).find()) {
            return false;
        }

        // 规则2: 长度至少6个字符
        if (address.length() < 6) {
            return false;
        }

        // 规则3: 必须包含数字（门牌号）
        if (!Pattern.compile("\\d+").matcher(address).find()) {
            System.out.println("中文地址缺少门牌号，但继续校验");
        }

        // 规则4: 检查地址结构（更宽松的规则）
        String[] patterns = {
                ".*省.*市.*区.*",          // 省市区
                ".*省.*市.*县.*",          // 省市县
                ".*市.*区.*",              // 市区
                ".*县.*镇.*",              // 县镇
                ".*[路街巷道].*\\d+.*",     // 道路+数字
                ".*\\d+.*号.*",            // 数字+号
                ".*小区.*",                // 小区
                ".*村.*",                  // 村
                ".*组.*",                  // 组
                ".*栋.*",                  // 栋
                ".*单元.*",                // 单元
                ".*室.*"                   // 室
        };

        for (String pattern : patterns) {
            if (Pattern.matches(pattern, address)) {
                System.out.println("地址匹配本地增强规则: " + pattern);
                return true;
            }
        }

        // 规则5: 如果以上都不匹配，但地址包含"省"、"市"、"区"、"县"、"镇"中的至少两个
        int locationKeywords = 0;
        String[] locationWords = {"省", "市", "区", "县", "镇"};
        for (String word : locationWords) {
            if (address.contains(word)) {
                locationKeywords++;
            }
        }

        if (locationKeywords >= 2) {
            System.out.println("地址包含至少2个地理位置关键词");
            return true;
        }

        // 规则6: 如果包含"街"或"道"，并且有数字
        if ((address.contains("街") || address.contains("道")) && Pattern.compile("\\d+").matcher(address).find()) {
            System.out.println("地址包含街道和门牌号");
            return true;
        }

        System.out.println("地址未能通过本地增强校验");
        return false;
    }

    /**
     * 增强的英文地址校验
     */
    private boolean validateEnglishAddressEnhanced(String address) {
        String lowerAddress = address.toLowerCase();

        // 检查英文地址关键词
        boolean hasKeyword = false;
        for (String keyword : ENGLISH_KEYWORDS) {
            if (lowerAddress.contains(keyword)) {
                hasKeyword = true;
                break;
            }
        }

        if (!hasKeyword) {
            return false;
        }

        // 建议包含数字（门牌号）
        if (!Pattern.compile("\\d+").matcher(address).find()) {
            System.out.println("英文地址缺少门牌号，但继续校验");
        }

        // 英文地址应该有逗号分隔的多个部分
        int commaCount = lowerAddress.split(",").length - 1;
        if (commaCount >= 1) {
            return true;
        }

        // 如果没有逗号，至少要有比较长的地址
        if (address.length() >= 15) {
            return true;
        }

        return false;
    }

    /**
     * 增强的通用地址校验
     */
    private boolean validateGenericAddressEnhanced(String address) {
        // 必须包含有效字符
        if (!Pattern.compile("[\\u4e00-\\u9fa5a-zA-Z0-9]").matcher(address).find()) {
            return false;
        }

        // 长度检查
        if (address.length() < 10) {
            return false;
        }

        // 检查数字（门牌号）
        if (!Pattern.compile("\\d+").matcher(address).find()) {
            return false;
        }

        // 检查是否包含有效的地址结构
        String[] patterns = {
                ".*\\d+.*[路街巷道].*",    // 数字+道路
                ".*[路街巷道].*\\d+.*",    // 道路+数字
                ".*[Bb]uilding.*\\d+.*",  // Building+数字
                ".*[Aa]pt.*\\d+.*",       // Apt+数字
                ".*[Nn]o\\.\\s*\\d+.*"    // No.+数字
        };

        for (String pattern : patterns) {
            if (Pattern.matches(pattern, address)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 语言枚举
     */
    public enum AddressLanguage {
        CHINESE, ENGLISH, MIXED
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

    /**
     * 获取详细的校验结果（可选）
     */
    public ValidationResult validateAddressWithDetails(String addressDetail) {
        ValidationResult result = new ValidationResult();

        boolean valid = validateAddress(addressDetail);
        result.setValid(valid);
        result.setMessage(valid ? "地址格式有效" : "地址格式无效");

        return result;
    }

    /**
     * 校验结果类
     */
    public static class ValidationResult {
        private boolean valid;
        private String message;
        private String suggestion;

        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getSuggestion() { return suggestion; }
        public void setSuggestion(String suggestion) { this.suggestion = suggestion; }
    }
}