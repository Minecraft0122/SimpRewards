package com.example.rewards;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RewardGUI {
    
    private final RewardsPlugin plugin;
    private final PlayerCacheManager cacheManager;
    private final RewardManager rewardManager;
    private final Map<UUID, Inventory> playerGUIs;
    
    public RewardGUI(RewardsPlugin plugin, PlayerCacheManager cacheManager, RewardManager rewardManager) {
        this.plugin = plugin;
        this.cacheManager = cacheManager;
        this.rewardManager = rewardManager;
        this.playerGUIs = new HashMap<>();
    }
    
    public void openMainGUI(Player player) {
        PlayerCache cache = cacheManager.getPlayerCache(player.getUniqueId());
        if (cache == null) {
            player.sendMessage("§c正在加载数据，请稍后再试...");
            return;
        }
        
        Inventory gui = Bukkit.createInventory(null, 54, "§6§l奖励中心");
        
        // 玩家信息面板
        ItemStack infoItem = createItem(Material.PLAYER_HEAD, "§a玩家信息", Arrays.asList(
            "§7在线时间: §e" + formatTime(cache.getData().getTotalSeconds()),
            "§7今日在线: §e" + formatTime(cache.getData().getDailySeconds()),
            "§7本周在线: §e" + formatTime(cache.getData().getWeeklySeconds()),
            "§7连签天数: §e" + cache.getData().getSignStreak(),
            "§7连续活跃: §e" + cache.getData().getConsecutiveDays() + "天"
        ));
        setSkullOwner(infoItem, player.getName());
        gui.setItem(4, infoItem);
        
        // 签到按钮
        ItemStack signInItem = createItem(Material.GOLD_INGOT, "§e每日签到", Arrays.asList(
            "§7点击进行每日签到",
            "§7今日奖励: §6" + getDailyRewardSummary(),
            "§7连签奖励: §6" + getStreakRewardSummary(cache.getData().getSignStreak() + 1)
        ));
        if (isTodaySigned(cache.getData())) {
            signInItem = createItem(Material.GRAY_DYE, "§c今日已签到", Arrays.asList("§7明天再来签到吧！"));
        }
        gui.setItem(13, signInItem);
        
        // 里程碑奖励
        gui.setItem(20, createMilestoneItem("§b累计在线奖励", 3600, cache, "milestone_3600"));
        gui.setItem(21, createMilestoneItem("§b累计在线奖励", 7200, cache, "milestone_7200"));
        gui.setItem(22, createMilestoneItem("§b累计在线奖励", 14400, cache, "milestone_14400"));
        gui.setItem(23, createMilestoneItem("§b累计在线奖励", 28800, cache, "milestone_28800"));
        gui.setItem(24, createMilestoneItem("§b累计在线奖励", 86400, cache, "milestone_86400"));
        
        // 每日任务
        gui.setItem(29, createTaskItem("§a每日任务", 3600, cache, true));
        gui.setItem(30, createTaskItem("§a每日任务", 7200, cache, true));
        
        // 每周任务
        gui.setItem(33, createTaskItem("§9每周任务", 25200, cache, false));
        gui.setItem(34, createTaskItem("§9每周任务", 50400, cache, false));
        
        // 连续活跃奖励
        gui.setItem(41, createConsecutiveItem("§d连续活跃奖励", 7, cache, "consecutive_7"));
        gui.setItem(42, createConsecutiveItem("§d连续活跃奖励", 14, cache, "consecutive_14"));
        gui.setItem(43, createConsecutiveItem("§d连续活跃奖励", 30, cache, "consecutive_30"));
        
        // 装饰物品
        for (int i = 0; i < 54; i++) {
            if (gui.getItem(i) == null) {
                gui.setItem(i, createItem(Material.GRAY_STAINED_GLASS_PANE, " ", Arrays.asList()));
            }
        }
        
        player.openInventory(gui);
        playerGUIs.put(player.getUniqueId(), gui);
    }
    
    private boolean isTodaySigned(PlayerRewardsData data) {
        if (data.getLastSignTime() == 0) return false;
        java.time.LocalDateTime lastSign = java.time.LocalDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(data.getLastSignTime()), 
            java.time.ZoneId.systemDefault()
        );
        return lastSign.toLocalDate().isEqual(java.time.LocalDate.now());
    }
    
    private ItemStack createMilestoneItem(String name, int seconds, PlayerCache cache, String milestoneId) {
        PlayerRewardsData data = cache.getData();
        boolean claimed = data.getClaimedMilestones().contains(milestoneId);
        boolean unlocked = data.getTotalSeconds() >= seconds;
        
        Material material = claimed ? Material.LIME_CONCRETE : (unlocked ? Material.YELLOW_CONCRETE : Material.RED_CONCRETE);
        String status = claimed ? "§a§l✓ 已领取" : (unlocked ? "§e§l! 可领取" : "§c§l✗ 未解锁");
        String timeRequired = formatTime(seconds);
        String timeProgress = formatTime(data.getTotalSeconds()) + "§7/" + timeRequired;
        
        List<String> lore = Arrays.asList(
            "§7需要累计在线: §e" + timeRequired,
            "§7当前进度: §e" + timeProgress,
            status
        );
        
        return createItem(material, name, lore);
    }
    
    private ItemStack createTaskItem(String name, int seconds, PlayerCache cache, boolean daily) {
        PlayerRewardsData data = cache.getData();
        boolean claimed;
        boolean unlocked;
        String timeProgress;
        
        if (daily) {
            String today = java.time.LocalDate.now().toString();
            claimed = data.getDailyClaimedDate() != null && data.getDailyClaimedDate().equals(today);
            unlocked = data.getDailySeconds() >= seconds;
            timeProgress = formatTime(data.getDailySeconds()) + "§7/" + formatTime(seconds);
        } else {
            String currentWeek = java.time.LocalDate.now().get(YearWeekField.INSTANCE);
            claimed = data.getWeeklyClaimedWeek() != null && data.getWeeklyClaimedWeek().equals(currentWeek);
            unlocked = data.getWeeklySeconds() >= seconds;
            timeProgress = formatTime(data.getWeeklySeconds()) + "§7/" + formatTime(seconds);
        }
        
        Material material = claimed ? Material.LIME_CONCRETE : (unlocked ? Material.YELLOW_CONCRETE : Material.RED_CONCRETE);
        String status = claimed ? "§a§l✓ 已领取" : (unlocked ? "§e§l! 可领取" : "§c§l✗ 未完成");
        
        List<String> lore = Arrays.asList(
            "§7需要在线: §e" + formatTime(seconds),
            "§7当前进度: §e" + timeProgress,
            status
        );
        
        return createItem(material, name, lore);
    }
    
    private ItemStack createConsecutiveItem(String name, int days, PlayerCache cache, String consecutiveId) {
        PlayerRewardsData data = cache.getData();
        boolean claimed = data.getClaimedConsecutive().contains(consecutiveId);
        boolean unlocked = data.getConsecutiveDays() >= days;
        
        Material material = claimed ? Material.LIME_CONCRETE : (unlocked ? Material.YELLOW_CONCRETE : Material.RED_CONCRETE);
        String status = claimed ? "§a§l✓ 已领取" : (unlocked ? "§e§l! 可领取" : "§c§l✗ 需要 " + days + "天");
        String progress = data.getConsecutiveDays() + "§7/" + days + "天";
        
        List<String> lore = Arrays.asList(
            "§7需要连续活跃: §e" + days + "天",
            "§7当前进度: §e" + progress,
            status
        );
        
        return createItem(material, name, lore);
    }
    
    private ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }
    
    private void setSkullOwner(ItemStack item, String playerName) {
        if (item.getType() == Material.PLAYER_HEAD || item.getType() == Material.SKULL_ITEM) {
            org.bukkit.inventory.meta.SkullMeta meta = (org.bukkit.inventory.meta.SkullMeta) item.getItemMeta();
            if (meta != null) {
                meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
                item.setItemMeta(meta);
            }
        }
    }
    
    private String formatTime(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        if (hours > 0) {
            return hours + "h" + minutes + "m";
        }
        return minutes + "m";
    }
    
    private String getDailyRewardSummary() {
        return "金币 + 钻石";
    }
    
    private String getStreakRewardSummary(int days) {
        if (days >= 30) return "10000金币 + 5金苹果";
        if (days >= 14) return "5000金币";
        if (days >= 7) return "2000金币";
        if (days >= 3) return "500金币";
        return "无";
    }
    
    public void handleGUIClick(Player player, int slot) {
        Inventory gui = playerGUIs.get(player.getUniqueId());
        if (gui == null) return;
        
        ItemStack clicked = gui.getItem(slot);
        if (clicked == null || clicked.getType() == Material.GRAY_STAINED_GLASS_PANE) return;
        
        PlayerCache cache = cacheManager.getPlayerCache(player.getUniqueId());
        if (cache == null) return;
        
        switch (slot) {
            case 13: // 签到按钮
                if (!isTodaySigned(cache.getData())) {
                    rewardManager.grantSignInReward(player);
                }
                break;
            case 20: // 1小时里程碑
                if (cache.getData().getTotalSeconds() >= 3600 && 
                    !cache.getData().getClaimedMilestones().contains("milestone_3600")) {
                    rewardManager.grantMilestoneReward(player, "milestone_3600", 3600);
                }
                break;
            case 21: // 2小时里程碑
                if (cache.getData().getTotalSeconds() >= 7200 && 
                    !cache.getData().getClaimedMilestones().contains("milestone_7200")) {
                    rewardManager.grantMilestoneReward(player, "milestone_7200", 7200);
                }
                break;
            case 22: // 4小时里程碑
                if (cache.getData().getTotalSeconds() >= 14400 && 
                    !cache.getData().getClaimedMilestones().contains("milestone_14400")) {
                    rewardManager.grantMilestoneReward(player, "milestone_14400", 14400);
                }
                break;
            case 23: // 8小时里程碑
                if (cache.getData().getTotalSeconds() >= 28800 && 
                    !cache.getData().getClaimedMilestones().contains("milestone_28800")) {
                    rewardManager.grantMilestoneReward(player, "milestone_28800", 28800);
                }
                break;
            case 24: // 24小时里程碑
                if (cache.getData().getTotalSeconds() >= 86400 && 
                    !cache.getData().getClaimedMilestones().contains("milestone_86400")) {
                    rewardManager.grantMilestoneReward(player, "milestone_86400", 86400);
                }
                break;
            case 29: // 1小时每日任务
                String today = java.time.LocalDate.now().toString();
                if (cache.getData().getDailySeconds() >= 3600 && 
                    (cache.getData().getDailyClaimedDate() == null || 
                     !cache.getData().getDailyClaimedDate().equals(today))) {
                    rewardManager.grantDailyTaskReward(player, "daily_3600", 3600);
                }
                break;
            case 30: // 2小时每日任务
                if (cache.getData().getDailySeconds() >= 7200 && 
                    (cache.getData().getDailyClaimedDate() == null || 
                     !cache.getData().getDailyClaimedDate().equals(today))) {
                    rewardManager.grantDailyTaskReward(player, "daily_7200", 7200);
                }
                break;
            case 33: // 7小时每周任务
                String currentWeek = java.time.LocalDate.now().get(YearWeekField.INSTANCE);
                if (cache.getData().getWeeklySeconds() >= 25200 && 
                    (cache.getData().getWeeklyClaimedWeek() == null || 
                     !cache.getData().getWeeklyClaimedWeek().equals(currentWeek))) {
                    rewardManager.grantWeeklyTaskReward(player, "weekly_25200", 25200);
                }
                break;
            case 34: // 14小时每周任务
                if (cache.getData().getWeeklySeconds() >= 50400 && 
                    (cache.getData().getWeeklyClaimedWeek() == null || 
                     !cache.getData().getWeeklyClaimedWeek().equals(currentWeek))) {
                    rewardManager.grantWeeklyTaskReward(player, "weekly_50400", 50400);
                }
                break;
            case 41: // 7天连续活跃
                if (cache.getData().getConsecutiveDays() >= 7 && 
                    !cache.getData().getClaimedConsecutive().contains("consecutive_7")) {
                    rewardManager.grantConsecutiveReward(player, "consecutive_7", 7);
                }
                break;
            case 42: // 14天连续活跃
                if (cache.getData().getConsecutiveDays() >= 14 && 
                    !cache.getData().getClaimedConsecutive().contains("consecutive_14")) {
                    rewardManager.grantConsecutiveReward(player, "consecutive_14", 14);
                }
                break;
            case 43: // 30天连续活跃
                if (cache.getData().getConsecutiveDays() >= 30 && 
                    !cache.getData().getClaimedConsecutive().contains("consecutive_30")) {
                    rewardManager.grantConsecutiveReward(player, "consecutive_30", 30);
                }
                break;
        }
        
        // 刷新GUI
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            openMainGUI(player);
        }, 1L);
    }
    
    public void closeGUI(Player player) {
        playerGUIs.remove(player.getUniqueId());
    }
}
