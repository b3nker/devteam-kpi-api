package com.jira.report.config;
import lombok.Data;
import org.apache.commons.lang3.RegExUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@ConfigurationProperties("jira.report.individuals")
@Data
public class JiraReportConfigIndividuals {
    private Map<String, String> idCollabs;
    private String teamNameOne;
    private String teamNameTwo;
    private String teamNameThree;
    private List<String> teamOne;
    private List<String> teamTwo;
    private List<String> teamThree;

    /**
     * Creates a Map (key: accountId, value: Collaborator's role) by replacing collected data in "idCollabs" from application.yml
     * Replace  all '_' with ':' in keys
     * @return A map
     */
    public Map<String, String> getCollabs(){
        return idCollabs.entrySet().stream()
                .collect(Collectors.toMap(entry -> RegExUtils.replaceFirst(entry.getKey(),"_",":"), Map.Entry::getValue));
    }
}
