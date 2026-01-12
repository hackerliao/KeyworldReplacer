/*
 * KeywordReplacer - 替换规则类
 * 作者：hacker_liao
 * 包名：com.hacker_liao.keywordreplacer
 */

package com.hacker_liao.keywordreplacer;

import java.util.List;

public class ReplacementRule {
    private String id;
    private String trigger;
    private String replace;
    private List<String> groups;
    private List<String> players;
    private boolean enabled;
    private boolean caseSensitive;
    private boolean wholeWord;

    public ReplacementRule(String id, String trigger, String replace,
                           List<String> groups, List<String> players,
                           boolean enabled, boolean caseSensitive, boolean wholeWord) {
        this.id = id;
        this.trigger = trigger;
        this.replace = replace;
        this.groups = groups;
        this.players = players;
        this.enabled = enabled;
        this.caseSensitive = caseSensitive;
        this.wholeWord = wholeWord;
    }

    // Getters
    public String getId() { return id; }
    public String getTrigger() { return trigger; }
    public String getReplace() { return replace; }
    public List<String> getGroups() { return groups; }
    public List<String> getPlayers() { return players; }
    public boolean isEnabled() { return enabled; }
    public boolean isCaseSensitive() { return caseSensitive; }
    public boolean isWholeWord() { return wholeWord; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTrigger(String trigger) { this.trigger = trigger; }
    public void setReplace(String replace) { this.replace = replace; }
    public void setGroups(List<String> groups) { this.groups = groups; }
    public void setPlayers(List<String> players) { this.players = players; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setCaseSensitive(boolean caseSensitive) { this.caseSensitive = caseSensitive; }
    public void setWholeWord(boolean wholeWord) { this.wholeWord = wholeWord; }
}