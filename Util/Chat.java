package me.illusion.hyluminamines.Util;

import me.illusion.hyluminamines.Main;
import me.illusion.hyluminamines.Util.Config.GetConfigData;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Chat {
    public static String watermark = new GetConfigData(Main.GetInstance().config).getStringFromConfig("hylumina.prefix") + " ";

    private Player ply;
    private String message;

    public Chat(Player ply, String message) {
        this.ply = ply;
        this.message = message;
    }

    public void WatermarkMessage() {
        ply.sendMessage(watermark + message);
    }

    public void message() {
        ply.sendMessage(message);
    }
}
