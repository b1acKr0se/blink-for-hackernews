package nt.hai.blinkforhackernews.data.model;

import java.io.Serializable;
import java.util.Arrays;

public class Item implements Serializable {
    private String id;
    private boolean deleted;
    private String type;
    private String by;
    private long time;
    private String text;
    private boolean dead;
    private long parent;
    private int[] kids;
    private String url;
    private int score;
    private String title;
    private int[] parts;
    private int descendants;
    private boolean isLoaded;
    private int level;
    private boolean isExpanded = true;
    private boolean isMenuOpened;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public long getParent() {
        return parent;
    }

    public void setParent(long parent) {
        this.parent = parent;
    }

    public int[] getKids() {
        return kids;
    }

    public void setKids(int[] kids) {
        this.kids = kids;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int[] getParts() {
        return parts;
    }

    public void setParts(int[] parts) {
        this.parts = parts;
    }

    public int getDescendants() {
        return descendants;
    }

    public void setDescendants(int descendants) {
        this.descendants = descendants;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public boolean isMenuOpened() {
        return isMenuOpened;
    }

    public void setMenuOpened(boolean menuOpened) {
        isMenuOpened = menuOpened;
    }

    public void copy(Item item) {
        id = item.getId();
        deleted = item.isDeleted();
        type = item.getType();
        by = item.getBy();
        time = item.getTime();
        text = item.getText();
        dead = item.isDead();
        parent = item.getParent();
        kids = item.getKids();
        url = item.getUrl();
        score = item.getScore();
        title = item.getTitle();
        parts = item.getParts();
        descendants = item.getDescendants();
        isLoaded = item.isLoaded();
        level = item.getLevel();
        isExpanded = item.isExpanded();
        isMenuOpened = item.isMenuOpened();
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", deleted=" + deleted +
                ", type='" + type + '\'' +
                ", by='" + by + '\'' +
                ", time=" + time +
                ", text='" + text + '\'' +
                ", dead=" + dead +
                ", parent=" + parent +
                ", kids=" + Arrays.toString(kids) +
                ", url='" + url + '\'' +
                ", score=" + score +
                ", title='" + title + '\'' +
                ", parts=" + Arrays.toString(parts) +
                ", descendants=" + descendants +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(((Item) obj).getId());
    }
}
