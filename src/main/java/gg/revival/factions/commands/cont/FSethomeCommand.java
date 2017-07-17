package gg.revival.factions.commands.cont;

import gg.revival.factions.claims.Claim;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.tools.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FSethomeCommand extends FCommand {

    public FSethomeCommand() {
        super(
                "sethome",
                null,
                "/f sethome",
                "Set your faction home",
                null,
                1,
                1,
                true
        );
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

        for(Claim claims : faction.getClaims()) {
            if(!claims.inside(player.getLocation(), true)) continue;

            faction.setHomeLocation(player.getLocation());

            player.sendMessage(Messages.homeSet());
            faction.sendMessage(Messages.homeSetOther(player.getName()));

            return;
        }

        player.sendMessage(Messages.homeOutsideClaims());
    }

}
