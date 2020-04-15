package eu.billyinc.mineralcontest.task;

import eu.billyinc.mineralcontest.App;
import eu.billyinc.mineralcontest.GameState;
import eu.billyinc.mineralcontest.model.PlayerTeam;
import eu.billyinc.mineralcontest.utils.FastBoard;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

public class GameCycle extends BukkitRunnable {

    private App main;
    private int timer = 30000;

    public GameCycle(App main) {
        this.main = main;
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
            main.updateScoreBoards(timer);
            this.timer-= 1000;
        }
    }
}
