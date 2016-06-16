package com.trenton.achievementrewards;

import java.util.HashMap;

import org.bukkit.Achievement;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class AchievementRewards extends JavaPlugin implements Listener {
	
private static HashMap<Achievement, ItemStack> REWARDS = new HashMap<Achievement, ItemStack>();

	@Override
	public void onEnable() {
		initRewards();
		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {

	}
	
	public void initRewards() {
		REWARDS.put(Achievement.OPEN_INVENTORY, new ItemStack(Material.STONE_AXE, 1));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("claim")) {
			sender.sendMessage("Claim test! (nothing yet)");
			return true;
		}
		return false;
	}
	
	@EventHandler
	public void achievementEarned(PlayerAchievementAwardedEvent event) {
		ItemStack reward = REWARDS.get(event.getAchievement());
		if (reward != null) {
			event.getPlayer().getInventory().addItem(reward);
			event.getPlayer().sendMessage("Congratulations! You recieved "+reward.getAmount()+" "+reward.getType().name().toLowerCase().replace("_", " ")+"!");
		} else {
			event.getPlayer().sendMessage("No reward added for: "+event.getAchievement().name()+".");
		}
	}

}
