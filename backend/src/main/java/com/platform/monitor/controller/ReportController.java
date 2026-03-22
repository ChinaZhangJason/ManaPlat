package com.platform.monitor.controller;

import com.platform.common.Result;
import com.platform.monitor.dto.ReportConfigRequest;
import com.platform.monitor.dto.WidgetDataRequest;
import com.platform.monitor.model.ReportConfig;
import com.platform.monitor.service.DataSourceService;
import com.platform.monitor.service.ReportConfigService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/monitor/report")
public class ReportController {

    private final ReportConfigService reportConfigService;
    private final Map<String, DataSourceService> dataSourceServices;

    public ReportController(
            ReportConfigService reportConfigService,
            List<DataSourceService> dataSourceServiceList) {
        this.reportConfigService = reportConfigService;
        this.dataSourceServices = dataSourceServiceList.stream()
                .collect(Collectors.toMap(DataSourceService::getType, s -> s));
    }

    @GetMapping("/list")
    public Result<List<ReportConfig>> list() {
        List<ReportConfig> list = reportConfigService.list();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<ReportConfig> get(@PathVariable Long id) {
        ReportConfig config = reportConfigService.getWithWidgets(id);
        return Result.success(config);
    }

    @PostMapping
    public Result<ReportConfig> create(@RequestBody ReportConfigRequest request) {
        ReportConfig config = reportConfigService.create(request, null);
        return Result.success(config);
    }

    @PutMapping("/{id}")
    public Result<ReportConfig> update(@PathVariable Long id, @RequestBody ReportConfigRequest request) {
        ReportConfig config = reportConfigService.update(id, request, null);
        return Result.success(config);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        reportConfigService.delete(id);
        return Result.success();
    }

    @PostMapping("/widget/data")
    public Result<Map<String, Object>> getWidgetData(@RequestBody WidgetDataRequest request) {
        try {
            DataSourceService service = dataSourceServices.get(request.getDataSourceType());
            if (service == null) {
                return Result.error(400, "不支持的数据源类型: " + request.getDataSourceType());
            }
            Map<String, Object> data = service.query(request);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error(500, "查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/test/connection")
    public Result<Boolean> testConnection(@RequestBody Map<String, Object> config) {
        try {
            String type = config.get("type").toString();
            DataSourceService service = dataSourceServices.get(type);
            if (service == null) {
                return Result.error(400, "不支持的数据源类型: " + type);
            }
            boolean success = service.testConnection(config);
            return Result.success(success);
        } catch (Exception e) {
            return Result.error(500, "连接测试失败: " + e.getMessage());
        }
    }
}
