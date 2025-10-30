package com.grokchat.listeners;

import com.grokchat.GrokChatPlugin;
import com.grokchat.utils.MessageUtils;
import com.grokchat.utils.PlayerDataUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final GrokChatPlugin plugin;

    public ChatListener(GrokChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String messageLower = message.toLowerCase();
        
        // Add message to chat history (do this for ALL messages, not just Grok mentions)
        plugin.getChatHistoryManager().addMessage(player, message);
        
        // Check permission first
        if (!player.hasPermission("grokchat.use")) {
            return;
        }
        
        String prefix = plugin.getConfigManager().getMentionPrefix();
        boolean isDirectMention = messageLower.contains(prefix.toLowerCase());
        boolean isAutoResponse = false;
        String question = null;
        
        // Check for direct mention (@grok)
        if (isDirectMention) {
            question = message.replaceFirst("(?i)" + prefix, "").trim();
            
            if (question.isEmpty()) {
                player.sendMessage(ChatColor.YELLOW + "Usage: " + prefix + " <your question>");
                return;
            }
        }
        // Check for auto-response (keyword-based)
        else if (plugin.getConfigManager().isAutoResponseEnabled()) {
            // Check if message has minimum word count
            String[] words = message.trim().split("\\s+");
            if (words.length < plugin.getConfigManager().getAutoResponseMinWords()) {
                return; // Too short, ignore
            }
            
            // Check for keywords
            java.util.List<String> keywords = plugin.getConfigManager().getAutoResponseKeywords();
            if (keywords == null || keywords.isEmpty()) {
                return;
            }
            
            for (String keyword : keywords) {
                if (messageLower.contains(keyword.toLowerCase())) {
                    isAutoResponse = true;
                    // For auto-response, send the full message as context
                    question = message;
                    break;
                }
            }
            
            if (!isAutoResponse) {
                return; // No keywords matched
            }
        } else {
            return; // Not a mention and auto-response is disabled
        }
        
        // At this point, we have a valid question to process
        
        // Check message length
        if (question.length() > plugin.getConfigManager().getMaxMessageLength()) {
            if (!isAutoResponse) { // Only notify for direct mentions
                player.sendMessage(ChatColor.RED + "Your message is too long! Maximum " + 
                    plugin.getConfigManager().getMaxMessageLength() + " characters.");
            }
            return;
        }
        
        // Check cooldown
        if (plugin.getCooldownManager().isOnCooldown(player)) {
            if (!isAutoResponse) { // Only notify for direct mentions
                long remaining = plugin.getCooldownManager().getRemainingCooldown(player);
                player.sendMessage(ChatColor.RED + "Please wait " + remaining + " seconds before asking again.");
            }
            return;
        }
        
        // Check rate limit
        if (!plugin.getRateLimitManager().canMakeRequest(player)) {
            if (!isAutoResponse) { // Only notify for direct mentions
                player.sendMessage(ChatColor.RED + "You've reached your hourly limit. Please try again later.");
            }
            return;
        }
        
        // Check API key
        if (!plugin.getConfigManager().isApiKeyValid()) {
            if (!isAutoResponse) { // Only notify for direct mentions
                player.sendMessage(ChatColor.RED + "Grok is not configured. Please contact an administrator.");
            }
            return;
        }
        
        // Log query if enabled
        if (plugin.getConfigManager().isLogQueries()) {
            String logType = isAutoResponse ? "auto-response" : "direct mention";
            plugin.getLogger().info(player.getName() + " triggered Grok (" + logType + "): " + question);
        }
        
        // Set cooldown and record request
        plugin.getCooldownManager().setCooldown(player);
        plugin.getRateLimitManager().recordRequest(player);
        
        // Show thinking message (only for direct mentions)
        if (!isAutoResponse && plugin.getConfigManager().isShowThinking()) {
            player.sendMessage(MessageUtils.colorize(plugin.getConfigManager().getThinkingMessage()));
        }
        
        // Get chat history context if enabled
        String chatContext = null;
        if ((isAutoResponse && plugin.getConfigManager().useHistoryInAutoResponse()) ||
            (!isAutoResponse && plugin.getConfigManager().useHistoryInDirectMention())) {
            chatContext = plugin.getChatHistoryManager().getHistoryContext();
        }
        
        // Get player data context if enabled (must be done on main thread)
        String finalQuestion = question;
        boolean finalIsAutoResponse = isAutoResponse;
        String finalChatContext = chatContext;
        
        // Schedule player data gathering on main thread if needed
        boolean playerDataEnabled = plugin.getConfigManager().isPlayerDataEnabled();
        boolean useInAutoResponse = plugin.getConfigManager().usePlayerDataInAutoResponse();
        boolean useInDirectMention = plugin.getConfigManager().usePlayerDataInDirectMention();
        
        if (plugin.getConfigManager().isDebug()) {
            plugin.getLogger().info("[ChatListener] Player data check - Enabled: " + playerDataEnabled + 
                ", IsAutoResponse: " + isAutoResponse + 
                ", UseInAutoResponse: " + useInAutoResponse + 
                ", UseInDirectMention: " + useInDirectMention);
        }
        
        if (playerDataEnabled &&
            ((isAutoResponse && useInAutoResponse) ||
             (!isAutoResponse && useInDirectMention))) {
            
            // Get player data synchronously on main thread
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                String playerDataContext = PlayerDataUtils.getPlayerDataContext(player, plugin);
                
                if (plugin.getConfigManager().isDebug()) {
                    plugin.getLogger().info("[ChatListener] Player data context:\n" + playerDataContext);
                }
                
                // Call Grok API asynchronously
                plugin.getGrokApiClient().sendMessage(finalQuestion, player.getName(), finalChatContext, playerDataContext)
                    .thenAccept(response -> {
                        // Send response back on main thread
                        plugin.getServer().getScheduler().runTask(plugin, () -> {
                            if (finalIsAutoResponse) {
                                // For auto-response, broadcast to all online players
                                MessageUtils.sendGrokResponseToAll(plugin, response);
                            } else {
                                // For direct mention, send only to the player
                                MessageUtils.sendGrokResponse(player, response, plugin);
                            }
                            
                            // Log response if enabled
                            if (plugin.getConfigManager().isLogQueries()) {
                                plugin.getLogger().info("Grok responded: " + response);
                            }
                        });
                    })
                    .exceptionally(throwable -> {
                        plugin.getServer().getScheduler().runTask(plugin, () -> {
                            if (!finalIsAutoResponse) {
                                player.sendMessage(ChatColor.RED + "Error: " + throwable.getMessage());
                            }
                        });
                        return null;
                    });
            });
        } else {
            // No player data needed, call API directly
            if (plugin.getConfigManager().isDebug()) {
                String reason = !playerDataEnabled ? "Player data disabled" :
                    (isAutoResponse ? "Auto-response disabled for player data" : "Direct mention disabled for player data");
                plugin.getLogger().info("[ChatListener] Skipping player data - " + reason);
            }
            
            plugin.getGrokApiClient().sendMessage(finalQuestion, player.getName(), finalChatContext, null)
            .thenAccept(response -> {
                // Send response back on main thread
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    if (finalIsAutoResponse) {
                        // For auto-response, broadcast to all online players
                        MessageUtils.sendGrokResponseToAll(plugin, response);
                    } else {
                        // For direct mention, send only to the player
                        MessageUtils.sendGrokResponse(player, response, plugin);
                    }
                    
                    // Log response if enabled
                    if (plugin.getConfigManager().isLogQueries()) {
                        plugin.getLogger().info("Grok responded: " + response);
                    }
                });
            })
            .exceptionally(throwable -> {
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    if (!finalIsAutoResponse) {
                        player.sendMessage(ChatColor.RED + "Error: " + throwable.getMessage());
                    }
                });
                return null;
            });
        }
    }
}

