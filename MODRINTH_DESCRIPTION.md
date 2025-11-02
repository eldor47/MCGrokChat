# MCGrokChat

Bring Grok AI directly into your Minecraft server chat! Players can ask questions, get help, and have natural conversations with Grok AI using simple `@grok` mentions or commands.

**[GitHub Repository](https://github.com/eldor47/MCGrokChat)** | **[Issues](https://github.com/eldor47/MCGrokChat/issues)** | **[Wiki](https://github.com/eldor47/MCGrokChat/wiki)**

## Features

- **Chat Integration** - Mention `@grok` in chat or use `/grok` command
- **Auto-Response** - Grok joins conversations naturally when keywords are mentioned (enabled by default)
- **Chat History Context** - Remembers the last 10 messages for better conversation flow
- **Player Data Context** - Optional location, biome, health, and equipment awareness for location-specific advice
- **Tab Completion** - Smart autocomplete for all commands
- **Custom System Prompts** - Change Grok's personality with configurable prompts
- **Rate Limiting & Cooldowns** - Prevent spam with configurable limits
- **Admin Tools** - Reload config, test API, view stats, and more
- **Full Permissions System** - Fine-grained control over who can use what

## Requirements

- Java 17 or higher
- Spigot/Paper/Purpur 1.21+ (or compatible Spigot-based server)
- xAI API key ([get one here](https://console.x.ai/))

## Supported Servers

- **Spigot** 1.21+
- **Paper** 1.21+ (recommended)
- **Purpur** 1.21+
- Other Spigot-based servers (Bukkit-compatible)

*Note: The plugin uses the Spigot API which includes Bukkit compatibility. It works on any server that implements the Spigot/Bukkit API (Spigot, Paper, Purpur, etc.). Pure Bukkit (original) is obsolete and doesn't support modern Minecraft versions.*

## Configuration

All settings can be configured in `plugins/MCGrokChat/config.yml`. You'll need an xAI API key to get started - [sign up at console.x.ai](https://console.x.ai/) to get yours.

**Example configuration:**

```yaml
api:
  key: "your-xai-api-key-here"
  model: "grok-3-mini"

chat:
  enable-auto-response: true
  auto-response-keywords:
    - "grok"
    - "hey grok"

chat-history:
  enabled: true
  size: 10
```

## Example Usage

```
# Direct Mention (Private Response)
Player: @grok how do I find diamonds?
[Grok] The best way to find diamonds is to mine at Y-level -59. Bring iron pickaxes and watch out for lava!

# Auto-Response (Public - Everyone sees)
Player: hey grok what's the best enchantment for a sword?
[Grok] For swords, Sharpness V is essential! Also get Sweeping Edge III for crowd control. Don't forget Looting III for better drops!

# Command Usage
/grok what's the best food in minecraft?
[Grok] Golden carrots are the best food for saturation, but cooked porkchops and steak are great for hunger restoration!
```

## Documentation

- **[Full README](https://github.com/eldor47/MCGrokChat/blob/main/README.md)** - Complete documentation and examples
- **[Configuration Examples](https://github.com/eldor47/MCGrokChat/tree/main/examples)** - Pre-made config files for different setups
- **[Setup Guide](https://github.com/eldor47/MCGrokChat/blob/main/docs/guides/SETUP_GUIDE.md)** - Detailed installation instructions
- **[Feature Guides](https://github.com/eldor47/MCGrokChat/tree/main/docs/guides)** - Guides for auto-response, chat history, customization, and more

## Support

- **Report Issues:** [GitHub Issues](https://github.com/eldor47/MCGrokChat/issues)
- **Wiki:** [GitHub Wiki](https://github.com/eldor47/MCGrokChat/wiki)
- **Source Code:** [GitHub Repository](https://github.com/eldor47/MCGrokChat)

## API Costs

This plugin uses the xAI API. Check [xAI's pricing page](https://x.ai/api) for current rates. The plugin uses `grok-3-mini` by default, which is optimized for cost efficiency.

## License

This project is licensed under the MIT License. See the [LICENSE file](https://github.com/eldor47/MCGrokChat/blob/main/LICENSE) for details.

---

**Made with love for the Minecraft community**

*Not affiliated with Mojang, Microsoft, or xAI*

