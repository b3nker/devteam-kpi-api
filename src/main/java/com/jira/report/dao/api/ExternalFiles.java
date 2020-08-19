package com.jira.report.dao.api;

import com.jira.report.config.JiraReportConfigExternal;
import com.jira.report.model.Release;
import com.jira.report.model.Sprint;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
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
    private static final Character SEPARATOR = ',';
    private static final int INDEX_ACCOUNT_ID = 2;
    private static final int FIRST_ROW = 4;
    private static final String RUN_CHAR = "R";

    public ExternalFiles(){}

    /**
     * Creates a Map (key: accountId, value: [totalWorkingTime, availableTime])
     * @param planningPath path to "planning.csv"
     * @param sprint Sprint to get precise start and end date for each team (as a team is assigned to a sprint)
     * @return A Map containing, for each collaborator, his number of hours on the sprint, and left time as of today
     * @throws IOException, If "planning.csv" cannot be found.
     */
    public Map<String, Float[]> getPlanning(String planningPath, Sprint sprint) {
        /*
         Variables
         */
        Map<String, Float[]> planning = new HashMap<>();
        int NOT_FOUND_START_INDEX = 4;
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
            CSVParser parser = new CSVParserBuilder().withSeparator(SEPARATOR).build();
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withCSVParser(parser)
                    .build();
            for (int i = 0; i < FIRST_ROW; i++) {
                csvReader.readNext();
            }
            dates = csvReader.readNext();
            for (int i = 0; i < dates.length; i++) {
                if (sprint.getStartDate().format(dtfEurope).equals(dates[i])) {
                    startIndex = i;
                }
                if (sprint.getEndDate().format(dtfEurope).equals(dates[i])) {
                    endIndex = i;
                }
                if (TODAY.format(dtfEurope).equals(dates[i])) {
                    todayIndex = i;
                }
            }
            //Si la date de début n'est pas trouvé
            if(startIndex < 0){
                startIndex = NOT_FOUND_START_INDEX;
            }
            if(endIndex < 0){
                endIndex = NOT_FOUND_START_INDEX;
            }
            if(todayIndex < 0){
                todayIndex = NOT_FOUND_START_INDEX;
            }
            //On saute une ligne
            csvReader.readNext();
            String[] infos;
            while ((infos = csvReader.readNext()) != null) {
                if (!infos[INDEX_ACCOUNT_ID].isEmpty()) {
                    accountId = infos[INDEX_ACCOUNT_ID];
                    float totalWorkingTime = 8f * (endIndex - startIndex);
                    float availableTime = 8f * (endIndex - todayIndex);
                    for (int i = startIndex; i <= endIndex; i++) {
                        if (!infos[i].isEmpty() && !infos[i].contains(RUN_CHAR)) {
                            totalWorkingTime -= parseFloat(infos[i].replace(",", ".")) * 8;
                        }
                    }
                    for (int i = todayIndex; i <= endIndex; i++) {
                        if (!infos[i].isEmpty() && !infos[i].contains(RUN_CHAR)) {
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

    /**
     *Creates a list of Release objects.
     * @param path path to "releases.csv"
     * @return A list containing releases data
     * @throws IOException, If file cannot be found
     * @throws ParseException, If file cannot be parsed (illegal character)
     */
    public List<Release> getReleases(String path) throws IOException, ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        int nbLinesToSkip = 2;
        char valueDot = '.';
        char valueComma = ',';
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
