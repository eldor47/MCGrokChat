package com.grokchat.managers;

import com.grokchat.GrokChatPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final GrokChatPlugin plugin;
    private final FileConfiguration config;

    public ConfigManager(GrokChatPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    // API Settings
    public String getApiKey() {
        return config.getString("api.key", "");
    }

    public boolean isApiKeyValid() {
        String key = getApiKey();
        return key != null && !key.isEmpty() && !key.equals("your-xai-api-key-here");
    }

    public String getApiEndpoint() {
        return config.getString("api.endpoint", "https://api.x.ai/v1/chat/completions");
    }

    public String getModel() {
        return config.getString("api.model", "grok-beta");
    }

    public int getTimeout() {
        return config.getInt("api.timeout", 30);
    }

    // Chat Settings
    public boolean isMentionsEnabled() {
        return config.getBoolean("chat.enable-mentions", true);
    }

    public String getMentionPrefix() {
        return config.getString("chat.mention-prefix", "@grok");
    }

    public boolean isCommandEnabled() {
        return config.getBoolean("chat.enable-command", true);
    }

    public int getMaxMessageLength() {
        return config.getInt("chat.max-message-length", 500);
    }

    public int getCooldown() {
        return config.getInt("chat.cooldown", 5);
    }

    public boolean isAutoResponseEnabled() {
        return config.getBoolean("chat.enable-auto-response", true);
    }

    public java.util.List<String> getAutoResponseKeywords() {
        return config.getStringList("chat.auto-response-keywords");
    }

    public int getAutoResponseMinWords() {
        return config.getInt("chat.auto-response-min-words", 3);
    }

    // Blacklist Settings
    public boolean isBlacklistEnabled() {
        return config.getBoolean("blacklist.enabled", true);
    }

    public java.util.List<String> getBlacklistedWords() {
        return config.getStringList("blacklist.blocked-words");
    }

    // Response Settings
    public int getMaxResponseLength() {
        return config.getInt("response.max-length", 256);
    }

    public String getResponseColor() {
        return config.getString("response.color", "AQUA");
    }

    public String getResponsePrefix() {
        return config.getString("response.prefix", "&b[Grok]&r ");
    }

    public boolean isShowThinking() {
        return config.getBoolean("response.show-thinking", true);
    }

    public String getThinkingMessage() {
        return config.getString("response.thinking-message", "&7Grok is thinking...");
    }

    // Rate Limiting
    public boolean isRateLimitEnabled() {
        return config.getBoolean("rate-limit.enabled", true);
    }

    public int getMaxRequestsPerHour() {
        return config.getInt("rate-limit.max-requests-per-hour", 20);
    }

    /**
     * Get the maximum requests per hour for a specific player based on their permissions.
     * Checks all group rate limit permissions and returns the highest value.
     * Falls back to default if player has no group permissions.
     * 
     * @param player The player to check
     * @return The maximum requests per hour for this player
     */
    public int getMaxRequestsPerHour(org.bukkit.entity.Player player) {
        if (player == null) {
            return getMaxRequestsPerHour();
        }

        int maxLimit = getMaxRequestsPerHour(); // Default limit
        org.bukkit.configuration.ConfigurationSection groupsSection = config.getConfigurationSection("rate-limit.groups");
        
        if (groupsSection == null) {
            return maxLimit;
        }

        // Check all group rate limit permissions and find the highest
        for (String permission : groupsSection.getKeys(false)) {
            if (player.hasPermission(permission)) {
                int groupLimit = groupsSection.getInt(permission, maxLimit);
                if (groupLimit > maxLimit) {
                    maxLimit = groupLimit;
                }
            }
        }

        return maxLimit;
    }

    /**
     * Get all configured group rate limits as a map.
     * 
     * @return Map of permission node to rate limit
     */
    public java.util.Map<String, Integer> getGroupRateLimits() {
        java.util.Map<String, Integer> groups = new java.util.HashMap<>();
        org.bukkit.configuration.ConfigurationSection groupsSection = config.getConfigurationSection("rate-limit.groups");
        
        if (groupsSection != null) {
            for (String permission : groupsSection.getKeys(false)) {
                groups.put(permission, groupsSection.getInt(permission));
            }
        }
        
        return groups;
    }

    // Chat History Settings
    public boolean isChatHistoryEnabled() {
        return config.getBoolean("chat-history.enabled", true);
    }

    public int getChatHistorySize() {
        return config.getInt("chat-history.size", 10);
    }

    public boolean useHistoryInAutoResponse() {
        return config.getBoolean("chat-history.use-in-auto-response", true);
    }

    public boolean useHistoryInDirectMention() {
        return config.getBoolean("chat-history.use-in-direct-mention", false);
    }

    // Player Data Settings
    public boolean isPlayerDataEnabled() {
        return config.getBoolean("player-data.enabled", false);
    }

    public boolean includePlayerPosition() {
        return config.getBoolean("player-data.include-position", true);
    }

    public boolean includePlayerBiome() {
        return config.getBoolean("player-data.include-biome", true);
    }

    public boolean includePlayerWorld() {
        return config.getBoolean("player-data.include-world", true);
    }

    public boolean includePlayerHealth() {
        return config.getBoolean("player-data.include-health", true);
    }

    public boolean includePlayerExperience() {
        return config.getBoolean("player-data.include-experience", true);
    }

    public boolean includePlayerHeldItem() {
        return config.getBoolean("player-data.include-held-item", true);
    }

    public boolean includePlayerEquipment() {
        return config.getBoolean("player-data.include-equipment", true);
    }

    public boolean includePlayerGamemode() {
        return config.getBoolean("player-data.include-gamemode", true);
    }

    public boolean includePlayerEffects() {
        return config.getBoolean("player-data.include-effects", true);
    }

    public boolean usePlayerDataInAutoResponse() {
        return config.getBoolean("player-data.use-in-auto-response", false);
    }

    public boolean usePlayerDataInDirectMention() {
        return config.getBoolean("player-data.use-in-direct-mention", true);
    }

    // Privacy Settings
    public boolean isLogQueries() {
        return config.getBoolean("privacy.log-queries", false);
    }

    public boolean isShareContext() {
        return config.getBoolean("privacy.share-context", false);
    }

    // Advanced Settings
    public double getTemperature() {
        return config.getDouble("advanced.temperature", 0.7);
    }

    public int getMaxTokens() {
        return config.getInt("advanced.max-tokens", 500);
    }

    public String getSystemPrompt() {
        return config.getString("advanced.system-prompt", 
            "You are Grok, an AI assistant integrated into a Minecraft server. " +
            "You're helpful, witty, and concise. Keep responses brief and engaging. " +
            "You're talking to player: {player}");
    }

    public boolean isDebug() {
        return config.getBoolean("advanced.debug", false);
    }
}

