package eu.billyinc.mineralcontest.listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import eu.billyinc.mineralcontest.GameState;
import eu.billyinc.mineralcontest.manager.MineralContestManager;
import eu.billyinc.mineralcontest.model.MineralContestChest;
import eu.billyinc.mineralcontest.model.MineralContestPlayer;
import eu.billyinc.mineralcontest.model.MineralContestTeam;
import eu.billyinc.mineralcontest.utils.FastBoard;

public class PlayerListener implements Listener {

	@EventHandler
	public void onOpeningMineralContestChest(PlayerInteractEvent e) {
		if (e.getClickedBlock() != null) {
			if (e.getClickedBlock() instanceof Block && e.getClickedBlock().getState() instanceof Chest && e.getPlayer() instanceof Player) {
				Chest chest = (Chest) e.getClickedBlock().getState();
				MineralContestChest mcChest = MineralContestManager.getMineralContestChestManager().getMineralContestChestByChest(chest);

				if(mcChest instanceof MineralContestChest && !mcChest.isHasBeenTransfered()) {
					e.setCancelled(true);

					Player player = (Player) e.getPlayer();
					MineralContestPlayer mineralContestPlayer = MineralContestManager.getMineralContestPlayerManager().getMineralContestPlayerByUUID(player.getUniqueId());

					Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER, "Unlocking chest...");
					player.openInventory(inventory);

					for (int i = 0; i < inventory.getSize(); i++) {
						inventory.setItem(i, new ItemStack(Material.RED_WOOL, 1));

						final int x = i;
						mineralContestPlayer.addTask(
								Bukkit.getScheduler().runTaskLater(MineralContestManager.getApp(), () -> {
									inventory.setItem(x, new ItemStack(Material.GREEN_WOOL, 1));
									player.playNote(player.getLocation(), Instrument.BIT, new Note(1));
									chest.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, chest.getLocation(), 100);
								}, 20 * (i + 1))
										.getTaskId());
					}

					mineralContestPlayer.addTask(
							Bukkit.getScheduler().runTaskLater(MineralContestManager.getApp(), () -> {
								player.closeInventory();
								mcChest.transferTo(player);
							}, 20 * 5).getTaskId());
				}
			}
		}
		
		if (e.getClickedBlock() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block button = e.getClickedBlock();
            Location signLocation = button.getLocation();
            signLocation.add(0,1,0);

            BlockState blockState = signLocation.getBlock().getState();
            // player as chosen a team
            if (blockState instanceof Sign && MineralContestManager.getApp().getGameState() == GameState.WAITING) {
                this.setClickedTeam(blockState, e.getPlayer());
            }
        }

        // player try to reach a safe base
        if(e.getAction().equals(Action.PHYSICAL)) {
            if (e.getClickedBlock().getType() == Material.STONE_PRESSURE_PLATE) {
                this.authorize(e.getPlayer());
            }

        }
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		MineralContestPlayer mineralContestPlayer = new MineralContestPlayer(e.getPlayer().getUniqueId(), e.getPlayer());
		MineralContestManager.getMineralContestPlayerManager().addMineralContestPlayer(mineralContestPlayer);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		MineralContestPlayer mineralContestPlayer = MineralContestManager.getMineralContestPlayerManager().getMineralContestPlayerByUUID(e.getPlayer().getUniqueId());
		FastBoard board = MineralContestManager.getApp().getBoards().remove(e.getPlayer().getUniqueId());
		 
		if (board != null) {
            board.delete();
        }
		
		if(mineralContestPlayer instanceof MineralContestPlayer) {
			MineralContestManager.getMineralContestPlayerManager().removeMineralContestPlayer(mineralContestPlayer);
		}
	}
	
	@EventHandler
	public void onPlayerDead(PlayerDeathEvent e) {
		if (MineralContestManager.getApp().getGameState() == GameState.PLAYING) {
			Player player = e.getEntity();
			List<ItemStack> drops = e.getDrops();
			drops.clear();
			for (ItemStack item : player.getInventory()) {
				if (item instanceof ItemStack) {
					if (
							item.getType().equals(Material.EMERALD) ||
							item.getType().equals(Material.GOLD_INGOT) ||
							item.getType().equals(Material.GOLD_ORE) ||
							item.getType().equals(Material.DIAMOND) ||
							item.getType().equals(Material.IRON_INGOT) ||
							item.getType().equals(Material.IRON_ORE)
					) {
						drops.add(item);
					}
				}
			}
			e.setKeepLevel(true);
		}
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player player = e.getPlayer();
		if (MineralContestManager.getApp().getGameState() == GameState.PLAYING) {
			MineralContestPlayer mineralContestPlayer = MineralContestManager.getMineralContestPlayerManager().getMineralContestPlayerByUUID(player.getUniqueId());
			if (mineralContestPlayer instanceof MineralContestPlayer) {
				MineralContestTeam mineralContestTeam = MineralContestManager.getApp().getTeamByName(mineralContestPlayer.getTeamName());
				if (mineralContestTeam instanceof MineralContestTeam) {
					e.setRespawnLocation(mineralContestTeam.getSpawn());
				}
			}
			
			player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
			player.getInventory().setItem(1, new ItemStack(Material.BOW));
			player.getInventory().setItem(2, new ItemStack(Material.ARROW, 64));
			// equip iron armor
			player.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
			player.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
			player.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			player.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
		}
	}
	
	private void setClickedTeam(BlockState blockState, Player player) {
		MineralContestPlayer mineralContestPlayer = MineralContestManager.getMineralContestPlayerManager().getMineralContestPlayerByUUID(player.getUniqueId());
		if (mineralContestPlayer instanceof MineralContestPlayer) {
			Sign sign = (Sign) blockState;
	        if (sign.getLine(0).equals("[Choix équipes]")) {

	            MineralContestTeam mineralContestTeam = MineralContestManager.getApp().getTeamByName(sign.getLine(1));
	            if (mineralContestTeam instanceof MineralContestTeam) {
	            	mineralContestPlayer.setTeamName(mineralContestTeam.getName());
	            	this.displayScoreBoard(player, mineralContestPlayer, mineralContestTeam);
	                player.teleport(mineralContestTeam.getSpawn());
	                player.sendTitle(mineralContestTeam.getColor() + mineralContestTeam.getName(), ChatColor.WHITE + "Prepare for battle", 10, 70, 20);
	            }
	        }
		}
    }

    private void authorize(Player player) {
    	MineralContestPlayer mineralContestPlayer = MineralContestManager.getMineralContestPlayerManager().getMineralContestPlayerByUUID(player.getUniqueId());
    	if (mineralContestPlayer instanceof MineralContestPlayer) {
    		MineralContestTeam mineralContestTeam = MineralContestManager.getApp().getTeamByName(mineralContestPlayer.getTeamName());
    		if (mineralContestTeam instanceof MineralContestTeam) {
    			if (mineralContestTeam != null) {
    	            if (
    	                    (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.RED_TERRACOTTA && mineralContestTeam != MineralContestManager.getApp().getTeamByName("Team Rouge"))
    	                    || (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.BLUE_TERRACOTTA && mineralContestTeam != MineralContestManager.getApp().getTeamByName("Team Bleue"))
    	                    || (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.YELLOW_TERRACOTTA && mineralContestTeam != MineralContestManager.getApp().getTeamByName("Team Jaune"))
    	            ) {
    	                    player.sendMessage("zone non autorisée");
    	                    // TODO : voir pour le téleporte
    	                    player.setHealth(0);
    	            }
    	        }
    		}
    	}
    }
    
    private void displayScoreBoard(final Player player, MineralContestPlayer mineralContestPlayer, MineralContestTeam mineralContestTeam) {
        Collection<String> lines =  new ArrayList<String>();
        lines.add("00:00");
        
        for (Player playerInTeam : mineralContestTeam.getPlayers()) {
            lines.add(playerInTeam.getName() + " : Score");
            lines.add("");
        }

        lines.add("Total : " + mineralContestTeam.getScore());
        MineralContestManager.getApp().updateScoreBoards(0);

        FastBoard board = new FastBoard(player);
        board.updateTitle(mineralContestTeam.getColoredName());
        board.updateLines(lines);
        MineralContestManager.getApp().getBoards().put(player.getUniqueId(), board);
    }
}
