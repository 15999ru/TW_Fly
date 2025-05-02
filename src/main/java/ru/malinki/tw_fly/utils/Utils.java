package ru.malinki.tw_fly.utils;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.ConcurrentHashMap;

public class Utils implements Listener {

    public static ConcurrentHashMap<Player, Long> timesMap = new ConcurrentHashMap<>();

    public static void updateTime(Player player) {
        timesMap.put(player, System.currentTimeMillis());
    }

    public static boolean checkTime(Player player, Long time) {

        final Long last = timesMap.getOrDefault(player, 0L);

        return System.currentTimeMillis() - last >= time;
    }

    public static Long getTimeTo(Player player, Long time) {

        final Long last = timesMap.getOrDefault(player, 0L);

        return time - (System.currentTimeMillis() - last);
    }

    @EventHandler
    public void quitPlayer(PlayerQuitEvent e) {

        final Player player = e.getPlayer();

        timesMap.remove(player);
    }
}
