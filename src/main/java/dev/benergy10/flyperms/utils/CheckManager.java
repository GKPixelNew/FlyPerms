package dev.benergy10.flyperms.utils;

import dev.benergy10.flyperms.Constants.FlyState;
import dev.benergy10.flyperms.Constants.Permissions;
import dev.benergy10.flyperms.FlyPerms;
import dev.benergy10.flyperms.api.FPCheckManager;
import dev.benergy10.flyperms.checkers.GameModeChecker;
import dev.benergy10.flyperms.api.PlayerChecker;
import dev.benergy10.flyperms.checkers.SpeedChecker;
import dev.benergy10.flyperms.checkers.WorldChecker;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import static dev.benergy10.flyperms.Constants.FlyState.*;

/**
 * {@inheritDoc}
 */
public class CheckManager implements FPCheckManager {

    private final FlyPerms plugin;

    private final SpeedChecker speedChecker;
    private final WorldChecker worldChecker;
    private final GameModeChecker gameModeChecker;

    public CheckManager(FlyPerms plugin) {
        this.plugin = plugin;
        this.speedChecker = new SpeedChecker(plugin);
        this.worldChecker = new WorldChecker(plugin);
        this.gameModeChecker = new GameModeChecker(plugin);
    }

    /**
     * {@inheritDoc}
     */
    public FlyState calculateFlyState(Player player) {
        if (this.plugin.getFPConfig().isIgnoreWorld(player.getWorld())) {
            return IGNORED;
        }
        if (player.getGameMode().equals(GameMode.SPECTATOR)) {
            return SPECTATOR;
        }
        if (this.plugin.getFPConfig().isAllowCreative() && player.getGameMode().equals(GameMode.CREATIVE)) {
            return CREATIVE_BYPASS;
        }

        return isAllowedToFly(player) ? YES : NO;
    }

    private boolean isAllowedToFly(Player player) {
        if (!gameModeChecker.isEnabled() && !worldChecker.isEnabled()) {
            return checkBaseAllow(player);
        }

        return runPlayerChecker(player, worldChecker)
                && runPlayerChecker(player, gameModeChecker);
    }

    private boolean checkBaseAllow(Player player) {
        return player.hasPermission(Permissions.ALLOW_BASE);
    }

    private <T> boolean runPlayerChecker(Player player, PlayerChecker<T> checker) {
        return !checker.isEnabled() || checker.hasPerm(player);
    }

    /**
     * {@inheritDoc}
     */
    public boolean canChangeSpeedTo(Player player, double speed) {
        return this.plugin.getFPConfig()
                .getSpeedGroups()
                .stream()
                .anyMatch(group -> speedChecker.hasPerm(player, group) && group.isInRange(speed));
    }

    /**
     * {@inheritDoc}
     */
    public SpeedChecker getSpeedChecker() {
        return speedChecker;
    }

    /**
     * {@inheritDoc}
     */
    public WorldChecker getWorldChecker() {
        return worldChecker;
    }

    /**
     * {@inheritDoc}
     */
    public GameModeChecker getGameModeChecker() {
        return gameModeChecker;
    }
}