package com.example.backend.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlInjectionValidator {
    private final String SQL_TYPES = "TABLE, TABLESPACE, PROCEDURE, FUNCTION, TRIGGER, KEY, VIEW, MATERIALIZED VIEW, LIBRARY"
            + "DATABASE LINK, DBLINK, INDEX, CONSTRAINT, TRIGGER, USER, SCHEMA, DATABASE, PLUGGABLE DATABASE, BUCKET, "
            + "CLUSTER, COMMENT, SYNONYM, TYPE, JAVA, SESSION, ROLE, PACKAGE, PACKAGE BODY, OPERATOR"
            + "SEQUENCE, RESTORE POINT, PFILE, CLASS, CURSOR, OBJECT, RULE, USER, DATASET, DATASTORE, "
            + "COLUMN, FIELD, OPERATOR";

    private final String[] sqlRegexps = { "(?i)(.*)(\\b)+SELECT(\\b)+\\s.*(\\b)+FROM(\\b)+\\s.*(.*)",
            "(?i)(.*)(\\b)+INSERT(\\b)+\\s.*(\\b)+INTO(\\b)+\\s.*(.*)", "(?i)(.*)(\\b)+UPDATE(\\b)+\\s.*(.*)",
            "(?i)(.*)(\\b)+DELETE(\\b)+\\s.*(\\b)+FROM(\\b)+\\s.*(.*)", "(?i)(.*)(\\b)+UPSERT(\\b)+\\s.*(.*)",
            "(?i)(.*)(\\b)+SAVEPOINT(\\b)+\\s.*(.*)", "(?i)(.*)(\\b)+CALL(\\b)+\\s.*(.*)",
            "(?i)(.*)(\\b)+ROLLBACK(\\b)+\\s.*(.*)", "(?i)(.*)(\\b)+KILL(\\b)+\\s.*(.*)",
            "(?i)(.*)(\\b)+DROP(\\b)+\\s.*(.*)",
            "(?i)(.*)(\\b)+CREATE(\\b)+(\\s)*(" + this.SQL_TYPES.replaceAll(",", "|") + ")(\\b)+\\s.*(.*)",
            "(?i)(.*)(\\b)+ALTER(\\b)+(\\s)*(" + this.SQL_TYPES.replaceAll(",", "|") + ")(\\b)+\\s.*(.*)",
            "(?i)(.*)(\\b)+TRUNCATE(\\b)+(\\s)*(" + this.SQL_TYPES.replaceAll(",", "|") + ")(\\b)+\\s.*(.*)",
            "(?i)(.*)(\\b)+LOCK(\\b)+(\\s)*(" + this.SQL_TYPES.replaceAll(",", "|") + ")(\\b)+\\s.*(.*)",
            "(?i)(.*)(\\b)+UNLOCK(\\b)+(\\s)*(" + this.SQL_TYPES.replaceAll(",", "|") + ")(\\b)+\\s.*(.*)",
            "(?i)(.*)(\\b)+RELEASE(\\b)+(\\s)*(" + this.SQL_TYPES.replaceAll(",", "|") + ")(\\b)+\\s.*(.*)",
            "(?i)(.*)(\\b)+DESC(\\b)+(\\w)*\\s.*(.*)", "(?i)(.*)(\\b)+DESCRIBE(\\b)+(\\w)*\\s.*(.*)",
            "(.*)[\\s\\)]+(OR)[\\s\\(]+(.*)[=><]+(.*)",
            "(.*)[\\s\\)]+(OR)[\\s\\(]+(.*)([\\s\\(\\)](IN|EXISTS)[\\s\\(\\)])+(.*)", "(.*)(PRIOR|CONNECT\\s+BY)(.*)",
            "(.*)\\s(IN|EXISTS)\\s(.*)[\\(\\)]+(.*)", "(.*)\\s(ALL|ANY|SOME)\\s(.*)[\\(\\)]+(.*)",
            "(.*)\\s(BETWEEN)\\s(.*)\\s(AND)\\s(.*)", };

    private final String[] SQL_KEYS = new String[] { "SELECT", "UNION", "DUAL", "TABLE", "PROCEDURE", "FUNCTION",
            "TRIGGER", "MATERIALIZED VIEW", "LIBRARY", "DBLINK", "INDEX", "CONSTRAINT", "SCHEMA", "DATABASE", "BUCKET",
            "CLUSTER", "SYNONYM", "SESSION", "ROLE", "PACKAGE", "OPERATOR", "SEQUENCE", "RESTORE POINT", "PFILE",
            "CLASS", "CURSOR", "OBJECT", "RULE", "DATASET", "DATASTORE", "COLUMN", "FIELD", "INSERT", "DELETE",
            "SAVEPOINT", "DROP ", "ROLLBACK", "CREATE", "ALTER", "TRUNCATE", "RELEASE", "EXISTS", "BETWEEN",
            "CONNECT", "UPDATE", "UPSERT" };

    private final String[] SQL_KEYS_EXT = new String[] { "APEX_APPLICATION", "APEX_CUSTOM_AUTH", "APEX_ITEM",
            "APEX_UTIL", "CTX_ADM", "CTX_ANL", "CTX_CLS", "CTX_DDL", "CTX_DOC", "CTX_ENTITY", "CTX_OUTPUT", "CTX_QUERY",
            "CTX_REPORT", "CTX_THES", "CTX_ULEXER", "DBMS_ADDM", "DBMS_ADVANCED_REWRITE", "DBMS_ADVISOR", "DBMS_ALERT",
            "DBMS_AMD", "DBMS_APPLICATION_INFO", "DBMS_APPLY_ADM", "DBMS_APPLY_USER_AGENT", "DBMS_APP_CONT", "DBMS_AQ",
            "DBMS_AQADM", "DBMS_AQELM", "DBMS_AQIN", "DBMS_AQ_EXP_CMT_TIME_ TABLES", "DBMS_AQ_INV", "DBMS_ASH_INTERNAL",
            "DBMS_ASSERT", "DBMS_AUDIT_MGMT", "DBMS_AUDIT_UTIL", "DBMS_AUTO_REPORT", "DBMS_AUTO_SQLTUNE",
            "DBMS_AUTO_TASK_ADMIN", "DBMS_AW_STATS", "DBMS_AW_XML", "DBMS_CAPTURE_ADM", "DBMS_CDC_DPUTIL",
            "DBMS_CDC_EXPDP", "DBMS_CDC_EXPVD", "DBMS_CDC_IMPDP", "DBMS_CDC_IPUBLISH", "DBMS_CDC_ISUBSCRIBE",
            "DBMS_CDC_PUBLISH", "DBMS_CDC_SUBSCRIBE", "DBMS_CDC_SYS_IPUBLISH", "DBMS_CDC_UTILITY",
            "DBMS_CHANGE_NOTIFICATION", "DBMS_COMPARISON", "DBMS_COMPRESSION", "DBMS_CONNECTION_POOL",
            "DBMS_CQ_NOTIFICATION", "DBMS_CREDENTIAL", "DBMS_CRYPTO", "DBMS_CRYPT_FFI", "DBMS_CSX_ADMIN", "DBMS_CUBE",
            "DBMS_CUBE_ADVISE", "DBMS_CUBE_LOG", "DBMS_DATAPUMP", "DBMS_DATAPUMP_UTL", "DBMS_DATA_MINING",
            "DBMS_DATA_MINING_TRANSFORM", "DBMS_DBCOMP", "DBMS_DBFS_CONTENT", "DBMS_DBFS_CONTENT_SPI", "DBMS_DBFS_HS",
            "DBMS_DBFS_SFS", "DBMS_DBLINK", "DBMS_DBUPGRADE", "DBMS_DBVERIFY", "DBMS_DB_VERSION", "DBMS_DDL",
            "DBMS_DEBUG", "DBMS_DEBUG_JDWP", "DBMS_DEBUG_JDWP_CUSTOM", "DBMS_DEFER", "DBMS_DEFER_QUERY",
            "DBMS_DEFER_SYS", "DBMS_DESCRIBE", "DBMS_DG", "DBMS_DIMENSION", "DBMS_DISTRIBUTED_TRUST_ADMIN", "DBMS_DNFS",
            "DBMS_DRS", "DBMS_DST", "DBMS_EDITIONS_UTILITIES", "DBMS_EPG", "DBMS_ERRLOG", "DBMS_FBT",
            "DBMS_FEATURE_USAGE", "DBMS_FEATURE_USAGE_INTERNAL", "DBMS_FEATURE_USAGE_REPORT", "DBMS_FGA",
            "DBMS_FILE_GROUP", "DBMS_FILE_TRANSFER", "DBMS_FLASHBACK", "DBMS_FLASHBACK_ARCHIVE",
            "DBMS_FREQUENT_ITEMSET", "DBMS_FS", "DBMS_GOLDENGATE_ADM", "DBMS_GOLDENGATE_AUTH", "DBMS_HADOOP",
            "DBMS_HANG_MANAGER", "DBMS_HEAT_MAP", "DBMS_HIERARCHY", "DBMS_HM", "DBMS_HPROF", "DBMS_HS_PARALLEL",
            "DBMS_HS_PASSTHROUGH", "DBMS_ILM", "DBMS_ILM_ADMIN", "DBMS_INDEX_UTL", "DBMS_INMEMORY",
            "DBMS_INMEMORY_ADMIN", "DBMS_INTERNAL_SAFE_SCN", "DBMS_IOT", "DBMS_ISCHED", "DBMS_I_INDEX_UTL", "DBMS_JAVA",
            "DBMS_JAVA_DUMP", "DBMS_JMS_PLSQL", "DBMS_JOB", "DBMS_JSON", "DBMS_LDAP", "DBMS_LDAP_UTL", "DBMS_LIBCACHE",
            "DBMS_LOB", "DBMS_LOCK", "DBMS_LOGMNR", "DBMS_LOGMNR_D", "DBMS_LOGREP_UTIL_INVOK", "DBMS_LOGSTDBY",
            "DBMS_LOGSTDBY_CONTEXT", "DBMS_LOGSTDBY_PUBLIC", "DBMS_METADATA", "DBMS_METADATA_DIFF", "DBMS_MGD_ID_UTL",
            "DBMS_MGWADM", "DBMS_MGWMSG", "DBMS_MONITOR", "DBMS_MVIEW", "DBMS_MVIEW_STATS", "DBMS_NETWORK_ACL_ADMIN",
            "DBMS_NETWORK_ACL_UTILITY", "DBMS_OBFUSCATION_TOOLKIT", "DBMS_ODCI", "DBMS_OFFLINE_OG",
            "DBMS_OFFLINE_SNAPSHOT", "DBMS_OUTLN", "DBMS_OUTLN_EDIT", "DBMS_OUTPUT", "DBMS_PARALLEL_EXECUTE",
            "DBMS_PART", "DBMS_PCLXUTIL", "DBMS_PDB", "DBMS_PDB_ALTER_SHARING", "DBMS_PERF", "DBMS_PIPE",
            "DBMS_PLSQL_CODE_COVERAGE", "DBMS_PREDICTIVE_ANALYTICS", "DBMS_PREPROCESSOR", "DBMS_PRIVILEGE_CAPTURE",
            "DBMS_PROCESS", "DBMS_PROFILER", "DBMS_PROPAGATION_ADM", "DBMS_PRVT_TRACE", "DBMS_QOPATCH", "DBMS_RANDOM",
            "DBMS_RECTIFIER_DIFF", "DBMS_REDACT", "DBMS_REDEFINITION", "DBMS_REFRESH", "DBMS_REGISTRY_SERVER",
            "DBMS_REPAIR", "DBMS_REPCAT", "DBMS_REPCAT_ADMIN", "DBMS_REPCAT_EXP", "DBMS_REPCAT_INSTANTIATE",
            "DBMS_REPCAT_MIGRATION", "DBMS_REPCAT_RGT", "DBMS_REPCAT_SNA", "DBMS_REPUTIL", "DBMS_RESCONFIG",
            "DBMS_RESOURCE_MANAGER", "DBMS_RESOURCE_MANAGER_PRIVS", "DBMS_RESULT_CACHE", "DBMS_RESUMABLE", "DBMS_RLS",
            "DBMS_ROLLING", "DBMS_ROWID", "DBMS_RULE", "DBMS_RULE_ADM", "DBMS_RULE_UTLI", "DBMS_SCHEDULER",
            "DBMS_SCHED_CLASS_EXPORT", "DBMS_SCHED_EXPORT_CALLOUTS", "DBMS_SCHED_JOB_EXPORT", "DBMS_SCHED_MAIN_EXPORT",
            "DBMS_SCHED_PROGRAM_EXPORT", "DBMS_SCHED_SCHEDULE_EXPORT", "DBMS_SCHED_WINDOW_EXPORT",
            "DBMS_SCHED_WINGRP_EXPOR", "DBMS_SCHEMA_COPY", "DBMS_SERVER_ALERT", "DBMS_SERVER_ALERT_EXPORT",
            "DBMS_SERVICE", "DBMS_SESSION", "DBMS_SFW_ACL_ADMIN", "DBMS_SHARED_POOL", "DBMS_SNAPSHOT", "DBMS_SPACE",
            "DBMS_SPACE_ADMIN", "DBMS_SPD", "DBMS_SPM", "DBMS_SQL", "DBMS_SQLDIAG", "DBMS_SQLPA", "DBMS_SQLTUNE",
            "DBMS_SQLTUNE_INTERNAL", "DBMS_SQL_MONITOR", "DBMS_SQL_TRANSLATOR", "DBMS_STATS", "DBMS_STAT_FUNCS",
            "DBMS_STAT_FUNCS_AUX", "DBMS_STORAGE_MAP", "DBMS_STREAMS", "DBMS_STREAMS_ADM", "DBMS_STREAMS_ADM_UTL_INVOK",
            "DBMS_STREAMS_ADVISOR_ADM", "DBMS_STREAMS_AUTH", "DBMS_STREAMS_CDC_ADM", "DBMS_STREAMS_DATAPUMP",
            "DBMS_STREAMS_DATAPUMP_UTIL", "DBMS_STREAMS_HANDLER_ADM", "DBMS_STREAMS_LCR_INT", "DBMS_STREAMS_MESSAGING",
            "DBMS_STREAMS_PUB_RPC", "DBMS_STREAMS_RPC", "DBMS_STREAMS_RPC_INTERNAL", "DBMS_STREAMS_TABLESPACE_ ADM",
            "DBMS_STREAMS_TABLESPACE_ADM", "DBMS_STREAMS_TBS_INT", "DBMS_STREAMS_TBS_INT_INVOK", "DBMS_SUMMARY",
            "DBMS_SUM_RWEQ_EXPORT", "DBMS_SUM_RWEQ_EXPORT_ INTERNAL", "DBMS_SUPPORT", "DBMS_SWRF_INTERNAL",
            "DBMS_SWRF_REPORT_INTERNAL", "DBMS_SYNC_REFRESH", "DBMS_SYSTEM", "DBMS_TDB", "DBMS_TNS", "DBMS_TRACE",
            "DBMS_TRANSACTION", "DBMS_TRANSFORM", "DBMS_TRANSFORM_INTERNAL", "DBMS_TSDP_MANAGE", "DBMS_TSDP_PROTECT",
            "DBMS_TTS", "DBMS_TYPES", "DBMS_UMF", "DBMS_UNDO_ADV", "DBMS_UPGRADE_INTERNAL", "DBMS_UTILITY",
            "DBMS_WARNING", "DBMS_WARNING_INTERNAL", "DBMS_WM", "DBMS_WORKLOAD_CAPTURE", "DBMS_WORKLOAD_REPLAY",
            "DBMS_WORKLOAD_REPOSITORY", "DBMS_XA", "DBMS_XDB", "DBMS_XDBRESOURCE", "DBMS_XDBT", "DBMS_XDBZ",
            "DBMS_XDB_ADMIN", "DBMS_XDB_CONFIG", "DBMS_XDB_CONSTANTS", "DBMS_XDB_REPOS", "DBMS_XDB_VERSION",
            "DBMS_XEVENT", "DBMS_XMLDOM", "DBMS_XMLGEN", "DBMS_XMLINDEX", "DBMS_XMLPARSER", "DBMS_XMLQUERY",
            "DBMS_XMLSAVE", "DBMS_XMLSCHEMA", "DBMS_XMLSCHEMA_ANNOTATE", "DBMS_XMLSTORAGE_MANAGE", "DBMS_XMLSTORE",
            "DBMS_XMLTRANSLATIONS", "DBMS_XPLAN", "DBMS_XSLPROCESSOR", "DBMS_XSOQ", "DBMS_XSOQ_ODBO", "DBMS_XSOQ_UTIL",
            "DBMS_XSTREAM_ADM", "DBMS_XSTREAM_AUTH", "DEBUG_EXTPROC", "ORD_AUDIO", "ORD_DICOM",
            "ORD_DICOM_ADMIN", "ORD_DOC", "ORD_IMAGE", "ORD_VIDEO", "OUTLN_EDIT_PKG", "OUTLN_PKG", "OWA", "OWA_CACHE",
            "OWA_COOKIE", "OWA_CUSTOM", "OWA_IMAGE", "OWA_OPT_LOCK", "OWA_PATTERN", "OWA_SEC", "OWA_TEXT", "OWA_UTIL",
            "SDO_CS", "SDO_CSW_PROCESS", "SDO_GCDR", "SDO_GEOM", "SDO_GEOR", "SDO_GEOR_ADMIN", "SDO_GEOR_AGGR",
            "SDO_GEOR_RA", "SDO_GEOR_UTL", "SDO_LRS", "SDO_MIGRATE", "SDO_NET", "SDO_NFE", "SDO_OLS", "SDO_PC_PKG",
            "SDO_SAM", "SDO_TIN_PKG", "SDO_TOPO", "SDO_TOPO_MAP", "SDO_TUNE", "SDO_UTIL", "SDO_WFS_LOCK",
            "SDO_WFS_PROCESS", "SEM_APIS", "SEM_OLS", "SEM_PERF", "SEM_RDFCTX", "SEM_RDFSA", "URIFACTORY",
            "UTL_CALL_STACK", "UTL_COLL", "UTL_COMPRESS", "UTL_ENCODE", "UTL_FILE", "UTL_GDK", "UTL_HTTP", "UTL_I18N",
            "UTL_IDENT", "UTL_INADDR", "UTL_LMS", "UTL_MAIL", "UTL_MATCH", "UTL_NLA", "UTL_RAW", "UTL_RECOMP",
            "UTL_REF", "UTL_SMTP", "UTL_SPADV", "UTL_TCP", "UTL_URL", "UTL_XML", "WPG_DOCLOAD", "CTXSYS", "CTXAPP" };

    public boolean validate(String value) {
        if (!(value == null || value.trim() == "")) {
            String testValue = value.toUpperCase().replace("'", "").replace("||", "");
            if (Arrays.stream(this.SQL_KEYS).anyMatch(testValue::contains)
                    || Arrays.stream(this.SQL_KEYS_EXT).anyMatch(testValue::contains)) {
                return false;
            } else {
                List<Pattern> validationPatterns = this.getValidationPatterns();
                for (Pattern pattern : validationPatterns) {
                    Matcher matcher = pattern.matcher(value);
                    if (matcher.matches()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Map<String, Object> checkInvalid(String value) {
        if (!(value == null || value.trim().length() == 0)) {
            Map<String, Object> result = new HashMap<>();
            List<String> keys = new ArrayList<String>();
            String valueLower = value.toLowerCase();
            for (String key : this.SQL_KEYS) {
                if (valueLower.contains(key.toLowerCase())) {
                    keys.add(key);
                }
            }
            for (String key : this.SQL_KEYS_EXT) {
                if (valueLower.contains(key.toLowerCase())) {
                    keys.add(key);
                }
            }
            if (keys.size() > 0) {
                result.put("key-words", keys);
            }
            List<String> patterns = new ArrayList<String>();
            List<Pattern> validationPatterns = this.getValidationPatterns();
            for (Pattern pattern : validationPatterns) {
                Matcher matcher = pattern.matcher(value);
                if (matcher.matches()) {
                    patterns.add(matcher.pattern().toString());
                }
            }
            if (patterns.size() > 0) {
                result.put("reg-exs", patterns);
            }
            return result.size() == 0 ? null : result;
        }
        return null;
    }

    private List<Pattern> getValidationPatterns() {
        List<Pattern> patterns = new ArrayList<Pattern>();
        for (String sqlExpression : sqlRegexps) {
            Pattern pattern = Pattern.compile(sqlExpression,
                    Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.DOTALL);
            patterns.add(pattern);
        }
        return patterns;
    }
}
