package eu.billyinc.mineralcontest.listener;

import eu.billyinc.mineralcontest.App;
import eu.billyinc.mineralcontest.command.TeamCommandExecutor;
import eu.billyinc.mineralcontest.model.PlayerTeam;
import eu.billyinc.mineralcontest.model.Team;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.*;
import org.bukkit.util.Vector;

import java.awt.*;

public class SpawnListener implements Listener {

    private App main;
    private PlayerTeam playerTeam;

    public SpawnListener(App main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        this.playerTeam = new PlayerTeam(player);
        player.setGameMode(GameMode.ADVENTURE);
        // change to team
        main.getCommand("setTeamSpawnLocation").setExecutor(new TeamCommandExecutor(main));
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent e) {

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent clickEvent) {
        Player player = clickEvent.getPlayer();
        if (clickEvent.getClickedBlock() != null && clickEvent.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block button = clickEvent.getClickedBlock();
            Location signLocation = button.getLocation();
            signLocation.add(0,1,0);

            BlockState blockState = signLocation.getBlock().getState();
            // player as chosen a team
            if (blockState instanceof Sign) {
                this.setClickedTeam(blockState, player);
            }
        }

        // player try to reach a safe base
        if(clickEvent.getAction().equals(Action.PHYSICAL)) {
            if (clickEvent.getClickedBlock().getType() == Material.STONE_PRESSURE_PLATE) {
                this.authorize(player);
            }

        }
    }

    private void setClickedTeam(BlockState blockState, Player player) {
        Sign sign = (Sign) blockState;
        if (sign.getLine(0).equals("[Choix des équipes]")) {
            switch (sign.getLine(1)) {
                case "Team Rouge":
                    this.playerTeam.setTeam(main.getTeamByName("Team Rouge"));
                    break;
                case "Team Bleue":
                    this.playerTeam.setTeam(main.getTeamByName("Team Bleue"));
                    break;
                case "Team Jaune":
                    this.playerTeam.setTeam(main.getTeamByName("Team Jaune"));
                    break;
            }
            this.displayScoreBoard(player);
            player.teleport(playerTeam.getTeam().getSpawn());
        }
    }

    private void authorize(Player player) {
        Team team = playerTeam.getTeam();
        if (team != null) {
            if (
                    (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.RED_TERRACOTTA && team != main.getTeamByName("Team Rouge"))
                    || (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.BLUE_TERRACOTTA && team != main.getTeamByName("Team Bleue"))
                    || (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.YELLOW_TERRACOTTA && team != main.getTeamByName("Team Yellow"))
            ) {
                    player.sendMessage("zone non autorisée");
                    // TODO : voir pour le téleporte
                    player.setHealth(0);
            }
        }
    }


    private void displayScoreBoard(final Player player) {
        // TODO : singleton quelques chose comme ça pour ne pas instancier 1000 Runnable
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, () -> {
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
        },0, 20 * 10);
    }
}