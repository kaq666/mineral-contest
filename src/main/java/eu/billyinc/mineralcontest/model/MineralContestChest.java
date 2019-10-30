package eu.billyinc.mineralcontest.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.billyinc.mineralcontest.manager.MineralContestManager;

public class MineralContestChest {

	private static int MAX_EMERALD = 3;
	private static int MAX_DIAMOND = 6;
	private static int MAX_GOLD = 18;
	private static int MAX_IRON = 90;
	
	private static double[] sounds = {0.0, 0.53, 0.56, 0.6, 0.63};
	
	private int emerald;
	private int diamond;
	private int gold;
	private int iron;
	private boolean hasBeenTransfered = false;
	private Location location;
	
	public MineralContestChest(Location location) {
		this.location = location;
		
		this.emerald = ThreadLocalRandom.current().nextInt(0, MAX_EMERALD + 1);
		this.diamond = ThreadLocalRandom.current().nextInt(0, MAX_DIAMOND + 1);
		this.gold = ThreadLocalRandom.current().nextInt(0, MAX_GOLD + 1);
		this.iron = ThreadLocalRandom.current().nextInt(0, MAX_IRON + 1);
	}
	
	public int getEmerald() {
		return emerald;
	}

	public void setEmerald(int emerald) {
		this.emerald = emerald;
	}

	public int getDiamond() {
		return diamond;
	}

	public void setDiamond(int diamond) {
		this.diamond = diamond;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getIron() {
		return iron;
	}

	public void setIron(int iron) {
		this.iron = iron;
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

	public void spawn() {		
		Block block = this.location.getBlock();
		block.setType(Material.CHEST);
		
		Chest chest = (Chest) block.getState();
		chest.getInventory().setItem(this.getItemsPositions()[0], new ItemStack(Material.EMERALD, this.getEmerald()));
		chest.getInventory().setItem(this.getItemsPositions()[1], new ItemStack(Material.DIAMOND, this.getDiamond()));
		chest.getInventory().setItem(this.getItemsPositions()[2], new ItemStack(Material.GOLD_INGOT, this.getGold()));
		chest.getInventory().setItem(this.getItemsPositions()[3], new ItemStack(Material.IRON_INGOT, this.getIron()));
		
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
	}
	
	private int[] getItemsPositions() {
		List<Integer> list = new ArrayList<Integer>();
		
		for (int i = 0; i < 27; i++) {
			list.add(i);
		}
		
		for (int i = 0; i < 10; i++) {
			Collections.shuffle(list);
		}
		
		int[] result = {list.get(0), list.get(1), list.get(2), list.get(3)};		
		return result;
	}
}
