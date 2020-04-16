package eu.billyinc.mineralcontest.task;

import eu.billyinc.mineralcontest.GameState;
import eu.billyinc.mineralcontest.manager.MineralContestManager;
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
    private Timer javaTimer = new Timer();
    private GameTimer gameTimer = new GameTimer();
    private boolean hasStarted = false;

    public GameCycle() {
        int arenaEvent = new Random().nextInt(5) + 2;
        for (int i = 0; i <= arenaEvent; i++) {
                arenas.add(new Random().nextInt(9) * 10000 );
        }
        arenas.add(new Random().nextInt(9) * 1000 );
        System.out.println(arenas);
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
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendTitle("Fin de la partie", MineralContestManager.getApp().getWinners().getColor() + MineralContestManager.getApp().getWinners().getName() + " gagne la game", 10, 40,10);
                    for (FastBoard board : MineralContestManager.getApp().getBoards().values()) {
                        board.delete();
                    }
                    MineralContestManager.getApp().finishGame();
                    MineralContestManager.getApp().setGameState(GameState.WAITING);
                }
            } else {
            	MineralContestManager.getApp().updateScoreBoards(gameTimer.getRemaingGameTime());
                if (arenas.contains(gameTimer.getRemaingGameTime())) {
                	arenas.remove(gameTimer.getRemaingGameTime());
                    System.out.println("Arena time !!!");
                    MineralContestManager.getApp().spawnArenaChest();
                }
            }
    	}
    }
}
