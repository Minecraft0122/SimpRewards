package com.example.rewards;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RewardsCommand implements CommandExecutor {
    
    private final RewardsPlugin plugin;
    private final RewardGUI rewardGUI;
    
    public RewardsCommand(RewardsPlugin plugin, RewardGUI rewardGUI) {
        this.plugin = plugin;
        this.rewardGUI = rewardGUI;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§c只有玩家可以使用此命令！");
            return true;
        }
        
        if (args.length == 0) {
            rewardGUI.openMainGUI(player);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "reload":
                if (player.hasPermission("rewards.reload")) {
                    plugin.getConfigManager().reload();
                    plugin.getRewardManager().reload();
                    player.sendMessage("§a§l✓ §a配置已重新加载！");
                } else {
                    player.sendMessage("§c你没有权限使用此命令！");
                }
                break;
            case "reset":
                if (player.hasPermission("rewards.reset")) {
                    if (args.length >= 2) {
                        switch (args[1].toLowerCase()) {
                            case "daily":
                                resetDailyData(player);
                                break;
                            case "weekly":
                                resetWeeklyData(player);
                                break;
                            case "all":
                                resetAllData(player);
                                break;
                            default:
                                player.sendMessage("§c用法: /rewards reset <daily|weekly|all>");
                        }
                    } else {
                        player.sendMessage("§c用法: /rewards reset <daily|weekly|all>");
                    }
                } else {
                    player.sendMessage("§c你没有权限使用此命令！");
                }
                break;
            default:
                player.sendMessage("§c未知子命令！可用命令: reload, reset");
        }
        
        return true;
    }
    
    private void resetDailyData(Player player) {
        PlayerCache cache = plugin.getCacheManager().getPlayerCache(player.getUniqueId());
        if (cache != null) {
            PlayerRewardsData data = cache.getData();
            data.setDailySeconds(0);
            data.setDailyClaimedDate("");
            cache.markDirty();
            player.sendMessage("§a§l✓ §a每日数据已重置！");
        }
    }
    
    private void resetWeeklyData(Player player) {
        PlayerCache cache = plugin.getCacheManager().getPlayerCache(player.getUniqueId());
        if (cache != null) {
            PlayerRewardsData data = cache.getData();
            data.setWeeklySeconds(0);
            data.setWeeklyClaimedWeek("");
            cache.markDirty();
            player.sendMessage("§a§l✓ §a每周数据已重置！");
        }
    }
    
    private void resetAllData(Player player) {
        PlayerCache cache = plugin.getCacheManager().getPlayerCache(player.getUniqueId());
        if (cache != null) {
            PlayerRewardsData data = cache.getData();
            data.setTotalSeconds(0);
            data.setDailySeconds(0);
            data.setWeeklySeconds(0);
            data.setConsecutiveDays(0);
            data.setSignStreak(0);
            data.setLastSignTime(0);
            data.setClaimedMilestones(new java.util.HashSet<>());
            data.setClaimedConsecutive(new java.util.HashSet<>());
            data.setDailyClaimedDate("");
            data.setWeeklyClaimedWeek("");
            cache.markDirty();
            player.sendMessage("§a§l✓ §a所有数据已重置！");
        }
    }
}
