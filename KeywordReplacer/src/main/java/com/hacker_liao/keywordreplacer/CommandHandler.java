/*
 * KeywordReplacer - 命令处理器
 * 作者：hacker_liao
 * 包名：com.hacker_liao.keywordreplacer
 */

package com.hacker_liao.keywordreplacer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandHandler implements CommandExecutor {

    private final KeywordReplacerPlugin plugin;
    private final ConfigManager configManager;

    public CommandHandler(KeywordReplacerPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                return handleReload(sender);

            case "list":
                return handleList(sender);

            case "add":
                return handleAdd(sender, args);

            case "remove":
                return handleRemove(sender, args);

            case "toggle":
                return handleToggle(sender, args);

            case "help":
            default:
                sendHelp(sender);
                return true;
        }
    }

    private boolean handleReload(CommandSender sender) {
        if (!sender.hasPermission("keywordreplacer.reload")) {
            sender.sendMessage("§c你没有权限执行此命令！");
            return true;
        }

        configManager.reload();
        sender.sendMessage("§a配置已重载！");
        return true;
    }

    private boolean handleList(CommandSender sender) {
        if (!sender.hasPermission("keywordreplacer.list")) {
            sender.sendMessage("§c你没有权限执行此命令！");
            return true;
        }

        List<ReplacementRule> rules = configManager.getRules();

        if (rules.isEmpty()) {
            sender.sendMessage("§6=== 当前替换规则 ===");
            sender.sendMessage("§e暂无任何规则。使用 /kr add 添加规则");
            return true;
        }

        sender.sendMessage("§6=== 当前替换规则 (共 " + rules.size() + " 条) ===");
        for (ReplacementRule rule : rules) {
            String status = rule.isEnabled() ? "§a✓ 启用" : "§c✗ 禁用";
            String groups = rule.getGroups().isEmpty() ? "无" : String.join(", ", rule.getGroups());
            String players = rule.getPlayers().isEmpty() ? "无" : String.join(", ", rule.getPlayers());

            sender.sendMessage(String.format("§e[%s] §f%s → %s %s",
                    rule.getId(), rule.getTrigger(), rule.getReplace(), status));
            sender.sendMessage("  §7组: §f" + groups);
            sender.sendMessage("  §7玩家: §f" + players);
        }
        return true;
    }

    private boolean handleAdd(CommandSender sender, String[] args) {
        if (!sender.hasPermission("keywordreplacer.add")) {
            sender.sendMessage("§c你没有权限执行此命令！");
            return true;
        }

        if (args.length < 4) {
            sender.sendMessage("§c用法: /kr add <规则ID> <触发词> <替换词>");
            sender.sendMessage("§7可选参数:");
            sender.sendMessage("§7  -g <组1,组2,...>  指定玩家组");
            sender.sendMessage("§7  -p <玩家1,玩家2,...> 指定玩家");
            sender.sendMessage("§7  -cs  区分大小写");
            sender.sendMessage("§7  -ww  全词匹配");
            sender.sendMessage("§e示例: /kr add 拂晓规则 拂晓 主人 -g fuxiaogirl,vip -p orca");
            return true;
        }

        String ruleId = args[1];
        String trigger = args[2];
        String replace = args[3];

        // 检查规则ID是否已存在
        if (configManager.getRule(ruleId) != null) {
            sender.sendMessage("§c错误：规则ID '" + ruleId + "' 已存在！");
            return true;
        }

        List<String> groups = new ArrayList<>();
        List<String> players = new ArrayList<>();
        boolean caseSensitive = false;
        boolean wholeWord = false;

        // 解析可选参数
        for (int i = 4; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-g") && i + 1 < args.length) {
                groups = Arrays.asList(args[i + 1].split(","));
                i++; // 跳过下一个参数
            } else if (args[i].equalsIgnoreCase("-p") && i + 1 < args.length) {
                players = Arrays.asList(args[i + 1].split(","));
                i++; // 跳过下一个参数
            } else if (args[i].equalsIgnoreCase("-cs")) {
                caseSensitive = true;
            } else if (args[i].equalsIgnoreCase("-ww")) {
                wholeWord = true;
            }
        }

        // 创建新规则
        ReplacementRule newRule = new ReplacementRule(
                ruleId,
                trigger,
                replace,
                groups,
                players,
                true, // 默认启用
                caseSensitive,
                wholeWord
        );

        // 添加到配置
        configManager.addRule(newRule);

        sender.sendMessage("§a成功添加规则: §e" + ruleId);
        sender.sendMessage("§7触发词: §f" + trigger);
        sender.sendMessage("§7替换词: §f" + replace);
        if (!groups.isEmpty()) {
            sender.sendMessage("§7适用组: §f" + String.join(", ", groups));
        }
        if (!players.isEmpty()) {
            sender.sendMessage("§7适用玩家: §f" + String.join(", ", players));
        }
        if (caseSensitive) {
            sender.sendMessage("§7区分大小写: §a是");
        }
        if (wholeWord) {
            sender.sendMessage("§7全词匹配: §a是");
        }

        return true;
    }

    private boolean handleRemove(CommandSender sender, String[] args) {
        if (!sender.hasPermission("keywordreplacer.remove")) {
            sender.sendMessage("§c你没有权限执行此命令！");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("§c用法: /kr remove <规则ID>");
            return true;
        }

        String ruleId = args[1];

        if (configManager.getRule(ruleId) == null) {
            sender.sendMessage("§c错误：找不到规则 '" + ruleId + "'");
            return true;
        }

        configManager.removeRule(ruleId);
        sender.sendMessage("§a成功删除规则: §e" + ruleId);
        return true;
    }

    private boolean handleToggle(CommandSender sender, String[] args) {
        if (!sender.hasPermission("keywordreplacer.toggle")) {
            sender.sendMessage("§c你没有权限执行此命令！");
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage("§c用法: /kr toggle <规则ID> <enable|disable>");
            return true;
        }

        String ruleId = args[1];
        String action = args[2].toLowerCase();

        ReplacementRule rule = configManager.getRule(ruleId);
        if (rule == null) {
            sender.sendMessage("§c错误：找不到规则 '" + ruleId + "'");
            return true;
        }

        if (action.equals("enable")) {
            rule.setEnabled(true);
            sender.sendMessage("§a已启用规则: §e" + ruleId);
        } else if (action.equals("disable")) {
            rule.setEnabled(false);
            sender.sendMessage("§c已禁用规则: §e" + ruleId);
        } else {
            sender.sendMessage("§c错误：参数必须是 'enable' 或 'disable'");
            return true;
        }

        configManager.saveConfig();
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6=== 关键词替换插件帮助 ===");
        sender.sendMessage("§e/kr help §7- 显示帮助信息");
        sender.sendMessage("§e/kr reload §7- 重载配置文件");
        sender.sendMessage("§e/kr list §7- 列出所有替换规则");
        sender.sendMessage("§e/kr add <ID> <触发词> <替换词> [参数] §7- 添加新规则");
        sender.sendMessage("§e/kr remove <ID> §7- 删除规则");
        sender.sendMessage("§e/kr toggle <ID> <enable|disable> §7- 启用/禁用规则");
        sender.sendMessage("§7");
        sender.sendMessage("§7添加规则参数：");
        sender.sendMessage("§7  -g <组1,组2>  指定玩家组");
        sender.sendMessage("§7  -p <玩家1,玩家2> 指定玩家");
        sender.sendMessage("§7  -cs  区分大小写");
        sender.sendMessage("§7  -ww  全词匹配");
        sender.sendMessage("§7");
        sender.sendMessage("§7也可以直接编辑 plugins/KeywordReplacer/config.yml");
    }
}