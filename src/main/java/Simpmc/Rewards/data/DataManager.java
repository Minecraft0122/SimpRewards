package Simpmc.Rewards.data;

import Simpmc.Rewards.SimpRewardsPlugin;

import java.io.File;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DataManager {

    private final SimpRewardsPlugin plugin;

    private final Map<UUID, PlayerData> cache = new ConcurrentHashMap<>();

    private File dataFolder;

    public DataManager(SimpRewardsPlugin plugin) {
        this.plugin = plugin;
    }

    public void init() {

        dataFolder = new File(plugin.getDataFolder(), "data");

        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
    }

    public PlayerData getPlayerData(UUID uuid) {

        return cache.computeIfAbsent(uuid, id -> {

            PlayerData data = JsonStorage.load(dataFolder, id);

            if (data == null) {
                data = new PlayerData(id);
            }

            return data;
        });
    }

    public void save(UUID uuid) {

        PlayerData data = cache.get(uuid);

        if (data == null) {
            return;
        }

        JsonStorage.save(dataFolder, data);
    }

    public void saveAll() {

        cache.keySet().forEach(this::save);
    }

    public Map<UUID, PlayerData> getCache() {
        return cache;
    }
}
