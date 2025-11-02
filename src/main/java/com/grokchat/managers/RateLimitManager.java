package com.grokchat.managers;

import com.grokchat.GrokChatPlugin;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RateLimitManager {

    private final GrokChatPlugin plugin;
    private final Map<UUID, ConcurrentLinkedQueue<Long>> requestTimestamps;

    public RateLimitManager(GrokChatPlugin plugin) {
        this.plugin = plugin;
        this.requestTimestamps = new HashMap<>();
    }

    public boolean canMakeRequest(Player player) {
        if (!plugin.getConfigManager().isRateLimitEnabled()) {
            return true;
        }

        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        long oneHourAgo = currentTime - (60 * 60 * 1000);

        // Get or create queue for this player
        ConcurrentLinkedQueue<Long> timestamps = requestTimestamps.computeIfAbsent(
            playerId, k -> new ConcurrentLinkedQueue<>()
        );

        // Remove timestamps older than 1 hour
        timestamps.removeIf(timestamp -> timestamp < oneHourAgo);

        // Check if player has exceeded limit (uses per-group limits if configured)
        int maxRequests = plugin.getConfigManager().getMaxRequestsPerHour(player);
        return timestamps.size() < maxRequests;
    }

    public void recordRequest(Player player) {
        UUID playerId = player.getUniqueId();
        ConcurrentLinkedQueue<Long> timestamps = requestTimestamps.computeIfAbsent(
            playerId, k -> new ConcurrentLinkedQueue<>()
        );
        timestamps.add(System.currentTimeMillis());
    }

    public int getRemainingRequests(Player player) {
        if (!plugin.getConfigManager().isRateLimitEnabled()) {
            return Integer.MAX_VALUE;
        }

        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        long oneHourAgo = currentTime - (60 * 60 * 1000);

        ConcurrentLinkedQueue<Long> timestamps = requestTimestamps.get(playerId);
        if (timestamps == null) {
            return plugin.getConfigManager().getMaxRequestsPerHour(player);
        }

        // Remove old timestamps
        timestamps.removeIf(timestamp -> timestamp < oneHourAgo);

        int maxRequests = plugin.getConfigManager().getMaxRequestsPerHour(player);
        return Math.max(0, maxRequests - timestamps.size());
    }

    public void clearAll() {
        requestTimestamps.clear();
    }
}

