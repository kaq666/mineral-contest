package eu.billyinc.mineralcontest.command;

import eu.billyinc.mineralcontest.GameState;
import eu.billyinc.mineralcontest.model.MineralContestTeam;
import eu.billyinc.mineralcontest.task.GameCycle;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.billyinc.mineralcontest.manager.MineralContestManager;
import eu.billyinc.mineralcontest.model.MineralContestChest;
import eu.billyinc.mineralcontest.model.MineralContestPlayer;

public class MineralContestCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (args[0].toLowerCase().equals("setarenalocation") && sender instanceof Player && sender.isOp()) {
			MineralContestManager.getMineralContestGameManager().setArenaLocation(((Player) sender).getLocation());
			sender.sendMessage("arena location seted!");
			
			return true;
		}

		if (args[0].toLowerCase().equals("setarenachestlocation") && sender instanceof Player && sender.isOp()) {
			MineralContestManager.getMineralContestGameManager().setArenaChestLocation(((Player) sender).getLocation());
			sender.sendMessage("arena chest location seted!");

			return true;
		}
		
		if (args[0].toLowerCase().equals("setmapsize") && sender instanceof Player && sender.isOp()) {
			MineralContestManager.getMineralContestGameManager().setMapSize(Integer.valueOf(args[1])); 
			
			return true;
		}
		
		if (args[0].toLowerCase().equals("chest") && sender instanceof Player && sender.isOp()) {
			Player player = (Player) sender;
			
			MineralContestChest mcChest = new MineralContestChest(player.getLocation());
			mcChest.drop();
			
			return true;
		}

		if (args[0].toLowerCase().equals("arenachest") && sender instanceof Player && sender.isOp()) {
			MineralContestManager.getApp().spawnArenaChest();
			return true;
		}
		
		if (args[0].toLowerCase().equals("setlimit") && sender instanceof Player && sender.isOp()) {
			WorldBorder wb = ((Player) sender).getWorld().getWorldBorder();
			wb.setCenter(MineralContestManager.getMineralContestGameManager().getArenaChestLocation());
			wb.setSize(Integer.valueOf(args[1]));

			return true;
		}

		if (args[0].toLowerCase().equals("removelimit") && sender instanceof Player && sender.isOp()) {
			WorldBorder wb = ((Player) sender).getWorld().getWorldBorder();
			wb.reset();

			return true;
		}

		if (args[0].toLowerCase().equals("arene") && sender instanceof Player) {
			Player player = (Player) sender;
			
			if (MineralContestManager.getMineralContestChestManager().getMineralContestArenaChest() instanceof MineralContestChest) {
				MineralContestPlayer mineralContestPlayer = MineralContestManager.getMineralContestPlayerManager().getMineralContestPlayerByUUID(player.getUniqueId());
				if (mineralContestPlayer instanceof MineralContestPlayer) {
					MineralContestTeam mineralContestTeam = MineralContestManager.getApp().getTeamByName(mineralContestPlayer.getTeamName());
					if (mineralContestTeam instanceof MineralContestTeam) {
						for (Player p : mineralContestTeam.getPlayers()) {
							p.teleport(MineralContestManager.getMineralContestGameManager().getArenaLocation());
						}
					}
				}
			} else {
				player.sendMessage(ChatColor.RED + "L'arène n'est plus disponible");
			}
			
			return true;
		}

		if (args[0].toLowerCase().equals("start") && sender instanceof Player && sender.isOp()) {
			if (MineralContestManager.getMineralContestGameManager().getArenaChestLocation() instanceof Location && MineralContestManager.getMineralContestGameManager().getArenaLocation() instanceof Location) {
				if (MineralContestManager.getApp().allTeamAsAPlayer()) {
					if (MineralContestManager.getApp().getGameState() == GameState.WAITING) {
						MineralContestManager.getApp().setGameState(GameState.STARTING);
						GameCycle starter = null;
						
						if (args.length > 1) {
							try {
								int time = Integer.parseInt(args[1]);
								starter = new GameCycle(time);
							} catch (NumberFormatException e) {
								starter = new GameCycle();
							}
						} else {
							starter = new GameCycle();
						}
						
						starter.runTaskTimer(MineralContestManager.getApp(), 0, 20);
					} else {
						sender.sendMessage(ChatColor.RED + "Impossible de lancer le jeu");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Lancement impossible : Il reste une équipe vide");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Lancement impossible : les spawns ne sont pas définis");
			}
			
			return true;
		}

		if (args[0].toLowerCase().equals("reset") && sender instanceof Player && sender.isOp()) {
			MineralContestManager.getApp().setGameState(GameState.WAITING);
			MineralContestManager.getApp().resetTeams();
			
			return true;
		}
		
		if (args[0].toLowerCase().equals("setteamspawnlocation") && sender instanceof Player && sender.isOp()) {
			if (args.length > 1 ) {
                Player player = (Player) sender;
                MineralContestTeam team = MineralContestManager.getApp().getTeamByName("Team " + args[1]);
                if (team != null) {
                    team.setSpawn(player.getLocation());
                    sender.sendMessage("Le spawn de la " + team.getName() + " à été définie sur votre position");
                } else {
                    sender.sendMessage("Equipe introuvable : argument attendu <Jaune> pour la Team Jaune");
                }
            }
			
			return true;
		}
		
		if (args[0].toLowerCase().equals("events") && sender instanceof Player && sender.isOp()) {
			sender.sendMessage("évenements d'arènes : " + GameCycle.arenasS.toString());
			sender.sendMessage("évenements drops : " + GameCycle.dropsS.toString());
			
			return true;
		}

		return false;
	}

}
