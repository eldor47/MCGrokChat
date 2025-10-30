package com.grokchat.utils;

import com.grokchat.GrokChatPlugin;
import com.grokchat.managers.ConfigManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.text.DecimalFormat;
import java.util.Collection;

public class PlayerDataUtils {

    private static final DecimalFormat COORD_FORMAT = new DecimalFormat("#.##");

    /**
     * Get formatted player data context string
     */
    public static String getPlayerDataContext(Player player, GrokChatPlugin plugin) {
        if (!plugin.getConfigManager().isPlayerDataEnabled()) {
            return "";
        }

        ConfigManager config = plugin.getConfigManager();
        StringBuilder context = new StringBuilder();
        context.append("Player context:\n");

        Location loc = player.getLocation();
        World world = player.getWorld();

        // Position
        if (config.includePlayerPosition()) {
            context.append("- Position: ")
                   .append(COORD_FORMAT.format(loc.getX()))
                   .append(", ")
                   .append(COORD_FORMAT.format(loc.getY()))
                   .append(", ")
                   .append(COORD_FORMAT.format(loc.getZ()))
                   .append(" (Y-level: ")
                   .append((int) loc.getY())
                   .append(")\n");
        }

        // Biome
        if (config.includePlayerBiome()) {
            String biomeName = world.getBiome(loc).toString().toLowerCase()
                .replace("_", " ");
            context.append("- Biome: ").append(biomeName).append("\n");
        }

        // World/Dimension
        if (config.includePlayerWorld()) {
            String worldName = world.getEnvironment().toString();
            String worldDisplayName = formatWorldName(worldName);
            context.append("- Dimension: ").append(worldDisplayName).append("\n");
        }

        // Health
        if (config.includePlayerHealth()) {
            double health = player.getHealth();
            double maxHealth = player.getMaxHealth();
            int foodLevel = player.getFoodLevel();
            double saturation = player.getSaturation();
            
            context.append("- Health: ")
                   .append(formatHealth(health))
                   .append("/")
                   .append(formatHealth(maxHealth))
                   .append(" hearts\n");
            
            context.append("- Hunger: ")
                   .append(foodLevel)
                   .append("/20");
            
            if (saturation > 0) {
                context.append(" (saturation: ")
                       .append(COORD_FORMAT.format(saturation))
                       .append(")");
            }
            context.append("\n");
        }

        // Experience
        if (config.includePlayerExperience()) {
            int level = player.getLevel();
            float expProgress = player.getExp();
            int totalExp = player.getTotalExperience();
            
            context.append("- Experience: Level ")
                   .append(level)
                   .append(" (")
                   .append(COORD_FORMAT.format(expProgress * 100))
                   .append("% to next level)");
            
            if (totalExp > 0) {
                context.append(", Total XP: ")
                       .append(totalExp);
            }
            context.append("\n");
        }

        // Held Item
        if (config.includePlayerHeldItem()) {
            PlayerInventory inv = player.getInventory();
            
            // Check both hands
            ItemStack mainHand = inv.getItemInMainHand();
            ItemStack offHand = inv.getItemInOffHand();
            
            boolean hasMainHand = mainHand != null && !mainHand.getType().isAir() && mainHand.getAmount() > 0;
            boolean hasOffHand = offHand != null && !offHand.getType().isAir() && offHand.getAmount() > 0;
            
            // Debug logging if enabled
            if (config.isDebug()) {
                plugin.getLogger().info("[PlayerData] Player: " + player.getName() + 
                    ", Main Hand: " + (mainHand != null ? mainHand.getType().toString() : "null") + 
                    ", Off Hand: " + (offHand != null ? offHand.getType().toString() : "null") +
                    ", Selected Slot: " + inv.getHeldItemSlot());
            }
            
            if (hasMainHand) {
                String itemName = formatItemName(mainHand);
                context.append("- Held Item (Main Hand): ")
                       .append(itemName);
                
                // Add durability if it's a tool/armor
                if (mainHand.getType().getMaxDurability() > 0) {
                    int durability = mainHand.getType().getMaxDurability() - mainHand.getDurability();
                    int maxDurability = mainHand.getType().getMaxDurability();
                    int percent = (int) ((durability / (double) maxDurability) * 100);
                    context.append(" (Durability: ")
                           .append(percent)
                           .append("%)");
                }
                
                // Add amount if stackable
                if (mainHand.getAmount() > 1 && mainHand.getType().getMaxStackSize() > 1) {
                    context.append(" x").append(mainHand.getAmount());
                }
                
                context.append("\n");
            }
            
            // Also show off-hand item if it exists
            if (hasOffHand) {
                String itemName = formatItemName(offHand);
                context.append("- Held Item (Off Hand): ")
                       .append(itemName);
                
                // Add durability if it's a tool/armor
                if (offHand.getType().getMaxDurability() > 0) {
                    int durability = offHand.getType().getMaxDurability() - offHand.getDurability();
                    int maxDurability = offHand.getType().getMaxDurability();
                    int percent = (int) ((durability / (double) maxDurability) * 100);
                    context.append(" (Durability: ")
                           .append(percent)
                           .append("%)");
                }
                
                // Add amount if stackable
                if (offHand.getAmount() > 1 && offHand.getType().getMaxStackSize() > 1) {
                    context.append(" x").append(offHand.getAmount());
                }
                
                context.append("\n");
            }
            
            // If both hands are empty, check selected hotbar slot as fallback
            if (!hasMainHand && !hasOffHand) {
                int selectedSlot = inv.getHeldItemSlot();
                ItemStack hotbarItem = inv.getItem(selectedSlot);
                
                if (hotbarItem != null && !hotbarItem.getType().isAir() && hotbarItem.getAmount() > 0) {
                    String itemName = formatItemName(hotbarItem);
                    context.append("- Selected Item (Hotbar Slot ").append(selectedSlot + 1).append("): ")
                           .append(itemName);
                    
                    if (hotbarItem.getType().getMaxDurability() > 0) {
                        int durability = hotbarItem.getType().getMaxDurability() - hotbarItem.getDurability();
                        int maxDurability = hotbarItem.getType().getMaxDurability();
                        int percent = (int) ((durability / (double) maxDurability) * 100);
                        context.append(" (Durability: ")
                               .append(percent)
                               .append("%)");
                    }
                    
                    if (hotbarItem.getAmount() > 1 && hotbarItem.getType().getMaxStackSize() > 1) {
                        context.append(" x").append(hotbarItem.getAmount());
                    }
                    
                    context.append("\n");
                }
            }
        }

        // Equipment
        if (config.includePlayerEquipment()) {
            PlayerInventory inv = player.getInventory();
            ItemStack helmet = inv.getHelmet();
            ItemStack chestplate = inv.getChestplate();
            ItemStack leggings = inv.getLeggings();
            ItemStack boots = inv.getBoots();
            
            boolean hasEquipment = false;
            if (helmet != null && !helmet.getType().isAir()) {
                context.append("- Equipment: Helmet: ")
                       .append(formatItemName(helmet));
                hasEquipment = true;
            }
            if (chestplate != null && !chestplate.getType().isAir()) {
                if (hasEquipment) context.append(", ");
                else context.append("- Equipment: ");
                context.append("Chestplate: ")
                       .append(formatItemName(chestplate));
                hasEquipment = true;
            }
            if (leggings != null && !leggings.getType().isAir()) {
                if (hasEquipment) context.append(", ");
                else context.append("- Equipment: ");
                context.append("Leggings: ")
                       .append(formatItemName(leggings));
                hasEquipment = true;
            }
            if (boots != null && !boots.getType().isAir()) {
                if (hasEquipment) context.append(", ");
                else context.append("- Equipment: ");
                context.append("Boots: ")
                       .append(formatItemName(boots));
                hasEquipment = true;
            }
            if (hasEquipment) {
                context.append("\n");
            }
        }

        // Game Mode
        if (config.includePlayerGamemode()) {
            GameMode gameMode = player.getGameMode();
            context.append("- Game Mode: ")
                   .append(formatGameMode(gameMode))
                   .append("\n");
        }

        // Status Effects
        if (config.includePlayerEffects()) {
            Collection<PotionEffect> effects = player.getActivePotionEffects();
            boolean isOnFire = player.getFireTicks() > 0;
            
            if (!effects.isEmpty() || isOnFire) {
                context.append("- Status Effects: ");
                boolean first = true;
                
                if (isOnFire) {
                    int fireTicks = player.getFireTicks() / 20; // Convert to seconds
                    context.append("On Fire (").append(fireTicks).append("s)");
                    first = false;
                }
                
                for (PotionEffect effect : effects) {
                    if (!first) context.append(", ");
                    String effectName = formatEffectName(effect.getType());
                    int amplifier = effect.getAmplifier();
                    int duration = effect.getDuration() / 20; // Convert to seconds
                    
                    context.append(effectName);
                    if (amplifier > 0) {
                        context.append(" ").append(amplifier + 1); // Display as II, III, etc.
                    }
                    context.append(" (").append(formatDuration(duration)).append(")");
                    first = false;
                }
                context.append("\n");
            }
        }

        return context.toString();
    }

    /**
     * Format health value for display
     */
    private static String formatHealth(double health) {
        return COORD_FORMAT.format(health / 2.0);
    }

    /**
     * Format world name for display
     */
    private static String formatWorldName(String worldName) {
        switch (worldName.toUpperCase()) {
            case "NORMAL":
                return "Overworld";
            case "NETHER":
                return "Nether";
            case "THE_END":
                return "End";
            default:
                return worldName;
        }
    }

    /**
     * Format item name for display
     */
    private static String formatItemName(ItemStack item) {
        if (item == null) {
            return "Unknown Item";
        }
        
        // Check for custom display name first
        if (item.hasItemMeta()) {
            try {
                org.bukkit.inventory.meta.ItemMeta meta = item.getItemMeta();
                if (meta != null && meta.hasDisplayName()) {
                    return meta.getDisplayName();
                }
            } catch (Exception e) {
                // Fall through to default formatting
            }
        }
        
        // Convert Material name to readable format
        String materialName = item.getType().toString().toLowerCase();
        
        // Handle special cases
        if (materialName.equals("air") || materialName.equals("legacy_air")) {
            return "Empty";
        }
        
        // Replace underscores with spaces
        materialName = materialName.replace("_", " ");
        
        // Capitalize first letter of each word
        String[] words = materialName.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (word.isEmpty()) continue;
            if (result.length() > 0) result.append(" ");
            result.append(word.substring(0, 1).toUpperCase());
            if (word.length() > 1) {
                result.append(word.substring(1));
            }
        }
        
        return result.toString();
    }

    /**
     * Format game mode for display
     */
    private static String formatGameMode(GameMode gameMode) {
        switch (gameMode) {
            case SURVIVAL:
                return "Survival";
            case CREATIVE:
                return "Creative";
            case ADVENTURE:
                return "Adventure";
            case SPECTATOR:
                return "Spectator";
            default:
                return gameMode.toString();
        }
    }

    /**
     * Format effect name for display
     */
    private static String formatEffectName(PotionEffectType effectType) {
        String name = effectType.getName().toLowerCase()
            .replace("_", " ");
        
        // Capitalize first letter of each word
        String[] words = name.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (result.length() > 0) result.append(" ");
            result.append(word.substring(0, 1).toUpperCase())
                  .append(word.substring(1));
        }
        
        return result.toString();
    }

    /**
     * Format duration in seconds to readable format
     */
    private static String formatDuration(int seconds) {
        if (seconds < 60) {
            return seconds + "s";
        } else if (seconds < 3600) {
            int minutes = seconds / 60;
            int remainingSeconds = seconds % 60;
            if (remainingSeconds == 0) {
                return minutes + "m";
            }
            return minutes + "m " + remainingSeconds + "s";
        } else {
            int hours = seconds / 3600;
            int remainingMinutes = (seconds % 3600) / 60;
            if (remainingMinutes == 0) {
                return hours + "h";
            }
            return hours + "h " + remainingMinutes + "m";
        }
    }
}

