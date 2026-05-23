package com.example.rewards;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PlayerCacheManager {
    
    private final RewardsPlugin plugin;
    private final PlayerRewardsDAO rewardsDAO;
    private final Map<UUID, PlayerCache> playerCaches;
    private final ScheduledExecutorService saveScheduler;
    
    public PlayerCacheManager(RewardsPlugin plugin, PlayerRewardsDAO rewardsDAO) {
        this.plugin = plugin;
        this.rewardsDAO = rewardsDAO;
        this.playerCaches = new ConcurrentHashMap<>();
        this.saveScheduler = new ScheduledThreadPoolExecutor(1);
        
        // 每30秒保存一次脏数据
        saveScheduler.scheduleAtFixedRate(this::saveDirtyData, 30, 30, TimeUnit.SECONDS);
    }
    
    public void loadPlayerData(Player player) {
        rewardsDAO.getPlayerData(player.getUniqueId().toString()).thenAccept(data -> {
            PlayerCache cache = new PlayerCache(data);
            playerCaches.put(player.getUniqueId(), cache);
            // 启动玩家定时任务
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                PlayerScheduler scheduler = new PlayerScheduler(plugin, player, cache);
                scheduler.start();
            }, 20L); // 延迟20 ticks确保玩家完全加入
        });
    }
    
    public void unloadPlayerData(Player player) {
        PlayerCache cache = playerCaches.remove(player.getUniqueId());
        if (cache != null) {
            cache.markDirty(); // 确保保存
            savePlayerCache(cache); // 立即保存
        }
    }
    
    public PlayerCache getPlayerCache(UUID playerId) {
        return playerCaches.get(playerId);
    }
    
    public void savePlayerCache(PlayerCache cache) {
        if (cache.isDirty()) {
            rewardsDAO.savePlayerData(cache.getData()).thenAccept(success -> {
                if (success) {
                    cache.setDirty(false);
                }
            });
        }
    }
    
    private void saveDirtyData() {
        for (PlayerCache cache : playerCaches.values()) {
            if (cache.isDirty()) {
                savePlayerCache(cache);
            }
        }
    }
    
    public void saveAllOnlinePlayers() {
        for (PlayerCache cache : playerCaches.values()) {
            cache.markDirty();
            savePlayerCache(cache);
        }
    }
    
    public void shutdown() {
        saveAllOnlinePlayers();
        saveScheduler.shutdown();
    }
}
