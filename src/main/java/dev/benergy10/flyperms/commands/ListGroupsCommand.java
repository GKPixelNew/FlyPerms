package dev.benergy10.flyperms.commands;

import dev.benergy10.flyperms.Constants.Commands;
import dev.benergy10.flyperms.Constants.MessageKey;
import dev.benergy10.flyperms.Constants.Permissions;
import dev.benergy10.flyperms.FlyPerms;
import dev.benergy10.minecrafttools.acf.annotation.CommandAlias;
import dev.benergy10.minecrafttools.acf.annotation.CommandPermission;
import dev.benergy10.minecrafttools.acf.annotation.Description;
import dev.benergy10.minecrafttools.acf.annotation.Subcommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@CommandAlias(Commands.BASE)
public class ListGroupsCommand extends FlyPermsCommand {

    public ListGroupsCommand(@NotNull FlyPerms plugin) {
        super(plugin);
    }

    @Subcommand(Commands.LIST_GROUPS)
    @CommandPermission(Permissions.LIST_GROUPS)
    @Description("Show all the speed groups available.")
    public void onListGroups(CommandSender sender) {
        this.messenger.send(sender, MessageKey.LISTGROUP_HEADER);
        this.plugin.getFPConfig()
                .getSpeedGroups()
                .forEach(group -> this.messenger.send(sender, MessageKey.LISTGROUP_SPEED_INFO,
                        group.getName(),
                        group.getLowerLimit(),
                        group.getUpperLimit())
                );
    }
}
