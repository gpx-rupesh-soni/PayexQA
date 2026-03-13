package rcom.payex.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import rcom.payex.model.CentralInvoices;
import rcom.payex.model.InvoiceTransaction;
import rcom.payex.model.RetailerCreditNote;
import rcom.payex.model.RetailerVacc;
import rcom.payex.model.TransactionCore;
import rcom.payex.model.TxnApiLog;

/** InvoiceTransactionDao */
public interface InvoiceTransactionDao {

	public JSONObject updateNachStatus(List<Long> passPpids, List<Long> failedPpids);
	public void savePayexCentralInvoice(CentralInvoices payexCentralInvoice);
	public int savePayexCentralInvoices(Collection<CentralInvoices> payexCentralInvoiceList);
	public List<CentralInvoices> getInvoicesDetails(String payexCpgCode, String payexRetailerCode,
			String cpgInvoiceNumber);

	public List<CentralInvoices> getInvoicesOfTxn(Long ppid);
	public List<RetailerCreditNote> getCreditNoteOfTxn(Long ppid);

	public CentralInvoices getInvoicesDetails(Long ppid);


	public RetailerVacc getRetailerVaccMaster(String payexCpgCode, String cpgRetailerCode);

	public InvoiceTransaction getInvoiceByPayexPaymentId(Long payexPaymentId);

	public List<TransactionCore> getTransactions(String payexCpgCode, String payexRetailerCode, int status, Long payexPaymentId);
	public TransactionCore getTransaction(Long payexPaymentId);

	public JSONObject updateTxnAsPending(long ppid);
	public JSONObject deleteTxn(long ppid);
	public JSONObject deleteRetailerData(String payexRetailerCode);

	public List<TxnApiLog> getTxnApiLog(String payexRetailerCode, Date createDate);
	public TxnApiLog getTxnApiLog(Long payexPaymentId);
	public List<Object[]> fetchPayexCentralInvoices(String invId);
	public List<TransactionCore> getTransactions(List<Long> payexPaymentIds);
}
