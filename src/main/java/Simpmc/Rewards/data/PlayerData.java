package Simpmc.Rewards.data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerData {

    private UUID uuid;

    private long totalMinutes;
    private long dailyMinutes;
    private long weeklyMinutes;

    private int streakDays;

    private String lastSignDate = "";

    private boolean dailyClaimed;
    private boolean weeklyClaimed;

    private Set<Integer> claimedTotalRewards = new HashSet<>();
    private Set<Integer> claimedStreakRewards = new HashSet<>();

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public long getTotalMinutes() {
        return totalMinutes;
    }

    public void addTotalMinutes(long amount) {
        this.totalMinutes += amount;
    }

    public long getDailyMinutes() {
        return dailyMinutes;
    }

    public void addDailyMinutes(long amount) {
        this.dailyMinutes += amount;
    }

    public long getWeeklyMinutes() {
        return weeklyMinutes;
    }

    public void addWeeklyMinutes(long amount) {
        this.weeklyMinutes += amount;
    }

    public int getStreakDays() {
        return streakDays;
    }

    public void setStreakDays(int streakDays) {
        this.streakDays = streakDays;
    }

    public String getLastSignDate() {
        return lastSignDate;
    }

    public void setLastSignDate(String lastSignDate) {
        this.lastSignDate = lastSignDate;
    }

    public boolean isDailyClaimed() {
        return dailyClaimed;
    }

    public void setDailyClaimed(boolean dailyClaimed) {
        this.dailyClaimed = dailyClaimed;
    }

    public boolean isWeeklyClaimed() {
        return weeklyClaimed;
    }

    public void setWeeklyClaimed(boolean weeklyClaimed) {
        this.weeklyClaimed = weeklyClaimed;
    }

    public Set<Integer> getClaimedTotalRewards() {
        return claimedTotalRewards;
    }

    public Set<Integer> getClaimedStreakRewards() {
        return claimedStreakRewards;
    }
}
