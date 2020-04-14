package eu.billyinc.mineralcontest.command;

import org.bukkit.WorldBorder;
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
		
		if (args[0].toLowerCase().equals("setmapsize") && sender instanceof Player) {
			MineralContestManager.getMineralContestGameManager().setMapSize(Integer.valueOf(args[1])); 
			
			return true;
		}
		
		if (args[0].toLowerCase().equals("chest") && sender instanceof Player) {
			MineralContestChest mcChest = new MineralContestChest(MineralContestManager.getMineralContestGameManager().getSpawn());
			mcChest.drop();
			
			sender.sendMessage("chest spawned!");
			
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
		
		return false;
	}

}
