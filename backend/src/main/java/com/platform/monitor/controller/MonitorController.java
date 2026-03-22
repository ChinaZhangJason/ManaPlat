package com.platform.monitor.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.common.Result;
import com.platform.monitor.model.MonitorSqlConfig;
import com.platform.monitor.model.SapSystem;
import com.platform.monitor.service.HanaQueryService;
import com.platform.monitor.service.SqlConfigService;
import com.platform.monitor.service.SapSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/monitor")
public class MonitorController {

    @Autowired
    private SapSystemService sapSystemService;
    
    @Autowired
    private SqlConfigService sqlConfigService;
    
    @Autowired
    private HanaQueryService hanaQueryService;
    
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/systems")
    public Result<List<SapSystem>> listSystems() {
        return Result.success(sapSystemService.listAll());
    }

    @GetMapping("/systems/{id}")
    public Result<SapSystem> getSystem(@PathVariable Long id) {
        return Result.success(sapSystemService.getById(id));
    }

    @PostMapping("/systems")
    public Result<Boolean> saveSystem(@RequestBody SapSystem system) {
        return Result.success(sapSystemService.save(system));
    }

    @PutMapping("/systems")
    public Result<Boolean> updateSystem(@RequestBody SapSystem system) {
        return Result.success(sapSystemService.update(system));
    }

    @DeleteMapping("/systems/{id}")
    public Result<Boolean> deleteSystem(@PathVariable Long id) {
        return Result.success(sapSystemService.deleteById(id));
    }

    @PostMapping("/systems/{id}/test")
    public Result<Boolean> testConnection(@PathVariable Long id) {
        return Result.success(sapSystemService.testConnection(id));
    }

    @GetMapping("/sql-config")
    public Result<List<MonitorSqlConfig>> listSqlConfigs() {
        return Result.success(sqlConfigService.listAll());
    }

    @GetMapping("/sql-config/{id}")
    public Result<MonitorSqlConfig> getSqlConfig(@PathVariable Long id) {
        return Result.success(sqlConfigService.getById(id));
    }

    @GetMapping("/sql-config/system/{systemId}")
    public Result<List<MonitorSqlConfig>> getSqlConfigsBySystem(@PathVariable Long systemId) {
        return Result.success(sqlConfigService.getBySystemId(systemId));
    }

    @PostMapping("/sql-config")
    public Result<Boolean> saveSqlConfig(@RequestBody MonitorSqlConfig config) {
        return Result.success(sqlConfigService.save(config));
    }

    @PutMapping("/sql-config")
    public Result<Boolean> updateSqlConfig(@RequestBody MonitorSqlConfig config) {
        return Result.success(sqlConfigService.update(config));
    }

    @DeleteMapping("/sql-config/{id}")
    public Result<Boolean> deleteSqlConfig(@PathVariable Long id) {
        return Result.success(sqlConfigService.deleteById(id));
    }

    @PostMapping("/metrics")
    public Result<List<Map<String, Object>>> queryMetrics(@RequestBody Map<String, Object> request) {
        Long systemId = ((Number) request.get("systemId")).longValue();
        String sql = (String) request.get("sql");
        Map<String, Object> params = (Map<String, Object>) request.get("params");
        
        List<Map<String, Object>> results = hanaQueryService.executeQuery(systemId, sql, params);
        return Result.success(results);
    }

    @PostMapping("/metrics/{configId}")
    public Result<List<Map<String, Object>>> queryMetricsByConfig(
            @PathVariable Long configId,
            @RequestBody Map<String, Object> params) {
        
        MonitorSqlConfig config = sqlConfigService.getById(configId);
        if (config == null) {
            return Result.error("配置不存在");
        }
        
        List<Map<String, Object>> results = hanaQueryService.executeQuery(
            config.getSystemId(), 
            config.getSqlTemplate(), 
            params
        );
        return Result.success(results);
    }
}
