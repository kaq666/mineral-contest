package eu.billyinc.mineralcontest.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;

import eu.billyinc.mineralcontest.GameState;
import eu.billyinc.mineralcontest.manager.MineralContestManager;

public class FurnaceListener implements Listener {

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
	
}
