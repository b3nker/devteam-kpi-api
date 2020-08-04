package com.jira.report.dao.api;

import com.jira.report.model.Release;
import com.jira.report.model.Sprint;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.jira.report.dao.api.API.*;
import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class ExternalFiles {
    static final Character SEPARATOR = ',';
    static final Character VALUE_COMMA = ',';
    static final Character VALUE_DOT = '.';
    static final int INDEX_ACC_ID = 2;
    static final int FIRST_ROW = 4;

    public ExternalFiles() {
    }

    /* Reads "planning.csv" and extract two data, the working time and the available time per collaborator
     * HashMap <AccountID,[workingTime, availableTime]>
     */
    public Map<String, Float[]> getPlanning(String PLANNING_PATH, Sprint s) {
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
            FileReader filereader = new FileReader(PLANNING_PATH);
            CSVParser parser = new CSVParserBuilder().withSeparator(SEPARATOR).build();
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withCSVParser(parser)
                    .build();
            for (int i = 0; i < FIRST_ROW; i++) {
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
                if (!infos[INDEX_ACC_ID].isEmpty()) {
                    accountId = infos[INDEX_ACC_ID];
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
            Release r = Release.builder()
                    .name(infos[0])
                    .startDate(startDate)
                    .endDate(endDate)
                    .nbOpenDays(parseInt(infos[3]))
                    .nbWorkingDays(parseDouble(infos[4].replace(VALUE_COMMA, VALUE_DOT)))
                    .buildCapacityFront(parseDouble(infos[5].replace(VALUE_COMMA, VALUE_DOT)))
                    .buildCapacityMiddle(parseDouble(infos[6].replace(VALUE_COMMA, VALUE_DOT)))
                    .buildCapacityTotal(parseDouble(infos[7].replace(VALUE_COMMA, VALUE_DOT)))
                    .build();
            releases.add(r);
        }
        return releases;
    }
}
