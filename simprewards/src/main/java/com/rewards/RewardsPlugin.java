package com.example.rewards;

import org.bukkit.plugin.java.JavaPlugin;

public class RewardsPlugin extends JavaPlugin {
    
    private ConfigManager configManager;
    private PlayerRewardsDAO rewardsDAO;
    private PlayerCacheManager cacheManager;
    private PlayerListener playerListener;
    private RewardGUI rewardGUI;
    private RewardManager rewardManager;
    
    @Override
    public void onEnable() {
        // 初始化配置
        this.configManager = new ConfigManager(this);
        configManager.setup();
        
        // 初始化数据访问层
        this.rewardsDAO = new PlayerRewardsDAO(this);
        if (!rewardsDAO.initialize()) {
            getLogger().severe("Failed to initialize database connection. Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        // 初始化缓存管理器
        this.cacheManager = new PlayerCacheManager(this, rewardsDAO);
        
        // 初始化奖励管理器
        this.rewardManager = new RewardManager(this, configManager, cacheManager);
        
        // 初始化GUI
        this.rewardGUI = new RewardGUI(this, cacheManager, rewardManager);
        
        // 注册事件监听器
        this.playerListener = new PlayerListener(this, cacheManager, rewardGUI, rewardManager);
        getServer().getPluginManager().registerEvents(playerListener, this);
        
        // 注册命令
        getCommand("rewards").setExecutor(new RewardsCommand(this, rewardGUI));
        getCommand("rewardsreload").setExecutor(new ReloadCommand(this, configManager, rewardManager));
        
        getLogger().info("Rewards plugin enabled successfully with " + 
                        configManager.getDatabaseType() + " database and Folia compatibility.");
    }
    
    @Override
    public void onDisable() {
        // 保存所有在线玩家数据
        cacheManager.saveAllOnlinePlayers();
        
        // 关闭数据库连接
        if (rewardsDAO != null) {
            rewardsDAO.close();
        }
        
        getLogger().info("Rewards plugin disabled.");
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public PlayerRewardsDAO getRewardsDAO() {
        return rewardsDAO;
    }
    
    public PlayerCacheManager getCacheManager() {
        return cacheManager;
    }
    
    public RewardManager getRewardManager() {
        return rewardManager;
    }
}
