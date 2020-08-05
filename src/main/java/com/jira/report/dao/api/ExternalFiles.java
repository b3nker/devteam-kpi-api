package com.jira.report.dao.api;

import com.jira.report.config.JiraReportConfigExternal;
import com.jira.report.model.Release;
import com.jira.report.model.Sprint;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.jira.report.dao.api.API.*;
import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

@Service
public class ExternalFiles {
    private final Character separator;
    private final int indexAccountId;
    private final int firstRow;

    public ExternalFiles(JiraReportConfigExternal jiraReportConfigExternal){
        this.separator = ',';
        this.indexAccountId = jiraReportConfigExternal.getIndexAccountId();
        this.firstRow = jiraReportConfigExternal.getFirstRow();
    }

    /* Reads "planning.csv" and extract two data, the working time and the available time per collaborator
     * HashMap <AccountID,[workingTime, availableTime]>
     */
    public Map<String, Float[]> getPlanning(String planningPath, Sprint s) {
        /*
         Variables
         */
        Map<String, Float[]> planning = new HashMap<>();
        String accountId;
        int startIndex = -1;
        int endIndex = -1;
        int todayIndex = -1;
        //Two constants giving the column containing information about accountId and the 1st date
        String[] dates;
        /*
        Logic
         */
        try {
            FileReader filereader = new FileReader(planningPath);
            CSVParser parser = new CSVParserBuilder().withSeparator(separator).build();
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withCSVParser(parser)
                    .build();
            for (int i = 0; i < firstRow; i++) {
                csvReader.readNext();
            }
            dates = csvReader.readNext();
            for (int i = 0; i < dates.length; i++) {
                if (s.getStartDate().format(dtfEurope).equals(dates[i])) {
                    startIndex = i;
                }
                if (s.getEndDate().format(dtfEurope).equals(dates[i])) {
                    endIndex = i;
                }
                if (TODAY.format(dtfEurope).equals(dates[i])) {
                    todayIndex = i;
                }
            }
            //On saute une ligne
            csvReader.readNext();
            String[] infos;
            while ((infos = csvReader.readNext()) != null) {
                if (!infos[indexAccountId].isEmpty()) {
                    accountId = infos[indexAccountId];
                    float totalWorkingTime = 8f * (endIndex - startIndex + 1);
                    float availableTime = 8f * (endIndex - todayIndex + 1);
                    for (int i = startIndex; i <= endIndex; i++) {
                        if (!infos[i].isEmpty()) {
                            totalWorkingTime -= parseFloat(infos[i].replace(",", ".")) * 8;
                        }
                    }
                    for (int i = todayIndex; i <= endIndex; i++) {
                        if (!infos[i].isEmpty()) {
                            availableTime -= parseFloat(infos[i].replace(",", ".")) * 8;
                        }
                    }
                    Float[] workTime = {totalWorkingTime,availableTime};
                    planning.put(accountId, workTime);
                    // System.out.println(accountId + " " + workTime[0] + " " + workTime[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return planning;
    }

    public List<Release> getReleases(String path) throws IOException, ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        int nbLinesToSkip = 2;
        char valueDot = '.';
        char valueComma = ',';
        List<Release> releases = new ArrayList<>();
        FileReader filereader = new FileReader(path);
        CSVParser parser = new CSVParserBuilder().withSeparator(separator).build();
        CSVReader csvReader = new CSVReaderBuilder(filereader)
                .withCSVParser(parser)
                .build();
        for (int i = 0; i < nbLinesToSkip; i++) {
            csvReader.readNext();
        }
        String[] infos;
        while ((infos = csvReader.readNext()) != null) {
            Date startDate = formatter.parse(infos[1]);
            Date endDate = formatter.parse(infos[2]);
            Release r = Release.builder()
                    .name(infos[0])
                    .startDate(startDate)
                    .endDate(endDate)
                    .nbOpenDays(parseInt(infos[3]))
                    .nbWorkingDays(parseDouble(infos[4].replace(valueComma, valueDot)))
                    .buildCapacityFront(parseDouble(infos[5].replace(valueComma, valueDot)))
                    .buildCapacityMiddle(parseDouble(infos[6].replace(valueComma, valueDot)))
                    .buildCapacityTotal(parseDouble(infos[7].replace(valueComma, valueDot)))
                    .build();
            releases.add(r);
        }
        return releases;
    }
}
