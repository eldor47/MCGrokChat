# GrokChat Quick Start

Get GrokChat running on your server in 5 minutes!

## 🚀 Fast Setup

### 1. Get API Key (2 min)
- Go to [console.x.ai](https://console.x.ai/)
- Sign in with X/Twitter
- Create API key
- Copy it!

### 2. Install Plugin (1 min)
```bash
# Build the plugin
mvn clean package

# Copy to server
cp target/GrokChat-1.0.0.jar /path/to/server/plugins/

# Start server
cd /path/to/server
java -jar spigot.jar
```

### 3. Configure (1 min)
```bash
# Stop server
stop

# Edit config
nano plugins/GrokChat/config.yml
# Replace: key: "your-xai-api-key-here"
# With: key: "xai-YOUR-ACTUAL-KEY"

# Start server again
java -jar spigot.jar
```

### 4. Test (1 min)
In-game:
```
/grokchat test
```

Should see: `✓ API connection successful!`

### 5. Use It!
```
@grok Hello!
/grok What's the best way to mine diamonds?
```

## 🎮 Basic Usage

| Action | Command | Example |
|--------|---------|---------|
| Ask in chat | `@grok <question>` | `@grok how do I make TNT?` |
| Ask via command | `/grok <question>` | `/grok tell me a joke` |
| Check status | `/grokchat info` | Shows plugin info |
| Reload config | `/grokchat reload` | After changing config |

## ⚙️ Essential Settings

### Reduce Costs
```yaml
chat:
  cooldown: 30  # Wait 30 seconds between requests
rate-limit:
  max-requests-per-hour: 5  # Only 5 requests per hour per player
advanced:
  max-tokens: 200  # Shorter responses
```

### For Testing/Development
```yaml
chat:
  cooldown: 0  # No cooldown
rate-limit:
  enabled: false  # No limits
advanced:
  debug: true  # Show detailed logs
```

### For Production
```yaml
chat:
  cooldown: 10
rate-limit:
  enabled: true
  max-requests-per-hour: 15
advanced:
  debug: false
```

## 🔒 Basic Permissions

### Everyone can use:
```
grokchat.use: true
grokchat.command: true
```

### Only admins:
```yaml
# In your permissions plugin:
grokchat.use: false
grokchat.command: false
```

Then give to specific players/groups.

## ❓ Common First-Time Issues

**"API key not configured"**
→ Edit `plugins/GrokChat/config.yml` and add your key

**"Connection failed"**
→ Check your server has internet: `ping api.x.ai`

**"Permission denied"**
→ Make sure you're OP or have `grokchat.admin` permission

**No response**
→ Check console for errors, enable debug mode

## 📊 Monitor Usage

Check a player's usage:
```
/grokchat stats PlayerName
```

Shows:
- Requests used
- Requests remaining
- Cooldown status

## 🎯 Next Steps

1. ✅ Read [README.md](README.md) for full features
2. ✅ Read [SETUP_GUIDE.md](SETUP_GUIDE.md) for detailed setup
3. ✅ Customize your config.yml
4. ✅ Set up permissions for your server
5. ✅ Tell your players how to use it!

## 💡 Pro Tips

1. **Start restrictive** - It's easier to loosen restrictions than tighten them
2. **Monitor API costs** - Check your xAI dashboard regularly
3. **Use cooldowns** - Prevents spam and reduces costs
4. **Enable rate limiting** - Protects your API budget
5. **Disable debug in production** - Keeps logs clean

## 🆘 Help

Stuck? Check:
- Console logs: `logs/latest.log`
- Plugin status: `/grokchat info`
- API connection: `/grokchat test`
- GitHub issues: [Report a bug](https://github.com/yourusername/grokchat/issues)

---

**You're all set! Have fun with Grok in Minecraft! 🎮🤖**

