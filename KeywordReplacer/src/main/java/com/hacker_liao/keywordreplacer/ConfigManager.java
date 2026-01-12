/*
 * KeywordReplacer - 配置管理器
 * 作者：hacker_liao
 * 包名：com.hacker_liao.keywordreplacer
 */

package com.hacker_liao.keywordreplacer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Logger;

public class ConfigManager {

    private final JavaPlugin plugin;
    private final List<ReplacementRule> rules;
    private FileConfiguration config;
    private Logger logger;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.rules = new ArrayList<>();
        this.logger = plugin.getLogger();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
        rules.clear();

        if (config.contains("rules")) {
            ConfigurationSection rulesSection = config.getConfigurationSection("rules");
            if (rulesSection != null) {
                for (String key : rulesSection.getKeys(false)) {
                    ConfigurationSection ruleSection = rulesSection.getConfigurationSection(key);
                    if (ruleSection != null) {
                        ReplacementRule rule = new ReplacementRule(
                                key,
                                ruleSection.getString("trigger", ""),
                                ruleSection.getString("replace", ""),
                                ruleSection.getStringList("groups"),
                                ruleSection.getStringList("players"),
                                ruleSection.getBoolean("enabled", true),
                                ruleSection.getBoolean("case_sensitive", false),
                                ruleSection.getBoolean("whole_word", false)
                        );
                        rules.add(rule);
                    }
                }
            }
        }

        // 如果没有规则，添加默认规则
        if (rules.isEmpty()) {
            addDefaultRules();
            saveConfig();
        }

        logger.info("已加载 " + rules.size() + " 条替换规则");
    }

    private void addDefaultRules() {
        // 添加默认规则：在all组下将hacker_liao替换为主人sama~
        ReplacementRule rule1 = new ReplacementRule(
                "主人规则",
                "hacker_liao",
                "主人sama~",
                Arrays.asList("all"),
                Arrays.asList(),
                true,
                false,
                false
        );
        rules.add(rule1);

        // 添加一个测试规则
        ReplacementRule rule2 = new ReplacementRule(
                "all组测试规则",
                "测试",
                "已替换",
                Arrays.asList("all"),
                Arrays.asList(),
                true,
                false,
                true
        );
        rules.add(rule2);

        saveConfig();
    }

    public void saveConfig() {
        config.set("rules", null); // 清除现有规则

        for (ReplacementRule rule : rules) {
            String path = "rules." + rule.getId();
            config.set(path + ".trigger", rule.getTrigger());
            config.set(path + ".replace", rule.getReplace());
            config.set(path + ".groups", rule.getGroups());
            config.set(path + ".players", rule.getPlayers());
            config.set(path + ".enabled", rule.isEnabled());
            config.set(path + ".case_sensitive", rule.isCaseSensitive());
            config.set(path + ".whole_word", rule.isWholeWord());
        }

        plugin.saveConfig();
    }

    public void addRule(ReplacementRule rule) {
        rules.add(rule);
        saveConfig();
        logger.info("已添加规则: " + rule.getId());
    }

    public void removeRule(String ruleId) {
        rules.removeIf(rule -> rule.getId().equals(ruleId));
        saveConfig();
        logger.info("已删除规则: " + ruleId);
    }

    public ReplacementRule getRule(String ruleId) {
        return rules.stream()
                .filter(rule -> rule.getId().equals(ruleId))
                .findFirst()
                .orElse(null);
    }

    public List<ReplacementRule> getRules() {
        return new ArrayList<>(rules);
    }

    public void reload() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        loadConfig();
    }
}