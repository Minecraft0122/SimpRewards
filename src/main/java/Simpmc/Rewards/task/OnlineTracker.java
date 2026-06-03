package Simpmc.Rewards.task;

import Simpmc.Rewards.SimpRewardsPlugin;
import Simpmc.Rewards.data.PlayerData;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class OnlineTracker {

    private final SimpRewardsPlugin plugin;

    public OnlineTracker(SimpRewardsPlugin plugin) {
        this.plugin = plugin;
    }

    public void start() {

        plugin.getServer().getAsyncScheduler().runAtFixedRate(
                plugin,
                task -> {

                    for (Player player :
                            plugin.getServer().getOnlinePlayers()) {

                        plugin.getServer()
                                .getRegionScheduler()
                                .execute(
                                        plugin,
                                        player.getLocation(),
                                        () -> {

                                            PlayerData data =
                                                    plugin.getDataManager()
                                                            .getPlayerData(
                                                                    player.getUniqueId()
                                                            );

                                            data.addTotalMinutes(1);
                                            data.addDailyMinutes(1);
                                            data.addWeeklyMinutes(1);
                                        }
                                );
                    }

                },
                60000,
                60000,
                TimeUnit.MILLISECONDS
        );
    }
}
