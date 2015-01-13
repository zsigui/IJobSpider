package com.ijob.spider.model;

public class Recruitment {
	private int id;
	private int orgId;
	private String orgName; // 用于获取用人单位外键id
	private String type;
	private String infoProvider;
	private String name;
	private String holdLocation;
	private String holdTime;
	private String targetEduc;
	private String targetMajor;
	private String descriPtion;
	private String contacts;
	private String contactType;
	private String faxCode;
	private String email;
	private String relateLink;
	private int scanNum = 0;

	public Recruitment() {
	}

	public Recruitment(int id, int orgId, String type, String infoProvider,
			String name, String holdLocation, String holdTime,
			String targetEduc, String targetMajor, String descriPtion,
			String contacts, String contactType, String faxCode, String email,
			String relateLink, int scanNum) {
		this.id = id;
		this.orgId = orgId;
		this.type = type;
		this.infoProvider = infoProvider;
		this.name = name;
		this.holdLocation = holdLocation;
		this.holdTime = holdTime;
		this.targetEduc = targetEduc;
		this.targetMajor = targetMajor;
		this.descriPtion = descriPtion;
		this.contacts = contacts;
		this.contactType = contactType;
		this.faxCode = faxCode;
		this.email = email;
		this.relateLink = relateLink;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInfoProvider() {
		return infoProvider;
	}

	public void setInfoProvider(String infoProvider) {
		this.infoProvider = infoProvider;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHoldLocation() {
		return holdLocation;
	}

	public void setHoldLocation(String holdLocation) {
		this.holdLocation = holdLocation;
	}

	public String getHoldTime() {
		return holdTime;
	}

	public void setHoldTime(String holdTime) {
		this.holdTime = holdTime;
	}

	public String getTargetEduc() {
		return targetEduc;
	}

	public void setTargetEduc(String targetEduc) {
		this.targetEduc = targetEduc;
	}

	public String getTargetMajor() {
		return targetMajor;
	}

	public void setTargetMajor(String targetMajor) {
		this.targetMajor = targetMajor;
	}

	public String getDescriPtion() {
		return descriPtion;
	}

	public void setDescriPtion(String descriPtion) {
		this.descriPtion = descriPtion;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getContactType() {
		return contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

	public String getFaxCode() {
		return faxCode;
	}

	public void setFaxCode(String faxCode) {
		this.faxCode = faxCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRelateLink() {
		return relateLink;
	}

	public void setRelateLink(String relateLink) {
		this.relateLink = relateLink;
	}

	public int getScanNum() {
		return scanNum;
	}

	public void setScanNum(int scanNum) {
		this.scanNum = scanNum;
	}

}
