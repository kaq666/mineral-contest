package eu.billyinc.mineralcontest;

import eu.billyinc.mineralcontest.listener.SpawnListener;
import eu.billyinc.mineralcontest.model.MineralContestChest;
import eu.billyinc.mineralcontest.model.PlayerTeam;
import eu.billyinc.mineralcontest.model.Team;
import eu.billyinc.mineralcontest.task.ArenaCycle;
import eu.billyinc.mineralcontest.utils.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.*;

import eu.billyinc.mineralcontest.command.MineralContestCommand;
import eu.billyinc.mineralcontest.listener.MineralContestListener;
import eu.billyinc.mineralcontest.manager.MineralContestManager;

/**
 * Hello world!
 *
 */
public class App extends JavaPlugin {

    private List<Team> teams = new ArrayList<Team>();
    private final Map<UUID, FastBoard> boards = new HashMap<>();
    private final Map<UUID, PlayerTeam> playerTeamMap = new HashMap<>();
    private GameState gameState = GameState.WAITING;
    private boolean areneActive = false;

	@Override
    public void onEnable() {
        super.onEnable();
        System.out.println("Mineral Contest Enabled");
        SpawnListener spawnListener = new SpawnListener(this);
        this.getServer().getPluginManager().registerEvents(spawnListener, this);
        this.registerTeams();
        MineralContestManager.setApp(this);
        getCommand("mc").setExecutor(new MineralContestCommand(this));
        getServer().getPluginManager().registerEvents(new MineralContestListener(this), this);
    }



    @Override
    public void onDisable() {
        super.onDisable();
        System.out.println("Mineral Contest Disabled");
    }

    private void registerTeams() {
	    this.teams.add(new Team("Team Bleue", ChatColor.BLUE,  new Location(Bukkit.getServer().getWorld("World"),-352, 65, -342, 180f, 1.9f)));
	    this.teams.add(new Team("Team Jaune", ChatColor.YELLOW, new Location(Bukkit.getServer().getWorld("World"),-233, 65, -342, 90f, 1.5f)));
	    this.teams.add(new Team("Team Rouge", ChatColor.RED, new Location(Bukkit.getServer().getWorld("World"),-278, 65, -387, -90f, 0f)));
    }

    public Team getTeamByName(String name) {
	    for (Team team : this.teams) {
	        if (team.getName().equals(name)) {
	            return team;
            }
        }
	    return null;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public boolean allTeamAsAPlayer() {
	    //TODO : remove always true condition
        return true;
//	    for (Team team : this.teams) {
//	        if (team.getPlayers().isEmpty()) {
//	            return false;
//            }
//        }
//	    return true;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Map<UUID, FastBoard> getBoards() {
        return boards;
    }

    public Map<UUID, PlayerTeam> getPlayerTeamMap() {
        return playerTeamMap;
    }

    public boolean isAreneActive() {
        return areneActive;
    }

    public void setAreneActive(boolean areneActive) {
        this.areneActive = areneActive;
    }

    public void updateScoreBoards(int timer) {
        for (FastBoard board : this.getBoards().values()) {
            PlayerTeam playerTeam = this.getPlayerTeamMap().get(board.getPlayer().getUniqueId());
            Collection<String> lines = new ArrayList<>();
            
            int minutes = ~~((timer % 3600) / 60);
            int secondes = ~~timer % 60;
            
            lines.add("Timer : " + minutes + ":" + secondes);
            for (Player player : playerTeam.getTeam().getPlayers()) {
            	player.sendMessage("t" + timer);
                int score = this.getInventoryValue(player);
                lines.add(player.getDisplayName() + ": " + score + " Points");
                lines.add("");
            }
            lines.add("Total : " + playerTeam.getTeam().getScore());
            board.updateLines(lines);
        }
    }

    public void spawnArenaChest() {
        if (this.getGameState() == GameState.PLAYING) {
            MineralContestChest mcChest = new MineralContestChest(MineralContestManager.getMineralContestGameManager().getArenaChestLocation());
            mcChest.spawn();

            this.setAreneActive(true);
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(ChatColor.DARK_PURPLE + "Arène", "une coffre d'arène est disponible", 10, 10, 20);
            }
            ArenaCycle arenaCycle = new ArenaCycle(this);
            arenaCycle.runTaskTimer(this, 0, 20);
        }
    }

    public void finishGame() {
        this.gameState = GameState.FINISH;
        this.resetTeams();
        this.boards.clear();
        this.playerTeamMap.clear();
    }

    private int getInventoryValue(Player player) {
	    int inventoryValue = 0;
        Inventory inventory = player.getInventory();
        for (ItemStack itemStack : inventory.getContents()) {
            inventoryValue+= this.getItemStackValue(itemStack);
        }
        return inventoryValue;
    }

    public int getItemStackValue(ItemStack itemStack) {
	    int res = 0;
        if (itemStack != null) {
            switch (itemStack.getType()) {
                case IRON_INGOT:
                    res= itemStack.getAmount() * 10;
                    break;
                case GOLD_INGOT:
                    res = itemStack.getAmount() * 50;
                    break;
                case DIAMOND:
                    res = itemStack.getAmount() * 150;
                    break;
                case EMERALD:
                    res = itemStack.getAmount() * 300;
                    break;
            }
        }
        return res;
    }

    public void resetTeams() {
	    for (Team team : this.teams) {
	        team.getPlayers().clear();
        }
    }

    public Team getWinners() {
	    int max = 0;
	    Team winners = this.teams.get(0);
	    for (Team team : this.teams) {
	        if (team.getScore() > max) {
	            max = team.getScore();
	            winners = team;
            }
        }
        return winners;
    }
}
