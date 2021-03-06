package gg.revival.factions.commands.cont;

import gg.revival.factions.commands.CmdCategory;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Configuration;
import gg.revival.factions.tools.Logger;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.logging.Level;

public class FAcceptCommand extends FCommand {

    public FAcceptCommand() {
        super(
                "accept",
                Arrays.asList("join"),
                "/f accept <faction>",
                "Accept a faction invitation",
                null,
                CmdCategory.BASICS,
                2,
                2,
                true
        );
    }

    @Override
    public void onCommand(CommandSender sender, String args[]) {
        if (!(sender instanceof Player) && isPlayerOnly()) {
            sender.sendMessage(Messages.noConsole());
            return;
        }

        Player player = (Player) sender;

        if (args.length < getMinArgs() || args.length > getMaxArgs()) {
            player.sendMessage(ChatColor.RED + getSyntax());
            return;
        }

        if (FactionManager.getFactionByPlayer(player.getUniqueId()) != null) {
            player.sendMessage(Messages.alreadyInFaction());
            return;
        }

        String namedFaction = args[1];

        if (FactionManager.getFactionByName(namedFaction) == null) {
            player.sendMessage(Messages.factionNotFound());
            return;
        }

        PlayerFaction faction = (PlayerFaction) FactionManager.getFactionByName(namedFaction);

        if (!faction.getPendingInvites().contains(player.getUniqueId()) && !player.hasPermission(Permissions.ADMIN)) {
            player.sendMessage(Messages.noPendingInviteOther());
            return;
        }

        if (faction.getRoster(false).size() >= Configuration.MAX_FAC_SIZE && !player.hasPermission(Permissions.ADMIN)) {
            player.sendMessage(Messages.factionFull());
            return;
        }

        if (faction.isFrozen() && !player.hasPermission(Permissions.ADMIN)) {
            player.sendMessage(Messages.unfrozenRequiredOther());
            return;
        }

        faction.getMembers().add(player.getUniqueId());
        faction.sendMessage(Messages.joinedFactionOther(player.getName()));

        player.sendMessage(Messages.joinedFaction(faction.getDisplayName()));

        Logger.log(Level.INFO, player.getName() + " joined " + faction.getDisplayName());
    }

}
