package com.jira.report.dao.api;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.jira.report.dto.sheets.GoogleSheetsDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoogleSheetsAPI {
    private static String SPEADSHEET_ID = "10sejSG58ufu9m5i-uJg4bzrFF_f3NEnzSX5TLAnIhxU";
    private static String RANGE = "Absences été!A1:BP45"; // NOM_FEUILLE!CASE_DEP:CASE_FIN
    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public static void getData() throws Exception {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Sheets sheetsService = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, GoogleSheetsDto.getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = sheetsService.spreadsheets().values()
                .get(SPEADSHEET_ID, RANGE)
                .execute();
        List<List<Object>> values = response.getValues();
        if(values == null || values.isEmpty()){
            System.out.println("No data found.");
        }else{
            for(List row: values){
                System.out.print(row.get(0));
            }
        }


    }
}
