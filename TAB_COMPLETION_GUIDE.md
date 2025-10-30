# Tab Completion Guide

GrokChat includes intelligent tab completion (type-ahead) for both player and admin commands, making it easier to discover and use features!

## 🎮 For Players: `/grok` Command

### How It Works

Press **TAB** while typing to see suggestions:

```
/grok <TAB>
→ how  what  where  when  why  can  help

/grok how <TAB>
→ do  I  make  craft  find  get  build  enchant  mine  farm

/grok how do <TAB>
→ diamonds  iron  gold  netherite  enchantment  beacon  farm  ...
```

### Example Usage

**Type:** `/grok h` + **TAB**
```
→ /grok how
```

**Type:** `/grok how ` + **TAB**
```
→ Shows: do, I, make, craft, find, get, build, enchant, mine, farm
```

**Type:** `/grok how do I ` + **TAB**
```
→ Shows common items: diamonds, iron, gold, netherite, etc.
```

### Smart Suggestions

The tab completer suggests:
1. **Question starters** - how, what, where, when, why, can, help
2. **Common verbs** - do, make, craft, find, get, build, enchant
3. **Minecraft items** - diamonds, iron, netherite, farm, redstone, etc.

---

## 🔧 For Admins: `/grokchat` Command

### Subcommand Completion

Press **TAB** to see available admin commands:

```
/grokchat <TAB>
→ reload  info  setkey  test  stats
```

### Examples

#### 1. Reload Configuration
```
/grokchat re<TAB>
→ /grokchat reload
```

#### 2. View Statistics
```
/grokchat st<TAB>
→ /grokchat stats

/grokchat stats <TAB>
→ Shows list of online players
→ Select: Steve, Alex, Notch, etc.

/grokchat stats St<TAB>
→ /grokchat stats Steve
```

#### 3. Set API Key
```
/grokchat set<TAB>
→ /grokchat setkey

/grokchat setkey <TAB>
→ /grokchat setkey <api-key>
```

#### 4. Test API Connection
```
/grokchat te<TAB>
→ /grokchat test
```

#### 5. Show Plugin Info
```
/grokchat in<TAB>
→ /grokchat info
```

---

## 📋 Complete Command Reference

### `/grok` Tab Completion Tree

```
/grok
  ├─ how
  │   ├─ do
  │   ├─ I
  │   ├─ make
  │   ├─ craft
  │   ├─ find
  │   ├─ get
  │   ├─ build
  │   ├─ enchant
  │   ├─ mine
  │   └─ farm
  │       ├─ diamonds
  │       ├─ iron
  │       ├─ gold
  │       ├─ netherite
  │       ├─ enchantment
  │       ├─ beacon
  │       ├─ farm
  │       ├─ base
  │       ├─ redstone
  │       ├─ sword
  │       ├─ pickaxe
  │       ├─ armor
  │       └─ food
  ├─ what
  ├─ where
  ├─ when
  ├─ why
  ├─ can
  └─ help
```

### `/grokchat` Tab Completion Tree

```
/grokchat
  ├─ reload
  ├─ info
  ├─ setkey
  │   └─ <api-key>
  ├─ test
  └─ stats
      └─ <player-name>
          ├─ Steve
          ├─ Alex
          └─ [all online players]
```

---

## 💡 Tips & Tricks

### 1. Partial Matching
Type part of a word and press TAB:
```
/grokchat rel<TAB> → /grokchat reload
/grok wh<TAB>      → /grok what/where/when (multiple matches)
```

### 2. Multiple TAB Presses
If there are multiple matches, press TAB again to cycle:
```
/grok wh<TAB>     → what
<TAB>             → where
<TAB>             → when
<TAB>             → what (cycles back)
```

### 3. Case-Insensitive
Works with any case:
```
/grokchat REL<TAB> → /grokchat reload
/grok HOW<TAB>     → /grok how
```

### 4. Player Name Completion
Admins can quickly select players:
```
/grokchat stats N<TAB> → /grokchat stats Notch
```

---

## 🎯 Benefits

### For New Players
- 📚 **Discovery** - Learn available commands without reading docs
- 🚀 **Speed** - Type less, complete more
- ✅ **Correctness** - No typos in commands

### For Experienced Players
- ⚡ **Efficiency** - Fast command entry
- 💭 **Memory Aid** - Reminder of exact command names
- 🎮 **Better UX** - Smooth, modern command experience

### For Server Admins
- 📊 **Quick Stats** - Tab through player names easily
- 🔧 **Fast Config** - Quick access to admin commands
- 👥 **Player Names** - No need to type full names

---

## 🛠️ Technical Details

### Implementation
- Uses Bukkit's `TabCompleter` interface
- Filters suggestions based on user input
- Case-insensitive matching
- Permission-aware (only shows commands user can access)

### Performance
- ✅ Lightweight - Suggestions generated on-demand
- ✅ Fast - Uses Java 8 streams for filtering
- ✅ Efficient - No database queries needed

### Permissions
Tab completion respects permissions:
- `/grok` requires `grokchat.command`
- `/grokchat` requires `grokchat.admin`
- Players without permission see no suggestions

---

## 📖 Examples in Action

### Example 1: New Player Asking Question
```
Player types: /grok h<TAB>
System shows: how
Player presses TAB again, types: do I<TAB>
System shows: do
Player continues: fin<TAB>
System shows: find
Player types: d<TAB>
System shows: diamonds
Final command: /grok how do I find diamonds
```

### Example 2: Admin Checking Stats
```
Admin types: /grokchat st<TAB>
System shows: stats
Admin presses SPACE then TAB
System shows: Steve, Alex, Notch, ...
Admin types: St<TAB>
System shows: Steve
Final command: /grokchat stats Steve
```

### Example 3: Quick Reload
```
Admin types: /gc re<TAB>
System shows: reload
Admin presses ENTER
Result: Configuration reloaded!
```

---

## 🔮 Future Enhancements

Potential improvements for future versions:
- [ ] Context-aware suggestions based on recent queries
- [ ] Dynamic suggestions based on server state
- [ ] Custom vocabulary from server admins
- [ ] Multi-language support
- [ ] Recent command history
- [ ] Smart completion based on chat context

---

## 🎓 Learning Resources

### For Players
1. Start typing `/grok` and press TAB to explore
2. Try common question words (how, what, where)
3. Experiment with different combinations
4. The suggestions are just helpers - you can type anything!

### For Admins
1. Type `/grokchat` + TAB to see all admin commands
2. Use `/grokchat stats` + TAB to quickly check player usage
3. Remember: aliases work too! `/gc` = `/grokchat`

---

## ❓ FAQ

**Q: Can I disable tab completion?**  
A: Not currently - it's a standard Minecraft feature that improves UX.

**Q: Why don't I see suggestions for `/grok`?**  
A: Check you have the `grokchat.command` permission.

**Q: Can I customize the suggestions?**  
A: Not in config currently - requires code modification.

**Q: Do suggestions affect what I can ask?**  
A: No! Suggestions are just helpers. You can ask anything.

**Q: Why does `/grokchat` show player names for stats?**  
A: To make it easy to select who you want to check stats for.

**Q: Can I tab-complete in chat for @grok mentions?**  
A: No - tab completion only works for commands starting with `/`.

---

## 🎉 Summary

Tab completion makes GrokChat commands:
- ✅ **Easier to discover** - See what's available
- ✅ **Faster to use** - Less typing required
- ✅ **Harder to mistype** - Auto-complete prevents errors
- ✅ **More professional** - Modern, polished UX

Just type `/grok` or `/grokchat` and press **TAB** to get started!

