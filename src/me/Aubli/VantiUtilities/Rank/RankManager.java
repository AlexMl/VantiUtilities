package me.Aubli.VantiUtilities.Rank;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;

import me.Aubli.VantiUtilities.VantiUtilities;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class RankManager {
    
    private static RankManager instance;
    
    private static File rankFile;
    
    private static List<Rank> ranks = new ArrayList<Rank>();
    
    public RankManager() {
	instance = this;
	rankFile = new File(VantiUtilities.getInstance().getDataFolder(), "ranks.yml");
	loadContent();
    }
    
    private void loadContent() {
	
	FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(rankFile);
	
	if (!rankFile.exists()) {
	    try {
		rankFile.createNewFile();
		
		fileConfig.options().header("This is the rank configuration of " + VantiUtilities.getInstance().getName() + "!\nUse '<player>' for addressing the player\nAvailable options are:\n'permission:' Permission to execute /rank rankname player\n'commands:' the commands in list format\n'message:' the message send to the player who received the rank\n");
		fileConfig.set("ranks", "[]");
		
		fileConfig.options().copyHeader();
		fileConfig.save(rankFile);
	    } catch (IOException e) {
		VantiUtilities.getPluginLogger().log(getClass(), Level.WARNING, "Could not save rank file: " + e.getMessage(), true, false, e);
	    }
	    return;
	}
	
	if (fileConfig.get("ranks") != null) {
	    
	    if (fileConfig.getConfigurationSection("ranks") != null) {
		for (Entry<String, Object> entry : fileConfig.getConfigurationSection("ranks").getValues(false).entrySet()) {
		    Rank rank = new Rank(entry.getKey(), fileConfig.getString("ranks." + entry.getKey() + ".permission"), fileConfig.getStringList("ranks." + entry.getKey() + ".commands"), fileConfig.getString("ranks." + entry.getKey() + ".message"));
		    ranks.add(rank);
		    VantiUtilities.getPluginLogger().log(getClass(), Level.FINE, "Successfully loaded " + rank, true, true);
		}
	    }
	    
	}
    }
    
    private static RankManager getManager() {
	return instance;
    }
    
    public static void reloadRanks() {
	ranks.clear();
	getManager().loadContent();
    }
    
    public static Rank getRank(String name) {
	for (Rank rank : ranks) {
	    if (rank.getName().equalsIgnoreCase(name)) {
		return rank;
	    }
	}
	return null;
    }
    
    public static void execute(Rank rank, OfflinePlayer player) throws NullPointerException {
	
	if (rank == null || player == null) {
	    throw new NullPointerException("Rank and Player can not be null!");
	}
	
	for (String command : rank.getCommandList()) {
	    VantiUtilities.getInstance().getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("<player>", player.getName()));
	}
	
	RankMessages.sendMessage(player, rank.getMessage());
	VantiUtilities.getPluginLogger().log(RankManager.class, Level.INFO, "Player " + player.getName() + " got moved to " + rank.getName() + " successfully!", true, true);
    }
}
