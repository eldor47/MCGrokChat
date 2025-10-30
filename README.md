# MCGrokChat - Bring Grok AI to Minecraft! 🤖⛏️

[![GitHub](https://img.shields.io/badge/GitHub-MCGrokChat-brightgreen.svg)](https://github.com/eldor47/MCGrokChat)
[![Spigot](https://img.shields.io/badge/Spigot-1.21+-orange.svg)](https://www.spigotmc.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

**Minecraft plugin that integrates Grok AI into server chat with @mentions, auto-response, and contextual awareness**

Integrate Grok AI (by xAI) directly into your Minecraft server! Players can ask Grok questions using `@grok` in chat or the `/grok` command.

## ✨ Features

- 🗨️ **Chat Integration** - Mention `@grok` in chat to ask questions
- 🤖 **Auto-Response** - Grok joins conversations when mentioned naturally (ON by default!)
- 🧠 **Chat History Context** - Grok understands conversation flow (last 10 messages)
- 📍 **Player Data Context** - Optional location/biome/health awareness for better advice
- ⚡ **Command Support** - Use `/grok <question>` for direct queries
- ⌨️ **Tab Completion** - Smart type-ahead for all commands
- 🔒 **Rate Limiting** - Configurable hourly request limits per player
- ⏱️ **Cooldown System** - Prevent spam with customizable cooldowns
- 🎨 **Customizable Responses** - Configure colors, prefixes, and message formats
- 🎭 **Custom Personalities** - Change Grok's behavior with system prompts
- 🔧 **Admin Tools** - Reload config, test API, view stats, and more
- 📊 **Usage Statistics** - Track player usage and remaining requests
- 🔐 **Permissions System** - Fine-grained control over who can use what
- 🐛 **Debug Mode** - Detailed logging for troubleshooting

## 📋 Requirements

- Java 17 or higher
- Spigot/Paper 1.20+ (may work on earlier versions)
- xAI API key (get yours at [console.x.ai](https://console.x.ai/))

## 🚀 Installation

1. **Download** the latest `MCGrokChat.jar` from [releases](https://github.com/eldor47/MCGrokChat/releases)
2. **Place** the jar in your server's `plugins` folder
3. **Start** your server to generate the config file
4. **Stop** your server
5. **Edit** `plugins/MCGrokChat/config.yml` and add your xAI API key
6. **Start** your server again

## 🔑 Getting Your API Key

1. Visit [https://console.x.ai/](https://console.x.ai/)
2. Sign in with your X (Twitter) account
3. Create a new API key
4. Copy the key and paste it in `config.yml`

## ⚙️ Configuration

Edit `plugins/MCGrokChat/config.yml`:

```yaml
api:
  # Your xAI API key (required)
  key: "your-xai-api-key-here"
  
  # Model to use
  model: "grok-beta"
  
  # Request timeout in seconds
  timeout: 30

chat:
  # Enable @grok mentions in chat
  enable-mentions: true
  
  # Prefix for triggering Grok
  mention-prefix: "@grok"
  
  # Enable /grok command
  enable-command: true
  
  # Maximum characters per message
  max-message-length: 500
  
  # Cooldown between requests (seconds)
  cooldown: 5

response:
  # Maximum response length
  max-length: 256
  
  # Color of Grok's responses (AQUA, GREEN, YELLOW, etc.)
  color: "AQUA"
  
  # Prefix for Grok's responses
  prefix: "&b[Grok]&r "

rate-limit:
  # Enable rate limiting
  enabled: true
  
  # Max requests per player per hour
  max-requests-per-hour: 20

advanced:
  # Temperature for responses (0.0-2.0, higher = more creative)
  temperature: 0.7
  
  # Maximum tokens in response
  max-tokens: 500
  
  # System prompt (customize Grok's personality)
  # Use {player} placeholder for player name
  system-prompt: "You are Grok, an AI assistant integrated into a Minecraft server. You're helpful, witty, and concise. Keep responses brief and engaging. You're talking to player: {player}"
  
  # Enable debug mode
  debug: false
```

## 📝 Commands

### Player Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/grok <question>` | Ask Grok a question | `grokchat.command` |
| `@grok <question>` | Mention Grok in chat | `grokchat.use` |

**Tab Completion:** Press TAB while typing for smart suggestions!
```
/grok <TAB>           → how, what, where, when, why, can, help
/grok how <TAB>       → do, I, make, craft, find, get, build
/grok how do I <TAB>  → diamonds, iron, gold, netherite, etc.
```

**Examples:**
```
@grok How do I make a diamond pickaxe?
/grok What are the best enchantments for a sword?
@grok Tell me a Minecraft joke
```

### Admin Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/grokchat reload` | Reload configuration | `grokchat.admin` |
| `/grokchat info` | Show plugin information | `grokchat.admin` |
| `/grokchat setkey <key>` | Set API key | `grokchat.admin` |
| `/grokchat test` | Test API connection | `grokchat.admin` |
| `/grokchat stats [player]` | View usage statistics | `grokchat.admin` |

**Tab Completion:** Press TAB for command & player suggestions!
```
/grokchat <TAB>        → reload, info, setkey, test, stats
/grokchat stats <TAB>  → [list of online players]
```

**Aliases:** `/gc`, `/ask`, `/ai`

## 🔐 Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `grokchat.use` | Use @grok in chat | `true` |
| `grokchat.command` | Use /grok command | `true` |
| `grokchat.admin` | Access admin commands | `op` |

## 🎮 Usage Examples

### For Players

**Direct Mention (Private Response):**
```
Player: @grok how do I find diamonds?
[Grok] The best way to find diamonds is to mine at Y-level -59 in version 1.19+. 
[Grok] Bring iron pickaxes and watch out for lava!
```
*Only the player sees the response*

**Auto-Response (Public, NEW!):**
```
Player: hey grok what's the best enchantment?
[Grok] For swords, Sharpness V is essential! Also get Sweeping Edge III for 
[Grok] crowd control. Don't forget Looting III for better drops!
```
*Everyone online sees the response - Grok joins the conversation naturally!*

**Using Command:**
```
/grok what's the best food in minecraft?
[Grok] Golden carrots are the best food for saturation, but cooked porkchops 
[Grok] and steak are great for hunger restoration!
```
*Private response to player*

### For Admins

**Test API Connection:**
```
/grokchat test
✓ API connection successful!
Response: OK
```

**View Player Stats:**
```
/grokchat stats Steve
=== Grok Stats for Steve ===
Requests Used: 5/20
Requests Remaining: 15
Cooldown: Ready
```

**Reload Config:**
```
/grokchat reload
✓ GrokChat configuration reloaded!
```

## 🛠️ Building from Source

Requirements:
- Maven 3.6+
- Java 17+

```bash
# Clone the repository
git clone https://github.com/yourusername/grokchat.git
cd grokchat

# Build with Maven
mvn clean package

# Find the jar in target/GrokChat-1.0.0.jar
```

## 🐛 Troubleshooting

### Plugin won't load
- Make sure you're using Java 17+
- Check server logs for errors
- Verify you have Spigot/Paper 1.20+

### "API key not configured" warning
- Edit `plugins/GrokChat/config.yml`
- Replace `your-xai-api-key-here` with your actual API key
- Run `/grokchat reload`

### API connection failed
- Check your API key is valid at [console.x.ai](https://console.x.ai/)
- Verify your server has internet access
- Check firewall isn't blocking `api.x.ai`
- Enable debug mode: set `advanced.debug: true` in config

### Responses are cut off
- Increase `response.max-length` in config
- Increase `advanced.max-tokens` for longer responses

### Rate limit issues
- Adjust `rate-limit.max-requests-per-hour`
- Or disable rate limiting: `rate-limit.enabled: false`

## 📊 API Usage & Costs

GrokChat uses the xAI API. Check [xAI's pricing page](https://x.ai/api) for current rates.

**Tips to reduce costs:**
- Lower `advanced.max-tokens` in config
- Increase `chat.cooldown` to reduce requests
- Lower `rate-limit.max-requests-per-hour`
- Disable features you don't need

## 🔒 Privacy & Security

- **API Key Security**: Your API key is stored in `config.yml`. Use proper file permissions!
- **Query Logging**: Disabled by default. Enable with `privacy.log-queries: true`
- **Rate Limiting**: Prevents abuse and controls costs
- **Context Sharing**: Disabled by default for privacy

## 🤝 Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Credits

- Built for Spigot/Paper Minecraft servers
- Powered by [Grok](https://grok.x.ai/) by xAI
- Inspired by Twitter/X's @grok feature

## 📞 Support

- **Issues**: [GitHub Issues](https://github.com/yourusername/grokchat/issues)
- **Discord**: [Your Discord Server](#)
- **Wiki**: [GitHub Wiki](https://github.com/yourusername/grokchat/wiki)

## 🤖 Auto-Response Feature

Grok can **join conversations naturally** when players mention keywords! This is **ON by default**.

**How it works:**
- **`@grok` mention** → Private response to player only
- **"hey grok" in chat** → Public response everyone sees!

**Configure in config.yml:**
```yaml
chat:
  enable-auto-response: true  # ON by default
  auto-response-keywords:
    - "grok"
    - "hey grok"
    - "yo grok"
  auto-response-min-words: 3  # Prevents spam
```

**Example Conversation:**
```
Player1: "I can't find any diamonds"
Player2: "hey grok where are diamonds?"
[Grok] Diamonds spawn best at Y-level -59! Branch mine and you'll find them.
Player1: "thanks!"
```

**To disable:** Set `enable-auto-response: false` in config

See `examples/config-auto-response.yml` for detailed configuration options!

## 🧠 Chat History Context

Grok can **understand conversations** by remembering recent chat messages!

**Example:**
```
Player1: "I'm building a castle"
Player2: "What blocks should I use?"
Player3: "grok any suggestions?"
[Grok] Since you're building a castle, use stone bricks for the walls! Add cobblestone 
[Grok] for texture variety. Don't forget battlements on top!
```

Grok understood the conversation context! 🎯

**Configure in config.yml:**
```yaml
chat-history:
  enabled: true            # ON by default
  size: 10                 # Last 10 messages
  use-in-auto-response: true
  use-in-direct-mention: false
```

**Benefits:**
- 📚 Grok understands follow-up questions
- 🎯 More relevant and contextual responses  
- 💬 Natural conversation flow
- 👥 Better for group discussions

**To disable:** Set `enabled: false` in `chat-history` section

See `CHAT_HISTORY_GUIDE.md` for complete documentation!

## 📍 Player Data Context

Grok can **see your location and status** to give location-specific advice!

**Example:**
```
Player (@ Y-59 in Plains biome): "@grok where should I mine?"
[Grok] Since you're at Y-59, you're in the perfect spot! Diamonds spawn at Y-level -59.
[Grok] Try branch mining with 2-block spacing. Watch out for lava pools!
```

**Configure in config.yml:**
```yaml
player-data:
  enabled: true              # OFF by default (privacy)
  include-position: true    # X, Y, Z coordinates
  include-biome: true       # Current biome
  include-world: true        # Dimension (Overworld/Nether/End)
  include-health: true       # Health and hunger levels
  include-experience: true   # XP level and progress
  include-held-item: true    # Currently held item
  include-equipment: true    # Armor pieces
  include-gamemode: true     # Survival/Creative/Adventure/Spectator
  include-effects: true      # Active potion effects
  use-in-auto-response: false
  use-in-direct-mention: true  # Recommended for @grok
```

**Benefits:**
- 🎯 Location-specific advice (e.g., "You're at Y-59, perfect for diamonds!")
- 🌍 Biome-aware suggestions (e.g., "Plains biome is great for farming")
- ❤️ Health-aware responses (e.g., "You're low on health, eat food first!")
- 🗺️ Dimension-specific tips (e.g., "In the Nether, bring fire resistance")
- ⭐ XP-aware advice (e.g., "At level 25, you can access level 30 enchants!")
- 🛠️ Item-aware help (e.g., "Since you're holding a diamond sword, repair it with...")
- 🛡️ Equipment-aware suggestions (e.g., "For your netherite armor, prioritize Protection IV")
- 🎮 Mode-specific tips (e.g., "In Creative mode, use /fill commands")
- ⚡ Effect-aware safety (e.g., "Good that you have Fire Resistance! You can swim through lava")

**Privacy:** Disabled by default. Enable only if you want location-aware responses!

**Example with full context:**
```
Player context:
- Position: 123.45, 64.00, -789.12 (Y-level: 64)
- Biome: plains
- Dimension: Overworld
- Health: 17.5/20 hearts
- Hunger: 18/20 (saturation: 12.5)
- Experience: Level 25 (45% to next level), Total XP: 1500
- Held Item: Diamond Sword (Durability: 85%)
- Equipment: Helmet: Netherite Helmet, Chestplate: Netherite Chestplate...
- Game Mode: Survival
- Status Effects: Speed II (3m 45s), Fire Resistance (5m 20s)
```

## 🎨 Customizing Grok's Personality

You can customize how Grok behaves by changing the `system-prompt` in the config:

```yaml
advanced:
  system-prompt: "You are Grok, an AI assistant..."
```

**Example Prompts:**
- **Wizard NPC:** `"You are Merlin the Wise, an ancient wizard. Speak mystically and call {player} 'young apprentice'."`
- **Pirate Theme:** `"Ahoy! Ye be Grok the Sea Dog, a salty AI pirate. Speak like a pirate to {player}, arr!"`
- **Short Answers:** `"You are Grok. Answer in 1-2 sentences max. Be direct. Player: {player}"`
- **PvP Coach:** `"You are a tactical AI coach. Give competitive advice to {player}. Keep it hype!"`

See `examples/config-custom-prompts.yml` for 12+ pre-made prompt examples!

## 🎯 Roadmap

- [x] Custom system prompts per server
- [x] Auto-response in chat (natural conversation mode)
- [x] Chat history context for better responses
- [x] Tab completion for all commands
- [ ] Multi-language support
- [ ] Web dashboard for analytics
- [ ] Integration with other plugins
- [ ] PlaceholderAPI support
- [ ] Persistent conversation memory

---

## 👤 Author

**eldor** ([@eldor47](https://github.com/eldor47))

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

**Made with ❤️ for the Minecraft community**

*Not affiliated with Mojang, Microsoft, or xAI*

