package com.platform.alert.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.alert.model.AlertReceiver;
import java.util.List;

public interface AlertReceiverService extends IService<AlertReceiver> {
    List<AlertReceiver> listEnabled();
    AlertReceiver create(AlertReceiver receiver);
    AlertReceiver update(Long id, AlertReceiver receiver);
    void delete(Long id);
}
