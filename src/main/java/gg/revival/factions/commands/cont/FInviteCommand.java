package gg.revival.factions.commands.cont;

import gg.revival.factions.FP;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Messages;
import gg.revival.factions.tools.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.UUID;

public class FInviteCommand extends FCommand {

    public FInviteCommand() {
        super(
                "invite",
                Arrays.asList("inv"),
                "/f invite",
                "Invite a player to your faction",
                null,
                2,
                2,
                true);
    }

    @Override
    public void onCommand(CommandSender sender, String args[]) {
        if(!(sender instanceof Player) && isPlayerOnly()) {
            sender.sendMessage(Messages.noConsole());
            return;
        }

        Player player = (Player)sender;

        if(args.length < getMinArgs() || args.length > getMaxArgs()) {
            player.sendMessage(ChatColor.RED + getSyntax());
            return;
        }

        if(FactionManager.getFactionByPlayer(player.getUniqueId()) == null) {
            player.sendMessage(Messages.notInFaction());
            return;
        }

        PlayerFaction faction = (PlayerFaction)FactionManager.getFactionByPlayer(player.getUniqueId());

        if(!faction.getLeader().equals(player.getUniqueId()) && !faction.getOfficers().contains(player.getUniqueId())) {
            player.sendMessage(Messages.officerRequired());
            return;
        }

        String namedPlayer = args[1];

        new BukkitRunnable() {
            public void run() {
                try {
                    UUID uuid = UUIDFetcher.getUUIDOf(namedPlayer);
                    String properUsername = Bukkit.getOfflinePlayer(uuid).getName();

                    new BukkitRunnable() {
                        public void run() {
                            if(uuid == null || properUsername == null) {
                                player.sendMessage(Messages.playerNotFound());
                                return;
                            }

                            if(FactionManager.getFactionByPlayer(uuid) != null) {
                                player.sendMessage(Messages.alreadyInFactionOther());
                                return;
                            }

                            faction.getPendingInvites().add(uuid);

                            if(Bukkit.getPlayer(uuid) != null && Bukkit.getPlayer(uuid).isOnline()) {
                                Messages.sendFactionInvite(Bukkit.getPlayer(uuid), faction.getDisplayName(), player.getName());
                            }

                            faction.sendMessage(Messages.invitedPlayer(player.getName(), properUsername));
                        }
                    }.runTask(FP.getInstance());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(FP.getInstance());
    }

}