package com.kims.coreevent.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "event.jpa")
public class EventJPAProperties {

    private String ddlAuto;
    private boolean showSql;
    private String databasePlatform;
    private boolean formatSql;

    @Override
    public String toString() {
        return "EventJPAProperties{" +
                "hibernateDdlAuto='" + ddlAuto + '\'' +
                ", showSql=" + showSql +
                ", databasePlatform='" + databasePlatform + '\'' +
                ", formatSql=" + formatSql +
                '}';
    }

    public String getDdlAuto() {
        return ddlAuto;
    }

    public void setDdlAuto(String ddlAuto) {
        this.ddlAuto = ddlAuto;
    }

    public boolean isShowSql() {
        return showSql;
    }

    public void setShowSql(boolean showSql) {
        this.showSql = showSql;
    }

    public String getDatabasePlatform() {
        return databasePlatform;
    }

    public void setDatabasePlatform(String databasePlatform) {
        this.databasePlatform = databasePlatform;
    }

    public boolean isFormatSql() {
        return formatSql;
    }

    public void setFormatSql(boolean formatSql) {
        this.formatSql = formatSql;
    }
}
