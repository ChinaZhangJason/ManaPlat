package com.platform.monitor.service.impl;

import cn.hutool.json.JSONUtil;
import com.platform.monitor.dto.WidgetDataRequest;
import com.platform.monitor.service.DataSourceService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ApiDataSourceServiceImpl implements DataSourceService {

    private final RestTemplate restTemplate;

    public ApiDataSourceServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getType() {
        return "API";
    }

    @Override
    public Map<String, Object> query(WidgetDataRequest request) throws Exception {
        String url = request.getDataSourceConfig().get("url").toString();
        String method = request.getDataSourceConfig().getOrDefault("method", "GET").toString();
        
        Map<String, Object> headers = null;
        if (request.getDataSourceConfig().containsKey("headers")) {
            headers = JSONUtil.toBean(
                request.getDataSourceConfig().get("headers").toString(), 
                Map.class
            );
        }
        
        long startTime = System.currentTimeMillis();
        
        Object response;
        if ("POST".equalsIgnoreCase(method)) {
            Map<String, Object> body = null;
            if (request.getDataSourceConfig().containsKey("body")) {
                body = JSONUtil.toBean(
                    request.getDataSourceConfig().get("body").toString(),
                    Map.class
                );
            }
            response = restTemplate.postForObject(url, body, Object.class);
        } else {
            response = restTemplate.getForObject(url, Object.class);
        }
        
        long executeTime = System.currentTimeMillis() - startTime;

        Map<String, Object> result = new HashMap<>();
        result.put("data", response);
        result.put("executeTime", executeTime);
        
        return result;
    }

    @Override
    public boolean testConnection(Map<String, Object> config) throws Exception {
        try {
            String url = config.get("url").toString();
            restTemplate.getForObject(url, Object.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
