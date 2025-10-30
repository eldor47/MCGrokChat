# GrokChat Setup Guide

This guide will walk you through setting up GrokChat on your Minecraft server step by step.

## Prerequisites

Before you begin, make sure you have:

1. ✅ A Minecraft server running Spigot or Paper 1.20+
2. ✅ Java 17 or higher installed
3. ✅ Maven installed (for building from source)
4. ✅ An xAI API key (free to create at [console.x.ai](https://console.x.ai/))

## Step 1: Get Your xAI API Key

1. Go to [https://console.x.ai/](https://console.x.ai/)
2. Sign in with your X (Twitter) account
3. Navigate to API Keys section
4. Click "Create API Key"
5. Give it a name (e.g., "Minecraft Server")
6. Copy the generated API key (keep it secret!)

**Important:** Your API key is sensitive. Don't share it or commit it to version control!

## Step 2: Build the Plugin

### Option A: Building from Source (Recommended for Development)

```bash
# Clone or navigate to the GrokChat directory
cd GrokChat

# Build with Maven
mvn clean package

# The compiled jar will be in target/GrokChat-1.0.0.jar
```

### Option B: Download Pre-built Release

Download the latest `GrokChat-1.0.0.jar` from the releases page.

## Step 3: Install the Plugin

1. Copy `GrokChat-1.0.0.jar` to your server's `plugins` folder
   ```bash
   cp target/GrokChat-1.0.0.jar /path/to/your/server/plugins/
   ```

2. Start your server
   ```bash
   cd /path/to/your/server
   java -jar spigot-1.20.1.jar
   ```

3. The plugin will generate default config files

4. Stop your server (type `stop` in console)

## Step 4: Configure the Plugin

1. Open `plugins/GrokChat/config.yml` in a text editor

2. Replace the API key:
   ```yaml
   api:
     key: "your-actual-xai-api-key-here"  # <- Change this!
   ```

3. Adjust other settings as needed (optional):
   ```yaml
   chat:
     mention-prefix: "@grok"  # Change to !grok, /grok, etc.
     cooldown: 5  # Seconds between requests
   
   rate-limit:
     max-requests-per-hour: 20  # Per player
   
   response:
     color: "AQUA"  # YELLOW, GREEN, BLUE, etc.
     prefix: "&b[Grok]&r "  # Customize the prefix
   ```

4. Save the file

## Step 5: Start Your Server

1. Start your server again
   ```bash
   java -jar spigot-1.20.1.jar
   ```

2. Watch for GrokChat loading in the console:
   ```
   [GrokChat] GrokChat has been enabled!
   [GrokChat] Mention prefix: @grok
   ```

3. If you see warnings about the API key, go back to Step 4

## Step 6: Test the Plugin

### Test 1: Verify Installation

In-game, run:
```
/grokchat info
```

You should see plugin information with "API Status: ✓ Configured"

### Test 2: Test API Connection

As an admin, run:
```
/grokchat test
```

You should see:
```
✓ API connection successful!
Response: OK
```

### Test 3: Try a Question

In chat, type:
```
@grok Hello!
```

Or use the command:
```
/grok What is Minecraft?
```

Grok should respond!

## Step 7: Set Permissions (Optional)

If you use a permissions plugin (LuckPerms, PermissionsEx, etc.):

### Allow everyone to use Grok:
```
lp group default permission set grokchat.use true
lp group default permission set grokchat.command true
```

### Restrict to VIP players only:
```
lp group default permission set grokchat.use false
lp group default permission set grokchat.command false
lp group vip permission set grokchat.use true
lp group vip permission set grokchat.command true
```

### Admin permissions:
```
lp group admin permission set grokchat.admin true
```

## Common Setup Issues

### Issue: "API key not configured" warning

**Solution:** 
1. Check `plugins/GrokChat/config.yml`
2. Make sure you replaced `your-xai-api-key-here` with your actual key
3. Run `/grokchat reload`

### Issue: "API connection failed"

**Solutions:**
1. Verify your API key at [console.x.ai](https://console.x.ai/)
2. Check your server has internet access (try `ping api.x.ai`)
3. Check firewall isn't blocking outbound HTTPS
4. Enable debug mode in config: `advanced.debug: true`
5. Check server logs for detailed error messages

### Issue: Plugin won't load

**Solutions:**
1. Check you're using Java 17+: `java -version`
2. Verify Spigot/Paper version: must be 1.20+ (earlier versions may work)
3. Check for errors in `logs/latest.log`
4. Make sure the jar is in the `plugins` folder, not a subfolder

### Issue: Responses are empty or cut off

**Solutions:**
1. Increase `response.max-length` in config
2. Increase `advanced.max-tokens` 
3. Check your API key has available credits

### Issue: "Rate limit exceeded" messages

**Solutions:**
1. Increase `rate-limit.max-requests-per-hour`
2. Or disable rate limiting: `rate-limit.enabled: false`
3. Check your xAI account hasn't hit API limits

## Performance Tuning

### For Small Servers (1-20 players):
```yaml
chat:
  cooldown: 5
rate-limit:
  max-requests-per-hour: 20
advanced:
  max-tokens: 500
```

### For Large Servers (50+ players):
```yaml
chat:
  cooldown: 10
rate-limit:
  max-requests-per-hour: 10
advanced:
  max-tokens: 300
```

### To Minimize API Costs:
```yaml
chat:
  cooldown: 30  # 30 second cooldown
  max-message-length: 200  # Shorter questions
rate-limit:
  max-requests-per-hour: 5  # Only 5 per hour
advanced:
  max-tokens: 200  # Shorter responses
```

## Advanced Configuration

### Custom System Prompt

Edit the system message in `GrokApiClient.java`:

```java
systemMsg.addProperty("content", 
    "You are Grok, a helpful AI assistant for a Minecraft survival server. " +
    "Give short, helpful answers. Be friendly and use Minecraft terms.");
```

### Change Response Format

In `config.yml`:

```yaml
response:
  prefix: "&6[&bGrok AI&6]&r "  # Gold brackets, blue text
  color: "YELLOW"
  max-length: 512  # Longer responses
```

### Integrate with Discord

You can use plugins like DiscordSRV to bridge Grok responses to Discord!

## Next Steps

- Read the [full README](README.md) for all features
- Check out [example configurations](examples/)
- Join our [Discord community](#) for support
- Consider setting up [monitoring and analytics](#)

## Need Help?

- **GitHub Issues**: [Report bugs or request features](https://github.com/yourusername/grokchat/issues)
- **Discord**: [Join our support server](#)
- **Wiki**: [More detailed guides](https://github.com/yourusername/grokchat/wiki)

---

**Happy chatting with Grok! 🤖⛏️**

