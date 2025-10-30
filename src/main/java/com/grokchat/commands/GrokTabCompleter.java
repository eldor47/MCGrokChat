package com.grokchat.commands;

import com.grokchat.GrokChatPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GrokTabCompleter implements TabCompleter {

    private final GrokChatPlugin plugin;

    public GrokTabCompleter(GrokChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        // Check permission
        if (!sender.hasPermission("grokchat.command")) {
            return completions;
        }

        // /grok <question>
        if (args.length == 1) {
            // Suggest common question starters
            List<String> suggestions = Arrays.asList(
                "how",
                "what",
                "where",
                "when",
                "why",
                "can",
                "help"
            );
            
            String input = args[0].toLowerCase();
            completions = suggestions.stream()
                .filter(word -> word.startsWith(input))
                .collect(Collectors.toList());
        }
        // After first word, suggest common Minecraft terms
        else if (args.length == 2) {
            List<String> minecraftTerms = Arrays.asList(
                "do",
                "I",
                "make",
                "craft",
                "find",
                "get",
                "build",
                "enchant",
                "mine",
                "farm"
            );
            
            String input = args[1].toLowerCase();
            completions = minecraftTerms.stream()
                .filter(word -> word.startsWith(input))
                .collect(Collectors.toList());
        }
        // Third word - common minecraft items/concepts
        else if (args.length == 3) {
            List<String> commonItems = Arrays.asList(
                "diamonds",
                "iron",
                "gold",
                "netherite",
                "enchantment",
                "beacon",
                "farm",
                "base",
                "redstone",
                "sword",
                "pickaxe",
                "armor",
                "food"
            );
            
            String input = args[2].toLowerCase();
            completions = commonItems.stream()
                .filter(word -> word.startsWith(input))
                .collect(Collectors.toList());
        }

        return completions;
    }
}

