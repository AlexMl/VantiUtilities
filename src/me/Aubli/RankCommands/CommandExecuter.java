package me.Aubli.RankCommands;

import me.Aubli.RankCommands.Rank.Rank;
import me.Aubli.RankCommands.Rank.RankManager;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CommandExecuter implements CommandExecutor {
    
    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	
	if (cmd.getName().equalsIgnoreCase("rank")) {
	    // TODO permission check
	    if (args.length == 2) {
		Player argPlayer = Bukkit.getPlayer(args[0]);
		
		// TODO maybe handle offlineplayers as well
		if (argPlayer != null && argPlayer.isOnline()) {
		    Rank rank = RankManager.getRank(args[1]);
		    RankManager.execute(rank, argPlayer);
		    sender.sendMessage(argPlayer.getName() + " changed rank to " + rank);
		    return true;
		} else {
		    // TODO message
		}
	    }
	    
	}
	
	return false;
    }
    
}