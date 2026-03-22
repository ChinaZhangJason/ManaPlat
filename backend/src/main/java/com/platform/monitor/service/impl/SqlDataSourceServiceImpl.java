package com.platform.monitor.service.impl;

import cn.hutool.json.JSONUtil;
import com.platform.monitor.dto.WidgetDataRequest;
import com.platform.monitor.service.DataSourceService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SqlDataSourceServiceImpl implements DataSourceService {

    private final JdbcTemplate jdbcTemplate;
    private final Map<String, JdbcTemplate> templateCache = new ConcurrentHashMap<>();

    public SqlDataSourceServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String getType() {
        return "SQL";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> query(WidgetDataRequest request) throws Exception {
        String sql = request.getDataSourceConfig().get("sql").toString();
        String finalSql = replaceParams(sql, request.getFilters());
        
        long startTime = System.currentTimeMillis();
        List<Map<String, Object>> results = jdbcTemplate.queryForList(finalSql);
        long executeTime = System.currentTimeMillis() - startTime;

        Map<String, Object> response = new HashMap<>();
        response.put("data", results);
        response.put("total", results.size());
        response.put("executeTime", executeTime);
        
        return response;
    }

    @Override
    public boolean testConnection(Map<String, Object> config) throws Exception {
        try {
            String sql = "SELECT 1";
            jdbcTemplate.queryForList(sql);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String replaceParams(String sql, Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return sql;
        }
        String result = sql;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            String value = entry.getValue() != null ? entry.getValue().toString() : "";
            result = result.replace(placeholder, value);
        }
        return result;
    }
}
