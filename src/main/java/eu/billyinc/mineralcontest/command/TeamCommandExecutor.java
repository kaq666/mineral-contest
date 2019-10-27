package eu.billyinc.mineralcontest.command;

import eu.billyinc.mineralcontest.App;
import eu.billyinc.mineralcontest.model.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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

        // TODO : faire un switch ici aussi
        if (command.getName().equalsIgnoreCase("setTeamSpawnLocatioon")) {
            if (commandSender instanceof Player && arguments.length == 1 ) {
                Player player = (Player) commandSender;
                Team team = main.getTeamByName("Team " + arguments[0]);
                if (team != null) {
                    team.setSpawn(player.getLocation());
                    commandSender.sendMessage("Le spawn de la " + team.getName() + " à été définie sur votre position");
                }
            }
        }
        return false;
    }
}
