package com.platform.monitor.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.common.BusinessException;
import com.platform.monitor.mapper.SapSystemMapper;
import com.platform.monitor.mapper.SqlLogMapper;
import com.platform.monitor.model.MonitorSqlLog;
import com.platform.monitor.model.SapSystem;
import com.platform.monitor.service.HanaQueryService;
import cn.hutool.core.util.StrUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class HanaQueryServiceImpl implements HanaQueryService {

    private static final Pattern PARAM_PATTERN = Pattern.compile("\\{([^}]+)\\}");
    
    private final Map<Long, DataSource> dataSourceCache = new HashMap<>();
    
    @Autowired
    private SapSystemMapper sapSystemMapper;
    
    @Autowired
    private SqlLogMapper sqlLogMapper;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<Map<String, Object>> executeQuery(Long systemId, String sql, Map<String, Object> params) {
        MonitorSqlLog log = new MonitorSqlLog();
        log.setSystemId(systemId);
        log.setSqlContent(sql);
        log.setParams(toJson(params));
        
        long startTime = System.currentTimeMillis();
        try {
            String executedSql = replaceParams(sql, params);
            log.setSqlContent(executedSql);
            
            List<Map<String, Object>> result = executeQueryInternal(systemId, executedSql);
            
            log.setStatus(1);
            log.setExecuteTime((int) (System.currentTimeMillis() - startTime));
            saveLog(log);
            
            return result;
        } catch (Exception e) {
            log.setStatus(0);
            log.setErrorMessage(e.getMessage());
            log.setExecuteTime((int) (System.currentTimeMillis() - startTime));
            saveLog(log);
            throw new BusinessException("SQL执行失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> executeQueryForSingle(Long systemId, String sql, Map<String, Object> params) {
        List<Map<String, Object>> results = executeQuery(systemId, sql, params);
        if (results.isEmpty()) {
            return new HashMap<>();
        }
        return results.get(0);
    }

    @Override
    public boolean testConnection(Long systemId) {
        try {
            DataSource ds = getDataSource(systemId);
            try (Connection conn = ds.getConnection()) {
                return conn.isValid(5);
            }
        } catch (Exception e) {
            throw new BusinessException("数据库连接测试失败: " + e.getMessage());
        }
    }

    private DataSource getDataSource(Long systemId) {
        return dataSourceCache.computeIfAbsent(systemId, id -> {
            SapSystem system = sapSystemMapper.selectById(id);
            if (system == null) {
                throw new BusinessException("SAP系统不存在: " + id);
            }
            return createDataSource(system);
        });
    }

    private DataSource createDataSource(SapSystem system) {
        HikariConfig config = new HikariConfig();
        String jdbcUrl = String.format("jdbc:hana://%s:%d/%s", 
            system.getHost(), system.getPort(), system.getDatabaseName());
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(system.getUsername());
        config.setPassword(system.getPassword());
        config.setDriverClassName("com.sap.db.jdbc.Driver");
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(1);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        return new HikariDataSource(config);
    }

    private List<Map<String, Object>> executeQueryInternal(Long systemId, String sql) {
        List<Map<String, Object>> results = new ArrayList<>();
        DataSource ds = getDataSource(systemId);
        
        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    Object value = rs.getObject(i);
                    row.put(columnName, value);
                }
                results.add(row);
            }
        } catch (SQLException e) {
            throw new BusinessException("SQL查询失败: " + e.getMessage());
        }
        
        return results;
    }

    private String replaceParams(String sql, Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return sql;
        }
        
        String result = sql;
        Matcher matcher = PARAM_PATTERN.matcher(sql);
        StringBuffer sb = new StringBuffer();
        
        while (matcher.find()) {
            String paramName = matcher.group(1);
            Object paramValue = params.get(paramName);
            String replacement = paramValue != null ? paramValue.toString() : "NULL";
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(sb);
        
        return sb.toString();
    }

    private String toJson(Map<String, Object> params) {
        if (params == null) {
            return "{}";
        }
        try {
            return objectMapper.writeValueAsString(params);
        } catch (Exception e) {
            return "{}";
        }
    }

    private void saveLog(MonitorSqlLog log) {
        try {
            log.setCreateTime(LocalDateTime.now());
            sqlLogMapper.insert(log);
        } catch (Exception e) {
            // log saving failed, ignore
        }
    }
}
