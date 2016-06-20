package com.trenton;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Achievement;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.trenton.achievementrewards.util.SerializableFileManager;

public class AchievementRewards extends JavaPlugin implements Listener {

	private static HashMap<Achievement, ItemStack> REWARDS = new HashMap<Achievement, ItemStack>();

	private static HashMap<String, ArrayList<Achievement>> MISSED_MAP;

	@Override 
	public void onEnable() {
		loadRewards();
		MISSED_MAP = SerializableFileManager.loadMissedAchievementMap();
		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {
		if (MISSED_MAP != null && !MISSED_MAP.isEmpty())
			SerializableFileManager.saveMissedAchievementMap(MISSED_MAP);
	}

	public void loadRewards() {
		REWARDS.put(Achievement.OPEN_INVENTORY, new ItemStack(Material.BED, 1));
		REWARDS.put(Achievement.MINE_WOOD, new ItemStack(Material.APPLE, 5));
		REWARDS.put(Achievement.BUILD_WORKBENCH, new ItemStack(Material.TORCH, 16));
		REWARDS.put(Achievement.BUILD_PICKAXE, new ItemStack(Material.CHEST, 2));
		REWARDS.put(Achievement.BUILD_BETTER_PICKAXE, new ItemStack(Material.WATER_BUCKET, 1));
		REWARDS.put(Achievement.BUILD_FURNACE, new ItemStack(Material.COAL, 8));
		REWARDS.put(Achievement.ACQUIRE_IRON, new ItemStack(Material.IRON_CHESTPLATE, 1));
		REWARDS.put(Achievement.BUILD_HOE, new ItemStack(Material.SEEDS, 32));
		REWARDS.put(Achievement.MAKE_BREAD, new ItemStack(Material.BAKED_POTATO, 16));
		REWARDS.put(Achievement.BAKE_CAKE, new ItemStack(Material.RABBIT_STEW, 16));
		REWARDS.put(Achievement.COOK_FISH, new ItemStack(Material.GOLDEN_CARROT, 1));
		REWARDS.put(Achievement.ON_A_RAIL, new ItemStack(Material.RAILS, 32));
		REWARDS.put(Achievement.BUILD_SWORD, new ItemStack(Material.LEATHER_CHESTPLATE, 1));
		REWARDS.put(Achievement.KILL_ENEMY, new ItemStack(Material.LEATHER_LEGGINGS, 1));
		REWARDS.put(Achievement.KILL_COW, new ItemStack(Material.LEATHER_HELMET, 1));
		REWARDS.put(Achievement.FLY_PIG, new ItemStack(Material.GOLDEN_APPLE, 1));
		REWARDS.put(Achievement.SNIPE_SKELETON, new ItemStack(Material.ARROW, 64));
		REWARDS.put(Achievement.GET_DIAMONDS, new ItemStack(Material.DIAMOND_SPADE, 1));
		REWARDS.put(Achievement.NETHER_PORTAL, new ItemStack(Material.GLOWSTONE, 8));
		REWARDS.put(Achievement.GHAST_RETURN, new ItemStack(Material.GHAST_TEAR, 10));
		REWARDS.put(Achievement.GET_BLAZE_ROD, new ItemStack(Material.MAGMA_CREAM, 6));
		REWARDS.put(Achievement.BREW_POTION, new ItemStack(Material.NETHER_WARTS, 16));
		REWARDS.put(Achievement.END_PORTAL, new ItemStack(Material.DRAGON_EGG, 1));
		REWARDS.put(Achievement.THE_END, new ItemStack(Material.ENDER_STONE, 32));
		REWARDS.put(Achievement.ENCHANTMENTS, new ItemStack(Material.EXP_BOTTLE, 16));
		REWARDS.put(Achievement.OVERKILL, new ItemStack(Material.DIAMOND_SWORD, 1));
		REWARDS.put(Achievement.BOOKCASE, new ItemStack(Material.BOOKSHELF, 5));
		REWARDS.put(Achievement.EXPLORE_ALL_BIOMES, new ItemStack(Material.EMERALD, 32));
		REWARDS.put(Achievement.SPAWN_WITHER, new ItemStack(Material.OBSIDIAN, 32));
		REWARDS.put(Achievement.KILL_WITHER, new ItemStack(Material.NETHER_STAR, 1));
		REWARDS.put(Achievement.FULL_BEACON, new ItemStack(Material.DIAMOND_BLOCK, 1));
		REWARDS.put(Achievement.BREED_COW, new ItemStack(Material.RAW_BEEF, 16));
		REWARDS.put(Achievement.DIAMONDS_TO_YOU, new ItemStack(Material.DIAMOND, 3));
		REWARDS.put(Achievement.OVERPOWERED, new ItemStack(Material.GOLDEN_APPLE, 16));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			if (command.getName().equalsIgnoreCase("claim")) {
				checkMissedReward((Player) sender);
				return true;
			}
		}
		return false;
	}

	@EventHandler
	public void achievementEarned(PlayerAchievementAwardedEvent event) {
		ItemStack reward = REWARDS.get(event.getAchievement());
		if (reward != null) {
			if (event.getPlayer().getInventory().firstEmpty() != -1) {
				event.getPlayer().getInventory().addItem(reward);
				event.getPlayer().sendMessage("Congratulations! You recieved " + reward.getAmount() + " " + reward.getType().name().toLowerCase().replace("_", " ") + "!");
			} else {
				addMissedAchievement(event.getPlayer(), event.getAchievement());
				event.getPlayer().sendMessage("You missed out on a reward for an achievement! Do /claim with enough inventory space!");
			}
		} else {
			event.getPlayer().sendMessage("No reward added for: " + event.getAchievement().name() + ".");
		}
	}
	
	public void checkMissedReward(Player player) {
		if (!MISSED_MAP.containsKey(player.getName())) {
			player.sendMessage("No missed achievements found.");
		} else {
			if (player.getInventory().firstEmpty() != -1) {
				Achievement achievement = MISSED_MAP.get(player.getName()).get(0);
				if (achievement != null) {
					ItemStack reward = REWARDS.get(achievement);
					player.getInventory().addItem(reward);
					player.sendMessage("Congratulations! You recieved " + reward.getAmount() + " " + reward.getType().name().toLowerCase().replace("_", " ") + "!");
					MISSED_MAP.get(player.getName()).remove(achievement);
				} else {
					MISSED_MAP.remove(player.getName());
				}
				SerializableFileManager.saveMissedAchievementMap(MISSED_MAP);
			} else {
				player.sendMessage("Your inventory is full. Clear up some space!");
			}
		}
	}

	public void addMissedAchievement(Player player, Achievement achievement) {
		if (!MISSED_MAP.containsKey(player.getName())) {
			ArrayList<Achievement> list = new ArrayList<Achievement>();
			MISSED_MAP.put(player.getName(), list);
		} else {
			ArrayList<Achievement> list = MISSED_MAP.get(player.getName());
			list.add(achievement);
		}
		SerializableFileManager.saveMissedAchievementMap(MISSED_MAP);
	}
}
