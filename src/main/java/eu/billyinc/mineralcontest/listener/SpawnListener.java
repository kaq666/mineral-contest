package eu.billyinc.mineralcontest.listener;

import eu.billyinc.mineralcontest.App;
import eu.billyinc.mineralcontest.GameState;
import eu.billyinc.mineralcontest.command.TeamCommandExecutor;
import eu.billyinc.mineralcontest.model.PlayerTeam;
import eu.billyinc.mineralcontest.model.Team;
import eu.billyinc.mineralcontest.utils.FastBoard;
import org.bukkit.*;
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
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Collection;

public class SpawnListener implements Listener {

    private App main;

    public SpawnListener(App main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        PlayerTeam playerTeam = new PlayerTeam(player);
        main.getPlayerTeamMap().put(player.getUniqueId(), playerTeam);
        player.setGameMode(GameMode.ADVENTURE);
        // change to team
        main.getCommand("setTeamSpawnLocation").setExecutor(new TeamCommandExecutor(main));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        FastBoard board = main.getBoards().remove(p.getUniqueId());
        main.getPlayerTeamMap().remove(p.getUniqueId());

        if (board != null) {
            board.delete();
        }
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
            if (blockState instanceof Sign && main.getGameState() == GameState.WAITING) {
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
        PlayerTeam playerTeam = main.getPlayerTeamMap().get(player.getUniqueId());
        Sign sign = (Sign) blockState;
        if (sign.getLine(0).equals("[Choix des équipes]")) {
            // if the player is already in a team, remove it first
            for (Team team : main.getTeams()) {
                team.getPlayers().remove(player);
            }
            switch (sign.getLine(1)) {
                case "Team Rouge":
                    playerTeam.setTeam(main.getTeamByName("Team Rouge"));
                    break;
                case "Team Bleue":
                    playerTeam.setTeam(main.getTeamByName("Team Bleue"));
                    break;
                case "Team Jaune":
                    playerTeam.setTeam(main.getTeamByName("Team Jaune"));
                    break;
            }
            this.displayScoreBoard(player, playerTeam);
            player.teleport(playerTeam.getTeam().getSpawn());
        }
    }

    private void authorize(Player player) {
        Team team = main.getPlayerTeamMap().get(player.getUniqueId()).getTeam();
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


    private void displayScoreBoard(final Player player, PlayerTeam playerTeam) {
        Collection<String> lines =  new ArrayList<String>();
        lines.add("00:00");
        for (Player teamPlayer : playerTeam.getTeam().getPlayers()) {
            lines.add(teamPlayer.getName() + " : Score");
            lines.add("");
        }

        lines.add("Total : " + playerTeam.getTeam().getScore());
        main.updateScoreBoards(0);

        FastBoard board = new FastBoard(player);
        board.updateTitle(playerTeam.getTeam().getColoredName());
        board.updateLines(lines);
        main.getBoards().put(player.getUniqueId(), board);
    }

}