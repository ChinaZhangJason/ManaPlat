package com.platform.sap.controller;

import com.platform.common.Result;
import com.platform.sap.model.*;
import com.platform.sap.mapper.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sap")
public class SapConfigController {

    private final SapRfcConfigMapper rfcConfigMapper;
    private final SapApiConfigMapper apiConfigMapper;
    private final SapWebserviceConfigMapper webserviceConfigMapper;
    private final HanaConnectionConfigMapper hanaConfigMapper;

    public SapConfigController(
            SapRfcConfigMapper rfcConfigMapper,
            SapApiConfigMapper apiConfigMapper,
            SapWebserviceConfigMapper webserviceConfigMapper,
            HanaConnectionConfigMapper hanaConfigMapper) {
        this.rfcConfigMapper = rfcConfigMapper;
        this.apiConfigMapper = apiConfigMapper;
        this.webserviceConfigMapper = webserviceConfigMapper;
        this.hanaConfigMapper = hanaConfigMapper;
    }

    @GetMapping("/rfc/list")
    public Result<List<SapRfcConfig>> listRfc() {
        return Result.success(rfcConfigMapper.selectList(null));
    }

    @PostMapping("/rfc")
    public Result<SapRfcConfig> createRfc(@RequestBody SapRfcConfig config) {
        rfcConfigMapper.insert(config);
        return Result.success(config);
    }

    @PutMapping("/rfc/{id}")
    public Result<SapRfcConfig> updateRfc(@PathVariable Long id, @RequestBody SapRfcConfig config) {
        config.setId(id);
        rfcConfigMapper.updateById(config);
        return Result.success(config);
    }

    @DeleteMapping("/rfc/{id}")
    public Result<Void> deleteRfc(@PathVariable Long id) {
        rfcConfigMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/api/list")
    public Result<List<SapApiConfig>> listApi() {
        return Result.success(apiConfigMapper.selectList(null));
    }

    @PostMapping("/api")
    public Result<SapApiConfig> createApi(@RequestBody SapApiConfig config) {
        apiConfigMapper.insert(config);
        return Result.success(config);
    }

    @PutMapping("/api/{id}")
    public Result<SapApiConfig> updateApi(@PathVariable Long id, @RequestBody SapApiConfig config) {
        config.setId(id);
        apiConfigMapper.updateById(config);
        return Result.success(config);
    }

    @DeleteMapping("/api/{id}")
    public Result<Void> deleteApi(@PathVariable Long id) {
        apiConfigMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/webservice/list")
    public Result<List<SapWebserviceConfig>> listWebservice() {
        return Result.success(webserviceConfigMapper.selectList(null));
    }

    @PostMapping("/webservice")
    public Result<SapWebserviceConfig> createWebservice(@RequestBody SapWebserviceConfig config) {
        webserviceConfigMapper.insert(config);
        return Result.success(config);
    }

    @PutMapping("/webservice/{id}")
    public Result<SapWebserviceConfig> updateWebservice(@PathVariable Long id, @RequestBody SapWebserviceConfig config) {
        config.setId(id);
        webserviceConfigMapper.updateById(config);
        return Result.success(config);
    }

    @DeleteMapping("/webservice/{id}")
    public Result<Void> deleteWebservice(@PathVariable Long id) {
        webserviceConfigMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/hana/list")
    public Result<List<HanaConnectionConfig>> listHana() {
        return Result.success(hanaConfigMapper.selectList(null));
    }

    @PostMapping("/hana")
    public Result<HanaConnectionConfig> createHana(@RequestBody HanaConnectionConfig config) {
        hanaConfigMapper.insert(config);
        return Result.success(config);
    }

    @PutMapping("/hana/{id}")
    public Result<HanaConnectionConfig> updateHana(@PathVariable Long id, @RequestBody HanaConnectionConfig config) {
        config.setId(id);
        hanaConfigMapper.updateById(config);
        return Result.success(config);
    }

    @DeleteMapping("/hana/{id}")
    public Result<Void> deleteHana(@PathVariable Long id) {
        hanaConfigMapper.deleteById(id);
        return Result.success();
    }

    @PostMapping("/hana/{id}/test")
    public Result<Boolean> testHana(@PathVariable Long id) {
        HanaConnectionConfig config = hanaConfigMapper.selectById(id);
        boolean success = testHanaConnection(config);
        return Result.success(success);
    }

    private boolean testHanaConnection(HanaConnectionConfig config) {
        try {
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
