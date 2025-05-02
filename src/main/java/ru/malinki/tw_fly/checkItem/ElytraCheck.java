package ru.malinki.tw_fly.checkItem;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ElytraCheck {

    public static boolean check(Player player) {
        return player.getInventory().getChestplate() == null || player.getInventory().getChestplate().getType() != Material.ELYTRA;
    }
}
