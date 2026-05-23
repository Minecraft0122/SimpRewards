package com.example.rewards;

import java.util.HashSet;
import java.util.Set;

public class PlayerRewardsData {
    
    private final String uuid;
    private long lastSignTime;
    private int signStreak;
    private int totalSeconds;
    private int dailySeconds;
    private int weeklySeconds;
    private int consecutiveDays;
    private String lastActiveDate;
    private Set<String> claimedMilestones;
    private Set<String> claimedConsecutive;
    private String dailyClaimedDate;
    private String weeklyClaimedWeek;
    
    public PlayerRewardsData(String uuid) {
        this.uuid = uuid;
        this.claimedMilestones = new HashSet<>();
        this.claimedConsecutive = new HashSet<>();
        this.lastActiveDate = getCurrentDate();
    }
    
    // Getters and setters
    public String getUuid() { return uuid; }
    public long getLastSignTime() { return lastSignTime; }
    public void setLastSignTime(long lastSignTime) { this.lastSignTime = lastSignTime; }
    public int getSignStreak() { return signStreak; }
    public void setSignStreak(int signStreak) { this.signStreak = signStreak; }
    public int getTotalSeconds() { return totalSeconds; }
    public void setTotalSeconds(int totalSeconds) { this.totalSeconds = totalSeconds; }
    public int getDailySeconds() { return dailySeconds; }
    public void setDailySeconds(int dailySeconds) { this.dailySeconds = dailySeconds; }
    public int getWeeklySeconds() { return weeklySeconds; }
    public void setWeeklySeconds(int weeklySeconds) { this.weeklySeconds = weeklySeconds; }
    public int getConsecutiveDays() { return consecutiveDays; }
    public void setConsecutiveDays(int consecutiveDays) { this.consecutiveDays = consecutiveDays; }
    public String getLastActiveDate() { return lastActiveDate; }
    public void setLastActiveDate(String lastActiveDate) { this.lastActiveDate = lastActiveDate; }
    public Set<String> getClaimedMilestones() { return claimedMilestones; }
    public void setClaimedMilestones(Set<String> claimedMilestones) { this.claimedMilestones = claimedMilestones; }
    public Set<String> getClaimedConsecutive() { return claimedConsecutive; }
    public void setClaimedConsecutive(Set<String> claimedConsecutive) { this.claimedConsecutive = claimedConsecutive; }
    public String getDailyClaimedDate() { return dailyClaimedDate; }
    public void setDailyClaimedDate(String dailyClaimedDate) { this.dailyClaimedDate = dailyClaimedDate; }
    public String getWeeklyClaimedWeek() { return weeklyClaimedWeek; }
    public void setWeeklyClaimedWeek(String weeklyClaimedWeek) { this.weeklyClaimedWeek = weeklyClaimedWeek; }
    
    private String getCurrentDate() {
        java.time.LocalDate date = java.time.LocalDate.now();
        return date.toString(); // YYYY-MM-DD
    }
    
    public String getCurrentWeek() {
        java.time.LocalDate date = java.time.LocalDate.now();
        java.time.temporal.WeekFields weekFields = java.time.temporal.WeekFields.of(java.util.Locale.getDefault());
        int weekNumber = date.get(weekFields.weekOfWeekBasedYear());
        int year = date.get(weekFields.weekBasedYear());
        return year + "-W" + (weekNumber < 10 ? "0" + weekNumber : weekNumber);
    }
}
