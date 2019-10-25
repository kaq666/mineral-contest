package eu.billyinc.mineralcontest.listener;

import eu.billyinc.mineralcontest.App;
import eu.billyinc.mineralcontest.model.PlayerTeam;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.*;
import org.bukkit.util.Vector;

import java.awt.*;

public class SpawnListener implements Listener {

    private App main;

    public SpawnListener(App main) {
        this.main = main;
    }
    private PlayerTeam playerTeam;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        this.playerTeam = new PlayerTeam(player);
        player.setGameMode(GameMode.ADVENTURE);
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent e) {

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent clickEvent) {
        if (clickEvent.getClickedBlock() != null && clickEvent.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = clickEvent.getPlayer();
            Block button = clickEvent.getClickedBlock();

            Location signLocation = button.getLocation();
            signLocation.add(0,1,0);

            BlockState blockState = signLocation.getBlock().getState();
            if (blockState instanceof Sign) {
                Sign sign = (Sign) blockState;
                if (sign.getLine(0).equals("[Choix des équipes]")) {
                    // TODO : Passé les if en switch
                    if (sign.getLine(1).equals("Team Rouge")) {
                        this.playerTeam.setTeam(main.getTeamByName("Team Rouge"));
                    }
                    if (sign.getLine(1).equals("Team Jaune")) {
                        this.playerTeam.setTeam(main.getTeamByName("Team Jaune"));
                    }
                    if (sign.getLine(1).equals("Team Bleue")) {
                        this.playerTeam.setTeam(main.getTeamByName("Team Bleue"));
                    }
                    this.displayScoreBoard(player);
                }
            }
        }
    }


    private void displayScoreBoard(final Player player) {
        // TODO : singleton quelques chose comme ça pour ne pas instancier 1000 Runnable
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
}