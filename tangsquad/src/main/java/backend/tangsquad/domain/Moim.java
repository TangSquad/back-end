package backend.tangsquad.domain;

import java.time.LocalDateTime;

public class Moim {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;
    private String moimOwner;

    private String[] moimMembers;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String moimName;
    private String moimIntro;
    private String moimContents;

    private Integer maxPeople;

    private Float price;

    private String[] licenseLimit;

    private String[] region;

    private String age;

    private String[] mood;

    public Moim() {
    }

    public String getMoimOwner() {
        return moimOwner;
    }

    public void setMoimOwner(String moimOwner) {
        this.moimOwner = moimOwner;
    }

    public String[] getMoimMembers() {
        return moimMembers;
    }

    public void setMoimMembers(String[] moimMembers) {
        this.moimMembers = moimMembers;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getMoimName() {
        return moimName;
    }

    public void setMoimName(String moimName) {
        this.moimName = moimName;
    }

    public String getMoimIntro() {
        return moimIntro;
    }

    public void setMoimIntro(String moimIntro) {
        this.moimIntro = moimIntro;
    }

    public String getMoimContents() {
        return moimContents;
    }

    public void setMoimContents(String moimContents) {
        this.moimContents = moimContents;
    }

    public Integer getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(Integer maxPeople) {
        this.maxPeople = maxPeople;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String[] getLicenseLimit() {
        return licenseLimit;
    }

    public void setLicenseLimit(String[] licenseLimit) {
        this.licenseLimit = licenseLimit;
    }

    public String[] getRegion() {
        return region;
    }

    public void setRegion(String[] region) {
        this.region = region;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String[] getMood() {
        return mood;
    }

    public void setMood(String[] mood) {
        this.mood = mood;
    }
}
