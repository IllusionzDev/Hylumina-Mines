package me.illusion.hyluminamines;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import me.illusion.hyluminamines.Commands.Mines;
import me.illusion.hyluminamines.Util.Config.ConfigStates;
import me.illusion.hyluminamines.Util.Config.CreateConfig;
import me.illusion.hyluminamines.Util.LogMe;
import me.illusion.hyluminamines.Util.WorldEdit.Area;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public final class Main extends JavaPlugin {
    private static Main instance = null;
    private WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");

    public CreateConfig config, mines;

    @Override
    public void onEnable() {
        new LogMe("Starting...").Warning();
        instance = this;

        if (this.worldEdit == null) {
            new LogMe("WorldEdit not found...").Error();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        config = new CreateConfig("config.yml", "Hylumina/Mines");
        mines = new CreateConfig("mines.yml", "Hylumina/Mines");
        new ConfigStates(config).ConfigDefaults();
        new ConfigStates(mines).LoadConfig();

        SetupCommands();
        SetupListeners();

        new LogMe("Startup successful!").Success();
    }

    @Override
    public void onDisable() {
        new LogMe("Saving data...").Warning();
        new ConfigStates(mines).SaveConfig();
    }

    public static Main GetInstance() {
        return instance;
    }

    public WorldEditPlugin GetWorldEdit() {
        return this.worldEdit;
    }

    private void SetupCommands() {
        getCommand("hm").setExecutor(new Mines());
    }

    private void SetupListeners() {
        String packageName = getClass().getPackage().getName();
        for (Class<?> cl : new Reflections(packageName + ".Events").getSubTypesOf(Listener.class)) {
            try {
                Listener listener = (Listener) cl.getDeclaredConstructor().newInstance();
                getServer().getPluginManager().registerEvents(listener, this);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
}
