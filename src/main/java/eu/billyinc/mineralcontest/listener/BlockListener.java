package eu.billyinc.mineralcontest.listener;

import eu.billyinc.mineralcontest.GameState;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import eu.billyinc.mineralcontest.manager.MineralContestManager;
import eu.billyinc.mineralcontest.model.MineralContestChest;

import java.util.ArrayList;
import java.util.List;

public class BlockListener implements Listener {

	private List<Material> protectedMaterials = new ArrayList<>();

	public BlockListener() {
		this.registerProtectedMaterials();
	}

	private void registerProtectedMaterials() {
		this.protectedMaterials.add(Material.GRAY_CONCRETE);
		this.protectedMaterials.add(Material.YELLOW_TERRACOTTA);
		this.protectedMaterials.add(Material.RED_TERRACOTTA);
		this.protectedMaterials.add(Material.BLUE_TERRACOTTA);
		this.protectedMaterials.add(Material.YELLOW_CONCRETE_POWDER);
		this.protectedMaterials.add(Material.RED_WOOL);
		this.protectedMaterials.add(Material.BLUE_WOOL);
	}

	@EventHandler
	public void onBreakChest(BlockBreakEvent e) {
		if (e.getBlock().getState() instanceof Chest) {
			Chest chest = (Chest) e.getBlock().getState();
			if(MineralContestManager.getMineralContestChestManager().getMineralContestChestByChest(chest) instanceof MineralContestChest) {
				MineralContestManager.getMineralContestChestManager().removeMineralContestChest(chest);
			}
		}
		if (this.protectedMaterials.contains(e.getBlock().getType()) && MineralContestManager.getApp().getGameState() == GameState.PLAYING) {
			e.setCancelled(true);
		}
	}

}
