package eu.billyinc.mineralcontest.task;

import eu.billyinc.mineralcontest.App;
import eu.billyinc.mineralcontest.GameState;
import eu.billyinc.mineralcontest.manager.MineralContestManager;
import eu.billyinc.mineralcontest.model.MineralContestChest;
import eu.billyinc.mineralcontest.model.PlayerTeam;
import eu.billyinc.mineralcontest.utils.FastBoard;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class GameCycle extends BukkitRunnable {

    private App main;
    private int timer = 100000;
    private List<Integer> arenas = new ArrayList<>();

    public GameCycle(App main) {
        this.main = main;
        int arenaEvent = new Random().nextInt(5) + 2;
        for (int i = 0; i <= arenaEvent; i++) {
                arenas.add(new Random().nextInt(9) * 10000 );
        }
        arenas.add(new Random().nextInt(9) * 1000 );
        System.out.println(arenas);
    }

    @Override
    public void run() {

        if (timer == 0) {
            cancel();

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle("Fin de la partie", main.getWinners().getColor() + main.getWinners().getName() + " gagne la game", 10, 40,10);
                for (FastBoard board : main.getBoards().values()) {
                    board.delete();
                }
                main.finishGame();
                main.setGameState(GameState.WAITING);
            }
        } else {
            System.out.println(timer);
            main.updateScoreBoards(timer);
            this.timer-= 1000;
            if (arenas.contains(timer)) {
                System.out.println("Arena time !!!");
                main.spawnArenaChest();
            }
        }
    }
}
