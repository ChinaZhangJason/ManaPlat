package com.platform.monitor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.BusinessException;
import com.platform.monitor.dto.ReportConfigRequest;
import com.platform.monitor.mapper.ReportConfigMapper;
import com.platform.monitor.mapper.ReportWidgetMapper;
import com.platform.monitor.model.ReportConfig;
import com.platform.monitor.model.ReportWidget;
import com.platform.monitor.service.ReportConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportConfigServiceImpl extends ServiceImpl<ReportConfigMapper, ReportConfig> 
    implements ReportConfigService {

    private final ReportWidgetMapper widgetMapper;

    public ReportConfigServiceImpl(ReportWidgetMapper widgetMapper) {
        this.widgetMapper = widgetMapper;
    }

    @Override
    public List<ReportConfig> listByUser(Long userId) {
        LambdaQueryWrapper<ReportConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReportConfig::getStatus, 1);
        wrapper.orderByDesc(ReportConfig::getCreatedAt);
        return this.list(wrapper);
    }

    @Override
    @Transactional
    public ReportConfig create(ReportConfigRequest request, Long userId) {
        ReportConfig config = new ReportConfig();
        config.setReportName(request.getReportName());
        config.setReportCode(request.getReportCode());
        config.setDescription(request.getDescription());
        config.setLayoutConfig(toJson(request.getLayoutConfig()));
        config.setFilterConfig(toJson(request.getFilterConfig()));
        config.setRefreshInterval(request.getRefreshInterval() != null ? request.getRefreshInterval() : 60);
        config.setStatus(1);
        config.setCreatedBy(userId);
        config.setCreatedAt(LocalDateTime.now());
        config.setUpdatedAt(LocalDateTime.now());
        
        this.save(config);
        
        if (request.getWidgets() != null && !request.getWidgets().isEmpty()) {
            saveWidgets(config.getId(), request.getWidgets());
        }
        
        return config;
    }

    @Override
    @Transactional
    public ReportConfig update(Long id, ReportConfigRequest request, Long userId) {
        ReportConfig config = this.getById(id);
        if (config == null) {
            throw new BusinessException("报表配置不存在");
        }
        
        config.setReportName(request.getReportName());
        config.setReportCode(request.getReportCode());
        config.setDescription(request.getDescription());
        config.setLayoutConfig(toJson(request.getLayoutConfig()));
        config.setFilterConfig(toJson(request.getFilterConfig()));
        config.setRefreshInterval(request.getRefreshInterval());
        config.setStatus(request.getStatus());
        config.setUpdatedAt(LocalDateTime.now());
        
        this.updateById(config);
        
        if (request.getWidgets() != null) {
            widgetMapper.delete(new LambdaQueryWrapper<ReportWidget>()
                .eq(ReportWidget::getReportId, id));
            saveWidgets(id, request.getWidgets());
        }
        
        return config;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        widgetMapper.delete(new LambdaQueryWrapper<ReportWidget>()
            .eq(ReportWidget::getReportId, id));
        this.removeById(id);
    }

    @Override
    public ReportConfig getWithWidgets(Long id) {
        ReportConfig config = this.getById(id);
        if (config != null) {
            List<ReportWidget> widgets = widgetMapper.selectList(
                new LambdaQueryWrapper<ReportWidget>()
                    .eq(ReportWidget::getReportId, id)
                    .orderByAsc(ReportWidget::getSortOrder)
            );
            config.set("widgets", widgets);
        }
        return config;
    }

    private void saveWidgets(Long reportId, List<ReportConfigRequest.WidgetConfig> widgetConfigs) {
        for (int i = 0; i < widgetConfigs.size(); i++) {
            ReportConfigRequest.WidgetConfig wc = widgetConfigs.get(i);
            ReportWidget widget = new ReportWidget();
            widget.setReportId(reportId);
            widget.setWidgetName(wc.getWidgetName());
            widget.setWidgetType(wc.getWidgetType());
            widget.setDataSourceType(wc.getDataSourceType());
            widget.setDataSourceConfig(toJson(wc.getDataSourceConfig()));
            widget.setChartConfig(toJson(wc.getChartConfig()));
            widget.setPositionConfig(toJson(wc.getPositionConfig()));
            widget.setSortOrder(wc.getSortOrder() != null ? wc.getSortOrder() : i);
            widget.setCreatedAt(LocalDateTime.now());
            widget.setUpdatedAt(LocalDateTime.now());
            widgetMapper.insert(widget);
        }
    }

    private String toJson(Object obj) {
        if (obj == null) return null;
        return JSONUtil.toJsonStr(obj);
    }
}
