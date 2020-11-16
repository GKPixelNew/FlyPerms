package com.benergy.flyperms;

import co.aikar.commands.PaperCommandManager;
import com.benergy.flyperms.api.FPPlugin;
import com.benergy.flyperms.commands.FPCommand;
import com.benergy.flyperms.commands.InfoCommand;
import com.benergy.flyperms.commands.ListGroupsCommand;
import com.benergy.flyperms.commands.ReloadCommand;
import com.benergy.flyperms.commands.SeeAllowedCommand;
import com.benergy.flyperms.commands.SpeedCommand;
import com.benergy.flyperms.commands.UsageCommand;
import com.benergy.flyperms.utils.FlyCheckScheduler;
import com.benergy.flyperms.listeners.FPPlayerListener;
import com.benergy.flyperms.listeners.FPWorldListener;
import com.benergy.flyperms.permissions.PermsFly;
import com.benergy.flyperms.permissions.PermsRegister;
import com.benergy.flyperms.utils.FPLogger;
import com.benergy.flyperms.utils.BstatsMetrics;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class FlyPerms extends JavaPlugin implements FPPlugin {
    // Config
    private final FlyPermsConfig FPConfig = new FlyPermsConfig(this);

    // Permissions
    private final PermsRegister FPRegister = new PermsRegister(this);
    private final PermsFly FPFly = new PermsFly(this);

    // Handlers
    private final FlyCheckScheduler flyCheckScheduler = new FlyCheckScheduler(this);

    @Override
    public void onEnable() {
        FPLogger.setup(this);
        FPLogger.showStartUpText();

        // Get config
        this.saveDefaultConfig();
        this.FPConfig.loadConfigValues();

        // Init bstats
        BstatsMetrics.configureMetrics(this);

        // Register events
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new FPPlayerListener(this), this);
        pm.registerEvents(new FPWorldListener(this), this);

        // Register permission nodes
        this.FPRegister.registerPerms();


        // Register commands
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");
        commandManager.registerCommand(new FPCommand(this));
        commandManager.registerCommand(new InfoCommand(this));
        commandManager.registerCommand(new ReloadCommand(this));
        commandManager.registerCommand(new SeeAllowedCommand(this));
        commandManager.registerCommand(new SpeedCommand(this));
        commandManager.registerCommand(new ListGroupsCommand(this));
        commandManager.registerCommand(new UsageCommand(this));

        flyCheckScheduler.startFlyChecker();

        FPLogger.log(Level.INFO, "Started!");
    }

    public boolean reload() {
        flyCheckScheduler.stopFlyChecker();
        if (!this.FPConfig.reloadConfigValues()) {
            return false;
        }
        flyCheckScheduler.startFlyChecker();
        return true;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        flyCheckScheduler.stopFlyChecker();
        FPLogger.log(Level.INFO, "Stopped. Happy flying!");
    }

    public boolean isIgnoreWorld(World world) {
        return (FPConfig.getDisabledWorlds().contains(world.getName()));
    }

    public FlyPermsConfig getFPConfig() {
        return FPConfig;
    }

    public PermsRegister getFPRegister() {
        return FPRegister;
    }

    public PermsFly getFPFly() {
        return FPFly;
    }

    public FlyCheckScheduler getFlyCheckScheduler() {
        return flyCheckScheduler;
    }
}
