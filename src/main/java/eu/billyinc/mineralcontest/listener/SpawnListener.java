package eu.billyinc.mineralcontest.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnListener implements Listener {

    private JavaPlugin main;

    public SpawnListener(JavaPlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        player.setGameMode(GameMode.ADVENTURE);
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent e) {

    }
}