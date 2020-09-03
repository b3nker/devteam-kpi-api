package com.jira.report.model.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Getter
@Document(collection = "release")
public class ReleaseEntity {
    private String name;
    private Date startDate;
    private Date endDate;
    private int nbOpenDays;
    private double nbWorkingDays;
    private double buildCapacityFront;
    private double buildCapacityMiddle;
    private double buildCapacityTotal;
}
