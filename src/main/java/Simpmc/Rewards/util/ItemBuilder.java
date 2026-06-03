package Simpmc.Rewards.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    public ItemBuilder(Material material) {

        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    public ItemBuilder name(String name) {

        meta.setDisplayName(
                MessageUtil.color(name)
        );

        return this;
    }

    public ItemBuilder lore(List<String> lore) {

        meta.setLore(
                lore.stream()
                        .map(MessageUtil::color)
                        .toList()
        );

        return this;
    }

    public ItemStack build() {

        item.setItemMeta(meta);

        return item;
    }
}
