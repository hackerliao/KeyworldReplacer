/*
 * KeywordReplacer - 命令补全器
 * 作者：hacker_liao
 * 包名：com.hacker_liao.keywordreplacer
 */

package com.hacker_liao.keywordreplacer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String[] subCommands = {"help", "reload", "list", "add", "remove", "toggle"};
            for (String sub : subCommands) {
                if (sub.startsWith(args[0].toLowerCase())) {
                    completions.add(sub);
                }
            }
        }

        // 为toggle命令提供规则ID补全
        if (args.length == 2 && args[0].equalsIgnoreCase("toggle")) {
            ConfigManager configManager = KeywordReplacerPlugin.getInstance().getConfigManager();
            for (ReplacementRule rule : configManager.getRules()) {
                if (rule.getId().toLowerCase().startsWith(args[1].toLowerCase())) {
                    completions.add(rule.getId());
                }
            }
        }

        // 为remove命令提供规则ID补全
        if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            ConfigManager configManager = KeywordReplacerPlugin.getInstance().getConfigManager();
            for (ReplacementRule rule : configManager.getRules()) {
                if (rule.getId().toLowerCase().startsWith(args[1].toLowerCase())) {
                    completions.add(rule.getId());
                }
            }
        }

        return completions.isEmpty() ? null : completions;
    }
}