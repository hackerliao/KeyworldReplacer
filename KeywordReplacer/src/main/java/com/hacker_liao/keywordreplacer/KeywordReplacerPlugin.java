package com.hacker_liao.keywordreplacer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

import java.util.logging.Logger;

public class KeywordReplacerPlugin extends JavaPlugin {

    private static KeywordReplacerPlugin instance;
    private LuckPerms luckPerms;
    private ConfigManager configManager;
    private ChatListener chatListener;
    private Logger logger;

    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();

        // 检查LuckPerms是否安装
        Plugin luckPermsPlugin = Bukkit.getPluginManager().getPlugin("LuckPerms");
        if (luckPermsPlugin == null) {
            logger.severe("LuckPerms未找到！本插件需要LuckPerms才能运行。");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        try {
            luckPerms = LuckPermsProvider.get();
            logger.info("成功连接到LuckPerms API");
        } catch (Exception e) {
            logger.severe("无法连接到LuckPerms API: " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // 初始化配置管理器
        configManager = new ConfigManager(this);
        configManager.loadConfig();

        // 注册事件监听器
        chatListener = new ChatListener(this, configManager, luckPerms);
        getServer().getPluginManager().registerEvents(chatListener, this);

        // 注册命令
        getCommand("keywordreplacer").setExecutor(new CommandHandler(this, configManager));
        getCommand("keywordreplacer").setTabCompleter(new TabCompletion());

        logger.info("关键词替换插件已启用！作者：hacker_liao");
        logger.info("已加载 " + configManager.getRules().size() + " 条替换规则");
    }

    @Override
    public void onDisable() {
        logger.info("关键词替换插件已禁用");
    }

    public static KeywordReplacerPlugin getInstance() {
        return instance;
    }

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }

    // 新增：获取ConfigManager的方法
    public ConfigManager getConfigManager() {
        return configManager;
    }
}