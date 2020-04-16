package eu.billyinc.mineralcontest.listener;

import eu.billyinc.mineralcontest.GameState;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import eu.billyinc.mineralcontest.manager.MineralContestManager;
import eu.billyinc.mineralcontest.model.MineralContestChest;
import eu.billyinc.mineralcontest.model.MineralContestPlayer;

import java.util.List;

public class MineralContestListener implements Listener {

	@EventHandler
	public void onOpeningMineralContestChest(PlayerInteractEvent e) {
		if (e.getClickedBlock() != null) {
			if (e.getClickedBlock() instanceof Block && e.getClickedBlock().getState() instanceof Chest && e.getPlayer() instanceof Player) {
				Chest chest = (Chest) e.getClickedBlock().getState();
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
									player.playNote(player.getLocation(), Instrument.BIT, new Note(1));
									chest.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, chest.getLocation(), 100);
								}, 20 * (i + 1))
										.getTaskId());
					}

					mineralContestPlayer.addTask(
							Bukkit.getScheduler().runTaskLater(MineralContestManager.getApp(), () -> {
								player.closeInventory();
								mcChest.transferTo(player);
							}, 20 * 5).getTaskId());
				}
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
	public void onPlayerOpenInventory(InventoryOpenEvent e) {
	}

	@EventHandler
	public void onFurnaceBurn(FurnaceSmeltEvent e) {
		if (MineralContestManager.getApp().getGameState() == GameState.PLAYING) {
			if (
					e.getSource().getType().equals(Material.IRON_SWORD) ||
							e.getSource().getType().equals(Material.IRON_HELMET) ||
							e.getSource().getType().equals(Material.IRON_BOOTS) ||
							e.getSource().getType().equals(Material.IRON_LEGGINGS) ||
							e.getSource().getType().equals(Material.IRON_CHESTPLATE)
			) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerDead(PlayerDeathEvent e) {
		if (MineralContestManager.getApp().getGameState() == GameState.PLAYING) {
			Player player = e.getEntity();
			List<ItemStack> drops = e.getDrops();
			drops.clear();
			for (ItemStack item : player.getInventory()) {
				if (
						item.getType().equals(Material.EMERALD) ||
						item.getType().equals(Material.GOLD_INGOT) ||
						item.getType().equals(Material.GOLD_ORE) ||
						item.getType().equals(Material.DIAMOND) ||
						item.getType().equals(Material.IRON_INGOT) ||
						item.getType().equals(Material.IRON_ORE)
				) {
					drops.add(item);
				}
			}
			e.setKeepLevel(true);
		}
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player player = e.getPlayer();
		if (MineralContestManager.getApp().getGameState() == GameState.PLAYING) {
			// send the player to it's spawn
			e.setRespawnLocation(MineralContestManager.getApp().getPlayerTeamMap().get(player.getUniqueId()).getTeam().getSpawn());
			player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
			player.getInventory().setItem(1, new ItemStack(Material.BOW));
			player.getInventory().setItem(2, new ItemStack(Material.ARROW, 64));
			// equip iron armor
			player.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
			player.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
			player.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			player.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
		}
	}

	@EventHandler
	public void onPlayerDismissInventory(InventoryCloseEvent e) {
		if (MineralContestManager.getApp().getGameState() == GameState.PLAYING) {
			if (e.getInventory().getType().equals(InventoryType.HOPPER) && e.getPlayer() instanceof Player) {
				Player player = (Player) e.getPlayer();
				MineralContestPlayer mineralContestPlayer = MineralContestManager.getMineralContestPlayerManager().getMineralContestPlayerByUUID(player.getUniqueId());

				mineralContestPlayer.removeAllTasks();
			}

			if (e.getInventory().getType().equals(InventoryType.SHULKER_BOX) && e.getPlayer() instanceof Player) {
				int inventoryValue = 0;
				Player player = (Player) e.getPlayer();
				for (ItemStack itemStack : e.getInventory().getContents()) {
					inventoryValue += MineralContestManager.getApp().getItemStackValue(itemStack);
				}
				if (MineralContestManager.getApp().getGameState() == GameState.PLAYING) {
					MineralContestManager.getApp().getPlayerTeamMap().get(player.getUniqueId()).getTeam().setScore(inventoryValue);
				}
			}
		}
	}
	
	@EventHandler
	public void onDragMineralContestChestInventory(InventoryClickEvent e) {
		if (MineralContestManager.getApp().getGameState() == GameState.PLAYING) {
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
}
