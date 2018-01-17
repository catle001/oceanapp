package com.example.user.fypapp.Model;

import android.graphics.Bitmap;

/**
 * Created by user on 28/11/2016.
 */
public class App {
    private String appId;
    private String title;
    private String summary;
    private String icon;
    private double score;
    private double price;
    private boolean free;
    private int minInstalls;
    private int maxInstalls;
    private int reviews;
    private String developer;
    private String developerEmail;
    private String developerWebsite;
    private String updated;
    private String version;
    private String genre;
    private String genreId;
    private String size;
    private String description;
    private String descriptionHTML;
    private String histogram;
    private boolean offersIAP = false;
    private boolean adSupported = false;
    private String androidVersionText;
    private String androidVersion;
    private String contentRating;
    private String screenshots;
    private String video;
    private String comments;
    private String recentChanges;
    private String url;
    private Bitmap imgbit;
    private String subcategory;
    private String foregroundtime;

    public App() { //ONLY FOR HEADER

    }


    public App(String appId, String foregroundtime, String category, String title) {
        this.setAppId(appId);
        this.setForegroundtime(foregroundtime);
        this.setGenre(category);
        this.setTitle(title);
        this.setSubCategory("Local");
    }


    public App(String url, String appId, String title, String developer, double score, double price, boolean free, String icon, String genre) {

        this.setUrl(url);
        this.setAppId(appId);
        this.setTitle(title);
        this.setDeveloper(developer);
        this.setScore(score);
        this.setPrice(price);
        this.setFree(free);
        this.setIcon(icon);
        this.setGenre(genre);
    }

    public App(String title, String summary, String icon, double price, boolean free, int minInstalls, int maxInstalls, double score, int reviews, String developer, String developerEmail, String developerWebsite, String updated, String version, String genre, String genreId, String size, String description, String descriptionHTML, String histogram, boolean offersIAP, boolean adSupported, String androidVersionText, String androidVersion, String contentRating, String screenshots, String video, String comments, String recentChanges, String url, String appId) {

        this.setTitle(title);
        this.setSummary(summary);
        this.setIcon(icon);
        this.setPrice(price);
        this.setFree(free);
        this.setMinInstalls(minInstalls);
        this.setMaxInstalls(maxInstalls);
        this.setScore(score);
        this.setReviews(reviews);
        this.setDeveloper(developer);
        this.setDeveloperEmail(developerEmail);
        this.setDeveloperWebsite(developerWebsite);
        this.setUpdated(updated);
        this.setVersion(version);
        this.setGenre(genre);
        this.setGenreId(genreId);
        this.setSize(size);
        this.setDescription(description);
        this.setDescriptionHTML(descriptionHTML);
        this.setHistogram(histogram);
        this.setOffersIAP(offersIAP);
        this.setAdSupported(adSupported);
        this.setAndroidVersionText(androidVersionText);
        this.setAndroidVersion(androidVersion);
        this.setContentRating(contentRating);
        this.setScreenshots(screenshots);
        this.setVideo(video);
        this.setComments(comments);
        this.setRecentChanges(recentChanges);
        this.setUrl(url);
        this.setAppId(appId);

    }

    public Bitmap getImgbit() {
        return imgbit;
    }

    public void setImgbit(Bitmap imgbit) {
        this.imgbit = imgbit;
    }

    public void setSubCategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getSubCategory() {
        return subcategory;
    }

    public String getAppId() {
        return appId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String newicon) {
        icon = newicon;
    }

    public String getGenre() {
        return genre;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getScreenshots() {
        return screenshots;
    }

    public String getComments() {
        return comments;
    }

    public double getScore() {
        return score;
    }

    public double getPrice() {
        return price;
    }

    public boolean getFree() {
        return isFree();
    }

    public boolean isFree() {
        return free;
    }

    public int getMinInstalls() {
        return minInstalls;
    }

    public int getMaxInstalls() {
        return maxInstalls;
    }

    public int getReviews() {
        return reviews;
    }

    public String getDeveloperEmail() {
        return developerEmail;
    }

    public String getDeveloperWebsite() {
        return developerWebsite;
    }

    public String getUpdated() {
        return updated;
    }

    public String getVersion() {
        return version;
    }

    public String getGenreId() {
        return genreId;
    }

    public String getSize() {
        return size;
    }

    public String getDescriptionHTML() {
        return descriptionHTML;
    }

    public String getHistogram() {
        return histogram;
    }

    public boolean isOffersIAP() {
        return offersIAP;
    }

    public boolean isAdSupported() {
        return adSupported;
    }

    public String getAndroidVersionText() {
        return androidVersionText;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public String getContentRating() {
        return contentRating;
    }

    public String getVideo() {
        return video;
    }

    public String getRecentChanges() {
        return recentChanges;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public void setMinInstalls(int minInstalls) {
        this.minInstalls = minInstalls;
    }

    public void setMaxInstalls(int maxInstalls) {
        this.maxInstalls = maxInstalls;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public void setDeveloperEmail(String developerEmail) {
        this.developerEmail = developerEmail;
    }

    public void setDeveloperWebsite(String developerWebsite) {
        this.developerWebsite = developerWebsite;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDescriptionHTML(String descriptionHTML) {
        this.descriptionHTML = descriptionHTML;
    }

    public void setHistogram(String histogram) {
        this.histogram = histogram;
    }

    public void setOffersIAP(boolean offersIAP) {
        this.offersIAP = offersIAP;
    }

    public void setAdSupported(boolean adSupported) {
        this.adSupported = adSupported;
    }

    public void setAndroidVersionText(String androidVersionText) {
        this.androidVersionText = androidVersionText;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public void setContentRating(String contentRating) {
        this.contentRating = contentRating;
    }

    public void setScreenshots(String screenshots) {
        this.screenshots = screenshots;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setRecentChanges(String recentChanges) {
        this.recentChanges = recentChanges;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getForegroundtime() {
        return foregroundtime;
    }

    public void setForegroundtime(String foregroundtime) {
        this.foregroundtime = foregroundtime;
    }
}
