package eu.billyinc.mineralcontest.task;

import eu.billyinc.mineralcontest.GameState;
import eu.billyinc.mineralcontest.manager.MineralContestManager;
import eu.billyinc.mineralcontest.model.MineralContestChest;
import eu.billyinc.mineralcontest.utils.FastBoard;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

public class GameCycle extends BukkitRunnable {

    private List<Integer> arenas = new ArrayList<>();
    private List<Integer> drops = new ArrayList<>();
    private Timer javaTimer = new Timer();
    private GameTimer gameTimer;
    private boolean hasStarted = false;
    
    public static List<Integer> arenasS = new ArrayList<>();
    public static List<Integer> dropsS = new ArrayList<>();

    public GameCycle() {
    	gameTimer = new GameTimer();
        int nbEvent = Math.round(gameTimer.getGAMETIME() / 750);
        for (int i = 0; i <= nbEvent; i++) {
        	arenas.add(new Random().nextInt(gameTimer.getGAMETIME()) + 1);
        	drops.add(new Random().nextInt(gameTimer.getGAMETIME()) + 1);
        }
        
        GameCycle.arenasS = this.arenas;
        GameCycle.dropsS = this.drops;
        
        javaTimer.schedule(this.gameTimer, 0, 1000);
    }
    
    public GameCycle(int time) {
    	gameTimer = new GameTimer(time);
    	int nbEvent = Math.round(gameTimer.getGAMETIME() / 750);
    	for (int i = 0; i <= nbEvent; i++) {
    		arenas.add(new Random().nextInt(gameTimer.getGAMETIME()) + 1);
    		drops.add(new Random().nextInt(gameTimer.getGAMETIME()) + 1);
    	}
    	
    	GameCycle.arenasS = this.arenas;
    	GameCycle.dropsS = this.drops;
    	
    	javaTimer.schedule(this.gameTimer, 0, 1000);
    }

    @Override
    public void run() {
    	if (gameTimer.getRemaingCountdownTime() > 0) {
    		for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(ChatColor.GREEN + String.valueOf(gameTimer.getRemaingCountdownTime()), "Lancement de la partie", 20, 20, 20);
                player.playNote(player.getLocation(), Instrument.BIT, new Note(1));
            }
    	} else {
    		if (!hasStarted) {
    			hasStarted = true;
    			for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendTitle(ChatColor.GREEN + "GO !", "", 5, 10, 20);
                    player.playNote(player.getLocation(), Instrument.BIT, new Note(24));
                }
    			MineralContestManager.getApp().setGameState(GameState.PLAYING);
    		}
    		
    		if (gameTimer.getRemaingGameTime() <= 0) {
    			if (!MineralContestManager.getApp().getGameState().equals(GameState.WAITING)) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle("Fin de la partie", MineralContestManager.getApp().getWinners().getColor() + MineralContestManager.getApp().getWinners().getName() + " gagne la game", 10, 20 * 60, 10);
                        for (FastBoard board : MineralContestManager.getApp().getBoards().values()) {
                            board.delete();
                        }
                        MineralContestManager.getApp().finishGame();
                        MineralContestManager.getApp().setGameState(GameState.WAITING);
                    }
    			}
            } else {
            	MineralContestManager.getApp().updateScoreBoards(gameTimer.getRemaingGameTime());
            	this.checkArena();
            	this.checkDrop();
            }
    	}
    }
    
    private void checkArena() {
    	int time = gameTimer.getRemaingGameTime();
    	if (arenas.contains(time)) {
    		arenas.remove(arenas.indexOf(time));
            MineralContestManager.getApp().spawnArenaChest();
        }
    }
    
    private void checkDrop() {
    	int time = gameTimer.getRemaingGameTime();
    	if (drops.contains(time)) {
    		drops.remove(drops.indexOf(time));
           
        	Location arenaLocation = MineralContestManager.getMineralContestGameManager().getArenaChestLocation();
        	int x = new Random().nextInt(201) - 100;
        	int z = new Random().nextInt(201) - 100;
        	Location dropChestLocation = arenaLocation.clone().add(x, 0, z);
        	
        	MineralContestChest mineralContestChest = new MineralContestChest(dropChestLocation);
        	mineralContestChest.drop();
        }
    }
}
