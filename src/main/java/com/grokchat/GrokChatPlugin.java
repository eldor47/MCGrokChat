package com.grokchat;

import com.grokchat.commands.GrokCommand;
import com.grokchat.commands.GrokChatCommand;
import com.grokchat.commands.GrokTabCompleter;
import com.grokchat.commands.GrokChatTabCompleter;
import com.grokchat.listeners.ChatListener;
import com.grokchat.managers.ConfigManager;
import com.grokchat.managers.CooldownManager;
import com.grokchat.managers.RateLimitManager;
import com.grokchat.managers.ChatHistoryManager;
import com.grokchat.api.GrokApiClient;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class GrokChatPlugin extends JavaPlugin {

    private static GrokChatPlugin instance;
    private ConfigManager configManager;
    private GrokApiClient grokApiClient;
    private CooldownManager cooldownManager;
    private RateLimitManager rateLimitManager;
    private ChatHistoryManager chatHistoryManager;

    @Override
    public void onEnable() {
        instance = this;
        
        // Save default config
        saveDefaultConfig();
        
        // Initialize managers
        this.configManager = new ConfigManager(this);
        this.cooldownManager = new CooldownManager(this);
        this.rateLimitManager = new RateLimitManager(this);
        this.chatHistoryManager = new ChatHistoryManager(this);
        
        // Initialize API client
        try {
            this.grokApiClient = new GrokApiClient(this);
            
            // Validate API key
            if (!configManager.isApiKeyValid()) {
                getLogger().warning("═══════════════════════════════════════════");
                getLogger().warning("  API key not configured!");
                getLogger().warning("  Please set your xAI API key in config.yml");
                getLogger().warning("  Get your key at: https://console.x.ai/");
                getLogger().warning("═══════════════════════════════════════════");
            }
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to initialize Grok API client", e);
        }
        
        // Register listeners
        if (configManager.isMentionsEnabled()) {
            getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        }
        
        // Register commands and tab completers
        if (configManager.isCommandEnabled()) {
            getCommand("grok").setExecutor(new GrokCommand(this));
            getCommand("grok").setTabCompleter(new GrokTabCompleter(this));
        }
        getCommand("grokchat").setExecutor(new GrokChatCommand(this));
        getCommand("grokchat").setTabCompleter(new GrokChatTabCompleter(this));
        
        getLogger().info("GrokChat has been enabled!");
        getLogger().info("Mention prefix: " + configManager.getMentionPrefix());
    }

    @Override
    public void onDisable() {
        // Cleanup
        if (grokApiClient != null) {
            grokApiClient.shutdown();
        }
        getLogger().info("GrokChat has been disabled!");
    }

    public void reload() {
        reloadConfig();
        configManager = new ConfigManager(this);
        cooldownManager = new CooldownManager(this);
        rateLimitManager = new RateLimitManager(this);
        chatHistoryManager = new ChatHistoryManager(this);
        
        if (grokApiClient != null) {
            grokApiClient.shutdown();
        }
        grokApiClient = new GrokApiClient(this);
        
        getLogger().info("GrokChat configuration reloaded!");
    }

    public static GrokChatPlugin getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public GrokApiClient getGrokApiClient() {
        return grokApiClient;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public RateLimitManager getRateLimitManager() {
        return rateLimitManager;
    }

    public ChatHistoryManager getChatHistoryManager() {
        return chatHistoryManager;
    }
}

