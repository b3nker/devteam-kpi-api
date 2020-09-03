package com.jira.report.dto.agile;

import lombok.Data;

@Data
public class SprintDto {
    private Long id;
    private String state;
    private String name;
    private String startDate;
    private String endDate;
}
