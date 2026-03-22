package com.platform.alert.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.alert.mapper.AlertReceiverMapper;
import com.platform.alert.model.AlertReceiver;
import com.platform.alert.service.AlertReceiverService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertReceiverServiceImpl extends ServiceImpl<AlertReceiverMapper, AlertReceiver> implements AlertReceiverService {

    @Override
    public List<AlertReceiver> listEnabled() {
        return lambdaQuery().eq(AlertReceiver::getStatus, 1).list();
    }

    @Override
    public AlertReceiver create(AlertReceiver receiver) {
        receiver.setCreatedAt(LocalDateTime.now());
        receiver.setUpdatedAt(LocalDateTime.now());
        receiver.setStatus(1);
        this.save(receiver);
        return receiver;
    }

    @Override
    public AlertReceiver update(Long id, AlertReceiver receiver) {
        receiver.setId(id);
        receiver.setUpdatedAt(LocalDateTime.now());
        this.updateById(receiver);
        return receiver;
    }

    @Override
    public void delete(Long id) {
        this.removeById(id);
    }
}
