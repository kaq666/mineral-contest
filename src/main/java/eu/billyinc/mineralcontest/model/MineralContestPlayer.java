package eu.billyinc.mineralcontest.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;

public class MineralContestPlayer {
	private UUID ID;
	private List<Integer> tasks = new ArrayList<Integer>();
	
	public MineralContestPlayer(UUID ID) {
		this.ID = ID;
	}

	public UUID getID() {
		return ID;
	}

	public void setID(UUID iD) {
		ID = iD;
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
}
