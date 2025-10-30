package com.grokchat.commands;

import com.grokchat.GrokChatPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GrokChatCommand implements CommandExecutor {

    private final GrokChatPlugin plugin;

    public GrokChatCommand(GrokChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check permission
        if (!sender.hasPermission("grokchat.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                handleReload(sender);
                break;
                
            case "info":
                handleInfo(sender);
                break;
                
            case "setkey":
                handleSetKey(sender, args);
                break;
                
            case "test":
                handleTest(sender);
                break;
                
            case "stats":
                handleStats(sender, args);
                break;
                
            default:
                sendHelp(sender);
                break;
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "=== GrokChat Admin Commands ===");
        sender.sendMessage(ChatColor.YELLOW + "/grokchat reload " + ChatColor.GRAY + "- Reload configuration");
        sender.sendMessage(ChatColor.YELLOW + "/grokchat info " + ChatColor.GRAY + "- Show plugin information");
        sender.sendMessage(ChatColor.YELLOW + "/grokchat setkey <key> " + ChatColor.GRAY + "- Set API key");
        sender.sendMessage(ChatColor.YELLOW + "/grokchat test " + ChatColor.GRAY + "- Test API connection");
        sender.sendMessage(ChatColor.YELLOW + "/grokchat stats [player] " + ChatColor.GRAY + "- View usage statistics");
    }

    private void handleReload(CommandSender sender) {
        try {
            plugin.reload();
            sender.sendMessage(ChatColor.GREEN + "✓ GrokChat configuration reloaded!");
            
            if (!plugin.getConfigManager().isApiKeyValid()) {
                sender.sendMessage(ChatColor.RED + "⚠ Warning: API key is not configured!");
            }
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Error reloading configuration: " + e.getMessage());
            plugin.getLogger().severe("Error reloading: " + e.getMessage());
        }
    }

    private void handleInfo(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "=== GrokChat Information ===");
        sender.sendMessage(ChatColor.YELLOW + "Version: " + ChatColor.WHITE + plugin.getDescription().getVersion());
        sender.sendMessage(ChatColor.YELLOW + "Model: " + ChatColor.WHITE + plugin.getConfigManager().getModel());
        sender.sendMessage(ChatColor.YELLOW + "API Status: " + ChatColor.WHITE + 
            (plugin.getConfigManager().isApiKeyValid() ? ChatColor.GREEN + "✓ Configured" : ChatColor.RED + "✗ Not Configured"));
        sender.sendMessage(ChatColor.YELLOW + "Mention Prefix: " + ChatColor.WHITE + plugin.getConfigManager().getMentionPrefix());
        sender.sendMessage(ChatColor.YELLOW + "Command Enabled: " + ChatColor.WHITE + 
            (plugin.getConfigManager().isCommandEnabled() ? "Yes" : "No"));
        sender.sendMessage(ChatColor.YELLOW + "Mentions Enabled: " + ChatColor.WHITE + 
            (plugin.getConfigManager().isMentionsEnabled() ? "Yes" : "No"));
        sender.sendMessage(ChatColor.YELLOW + "Rate Limit: " + ChatColor.WHITE + 
            plugin.getConfigManager().getMaxRequestsPerHour() + " requests/hour");
        sender.sendMessage(ChatColor.YELLOW + "Cooldown: " + ChatColor.WHITE + 
            plugin.getConfigManager().getCooldown() + " seconds");
    }

    private void handleSetKey(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /grokchat setkey <api-key>");
            sender.sendMessage(ChatColor.GRAY + "Get your API key at: https://console.x.ai/");
            return;
        }

        String apiKey = args[1];
        
        // Save to config
        plugin.getConfig().set("api.key", apiKey);
        plugin.saveConfig();
        plugin.reload();
        
        sender.sendMessage(ChatColor.GREEN + "✓ API key has been set and configuration reloaded!");
        sender.sendMessage(ChatColor.GRAY + "Use /grokchat test to verify the connection.");
    }

    private void handleTest(CommandSender sender) {
        if (!plugin.getConfigManager().isApiKeyValid()) {
            sender.sendMessage(ChatColor.RED + "API key is not configured!");
            return;
        }

        sender.sendMessage(ChatColor.YELLOW + "Testing API connection...");
        
        // Send a simple test message
        plugin.getGrokApiClient().sendMessage("Respond with just 'OK' if you can hear me.", "ServerAdmin")
            .thenAccept(response -> {
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    sender.sendMessage(ChatColor.GREEN + "✓ API connection successful!");
                    sender.sendMessage(ChatColor.GRAY + "Response: " + response);
                });
            })
            .exceptionally(throwable -> {
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    sender.sendMessage(ChatColor.RED + "✗ API connection failed!");
                    sender.sendMessage(ChatColor.RED + "Error: " + throwable.getMessage());
                });
                return null;
            });
    }

    private void handleStats(CommandSender sender, String[] args) {
        if (args.length < 2) {
            // Show sender's stats if they're a player
            if (sender instanceof Player) {
                Player player = (Player) sender;
                showPlayerStats(sender, player);
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /grokchat stats <player>");
            }
        } else {
            // Show specific player's stats
            Player target = plugin.getServer().getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player not found!");
                return;
            }
            showPlayerStats(sender, target);
        }
    }

    private void showPlayerStats(CommandSender sender, Player player) {
        sender.sendMessage(ChatColor.GOLD + "=== Grok Stats for " + player.getName() + " ===");
        
        int remaining = plugin.getRateLimitManager().getRemainingRequests(player);
        int max = plugin.getConfigManager().getMaxRequestsPerHour();
        int used = max - remaining;
        
        sender.sendMessage(ChatColor.YELLOW + "Requests Used: " + ChatColor.WHITE + used + "/" + max);
        sender.sendMessage(ChatColor.YELLOW + "Requests Remaining: " + ChatColor.WHITE + remaining);
        
        if (plugin.getCooldownManager().isOnCooldown(player)) {
            long cooldown = plugin.getCooldownManager().getRemainingCooldown(player);
            sender.sendMessage(ChatColor.YELLOW + "Cooldown: " + ChatColor.WHITE + cooldown + " seconds");
        } else {
            sender.sendMessage(ChatColor.YELLOW + "Cooldown: " + ChatColor.GREEN + "Ready");
        }
    }
}

