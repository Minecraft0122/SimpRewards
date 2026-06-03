package Simpmc.Rewards.command;

import Simpmc.Rewards.config.ConfigManager;
import Simpmc.Rewards.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        ConfigManager.load();

        MessageUtil.color("&aSimpRewards 配置已重载");

        return true;
    }
}
