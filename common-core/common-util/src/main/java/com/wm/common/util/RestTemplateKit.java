package com.wm.common.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-08-30 16:18
 */
@Component
@Slf4j
public class RestTemplateKit {

    private static final Integer success = 200;
    private static final String SERIAL_VERSION_UID = "serialVersionUID";

    @Resource
    private RestTemplate restTemplate;

    /**
     * 获取RestTemplate实例对象，可自由调用其方法
     *
     * @return RestTemplate实例对象
     */
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    /**
     * GET请求调用方式
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @return T 响应对象
     */
    public <T> T get(String url, Class<T> responseType) {
        return restTemplate.getForEntity(url, responseType).getBody();
    }

    /**
     * GET请求调用方式
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @return T 响应对象
     */
    public <T> T get(String url, ParameterizedTypeReference<T> responseType) {
        return exchange(url, HttpMethod.GET, HttpEntity.EMPTY, responseType);
    }

    /**
     * GET请求调用方式
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T get(String url, Class<T> responseType, Object... uriVariables) {
        return restTemplate.getForEntity(url, responseType, uriVariables).getBody();
    }

    /**
     * GET请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T get(String url, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        return exchange(url, HttpMethod.GET, HttpEntity.EMPTY, responseType, uriVariables);
    }

    /**
     * GET请求调用方式
     *
     * @param url          请求URL  http://localhost:8762/hi?name={name}
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T get(String url, Class<T> responseType, Map<String, ?> uriVariables) {
        return restTemplate.getForEntity(url, responseType, uriVariables).getBody();
    }

    /**
     * GET请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T get(String url, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) {
        return exchange(url, HttpMethod.GET, HttpEntity.EMPTY, responseType, uriVariables);
    }

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T get(String url, Map<String, String> headers, Class<T> responseType, Object... uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return get(url, httpHeaders, responseType, uriVariables);
    }

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T get(String url, Map<String, String> headers, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return get(url, httpHeaders, responseType, uriVariables);
    }

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T get(String url, HttpHeaders headers, Class<T> responseType, Object... uriVariables) {
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        return exchange(url, HttpMethod.GET, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T get(String url, HttpHeaders headers, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        return exchange(url, HttpMethod.GET, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T get(String url, Map<String, String> headers, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return get(url, httpHeaders, responseType, uriVariables);
    }

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T get(String url, Map<String, String> headers, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return get(url, httpHeaders, responseType, uriVariables);
    }

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T get(String url, HttpHeaders headers, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        return exchange(url, HttpMethod.GET, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T get(String url, HttpHeaders headers, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        return exchange(url, HttpMethod.GET, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @param params  URL 中的变量
     * @return T 响应对象
     */
    public <T> T get(String url, Map<String, ?> params, Map<String, String> headers, Class<T> responseType, Object... uriVariables) {
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        return exchange(urlSplice(url, params), HttpMethod.GET, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @param params  URL 中的变量
     * @return T 响应对象
     */
    public <T> T get(String url, Map<String, ?> params, Map<String, String> headers, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        return exchange(urlSplice(url, params), HttpMethod.GET, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @param params  URL 中的变量
     * @return T 响应对象
     */
    public <T> T get(String url, Map<String, ?> params, Map<String, String> headers, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        return exchange(urlSplice(url, params), HttpMethod.GET, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @param params  URL 中的变量
     * @return T 响应对象
     */
    public <T> T get(String url, Map<String, ?> params, Map<String, String> headers, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        return exchange(urlSplice(url, params), HttpMethod.GET, requestEntity, responseType, uriVariables);
    }


    // ----------------------------------POST-------------------------------------------------------

    /**
     * POST请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @return
     */
    public <T> T post(String url, Class<T> responseType) {
        return restTemplate.postForEntity(url, HttpEntity.EMPTY, responseType).getBody();
    }

    /**
     * POST请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @return
     */
    public <T> T post(String url, ParameterizedTypeReference<T> responseType) {
        return exchange(url, HttpMethod.POST, HttpEntity.EMPTY, responseType);
    }

    /**
     * POST请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @return T 响应对象
     */
    public <T> T post(String url, Object requestBody, Class<T> responseType) {
        return restTemplate.postForEntity(url, requestBody, responseType).getBody();
    }

    /**
     * POST请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @return T 响应对象
     */
    public <T> T post(String url, Object requestBody, ParameterizedTypeReference<T> responseType) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return exchange(url, HttpMethod.POST, requestEntity, responseType);
    }

    /**
     * POST请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T post(String url, Object requestBody, Class<T> responseType, Object... uriVariables) {
        return restTemplate.postForEntity(url, requestBody, responseType, uriVariables).getBody();
    }

    /**
     * POST请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T post(String url, Object requestBody, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return exchange(url, HttpMethod.POST, requestEntity, responseType, uriVariables);
    }

    /**
     * POST请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T post(String url, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) {
        return restTemplate.postForEntity(url, requestBody, responseType, uriVariables).getBody();
    }

    /**
     * POST请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T post(String url, Object requestBody, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return exchange(url, HttpMethod.POST, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的POST请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T post(String url, Map<String, String> headers, Object requestBody, Class<T> responseType, Object... uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return post(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的POST请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T post(String url, Map<String, String> headers, Object requestBody, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return post(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的POST请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T post(String url, HttpHeaders headers, Object requestBody, Class<T> responseType, Object... uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return post(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的POST请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T post(String url, HttpHeaders headers, Object requestBody, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return post(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的POST请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T post(String url, Map<String, String> headers, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return post(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的POST请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T post(String url, Map<String, String> headers, Object requestBody, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return post(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的POST请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T post(String url, HttpHeaders headers, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return post(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的POST请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T post(String url, HttpHeaders headers, Object requestBody, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return post(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 自定义请求头和请求体的POST请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T post(String url, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        return exchange(url, HttpMethod.POST, requestEntity, responseType, uriVariables);
    }

    /**
     * 自定义请求头和请求体的POST请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T post(String url, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) {
        return exchange(url, HttpMethod.POST, requestEntity, responseType, uriVariables);
    }

    /**
     * 自定义请求头和请求体的POST请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T post(String url, HttpEntity<?> requestEntity, Class<T> responseType, Map<String, ?> uriVariables) {
        return exchange(url, HttpMethod.POST, requestEntity, responseType, uriVariables);
    }

    /**
     * 自定义请求头和请求体的POST请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T post(String url, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) {
        return exchange(url, HttpMethod.POST, requestEntity, responseType, uriVariables);
    }

    /**
     * POST请求调用方式
     *
     * @param url          请求URL
     * @param params       请求Body 参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return List<T> 返回对象集合
     */
    public <T> List<T> post4List(String url, Object params, Class<T> responseType, Map<String, ?> uriVariables) {
        ResponseEntity<JSONArray> responseEntity = restTemplate.postForEntity(url, objectToMultiMap(params, null), JSONArray.class, uriVariables);
        return responseEntity.getBody().toJavaList(responseType);
    }

    /**
     * POST请求调用方式
     *
     * @param url          请求URL
     * @param params       请求Body 参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量
     * @return List<T> 返回对象集合
     */
    public <T> List<T> post4List(String url, Object params, Class<T> responseType, Object... uriVariables) {
        ResponseEntity<JSONArray> responseEntity = restTemplate.postForEntity(url, objectToMultiMap(params, null), JSONArray.class, uriVariables);
        return responseEntity.getBody().toJavaList(responseType);
    }

    /**
     * 带文件 POST请求调用方式
     *
     * @param url          请求URL
     * @param params       请求Body 参数
     * @param responseType 返回对象类型
     * @param fileFields   文件字段集合
     * @param uriVariables URL中的变量
     * @return T 返回对象
     */
    public <T> T post4File(String url, Object params, Class<T> responseType, List<String> fileFields, Object... uriVariables) {

        MultiValueMap<String, Object> param = objectToMultiMap(params, fileFields);

        List<File> files = null;

        if (ObjectUtil.isNotEmpty(params)) {
            try {
                files = new ArrayList<File>();
                for (String name : fileFields) {
                    Field field = params.getClass().getDeclaredField(name);
                    field.setAccessible(true);

                    //因上层代码已转为io.File,因此此处无须再转
                    File file = (File) field.get(params);
                    files.add(file);
                    param.add(name, new FileSystemResource(file));
                }
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
                log.error("post4File {}", e.getMessage());
            }
        }
        ResponseEntity<T> responseEntity = restTemplate.postForEntity(url, param, responseType, uriVariables);
        if (CollectionUtil.isNotEmpty(files)) {
            files.forEach(f -> {
                if (f.exists()) {
                    f.delete();
                }
            });
        }

        return responseEntity.getBody();
    }

    /**
     * 自定义请求头POST请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param uriVariables  URL中的变量，按顺序依次对应
     * @return Boolean true/false
     */
    public Boolean post4Boolean(String url, HttpEntity<?> requestEntity, Map<String, ?> uriVariables) {
        return getBooleanMethod(url, requestEntity, HttpMethod.POST, uriVariables);
    }

    /**
     * 自定义请求头POST请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param uriVariables  URL中的变量
     * @return Boolean true/false
     */
    public Boolean post4Boolean(String url, HttpEntity<?> requestEntity, Object... uriVariables) {
        return getBooleanMethod(url, requestEntity, HttpMethod.POST, uriVariables);
    }

    // ----------------------------------PUT-------------------------------------------------------

    /**
     * PUT请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T put(String url, Class<T> responseType, Object... uriVariables) {
        return put(url, HttpEntity.EMPTY, responseType, uriVariables);
    }

    /**
     * PUT请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T put(String url, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        return put(url, HttpEntity.EMPTY, responseType, uriVariables);
    }

    /**
     * PUT请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T put(String url, Object requestBody, Class<T> responseType, Object... uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return put(url, requestEntity, responseType, uriVariables);
    }

    /**
     * PUT请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T put(String url, Object requestBody, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return put(url, requestEntity, responseType, uriVariables);
    }

    /**
     * PUT请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T put(String url, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return put(url, requestEntity, responseType, uriVariables);
    }

    /**
     * PUT请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T put(String url, Object requestBody, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return put(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的PUT请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T put(String url, Map<String, String> headers, Object requestBody, Class<T> responseType, Object... uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return put(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的PUT请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T put(String url, Map<String, String> headers, Object requestBody, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return put(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的PUT请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T put(String url, HttpHeaders headers, Object requestBody, Class<T> responseType, Object... uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return put(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的PUT请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T put(String url, HttpHeaders headers, Object requestBody, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return put(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的PUT请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T put(String url, Map<String, String> headers, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return put(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的PUT请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T put(String url, Map<String, String> headers, Object requestBody, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return put(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的PUT请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T put(String url, HttpHeaders headers, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return put(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的PUT请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T put(String url, HttpHeaders headers, Object requestBody, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return put(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 自定义请求头和请求体的PUT请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T put(String url, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) {
        return exchange(url, HttpMethod.PUT, requestEntity, responseType, uriVariables);
    }

    /**
     * 自定义请求头和请求体的PUT请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T put(String url, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        return exchange(url, HttpMethod.PUT, requestEntity, responseType, uriVariables);
    }

    /**
     * 自定义请求头和请求体的PUT请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T put(String url, HttpEntity<?> requestEntity, Class<T> responseType, Map<String, ?> uriVariables) {
        return exchange(url, HttpMethod.PUT, requestEntity, responseType, uriVariables);
    }

    /**
     * 自定义请求头和请求体的PUT请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T put(String url, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) {
        return exchange(url, HttpMethod.PUT, requestEntity, responseType, uriVariables);
    }

    /**
     * 自定义请求头PUT请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param uriVariables  URL中的变量
     * @return Boolean true/false
     */
    public Boolean put(String url, HttpEntity<?> requestEntity, Map<String, ?> uriVariables) {
        return getBooleanMethod(url, requestEntity, HttpMethod.PUT, uriVariables);
    }

    /**
     * 自定义请求头PUT请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param uriVariables  URL中的变量
     * @return Boolean true/false
     */
    public Boolean put(String url, HttpEntity<?> requestEntity, Object... uriVariables) {
        return getBooleanMethod(url, requestEntity, HttpMethod.PUT, uriVariables);
    }

    // ----------------------------------DELETE-------------------------------------------------------

    /**
     * DELETE请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T delete(String url, Class<T> responseType, Object... uriVariables) {
        return delete(url, HttpEntity.EMPTY, responseType, uriVariables);
    }

    /**
     * DELETE请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T delete(String url, Class<T> responseType, Map<String, ?> uriVariables) {
        return delete(url, HttpEntity.EMPTY, responseType, uriVariables);
    }

    /**
     * DELETE请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T delete(String url, Object requestBody, Class<T> responseType, Object... uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * DELETE请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T delete(String url, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的DELETE请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T delete(String url, Map<String, String> headers, Class<T> responseType, Object... uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return delete(url, httpHeaders, responseType, uriVariables);
    }

    /**
     * 带请求头的DELETE请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T delete(String url, HttpHeaders headers, Class<T> responseType, Object... uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的DELETE请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T delete(String url, Map<String, String> headers, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return delete(url, httpHeaders, responseType, uriVariables);
    }

    /**
     * 带请求头的DELETE请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T delete(String url, HttpHeaders headers, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的DELETE请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T delete(String url, Map<String, String> headers, Object requestBody, Class<T> responseType, Object... uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return delete(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的DELETE请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T delete(String url, HttpHeaders headers, Object requestBody, Class<T> responseType, Object... uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的DELETE请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T delete(String url, Map<String, String> headers, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return delete(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的DELETE请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T delete(String url, HttpHeaders headers, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 自定义请求头和请求体的DELETE请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T delete(String url, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) {
        return restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, responseType, uriVariables).getBody();
    }

    /**
     * 自定义请求头和请求体的DELETE请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T delete(String url, HttpEntity<?> requestEntity, Class<T> responseType, Map<String, ?> uriVariables) {
        return restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, responseType, uriVariables).getBody();
    }

    /**
     * 自定义请求头DELETE请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param uriVariables  URL中的变量
     * @return Boolean true/false
     */
    public Boolean delete(String url, HttpEntity<?> requestEntity, Map<String, ?> uriVariables) {
        return getBooleanMethod(url, requestEntity, HttpMethod.DELETE, uriVariables);
    }

    /**
     * 自定义请求头DELETE请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param uriVariables  URL中的变量
     * @return Boolean true/false
     */
    public Boolean delete(String url, HttpEntity<?> requestEntity, Object... uriVariables) {
        return getBooleanMethod(url, requestEntity, HttpMethod.DELETE, uriVariables);
    }

    // ----------------------------------PATCH-------------------------------------------------------

    /**
     * PATCH请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T patch(String url, Class<T> responseType, Object... uriVariables) {
        return patch(url, HttpEntity.EMPTY, responseType, uriVariables);
    }

    /**
     * PATCH请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T patch(String url, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        return patch(url, HttpEntity.EMPTY, responseType, uriVariables);
    }

    /**
     * PATCH请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T patch(String url, Object requestBody, Class<T> responseType, Object... uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return patch(url, requestEntity, responseType, uriVariables);
    }

    /**
     * PATCH请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T patch(String url, Object requestBody, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return patch(url, requestEntity, responseType, uriVariables);
    }

    /**
     * PATCH 请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T patch(String url, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return patch(url, requestEntity, responseType, uriVariables);
    }

    /**
     * PATCH 请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T patch(String url, Object requestBody, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return patch(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的PATCH 请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T patch(String url, Map<String, String> headers, Object requestBody, Class<T> responseType, Object... uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return patch(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的PATCH 请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T patch(String url, Map<String, String> headers, Object requestBody, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return patch(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的PATCH请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T patch(String url, HttpHeaders headers, Object requestBody, Class<T> responseType, Object... uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return patch(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的PATCH请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T patch(String url, HttpHeaders headers, Object requestBody, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return patch(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的PATCH请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T patch(String url, Map<String, String> headers, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return patch(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的PATCH请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T patch(String url, Map<String, String> headers, Object requestBody, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return patch(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的PATCH请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T patch(String url, HttpHeaders headers, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return patch(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的PATCH请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T patch(String url, HttpHeaders headers, Object requestBody, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return patch(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 自定义请求头和请求体的PATCH请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T patch(String url, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) {
        return exchange(url, HttpMethod.PATCH, requestEntity, responseType, uriVariables);
    }

    /**
     * 自定义请求头和请求体的PATCH请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T patch(String url, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        return exchange(url, HttpMethod.PATCH, requestEntity, responseType, uriVariables);
    }

    /**
     * 自定义请求头和请求体的PATCH请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T patch(String url, HttpEntity<?> requestEntity, Class<T> responseType, Map<String, ?> uriVariables) {
        return exchange(url, HttpMethod.PATCH, requestEntity, responseType, uriVariables);
    }

    /**
     * 自定义请求头和请求体的PATCH请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T patch(String url, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) {
        return exchange(url, HttpMethod.PATCH, requestEntity, responseType, uriVariables);
    }

    /**
     * 自定义请求头PATCH请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param uriVariables  URL中的变量
     * @return Boolean true/false
     */
    public Boolean patch(String url, HttpEntity<?> requestEntity, Map<String, ?> uriVariables) {
        return getBooleanMethod(url, requestEntity, HttpMethod.PATCH, uriVariables);
    }

    /**
     * 自定义请求头PATCH请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param uriVariables  URL中的变量
     * @return Boolean true/false
     */
    public Boolean patch(String url, HttpEntity<?> requestEntity, Object... uriVariables) {
        return getBooleanMethod(url, requestEntity, HttpMethod.PATCH, uriVariables);
    }

    // ----------------------------------通用方法-------------------------------------------------------


    /**
     * 通用调用方式
     *
     * @param url           请求URL
     * @param method        请求方法类型
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) {
        return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables).getBody();
    }

    /**
     * 通用调用方式
     *
     * @param url           请求URL
     * @param method        请求方法类型
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Map<String, ?> uriVariables) {
        return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables).getBody();
    }

    /**
     * 通用调用方式
     *
     * @param url           请求URL
     * @param method        请求方法类型
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @return T 响应对象
     */
    public <T> T exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType) {
        return restTemplate.exchange(url, method, requestEntity, responseType).getBody();
    }

    /**
     * 通用调用方式
     *
     * @param url           请求URL
     * @param method        请求方法类型
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，按顺序依次对应
     * @return T 响应对象
     */
    public <T> T exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables).getBody();
    }

    /**
     * 通用调用方式
     *
     * @param url           请求URL
     * @param method        请求方法类型
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，与Map中的key对应
     * @return T 响应对象
     */
    public <T> T exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) {
        return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables).getBody();
    }

    /**
     * 通用调用方式
     *
     * @param url           请求URL
     * @param method        请求方法类型
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @return T 响应对象
     */
    public <T> T exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType) {
        return restTemplate.exchange(url, method, requestEntity, responseType).getBody();
    }

    /**
     * Boolean 返回对象请求方法
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param method        请求方式
     * @param uriVariables  URL中的变量，与Map中的key对应
     * @return Boolean true/false
     */
    private Boolean getBooleanMethod(String url, HttpEntity<?> requestEntity, HttpMethod method, Map<String, ?> uriVariables) {
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(url, method, requestEntity, JSONObject.class,
                uriVariables);
        return success.equals(responseEntity.getStatusCodeValue());
    }

    /**
     * Boolean 返回对象请求方法
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param method        请求方式
     * @param uriVariables  URL中的变量
     * @return Boolean true/false
     */
    private Boolean getBooleanMethod(String url, HttpEntity<?> requestEntity, HttpMethod method, Object... uriVariables) {
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(url, method, requestEntity, JSONObject.class,
                uriVariables);
        return success.equals(responseEntity.getStatusCodeValue());
    }

    /**
     *  对象转换MultiValueMap
     * @param obj 对象
     * @param excludes 排除字段名
     * @return MultiValueMap key: 字段名，value: 值
     */
    public MultiValueMap<String, Object> objectToMultiMap(Object obj, List<String> excludes) {

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

        if (obj != null) {
            Arrays.asList(obj.getClass().getDeclaredFields()).forEach(field -> {
                field.setAccessible(true);
                Object value = null;

                try {
                    value = field.get(obj);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    log.error("MultiValueMap() IllegalAccessException {}", e.getMessage());
                }

                if (value != null && !SERIAL_VERSION_UID.equals(field.getName())) {
                    map.add(field.getName(), value);
                }
            });

            if (excludes != null) {
                excludes.forEach(map::remove);
            }
        }
        return map;
    }

    private String urlSplice(String originalUrl, Map<String, ?> params) {
        StringBuilder urlSb = new StringBuilder(originalUrl);
        if (CollectionUtil.isNotEmpty(params) && params.size() > 0) {
            StringJoiner stringJoiner = new StringJoiner("&");
            params.forEach((k, v) -> {
                if (ObjectUtil.isNotEmpty(v)){
                    stringJoiner.add(k + "=" + v);
                }
            });
            urlSb.append("?").append(stringJoiner.toString());
        }
        return urlSb.toString();
    }


}