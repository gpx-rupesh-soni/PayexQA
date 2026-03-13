package rcom.payex.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "API_LOG")
public class TxnApiLog {

	@Id
	@Column(name = "ID")
	private String id;
	
	@Column(name = "PPID")
	private Long ppid;
	
	@Column(name = "CREATE_DATE")
	private Date createDate;

	@Column(name = "PAYEX_RETAILER_CODE")
	private String payexRetailerCode;

	@Column(name = "REQUEST")
	@JsonIgnore
	private String request;

	@Column(name = "RESPONSE")
	@JsonIgnore
	private String response;
	
	@Transient
	private JSONObject req;

	@Transient
	private JSONObject res;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getPpid() {
		return ppid;
	}

	public void setPpid(Long ppid) {
		this.ppid = ppid;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getPayexRetailerCode() {
		return payexRetailerCode;
	}

	public void setPayexRetailerCode(String payexRetailerCode) {
		this.payexRetailerCode = payexRetailerCode;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public JSONObject getReq() {
		if(req==null) {
			try {
				req = (JSONObject) new JSONParser().parse(this.request);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return req;
	}

	public void setReq(JSONObject req) {
		this.req = req;
	}

	public JSONObject getRes() {
		if(res==null) {
			try {
				res = (JSONObject) new JSONParser().parse(this.response);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return res;
	}

	public void setRes(JSONObject res) {
		this.res = res;
	}	
	
}
