package eu.billyinc.mineralcontest.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import eu.billyinc.mineralcontest.manager.MineralContestManager;
import eu.billyinc.mineralcontest.model.MineralContestChest;
import eu.billyinc.mineralcontest.model.MineralContestPlayer;

public class MineralContestListener implements Listener {
	
	public MineralContestListener() {
	}

	@EventHandler
	public void onOpeningMineralContestChest(InventoryOpenEvent e) {
		if (e.getInventory().getHolder() instanceof Chest && e.getPlayer() instanceof Player) {
			Chest chest = (Chest) e.getInventory().getHolder();
			MineralContestChest mcChest = MineralContestManager.getMineralContestChestManager().getMineralContestChestByChest(chest);
			
			if(mcChest instanceof MineralContestChest && !mcChest.isHasBeenTransfered()) {
				e.setCancelled(true);
				
				Player player = (Player) e.getPlayer();
				MineralContestPlayer mineralContestPlayer = MineralContestManager.getMineralContestPlayerManager().getMineralContestPlayerByUUID(player.getUniqueId());
				
				Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER, "Unlocking chest...");
				player.openInventory(inventory);
				
				for (int i = 0; i < inventory.getSize(); i++) {
					inventory.setItem(i, new ItemStack(Material.RED_WOOL, 1));
					
					final int x = i;
					mineralContestPlayer.addTask(
					Bukkit.getScheduler().runTaskLater(MineralContestManager.getApp(), () -> {
						inventory.setItem(x, new ItemStack(Material.GREEN_WOOL, 1));
						player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 2.0f, (float) mcChest.getSounds()[x]);
					}, 20 * (i + 1)).getTaskId());
				}
				
				mineralContestPlayer.addTask(
				Bukkit.getScheduler().runTaskLater(MineralContestManager.getApp(), () -> {
					e.setCancelled(false);
					
					player.closeInventory();
					mcChest.transferTo(player);
					player.openInventory(chest.getInventory());
					
					mineralContestPlayer.addTask(
					Bukkit.getScheduler().runTaskLater(MineralContestManager.getApp(), () -> {
						player.closeInventory();
						chest.getInventory().clear();
					}, 20).getTaskId());
				}, 20*5).getTaskId());
			}
		}
	}
	
	@EventHandler
	public void onBreakChest(BlockBreakEvent e) {
		if (e.getBlock().getState() instanceof Chest) {
			Chest chest = (Chest) e.getBlock().getState();
			if(MineralContestManager.getMineralContestChestManager().getMineralContestChestByChest(chest) instanceof MineralContestChest) {
				MineralContestManager.getMineralContestChestManager().removeMineralContestChest(chest);
			}
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		MineralContestPlayer mineralContestPlayer = new MineralContestPlayer(e.getPlayer().getUniqueId());
		MineralContestManager.getMineralContestPlayerManager().addMineralContestPlayer(mineralContestPlayer);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		MineralContestPlayer mineralContestPlayer = MineralContestManager.getMineralContestPlayerManager().getMineralContestPlayerByUUID(e.getPlayer().getUniqueId());
		
		if(mineralContestPlayer instanceof MineralContestPlayer) {
			MineralContestManager.getMineralContestPlayerManager().removeMineralContestPlayer(mineralContestPlayer);
		}
	}
	
	@EventHandler
	public void onPlayerDamaged(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			MineralContestPlayer mineralContestPlayer = MineralContestManager.getMineralContestPlayerManager().getMineralContestPlayerByUUID(player.getUniqueId());
			
			player.closeInventory();
			mineralContestPlayer.removeAllTasks();
		}
	}
	
	@EventHandler
	public void onPlayerDismissInventory(InventoryCloseEvent e) {
		if (e.getInventory().getType().equals(InventoryType.HOPPER) && e.getPlayer() instanceof Player) {
			Player player = (Player) e.getPlayer();
			MineralContestPlayer mineralContestPlayer = MineralContestManager.getMineralContestPlayerManager().getMineralContestPlayerByUUID(player.getUniqueId());
		
			mineralContestPlayer.removeAllTasks();
		}
	}
	
	@EventHandler
	public void onDragMineralContestChestInventory(InventoryClickEvent e) {
		if(e.getWhoClicked() instanceof Player) {
			if(e.getInventory().getHolder() instanceof Chest) {
				Chest chest = (Chest) e.getInventory().getHolder();
				MineralContestChest mcChest = MineralContestManager.getMineralContestChestManager().getMineralContestChestByChest(chest);
				
				if(mcChest instanceof MineralContestChest) {
					e.setCancelled(true);
				}
			} else if(e.getInventory().getType().equals(InventoryType.HOPPER)) {
				e.setCancelled(true);
			}
		}
	}
}
