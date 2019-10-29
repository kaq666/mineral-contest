package eu.billyinc.mineralcontest.task;

import eu.billyinc.mineralcontest.App;
import eu.billyinc.mineralcontest.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

        if (timer == 0) {
            Bukkit.broadcastMessage("Lancement de la partie");
            main.setGameState(GameState.PLAYING);
            cancel();
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(ChatColor.GREEN + String.valueOf(timer), "Lancement de la partie", 10, 20, 10);
        }

        timer--;
    }
}
