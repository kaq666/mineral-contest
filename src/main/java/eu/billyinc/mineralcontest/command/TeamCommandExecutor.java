package eu.billyinc.mineralcontest.command;

import eu.billyinc.mineralcontest.App;
import eu.billyinc.mineralcontest.model.Team;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommandExecutor implements CommandExecutor {

    private App main;

    public TeamCommandExecutor(App main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] arguments) {

        if (command.getName().equalsIgnoreCase("setTeamSpawnLocation")) {
            if (commandSender instanceof Player && arguments.length == 1 ) {
                Player player = (Player) commandSender;
                Team team = main.getTeamByName("Team " + arguments[0]);
                if (player.isOp()) {
                    if (team != null) {
                        team.setSpawn(player.getLocation());
                        commandSender.sendMessage("Le spawn de la " + team.getName() + " à été définie sur votre position");
                    } else {
                        commandSender.sendMessage("Equipe introuvable : argument attendu <Jaune> pour la Team Jaune");
                    }
                } else {
                    commandSender.sendMessage(ChatColor.RED + "Tu doit être opérateur du serveur pour éxécuter cette commande");
                }
            }
        }
        return false;
    }
}
