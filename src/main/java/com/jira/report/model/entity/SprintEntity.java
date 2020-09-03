package com.jira.report.model.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Getter
@Builder
@Document(collection = "sprint")
public class SprintEntity {

    @Id
    private Long id;
    private String name;
    private LocalDateTime startDate;
    private int timeLeft; // in hours without considering public holidays
    private int totalTime; // in hours without considering public holidays
    private LocalDateTime endDate;
    private TeamEntity team;
    private double addedWork;
    private TicketEntity addedTickets;

}
