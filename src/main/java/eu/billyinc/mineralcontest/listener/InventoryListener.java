package eu.billyinc.mineralcontest.listener;

import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import eu.billyinc.mineralcontest.GameState;
import eu.billyinc.mineralcontest.manager.MineralContestManager;
import eu.billyinc.mineralcontest.model.MineralContestChest;
import eu.billyinc.mineralcontest.model.MineralContestPlayer;
import eu.billyinc.mineralcontest.model.MineralContestTeam;

public class InventoryListener implements Listener {
	
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
					MineralContestPlayer mineralContestPlayer = MineralContestManager.getMineralContestPlayerManager().getMineralContestPlayerByUUID(player.getUniqueId());
					if (mineralContestPlayer instanceof MineralContestPlayer) {
						MineralContestTeam mineralContestTeam = MineralContestManager.getApp().getTeamByName(mineralContestPlayer.getTeamName());
						if (mineralContestTeam instanceof MineralContestTeam) {
							mineralContestTeam.setScore(inventoryValue);
						}
					}
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
