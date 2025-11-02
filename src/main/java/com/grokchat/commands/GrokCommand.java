package com.grokchat.commands;

import com.grokchat.GrokChatPlugin;
import com.grokchat.utils.MessageUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GrokCommand implements CommandExecutor {

    private final GrokChatPlugin plugin;

    public GrokCommand(GrokChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;

        // Check permission
        if (!player.hasPermission("grokchat.command")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        // Check if command is enabled
        if (!plugin.getConfigManager().isCommandEnabled()) {
            player.sendMessage(ChatColor.RED + "The /grok command is currently disabled.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /grok <your question>");
            player.sendMessage(ChatColor.GRAY + "Example: /grok How do I make a diamond pickaxe?");
            return true;
        }

        // Build question from args
        String question = String.join(" ", args);

        // Check blacklist (silently ignore if message contains blacklisted words)
        if (plugin.getConfigManager().isBlacklistEnabled()) {
            java.util.List<String> blacklistedWords = plugin.getConfigManager().getBlacklistedWords();
            if (MessageUtils.containsBlacklistedWord(question, blacklistedWords)) {
                player.sendMessage(ChatColor.RED + "Your message contains blacklisted content and cannot be processed.");
                return true;
            }
        }

        // Check message length
        if (question.length() > plugin.getConfigManager().getMaxMessageLength()) {
            player.sendMessage(ChatColor.RED + "Your message is too long! Maximum " + 
                plugin.getConfigManager().getMaxMessageLength() + " characters.");
            return true;
        }

        // Check cooldown
        if (plugin.getCooldownManager().isOnCooldown(player)) {
            long remaining = plugin.getCooldownManager().getRemainingCooldown(player);
            player.sendMessage(ChatColor.RED + "Please wait " + remaining + " seconds before asking again.");
            return true;
        }

        // Check rate limit
        if (!plugin.getRateLimitManager().canMakeRequest(player)) {
            int remaining = plugin.getRateLimitManager().getRemainingRequests(player);
            player.sendMessage(ChatColor.RED + "You've reached your hourly limit. Requests remaining: " + remaining);
            return true;
        }

        // Check API key
        if (!plugin.getConfigManager().isApiKeyValid()) {
            player.sendMessage(ChatColor.RED + "Grok is not configured. Please contact an administrator.");
            return true;
        }

        // Log query if enabled
        if (plugin.getConfigManager().isLogQueries()) {
            plugin.getLogger().info(player.getName() + " asked Grok: " + question);
        }

        // Set cooldown and record request
        plugin.getCooldownManager().setCooldown(player);
        plugin.getRateLimitManager().recordRequest(player);

        // Show thinking message
        if (plugin.getConfigManager().isShowThinking()) {
            player.sendMessage(MessageUtils.colorize(plugin.getConfigManager().getThinkingMessage()));
        }

        // Call Grok API asynchronously
        plugin.getGrokApiClient().sendMessage(question, player.getName())
            .thenAccept(response -> {
                // Send response back to player on main thread
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    MessageUtils.sendGrokResponse(player, response, plugin);
                    
                    // Log response if enabled
                    if (plugin.getConfigManager().isLogQueries()) {
                        plugin.getLogger().info("Grok responded to " + player.getName() + ": " + response);
                    }
                });
            })
            .exceptionally(throwable -> {
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    player.sendMessage(ChatColor.RED + "Error: " + throwable.getMessage());
                });
                return null;
            });

        return true;
    }
}

