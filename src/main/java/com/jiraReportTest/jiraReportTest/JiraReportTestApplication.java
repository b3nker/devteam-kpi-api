package com.jiraReportTest.jiraReportTest;

import com.jiraReportTest.jiraReportTest.dao.api.JiraTempoAPI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JiraReportTestApplication {

	public static void main(String[] args) {
		int worklog = JiraTempoAPI.getWorklogByAccountID("5c17b4599f443a65fecae3ca","2020-07-22","2020-07-29");
		System.out.println("résultat du worklog: " + worklog);
		SpringApplication.run(JiraReportTestApplication.class, args);
	}

}
