package eu.billyinc.mineralcontest.task;

import eu.billyinc.mineralcontest.App;
import eu.billyinc.mineralcontest.GameState;
import eu.billyinc.mineralcontest.utils.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ArenaCycle extends BukkitRunnable {

    private App main;
    private int timer = 10000;

    public ArenaCycle(App main) {
        this.main = main;
    }

    @Override
    public void run() {

        if (timer == 0) {
            cancel();
            main.setAreneActive(false);
            Bukkit.broadcastMessage("La téléportation de l'arène n'est plus disponible");
        } else {
            this.timer-= 1000;
        }
    }
}
