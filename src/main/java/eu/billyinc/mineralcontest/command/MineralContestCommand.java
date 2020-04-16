package eu.billyinc.mineralcontest.command;

import eu.billyinc.mineralcontest.GameState;
import eu.billyinc.mineralcontest.model.Team;
import eu.billyinc.mineralcontest.task.GameCycle;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.billyinc.mineralcontest.manager.MineralContestManager;
import eu.billyinc.mineralcontest.model.MineralContestChest;

public class MineralContestCommand implements CommandExecutor {

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

		if (args[0].toLowerCase().equals("setarenachestlocation") && sender instanceof Player) {
			MineralContestManager.getMineralContestGameManager().setArenaChestLocation(((Player) sender).getLocation());
			sender.sendMessage("arena chest location seted!");

			return true;
		}
		
		if (args[0].toLowerCase().equals("setmapsize") && sender instanceof Player) {
			MineralContestManager.getMineralContestGameManager().setMapSize(Integer.valueOf(args[1])); 
			
			return true;
		}
		
		if (args[0].toLowerCase().equals("chest") && sender instanceof Player) {
			if (MineralContestManager.getApp().getGameState() == GameState.PLAYING) {
				MineralContestChest mcChest = new MineralContestChest(MineralContestManager.getMineralContestGameManager().getSpawn());

				mcChest.drop();
			}
			return true;
		}

		if (args[0].toLowerCase().equals("arenachest") && sender instanceof Player) {
			MineralContestManager.getApp().spawnArenaChest();
			return true;
		}
		
		if (args[0].toLowerCase().equals("setlimit") && sender instanceof Player) {
			WorldBorder wb = ((Player) sender).getWorld().getWorldBorder();
			wb.setCenter(MineralContestManager.getMineralContestGameManager().getSpawn());
			wb.setSize(Integer.valueOf(args[1]));

			return true;
		}

		if (args[0].toLowerCase().equals("removelimit") && sender instanceof Player) {
			WorldBorder wb = ((Player) sender).getWorld().getWorldBorder();
			wb.reset();

			return true;
		}

		if (args[0].toLowerCase().equals("arene") && sender instanceof Player) {
			Player player = (Player) sender;
			
			if (MineralContestManager.getMineralContestChestManager().getMineralContestArenaChest() instanceof MineralContestChest) {
				Team team = MineralContestManager.getApp().getPlayerTeamMap().get(player.getUniqueId()).getTeam();
				for (Player p : team.getPlayers()) {
					p.teleport(MineralContestManager.getMineralContestGameManager().getArenaLocation());
				}
			} else {
				player.sendMessage(ChatColor.RED + "L'arène n'est plus disponible");
			}
		}

		if (args[0].toLowerCase().equals("start") && sender instanceof Player) {
			if (sender.isOp()) {
				if (MineralContestManager.getApp().allTeamAsAPlayer()) {
					if (MineralContestManager.getApp().getGameState() == GameState.WAITING) {
						MineralContestManager.getApp().setGameState(GameState.STARTING);
						GameCycle starter = new GameCycle();
						starter.runTaskTimer(MineralContestManager.getApp(), 0, 20);
					} else {
						sender.sendMessage(ChatColor.RED + "Impossible de lancer le jeu");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Lancement impossible : Il reste une équipe vide");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Tu doit être opérateur du serveur pour éxécuter cette commande");
			}
		}

		if (args[0].toLowerCase().equals("reset") && sender instanceof Player) {
			if (sender.isOp()) {
				MineralContestManager.getApp().setGameState(GameState.WAITING);
				MineralContestManager.getApp().resetTeams();
			} else {
				sender.sendMessage(ChatColor.RED + "Tu doit être opérateur du serveur pour éxécuter cette commande");
			}
		}

		return false;
	}

}
