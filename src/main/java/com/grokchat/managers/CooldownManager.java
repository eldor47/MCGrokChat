package com.grokchat.managers;

import com.grokchat.GrokChatPlugin;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final GrokChatPlugin plugin;
    private final Map<UUID, Long> cooldowns;

    public CooldownManager(GrokChatPlugin plugin) {
        this.plugin = plugin;
        this.cooldowns = new HashMap<>();
    }

    public boolean isOnCooldown(Player player) {
        if (!cooldowns.containsKey(player.getUniqueId())) {
            return false;
        }

        long lastUsed = cooldowns.get(player.getUniqueId());
        long cooldownSeconds = plugin.getConfigManager().getCooldown();
        long cooldownMillis = cooldownSeconds * 1000L;

        return System.currentTimeMillis() - lastUsed < cooldownMillis;
    }

    public long getRemainingCooldown(Player player) {
        if (!cooldowns.containsKey(player.getUniqueId())) {
            return 0;
        }

        long lastUsed = cooldowns.get(player.getUniqueId());
        long cooldownSeconds = plugin.getConfigManager().getCooldown();
        long cooldownMillis = cooldownSeconds * 1000L;
        long elapsed = System.currentTimeMillis() - lastUsed;

        return Math.max(0, (cooldownMillis - elapsed) / 1000);
    }

    public void setCooldown(Player player) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public void removeCooldown(Player player) {
        cooldowns.remove(player.getUniqueId());
    }

    public void clearAll() {
        cooldowns.clear();
    }
}

