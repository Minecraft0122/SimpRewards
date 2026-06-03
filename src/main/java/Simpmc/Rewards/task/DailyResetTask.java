package Simpmc.Rewards.task;

import Simpmc.Rewards.SimpRewardsPlugin;
import Simpmc.Rewards.data.PlayerData;

public class DailyResetTask {

    private final SimpRewardsPlugin plugin;

    public DailyResetTask(SimpRewardsPlugin plugin) {
        this.plugin = plugin;
    }

    public void start() {

        plugin.getServer().getAsyncScheduler()
                .runAtFixedRate(
                        plugin,
                        task -> {

                            for (PlayerData data :
                                    plugin.getDataManager()
                                            .getCache()
                                            .values()) {

                                data.setDailyClaimed(false);
                            }

                        },
                        1,
                        86400000,
                        java.util.concurrent.TimeUnit.MILLISECONDS
                );
    }
}
