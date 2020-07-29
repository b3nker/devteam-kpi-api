package com.jiraReportTest.jiraReportTest.dto.jiraApi;

import lombok.Data;

@Data
public class FieldsDto {
    private AssigneeDto assignee;
    private StatusDto status;
    private int timeoriginalestimate;
    private int timeestimate;
    private int timespent;
    private double customfield_10005; // story points
}
