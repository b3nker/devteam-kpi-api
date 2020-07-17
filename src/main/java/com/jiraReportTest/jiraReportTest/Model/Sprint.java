package com.jiraReportTest.jiraReportTest.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.lang.Math.toIntExact;

public class Sprint {
    private String name;
    private LocalDateTime startDate;
    private int timeLeft; // in hours without considering public holidays
    private int totalTime; // in hours without considering public holidays
    private LocalDateTime endDate;
    //private HashMap<String,Team> teams;
    private Team[] teams;
    public Sprint() {}

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Team[] getTeams() {
        return teams;
    }

    public void setTeams(Team[] teams) {
        this.teams = teams;
    }

    public String toString(){
        return ("name : " + this.name +
                ", start : " + this.startDate.toString() +
                ", end : " + this.endDate.toString() +
                ", timeLeft : " + this.timeLeft +
                ", totalTime : " + this.totalTime
        );
    }


    /*
    Return the number of working days between 2 LocalDateTime
     */
    public static int getWorkingDays(LocalDateTime start, LocalDateTime end){
        int nbWorkingDays = 0;
        LocalDateTime ldt = start;
        while(ldt.isBefore(end)){
            DayOfWeek dow = ldt.getDayOfWeek();
            if(dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY){
                nbWorkingDays++;
            }
            ldt = ldt.plusDays(1);
        }
        return nbWorkingDays;
    }

    /*
    Convert a string with the following format: "2020-03-27T08:59:12.342Z"
    to a LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(String date){
        int year = parseInt(date.substring(0,4));
        int month = parseInt(date.substring(5,7));
        int dayOfMonth = parseInt(date.substring(8,10));
        int hours = parseInt(date.substring(11,13));
        int minutes = parseInt(date.substring(14,16));
        int seconds = parseInt(date.substring(17,19));
        return LocalDateTime.of(year, month, dayOfMonth, hours, minutes, seconds);
    }

    /*
    Hypothesis : There are 2 weekends during a sprint
     */
    public static int durationOfSprint(LocalDateTime start, LocalDateTime end){
        int workingDays = Sprint.getWorkingDays(start, end);
        int hours = workingDays*8;
        return hours;
    }


    public static int timeLeftOnSprint(LocalDateTime end){
        int workingDays = Sprint.getWorkingDays(LocalDateTime.now(), end);
        int hours = workingDays*8;
        return toIntExact(hours);
    }

}
