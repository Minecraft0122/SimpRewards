package com.example.rewards;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ConfigManager {
    
    private final RewardsPlugin plugin;
    private String databaseType;
    private String mysqlHost;
    private int mysqlPort;
    private String mysqlDatabase;
    private String mysqlUser;
    private String mysqlPassword;
    private boolean useRedis;
    
    public ConfigManager(RewardsPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void setup() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveDefaultConfig();
        }
        
        FileConfiguration config = plugin.getConfig();
        
        // 默认配置
        config.addDefault("database.type", "sqlite");
        config.addDefault("database.mysql.host", "localhost");
        config.addDefault("database.mysql.port", 3306);
        config.addDefault("database.mysql.database", "minecraft");
        config.addDefault("database.mysql.user", "root");
        config.addDefault("database.mysql.password", "");
        config.addDefault("database.use_redis", false);
        
        // 奖励配置
        config.addDefault("rewards.daily.sign_in", Arrays.asList("money:100", "item:DIAMOND:1"));
        config.addDefault("rewards.daily.streak.3", Arrays.asList("money:500"));
        config.addDefault("rewards.daily.streak.7", Arrays.asList("money:1000", "item:EMERALD:5"));
        
        config.addDefault("rewards.milestones.3600", Arrays.asList("money:500")); // 1 hour
        config.addDefault("rewards.milestones.86400", Arrays.asList("money:5000", "item:DIAMOND_BLOCK:1")); // 24 hours
        
        config.addDefault("rewards.daily_tasks.3600", Arrays.asList("money:200")); // 1 hour daily
        config.addDefault("rewards.weekly_tasks.25200", Arrays.asList("money:3000")); // 7 hours weekly
        
        config.addDefault("rewards.consecutive_days.7", Arrays.asList("money:2000"));
        config.addDefault("rewards.consecutive_days.30", Arrays.asList("money:10000", "item:GOLDEN_APPLE:5"));
        
        config.options().copyDefaults(true);
        plugin.saveConfig();
        
        // 加载配置
        databaseType = config.getString("database.type", "sqlite");
        mysqlHost = config.getString("database.mysql.host", "localhost");
        mysqlPort = config.getInt("database.mysql.port", 3306);
        mysqlDatabase = config.getString("database.mysql.database", "minecraft");
        mysqlUser = config.getString("database.mysql.user", "root");
        mysqlPassword = config.getString("database.mysql.password", "");
        useRedis = config.getBoolean("database.use_redis", false);
    }
    
    public String getDatabaseType() {
        return databaseType;
    }
    
    public String getMysqlHost() {
        return mysqlHost;
    }
    
    public int getMysqlPort() {
        return mysqlPort;
    }
    
    public String getMysqlDatabase() {
        return mysqlDatabase;
    }
    
    public String getMysqlUser() {
        return mysqlUser;
    }
    
    public String getMysqlPassword() {
        return mysqlPassword;
    }
    
    public boolean isUseRedis() {
        return useRedis;
    }
    
    public List<String> getDailySignInRewards() {
        return plugin.getConfig().getStringList("rewards.daily.sign_in");
    }
    
    public List<String> getStreakRewards(int days) {
        return plugin.getConfig().getStringList("rewards.daily.streak." + days);
    }
    
    public List<String> getMilestoneRewards(int seconds) {
        return plugin.getConfig().getStringList("rewards.milestones." + seconds);
    }
    
    public List<String> getDailyTaskRewards(int seconds) {
        return plugin.getConfig().getStringList("rewards.daily_tasks." + seconds);
    }
    
    public List<String> getWeeklyTaskRewards(int seconds) {
        return plugin.getConfig().getStringList("rewards.weekly_tasks." + seconds);
    }
    
    public List<String> getConsecutiveDayRewards(int days) {
        return plugin.getConfig().getStringList("rewards.consecutive_days." + days);
    }
    
    public void reload() {
        plugin.reloadConfig();
        setup();
    }
}
