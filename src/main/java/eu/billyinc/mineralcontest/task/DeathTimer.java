package eu.billyinc.mineralcontest.task;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.TimerTask;

public class DeathTimer extends TimerTask {

	private int remainingDeathTime = 10;
	private Player player;

	public DeathTimer(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		this.remainingDeathTime--;
		if (remainingDeathTime <= 0) {
			this.cancel();
		}
		this.player.sendTitle(String.valueOf(this.getRemainingDeathTime()), ChatColor.RED + "Réaparition", 20, 20, 20);
	}
	
	public int getRemainingDeathTime() {
		return this.remainingDeathTime;
	}

}
