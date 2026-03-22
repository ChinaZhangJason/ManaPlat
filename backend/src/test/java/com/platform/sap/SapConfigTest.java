package com.platform.sap;

import com.platform.sap.model.SapRfcConfig;
import com.platform.sap.model.SapApiConfig;
import com.platform.sap.model.SapWebserviceConfig;
import com.platform.sap.model.HanaConnectionConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SapConfigTest {

    @Test
    void testHanaConnectionConfig() {
        HanaConnectionConfig config = new HanaConnectionConfig();
        config.setId(1L);
        config.setConfigName("HANA生产库");
        config.setConfigCode("HANA_PROD");
        config.setHost("hana.example.com");
        config.setPort(30015);
        config.setDatabaseName("HXE");
        config.setUsername("SYSTEM");
        config.setPassword("encrypted_password");
        config.setSslEnabled(1);
        
        assertEquals("hana.example.com", config.getHost());
        assertEquals(30015, config.getPort());
        assertEquals("HXE", config.getDatabaseName());
        assertEquals(1, config.getSslEnabled());
    }

    @Test
    void testHanaConnectionPool() {
        HanaConnectionConfig config = new HanaConnectionConfig();
        config.setPoolSize(10);
        config.setPoolMinIdle(2);
        config.setConnectionTimeout(30000);
        config.setSocketTimeout(60000);
        
        assertEquals(10, config.getPoolSize());
        assertEquals(2, config.getPoolMinIdle());
        assertEquals(30000, config.getConnectionTimeout());
    }

    @Test
    void testSapRfcConfig() {
        SapRfcConfig config = new SapRfcConfig();
        config.setId(1L);
        config.setConfigName("SAP RFC连接");
        config.setConfigCode("SAP_RFC_001");
        config.setConnectionType("JCO");
        config.setHost("sap.example.com");
        config.setClient("100");
        config.setSystemNumber("00");
        config.setPoolSize(5);
        
        assertEquals("JCO", config.getConnectionType());
        assertEquals("100", config.getClient());
        assertEquals("00", config.getSystemNumber());
    }

    @Test
    void testSapApiConfig() {
        SapApiConfig config = new SapApiConfig();
        config.setId(1L);
        config.setConfigName("SAP OData API");
        config.setConfigCode("SAP_ODATA_001");
        config.setApiType("OData");
        config.setBaseUrl("https://sap.example.com/sap/opu/odata/sap/");
        config.setAuthType("OAUTH2");
        config.setTimeout(30000);
        config.setRetryCount(3);
        
        assertEquals("OData", config.getApiType());
        assertEquals("OAUTH2", config.getAuthType());
        assertEquals(30000, config.getTimeout());
        assertEquals(3, config.getRetryCount());
    }

    @Test
    void testSapWebserviceConfig() {
        SapWebserviceConfig config = new SapWebserviceConfig();
        config.setId(1L);
        config.setConfigName("SAP WebService");
        config.setConfigCode("SAP_WS_001");
        config.setWsdlUrl("https://sap.example.com/wsdl/service.wsdl");
        config.setServiceName("ZFM_SAMPLE_SERVICE");
        config.setPortName("ZFM_SAMPLE_PORT");
        config.setEndpointUrl("https://sap.example.com/soap/rpc");
        config.setAuthType("BASIC");
        
        assertEquals("ZFM_SAMPLE_SERVICE", config.getServiceName());
        assertEquals("BASIC", config.getAuthType());
    }

    @Test
    void testConnectionStringGeneration() {
        HanaConnectionConfig config = new HanaConnectionConfig();
        config.setHost("hana.example.com");
        config.setPort(30015);
        config.setDatabaseName("HXE");
        config.setTenantName("TENANT1");
        
        String connectionString = String.format("jdbc:sap://%s:%d/?databaseName=%s&tenantName=%s",
            config.getHost(), config.getPort(), config.getDatabaseName(), config.getTenantName());
        
        assertTrue(connectionString.contains("hana.example.com"));
        assertTrue(connectionString.contains("30015"));
        assertTrue(connectionString.contains("HXE"));
        assertTrue(connectionString.contains("TENANT1"));
    }

    @Test
    void testApiEndpointValidation() {
        SapApiConfig config = new SapApiConfig();
        config.setBaseUrl("https://api.example.com/v1");
        
        String baseUrl = config.getBaseUrl();
        boolean isValidUrl = baseUrl != null && 
                              (baseUrl.startsWith("http://") || baseUrl.startsWith("https://"));
        
        assertTrue(isValidUrl);
    }
}
