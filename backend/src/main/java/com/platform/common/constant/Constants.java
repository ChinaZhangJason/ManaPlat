package com.platform.common.constant;

public class Constants {

    public static class Status {
        public static final int ENABLED = 1;
        public static final int DISABLED = 0;
    }

    public static class Alert {
        public static final String LEVEL_WARNING = "WARNING";
        public static final String LEVEL_ERROR = "ERROR";
        public static final String LEVEL_CRITICAL = "CRITICAL";

        public static final String STATUS_PENDING = "PENDING";
        public static final String STATUS_SENT = "SENT";
        public static final String STATUS_ACKED = "ACKED";
        public static final String STATUS_CLOSED = "CLOSED";
    }

    public static class AlertCondition {
        public static final String GT = "GT";
        public static final String LT = "LT";
        public static final String EQ = "EQ";
        public static final String NE = "NE";
        public static final String GE = "GE";
        public static final String LE = "LE";
        public static final String BETWEEN = "BETWEEN";
    }

    public static class DataSource {
        public static final String SQL = "SQL";
        public static final String HANA = "HANA";
        public static final String API = "API";
        public static final String WEBSOCKET = "WS";
        public static final String FILE = "FILE";
    }

    public static class ChartType {
        public static final String LINE = "LINE";
        public static final String BAR = "BAR";
        public static final String PIE = "PIE";
        public static final String SCATTER = "SCATTER";
        public static final String GAUGE = "GAUGE";
        public static final String TABLE = "TABLE";
        public static final String RADAR = "RADAR";
        public static final String HEATMAP = "HEATMAP";
    }

    public static class Workflow {
        public static final String STATE_DRAFT = "DRAFT";
        public static final String STATE_PENDING = "PENDING";
        public static final String STATE_APPROVED = "APPROVED";
        public static final String STATE_REJECTED = "REJECTED";
        public static final String STATE_COMPLETED = "COMPLETED";

        public static final String TASK_PENDING = "PENDING";
        public static final String TASK_COMPLETED = "COMPLETED";
    }

    public static class Auth {
        public static final String TOKEN_HEADER = "Authorization";
        public static final String TOKEN_PREFIX = "Bearer ";
        public static final int TOKEN_EXPIRE = 86400000;
        public static final int REFRESH_TOKEN_EXPIRE = 604800000;
    }

    public static class VerifyCode {
        public static final int CODE_LENGTH = 6;
        public static final int CODE_EXPIRE_MINUTES = 10;
        public static final String TYPE_SMS = "SMS";
        public static final String TYPE_EMAIL = "EMAIL";
        public static final String SCENE_LOGIN = "LOGIN";
        public static final String SCENE_REGISTER = "REGISTER";
        public static final String SCENE_RESET_PASSWORD = "RESET_PASSWORD";
    }

    public static class Response {
        public static final int SUCCESS_CODE = 200;
        public static final int ERROR_CODE = 500;
        public static final int UNAUTHORIZED_CODE = 401;
        public static final int FORBIDDEN_CODE = 403;
        public static final int NOT_FOUND_CODE = 404;
        public static final int BAD_REQUEST_CODE = 400;
    }

    public static class Redis {
        public static final String PREFIX = "manageplat:";
        public static final String TOKEN_PREFIX = PREFIX + "token:";
        public static final String USER_PREFIX = PREFIX + "user:";
        public static final String CACHE_PREFIX = PREFIX + "cache:";
        public static final String LOCK_PREFIX = PREFIX + "lock:";
    }
}
