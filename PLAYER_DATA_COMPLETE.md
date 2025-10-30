# Player Data Context - Complete Feature Guide

## Overview

GrokChat now supports **9 types of player data** that can be sent to Grok for incredibly contextual and personalized responses!

**Status:** ✅ All 9 data types implemented  
**Default:** Disabled (privacy-first approach)

---

## ✅ Available Player Data Types

### 1. **Position** (`include-position`)
- X, Y, Z coordinates
- Y-level (critical for mining advice)
- **Example:** "Position: 123.45, 64.00, -789.12 (Y-level: 64)"

### 2. **Biome** (`include-biome`)
- Current biome name
- **Example:** "Biome: plains" or "Biome: desert"

### 3. **World/Dimension** (`include-world`)
- Current dimension
- **Example:** "Dimension: Overworld" or "Dimension: Nether"

### 4. **Health** (`include-health`)
- Current health hearts
- Max health
- Hunger level (0-20)
- Saturation level
- **Example:** "Health: 17.5/20 hearts, Hunger: 18/20 (saturation: 12.5)"

### 5. **Experience** (`include-experience`) ⭐ NEW!
- Current XP level
- Progress percentage to next level
- Total XP points
- **Example:** "Experience: Level 25 (45% to next level), Total XP: 1500"

### 6. **Held Item** (`include-held-item`) ⭐ NEW!
- Currently held item name
- Durability percentage (for tools/weapons)
- **Example:** "Held Item: Diamond Sword (Durability: 85%)"

### 7. **Equipment** (`include-equipment`) ⭐ NEW!
- All armor pieces (helmet, chestplate, leggings, boots)
- **Example:** "Equipment: Helmet: Netherite Helmet, Chestplate: Netherite Chestplate..."

### 8. **Game Mode** (`include-gamemode`) ⭐ NEW!
- Survival, Creative, Adventure, or Spectator
- **Example:** "Game Mode: Survival"

### 9. **Status Effects** (`include-effects`) ⭐ NEW!
- Active potion effects (Speed, Fire Resistance, etc.)
- Effect level (I, II, III, etc.)
- Duration remaining
- Fire status (if on fire)
- **Example:** "Status Effects: Speed II (3m 45s), Fire Resistance (5m 20s)"

---

## 🎯 Real-World Examples

### Example 1: XP-Aware Enchantment Advice
```
Player (Level 25): "@grok what enchants should I get?"

Player Context:
- Experience: Level 25 (45% to next level)
- Held Item: Diamond Sword (Durability: 85%)
- Equipment: Full Netherite Armor

[Grok] At level 25, you can access level 30 enchantments! Since you're holding 
a diamond sword, prioritize Sharpness V, Sweeping Edge III, and Unbreaking III. 
For your netherite armor, get Protection IV on all pieces. You're 45% to level 26, 
so you'll have enough XP soon for those top-tier enchants!
```

### Example 2: Equipment-Aware Repair Advice
```
Player (85% durability sword): "@grok how do I repair this?"

Player Context:
- Held Item: Diamond Sword (Durability: 85%)
- Game Mode: Survival

[Grok] Your diamond sword is at 85% durability. In Survival mode, repair it at 
an anvil with another diamond sword or diamonds. Better yet, enchant it with 
Mending so it repairs automatically from XP! Since you're in Survival, save 
your diamonds - use Mending if possible.
```

### Example 3: Effect-Aware Safety Advice
```
Player (has Fire Resistance, in Nether): "@grok can I swim through lava?"

Player Context:
- Dimension: Nether
- Status Effects: Fire Resistance (5m 20s)
- Health: 18/20 hearts

[Grok] Yes! With Fire Resistance active for 5 more minutes, you can safely 
swim through lava in the Nether. Just watch out for magma blocks - they still 
do damage even with Fire Resistance! Also, be careful of ghasts shooting at you 
while you're in lava.
```

### Example 4: Mode-Specific Building Tips
```
Player (Creative mode): "@grok fastest way to build a castle?"

Player Context:
- Game Mode: Creative
- Position: 100, 64, 200

[Grok] In Creative mode, use /fill commands for large structures! For your castle 
at coordinates (100, 64, 200), try:
- /fill 100 64 200 150 80 250 minecraft:stone_bricks for walls
- Hold middle-click to copy blocks
- Use Ctrl+Q to drop entire stacks
- WorldEdit plugin would make this even faster!
```

### Example 5: Location + Item + XP Context
```
Player (@ Y-59, Level 30, holding Iron Pickaxe): "@grok best mining strategy?"

Player Context:
- Position: 123, -59, 456 (Y-level: -59)
- Experience: Level 30
- Held Item: Iron Pickaxe (Durability: 92%)

[Grok] Perfect! You're at Y-level -59, which is the best spot for diamonds! 
Since you have an iron pickaxe at 92% durability, you're ready to mine. At level 30, 
you can enchant with Fortune III for better diamond drops. Try branch mining with 
2-block spacing. Your iron pickaxe will break diamond ore, but consider upgrading 
to diamond for better efficiency!
```

---

## ⚙️ Configuration

### Full Configuration Options

```yaml
player-data:
  enabled: false  # Master switch (OFF by default)
  
  # Basic location data
  include-position: true
  include-biome: true
  include-world: true
  include-health: true
  
  # New advanced data
  include-experience: true
  include-held-item: true
  include-equipment: true
  include-gamemode: true
  include-effects: true
  
  # Usage modes
  use-in-auto-response: false  # Usually disabled (privacy)
  use-in-direct-mention: true   # Recommended for @grok
```

### Granular Control

You can enable/disable individual data types:

```yaml
player-data:
  enabled: true
  
  # Only include what you want
  include-position: true
  include-biome: true
  include-world: false  # Disabled
  include-health: true
  include-experience: true
  include-held-item: true
  include-equipment: false  # Disabled
  include-gamemode: true
  include-effects: false  # Disabled
```

---

## 💡 Use Cases

### For Enchantment Advice
**Enable:** `include-experience`, `include-held-item`, `include-equipment`
```
[Grok] At level 25 with your diamond sword, get Sharpness V! For your netherite 
armor, Protection IV is essential.
```

### For Repair Advice
**Enable:** `include-held-item`, `include-gamemode`
```
[Grok] Your diamond sword is at 85% durability. In Survival mode, repair it at 
an anvil with another diamond sword or diamonds. Better yet, enchant with Mending!
```

### For Safety Advice
**Enable:** `include-effects`, `include-dimension`, `include-health`
```
[Grok] You have Fire Resistance active, so you can safely swim through lava in 
the Nether. Watch out for magma blocks though - they still damage!
```

### For Building Advice
**Enable:** `include-gamemode`, `include-position`
```
[Grok] In Creative mode at your location, use /fill commands for large structures. 
Try /fill 100 64 200 150 80 250 minecraft:stone_bricks!
```

### For Mining Advice
**Enable:** `include-position`, `include-held-item`, `include-experience`
```
[Grok] You're at Y-59 with an iron pickaxe - perfect for diamonds! At level 30, 
enchant with Fortune III for better drops. Upgrade to diamond pickaxe for efficiency.
```

---

## 📊 Data Format Examples

### Minimal Data (Position Only)
```
Player context:
- Position: 123.45, 64.00, -789.12 (Y-level: 64)
```

### Standard Data (Location + Health)
```
Player context:
- Position: 123.45, 64.00, -789.12 (Y-level: 64)
- Biome: plains
- Dimension: Overworld
- Health: 17.5/20 hearts
- Hunger: 18/20 (saturation: 12.5)
```

### Full Data (Everything Enabled)
```
Player context:
- Position: 123.45, 64.00, -789.12 (Y-level: 64)
- Biome: plains
- Dimension: Overworld
- Health: 17.5/20 hearts
- Hunger: 18/20 (saturation: 12.5)
- Experience: Level 25 (45% to next level), Total XP: 1500
- Held Item: Diamond Sword (Durability: 85%)
- Equipment: Helmet: Netherite Helmet, Chestplate: Netherite Chestplate, Leggings: Netherite Leggings, Boots: Netherite Boots
- Game Mode: Survival
- Status Effects: Speed II (3m 45s), Fire Resistance (5m 20s)
```

---

## 🎯 Recommended Configurations

### 1. Privacy-First (Default)
```yaml
player-data:
  enabled: false  # Everything disabled
```
**Best for:** Servers prioritizing privacy

---

### 2. Location-Only
```yaml
player-data:
  enabled: true
  include-position: true
  include-biome: true
  include-world: true
  include-health: false
  include-experience: false
  include-held-item: false
  include-equipment: false
  include-gamemode: false
  include-effects: false
```
**Best for:** Basic location-aware advice without personal data

---

### 3. Smart Assistant (Recommended)
```yaml
player-data:
  enabled: true
  include-position: true
  include-biome: true
  include-world: true
  include-health: true
  include-experience: true
  include-held-item: true
  include-equipment: true
  include-gamemode: true
  include-effects: true
  use-in-direct-mention: true
  use-in-auto-response: false
```
**Best for:** Maximum helpfulness for @grok, privacy for auto-response

---

### 4. Full Context (Premium)
```yaml
player-data:
  enabled: true
  # All enabled
  include-position: true
  include-biome: true
  include-world: true
  include-health: true
  include-experience: true
  include-held-item: true
  include-equipment: true
  include-gamemode: true
  include-effects: true
  use-in-auto-response: true  # Use everywhere
  use-in-direct-mention: true
```
**Best for:** Servers with API budget wanting maximum context

---

## 💰 Token Usage Impact

### Per Data Type (Approximate)
- **Position:** ~20 tokens
- **Biome:** ~5 tokens
- **World:** ~5 tokens
- **Health:** ~15 tokens
- **Experience:** ~15 tokens
- **Held Item:** ~10-20 tokens (depends on item name)
- **Equipment:** ~30-50 tokens (depends on armor)
- **Game Mode:** ~5 tokens
- **Status Effects:** ~20-40 tokens (depends on number of effects)

### Total Impact
- **Minimal (position only):** ~30 tokens
- **Standard (location + health):** ~60 tokens
- **Full (everything):** ~150-250 tokens

**Recommendation:** Enable only what you need! Each data type adds tokens.

---

## 🔐 Privacy Considerations

### What Data is Shared?
- ✅ Position coordinates
- ✅ Current biome
- ✅ Dimension name
- ✅ Health/hunger levels
- ✅ XP level and progress
- ✅ Held item name and durability
- ✅ Armor pieces
- ✅ Game mode
- ✅ Active potion effects

### Not Shared
- ❌ Full inventory contents
- ❌ Player balance/money
- ❌ Other players' locations
- ❌ Server IP/details
- ❌ Private messages
- ❌ Command history

### Privacy Controls
1. **Master Switch:** `enabled: false` disables everything
2. **Per-Type Control:** Disable individual data types
3. **Per-Mode Control:** Use only for direct mentions, not auto-response
4. **Disabled by Default:** Privacy-first approach

---

## 🎓 Best Practices

### ✅ DO
- Enable only what you need
- Use for direct mentions (`@grok`) by default
- Start with basic data (position, biome, world)
- Add advanced data (XP, items, effects) if helpful
- Monitor token usage and adjust

### ❌ DON'T
- Enable everything unless you have budget
- Use in auto-response unless players know data is shared
- Enable sensitive data without informing players
- Forget to configure per-type settings
- Overlook privacy implications

---

## 🚀 Examples by Server Type

### PvP Server
```yaml
player-data:
  enabled: true
  include-position: false  # Privacy - don't share location
  include-health: true
  include-equipment: true
  include-effects: true
  include-experience: true
```

### Survival Server
```yaml
player-data:
  enabled: true
  include-position: true
  include-biome: true
  include-world: true
  include-health: true
  include-experience: true
  include-held-item: true
```

### Creative Server
```yaml
player-data:
  enabled: true
  include-position: true
  include-gamemode: true
  include-held-item: true
  include-health: false
  include-experience: false
```

---

## 📝 Summary

**9 Data Types Available:**
1. ✅ Position (X, Y, Z, Y-level)
2. ✅ Biome
3. ✅ World/Dimension
4. ✅ Health/Hunger
5. ✅ Experience Level ⭐ NEW
6. ✅ Held Item ⭐ NEW
7. ✅ Equipment/Armor ⭐ NEW
8. ✅ Game Mode ⭐ NEW
9. ✅ Status Effects ⭐ NEW

**Benefits:**
- 🎯 Location-specific advice
- ⭐ XP-aware enchantment suggestions
- 🛠️ Item-aware repair/crafting help
- 🛡️ Equipment-aware upgrade advice
- 🎮 Mode-specific tips
- ⚡ Effect-aware safety guidance

**Privacy:** ✅ Disabled by default, fully configurable

**Performance:** ✅ Fast data gathering (< 10ms)

**Cost:** ⚠️ Adds ~20-250 tokens per request depending on enabled data

---

**Ready to use!** Enable `player-data.enabled: true` and configure which data types you want. Grok will use this rich context to give incredibly personalized and helpful responses! 🚀

