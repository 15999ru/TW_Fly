package ru.malinki.tw_fly;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import ru.malinki.tw_fly.checkItem.ElytraCheck;
import ru.malinki.tw_fly.commands.FlyCMD;
import ru.malinki.tw_fly.utils.ChatUtil;
import ru.malinki.tw_fly.utils.Utils;

import java.util.Objects;

public final class Main extends JavaPlugin {

    public static boolean isBreak;

    public static Main i;
    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("f")).setExecutor(new FlyCMD());
        getServer().getPluginManager().registerEvents(new Utils(), this);
        i = this;
        saveDefaultConfig();
        isBreak = getConfig().getBoolean("isBreak");

        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player -> {

                    if(player.isFlying() && !ElytraCheck.check(player)) {
                        Bukkit.getScheduler().runTask(Main.this, () -> {

                            if (player.getLevel() == 0) {
                                player.setAllowFlight(false);
                                player.setFlying(false);
                                ChatUtil.sendMessage(player, getConfig().getString("expEnd"));
                            }
                            else {
                                int getExp = getConfig().getInt("takeExpSecond");
                                player.giveExp(-getExp);
                                if (isBreak) {
                                    ItemStack item =  player.getInventory().getChestplate();
                                    assert item != null;
                                    Damageable damageable = (Damageable) item.getItemMeta();
                                    damageable.setDamage(damageable.getDamage() + 1);
                                    item.setItemMeta((ItemMeta) damageable);
                                }
                            }
                        });
                    }

                    if(player.isFlying() && ElytraCheck.check(player)) {
                        Bukkit.getScheduler().runTask(Main.this, () -> {

                            if((player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR && !player.hasPermission("tw.admin"))) {
                                player.setAllowFlight(false);
                                player.setFlying(false);
                                ChatUtil.sendMessage(player, getConfig().getString("quitFly"));
                            }
                        });
                    }
                });
            }
        }.runTaskTimerAsynchronously(this, 0L, 20L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
