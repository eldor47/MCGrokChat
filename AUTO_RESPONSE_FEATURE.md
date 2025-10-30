# 🤖 Auto-Response Feature Guide

## Overview

The **Auto-Response** feature makes Grok feel like a real member of your Minecraft community! Instead of requiring players to use `@grok`, they can mention Grok naturally in conversation, and Grok will respond publicly to everyone.

**Status:** ✅ Enabled by default

---

## How It Works

### Two Response Modes

#### 1. Direct Mention (`@grok`) - Private
```
Player: "@grok how do I craft a beacon?"
[Grok] (private to player) To craft a beacon, you need 3 obsidian, 5 glass, and 1 nether star...
```
- ✉️ Response sent **only to the player**
- 💭 Shows "thinking" message
- 🔒 Private interaction

#### 2. Auto-Response (keywords) - Public
```
Player1: "I'm stuck on this build"
Player2: "hey grok any building tips?"
[Grok] (broadcast) Sure! Start with a solid foundation, use contrasting blocks for depth...
Player1: "oh that's helpful!"
Player3: "thanks grok!"
```
- 📢 Response **broadcast to everyone online**
- 🎭 Feels like Grok is part of the conversation
- 👥 Community feature

---

## Configuration

### Basic Setup (config.yml)

```yaml
chat:
  enable-auto-response: true  # ON by default
  
  auto-response-keywords:
    - "grok"
    - "hey grok"
    - "yo grok"
  
  auto-response-min-words: 3  # Minimum words to trigger
```

### Configuration Options

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `enable-auto-response` | boolean | `true` | Enable/disable auto-response |
| `auto-response-keywords` | list | `["grok", "hey grok", "yo grok"]` | Trigger words (case-insensitive) |
| `auto-response-min-words` | integer | `3` | Minimum words required in message |

---

## Examples

### Example 1: Natural Conversation
```
Player: "does anyone know where diamonds spawn?"
Player: "hey grok where are diamonds?"
[Grok] Diamonds spawn best between Y-levels -64 and -59. Branch mining at Y-59 is most efficient!
```

### Example 2: Quick Question
```
Player: "grok what's the best armor?"
[Grok] Full netherite armor with Protection IV is the best overall defense in the game!
```

### Example 3: Too Short (Ignored)
```
Player: "grok lol"  (only 2 words)
[Grok] (no response - below minimum word count)
```

### Example 4: No Keyword (Ignored)
```
Player: "how do I make a farm?"  (no keyword)
[Grok] (no response - keyword not detected)
```

---

## Comparison: @mention vs Auto-Response

| Feature | @grok (Direct) | Auto-Response |
|---------|----------------|---------------|
| **Who sees response?** | Player only | Everyone |
| **Thinking message?** | Yes | No |
| **Message processed** | Without @grok | Full message |
| **Use case** | Private queries | Community help |
| **Feels like** | Direct question | Natural chat |
| **Example** | `@grok help` | `hey grok help` |

---

## Why Two Modes?

### Direct Mention (`@grok`)
**Use when:**
- Player wants private help
- Question might be embarrassing
- Testing commands/features
- Admin queries

**Example scenarios:**
- "I forgot how to craft a chest" (private)
- "@grok am I in the right biome?" (private)
- "@grok test" (testing)

### Auto-Response
**Use when:**
- Community conversation
- Helping multiple players
- Making Grok feel present
- Creating engaging environment

**Example scenarios:**
- Natural chat flow
- Multiple players benefit
- Community knowledge sharing
- Making Grok feel like a member

---

## Configuration Recipes

### 1. Disable Auto-Response (Private Only)
```yaml
chat:
  enable-auto-response: false
```
Only `@grok` will work. Good for:
- Serious/competitive servers
- Cost control
- Private-only interactions

---

### 2. Strict Mode (Reduce Triggers)
```yaml
chat:
  enable-auto-response: true
  auto-response-keywords:
    - "hey grok"
    - "grok please"
  auto-response-min-words: 5
```
Requires explicit phrases and longer messages.

---

### 3. Friendly Mode (More Triggers)
```yaml
chat:
  enable-auto-response: true
  auto-response-keywords:
    - "grok"
    - "hey grok"
    - "yo grok"
    - "grok help"
    - "ask grok"
    - "grok question"
  auto-response-min-words: 3
```
More ways to trigger, better for community servers.

---

### 4. Roleplay Server (Custom Name)
```yaml
chat:
  enable-auto-response: true
  auto-response-keywords:
    - "merlin"
    - "hey merlin"
    - "wizard"
  auto-response-min-words: 3

advanced:
  system-prompt: "You are Merlin the Wise. Respond when addressed as Merlin or wizard. Call {player} 'young apprentice'."
```
Players talk to "Merlin" instead of "Grok"!

---

### 5. Cost-Optimized (Minimal Triggers)
```yaml
chat:
  enable-auto-response: true
  auto-response-keywords:
    - "grok"
  auto-response-min-words: 6
  
rate-limit:
  max-requests-per-hour: 10
```
Reduces API usage significantly.

---

## Best Practices

### ✅ DO

1. **Start with default settings** - They're well-balanced
2. **Monitor first week** - See how players use it
3. **Adjust min-words** - If getting too many/few triggers
4. **Match server theme** - Customize keywords for roleplay
5. **Enable debug temporarily** - To see what's triggering
6. **Educate players** - Explain the two modes

### ❌ DON'T

1. **Set min-words too low** - Results in spam/accidents
2. **Use common words** - "help" triggers too often
3. **Add too many keywords** - Increases costs
4. **Forget about costs** - Auto-response increases usage
5. **Disable without telling players** - They'll be confused

---

## Troubleshooting

### Problem: Too Many Triggers
**Solution:**
```yaml
auto-response-min-words: 5  # Increase from 3
```

### Problem: Not Triggering
**Checklist:**
- [ ] Is `enable-auto-response: true`?
- [ ] Is keyword in the list?
- [ ] Does message have enough words?
- [ ] Did you `/grokchat reload`?

### Problem: Triggering on Wrong Messages
**Example:** "That's so grok funny" (unintended)
**Solution:**
```yaml
auto-response-keywords:
  - "hey grok"  # Remove standalone "grok"
  - "yo grok"
auto-response-min-words: 4
```

### Problem: Costs Too High
**Solutions:**
1. Disable auto-response (only @grok)
2. Increase min-words to 5+
3. Reduce rate limit
4. Remove common keywords

---

## Technical Details

### Flow Diagram
```
Player sends chat message
    ↓
Contains @grok?
    ├─ YES → Extract question → Send to player only
    └─ NO
        ↓
    Auto-response enabled?
        ├─ NO → Ignore
        └─ YES
            ↓
        Has min words?
            ├─ NO → Ignore
            └─ YES
                ↓
            Contains keyword?
                ├─ NO → Ignore
                └─ YES → Send full message → Broadcast to all
```

### Code Implementation
- Checks for `@grok` first (takes priority)
- Falls back to keyword matching
- Case-insensitive keyword detection
- Broadcasts use `server.broadcastMessage()`
- Same rate limits/cooldowns apply to both modes

---

## Community Impact

### Benefits
✅ Makes Grok feel like a community member  
✅ Helps multiple players at once  
✅ Creates engaging server atmosphere  
✅ Natural conversation flow  
✅ Reduces barrier to asking questions  

### Considerations
⚠️ Increases API usage/costs  
⚠️ May trigger accidentally if not configured well  
⚠️ Can be spammy if cooldown too low  
⚠️ Public responses (privacy consideration)  

---

## Advanced Tips

### 1. Time-Based Keywords
Create different keywords for events:
```yaml
# During build competition
auto-response-keywords:
  - "grok build"
  - "hey grok building"
```

### 2. Role-Based Keywords
VIP players could have special keywords (requires plugin modification)

### 3. Context-Aware Prompts
```yaml
system-prompt: "You are Grok. When people ask in public chat (auto-response), be extra friendly and helpful since everyone is watching. With {player}."
```

### 4. Cooldown Strategies
- Lower cooldown for @grok (private)
- Higher cooldown for auto-response (public)
- Currently they share the same cooldown

---

## Testing Your Configuration

1. **Enable debug mode:**
```yaml
advanced:
  debug: true
```

2. **Test direct mention:**
```
@grok test
```

3. **Test auto-response:**
```
hey grok test message here
```

4. **Test too short:**
```
grok hi
```

5. **Test no keyword:**
```
how do I do this?
```

6. **Check logs:**
```
[GrokChat] player triggered Grok (auto-response): hey grok test
[GrokChat] Grok responded: Hello!
```

---

## Migration Guide

### From v0.9 (No Auto-Response)
Auto-response is **enabled by default** in v1.0. If you want old behavior:

```yaml
chat:
  enable-auto-response: false  # Disable new feature
```

### Informing Players
Example server announcement:
```
🤖 New Feature: Grok Auto-Response!

You can now talk to Grok naturally:
• "hey grok where are diamonds?" → Everyone sees response
• "@grok where are diamonds?" → Only you see response

Try it out! 🎉
```

---

## FAQ

**Q: Does auto-response cost more?**  
A: Potentially yes, since more messages trigger Grok. Monitor usage and adjust settings.

**Q: Can I have different cooldowns for each mode?**  
A: Not currently - feature request!

**Q: Will it trigger if I say "That's so grok"?**  
A: Yes, unless you adjust keywords. Use phrases like "hey grok" to prevent this.

**Q: Can I customize who sees auto-responses?**  
A: Not currently - broadcasts to all. Feature request!

**Q: What if two players trigger at once?**  
A: Both get processed (rate limits/cooldowns permitting).

**Q: Does it work in private messages?**  
A: No, only in public chat.

---

## Summary

The Auto-Response feature makes Grok an active community member rather than just a command bot. It's **enabled by default** because it creates a better player experience, but can be easily disabled or fine-tuned to match your server's needs.

**Key Takeaway:** You now have TWO ways to use Grok:
1. **`@grok`** - Private, explicit
2. **Natural mention** - Public, conversational

Choose what works best for your community! 🚀

---

**See Also:**
- `examples/config-auto-response.yml` - Detailed configuration examples
- `README.md` - Feature overview
- `CUSTOMIZATION_GUIDE.md` - System prompts and personalities

