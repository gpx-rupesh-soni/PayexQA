package rcom.payex.service;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;

import rcom.payex.dao.InvoiceTransactionDao;
import rcom.payex.model.CentralInvoices;
import rcom.payex.model.RetailerCreditNote;
import rcom.payex.model.RetailerVacc;
import rcom.payex.model.TransactionCore;
import rcom.payex.model.TxnApiLog;
import rcom.payex.utils.CentralInvoiceUtil;
import rcom.payex.utils.Utils;

@Service
public class CommonService {

	@Autowired
	InvoiceTransactionDao invoiceTransactionDao;

	@Autowired
	Gson gson;

	private static Logger logger = LogManager.getLogger(CommonService.class);

	@Transactional(rollbackFor=Exception.class)
	public void processNachStatus(Map<String,String> csvData) {
	    System.out.println(csvData);
	    String passPpids = csvData.get("passPpids");
	    String failPpids = csvData.get("failPpids");
	    System.out.println("passPpids:"+passPpids);
	    System.out.println("failPpids:"+failPpids);
	    List<Long> passPpidList = Arrays.stream(passPpids.split(","))
	            .map(String::trim)
	            .filter(s -> !s.isEmpty())
	            .map(Long::valueOf)
	            .collect(Collectors.toList());

	    List<Long> failPpidList = Arrays.stream(failPpids.split(","))
	            .map(String::trim)
	            .filter(s -> !s.isEmpty())
	            .map(Long::valueOf)
	            .collect(Collectors.toList());
	    invoiceTransactionDao.updateNachStatus(passPpidList, failPpidList);
	}

	@Transactional(readOnly = true)
	public List<TransactionCore> getTransactions(String payexCpgCode, String payexRetailerCode, String type, Long payexPaymentId) {

		int paymentStatus = 0;
		if(type!=null)
			switch (type) {
			case "approved-neft":
				paymentStatus = 31;
				break;
			case "approved-nach":
				paymentStatus = 30;
				break;
			case "failed":
				paymentStatus = 40;
				break;
			case "paid":
				paymentStatus = 80;
				break;
			default:
				break;
			}

		return invoiceTransactionDao.getTransactions(payexCpgCode, payexRetailerCode, paymentStatus, payexPaymentId);
	}

	@Transactional(readOnly = true)
	public Map<String, Object> getTransactionInfo(Long payexPaymentId) {

		Map<String, Object> txnInfo = new HashMap<String, Object>();
		TransactionCore txn = invoiceTransactionDao.getTransaction(payexPaymentId);
		txnInfo.put("txn", txn);
		List<CentralInvoices> inv = invoiceTransactionDao.getInvoicesOfTxn(payexPaymentId);
		System.out.println("inv.size() : "+inv.size());
		txnInfo.put("inv", inv);

		List<RetailerCreditNote> cn = invoiceTransactionDao.getCreditNoteOfTxn(payexPaymentId);
		System.out.println("cn.size() : "+cn.size());
		txnInfo.put("cn", cn);

		return txnInfo;
	}

	@Transactional(readOnly = true)
	public Map<String, Object> getTransactionOtherInfo(String action, Long payexPaymentId) {
		Map<String, Object> txnInfo = new HashMap<String, Object>();
		switch (action) {
		case "api-log":
			TxnApiLog apiLog = invoiceTransactionDao.getTxnApiLog(payexPaymentId);
			txnInfo.put("apiLog", apiLog);
			break;

		default:
			break;
		}
		return txnInfo;
	}

	@Transactional
	public JSONObject updateTransaction(Map<String,String> req) {
		Long ppid = Long.valueOf(req.get("id").toString());
	    String action = req.get("action").toString();

	    JSONObject res = new JSONObject();
	    try {
			res.put("action", action);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    System.out.println("action : "+ action);
	    if(action!=null)
			switch (action) {
			case "DELETE":
				try {
					res.put("data",invoiceTransactionDao.deleteTxn(ppid));
					res.put("msg", "delete action compleated.");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//invoiceTransactionDao.deleteTxn(ppid);
				break;
			case "PENDING":
				try {
					res.put("msg", "pending action compleated.");
					res.put("data",invoiceTransactionDao.updateTxnAsPending(ppid));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "FAILED":
				try {
					res.put("msg", "failed action compleated.");
					res.put("data",updateTxnAsFailed(ppid));
				} catch (JSONException e) {
					e.printStackTrace();
				}

				break;
			case "PAID":
				try {
					res.put("msg", "paid action compleated.");
					res.put("data",updateTxnAsPaid(ppid));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			default:
				break;
			}
	    try {
			res.put("success", true);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return res;
	}

	public JSONObject updateTxnAsFailed(Long ppid) {
		try {
			System.out.println("ppid : "+ppid);
			//InvoiceTransaction txn = invoiceTransactionDao.getInvoiceByPayexPaymentId(new Long(ppid));
			TransactionCore txn  = invoiceTransactionDao.getTransaction(ppid);
			int paymentMode = 0;
			if(txn!=null) {
				paymentMode = txn.getPaymentMode();
				switch (paymentMode) {
				case 0: //NACH
					return invoiceTransactionDao.updateNachStatus(Collections.emptyList(),Collections.singletonList(ppid));

				case 30: //NEFT

					break;

				default:
					break;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject updateTxnAsPaid(Long ppid) {
		try {
			System.out.println("updateTxnAsPaid called > ppid : "+ppid);
			//InvoiceTransaction txn = invoiceTransactionDao.getInvoiceByPayexPaymentId(new Long(ppid));
			TransactionCore txn  = invoiceTransactionDao.getTransaction(ppid);
			int paymentMode = 0;
			if(txn!=null) {
				paymentMode = txn.getPaymentMode();
				System.out.println("paymentMode : "+paymentMode);
				switch (paymentMode) {
				case 0: //NACH
					return invoiceTransactionDao.updateNachStatus(Collections.singletonList(ppid), Collections.emptyList());

				case 30: //NEFT
					return initiateNeftMis(txn);

				default:
					break;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject initiateNeftMis(TransactionCore txn) {
		System.out.println("initiateNeftMis called... ");
		System.out.println("getPAYEX_CPG_CODE : "+txn.getPayexCpgCode());
		System.out.println("getRETAILER_CODE : "+txn.getCPG_RETAILER_CODE());
		RetailerVacc vacc = invoiceTransactionDao.getRetailerVaccMaster(txn.getPayexCpgCode(), txn.getCPG_RETAILER_CODE());

		JSONObject neftMisPayload = new JSONObject();

		try {
			neftMisPayload.put("clientCode", "PAYEX");
			neftMisPayload.put("van", vacc.getVaccNo());
			neftMisPayload.put("txnAmount", txn.getAmountBank());
			neftMisPayload.put("currCode", "INR");
			neftMisPayload.put("payMode", "NEFT");
			neftMisPayload.put("utrn", "UTR-PAYEX-"+txn.getPayexPaymentId());
			neftMisPayload.put("txnDate",new SimpleDateFormat("dd/MM/yyyyyy").format(new Date()));
			neftMisPayload.put("crTime", new SimpleDateFormat("HH:mm:ss").format(new Date()));
			neftMisPayload.put("remitterName", txn.getPayexRetailerCode());
			neftMisPayload.put("remitterAccNo", "1234567890");
			neftMisPayload.put("ifsc", "IFSC0000001");
			neftMisPayload.put("bank", vacc.getBankTag());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		callQaPgNeftMis(neftMisPayload.toString());
		return neftMisPayload;
	}

	public void callQaPgNeftMis(String payload) {
		try {
			Utils.sendPost("https://payex.co.in/PayexUTQA/MIS/V001/PAYEXQA/neftmis", payload, "", null);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Transactional
	public void updateTransactionDoc(long ppid) {
		invoiceTransactionDao.updateTxnAsPending(ppid);
	}

	@Transactional(rollbackFor=Exception.class)
	public void addPartialInvoice(String payexCpgCode, String payexRetailerCode, String cpgInvoiceNumber) {
		try {
		List<CentralInvoices> oldInvs= invoiceTransactionDao.getInvoicesDetails(payexCpgCode, payexRetailerCode, cpgInvoiceNumber);
		System.out.println("oldInvs.size() : "+oldInvs.size());
		boolean partEntryAllowed = true;
		double paidAmount = 0;
		if(!oldInvs.isEmpty()) {
			for (CentralInvoices inv : oldInvs) {
				System.out.println("inv : "+inv.getPAYEX_INVOICE_NUMBER()+", status : "+inv.getPROCESS_STATUS());
				if(inv.getPROCESS_STATUS()<40) {
					partEntryAllowed = false;
				}
				paidAmount += inv.getAMOUNT_APPROVED();
			}
		}

		if(partEntryAllowed) {
			CentralInvoices orgInv = oldInvs.get(0);
			System.out.println("payexInvoiceNumber : "+orgInv.getPAYEX_INVOICE_NUMBER());
			System.out.println("getCPG_INVOICE_AMOUNT : "+orgInv.getCPG_INVOICE_AMOUNT());
			System.out.println("getTOTAL_AMOUNT_DUE : "+orgInv.getTOTAL_AMOUNT_DUE());
			String payexInvoiceNumber = orgInv.getPAYEX_INVOICE_NUMBER().split("#")[0]+"#00"+oldInvs.size();
			System.out.println("payexInvoiceNumber : "+payexInvoiceNumber);
			String sequenceFlag = "P0"+oldInvs.size();
			System.out.println("sequenceFlag : "+sequenceFlag);

			CentralInvoices payexCentralInvoice = CentralInvoiceUtil.getCentralInvoice(orgInv.getRETAILER_CODE(), payexRetailerCode,
					payexCpgCode, orgInv.getPAYEX_DISTRIBUTOR_CODE(), orgInv.getROUTE_CODE(), orgInv.getAGENT_CODE(),
					orgInv.getCPG_INVOICE_NUMBER(), payexInvoiceNumber, orgInv.getCPG_INCVOICE_DATE().getTime(), orgInv.getDATE_TO_DUE().getTime(), orgInv.getCPG_INVOICE_AMOUNT(), orgInv.getTAXABLE_AMOUNT(),
					(orgInv.getTOTAL_AMOUNT_DUE()-paidAmount), orgInv.getDISTRIBUTOR_NAME(), orgInv.getINVOICE_INFO(), orgInv.getINVOICE_SETTINGS(), orgInv.getDIV_CODE(), orgInv.getINVOICE_INFO_EXTRA(),
					orgInv.getVIEW_TYPE(), orgInv.getVIEW_TYPE(), orgInv.getINVOICE_SETTINGS(), orgInv.getINVOICE_TYPE(), 0, orgInv.getCPG_INVOICE_CREDIT(),orgInv.getFINANCIAL_YEAR());
			payexCentralInvoice.setSEQUENCE_FLAG(sequenceFlag);
			payexCentralInvoice.setAREA_CODE(orgInv.getAREA_CODE());
			payexCentralInvoice.setTcs(orgInv.getTcs());

			invoiceTransactionDao.savePayexCentralInvoice(payexCentralInvoice);

		}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Transactional(readOnly = true)
	public void validatePartPayCD(String invID) {
		List<Object[]> invs = invoiceTransactionDao.fetchPayexCentralInvoices(invID);
		try {
			processInvoicesAndGenerateCsv(invs,"https://freepay.co/PayexApiT/ret_invoices/CD_factor","/home/gpxpwrusr/Downloads/vg-partpay_cd.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void processInvoicesAndGenerateCsv(List<Object[]> rows, String postUrl, String csvOutputPath)
	        throws IOException {

	    ObjectMapper mapper = new ObjectMapper();

	    try (FileWriter csv = new FileWriter(csvOutputPath)) {
	        csv.write("TI,INV-ACD,CDT,CDF,CDV\n");

	        for (Object[] row : rows) {
	        	Long pa = row[2] == null ? null : ((Number) row[0]).longValue();
	            Long pd  = row[2] == null ? null : ((Number) row[1]).longValue();
	            Long ti       = row[2] == null ? null : ((Number) row[2]).longValue();
	            String pm     = (String) row[3];
	            String acd     = (String) row[4];

	            // ---- build JSON payload directly ----
	            ObjectNode prdItem = mapper.createObjectNode();
	            prdItem.put("TI", ti);
	            prdItem.put("WHT", 0);
	            prdItem.put("OTHER", 0);
	            prdItem.put("PRO", "");
	            prdItem.put("PRW", "");
	            prdItem.put("TDS", 0);

	            ArrayNode prdArray = mapper.createArrayNode();
	            prdArray.add(prdItem);

	            ObjectNode payload = mapper.createObjectNode();
	            payload.put("PayexCnIds", "");
	            payload.put("PP", true);
	            payload.put("PA", pa);
	            payload.put("PD", pd);
	            payload.put("TI", ti);
	            payload.put("PM", pm);
	            payload.set("PRD", prdArray);
	            payload.put("action", "CD_factor");
	            payload.put("UI", 2);

	            String jsonBody = mapper.writeValueAsString(payload);

	            System.out.println(jsonBody);
	            //if(true) return;

	            // ---- POST call via HttpURLConnection ----
	            String responseBody;
	            int statusCode;

	            URL url = new URL(postUrl);
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setRequestMethod("POST");
	            conn.setRequestProperty("Content-Type", "application/json");
	            conn.setDoOutput(true);
	            conn.setConnectTimeout(10000);
	            conn.setReadTimeout(30000);

	            try (OutputStream os = conn.getOutputStream()) {
	                os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
	            }

	            statusCode = conn.getResponseCode();
	            InputStream is = (statusCode / 100 == 2) ? conn.getInputStream() : conn.getErrorStream();
	            responseBody = readStream(is);
	            conn.disconnect();

	            if (statusCode / 100 != 2) {
	                System.err.println("HTTP " + statusCode + " for TI=" + ti + ": " + responseBody);
	                continue;
	            }

	            // ---- parse response & extract CDT, CDF, CDV directly ----
	            JsonNode respJson = mapper.readTree(responseBody);
	            boolean status = respJson.path("status").asBoolean(false);
	            JsonNode data = respJson.path("data");

	            if (status && !data.isMissingNode()) {
	                long respTi = data.path("TI").asLong();
	                int cdt     = data.path("CDT").asInt();
	                double cdf  = data.path("CDF").asDouble();
	                double cdv  = data.path("CDV").asDouble();

	                csv.write(ti+","+acd+","+respTi + "," + (cdt+"#"+formatNumber(cdv)+"#"+formatNumber(cdf)) + ","+ cdt + "," + cdf + "," + cdv + "\n");
	            } else {
	                String msg = respJson.path("msg").asText("");
	                System.err.println("Skipping TI=" + ti + " - status=false, msg=" + msg);
	            }
	        }
	    }
	}

	private String formatNumber(double value) {
	    BigDecimal bd = BigDecimal.valueOf(value).stripTrailingZeros();
	    return bd.toPlainString();
	}

	private String readStream(InputStream is) throws IOException {
	    if (is == null) return "";
	    ByteArrayOutputStream result = new ByteArrayOutputStream();
	    byte[] buffer = new byte[1024];
	    int length;
	    while ((length = is.read(buffer)) != -1) {
	        result.write(buffer, 0, length);
	    }
	    return result.toString(StandardCharsets.UTF_8.name());
	}


}
