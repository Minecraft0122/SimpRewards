package Simpmc.Rewards.gui;

import Simpmc.Rewards.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MenuItemFactory {

    public static ItemStack claimed() {

        return new ItemBuilder(Material.GRAY_DYE)
                .name("&7已领取")
                .lore(List.of("&8该奖励已经领取"))
                .build();
    }

    public static ItemStack available() {

        return new ItemBuilder(Material.LIME_DYE)
                .name("&a可领取")
                .lore(List.of("&7点击领取"))
                .build();
    }

    public static ItemStack locked() {

        return new ItemBuilder(Material.BARRIER)
                .name("&c未完成")
                .lore(List.of("&7条件未达成"))
                .build();
    }
}
