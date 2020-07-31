package com.jiraReportTest.jiraReportTest;

import com.jiraReportTest.jiraReportTest.dao.api.API;
import com.jiraReportTest.jiraReportTest.dao.api.ExternalFiles;
import com.jiraReportTest.jiraReportTest.dao.api.JiraTempoAPI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JiraReportTestApplication {

	public static void main(String[] args) {
		ExternalFiles.getPlanning("planning.csv");
		SpringApplication.run(JiraReportTestApplication.class, args);
	}

}
