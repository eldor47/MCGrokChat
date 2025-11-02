package com.grokchat.utils;

import com.grokchat.GrokChatPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MessageUtils {

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendGrokResponse(Player player, String response, GrokChatPlugin plugin) {
        String prefix = colorize(plugin.getConfigManager().getResponsePrefix());
        int maxLength = plugin.getConfigManager().getMaxResponseLength();
        
        // Split response if it's too long
        List<String> lines = splitMessage(response, maxLength);
        
        for (String line : lines) {
            player.sendMessage(prefix + colorize(line));
        }
    }

    public static void sendGrokResponseToAll(GrokChatPlugin plugin, String response) {
        String prefix = colorize(plugin.getConfigManager().getResponsePrefix());
        int maxLength = plugin.getConfigManager().getMaxResponseLength();
        
        // Split response if it's too long
        List<String> lines = splitMessage(response, maxLength);
        
        // Broadcast to all online players
        for (String line : lines) {
            plugin.getServer().broadcastMessage(prefix + colorize(line));
        }
    }

    public static List<String> splitMessage(String message, int maxLength) {
        List<String> lines = new ArrayList<>();
        
        if (message.length() <= maxLength) {
            lines.add(message);
            return lines;
        }
        
        // Split by words to avoid breaking mid-word
        String[] words = message.split(" ");
        StringBuilder currentLine = new StringBuilder();
        
        for (String word : words) {
            if (currentLine.length() + word.length() + 1 > maxLength) {
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder();
                }
                
                // Handle very long words
                if (word.length() > maxLength) {
                    lines.add(word.substring(0, maxLength));
                    word = word.substring(maxLength);
                }
            }
            
            if (currentLine.length() > 0) {
                currentLine.append(" ");
            }
            currentLine.append(word);
        }
        
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }
        
        return lines;
    }

    public static String formatError(String error) {
        return ChatColor.RED + "✗ " + error;
    }

    public static String formatSuccess(String message) {
        return ChatColor.GREEN + "✓ " + message;
    }

    public static String formatInfo(String message) {
        return ChatColor.YELLOW + "ℹ " + message;
    }

    /**
     * Checks if a message contains any blacklisted words/phrases.
     * Case-insensitive matching.
     * 
     * @param message The message to check
     * @param blacklistedWords List of blacklisted words/phrases
     * @return true if message contains any blacklisted word, false otherwise
     */
    public static boolean containsBlacklistedWord(String message, List<String> blacklistedWords) {
        if (blacklistedWords == null || blacklistedWords.isEmpty()) {
            return false;
        }
        
        String messageLower = message.toLowerCase();
        
        for (String blockedWord : blacklistedWords) {
            if (blockedWord == null || blockedWord.trim().isEmpty()) {
                continue;
            }
            
            // Case-insensitive partial match
            if (messageLower.contains(blockedWord.toLowerCase())) {
                return true;
            }
        }
        
        return false;
    }
}

