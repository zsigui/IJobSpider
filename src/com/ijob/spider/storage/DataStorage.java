package com.ijob.spider.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ijob.spider.model.Job;
import com.ijob.spider.model.Organization;
import com.ijob.spider.model.Recruitment;
import com.ijob.spider.model.SpiderParams;
import com.ijob.spider.queue.VisitedUrlQueue;

public class DataStorage {

	private static Logger Log = Logger.getLogger(DataStorage.class);
	public Connection mConn;

	// 用人单位信息插入SQL语句
	private final static String INSERT_ORGANIZATION = "insert into organization (org_name, org_industry, "
			+ "org_natrue, org_scale, org_registered_capital, org_real_capital, org_type, org_website, "
			+ "org_description, org_address, org_postal_code, org_contacts, org_email, org_pic) "
			+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private final static String SELECT_ORGANIZATION_ID = "select org_id from organization where org_name = ?";
	// 招聘会信息插入SQL语句
	private final static String INSERT_RECRUITMENT = "insert into recruitment (rcm_org_id, rcm_type, "
			+ "rcm_info_provider, rcm_name, rcm_hold_location, rcm_hold_time, rcm_target_educ, rcm_target_major, "
			+ "rcm_description, rcm_contacts, rcm_contact_type, rcm_fax_code, rcm_email, rcm_relate_link) "
			+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private final static String SELECT_RECRUITMENT_ID = "select rcm_id from recruitment where rcm_name = ? and rcm_org_id = ? and rcm_hold_time = ?";
	// 职位信息插入SQL语句
	private final static String INSERT_JOBS = "insert into jobs (job_org_id, job_name, job_location, job_pub_time, "
			+ "job_deadline, job_recuit_num, job_monthly_pay, job_category, job_type, job_gender_require, "
			+ "job_language_require, job_proficiency, job_min_qualification, job_major_require, job_description,"
			+ "job_contactType) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private final static String SELECT_JOBS_ID = "select job_id from jobs where job_name = ? and job_org_id = ? and job_pub_time = ?";
	// 其他招聘信息插入SQL语句
	//private final static String INSERT_ORECRUITMENT = "";

	private final static String UPDATE_STATE_OF_SCRALL = "update scrallurl set scr_dealed = 1 where scr_url = ?";

	public DataStorage() {

	}

	public void initOrOpenConn() throws ClassNotFoundException, SQLException {
		if (mConn == null || mConn.isClosed()) {
			Class.forName(SpiderParams.DRIVER);
			mConn = DriverManager.getConnection(SpiderParams.DB_URL,
					SpiderParams.USERNAME, SpiderParams.PWD);
		}
	}

	public void closeConn() throws SQLException {
		if (mConn != null && !mConn.isClosed()) {
			mConn.close();
			mConn = null;
		}
	}

	public boolean execute(String sql, List<Object> params) {
		boolean result = false;
		try {
			initOrOpenConn();
			PreparedStatement ps = mConn.prepareStatement(sql);
			int i = 0;
			for (Object param : params) {
				ps.setObject(++i, param);
			}
			result = ps.execute();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}

	public Object executeQueryOne(String sql, List<Object> params) {
		Object result = -1;
		try {
			initOrOpenConn();
			PreparedStatement ps = mConn.prepareStatement(sql);
			int i = 0;
			for (Object param : params) {
				ps.setObject(++i, param);
			}
			ResultSet rs = ps.executeQuery();
			rs.beforeFirst();
			if (rs.next())
				result = rs.getObject(1);
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}

	public void saveFetchState() {
		try {
			if (!VisitedUrlQueue.isEmpty()) {
				initOrOpenConn();
				PreparedStatement ps = mConn
						.prepareStatement(UPDATE_STATE_OF_SCRALL);
				int batchSize = 500;
				int count = 0;
				while (!VisitedUrlQueue.isEmpty()) {
					ps.setString(1, VisitedUrlQueue.outElement());
					ps.addBatch();
					if (count % batchSize == 0)
						ps.executeBatch();
				}
				ps.executeBatch();
				ps.close();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void store(Object data) {
		// store to DB
		Log.info("Start store to DB... ");
		if (data instanceof Recruitment) {
			insertRecruitment((Recruitment) data);
		} else if (data instanceof Organization) {
			insertOrganization((Organization) data);
		} else if (data instanceof Job) {
			insertJob((Job) data);
		}
		Log.info("End store to DB... ");
	}

	private void insertJob(Job job) {
		List<Object> params = new ArrayList<Object>();
		params.add(job.getOrgName());
		Object id = executeQueryOne(SELECT_ORGANIZATION_ID, params);
		if (id == null || Integer.parseInt(id.toString()) <= 0)
			return;
		job.setOrgId(Integer.parseInt(id.toString()));
		params.clear();
		params.add(job.getName());
		params.add(job.getOrgId());
		params.add(job.getPubTime());
		id = executeQueryOne(SELECT_JOBS_ID, params);
		if (id != null && Integer.parseInt(id.toString()) > 0)
			return;
		params.clear();
		params.add(job.getOrgId());
		params.add(job.getName());
		params.add(job.getLocation());
		params.add(job.getPubTime());
		params.add(job.getDeadline());
		params.add(job.getRecuitNum());
		params.add(job.getMonthlyPay());
		params.add(job.getCategory());
		params.add(job.getType());
		params.add(job.getGenderRequire());
		params.add(job.getLanguageRequire());
		params.add(job.getProficiency());
		params.add(job.getMinQualification());
		params.add(job.getMajorRequire());
		params.add(job.getDescription());
		params.add(job.getContactType());
		execute(INSERT_JOBS, params);
	}

	private void insertOrganization(Organization organization) {
		List<Object> params = new ArrayList<Object>();
		params.add(organization.getName());
		params.add(organization.getIndustry());
		params.add(organization.getNatrue());
		params.add(organization.getScale());
		params.add(organization.getRegisteredCapital());
		params.add(organization.getRealCapital());
		params.add(organization.getType());
		params.add(organization.getWebsite());
		params.add(organization.getDescription());
		params.add(organization.getAddress());
		params.add(organization.getPostalCode());
		params.add(organization.getContacts());
		params.add(organization.getEmail());
		params.add(organization.getPic());
		execute(INSERT_ORGANIZATION, params);
	}

	private void insertRecruitment(Recruitment recruitment) {
		List<Object> params = new ArrayList<Object>();
		params.add(recruitment.getInfoProvider());
		Object id = executeQueryOne(SELECT_ORGANIZATION_ID, params);
		if (id == null || Integer.parseInt(id.toString()) <= 0)
			return;
		recruitment.setOrgId(Integer.parseInt(id.toString()));
		params.clear();
		params.add(recruitment.getName());
		params.add(recruitment.getOrgId());
		params.add(recruitment.getHoldTime());
		id = executeQueryOne(SELECT_RECRUITMENT_ID, params);
		if (id != null && Integer.parseInt(id.toString()) > 0)
			return;
		params.clear();
		params.add(recruitment.getOrgId());
		params.add(recruitment.getType());
		params.add(recruitment.getInfoProvider());
		params.add(recruitment.getName());
		params.add(recruitment.getHoldLocation());
		params.add(recruitment.getHoldTime());
		params.add(recruitment.getTargetEduc());
		params.add(recruitment.getTargetMajor());
		params.add(recruitment.getDescriPtion());
		params.add(recruitment.getContacts());
		params.add(recruitment.getContactType());
		params.add(recruitment.getFaxCode());
		params.add(recruitment.getEmail());
		params.add(recruitment.getRelateLink());
		execute(INSERT_RECRUITMENT, params);
		System.out.println("insert success");
	}
}
