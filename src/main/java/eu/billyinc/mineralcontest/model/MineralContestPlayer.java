package eu.billyinc.mineralcontest.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.UUID;

import eu.billyinc.mineralcontest.task.DeathTimer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MineralContestPlayer {
	private UUID ID;
	private Player player;
	private String teamName;
	private List<Integer> tasks = new ArrayList<Integer>();
	private Timer javaTimer = new Timer();
	private DeathTimer deathTimer = null;
	
	public MineralContestPlayer(UUID ID, Player player) {
		this.ID = ID;
		this.player = player;
	}

	public UUID getID() {
		return ID;
	}

	public void setID(UUID iD) {
		ID = iD;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public String getTeamName() {
		return this.teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public List<Integer> getTasks() {
		return tasks;
	}

	public void setTasks(List<Integer> tasks) {
		this.tasks = tasks;
	}

	public void addTask(int task) {
		this.tasks.add(task);
	}
	
	public void removeTask(int task) {
		this.tasks.remove(task);
		Bukkit.getScheduler().cancelTask(task);
	}
	
	public void removeAllTasks() {
		for (Integer task : this.tasks) {
			Bukkit.getScheduler().cancelTask(task);
		}
	}

	public boolean isDead() {
		if (this.deathTimer == null) {
			return false;
		}
		return this.deathTimer.getRemainingDeathTime() > 0;
	}

	public void kill() {
		this.deathTimer = new DeathTimer(this.player);
		javaTimer.schedule(this.deathTimer, 0, 1000);
	}
}
