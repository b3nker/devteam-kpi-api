package com.jiraReportTest.jiraReportTest.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Release {
    private String name;
    private Date startDate;
    private Date endDate;
    private int nbOpenDays;
    private double nbWorkingDays;
    private double buildCapacityFront;
    private double buildCapacityMiddle;
    private double buildCapacityTotal;
}
