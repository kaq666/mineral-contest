package eu.billyinc.mineralcontest.listener;

import eu.billyinc.mineralcontest.App;
import eu.billyinc.mineralcontest.model.PlayerTeam;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.*;

public class SpawnListener implements Listener {

    private App main;

    public SpawnListener(App main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        final PlayerTeam playerTeam = new PlayerTeam(player);
        player.setGameMode(GameMode.ADVENTURE);
        player.sendRawMessage("Bienvenu fréro");

        // this need to be chosen by the player
        playerTeam.setTeam(main.getTeamByName("Team Rouge"));


        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
            public void run() {
                ScoreboardManager manager = Bukkit.getScoreboardManager();
                final Scoreboard board = manager.getNewScoreboard();
                final Objective objective = board.registerNewObjective("test", "dummy", "Display Name");
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                objective.setDisplayName(playerTeam.getTeam().getColoredName());
                for (Player teamPlayer : playerTeam.getTeam().getPlayers()) {
                    Score teamPlayerScore = objective.getScore(teamPlayer.getName());
                    teamPlayerScore.setScore(0);
                }

                Score score3 = objective.getScore("§fTotal");
                score3.setScore(1580);
                player.setScoreboard(board);
            }
        },0, 20 * 10);
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent e) {

    }
}