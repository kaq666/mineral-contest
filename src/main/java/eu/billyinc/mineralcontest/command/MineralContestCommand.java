package eu.billyinc.mineralcontest.command;

import eu.billyinc.mineralcontest.App;
import eu.billyinc.mineralcontest.GameState;
import eu.billyinc.mineralcontest.task.GameStarter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.billyinc.mineralcontest.manager.MineralContestManager;
import eu.billyinc.mineralcontest.model.MineralContestChest;

public class MineralContestCommand implements CommandExecutor {

	private App main;

	public MineralContestCommand(App main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (args[0].toLowerCase().equals("setspawn") && sender instanceof Player) {
			MineralContestManager.getMineralContestGameManager().setSpawn(((Player) sender).getLocation().getBlock().getLocation());
			sender.sendMessage("spawn seted!");
			
			return true;
		}
		
		if (args[0].toLowerCase().equals("setarenalocation") && sender instanceof Player) {
			MineralContestManager.getMineralContestGameManager().setArenaLocation(((Player) sender).getLocation());
			sender.sendMessage("arena location seted!");
			
			return true;
		}
		
		if (args[0].toLowerCase().equals("setmapsize") && sender instanceof Player) {
			MineralContestManager.getMineralContestGameManager().setMapSize(Integer.valueOf(args[1])); 
			
			return true;
		}
		
		if (args[0].toLowerCase().equals("chest") && sender instanceof Player) {
			MineralContestChest mcChest = new MineralContestChest(MineralContestManager.getMineralContestGameManager().getSpawn());
			
			mcChest.spawn();
			
			return true;
		}

		if (args[0].toLowerCase().equals("start") && sender instanceof Player) {
			sender.sendMessage("Start command");
			if (main.allTeamAsAPlayer()) {
				if (main.getGameState() == GameState.WAITING) {
					GameStarter starter = new GameStarter(this.main);
					starter.runTaskTimer(main, 0, 20);
				} else {
					sender.sendMessage(ChatColor.RED + "Impossible de lancer le jeu");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Lancement impossible : Il reste une équipe vide");
			}
		}

		return false;
	}

}
