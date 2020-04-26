package eu.billyinc.mineralcontest.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import eu.billyinc.mineralcontest.manager.MineralContestManager;
import eu.billyinc.mineralcontest.model.MineralContestPlayer;

public class EntityListener implements Listener {
	
	@EventHandler
	public void onPlayerDamaged(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			MineralContestPlayer mineralContestPlayer = MineralContestManager.getMineralContestPlayerManager().getMineralContestPlayerByUUID(player.getUniqueId());
			
			player.closeInventory();
			mineralContestPlayer.removeAllTasks();

			if (this.isInArena(e.getEntity().getLocation())) {
				e.setCancelled(true);
			}
		}
	}

	private boolean isInArena(Location location) {
		Location arenaLocation = MineralContestManager.getMineralContestGameManager().getArenaLocation();
		double maxX = arenaLocation.getX() + 5;
		double minX = arenaLocation.getX() - 5;
		double maxY = arenaLocation.getY() + 3;
		double minY = arenaLocation.getY() - 3;
		double maxZ = arenaLocation.getZ() + 5;
		double minZ = arenaLocation.getZ() - 5;
		return location.getX() <= maxX && location.getX() >= minX &&
				location.getY() <= maxY && location.getY() >= minY &&
				location.getZ() <= maxZ && location.getZ() >= minZ;
	}
	
}
