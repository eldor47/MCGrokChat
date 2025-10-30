package com.grokchat.commands;

import com.grokchat.GrokChatPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GrokChatTabCompleter implements TabCompleter {

    private final GrokChatPlugin plugin;

    public GrokChatTabCompleter(GrokChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        // Check permission
        if (!sender.hasPermission("grokchat.admin")) {
            return completions;
        }

        // /grokchat <subcommand>
        if (args.length == 1) {
            List<String> subcommands = Arrays.asList(
                "reload",
                "info",
                "setkey",
                "test",
                "stats"
            );
            
            // Filter based on what user has typed
            String input = args[0].toLowerCase();
            completions = subcommands.stream()
                .filter(sub -> sub.startsWith(input))
                .collect(Collectors.toList());
        }
        // /grokchat stats <player>
        else if (args.length == 2 && args[0].equalsIgnoreCase("stats")) {
            // Add online player names
            completions = plugin.getServer().getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                .collect(Collectors.toList());
        }
        // /grokchat setkey <key>
        else if (args.length == 2 && args[0].equalsIgnoreCase("setkey")) {
            completions.add("<api-key>");
        }

        return completions;
    }
}

