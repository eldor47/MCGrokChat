package com.grokchat.managers;

import com.grokchat.GrokChatPlugin;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Manages chat history for providing context to Grok's responses
 */
public class ChatHistoryManager {

    private final GrokChatPlugin plugin;
    private final LinkedList<ChatMessage> globalHistory;
    private final int maxHistorySize;

    public ChatHistoryManager(GrokChatPlugin plugin) {
        this.plugin = plugin;
        this.maxHistorySize = plugin.getConfigManager().getChatHistorySize();
        this.globalHistory = new LinkedList<>();
    }

    /**
     * Add a message to chat history
     */
    public void addMessage(Player player, String message) {
        if (!plugin.getConfigManager().isChatHistoryEnabled()) {
            return;
        }

        ChatMessage chatMessage = new ChatMessage(
            player.getName(),
            message,
            System.currentTimeMillis()
        );

        synchronized (globalHistory) {
            globalHistory.addLast(chatMessage);
            
            // Remove old messages if we exceed max size
            while (globalHistory.size() > maxHistorySize) {
                globalHistory.removeFirst();
            }
        }
    }

    /**
     * Get recent chat history as a formatted string for context
     */
    public String getHistoryContext() {
        if (!plugin.getConfigManager().isChatHistoryEnabled()) {
            return "";
        }

        synchronized (globalHistory) {
            if (globalHistory.isEmpty()) {
                return "";
            }

            StringBuilder context = new StringBuilder();
            context.append("Recent chat context:\n");

            for (ChatMessage msg : globalHistory) {
                context.append(msg.getPlayerName())
                       .append(": ")
                       .append(msg.getMessage())
                       .append("\n");
            }

            return context.toString();
        }
    }

    /**
     * Get the last N messages from history
     */
    public List<ChatMessage> getRecentMessages(int count) {
        synchronized (globalHistory) {
            List<ChatMessage> recent = new ArrayList<>();
            int start = Math.max(0, globalHistory.size() - count);
            
            for (int i = start; i < globalHistory.size(); i++) {
                recent.add(globalHistory.get(i));
            }
            
            return recent;
        }
    }

    /**
     * Clear all chat history
     */
    public void clearHistory() {
        synchronized (globalHistory) {
            globalHistory.clear();
        }
    }

    /**
     * Get the current history size
     */
    public int getHistorySize() {
        synchronized (globalHistory) {
            return globalHistory.size();
        }
    }

    /**
     * Inner class representing a chat message
     */
    public static class ChatMessage {
        private final String playerName;
        private final String message;
        private final long timestamp;

        public ChatMessage(String playerName, String message, long timestamp) {
            this.playerName = playerName;
            this.message = message;
            this.timestamp = timestamp;
        }

        public String getPlayerName() {
            return playerName;
        }

        public String getMessage() {
            return message;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}

