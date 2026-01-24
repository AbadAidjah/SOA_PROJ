package com.prescription.demo.model;

import java.util.List;

public class ValidationResponse {

    private boolean ok;
    private List<Issue> issues;
    private List<MappedItem> mappedItems;

    public static class Issue {
        public String drugA;
        public String drugB;
        public String level;
    }

    public static class MappedItem {
        public String drugId;
        public int quantity;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    public List<MappedItem> getMappedItems() {
        return mappedItems;
    }

    public void setMappedItems(List<MappedItem> mappedItems) {
        this.mappedItems = mappedItems;
    }
}