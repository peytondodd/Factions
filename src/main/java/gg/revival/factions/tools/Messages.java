package gg.revival.factions.tools;

import com.google.common.base.Joiner;
import gg.revival.factions.file.FileManager;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.obj.PlayerFaction;
import gg.revival.factions.obj.ServerFaction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.*;

public class Messages {

    public static String getValue(String path) {
        return ChatColor.translateAlternateColorCodes('&', FileManager.getMessages().getString(path));
    }

    public static String gameTag() {
        return getValue("misc.game-tag");
    }

    public static String memberOnline(String member) {
        return getValue("notifications.member-online").replace("%player%", member);
    }

    public static String memberOffline(String member) {
        return getValue("notifications.member-offline").replace("%player%", member);
    }

    public static String friendlyFire() {
        return getValue("notifications.friendly-fire");
    }

    public static String allyFire() {
        return getValue("notifications.ally-fire");
    }

    public static String enteringClaim(String claimInformation) {
        return getValue("notifications.entering-claim").replace("%claim%", claimInformation);
    }

    public static String leavingClaim(String claimInformation) {
        return getValue("notifications.leaving-claim").replace("%claim%", claimInformation);
    }

    public static String formattedGlobalFaction(String faction, String player, String message) {
        return getValue("chat-formatting.global-faction-member")
                .replace("%fac%", faction)
                .replace("%player%", player)
                .replace("%msg%", message);
    }

    public static String formattedGlobalAlly(String faction, String player, String message) {
        return getValue("chat-formatting.global-ally-member")
                .replace("%fac%", faction)
                .replace("%player%", player)
                .replace("%msg%", message);
    }

    public static String formattedGlobalEnemy(String faction, String player, String message) {
        return getValue("chat-formatting.global-enemy")
                .replace("%fac%", faction)
                .replace("%player%", player)
                .replace("%msg%", message);
    }

    public static String formattedFactionChat(String faction, String player, String message) {
        return getValue("chat-formatting.faction-chat")
                .replace("%fac%", faction)
                .replace("%player%", player)
                .replace("%msg%", message);
    }

    public static String formattedAllyChat(String faction, String player, String message) {
        return getValue("chat-formatting.ally-chat")
                .replace("%fac%", faction)
                .replace("%player%", player)
                .replace("%msg%", message);
    }

    public static String factionCreated(String faction, String player) {
        return getValue("broadcasts.fac-created").replace("%fac%", faction).replace("%player%", player);
    }

    public static String factionDisbanded(String faction, String player) {
        return getValue("broadcasts.fac-disbanded").replace("%fac%", faction).replace("%player%", player);
    }

    public static String noPermission() {
        return getValue("errors.no-permission");
    }

    public static String noConsole() {
        return getValue("errors.no-console");
    }

    public static String profileNotFound() {
        return getValue("errors.profile-not-found");
    }

    public static String playerNotFound() {
        return getValue("errors.player-not-found");
    }

    public static String factionNotFound() {
        return getValue("errors.faction-not-found");
    }

    public static String alreadyInFaction() {
        return getValue("errors.already-in-faction");
    }

    public static String alreadyInFactionOther() {
        return getValue("errors.already-in-faction-other");
    }

    public static String badFactionName() {
        return getValue("errors.bad-fac-name");
    }

    public static String facNameInUse() {
        return getValue("errors.fac-name-in-use");
    }

    public static String noSubclaimAccess() { return getValue("errors.no-subclaim-access"); }

    public static String subclaimDeleted() { return getValue("notifications.subclaim-deleted"); }

    public static String subclaimDeletedFaction() { return getValue("notifications.subclaim-deleted-faction"); }

    public static String subclaimTooClose() { return getValue("errors.subclaim-too-close"); }

    public static String leaderRequired() { return getValue("errors.leader-required"); }

    public static String officerRequired() { return getValue("errors.officer-required"); }

    public static String unraidableRequired() { return getValue("errors.unraidable-required"); }

    public static String unfrozenRequired() { return getValue("errors.unfrozen-required"); }

    public static String notInFaction() { return getValue("errors.not-in-faction"); }

    public static String factionInfo(PlayerFaction faction, Player displayedTo) {
        StringBuilder info = new StringBuilder();
        DecimalFormat format = new DecimalFormat("#,###.00");
        Map<UUID, String> namedRoster = new HashMap<UUID, String>();

        try {
            namedRoster = new NameFetcher(faction.getRoster(false)).call();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        info.append(
                ChatColor.DARK_GREEN + "" + ChatColor.STRIKETHROUGH + "----------------" +
                        ChatColor.GOLD + "" + ChatColor.BOLD + "[" + ChatColor.YELLOW + faction.getDisplayName() + ChatColor.GOLD + "" + ChatColor.BOLD + "]" +
                        ChatColor.DARK_GREEN + "" + ChatColor.STRIKETHROUGH + "----------------" + "\n");

        if(faction.getRoster(false).contains(displayedTo.getUniqueId()) && faction.getAnnouncement() != null) {
            info.append(ChatColor.YELLOW + " - " + ChatColor.GOLD + "Announcement" + ChatColor.WHITE + ": " + faction.getAnnouncement() + "\n");
        }

        info.append(ChatColor.YELLOW + " - " + ChatColor.GOLD + "Deaths Until Raidable" + ChatColor.WHITE + ": ");

        if(faction.getDtr().doubleValue() < 0.0) {
            info.append(ChatColor.DARK_RED + "" + Math.round(faction.getDtr().doubleValue()));
        } else if (faction.getDtr().doubleValue() < 1.0) {
            info.append(ChatColor.RED + "" + Math.round(faction.getDtr().doubleValue()));
        } else {
            info.append(Math.round(faction.getDtr().doubleValue()));
        }

        if(faction.isFrozen()) {
            info.append(ChatColor.GRAY + " (Frozen)" + "\n");
        } else if (faction.getDtr().doubleValue() == faction.getMaxDTR()) {
            info.append(ChatColor.BLUE + "(Max)" + "\n");
        } else {
            info.append("\n");
        }

        info.append(ChatColor.YELLOW + " - " + ChatColor.GOLD + "Balance" + ChatColor.WHITE + ": $" + format.format(faction.getBalance()));

        info.append(ChatColor.YELLOW + " - " + ChatColor.GOLD + "Leader" + ChatColor.WHITE + ": " + namedRoster.get(faction.getLeader()));

        List<String> onlineOfficers = new ArrayList<String>();
        List<String> onlineMembers = new ArrayList<String>();
        List<String> offlineOfficers = new ArrayList<String>();
        List<String> offlineMembers = new ArrayList<String>();

        for(UUID officerID : faction.getOfficers()) {
            if(Bukkit.getPlayer(officerID) != null && Bukkit.getPlayer(officerID).isOnline()) {
                onlineOfficers.add(ChatColor.GREEN + "*" + namedRoster.get(officerID));
            } else {
                offlineOfficers.add(ChatColor.GRAY + "*" + namedRoster.get(officerID));
            }
        }

        for(UUID memberID : faction.getMembers()) {
            if(Bukkit.getPlayer(memberID) != null && Bukkit.getPlayer(memberID).isOnline()) {
                onlineMembers.add(ChatColor.GREEN + namedRoster.get(memberID));
            } else {
                offlineMembers.add(ChatColor.GRAY + namedRoster.get(memberID));
            }
        }

        info.append(ChatColor.YELLOW + " - " + ChatColor.GOLD + "Officers" + ChatColor.WHITE + ": " + Joiner.on(ChatColor.WHITE + ", ").join(onlineOfficers));
        info.append(Joiner.on(ChatColor.WHITE + ", ").join(offlineOfficers) + "\n");
        info.append(ChatColor.YELLOW + " - " + ChatColor.GOLD + "Members" + ChatColor.WHITE + ": " + Joiner.on(ChatColor.WHITE + ", ").join(onlineMembers));
        info.append(Joiner.on(ChatColor.WHITE + ", ").join(offlineMembers) + "\n");

        if(faction.isFrozen()) {
            int seconds = 0, minutes = 0, hours = 0;
            long dur = faction.getUnfreezeTime() - System.currentTimeMillis();

            seconds = (int)dur / 1000;
            minutes = seconds / 60;
            hours = minutes / 60;

            String unfreezeTime = null;

            if(hours > 0) {
                unfreezeTime = hours + " hours";
            } else if(minutes > 0) {
                unfreezeTime = minutes + " minutes";
            } else {
                unfreezeTime = seconds + " seconds";
            }

            info.append("     " + "\n");
            info.append(ChatColor.GOLD + "This faction will begin regenerating DTR in " + ChatColor.WHITE + unfreezeTime + "\n");
            info.append(ChatColor.DARK_GREEN + "" + ChatColor.STRIKETHROUGH + "----------------------------------------");
        }

        return info.toString();
    }

}
