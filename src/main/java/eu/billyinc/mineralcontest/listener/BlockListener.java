package eu.billyinc.mineralcontest.listener;

import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import eu.billyinc.mineralcontest.manager.MineralContestManager;
import eu.billyinc.mineralcontest.model.MineralContestChest;

public class BlockListener implements Listener {
	
	@EventHandler
	public void onBreakChest(BlockBreakEvent e) {
		if (e.getBlock().getState() instanceof Chest) {
			Chest chest = (Chest) e.getBlock().getState();
			if(MineralContestManager.getMineralContestChestManager().getMineralContestChestByChest(chest) instanceof MineralContestChest) {
				MineralContestManager.getMineralContestChestManager().removeMineralContestChest(chest);
			}
		}
	}

}
