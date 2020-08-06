package com.jira.report.dto.agile;

import lombok.Data;

@Data
public class SprintDto {
    private int id;
    private String state;
    private String name;
    private String startDate;
    private String endDate;
}
