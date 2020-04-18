package eu.billyinc.mineralcontest.model;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.billyinc.mineralcontest.manager.MineralContestManager;

public class MineralContestChest {

	
	private static double[] sounds = {0.0, 0.53, 0.56, 0.6, 0.63};
	private boolean hasBeenTransfered = false;
	private Location location;
	private boolean isArenaChest;
	
	public MineralContestChest(Location location) {
		this.location = location.clone();
		this.isArenaChest = false;
	}
	
	public MineralContestChest(Location location, boolean isArenaChest) {
		this.location = location.clone();
		this.isArenaChest = isArenaChest;
	}
	
	private void removeParachute() {
		this.location.clone().add(0, 1, 0).getBlock().setType(Material.AIR);

		this.location.clone().add(1, 2, 0).getBlock().setType(Material.AIR);
		this.location.clone().add(-1, 2, 0).getBlock().setType(Material.AIR);
		this.location.clone().add(2, 3, 0).getBlock().setType(Material.AIR);
		this.location.clone().add(-2, 3, 0).getBlock().setType(Material.AIR);

		this.location.clone().add(2, 4, 0).getBlock().setType(Material.AIR);
		this.location.clone().add(-2, 4, 0).getBlock().setType(Material.AIR);
		this.location.clone().add(1, 5, 0).getBlock().setType(Material.AIR);
		this.location.clone().add(-1, 5, 0).getBlock().setType(Material.AIR);
		this.location.clone().add(0, 5, 0).getBlock().setType(Material.AIR);

		this.location.clone().add(2, 4, 1).getBlock().setType(Material.AIR);
		this.location.clone().add(-2, 4, 1).getBlock().setType(Material.AIR);
		this.location.clone().add(1, 5, 1).getBlock().setType(Material.AIR);
		this.location.clone().add(-1, 5, 1).getBlock().setType(Material.AIR);
		this.location.clone().add(0, 5, 1).getBlock().setType(Material.AIR);

		this.location.clone().add(2, 4, -1).getBlock().setType(Material.AIR);
		this.location.clone().add(-2, 4, -1).getBlock().setType(Material.AIR);
		this.location.clone().add(1, 5, -1).getBlock().setType(Material.AIR);
		this.location.clone().add(-1, 5, -1).getBlock().setType(Material.AIR);
		this.location.clone().add(0, 5, -1).getBlock().setType(Material.AIR);
	}

	private void buildParachute() {
			this.location.getBlock().setType(Material.CHEST);

			this.location.clone().add(1, 1, 0).getBlock().setType(Material.OAK_FENCE);
			this.location.clone().add(-1, 1, 0).getBlock().setType(Material.OAK_FENCE);
			this.location.clone().add(2, 2, 0).getBlock().setType(Material.OAK_FENCE);
			this.location.clone().add(-2, 2, 0).getBlock().setType(Material.OAK_FENCE);

			this.location.clone().add(2, 3, 0).getBlock().setType(Material.WHITE_WOOL);
			this.location.clone().add(-2, 3, 0).getBlock().setType(Material.WHITE_WOOL);
			this.location.clone().add(1, 4, 0).getBlock().setType(Material.WHITE_WOOL);
			this.location.clone().add(-1, 4, 0).getBlock().setType(Material.WHITE_WOOL);
			this.location.clone().add(0, 4, 0).getBlock().setType(Material.WHITE_WOOL);

			this.location.clone().add(2, 3, 1).getBlock().setType(Material.WHITE_WOOL);
			this.location.clone().add(-2, 3, 1).getBlock().setType(Material.WHITE_WOOL);
			this.location.clone().add(1, 4, 1).getBlock().setType(Material.WHITE_WOOL);
			this.location.clone().add(-1, 4, 1).getBlock().setType(Material.WHITE_WOOL);
			this.location.clone().add(0, 4, 1).getBlock().setType(Material.WHITE_WOOL);

			this.location.clone().add(2, 3, -1).getBlock().setType(Material.WHITE_WOOL);
			this.location.clone().add(-2, 3, -1).getBlock().setType(Material.WHITE_WOOL);
			this.location.clone().add(1, 4, -1).getBlock().setType(Material.WHITE_WOOL);
			this.location.clone().add(-1, 4, -1).getBlock().setType(Material.WHITE_WOOL);
			this.location.clone().add(0, 4, -1).getBlock().setType(Material.WHITE_WOOL);
	}

	public void drop() {
		Bukkit.broadcastMessage("Un coffre atterrira bientôt en x:" + this.location.getX() + " y:" + this.location.getY() + " z:" + this.location.getZ());
		this.location.setY(130);

		int i = 0;
		
		while (i < 130 && this.location.clone().add(0, -(i+1), 0).getBlock().isPassable()) {
			Bukkit.getScheduler().runTaskLater(MineralContestManager.getApp(), () -> {
				removeParachute();
				buildParachute();
				this.location.add(0, -1, 0);
			}, i * 20);
			i++;
		}

		Bukkit.getScheduler().runTaskLater(MineralContestManager.getApp(), () -> {
			removeParachute();
			spawn();
			Bukkit.broadcastMessage("Un coffre à atterit en x:" + this.location.getX() + " y:" + this.location.getY() + " z:" + this.location.getZ());
		}, (i +1 ) * 20);
	}
	
	public double[] getSounds() {
		return MineralContestChest.sounds;
	}
	
	public boolean isHasBeenTransfered() {
		return hasBeenTransfered;
	}

	public void setHasBeenTransfered(boolean hasBeenTransfered) {
		this.hasBeenTransfered = hasBeenTransfered;
	}
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	public boolean isArenaChest() {
		return this.isArenaChest;
	}

	public void spawn() {
		Block block = this.location.getBlock();
		block.setType(Material.CHEST);
		Chest chest = (Chest) block.getState();
		
		int maxItem, minItem;
        maxItem = 30;
        minItem = 10;
        Random r = new Random();
        int nbItem = r.nextInt((maxItem - minItem) + 1) + minItem;
        Material material = null;
        for(int i = 0; i < nbItem; i++) {
        	int rn = r.nextInt(100);
        	
        	if (rn >= 0 && rn <= 75) {
        		material = Material.IRON_INGOT;
        	} else if (rn > 75 && rn <= 90) {
        		material = Material.GOLD_INGOT;
        	} else if (rn > 90 && rn <= 97) {
        		material = Material.DIAMOND;
        	} else {
        		material = Material.EMERALD;
        	}
        	
        	chest.getInventory().addItem(new ItemStack(material));
        }
		
		MineralContestManager.getMineralContestChestManager().addMineralContestChest(chest, this);
	}

	public void transferTo(Player player) {
		this.hasBeenTransfered = true;
		
		Block block = this.location.getBlock();
		block.setType(Material.CHEST);
		
		Chest chest = (Chest) block.getState();

		for(ItemStack item : chest.getInventory().getContents()) {
			if (item instanceof ItemStack) {
				player.getInventory().addItem(item);
				player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 0.0f);
				chest.getInventory().remove(item);
			}
		}
		
		MineralContestManager.getMineralContestChestManager().removeMineralContestChest(chest);
	}
}
