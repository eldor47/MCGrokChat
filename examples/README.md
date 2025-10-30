# GrokChat Configuration Examples

This directory contains example configurations for different use cases.

## Available Examples

### 1. Minimal Configuration (`config-minimal.yml`)
**Use case:** Basic setup with default values
- Standard rate limits
- Default cooldowns
- Good for getting started

### 2. Cost-Optimized Configuration (`config-cost-optimized.yml`)
**Use case:** Minimize API costs
- Low rate limits (5/hour)
- Long cooldowns (30 seconds)
- Shorter responses (200 tokens)
- Less creative responses
- **Best for:** Servers on a budget

### 3. Premium Configuration (`config-premium.yml`)
**Use case:** Maximum quality and features
- High rate limits (50/hour)
- Short cooldowns (3 seconds)
- Longer responses (1000 tokens)
- More creative responses
- **Best for:** Premium/VIP servers

### 4. Development Configuration (`config-development.yml`)
**Use case:** Testing and development
- No rate limits
- No cooldowns
- Full debug logging
- **Best for:** Developers and testing environments

## How to Use These Examples

1. Choose the example that fits your needs
2. Copy it to `plugins/GrokChat/config.yml`
3. Replace `your-xai-api-key-here` with your actual API key
4. Adjust any values as needed
5. Run `/grokchat reload` in-game

## Example Usage Comparison

| Feature | Minimal | Cost-Optimized | Premium | Development |
|---------|---------|----------------|---------|-------------|
| Requests/hour | 20 | 5 | 50 | Unlimited |
| Cooldown | 5s | 30s | 3s | 0s |
| Max tokens | 500 | 200 | 1000 | 500 |
| Message length | 500 | 200 | 1000 | 1000 |
| Rate limiting | Yes | Yes | Yes | No |
| Debug logging | No | No | No | Yes |
| Est. cost/100 req | Medium | Low | High | Medium |

## Customizing Your Config

Feel free to mix and match settings from different examples! For instance:

**Balanced Configuration:**
- Rate limit: 15/hour (between minimal and cost-optimized)
- Cooldown: 10s (balanced)
- Max tokens: 400 (reasonable)
- Temperature: 0.7 (standard)

**VIP-Only Configuration:**
- Rate limit: 30/hour
- Cooldown: 5s
- Max tokens: 600
- Only give `grokchat.use` permission to VIP group

## Cost Estimation

Based on xAI's pricing (check current rates at [x.ai/api](https://x.ai/api)):

### Cost-Optimized Config
- ~200 tokens per request
- If 50 players × 5 requests = 250 requests/day
- Estimated: ~$X.XX per day

### Premium Config
- ~1000 tokens per request
- If 50 players × 50 requests = 2,500 requests/day
- Estimated: ~$XX.XX per day

**Note:** These are rough estimates. Monitor your actual usage in the xAI console.

## Tips for Each Configuration

### Minimal
✅ Good starting point
✅ Balanced costs and features
⚠️ May need adjustment based on server size

### Cost-Optimized
✅ Predictable costs
✅ Prevents abuse
⚠️ Players may find limits restrictive
💡 Tip: Consider VIP perks with higher limits

### Premium
✅ Best user experience
✅ Minimal restrictions
⚠️ Can get expensive quickly
💡 Tip: Monitor costs daily at first

### Development
✅ Easy testing
✅ Full visibility with debug mode
⚠️ Don't use in production!
💡 Tip: Switch to minimal/premium before going live

## Need Help?

- Not sure which to use? Start with **Minimal**
- Concerned about costs? Use **Cost-Optimized**
- Want the best experience? Try **Premium**
- Testing features? Use **Development**

See [SETUP_GUIDE.md](../SETUP_GUIDE.md) for detailed configuration info.

