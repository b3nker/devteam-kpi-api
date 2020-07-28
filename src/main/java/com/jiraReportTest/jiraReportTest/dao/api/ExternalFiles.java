package com.jiraReportTest.jiraReportTest.dao.api;

import com.jiraReportTest.jiraReportTest.model.Release;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.jiraReportTest.jiraReportTest.dao.api.API.SPRINT_ACTIF;
import static com.jiraReportTest.jiraReportTest.dao.api.API.TODAY;
import static com.jiraReportTest.jiraReportTest.dao.api.API.dtf;
import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class ExternalFiles {
    final static Character SEPARATOR = ',';
    final static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    /* Reads "planning.csv" and extract two data, the working time and the available time per collaborator
     * HashMap <AccountID,[workingTime, availableTime]>
     */
    public static HashMap<String, Float[]> getPlanning(String PLANNING_PATH) {
        /*
         Variables
         */
        HashMap<String, Float[]> planning = new HashMap<>();
        float totalWorkingTime;
        float availableTime;
        String accountId;
        int startIndex = -1;
        int endIndex = -1;
        int todayIndex = -1;
        //Two constants giving the column containing information about accountId and the 1st date
        final int INDEX_ACC_ID = 2;
        int FIRST_ROW = 4;
        Float[] workTime = new Float[2];
        String[] dates;
        /*
        Logic
         */
        try {
            FileReader filereader = new FileReader(PLANNING_PATH);
            CSVParser parser = new CSVParserBuilder().withSeparator(SEPARATOR).build();
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withCSVParser(parser)
                    .build();
            for (int i = 0; i < 4; i++) {
                csvReader.readNext();
            }
            dates = csvReader.readNext();
            for (int i = 0; i < dates.length; i++) {
                if (SPRINT_ACTIF.getStartDate().format(dtf).equals(dates[i])) {
                    startIndex = i;
                }
                if (SPRINT_ACTIF.getEndDate().format(dtf).equals(dates[i])) {
                    endIndex = i;
                }
                if (TODAY.equals(dates[i])) {
                    todayIndex = i;
                }
            }
            // When the initial date is not contained in the CSV
            if (startIndex < 0) {
                startIndex = FIRST_ROW;
            }
            //On saute une ligne
            csvReader.readNext();
            String[] infos;
            while ((infos = csvReader.readNext()) != null) {
                if (!infos[INDEX_ACC_ID].isEmpty()) {
                    accountId = infos[INDEX_ACC_ID];
                    totalWorkingTime = 8 * (endIndex - startIndex + 1);
                    availableTime = 8 * (endIndex - todayIndex + 1);
                    for (int i = startIndex; i <= endIndex; i++) {
                        if (!infos[i].isEmpty()) {
                            totalWorkingTime -= parseFloat(infos[i]) * 8;
                        }
                    }
                    for (int i = todayIndex; i <= endIndex; i++) {
                        if (!infos[i].isEmpty()) {
                            availableTime -= parseFloat(infos[i]) * 8;
                        }
                    }
                    workTime[0] = totalWorkingTime;
                    workTime[1] = availableTime;
                    planning.put(accountId, workTime);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return planning;
    }

    public static List<Release> getReleases(String path) throws IOException, ParseException {
        int nbLinesToSkip = 2;
        List<Release> releases = new ArrayList<>();
        FileReader filereader = new FileReader(path);
        CSVParser parser = new CSVParserBuilder().withSeparator(SEPARATOR).build();
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
            Double nbWorkingDays = parseDouble(infos[4]);
            Release r = Release.builder()
                    .name(infos[0])
                    .startDate(startDate)
                    .endDate(endDate)
                    .nbOpenDays(parseInt(infos[3]))
                    .nbWorkingDays(nbWorkingDays)
                    .buildCapacityFront(parseDouble(infos[5]))
                    .buildCapacityMiddle(parseDouble(infos[6]))
                    .buildCapacityTotal(parseDouble(infos[7]))
                    .build();
            releases.add(r);
        }
        return releases;
    }
}
