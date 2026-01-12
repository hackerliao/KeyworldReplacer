/*
 * KeywordReplacer - 聊天监听器
 * 作者：hacker_liao
 * 包名：com.hacker_liao.keywordreplacer
 */

package com.hacker_liao.keywordreplacer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class ChatListener implements Listener {

    private final KeywordReplacerPlugin plugin;
    private final ConfigManager configManager;
    private final LuckPerms luckPerms;

    public ChatListener(KeywordReplacerPlugin plugin, ConfigManager configManager, LuckPerms luckPerms) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.luckPerms = luckPerms;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        String originalMessage = event.getMessage();
        String modifiedMessage = originalMessage;

        // 获取玩家的LuckPerms用户对象
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) return;

        // 获取玩家所有的组
        Set<String> playerGroups = user.getNodes().stream()
                .filter(NodeType.INHERITANCE::matches)
                .map(NodeType.INHERITANCE::cast)
                .map(InheritanceNode::getGroupName)
                .collect(Collectors.toSet());

        // 检查每条规则
        for (ReplacementRule rule : configManager.getRules()) {
            if (!rule.isEnabled()) continue;

            // 检查玩家是否符合规则条件
            if (matchesRule(player, playerGroups, rule)) {
                // 应用替换
                modifiedMessage = applyReplacement(modifiedMessage, rule);
            }
        }

        // 如果消息被修改，更新事件
        if (!originalMessage.equals(modifiedMessage)) {
            event.setMessage(modifiedMessage);
        }
    }

    private boolean matchesRule(Player player, Set<String> playerGroups, ReplacementRule rule) {
        String playerName = player.getName();

        // 检查特定玩家
        if (rule.getPlayers() != null && !rule.getPlayers().isEmpty()) {
            for (String targetPlayer : rule.getPlayers()) {
                if (targetPlayer.equalsIgnoreCase(playerName)) {
                    return true;
                }
            }
        }

        // 检查玩家组
        if (rule.getGroups() != null && !rule.getGroups().isEmpty()) {
            for (String group : rule.getGroups()) {
                // 精确匹配
                if (playerGroups.contains(group)) {
                    return true;
                }

                // 忽略大小写匹配
                for (String playerGroup : playerGroups) {
                    if (playerGroup.equalsIgnoreCase(group)) {
                        return true;
                    }
                }

                // 处理默认组
                if (group.equalsIgnoreCase("default") && playerGroups.isEmpty()) {
                    return true;
                }
            }
        }

        // 如果组和玩家都为空，表示对所有人生效
        if ((rule.getGroups() == null || rule.getGroups().isEmpty()) &&
                (rule.getPlayers() == null || rule.getPlayers().isEmpty())) {
            return true;
        }

        return false;
    }

    private String applyReplacement(String message, ReplacementRule rule) {
        String trigger = rule.getTrigger();
        String replace = rule.getReplace();

        if (rule.isWholeWord()) {
            // 全词匹配
            String regex = rule.isCaseSensitive() ?
                    "\\b" + trigger + "\\b" :
                    "(?i)\\b" + trigger + "\\b";
            return message.replaceAll(regex, replace);
        } else {
            // 部分匹配
            if (rule.isCaseSensitive()) {
                return message.replace(trigger, replace);
            } else {
                // 不区分大小写替换
                String lowerMessage = message.toLowerCase();
                String lowerTrigger = trigger.toLowerCase();

                StringBuilder result = new StringBuilder();
                int lastIndex = 0;
                int index = lowerMessage.indexOf(lowerTrigger);

                while (index >= 0) {
                    // 添加前一部分
                    result.append(message, lastIndex, index);
                    // 添加替换词
                    result.append(replace);
                    // 更新索引
                    lastIndex = index + trigger.length();
                    // 查找下一个
                    index = lowerMessage.indexOf(lowerTrigger, lastIndex);
                }

                // 添加剩余部分
                result.append(message.substring(lastIndex));
                return result.toString();
            }
        }
    }
}