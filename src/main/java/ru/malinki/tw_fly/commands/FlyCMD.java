package ru.malinki.tw_fly.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.malinki.tw_fly.checkItem.ElytraCheck;
import ru.malinki.tw_fly.Main;
import ru.malinki.tw_fly.utils.ChatUtil;
import ru.malinki.tw_fly.utils.Utils;

import java.util.Objects;

public class FlyCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!(commandSender instanceof Player)) {
            ChatUtil.sendMessage(commandSender, Main.i.getConfig().getString("onlyPlayer"));
            return false;
        }

        Player player = (Player) commandSender;

        if (!player.hasPermission("tw.fly")) {
            ChatUtil.sendMessage(commandSender, Main.i.getConfig().getString("noPermission"));
            return true;
        }

        if (ElytraCheck.check(player)) {
            ChatUtil.sendMessage(commandSender, Main.i.getConfig().getString("elytraCheck"));
            return true;
        }

        if (player.isFlying()) {
            player.setAllowFlight(false);
            player.setFlying(false);
            ChatUtil.sendMessage(commandSender, Main.i.getConfig().getString("quitFly"));
            return true;
        }


        long time = Main.i.getConfig().getLong("cooldownTime");
        if (!Utils.checkTime(player, time)) {
            long timeToEnd = Utils.getTimeTo(player, time) / 1000;
            ChatUtil.sendMessage(commandSender, Objects.requireNonNull(Main.i.getConfig().getString("cooldown")).replace("{time}", Long.toString(timeToEnd)));
            return true;
        }

        if (((Player) commandSender).getLevel() < Main.i.getConfig().getInt("levelToStart")) {
            ChatUtil.sendMessage(commandSender, Main.i.getConfig().getString("noExp"));
            return true;
        }

        player.setAllowFlight(true);
        player.setFlying(true);
        Utils.updateTime(player);
        ChatUtil.sendMessage(commandSender, Main.i.getConfig().getString("flyStart"));

        return false;
    }
}
