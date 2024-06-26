package com.alya.aplikasilansia.ui.news;

public class News {
    private String name;
    private String date;
    private String category;
    private String newsContent;

    public News(String name, String date, String category) {
        this.name = name;
        this.date = date;
        this.category = category;
    }

//    public News(String name, String date, String category, String newsContent) {
//        this.name = name;
//        this.date = date;
//        this.category = category;
//        this.newsContent = newsContent;
//    }

    // Getter methods
    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public String getNewsContent() {
        return newsContent;
    }

    // Setter methods (if needed)
    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }
}
