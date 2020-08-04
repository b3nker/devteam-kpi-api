package com.jira.report.config;

import lombok.Data;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@ConfigurationProperties("jira.report")
@Data
public class JiraReportConfig {
    private Map<String, String> idCollabs;
    private List<String> status;
    private List<String> teamAlpha;
    private List<String> teamBeta;
    private List<String> teamGamma;
    private String baseUrl;
    public Map<String, String> getCollabs(){
        return idCollabs.entrySet().stream()
                .collect(Collectors.toMap(entry -> RegExUtils.replaceFirst(entry.getKey(),"_",":"), Map.Entry::getValue));
    }
}
