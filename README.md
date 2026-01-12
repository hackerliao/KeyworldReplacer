# KeyworldReplacer
A KeywordReplacer plugin for Spigot Minecraft Servers

This plugin is developed in Simplified Chinsese as it is the native language for me

If anyone would like to translate, feel free to contact me(Im just too lazy to do it myself qaq)

Thanks for Help from Deepseek for developing the code(This readme is also from deepseek)

Works perfectly on my 1.21.1 Spigot Server
# KeywordReplacer - Minecraft聊天关键词替换插件

一个基于LuckPerms权限系统的Minecraft聊天关键词替换插件，可以根据玩家组或特定玩家自动替换聊天消息中的关键词。

## ✨ 功能特性

- ✅ **智能替换**：根据预定义规则自动替换聊天消息中的关键词
- ✅ **权限组支持**：基于LuckPerms权限组系统，可为不同组设置不同规则
- ✅ **灵活配置**：支持玩家名、玩家组、大小写敏感、全词匹配等多种配置
- ✅ **实时管理**：游戏内命令实时添加、删除、启用/禁用规则
- ✅ **高性能**：异步处理聊天事件，不影响服务器性能
- ✅ **详细日志**：可选的替换日志记录，便于调试和管理

## 📦 安装要求

### 服务器要求
- **Minecraft服务器**: Spigot/Paper 1.13+
- **Java版本**: JDK 8 或 11
- **必需插件**: [LuckPerms](https://luckperms.net/)

### 安装步骤
1. 下载最新版本的 `KeywordReplacer.jar`
2. 将文件放入服务器的 `plugins` 文件夹
3. 重启服务器或使用 `/reload confirm`
4. 确保LuckPerms插件已安装并启用

## ⚙️ 配置说明

### 默认配置文件
插件首次运行会在 `plugins/KeywordReplacer/config.yml` 生成配置文件：

```yaml
rules:
  主人规则:
    trigger: "hacker_liao"
    replace: "主人sama~"
    groups: ["all"]
    players: []
    enabled: true
    case_sensitive: false
    whole_word: false
  
  all组测试规则:
    trigger: "测试"
    replace: "已替换"
    groups: ["all"]
    players: []
    enabled: true
    case_sensitive: false
    whole_word: true
```

### 规则配置参数
| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `trigger` | 字符串 | 必填 | 需要替换的关键词 |
| `replace` | 字符串 | 必填 | 替换后的词语 |
| `groups` | 字符串列表 | `[]` | 适用玩家组（空表示所有组） |
| `players` | 字符串列表 | `[]` | 特定玩家名（空表示所有玩家） |
| `enabled` | 布尔值 | `true` | 是否启用此规则 |
| `case_sensitive` | 布尔值 | `false` | 是否区分大小写 |
| `whole_word` | 布尔值 | `false` | 是否全词匹配 |

## 🎮 游戏内命令

### 命令格式
```
/kr <子命令> [参数]
/KeywordReplacer <子命令> [参数]
```

### 可用命令
| 命令 | 权限节点 | 描述 | 示例 |
|------|----------|------|------|
| `/kr help` | 无 | 显示帮助信息 | `/kr help` |
| `/kr reload` | `keywordreplacer.reload` | 重载配置文件 | `/kr reload` |
| `/kr list` | `keywordreplacer.list` | 列出所有规则 | `/kr list` |
| `/kr add` | `keywordreplacer.add` | 添加新规则 | `/kr add 规则ID 触发词 替换词 -g 组名 -p 玩家名` |
| `/kr remove` | `keywordreplacer.remove` | 删除规则 | `/kr remove 规则ID` |
| `/kr toggle` | `keywordreplacer.toggle` | 启用/禁用规则 | `/kr toggle 规则ID enable` |

### 添加规则示例
```bash
# 为fuxiaogirl组添加规则
/kr add 规则1 hacker_liao 主人 -g default

# 为特定玩家添加规则
/kr add orca规则 hacker_liao 主人 -p orca

# 多个组和玩家
/kr add 通用规则 测试 已测试 -g all,vip -p player1,player2

# 区分大小写和全词匹配
/kr add 英文规则 Hello 你好 -cs -ww
```

## 🔧 权限节点

| 权限节点 | 默认 | 描述 |
|----------|------|------|
| `keywordreplacer.*` | op | 所有权限 |
| `keywordreplacer.reload` | op | 重载配置权限 |
| `keywordreplacer.list` | op | 查看规则列表权限 |
| `keywordreplacer.add` | op | 添加规则权限 |
| `keywordreplacer.remove` | op | 删除规则权限 |
| `keywordreplacer.toggle` | op | 启用/禁用规则权限 |

## 🛠️ 开发者指南

### 项目结构
```
src/main/java/com/hacker_liao/keywordreplacer/
├── KeywordReplacerPlugin.java    # 主插件类
├── ConfigManager.java            # 配置管理类
├── ReplacementRule.java          # 规则数据类
├── ChatListener.java             # 聊天事件监听器
├── CommandHandler.java           # 命令处理器
└── TabCompletion.java            # 命令补全器
```

### 构建插件
1. 克隆或下载项目源码
2. 确保已安装 Maven 和 JDK 8+
3. 在项目根目录执行：
   ```bash
   mvn clean package
   ```
4. 生成的JAR文件在 `target/KeywordReplacer-1.0.0.jar`

### 依赖管理
```xml
<!-- Spigot API -->
<dependency>
    <groupId>org.spigotmc</groupId>
    <artifactId>spigot-api</artifactId>
    <version>1.19.4-R0.1-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>

<!-- LuckPerms API -->
<dependency>
    <groupId>net.luckperms</groupId>
    <artifactId>api</artifactId>
    <version>5.4</version>
    <scope>provided</scope>
</dependency>
```


## 🔍 调试和故障排除

### 常见问题
1. **插件不工作**
   - 检查LuckPerms是否安装并启用
   - 查看服务器控制台是否有错误信息
   - 确认配置文件格式正确

2. **规则不生效**
   - 使用 `/kr list` 确认规则已加载
   - 检查玩家是否在指定的组中
   - 确认规则已启用 (`enabled: true`)

3. **命令不可用**
   - 确认插件已正确加载
   - 检查是否有相应权限
   - 尝试使用完整命令 `/keywordreplacer help`

### 查看调试信息
在服务器控制台中查看插件日志：
```
[KeywordReplacer] 关键词替换插件已启用！作者：hacker_liao
[KeywordReplacer] 已加载 X 条替换规则
```

## 🤝 贡献指南

欢迎提交Issue和Pull Request来改进这个插件！


## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE)] 文件了解详情

## 👤 作者

**hacker_liao**

- 项目主页: [[KeywordReplacer](https://github.com/yourusername/KeywordReplacer)]
- 问题反馈: [[Issues](https://github.com/yourusername/KeywordReplacer/issues)]

## 🙏 致谢

- 感谢 [SpigotMC](https://www.spigotmc.org/) 社区
- 感谢 [LuckPerms](https://luckperms.net/) 团队提供的优秀API
- 感谢所有测试者和贡献者

---

**✨ 让Minecraft聊天更有趣！**
