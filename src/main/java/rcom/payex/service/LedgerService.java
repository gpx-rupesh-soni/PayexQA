package rcom.payex.service;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import rcom.payex.dao.InvoiceTransactionDao;
import rcom.payex.model.CentralInvoices;
import rcom.payex.model.RetailerVacc;
import rcom.payex.model.TransactionCore;
import rcom.payex.model.TxnApiLog;
import rcom.payex.utils.CentralInvoiceUtil;
import rcom.payex.utils.Utils;

@Service
public class LedgerService {

	@Autowired
	InvoiceTransactionDao invoiceTransactionDao;

	@Autowired
	Gson gson;

	private static final String SNAPSHOT_DIR = "D:/ApplicationLog/PayexApi/VG-CN-Pull-Compare/";
	/** Dates are compared by day, the time of a run never matches */
	/** One JSON line per compare run */
	private static final String COMPARE_LOG = SNAPSHOT_DIR + "compare-log.txt";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String TXN = "transaction";
	private static final String INVOICES = "invoices";
	private static final String CREDIT_NOTES = "creditNotes";
	private static final String INVOICE_KEY = "PAYEX_TRANSACTION_ID";
	private static final String CN_KEY = "PAYEX_CN_ID";
	private static final String TXN_KEY = "PAYEX_PAYMENT_ID";
	
	private static Logger logger = LogManager.getLogger(LedgerService.class);
	
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
		
		return neftMisPayload;
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
			
			String payexInvoiceNumber = orgInv.getPAYEX_INVOICE_NUMBER()+"#00"+oldInvs.size();
			System.out.println("payexInvoiceNumber : "+payexInvoiceNumber);
			String sequenceFlag = "P0"+oldInvs.size();
			System.out.println("sequenceFlag : "+sequenceFlag);
			
			CentralInvoices payexCentralInvoice = CentralInvoiceUtil.getCentralInvoice(orgInv.getRETAILER_CODE(), payexRetailerCode,
					payexCpgCode, orgInv.getPAYEX_DISTRIBUTOR_CODE(), orgInv.getROUTE_CODE(), orgInv.getAGENT_CODE(),
					orgInv.getCPG_INVOICE_NUMBER(), payexInvoiceNumber, orgInv.getCPG_INCVOICE_DATE().getTime(), orgInv.getDATE_TO_DUE().getTime(), orgInv.getCPG_INVOICE_AMOUNT(), orgInv.getTAXABLE_AMOUNT(),
					(orgInv.getCPG_INVOICE_AMOUNT()-paidAmount), orgInv.getDISTRIBUTOR_NAME(), orgInv.getINVOICE_INFO(), orgInv.getINVOICE_SETTINGS(), orgInv.getDIV_CODE(), orgInv.getINVOICE_INFO_EXTRA(),
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
	public List<CentralInvoices> getInvoicesDetails(String payexCpgCode, String payexRetailerCode, String cpgInvoiceNumber){
		return invoiceTransactionDao.getInvoicesDetails(payexCpgCode, payexRetailerCode, cpgInvoiceNumber);
	}
	
	/**
	 * Saves the compared rows of a payment to its snapshot file, overwriting an earlier snapshot.
	 */
	public JSONObject saveSnapshot(TransactionCore paymentDetails) {
		Path file = snapshotFile(paymentDetails.getPayexPaymentId());
		JSONObject resp = new JSONObject();
		try {
			if(paymentDetails.getPaymentStatus()==80){
				JSONObject snapshot = readFromDb(paymentDetails.getPayexPaymentId());
				if(snapshot.getJSONObject(TXN).length() == 0) {
					resp.put("Status", false);
					resp.put("msg", "No transaction found for PAYEX_PAYMENT_ID " + paymentDetails.getPayexPaymentId());
					return resp;
				}
				writeSnapshot(file, snapshot);
				resp.put("Status", true);
				resp.put("file", file.toString());
				resp.put("snapshot", snapshot);
			}else {
				String json = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
				resp.put("Status", true);
				resp.put("file", file.toString());
				resp.put("snapshot", new JSONObject(json));
			}
		} catch (IOException | JSONException e) {
			logger.error(Utils.getStackTrace(e));
			try {
				resp.put("Status", false);
				resp.put("msg", "Snapshot not saved : " + e.getLocalizedMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		return resp;
	}

	/**
	 * Compares the snapshots of the old flow and the new flow payment.
	 * A payment without a snapshot file gets one from the database first, so both sides are always compared snapshot to snapshot.
	 */
	public JSONObject compare(long oldPpi, long newPpi) {
		JSONObject resp = new JSONObject();
		try {
			JSONObject oldData = loadOrCreateSnapshot(oldPpi, "old", resp);
			JSONObject newData = loadOrCreateSnapshot(newPpi, "new", resp);

			JSONObject txnResult = compareRow(oldData.optJSONObject(TXN), newData.optJSONObject(TXN), TXN_KEY);
			JSONObject invoiceResult = compareRows(oldData.optJSONObject(INVOICES), newData.optJSONObject(INVOICES));
			JSONObject cnResult = compareRows(oldData.optJSONObject(CREDIT_NOTES), newData.optJSONObject(CREDIT_NOTES));
			boolean matched = txnResult.optBoolean("matched") && invoiceResult.optBoolean("matched") && cnResult.optBoolean("matched");
			logger.info("VG CN Pull Compare : old {} vs new {} matched : {}", oldPpi, newPpi, matched);

			resp.put("Status", true);
			resp.put("matched", matched);
			resp.put("oldPpi", oldPpi);
			resp.put("newPpi", newPpi);
			resp.put(TXN, txnResult);
			resp.put(INVOICES, invoiceResult);
			resp.put(CREDIT_NOTES, cnResult);
			logCompare(oldPpi, newPpi, matched, txnResult, invoiceResult, cnResult);
		} catch (IOException | JSONException | IllegalArgumentException e) {
			logger.error(Utils.getStackTrace(e));
			try {
				resp.put("Status", false);
				resp.put("msg", "Compare failed : " + e.getLocalizedMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		return resp;
	}

	/**
	 * The snapshot of a payment from its file, or taken from the database and saved when there is no file yet.
	 * @param side "old" / "new", puts &lt;side&gt;Source (file) and &lt;side&gt;SnapshotCreated in resp
	 */
	private JSONObject loadOrCreateSnapshot(long ppi, String side, JSONObject resp) throws IOException, JSONException {
		Path file = snapshotFile(ppi);
		boolean created = !Files.exists(file);
		JSONObject snapshot;
		if(created) {
			snapshot = readFromDb(ppi);
			if(snapshot.getJSONObject(TXN).length() == 0)
				throw new IllegalArgumentException("No transaction found for " + side + " PAYEX_PAYMENT_ID " + ppi);
			writeSnapshot(file, snapshot);
		} else {
			snapshot = new JSONObject(new String(Files.readAllBytes(file), StandardCharsets.UTF_8));
		}
		resp.put(side + "Source", file.toString());
		resp.put(side + "SnapshotCreated", created);
		return snapshot;
	}

	private synchronized void logCompare(long oldPpi, long newPpi, boolean matched, JSONObject txnResult, JSONObject invoiceResult, JSONObject cnResult) {
		try {
			JSONObject entry = new JSONObject();
			entry.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			entry.put("oldPpi", oldPpi);
			entry.put("newPpi", newPpi);
			entry.put("matched", matched);
			entry.put("txnMatched", txnResult.optBoolean("matched"));
			entry.put("invoicesMatched", invoiceResult.optBoolean("matched"));
			entry.put("creditNotesMatched", cnResult.optBoolean("matched"));
			Path log = Paths.get(COMPARE_LOG);
			Files.createDirectories(log.getParent());
			Files.write(log, (entry.toString() + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
					StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException | JSONException e) {
			// the compare result is still returned when the log is not written
			logger.error(Utils.getStackTrace(e));
		}
	}

	/**
	 * Previous compares, latest first, one entry per old/new pair (its last run) with how many times the pair was compared.
	 */
	public JSONObject getCompareLog() {
		JSONObject resp = new JSONObject();
		try {
			Path log = Paths.get(COMPARE_LOG);
			List<String> lines = Files.exists(log) ? Files.readAllLines(log, StandardCharsets.UTF_8) : Collections.<String>emptyList();
			Map<String, JSONObject> byPair = new LinkedHashMap<>();
			for(int i = lines.size() - 1; i >= 0; i--) {
				if(lines.get(i).trim().isEmpty())
					continue;
				JSONObject entry;
				try {
					entry = new JSONObject(lines.get(i));
				} catch (JSONException e) {
					logger.error("VG CN Pull Compare : bad compare log line {} : {}", i + 1, lines.get(i));
					continue;
				}
				String pair = entry.opt("oldPpi") + "/" + entry.opt("newPpi");
				JSONObject last = byPair.get(pair);
				if(last == null) {
					entry.put("count", 1);
					byPair.put(pair, entry);
				} else {
					last.put("count", last.optInt("count") + 1);
				}
			}
			JSONArray list = new JSONArray();
			for(JSONObject entry : byPair.values())
				list.put(entry);
			resp.put("Status", true);
			resp.put("list", list);
		} catch (IOException | JSONException e) {
			logger.error(Utils.getStackTrace(e));
			try {
				resp.put("Status", false);
				resp.put("msg", "Compare log not read : " + e.getLocalizedMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		return resp;
	}

	private void writeSnapshot(Path file, JSONObject snapshot) throws IOException, JSONException {
		Files.createDirectories(file.getParent());
		Files.write(file, snapshot.toString().getBytes(StandardCharsets.UTF_8));
		logger.info("VG CN Pull Compare : snapshot of {} saved to {}", snapshot.opt("ppi"), file);
	}

	private JSONObject readFromDb(long ppi) throws JSONException {
		List<Map<String, Object>> txnRows = invoiceTransactionDao.getTxnRows(ppi);
		JSONObject data = new JSONObject();
		data.put("ppi", ppi);
		data.put("snapshotTime", LocalDateTime.now().toString());
		data.put(TXN, txnRows.isEmpty() ? new JSONObject() : normalize(txnRows.get(0)));
		data.put(INVOICES, keyRows(invoiceTransactionDao.getInvoiceRows(ppi), INVOICE_KEY));
		data.put(CREDIT_NOTES, keyRows(invoiceTransactionDao.getCnRows(ppi), CN_KEY));
		return data;
	}

	/** Rows keyed by their id, so old and new rows of the same invoice/CN are compared */
	private JSONObject keyRows(List<Map<String, Object>> rows, String keyColumn) throws JSONException {
		JSONObject keyed = new JSONObject();
		for(Map<String, Object> row : rows) {
			JSONObject normalized = normalize(row);
			keyed.put(String.valueOf(normalized.opt(keyColumn)), normalized);
		}
		return keyed;
	}

	/** Column names upper-cased and values turned into strings that compare equal across runs and the snapshot file */
	private JSONObject normalize(Map<String, Object> row) throws JSONException {
		JSONObject normalized = new JSONObject();
		// org.json drops a key put with null, so a null column is simply absent (opt() returns null for it)
		for(Map.Entry<String, Object> column : row.entrySet())
			normalized.put(column.getKey().toUpperCase(), normalizeValue(column.getValue()));
		return normalized;
	}

	private String normalizeValue(Object value) {
		if(value == null)
			return null;
		if(value instanceof Number)
			return new BigDecimal(value.toString()).stripTrailingZeros().toPlainString();
		if(value instanceof Date)
			return new SimpleDateFormat(DATE_FORMAT).format((Date) value);
		if(value instanceof Clob) {
			try (Reader reader = ((Clob) value).getCharacterStream()) {
				StringBuilder text = new StringBuilder();
				char[] buffer = new char[1024];
				int read;
				while((read = reader.read(buffer)) != -1)
					text.append(buffer, 0, read);
				return text.toString();
			} catch (SQLException | IOException e) {
				logger.error(Utils.getStackTrace(e));
				return "CLOB not read : " + e.getLocalizedMessage();
			}
		}
		return value.toString();
	}

	private JSONObject compareRows(JSONObject oldRows, JSONObject newRows) throws JSONException {
		JSONObject result = new JSONObject();
		JSONArray onlyInOld = new JSONArray();
		JSONArray onlyInNew = new JSONArray();
		JSONArray diffs = new JSONArray();
		// every row with its full old and new values, for showing matched values too
		JSONArray rows = new JSONArray();
		TreeSet<String> ids = new TreeSet<>();
		ids.addAll(keySet(oldRows));
		ids.addAll(keySet(newRows));
		for(String id : ids) {
			JSONObject rowResult;
			if(!newRows.has(id)) {
				onlyInOld.put(id);
				rowResult = onlyInOneSide(oldRows.getJSONObject(id), new JSONObject());
			} else if(!oldRows.has(id)) {
				onlyInNew.put(id);
				rowResult = onlyInOneSide(new JSONObject(), newRows.getJSONObject(id));
			} else {
				rowResult = compareRow(oldRows.getJSONObject(id), newRows.getJSONObject(id), null);
				if(!rowResult.optBoolean("matched"))
					diffs.put(rowResult);
			}
			rowResult.put("id", id);
			rows.put(rowResult);
		}
		result.put("matched", onlyInOld.length() == 0 && onlyInNew.length() == 0 && diffs.length() == 0);
		result.put("oldCount", oldRows.length());
		result.put("newCount", newRows.length());
		result.put("onlyInOld", onlyInOld);
		result.put("onlyInNew", onlyInNew);
		result.put("diffs", diffs);
		result.put("rows", rows);
		return result;
	}

	private JSONObject onlyInOneSide(JSONObject oldRow, JSONObject newRow) throws JSONException {
		JSONObject result = new JSONObject();
		result.put("matched", false);
		result.put("msg", "only in " + (oldRow.length() == 0 ? "new" : "old"));
		result.put("old", oldRow);
		result.put("new", newRow);
		result.put("diffs", new JSONArray());
		return result;
	}

	/**
	 * @param skipColumn column left out of the comparison (the payment id, which always differs); null = none
	 */
	private JSONObject compareRow(JSONObject oldRow, JSONObject newRow, String skipColumn) throws JSONException {
		JSONObject result = new JSONObject();
		JSONArray diffs = new JSONArray();
		result.put("old", oldRow == null ? new JSONObject() : oldRow);
		result.put("new", newRow == null ? new JSONObject() : newRow);
		if(oldRow == null || newRow == null || oldRow.length() == 0 || newRow.length() == 0) {
			result.put("matched", false);
			result.put("msg", (oldRow == null || oldRow.length() == 0 ? "old" : "new") + " transaction not found");
			result.put("diffs", diffs);
			return result;
		}
		TreeSet<String> columns = new TreeSet<>(keySet(oldRow));
		columns.addAll(keySet(newRow));
		for(String column : columns) {
			if(column.equals(skipColumn))
				continue;
			Object oldValue = oldRow.opt(column);
			Object newValue = newRow.opt(column);
			if(oldValue == null ? newValue != null : !oldValue.equals(newValue)) {
				JSONObject diff = new JSONObject();
				diff.put("column", column);
				diff.put("old", oldValue == null ? JSONObject.NULL : oldValue);
				diff.put("new", newValue == null ? JSONObject.NULL : newValue);
				diffs.put(diff);
			}
		}
		result.put("matched", diffs.length() == 0);
		result.put("diffs", diffs);
		return result;
	}

	/** org.json 20090211 has no keySet(), only a raw keys() iterator */
	private static Set<String> keySet(JSONObject json) {
		Set<String> keys = new TreeSet<>();
		if(json == null)
			return keys;
		Iterator<?> it = json.keys();
		while(it.hasNext())
			keys.add(it.next().toString());
		return keys;
	}

	private Path snapshotFile(long ppi) {
		return Paths.get(SNAPSHOT_DIR + "txn-" + ppi + ".json");
	}
	
	public JSONObject updateTxnAsPending(long ppid) {
		JSONObject res = new JSONObject();		
		try {
			res.put("msg", "pending action compleated.");
			res.put("data",invoiceTransactionDao.updateTxnAsPending(ppid));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	@Transactional(readOnly = true)
	public TransactionCore getTransaction(long ppid) {
		return invoiceTransactionDao.getTransaction(ppid);
	}
	
	
}
