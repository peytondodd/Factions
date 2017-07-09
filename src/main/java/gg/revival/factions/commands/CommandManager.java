package gg.revival.factions.commands;

import gg.revival.factions.FP;
import gg.revival.factions.commands.cont.*;
import gg.revival.factions.tools.Logger;

import java.util.HashSet;

public class CommandManager {

    private static HashSet<FCommand> commands = new HashSet<FCommand>();

    public static HashSet<FCommand> getCommands() {
        return commands;
    }

    public static FCommand getCommandByLabel(String label) {
        for (FCommand foundCommands : commands) {
            if (foundCommands.getLabel().equalsIgnoreCase(label)) {
                return foundCommands;
            } else if (foundCommands.getAliases() != null && !foundCommands.getAliases().isEmpty()) {
                for (String aliases : foundCommands.getAliases()) {
                    if (aliases.equalsIgnoreCase(label)) {
                        return foundCommands;
                    }
                }
            }
        }

        return null;
    }

    public static void loadCommands() {
        FP.getInstance().getCommand("faction").setExecutor(new FactionsCommandExecutor());

        FCreateCommand createCommand = new FCreateCommand();
        FCreateServerFactionCommand createServerFactionCommand = new FCreateServerFactionCommand();

        FDisbandCommand disbandCommand = new FDisbandCommand();
        FDisbandOtherCommand disbandOtherCommand = new FDisbandOtherCommand();

        FShowCommand showCommand = new FShowCommand();

        FClaimCommand claimCommand = new FClaimCommand();

        commands.add(createCommand);
        commands.add(createServerFactionCommand);
        commands.add(disbandCommand);
        commands.add(disbandOtherCommand);
        commands.add(showCommand);
        commands.add(claimCommand);

        Logger.log("Loaded Commands");
    }

}
