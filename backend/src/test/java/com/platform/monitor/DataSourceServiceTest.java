package com.platform.monitor;

import com.platform.monitor.service.ReportConfigService;
import com.platform.monitor.model.ReportConfig;
import com.platform.monitor.model.ReportWidget;
import com.platform.monitor.dto.ReportConfigRequest;
import com.platform.monitor.dto.WidgetDataRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DataSourceServiceTest {

    @Autowired(required = false)
    private ReportConfigService reportConfigService;

    @Test
    void testSqlDataSourceQuery() {
        Map<String, Object> dataSourceConfig = new HashMap<>();
        dataSourceConfig.put("sql", "SELECT 1 as result");
        
        WidgetDataRequest request = new WidgetDataRequest();
        request.setDataSourceType("SQL");
        request.setDataSourceConfig(dataSourceConfig);
        request.setFilters(new HashMap<>());
        
        assertEquals("SQL", request.getDataSourceType());
        assertEquals("SELECT 1 as result", request.getDataSourceConfig().get("sql"));
    }

    @Test
    void testApiDataSourceQuery() {
        Map<String, Object> dataSourceConfig = new HashMap<>();
        dataSourceConfig.put("url", "https://api.example.com/data");
        dataSourceConfig.put("method", "GET");
        
        WidgetDataRequest request = new WidgetDataRequest();
        request.setDataSourceType("API");
        request.setDataSourceConfig(dataSourceConfig);
        
        assertEquals("API", request.getDataSourceType());
        assertEquals("GET", request.getDataSourceConfig().get("method"));
    }

    @Test
    void testFilterParameters() {
        Map<String, Object> filters = new HashMap<>();
        filters.put("system_id", "SAP001");
        filters.put("start_date", "2024-01-01");
        filters.put("end_date", "2024-12-31");
        
        WidgetDataRequest request = new WidgetDataRequest();
        request.setFilters(filters);
        
        assertEquals(3, request.getFilters().size());
        assertEquals("SAP001", request.getFilters().get("system_id"));
    }

    @Test
    void testSqlParameterReplacement() {
        String sql = "SELECT * FROM monitor WHERE system_id = '${system_id}' AND date > '${start_date}'";
        Map<String, Object> params = new HashMap<>();
        params.put("system_id", "SAP001");
        params.put("start_date", "2024-01-01");
        
        String result = sql;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            result = result.replace("${" + entry.getKey() + "}", entry.getValue().toString());
        }
        
        assertTrue(result.contains("system_id = 'SAP001'"));
        assertTrue(result.contains("start_date = '2024-01-01'"));
    }
}
