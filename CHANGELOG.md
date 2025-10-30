# Changelog

All notable changes to MCGrokChat will be documented in this file.

## [1.0.0] - 2025-10-30

### Added
- ✨ **Configurable System Prompts** - Customize Grok's personality and behavior
  - Added `advanced.system-prompt` configuration option
  - Supports `{player}` placeholder for personalization
  - See `examples/config-custom-prompts.yml` for 12 pre-made examples
- 🤖 **Auto-Response Feature** - Grok joins conversations naturally (ON by default!)
  - Responds when keywords are mentioned in chat (e.g., "hey grok")
  - Public responses broadcast to all players (feels like Grok is part of the community)
  - Direct @mentions remain private
  - Configurable keywords and minimum word count to prevent spam
  - `enable-auto-response` configuration option (default: true)
  - `auto-response-keywords` list for trigger words
  - `auto-response-min-words` to prevent accidental triggers
  - See `examples/config-auto-response.yml` for detailed examples
- ⌨️ **Tab Completion (Type-Ahead)** - Smart command suggestions
  - `/grok` command with intelligent question starters and Minecraft terms
  - `/grokchat` admin command with subcommand suggestions
  - Player name completion for `/grokchat stats` command
  - Permission-aware (only shows available commands)
  - Case-insensitive matching
  - See `TAB_COMPLETION_GUIDE.md` for complete documentation
- 🧠 **Chat History Context** - Grok understands conversations (ON by default!)
  - Captures last 10 chat messages as context
  - Enables follow-up questions and contextual responses
  - Configurable history size (5-15 messages recommended)
  - Can enable/disable per response mode (auto-response vs direct mention)
  - Thread-safe implementation with automatic cleanup
  - `chat-history.enabled`, `chat-history.size` configuration options
  - `use-in-auto-response` and `use-in-direct-mention` toggles
  - See `CHAT_HISTORY_GUIDE.md` for complete documentation
- 📍 **Player Data Context** - Optional location and status awareness (OFF by default)
  - Provides player position (X, Y, Z coordinates and Y-level)
  - Includes biome information (e.g., Plains, Desert, Ocean)
  - Shows world/dimension (Overworld, Nether, End)
  - Displays health and hunger status
  - **Experience level and progress** - XP-aware enchantment advice
  - **Currently held item** - Context-aware repair/crafting suggestions
  - **Equipment/armor** - Tailored enchantment and upgrade advice
  - **Game mode** - Mode-specific tips (Survival vs Creative)
  - **Status effects** - Effects-aware safety advice (potions, fire, etc.)
  - Configurable per data type (position, biome, world, health, experience, held-item, equipment, gamemode, effects)
  - Can enable/disable per response mode
  - Privacy-conscious (disabled by default)
  - Enables location-specific, biome-aware, health-conscious, XP-aware, item-aware, equipment-aware, mode-aware, and effect-aware responses
  - `player-data.enabled` to enable/disable feature
  - `include-position`, `include-biome`, `include-world`, `include-health`, `include-experience`, `include-held-item`, `include-equipment`, `include-gamemode`, `include-effects` options
  - `use-in-auto-response` and `use-in-direct-mention` toggles
- Initial plugin release
- `/grok` command for direct AI queries
- `@grok` chat mention support
- Rate limiting system (per-player hourly limits)
- Cooldown system to prevent spam
- Admin commands (`/grokchat`)
  - `reload` - Reload configuration
  - `info` - Show plugin information
  - `setkey` - Set API key at runtime
  - `test` - Test API connection
  - `stats` - View player usage statistics
- Comprehensive configuration options
- Permission system (grokchat.use, grokchat.command, grokchat.admin)
- Asynchronous API request handling
- Message splitting for long responses
- Color code support in responses
- Debug mode for troubleshooting
- Privacy controls (query logging, context sharing)
- Support for Minecraft 1.21+

### Technical Details
- Built with Java 17
- Uses Spigot/Paper API 1.21.1
- Shaded Gson dependency to avoid conflicts
- Thread pool executor for concurrent requests (4 threads)
- Proper resource cleanup on plugin disable
- Comprehensive error handling

### Configuration Options
- API settings (key, endpoint, model, timeout)
- Chat settings (mentions, prefix, command, length, cooldown)
- Response settings (max length, color, prefix, thinking message)
- Rate limiting (enabled, max requests per hour)
- Privacy settings (log queries, share context)
- Advanced settings (temperature, max tokens, system prompt, debug)

### Examples Included
- `config-minimal.yml` - Bare minimum configuration
- `config-development.yml` - Development/testing setup
- `config-premium.yml` - Premium server with generous limits
- `config-cost-optimized.yml` - Budget-friendly settings
- `config-custom-prompts.yml` - 12+ personality examples

### Documentation
- Comprehensive README with installation guide
- Detailed SETUP_GUIDE with step-by-step instructions
- QUICK_START guide for rapid deployment
- CODE_ANALYSIS_REPORT with technical details
- Troubleshooting section for common issues

---

## Roadmap for Future Versions

### Planned Features
- [ ] Conversation history/context support
- [ ] Multi-language support
- [ ] Web dashboard for analytics
- [ ] Integration with other popular plugins
- [ ] PlaceholderAPI support
- [ ] Custom responses per server region
- [ ] A/B testing for different prompts
- [ ] Advanced rate limiting (per-rank, per-time-of-day)
- [ ] Response caching for common questions
- [ ] Custom command aliases
- [ ] Webhook support for external integrations

### Under Consideration
- [ ] Voice-to-text integration
- [ ] Image/screenshot analysis (when API supports it)
- [ ] Auto-moderation features
- [ ] Player reputation system
- [ ] Achievement/milestone tracking
- [ ] Custom training on server-specific data

---

## Version History

**[1.0.0]** - Initial Release (2025-10-30)
- Full-featured Grok AI integration for Minecraft
- Configurable system prompts
- Comprehensive admin tools
- Production-ready with all safety features

---

## Support

Having issues or suggestions? 
- Check the [Troubleshooting Guide](README.md#troubleshooting)
- Report bugs on [GitHub Issues](https://github.com/yourusername/grokchat/issues)
- Join our [Discord Server](#) for community support

---

**Note:** This plugin requires an xAI API key. Get yours at [console.x.ai](https://console.x.ai/)

