package com.jira.report.dto.jira;

import lombok.Data;

@Data
public class AssigneeDto {
    private String accountId;
    private String emailAddress;
    private String displayName;

}
