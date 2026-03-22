package com.platform.monitor;

import com.platform.monitor.service.ReportConfigService;
import com.platform.monitor.model.ReportConfig;
import com.platform.monitor.dto.ReportConfigRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReportServiceTest {

    @Autowired
    private ReportConfigService reportConfigService;

    @Test
    void testCreateReport() {
        ReportConfigRequest request = new ReportConfigRequest();
        request.setReportName("测试报表");
        request.setReportCode("TEST_REPORT");
        request.setDescription("测试描述");
        request.setRefreshInterval(60);
        request.setWidgets(new ArrayList<>());

        ReportConfig config = reportConfigService.create(request, 1L);
        
        assertNotNull(config);
        assertNotNull(config.getId());
        assertEquals("测试报表", config.getReportName());
    }

    @Test
    void testListReports() {
        List<ReportConfig> list = reportConfigService.listByUser(1L);
        assertNotNull(list);
    }

    @Test
    void testGetReportWithWidgets() {
        ReportConfig config = reportConfigService.getWithWidgets(1L);
        assertNotNull(config);
    }
}
