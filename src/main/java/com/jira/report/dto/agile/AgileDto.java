package com.jira.report.dto.agile;

import lombok.Data;

@Data
public class AgileDto {
    private int maxResuts;
    private int startAt;
    private SprintDto[] values;
}
