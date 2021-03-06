package me.Aubli.VantiUtilities.Rank;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

import me.Aubli.VantiUtilities.VantiUtilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class RankMessages {
    
    public enum RankMessage {
	
	rank_changed(ChatColor.GREEN + "You moved " + ChatColor.GOLD + "%s" + ChatColor.GREEN + " to rank " + ChatColor.GOLD + "%s" + ChatColor.GREEN + "!"),
	rank_not_found(ChatColor.RED + "The rank " + ChatColor.GOLD + "%s" + ChatColor.RED + " is not available!"),
	not_enough_money(ChatColor.RED + "The player " + ChatColor.GOLD + "%s" + ChatColor.RED + " can not afford the rank " + ChatColor.GOLD + "%s" + ChatColor.RED + "!"),
	nightvision_enabled(ChatColor.GREEN + "Night vision enabled!"),
	nightvision_disabled(ChatColor.RED + "Night vision disabled!"),
	config_reloaded(ChatColor.GREEN + "Plugin configuration reloaded!"),
	no_permission(ChatColor.DARK_RED + "You do not have enough permission!");
	
	private String message;
	
	private RankMessage(String message) {
	    this.message = message;
	}
	
	public String getMessage() {
	    return this.message;
	}
	
	public String getFormatedMessage(Object... args) {
	    return String.format(getMessage(), args);
	}
    }
    
    public RankMessages() {
	Bukkit.getPluginManager().registerEvents(new JoinListener(), VantiUtilities.getInstance());
    }
    
    public static void sendMessage(OfflinePlayer player, String message) {
	MessageDelivery.sendMessage(player, message);
    }
    
    public static void sendMessage(OfflinePlayer player, RankMessage message) {
	MessageDelivery.sendMessage(player, message.getMessage());
    }
    
    public static void sendMessage(OfflinePlayer player, RankMessage message, Object... args) {
	MessageDelivery.sendMessage(player, message.getFormatedMessage(args));
    }
    
    public static void sendInstantMessage(CommandSender sender, RankMessage message) {
	MessageDelivery.sendMessage(sender, message.getMessage());
    }
    
    public static void sendInstantMessage(CommandSender sender, RankMessage message, Object... args) {
	MessageDelivery.sendMessage(sender, message.getFormatedMessage(args));
    }
    
    private static class MessageDelivery {
	
	private static Map<UUID, String> messageMap = new TreeMap<UUID, String>();
	
	public static void sendMessage(CommandSender sender, String message) {
	    sender.sendMessage(message);
	}
	
	public static void sendMessage(OfflinePlayer player, String message) {
	    messageMap.put(player.getUniqueId(), message);
	    pushMessages();
	}
	
	private static void pushMessages() {
	    
	    Map<UUID, String> queuedMessages = new TreeMap<UUID, String>();
	    
	    for (Entry<UUID, String> entry : messageMap.entrySet()) {
		if (Bukkit.getOfflinePlayer(entry.getKey()).isOnline()) {
		    Bukkit.getPlayer(entry.getKey()).sendMessage(entry.getValue());
		} else {
		    queuedMessages.put(entry.getKey(), entry.getValue());
		}
	    }
	    
	    messageMap = queuedMessages;
	}
    }
    
    private class JoinListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
	    MessageDelivery.pushMessages();
	}
    }
}
