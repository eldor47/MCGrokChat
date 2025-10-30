# Chat History Context Feature Guide

## Overview

The **Chat History Context** feature allows Grok to understand ongoing conversations by providing recent chat messages as context. This makes Grok's responses much more relevant and conversational!

**Status:** ✅ Enabled by default

---

## How It Works

### Without Chat History:
```
Player1: "I can't find any diamonds"
Player2: "hey grok where should I look?"
[Grok] Diamonds spawn between Y-levels -64 and -59. Branch mine for best results!
```

### With Chat History (NEW!):
```
Player1: "I can't find any diamonds"
Player2: "I've been mining at Y-60"
Player3: "hey grok any tips?"
[Grok] Since you're already at Y-60, you're in the right area! Try branch mining with 2-block spacing. Make sure you're looking in caves and ravines too - diamonds are more common there now!
```

Grok saw the conversation context and gave a more relevant answer! 🎯

---

## Configuration

### Basic Setup (config.yml)

```yaml
chat-history:
  # Enable chat history for context
  enabled: true
  
  # Number of recent messages to keep (5-10 recommended)
  size: 10
  
  # Include history in auto-responses (public)
  use-in-auto-response: true
  
  # Include history in direct mentions (private)
  use-in-direct-mention: false
```

### Configuration Options

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `enabled` | boolean | `true` | Enable/disable chat history |
| `size` | integer | `10` | Number of messages to keep |
| `use-in-auto-response` | boolean | `true` | Use history for `"hey grok"` mentions |
| `use-in-direct-mention` | boolean | `false` | Use history for `@grok` mentions |

---

## Use Cases

### 1. Follow-Up Questions
```
Player: "I need to build a farm"
Player: "grok what kind of farm is easiest?"
[Grok] Based on your needs, a wheat farm is the easiest to start! Just till dirt, plant seeds, and wait for rain or use water.
```

### 2. Clarifying Context
```
Player1: "This enchantment table isn't working"
Player2: "Are you using lapis?"
Player1: "yes"
Player2: "grok what else could be wrong?"
[Grok] Since they're using lapis, check if you have enough bookshelves around the enchantment table - you need 15 for max level enchantments, placed 1 block away.
```

### 3. Group Discussions
```
Player1: "Should we fight the dragon tonight?"
Player2: "I don't have good armor yet"
Player3: "I have extra potions"
Player4: "grok what do we need to be ready?"
[Grok] From your conversation, sounds like you need better armor first! For the dragon you'll want:
- Full diamond/netherite armor
- Plenty of arrows
- Slow falling potions (good you have some!)
- Ender pearls
- Beds for quick kills
```

### 4. Problem Solving
```
Player: "My redstone circuit isn't working"
Player: "I added a repeater but still nothing"
Player: "grok any ideas?"
[Grok] Since you added a repeater and it's still not working, check:
- Is the repeater pointing the right direction?
- Is there a power source?
- Are there any broken connections?
Try replacing the repeater first - sometimes they get stuck.
```

---

## Benefits

### ✅ Pros
- **More Relevant** - Grok understands conversation flow
- **Contextual** - Responses reference what was just said
- **Natural** - Feels like a real conversation
- **Helpful** - Can answer follow-ups without re-explaining
- **Group-Friendly** - Works great for team discussions

### ⚠️ Considerations
- **Token Usage** - More context = higher API costs
- **Privacy** - Chat history is shared with API
- **Memory** - Keeps last 10 messages in RAM
- **Accuracy** - Grok needs clear context to help

---

## Configuration Recipes

### 1. Default (Recommended)
```yaml
chat-history:
  enabled: true
  size: 10
  use-in-auto-response: true
  use-in-direct-mention: false
```
- Auto-responses get full context
- Direct mentions don't (cost savings)
- Perfect balance!

---

### 2. Full Context Mode (Max Helpfulness)
```yaml
chat-history:
  enabled: true
  size: 15
  use-in-auto-response: true
  use-in-direct-mention: true
```
- Both modes get context
- Larger history
- Best for small servers
- Higher API costs

---

### 3. Minimal Context (Cost Optimized)
```yaml
chat-history:
  enabled: true
  size: 5
  use-in-auto-response: true
  use-in-direct-mention: false
```
- Only 5 messages
- Auto-response only
- Lower costs
- Still helpful!

---

### 4. Disabled (No Context)
```yaml
chat-history:
  enabled: false
```
- No chat history
- Lowest costs
- Each question standalone
- Like version 1.0 behavior

---

### 5. Private Only (Direct Mentions)
```yaml
chat-history:
  enabled: true
  size: 8
  use-in-auto-response: false
  use-in-direct-mention: true
```
- Only `@grok` gets context
- Public responses standalone
- Unique use case!

---

## Technical Details

### What Gets Captured
- ✅ Player name
- ✅ Message content
- ✅ Timestamp
- ❌ NOT commands (starting with `/`)
- ❌ NOT Grok's responses

### Storage
- Kept in memory (RAM)
- Cleared on server restart
- Thread-safe implementation
- Fixed size (oldest messages removed)

### Context Format Sent to API
```
Recent chat context:
Player1: I'm building a castle
Player2: Need help with the walls
Player3: What blocks should we use?
```

This is prepended to the system prompt.

### API Impact
- Increases token usage per request
- More tokens = slightly higher cost
- But responses are MUCH better!
- Usually worth the small increase

---

## Best Practices

### ✅ DO
1. **Use 5-10 messages** - Good balance
2. **Enable for auto-response** - Makes public chat better
3. **Monitor costs** - Check if context helps enough
4. **Adjust size based on server** - Bigger servers may want less
5. **Test different configs** - Find what works for you

### ❌ DON'T
1. **Set size too high** - Over 15 is excessive
2. **Enable if privacy is critical** - Chat is sent to API
3. **Forget to test** - See if it actually helps
4. **Leave enabled if not used** - Disable to save costs
5. **Expect perfect understanding** - Context helps but isn't magic

---

## Privacy Considerations

### What's Shared with xAI API
- Last N messages from server chat
- Player names
- Message content

### What's NOT Shared
- Private messages
- Command history
- Player locations
- Server IP/details

### Control Options
1. **Disable entirely** - Set `enabled: false`
2. **Limit size** - Smaller size = less data shared
3. **Auto-response only** - Only public mentions get context
4. **Inform players** - Let them know context is used

---

## Examples in Action

### Example 1: Building Advice
```
[Recent Context]
Steve: I want to build something cool
Alex: Try a castle!
Steve: How do I start?

Steve: "grok any building tips?"

[Grok sees context]
"Since you're planning a castle, start with the walls! Use stone bricks or cobblestone for a medieval look. Map out the foundation first - at least 20x20 blocks. Build corner towers first, then connect them with walls."
```

### Example 2: Troubleshooting
```
[Recent Context]
Notch: My farm won't grow anything
Notch: I have water nearby
Notch: And it's lit up

Notch: "grok what's wrong?"

[Grok sees context]
"Your farm has water and light which is good! Check these:
1. Did you till the dirt with a hoe?
2. Is the water within 4 blocks of all farmland?
3. Are you staying close by? Crops only grow in loaded chunks
4. Is there enough space above? Some crops need 2 blocks of air"
```

### Example 3: Group Planning
```
[Recent Context]
Player1: Want to raid a bastion tonight?
Player2: I'm in!
Player3: Do we need anything special?
Player1: Not sure

Player3: "hey grok what should we bring?"

[Grok sees context]
"For a bastion raid, you'll need:
- Gold armor (piglins won't attack)
- Fire resistance potions (lava everywhere!)
- Food and healing
- Ender chest for valuable loot
- Blocks to bridge gaps
- Good weapons for piglin brutes
Stay together and watch for magma cubes!"
```

---

## Troubleshooting

### Problem: Context Not Helping
**Solution:**
- Increase history size to 12-15
- Make sure `enabled: true`
- Check relevant messages are being captured

### Problem: Costs Too High
**Solution:**
- Reduce history size to 5
- Disable for direct mentions
- Or disable entirely

### Problem: Irrelevant Context
**Solution:**
- Reduce history size
- Chat may be too varied
- Consider disabling if not helpful

### Problem: Privacy Concerns
**Solution:**
- Disable chat history
- Inform players about feature
- Consider auto-response only

---

## Cost Analysis

### Typical Impact
- **No context**: ~300 tokens per request
- **With 10 messages**: ~450-500 tokens per request
- **Increase**: ~50-70% more tokens
- **Cost increase**: Depends on xAI pricing

### Is It Worth It?
**YES if:**
- Players ask follow-up questions
- Group discussions are common
- Server has active chat
- Better answers justify cost

**NO if:**
- Standalone questions only
- Low server activity
- Very tight budget
- Privacy is critical

---

## Advanced Tips

### 1. Dynamic Sizing
Adjust based on server population:
```yaml
# Small server (< 10 players)
size: 15

# Medium server (10-50 players)
size: 10

# Large server (50+ players)
size: 5
```

### 2. Time-Based Config
Change during peak hours:
```yaml
# Peak hours (more context needed)
size: 12

# Off-peak (save costs)
size: 6
```

### 3. Event-Specific
During events, increase context:
```yaml
# During build competitions/raids
size: 15
use-in-auto-response: true
```

---

## FAQ

**Q: Does chat history persist across restarts?**  
A: No, it's cleared on restart to save memory.

**Q: Can I see what context is sent?**  
A: Enable `debug: true` in advanced settings to log requests.

**Q: Does it capture commands?**  
A: No, only normal chat messages (not starting with `/`).

**Q: What about private messages?**  
A: Private messages are never captured.

**Q: Can players opt-out?**  
A: Not individually - it's server-wide. Inform players via rules.

**Q: How much RAM does it use?**  
A: Minimal - ~1KB per message, so ~10KB for 10 messages.

**Q: Can I clear history manually?**  
A: Currently only via `/grokchat reload`.

**Q: Does history work with @grok mentions?**  
A: Only if `use-in-direct-mention: true` (off by default).

**Q: What if chat is very fast?**  
A: Recent messages stay - oldest are removed automatically.

**Q: Can I exclude certain players?**  
A: Not currently - feature request!

---

## Summary

Chat History Context makes Grok:
- ✅ **Smarter** - Understands conversations
- ✅ **More helpful** - Better answers
- ✅ **Natural** - Feels like a participant
- ✅ **Contextual** - References what was said

**Default config is great for most servers!** Try it and adjust based on your needs.

**Recommended:** Keep default settings (enabled, 10 messages, auto-response only) for the best balance of helpfulness and cost!

---

**See Also:**
- `AUTO_RESPONSE_FEATURE.md` - Auto-response documentation  
- `CUSTOMIZATION_GUIDE.md` - System prompts guide
- `README.md` - Main documentation

