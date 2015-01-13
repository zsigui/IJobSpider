package com.ijob.spider.model;

public class Job {
	private int id;
	private int orgId;
	private String orgName; // 用于获取用人单位外键id
	private String name;
	private String location;
	private String pubTime;
	private String deadline = "";
	private String recuitNum;
	private String monthlyPay;
	private String category;
	private String type;
	private String genderRequire;
	private String languageRequire;
	private String proficiency;
	private String minQualification;
	private String majorRequire;
	private String description;
	private String contactType;
	private int scanNum = 0;

	public Job() {
	}

	public Job(int id, int orgId, String orgName, String name, String location,
			String pubTime, String deadline, String recuitNum,
			String monthlyPay, String category, String type,
			String genderRequire, String languageRequire, String proficiency,
			String minQualification, String majorRequire, String description,
			String contactType, int scanNum) {
		this.id = id;
		this.orgId = orgId;
		this.orgName = orgName;
		this.name = name;
		this.location = location;
		this.pubTime = pubTime;
		this.deadline = deadline;
		this.recuitNum = recuitNum;
		this.monthlyPay = monthlyPay;
		this.category = category;
		this.type = type;
		this.genderRequire = genderRequire;
		this.languageRequire = languageRequire;
		this.proficiency = proficiency;
		this.minQualification = minQualification;
		this.majorRequire = majorRequire;
		this.description = description;
		this.contactType = contactType;
		this.scanNum = scanNum;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPubTime() {
		return pubTime;
	}

	public void setPubTime(String pubTime) {
		this.pubTime = pubTime;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public String getRecuitNum() {
		return recuitNum;
	}

	public void setRecuitNum(String recuitNum) {
		this.recuitNum = recuitNum;
	}

	public String getMonthlyPay() {
		return monthlyPay;
	}

	public void setMonthlyPay(String monthlyPay) {
		this.monthlyPay = monthlyPay;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGenderRequire() {
		return genderRequire;
	}

	public void setGenderRequire(String genderRequire) {
		this.genderRequire = genderRequire;
	}

	public String getLanguageRequire() {
		return languageRequire;
	}

	public void setLanguageRequire(String languageRequire) {
		this.languageRequire = languageRequire;
	}

	public String getProficiency() {
		return proficiency;
	}

	public void setProficiency(String proficiency) {
		this.proficiency = proficiency;
	}

	public String getMinQualification() {
		return minQualification;
	}

	public void setMinQualification(String minQualification) {
		this.minQualification = minQualification;
	}

	public String getMajorRequire() {
		return majorRequire;
	}

	public void setMajorRequire(String majorRequire) {
		this.majorRequire = majorRequire;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContactType() {
		return contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

	public int getScanNum() {
		return scanNum;
	}

	public void setScanNum(int scanNum) {
		this.scanNum = scanNum;
	}
}
