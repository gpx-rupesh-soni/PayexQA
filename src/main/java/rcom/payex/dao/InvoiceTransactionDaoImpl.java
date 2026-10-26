package rcom.payex.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import rcom.payex.model.CentralInvoices;
import rcom.payex.model.InvoiceTransaction;
import rcom.payex.model.RetailerCreditNote;
import rcom.payex.model.RetailerVacc;
import rcom.payex.model.TransactionCore;
import rcom.payex.model.TxnApiLog;
import rcom.payex.utils.Utils;

@Repository
public class InvoiceTransactionDaoImpl implements InvoiceTransactionDao {


	Logger logger = LogManager.getLogger(InvoiceTransactionDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	private static final String TXN_QUERY = "SELECT PAYEX_PAYMENT_ID,PAYEX_RETAILER_CODE, MULTIPAY_COUNT, AMOUNT_TXN, AMOUNT_APPROVED, AMOUNT_CD, AMOUNT_CN, AMOUNT_BANK,"
			+ " PAYMENT_DATE, PRESENTMENT_DATE, BANK_PROCESS_IN_DATE FROM PAYEX_TRANSACTION_CORE WHERE PAYEX_PAYMENT_ID = :ppi";

	private static final String INVOICE_QUERY = "SELECT PAYEX_TRANSACTION_ID, AMOUNT_APPROVED, PAYABLE_AMOUNT, AMOUNT_CN, AMOUNT_CD, APPROVED_CD_DETAILS,"
			+ " AMOUNT_BANK, DATE_TO_PAY, PRESENTMENT_DATE, PAYMENT_LOCK FROM PAYEX_CENTRAL_INVOICES WHERE PAYEX_PAYMENT_ID = :ppi";

	private static final String CN_QUERY = "SELECT PAYEX_CN_ID, CN_STATUS, AMOUNT_USED, SEQUENCE_FLAG FROM RETAILER_CREDIT_NOTE"
			+ " WHERE PAYEX_CN_ID IN (SELECT PAYEX_CN_ID FROM PAYEX_TXN_CN_INV WHERE PAYEX_PAYMENT_ID = :ppi)";
	
	@Transactional(readOnly = true)
	public List<Map<String, Object>> getTxnRows(long ppi) {
		return getRows(TXN_QUERY, ppi);
	}

	@Transactional(readOnly = true)
	public List<Map<String, Object>> getInvoiceRows(long ppi) {
		return getRows(INVOICE_QUERY, ppi);
	}

	@Transactional(readOnly = true)
	public List<Map<String, Object>> getCnRows(long ppi) {
		return getRows(CN_QUERY, ppi);
	}

	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getRows(String query, long ppi) {
		NativeQuery nativeQuery = sessionFactory.getCurrentSession().createNativeQuery(query);
		nativeQuery.setParameter("ppi", ppi);
		return nativeQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	@Override
	public JSONObject updateNachStatus(List<Long> passPpids, List<Long> failedPpids) {
		logger.info("----START----updateParentTxnStatusUsingChildTxnInPayexTxnCore()");
		JSONObject res = new JSONObject();
		try {
			if(passPpids.size()>0) {
				String passPpidsQueryCoreUpdate="UPDATE PAYEX_TRANSACTION_CORE CORE " +
						" SET CORE.PAYMENT_STATUS=80,CORE.CLEARED_AMOUNT=CORE.AMOUNT_BANK,CORE.UPDATE_DATE=systimestamp,CORE.BANK_PROCESS_IN_DATE=sysdate where CORE.PAYMENT_STATUS=30 and PAYEX_PAYMENT_ID IN (:ppids)";

				NativeQuery nativeQueryCorePass = getSession().createNativeQuery(passPpidsQueryCoreUpdate);
				nativeQueryCorePass.setParameter("ppids", passPpids);
				System.out.println("passPpidsQueryCoreUpdate : "+passPpidsQueryCoreUpdate);
				int updateCountTxn=nativeQueryCorePass.executeUpdate();
				res.put("paidUpdateCountTxn",updateCountTxn);
				logger.info("Paid UPDATE COUNT CORE: "+updateCountTxn);


				String passPpidsQueryCentralUpdate="UPDATE PAYEX_CENTRAL_INVOICES INV " +
						 " SET INV.PROCESS_STATUS=80,INV.UPDATE_DATE=systimestamp" +
						 ",INV.BANK_TRANSACTION_ID=('UTR-'||INV.PAYEX_PAYMENT_ID),INV.RECEIVE_FROM_BANK_DATETIME=sysdate,INV.CREDITED_DATE=sysdate " +
						 " WHERE INV.PROCESS_STATUS<80 and PAYEX_PAYMENT_ID IN (:ppids)";


				NativeQuery nativeQueryCentralPass = getSession().createNativeQuery(passPpidsQueryCentralUpdate);
				nativeQueryCentralPass.setParameter("ppids", passPpids);
				System.out.println("passPpidsQueryCentralUpdate : "+passPpidsQueryCentralUpdate);
				int updateCountInv=nativeQueryCentralPass.executeUpdate();
				res.put("paidUpdateCountInv",updateCountInv);

				logger.info("Paid UPDATE COUNT CENTRAL: "+updateCountInv);
			}


			if(failedPpids.size()>0) {
				/*Failed transactions*/

				String failPpidsQueryCoreUpdate="UPDATE PAYEX_TRANSACTION_CORE CORE " +
						" SET CORE.PAYMENT_STATUS=40,CORE.CLEARED_AMOUNT=CORE.AMOUNT_BANK,CORE.UPDATE_DATE=systimestamp,CORE.BANK_PROCESS_IN_DATE=sysdate where CORE.PAYMENT_STATUS=30 and PAYEX_PAYMENT_ID IN (:ppids)";

				NativeQuery nativeQueryCoreFail = getSession().createNativeQuery(failPpidsQueryCoreUpdate);
				nativeQueryCoreFail.setParameter("ppids", failedPpids);
				System.out.println("faliPpidsQueryCoreUpdate : "+failPpidsQueryCoreUpdate);
				int updateCoreCount=nativeQueryCoreFail.executeUpdate();
				res.put("failUpdateCountTxn",updateCoreCount);
				logger.info("FAIL UPDATE COUNT CORE: "+updateCoreCount);

				String failPpidsQueryCentralUpdate="UPDATE PAYEX_CENTRAL_INVOICES INV "
						+ " SET INV.PROCESS_STATUS=40,INV.RECEIVE_FROM_BANK_REMARKS='Filed',INV.CREDITED_DATE=sysdate,"
						+ " UPDATE_DATE=systimestamp WHERE INV.PROCESS_STATUS=30 and PAYEX_PAYMENT_ID IN (:ppids)";


				NativeQuery nativeQueryCentralFail = getSession().createNativeQuery(failPpidsQueryCentralUpdate);
				nativeQueryCentralFail.setParameter("ppids", failedPpids);
				System.out.println("failPpidsQueryCentralUpdate : "+failPpidsQueryCentralUpdate);
				int updateCentralCount=nativeQueryCentralFail.executeUpdate();
				res.put("failUpdateCountInv",updateCentralCount);
				logger.info("FAIL UPDATE COUNT CENTRAL: "+updateCentralCount);
			}

		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e));
		}finally {
			logger.info("----END----updateParentTxnStatusUsingChildTxnInPayexTxnCore()");
		}
		return res;
	}

	@Override
	public List<CentralInvoices> getInvoicesDetails(String payexCpgCode, String payexRetailerCode, String cpgInvoiceNumber) {

		Criteria criteria = getSession().createCriteria(CentralInvoices.class)
				.add(Restrictions.eq("PAYEX_RETAILER_CODE", payexRetailerCode))
				.add(Restrictions.eq("PAYEX_CPG_CODE", payexCpgCode))
				.add(Restrictions.eq("CPG_INVOICE_NUMBER", cpgInvoiceNumber));
		criteria.addOrder(Order.desc("PAYEX_TRANSACTION_ID"));
		List<CentralInvoices> invs = criteria.list();

		return invs;
	}
	
	@Override
	public List<CentralInvoices> getInvoicesOfTxn(Long ppid) {

		Criteria criteria = getSession().createCriteria(CentralInvoices.class)
				.add(Restrictions.eq("PAYEX_PAYMENT_ID", ppid));
		criteria.addOrder(Order.desc("PAYEX_TRANSACTION_ID"));
		return criteria.list();
	}
	
	public CentralInvoices getInvoice(Long pti) {
		return getSession().get(CentralInvoices.class, pti);
	}

	@Override
	public List<RetailerCreditNote> getCreditNoteOfTxn(Long ppid) {

		List<RetailerCreditNote> list = getSession()
			    .createNativeQuery(
			        "SELECT * FROM retailer_credit_note rcn " +
			        "WHERE rcn.PAYEX_CN_ID IN (" +
			        "SELECT r.PAYEX_CN_ID FROM PAYEX_TXN_CN_INV r WHERE r.PAYEX_PAYMENT_ID = :ppid" +
			        ")",
			        RetailerCreditNote.class
			    )
			    .setParameter("ppid", ppid)
			    .getResultList();
		return list;
	}

	@Override
	public CentralInvoices getInvoicesDetails(Long ppid) {
		Criteria criteria = getSession().createCriteria(CentralInvoices.class)
				.add(Restrictions.eq("PAYEX_PAYMENT_ID", ppid))	;

		List<CentralInvoices> invs = criteria.list();
		System.out.println("invs.size() : "+invs.size());
		if (invs.isEmpty()) {
			return null;
		}else {
			return invs.get(0);
		}
	}

	@Override
	public void savePayexCentralInvoice(CentralInvoices payexCentralInvoice) {
		getSession().save(payexCentralInvoice);
	}

	@Override
	public int savePayexCentralInvoices(Collection<CentralInvoices> payexCentralInvoiceList) {

		logger.info("payexCentralInvoiceList size : " + payexCentralInvoiceList.size());
		int saveCount = 0;
		try {
			Session session = getSession();
			for (CentralInvoices invoice : payexCentralInvoiceList) {
				session.save(invoice);
				saveCount++;
			}
			logger.info("Total Invoice Insert : " + payexCentralInvoiceList.size());

		} catch (Exception e) {
			saveCount = 0;
			logger.error("savePayexCentralInvoices() ::::: " + Utils.getStackTrace(e));
		}
		return saveCount;
	}

	@Override
	public RetailerVacc getRetailerVaccMaster(String payexCpgCode,String cpgRetailerCode) {
		Session session = getSession();

        Criteria criteria = session.createCriteria(RetailerVacc.class);
        			criteria.add(Restrictions.eq("payexCpgCode", payexCpgCode));
        			criteria.add(Restrictions.eq("cpgRetailerCode", cpgRetailerCode));
        			return (RetailerVacc) criteria.uniqueResult();
	}

	@Override
	public InvoiceTransaction getInvoiceByPayexPaymentId(Long payexPaymentId) {

		Session session=getSession();
		String sql="from InvoiceTransaction a where a.PAYEX_PAYMENT_ID =:payexPaymentId";

		Query query=session.createQuery(sql).setParameter("payexPaymentId",payexPaymentId);

		InvoiceTransaction inv = (InvoiceTransaction) query.uniqueResult();
		return inv;
	}

	@Override
	public List<TransactionCore> getTransactions(String payexCpgCode, String payexRetailerCode, int status, List<Long> payexPaymentIds) {

		Criteria criteria = getSession().createCriteria(TransactionCore.class);

		if (StringUtils.hasText(payexCpgCode)) {
	        criteria.add(Restrictions.eq("payexCpgCode", payexCpgCode));
	    }

	    if (StringUtils.hasText(payexRetailerCode)) {
	        criteria.add(Restrictions.eq("payexRetailerCode", payexRetailerCode));
	    }

	    // Prefer statuses list over single status
	    if (status !=0) {
	        criteria.add(Restrictions.eq("paymentStatus", status));
	    }

	    if (payexPaymentIds != null && !payexPaymentIds.isEmpty()) {
	        criteria.add(Restrictions.in("payexPaymentId", payexPaymentIds));
	    }

	    // all the asked ids are listed, a search without ids is capped
	    criteria.setMaxResults(Math.max(20, payexPaymentIds == null ? 0 : payexPaymentIds.size()));
	    criteria.addOrder(Order.desc("transactionDate"));

		return criteria.list();
	}

	@Override
	public TransactionCore getTransaction(Long payexPaymentId) {
		return getSession().get(TransactionCore.class, payexPaymentId);
	}

	@Override
	public JSONObject updateTxnAsPending(long ppid) {

		String txnUpdateQry = "UPDATE PAYEX_TRANSACTION_CORE SET PAYMENT_STATUS=50 WHERE PAYEX_PAYMENT_ID=:ppid";
		System.out.println("txnUpdateQry : "+txnUpdateQry);

		NativeQuery nativeQueryTxnUpdate=getSession().createNativeQuery(txnUpdateQry);
		nativeQueryTxnUpdate.setParameter("ppid", ppid);

		int txnUpdateCount = nativeQueryTxnUpdate.executeUpdate();
		JSONObject res = new JSONObject();
		try {
			res.put("txnUpdateCount",txnUpdateCount);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(txnUpdateCount==1){

			String invUpdateQuy="UPDATE PAYEX_CENTRAL_INVOICES SET "
					+ "RETAILER_REMARKS=null,"
					+ "PAYMENT_MODE=0,"
					+ "RETAILER_MODE_OF_PAYMENT=null,"
					+ "APRVD_OR_PAD_BY_DA_OR_RET=null,"
					+ "DATETIME_APPROVED=null,"
					+ "UPDATE_DATE=CREATE_DATE,"
					+ "APPROVED_BANK=null,"
					+ "APPROVED_BANK_UMRN=null,"
					+ "DATE_TO_PAY=0,"
					+ "PRESENTMENT_DATE=null,"
					+ "CHECKED_BY_TIMESTAMP=null,"
					+ "CHECKED_BY=null,"
					+ "BANK_TRANSACTION_ID=null,"
					+ "PAYABLE_AMOUNT=0,"
					+ "PAYMENT_OPTION=0,"
					+ "AMOUNT_CN=0,"
					+ "APPROVED_CD_DETAILS=null,"
					+ "AMOUNT_CD=0,"
					+ "AMOUNT_CD_2=0,"
					+ "AMOUNT_SCD=0,"
					+ "AMOUNT_BANK=0,"
					+ "PROCESS_STATUS=0,"
					+ "PART_PAY_REASON=null,"
					+ "AMOUNT_TDS=0, "
					+ "AMOUNT_APPROVED=0,"
					+ "PRW_REASON=null,"
					+ "PRO_REASON=null,"
					+ "AMOUNT_WHT=0,"
					+ "SEQUENCE_FLAG=null,"
					+ "CLIENT_VERSION=0,"
					+ "PAYMENT_LOCK=0,"
					+ "TDS_PERCENTAGE=0"
					+ " WHERE PAYEX_PAYMENT_ID=:ppid";
			System.out.println("invUpdateQuy : "+invUpdateQuy);


			String cnUpdateQuy="UPDATE RETAILER_CREDIT_NOTE SET "
					+ "CN_STATUS=0,"
					+ "AMOUNT_USED=0,"
					+ "IS_PARTIALLY_USED=0,"
					+ "PAID_DATE=null,"
					+ "PAYMENT_LOCK=0,"
					+ "SEQUENCE_FLAG='S00'"
					+ " where payex_cn_id in (select payex_cn_id from payex_txn_cn_inv where payex_payment_id=:ppid)";
			System.out.println("cnUpdateQuy : "+cnUpdateQuy);

			String txnCnInvMapDelQry = "DELETE FROM payex_txn_cn_inv WHERE PAYEX_PAYMENT_ID=:ppid";
			System.out.println("txnCnInvMapDelQry : "+txnCnInvMapDelQry);

			NativeQuery<?> nativeQueryInvUpdate=getSession().createNativeQuery(invUpdateQuy);
			nativeQueryInvUpdate.setParameter("ppid", ppid);

			NativeQuery<?> nativeQueryCnUpdate=getSession().createNativeQuery(cnUpdateQuy);
			nativeQueryCnUpdate.setParameter("ppid", ppid);

			NativeQuery<?> nativeQuerytxnCnInvMapDel=getSession().createNativeQuery(txnCnInvMapDelQry);
			nativeQuerytxnCnInvMapDel.setParameter("ppid", ppid);

			int invUpdateCount = nativeQueryInvUpdate.executeUpdate();
			int cnUpdateCount = nativeQueryCnUpdate.executeUpdate();
			int txnCnInvMapDelCount = nativeQuerytxnCnInvMapDel.executeUpdate();

			System.out.println("invUpdateCount : "+invUpdateCount);
			System.out.println("cnUpdateCount : "+cnUpdateCount);
			System.out.println("txnCnInvMapDelCount : "+txnCnInvMapDelCount);
			try {
				res.put("invUpdateCount",invUpdateCount);
				res.put("cnUpdateCount",cnUpdateCount);
				res.put("txnCnInvMapDelCount",txnCnInvMapDelCount);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return res;
	}

	@Override
	public JSONObject deleteTxn(long ppid) {
		String txnDelQry = "DELETE FROM PAYEX_TRANSACTION_CORE WHERE PAYEX_PAYMENT_ID=:ppid";
		System.out.println("txnDelQry : "+txnDelQry);
		NativeQuery nativeQueryTxnDel=getSession().createNativeQuery(txnDelQry);
		nativeQueryTxnDel.setParameter("ppid", ppid);

		int txnDelCount = nativeQueryTxnDel.executeUpdate();
		JSONObject res = new JSONObject();
		try {
			res.put("txnDelCount",txnDelCount);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(txnDelCount==1){
			String invDelQry = "DELETE FROM PAYEX_CENTRAL_INVOICES WHERE PAYEX_PAYMENT_ID=:ppid";
			System.out.println("invDelQry : "+invDelQry);

			String cnDelQry = "DELETE FROM RETAILER_CREDIT_NOTE where PAYEX_CN_ID in (select PAYEX_CN_ID from payex_txn_cn_inv where PAYEX_PAYMENT_ID=:ppid)";
			System.out.println("cnDelQry : "+cnDelQry);

			NativeQuery nativeQueryInvDel=getSession().createNativeQuery(invDelQry);
			nativeQueryInvDel.setParameter("ppid", ppid);

			NativeQuery nativeQueryCnDel=getSession().createNativeQuery(cnDelQry);
			nativeQueryCnDel.setParameter("ppid", ppid);

			int invDelCount = nativeQueryInvDel.executeUpdate();
			int cnDelCount = nativeQueryCnDel.executeUpdate();

			System.out.println("invDelCount : "+invDelCount);
			System.out.println("cnDelCount : "+cnDelCount);
			try {
				res.put("invDelCount",invDelCount);
				res.put("cnDelCount",cnDelCount);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return res;
	}

	@Override
	public JSONObject deleteRetailerData(String docType, String payexCpgCode, String retailerCode, String payexRetailerCode) {

		logger.info("deleteRetailerData called...");
		logger.info("docType : "+docType+", payexCpgCode : "+payexCpgCode+", retailerCode : "+retailerCode+", payexRetailerCode : "+payexRetailerCode);
		NativeQuery query;
		Session session = getSession();

		JSONObject res = new JSONObject();

		boolean hasCpgCode = payexCpgCode != null && !payexCpgCode.trim().isEmpty();
		List<String> retailerCodes = splitCodes(retailerCode);
		List<String> payexRetailerCodes = splitCodes(payexRetailerCode);
		boolean hasRetailerCode = !retailerCodes.isEmpty();
		boolean hasPayexRetailerCode = !payexRetailerCodes.isEmpty();

		// never delete without a retailer filter, otherwise whole tables (or a whole CPG) get wiped
		if (!hasRetailerCode && !hasPayexRetailerCode) {
			logger.info("deleteRetailerData : retailerCode or payexRetailerCode is required, skipping delete");
			return res;
		}

		String type = docType == null || docType.trim().isEmpty() ? "all" : docType.trim().toLowerCase();
		boolean isAll = "all".equals(type);

		// common where clause; transaction core keeps the retailer code in CPG_RETAILER_CODE
		String where = buildRetailerDataWhere("RETAILER_CODE", hasCpgCode, hasRetailerCode, hasPayexRetailerCode);
		String whereTxn = buildRetailerDataWhere("CPG_RETAILER_CODE", hasCpgCode, hasRetailerCode, hasPayexRetailerCode);

		try {
			if (isAll || "inv".equals(type) || "dn".equals(type)) {
				String sqlDelInv = "delete from payex_central_invoices" + where;
				if ("inv".equals(type)) {
					sqlDelInv += " and INVOICE_TYPE = 0";
				} else if ("dn".equals(type)) {
					sqlDelInv += " and INVOICE_TYPE = 4";
				}
				query = session.createNativeQuery(sqlDelInv);
				setRetailerDataParams(query, hasCpgCode ? payexCpgCode.trim() : null, retailerCodes, payexRetailerCodes);
				res.put("delRetInvCount", query.executeUpdate());
			}

			if (isAll || "cn".equals(type)) {
				String sqlDelCn = "delete from retailer_credit_note" + where;
				query = session.createNativeQuery(sqlDelCn);
				setRetailerDataParams(query, hasCpgCode ? payexCpgCode.trim() : null, retailerCodes, payexRetailerCodes);
				res.put("delRetCnCount", query.executeUpdate());
			}

			if (isAll || "txn".equals(type)) {
				String sqlDelTxn = "delete from payex_transaction_core" + whereTxn;
				if ("txn".equals(type)) {
					sqlDelTxn += " and INVOICE_TYPE = 0";
				}
				query = session.createNativeQuery(sqlDelTxn);
				setRetailerDataParams(query, hasCpgCode ? payexCpgCode.trim() : null, retailerCodes, payexRetailerCodes);
				res.put("delRetTxnCount", query.executeUpdate());
			}
		}catch (Exception e) {
			logger.info(Utils.getStackTrace(e));
		}

		return res;
	}

	@Override
	public List<Object[]> getRetailerViewFileDocs(String docType, String payexCpgCode, String retailerCode, String payexRetailerCode) {

		logger.info("getRetailerViewFileDocs called...");
		logger.info("docType : "+docType+", payexCpgCode : "+payexCpgCode+", retailerCode : "+retailerCode+", payexRetailerCode : "+payexRetailerCode);
		Session session = getSession();
		List<Object[]> docs = new ArrayList<>();

		boolean hasCpgCode = payexCpgCode != null && !payexCpgCode.trim().isEmpty();
		List<String> retailerCodes = splitCodes(retailerCode);
		List<String> payexRetailerCodes = splitCodes(payexRetailerCode);
		boolean hasRetailerCode = !retailerCodes.isEmpty();
		boolean hasPayexRetailerCode = !payexRetailerCodes.isEmpty();

		if (!hasCpgCode || (!hasRetailerCode && !hasPayexRetailerCode)) {
			logger.info("getRetailerViewFileDocs : payexCpgCode and (retailerCode or payexRetailerCode) are required");
			return docs;
		}

		String type = docType == null || docType.trim().isEmpty() ? "all" : docType.trim().toLowerCase();
		boolean isAll = "all".equals(type);
		String where = buildRetailerDataWhere("RETAILER_CODE", hasCpgCode, hasRetailerCode, hasPayexRetailerCode);
		NativeQuery query;

		// each row : [doc kind (INV / CN), doc number, doc date, view type, payex distributor code]
		if (isAll || "inv".equals(type) || "dn".equals(type)) {
			String sqlInv = "select 'INV', CPG_INVOICE_NUMBER, CPG_INCVOICE_DATE, VIEW_TYPE, PAYEX_DISTRIBUTOR_CODE from payex_central_invoices" + where;
			if ("inv".equals(type)) {
				sqlInv += " and INVOICE_TYPE = 0";
			} else if ("dn".equals(type)) {
				sqlInv += " and INVOICE_TYPE = 4";
			}
			query = session.createNativeQuery(sqlInv);
			setRetailerDataParams(query, payexCpgCode.trim(), retailerCodes, payexRetailerCodes);
			docs.addAll(query.list());
		}

		if (isAll || "cn".equals(type)) {
			String sqlCn = "select 'CN', CPG_CN_ID, CPG_STATRT_DATE, VIEW_TYPE, PAYEX_DISTRIBUTOR_CODE from retailer_credit_note" + where;
			query = session.createNativeQuery(sqlCn);
			setRetailerDataParams(query, payexCpgCode.trim(), retailerCodes, payexRetailerCodes);
			docs.addAll(query.list());
		}

		return docs;
	}

	private String buildRetailerDataWhere(String retailerCodeColumn, boolean hasCpgCode, boolean hasRetailerCode, boolean hasPayexRetailerCode) {
		StringBuilder where = new StringBuilder(" where 1=1");
		if (hasCpgCode) {
			where.append(" and PAYEX_CPG_CODE =:PAYEX_CPG_CODE");
		}
		if (hasRetailerCode) {
			where.append(" and ").append(retailerCodeColumn).append(" in (:RETAILER_CODE)");
		}
		if (hasPayexRetailerCode) {
			where.append(" and PAYEX_RETAILER_CODE in (:PAYEX_RETAILER_CODE)");
		}
		return where.toString();
	}

	private List<String> splitCodes(String codes) {
		List<String> list = new ArrayList<>();
		if (codes == null) {
			return list;
		}
		for (String code : codes.split(",")) {
			if (!code.trim().isEmpty()) {
				list.add(code.trim());
			}
		}
		return list;
	}

	private void setRetailerDataParams(NativeQuery query, String payexCpgCode, List<String> retailerCodes, List<String> payexRetailerCodes) {
		if (payexCpgCode != null) {
			query.setParameter("PAYEX_CPG_CODE", payexCpgCode);
		}
		if (!retailerCodes.isEmpty()) {
			query.setParameterList("RETAILER_CODE", retailerCodes);
		}
		if (!payexRetailerCodes.isEmpty()) {
			query.setParameterList("PAYEX_RETAILER_CODE", payexRetailerCodes);
		}
	}

	@Override
	public List<TxnApiLog> getTxnApiLog(String payexRetailerCode, Date createDate) {


		// Start of day
	    Calendar startCal = Calendar.getInstance();
	    startCal.setTime(createDate);
	    startCal.set(Calendar.HOUR_OF_DAY, 0);
	    startCal.set(Calendar.MINUTE, 0);
	    startCal.set(Calendar.SECOND, 0);
	    startCal.set(Calendar.MILLISECOND, 0);
	    Date startDate = startCal.getTime();

	    // End of day
	    Calendar endCal = Calendar.getInstance();
	    endCal.setTime(createDate);
	    endCal.set(Calendar.HOUR_OF_DAY, 23);
	    endCal.set(Calendar.MINUTE, 59);
	    endCal.set(Calendar.SECOND, 59);
	    endCal.set(Calendar.MILLISECOND, 999);
	    Date endDate = endCal.getTime();

	    Criteria criteria = getSession().createCriteria(TxnApiLog.class)
	            .add(Restrictions.eq("payexRetailerCode", payexRetailerCode))
	            .add(Restrictions.between("createDate", startDate, endDate));

		List<TxnApiLog> logs = criteria.list();
		return logs;
	}

	@Override
	public TxnApiLog getTxnApiLog(Long ppid){
		Criteria criteria = getSession().createCriteria(TxnApiLog.class)
	            .add(Restrictions.eq("ppid", ppid));
		TxnApiLog apiLog = (TxnApiLog) criteria.uniqueResult();
		return apiLog;
	}

	@Override
	public List<Object[]> fetchPayexCentralInvoices(String invId) {
		String sql = "select amount_approved as PA, date_to_pay as PD, payex_transaction_id as TI, retailer_mode_of_payment as PM, approved_cd_details as ACD "
		           + "from payex_central_invoices where payex_transaction_id in ("+invId+")";
		System.out.println("sql : " + sql);

		@SuppressWarnings("unchecked")
		List<Object[]> rows = getSession().createSQLQuery(sql).list();

		return rows;
	}

	@Override
	public List<TransactionCore> getTransactions(List<Long> payexPaymentIds){
		Criteria criteria = getSession().createCriteria(TransactionCore.class)
				.add(Restrictions.in("payexPaymentId", payexPaymentIds));
		return criteria.list();
	}

}
