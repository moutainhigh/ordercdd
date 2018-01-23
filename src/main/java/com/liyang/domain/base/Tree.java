package com.liyang.domain.base;

import java.util.HashSet;
import java.util.Set;

public class Tree {
    private String id;
    private String topic;
    private Set<Tree> children =new HashSet<>();
    private String notice;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Set<Tree> getChildren() {
        return children;
    }

    public void setChildren(Set<Tree> children) {
        this.children = children;
    }

    public void addTree(Tree tree)
    {
        children.add(tree);
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
