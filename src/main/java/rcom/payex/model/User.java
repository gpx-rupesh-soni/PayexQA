package rcom.payex.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PAYEXUSERS")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "USERID")
	private String userid;
	
	@Column(name = "PASSWORD")
	private String password;
	
	@Column(name = "ST_ROLE")
	private String role;
	
	@Id
	@Column(name = "USER_REF_ID")
	private String refId;
	
	@Column(name = "ST_OP")
	private String operation;
	
	@Column(name = "FNAME")
	private String fname;
	
	@Column(name = "LNAME")
	private String lname;
	
	//@author RahulB 01-06-2021 T4522 console based cf
	/*@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="CF_ID", referencedColumnName="USER_REF_ID", insertable = false, updatable = false)
	@NotFound( action = NotFoundAction.IGNORE )
	private List<DistPayeeCFSetting> distPayeeCfSettings;*/

	public User(String userid, String password) {
		this.userid = userid;
		this.password = password;
	}

	public User() {
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	/*public List<DistPayeeCFSetting> getDistPayeeCfSettings() {
		return distPayeeCfSettings;
	}

	public void setDistPayeeCfSettings(List<DistPayeeCFSetting> distPayeeCfSettings) {
		this.distPayeeCfSettings = distPayeeCfSettings;
	}*/
	
	

	

	
	
	
	
	
}
