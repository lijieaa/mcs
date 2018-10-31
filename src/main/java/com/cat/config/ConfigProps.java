package com.cat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 自定义配置
 */
@Component
@ConfigurationProperties(prefix="myprops")
public class ConfigProps {

    private String mqqtHost;

    public String getMqqtHost() {
        return mqqtHost;
    }

    public void setMqqtHost(String mqqtHost) {
        this.mqqtHost = mqqtHost;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    private String clientId;
    private String topic;
}
