package com.jira.report.dto.jiraApi;

import lombok.Data;

@Data
public class AssigneeDto {
    private String accountId;
    private String emailAddress;
    private String displayName;

}
