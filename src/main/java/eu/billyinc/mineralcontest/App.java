package eu.billyinc.mineralcontest;

import eu.billyinc.mineralcontest.model.MineralContestChest;
import eu.billyinc.mineralcontest.model.MineralContestPlayer;
import eu.billyinc.mineralcontest.model.MineralContestTeam;
import eu.billyinc.mineralcontest.utils.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import eu.billyinc.mineralcontest.command.MineralContestCommand;
import eu.billyinc.mineralcontest.listener.BlockListener;
import eu.billyinc.mineralcontest.listener.EntityListener;
import eu.billyinc.mineralcontest.listener.FurnaceListener;
import eu.billyinc.mineralcontest.listener.InventoryListener;
import eu.billyinc.mineralcontest.listener.PlayerListener;
import eu.billyinc.mineralcontest.manager.MineralContestManager;

/**
 * Hello world!
 *
 */
public class App extends JavaPlugin {

    private List<MineralContestTeam> teams = new ArrayList<MineralContestTeam>();
    private final Map<UUID, FastBoard> boards = new HashMap<>();
    private GameState gameState = GameState.WAITING;

	@Override
    public void onEnable() {
        super.onEnable();
        System.out.println("Mineral Contest Enabled");
        MineralContestManager.setApp(this);
        
        this.getCommand("mc").setExecutor(new MineralContestCommand());
        
        this.getServer().getPluginManager().registerEvents(new BlockListener(), this);
        this.getServer().getPluginManager().registerEvents(new EntityListener(), this);
        this.getServer().getPluginManager().registerEvents(new FurnaceListener(), this);
        this.getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        
        this.registerTeams();
    }



    @Override
    public void onDisable() {
        super.onDisable();
        System.out.println("Mineral Contest Disabled");
    }

    private void registerTeams() {
	    this.teams.add(new MineralContestTeam("Team Bleue", ChatColor.BLUE,  new Location(Bukkit.getServer().getWorld("World"),-352, 65, -342, 180f, 1.9f)));
	    this.teams.add(new MineralContestTeam("Team Jaune", ChatColor.YELLOW, new Location(Bukkit.getServer().getWorld("World"),-233, 65, -342, 90f, 1.5f)));
	    this.teams.add(new MineralContestTeam("Team Rouge", ChatColor.RED, new Location(Bukkit.getServer().getWorld("World"),-278, 65, -387, -90f, 0f)));
    }

    public MineralContestTeam getTeamByName(String name) {
	    for (MineralContestTeam team : this.teams) {
	        if (team.getName().equals(name)) {
	            return team;
            }
        }
	    return null;
    }

    public List<MineralContestTeam> getTeams() {
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

    public void updateScoreBoards(int timer) {
        for (FastBoard board : this.getBoards().values()) {
        	MineralContestPlayer mineralContestPlayer = MineralContestManager.getMineralContestPlayerManager().getMineralContestPlayerByUUID(board.getPlayer().getUniqueId());
            if (mineralContestPlayer instanceof MineralContestPlayer) {
            	MineralContestTeam mineralContestTeam = MineralContestManager.getApp().getTeamByName(mineralContestPlayer.getTeamName());
            	System.out.println(mineralContestPlayer.getTeamName());
            	if (mineralContestTeam instanceof MineralContestTeam) {
            		System.out.println("oh yes");
                	Collection<String> lines = new ArrayList<>();
                    
                    int minutes = ~~((timer % 3600) / 60);
                    int secondes = ~~timer % 60;
                    
                    if (secondes < 10) {
                    	lines.add("Timer : " + minutes + ":" + "0" + secondes);
                    } else {
                    	lines.add("Timer : " + minutes + ":" + secondes);
                    }
                    
                	for (Player player : mineralContestTeam.getPlayers()) {
                		int score = this.getInventoryValue(player);
                        lines.add(player.getDisplayName() + ": " + score + " Points");
                        lines.add("");
                	}
                    
                    lines.add("Total : " + mineralContestTeam.getScore());
                    board.updateLines(lines);
            	}
            }
        }
    }

    public void spawnArenaChest() {
        if (this.getGameState() == GameState.PLAYING) {
            MineralContestChest mcChest = new MineralContestChest(MineralContestManager.getMineralContestGameManager().getArenaChestLocation(), true);
            mcChest.spawn();
        }
    }

    public void finishGame() {
        this.gameState = GameState.FINISH;
        this.resetTeams();
        this.boards.clear();
        MineralContestManager.getMineralContestPlayerManager().getPlayers().clear();
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
                default:
                	break;
            }
        }
        return res;
    }

    public void resetTeams() {
	    for (MineralContestTeam team : this.teams) {
	        team.getPlayers().clear();
        }
    }

    public MineralContestTeam getWinners() {
	    int max = 0;
	    MineralContestTeam winners = this.teams.get(0);
	    for (MineralContestTeam team : this.teams) {
	        if (team.getScore() > max) {
	            max = team.getScore();
	            winners = team;
            }
        }
        return winners;
    }
}
