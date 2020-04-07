package eu.billyinc.mineralcontest.task;

import eu.billyinc.mineralcontest.App;
import eu.billyinc.mineralcontest.GameState;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameStarter extends BukkitRunnable {

    private int timer = 10;
    private App main;

    public GameStarter(App main) {
        this.main = main;
    }

    @Override
    public void run() {
        World world = Bukkit.getWorld("world");
        if (timer == 0) {
            cancel();
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(ChatColor.GREEN + "GO !", "", 5, 10, 20);
                player.playNote(player.getLocation(), Instrument.BIT, new Note(24));
            }
            main.setGameState(GameState.PLAYING);
            GameCycle gameCycle = new GameCycle(main);
            gameCycle.runTaskTimer(main, 0, 20);
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(ChatColor.GREEN + String.valueOf(timer), "Lancement de la partie", 20, 20, 20);
                player.playNote(player.getLocation(), Instrument.BIT, new Note(1));
            }
        }

        this.timer--;
    }
}
