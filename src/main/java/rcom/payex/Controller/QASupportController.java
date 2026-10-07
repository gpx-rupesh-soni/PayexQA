package rcom.payex.Controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

import org.apache.http.util.TextUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import rcom.payex.model.CentralInvoices;
import rcom.payex.model.Retailer;
import rcom.payex.model.TransactionCore;
import rcom.payex.service.CommonService;
import rcom.payex.service.InvoiceService;
import rcom.payex.service.LedgerService;
import rcom.payex.service.QASupportService;
import rcom.payex.service.RetailerService;
import rcom.payex.utils.Utils;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value = "qasupport/")
public class QASupportController extends HttpServlet {

	Logger logger = LogManager.getLogger(getClass());

	private static final long serialVersionUID = 1L;

	String mainLayout = "master";

	Gson gson = new Gson();

	@Autowired
	RetailerService reatilerService;

	@Autowired
	CommonService commonService;

	@Autowired
	LedgerService ledgerService;

	@Autowired
	InvoiceService invoiceService;

	@Autowired
	QASupportService qaSupportService;


	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 */

	public boolean isLoged(HttpSession session) {
		return session.getAttribute("user") != null;
	}

	public String getUserRole(HttpSession session) {
		return (String) session.getAttribute("role");
	}

	public String getUserOperation(HttpSession session) {
		return (String) session.getAttribute("op");
	}

	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String retailerIndex(Locale locale, Model model, HttpSession session) {
		if (!isLoged(session))
			return "redirect:../login";
		model.addAttribute("layout", "report/index");
		return mainLayout;
	}

	@RequestMapping(value = "/update_retailer", method = RequestMethod.GET)
	public String updateRetailerGet(Model model, HttpSession session,
			@RequestParam(value = "payex_retailer_code", required = false, defaultValue = "") String payexRetailerCode) {
		if (!isLoged(session))return "redirect:../login";

		Retailer retailer = reatilerService.getRetailer(payexRetailerCode);
		model.addAttribute("retailer", retailer);
		model.addAttribute("layout", "retailer/edit_retailer");
		return mainLayout;
	}

	@PostMapping("/searchRetailer")
	@ResponseBody
	public Map<String, Object> searchRetailer(
	        @RequestParam(required = false) String payex_retailer_code,
	        @RequestParam(required = false) String payex_cpg_code,
	        @RequestParam(required = false) String cpg_retailer_code) {

	    Map<String, Object> res = new HashMap<>();
	    Retailer ret = null;
	    // Your search logic here
	    if (payex_retailer_code != null && !payex_retailer_code.isEmpty()) {
	        res.put("searchType", "payex_retailer_code");
	        ret = reatilerService.getRetailerWithOtherDetails(payex_retailer_code);

	    }else if (cpg_retailer_code != null && !cpg_retailer_code.isEmpty()) {

	    	res.put("searchType", "cpg_retailer_code");
	    	ret = reatilerService.getRetailerByCpg(payex_cpg_code, cpg_retailer_code);
	    }else {
	    	res.put("status", "FAILED");
        	res.put("message", "Invalid search data");
	    }

	    if(ret==null) {
	    	res.put("status", "FAILED");
	    }else {
	    	res.put("status", "SUCCESS");
	    	res.put("ret",ret);
	    }
	    return res;
	}


	@RequestMapping(value = "/update_retailer", method = RequestMethod.POST)
	@ResponseBody
	public String updateRetailerPost(HttpSession session,
			@RequestParam(value = "payex_retailer_code", required = true, defaultValue = "") String payexRetailerCode,
			@RequestParam(value = "action", required = true, defaultValue = "") String action,
			@RequestParam(value = "cpgTag", required = false, defaultValue = "") List<String> cpgTag,
			@RequestParam(value = "new_owner", required = false, defaultValue = "") String newOwner,
			@RequestParam(value = "new_mobile", required = false, defaultValue = "") String newMobile,
			@RequestParam(value = "new_email", required = false, defaultValue = "") String newEmail,
			@RequestParam(value = "new_address", required = false, defaultValue = "") String newAddress
			) {
		JSONObject res = new JSONObject();
		System.out.println("payexRetailerCode : "+payexRetailerCode);
		System.out.println("action : "+action);
		System.out.println("cpgTag : "+cpgTag);

		Retailer retailer = new Retailer();
		retailer.setPayex_RETAILER_CODE(payexRetailerCode);
		retailer.setMobile_NUMBER(newMobile);
		retailer.setKey_PERSON(newOwner);
		retailer.setEmail_ID(newEmail);
		retailer.setAddress(newAddress);
		retailer.setCpgTag(cpgTag);

		try {
			res.put("status", "SUCCESS");
			res.put("data", reatilerService.updateRetailer(action,retailer));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res.toString();
	}

	@RequestMapping(value = "/txn-info", method = RequestMethod.GET)
	public String txnDetailsGet(Model model, HttpSession session,
			@RequestParam(value = "txnType", required = false) String txnType,
			@RequestParam(value = "payexCpgCode", required = false) String payexCpgCode,
			@RequestParam(value = "payexRetailerCode", required = false) String payexRetailerCode,
			@RequestParam(value = "payexPaymentId", required = false) Long payexPaymentId) {

		//if (!isLoged(session))return "redirect:../login";

		System.out.println("type : "+txnType);
		if(payexPaymentId!=null && payexPaymentId>0)
			model.addAttribute("ppid", payexPaymentId);

		if(StringUtils.hasText(txnType) || payexPaymentId!=null){
			System.out.println("type : "+txnType);
			Map<String, Object> txnInfo = commonService.getTransactionInfo(payexPaymentId);
			model.addAttribute("txnInfo", txnInfo);
		}
		//commonService.processTxnStatus(ppi);

		model.addAttribute("layout", "qasupport/txnInfo");

		return mainLayout;
	}

	@RequestMapping(value = "/txn-info", method = RequestMethod.POST)
	@ResponseBody
	public Map txnDetailsPost(
			@RequestParam(value = "action", required = false) String action,
			@RequestParam(value = "payexCpgCode", required = false) String payexCpgCode,
			@RequestParam(value = "payexRetailerCode", required = false) String payexRetailerCode,
			@RequestParam(value = "payexPaymentId", required = false) Long payexPaymentId) {

		//if (!isLoged(session))return "redirect:../login";

		System.out.println("action : "+action);
		Map<String, Object> txnInfo = commonService.getTransactionOtherInfo(action, payexPaymentId);
		return txnInfo;
	}

	@RequestMapping(value = "/update-nach", method = RequestMethod.GET)
	public String updateNachGet(Locale locale,Model model, HttpSession session,
			@RequestParam(value = "date", required = false, defaultValue = "") String date) {

		if (!isLoged(session))return "redirect:../login";
		model.addAttribute("layout", "qasupport/update_nach");
		return mainLayout;
	}

	@RequestMapping(value = "/update-nach", method = RequestMethod.POST)
	@ResponseBody
	public String updateNachPost(@RequestBody Map<String,String> csvData) {
	    commonService.processNachStatus(csvData);
	    return "SUCCESS";
	}

	@RequestMapping(value = "/update-txn", method = RequestMethod.GET)
	public String updateTxnGet(Model model, HttpSession session,
			@RequestParam(value = "txnType", required = false) String txnType,
			@RequestParam(value = "payexCpgCode", required = false) String payexCpgCode,
			@RequestParam(value = "payexRetailerCode", required = false) String payexRetailerCode,
			@RequestParam(value = "payexPaymentId", required = false) Long payexPaymentId) {

		System.out.println("nano second : "+System.nanoTime());
		System.out.println("mili second : "+System.currentTimeMillis());
		if (!isLoged(session))return "redirect:../login";

		System.out.println("type : "+txnType);

		if(StringUtils.hasText(txnType) || payexPaymentId!=null){
			System.out.println("type : "+txnType);
			List<TransactionCore> txns = commonService.getTransactions(payexCpgCode,payexRetailerCode,txnType, payexPaymentId);
			System.out.println("txns.size : "+txns.size());
			model.addAttribute("txns", txns);
		}
		//commonService.processTxnStatus(ppi);
		model.addAttribute("layout", "qasupport/txnUpdate");

		return mainLayout;
	}

	@RequestMapping(value = "/update-txn", method = RequestMethod.POST)
	@ResponseBody
	public String updateTxnPost(@RequestBody Map<String,String> req) {
		System.out.println("update-txn post called...");

		JSONObject res = commonService.updateTransaction(req);

	    return res.toString();
	}
	
	@RequestMapping(value = "/ret-inv", method = RequestMethod.GET)
	public String retInvoicesGet(Locale locale,Model model, HttpSession session,
			@RequestParam(value = "txnType", required = false) String txnType,
			@RequestParam(value = "payexCpgCode", required = false) String payexCpgCode,
			@RequestParam(value = "cpgInvoiceNumber", required = false) String cpgInvoiceNumber,
			@RequestParam(value = "payexRetailerCode", required = false) String payexRetailerCode) {

		if (!isLoged(session))return "redirect:../login";

		System.out.println("type : "+txnType);

		if(StringUtils.hasText(txnType) || cpgInvoiceNumber!=null){
			System.out.println("type : "+txnType);
			List<CentralInvoices> txns = ledgerService.getInvoicesDetails(payexCpgCode,payexRetailerCode,cpgInvoiceNumber);
			System.out.println("txns.size : "+txns.size());
			model.addAttribute("txns", txns);
		}

		model.addAttribute("docType", txnType);
		model.addAttribute("payexCpgCode", payexCpgCode);
		model.addAttribute("payexRetailerCode", payexRetailerCode);
		model.addAttribute("cpgInvoiceNumber", cpgInvoiceNumber);

		//commonService.processTxnStatus(ppi);
		model.addAttribute("layout", "qasupport/invUpdate");
		return mainLayout;
	}
	
	@RequestMapping(value = "/ret-inv", method = RequestMethod.POST)
	@ResponseBody
	public String retInvoicesPost(Locale locale,Model model, HttpSession session,
			@RequestParam(value = "action", required = true, defaultValue = "") String action,
			@RequestParam(value = "pcc", required = false, defaultValue = "") String payexCpgCode,
			@RequestParam(value = "prc", required = false, defaultValue = "") String payexRetailerCode,
			@RequestParam(value = "invno", required = false, defaultValue = "") String invNo,
			@RequestParam(value = "pti", required = false, defaultValue = "") Long payexTransactionID) {

		JSONObject res = new JSONObject();
		System.out.println("action : "+action+" , payexCpgCode : "+payexCpgCode+" , payexRetailerCode : "+payexRetailerCode+" , invNo : "+invNo);
		String msg = null;
		
		switch (action) {
		case "add-part-outstanding":
			if(payexTransactionID!=null)
				msg = commonService.addPartialInvoice(payexTransactionID);
			break;

		default:
			break;
		}
		
		try {
			res.put("msg", msg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return res.toString();
	}

	@RequestMapping(value = "/neft-payment", method = RequestMethod.GET)
	public String neftPaymentGet(Locale locale,Model model, HttpSession session,
			@RequestParam(value = "date", required = false, defaultValue = "") String date) {

		if (!isLoged(session))return "redirect:../login";
		model.addAttribute("layout", "qasupport/update_nach");
		return mainLayout;
	}

	@RequestMapping(value = "/neft-payment", method = RequestMethod.POST)
	public String neftPaymentPost(Locale locale,Model model, HttpSession session,
			@RequestParam(value = "date", required = false, defaultValue = "") String date) {

		if (!isLoged(session))return "redirect:../login";
		model.addAttribute("layout", "qasupport/update_nach");
		return mainLayout;
	}

	@RequestMapping(value = "/update-inv", method = RequestMethod.GET)
	@ResponseBody
	public String updateInv(@RequestParam long ppid) {

		System.out.println("update-inv called... >> "+ppid);
		commonService.updateTransactionDoc(ppid);
		return "Done";
	}	

	@RequestMapping(value = "/genInvData", method = RequestMethod.GET)
	public String generateInvDataGet(Model model, HttpSession session,
			@RequestParam(value = "date", required = false, defaultValue = "") String date) {

		//if (!isLoged(session))return "redirect:../login";
		model.addAttribute("layout", "qasupport/genInvData");
		return mainLayout;
	}

	@RequestMapping(value = "/genInvData", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> generateInvDataPost(Model model,
			@RequestParam(value = "docType", required = true) String docType,
			@RequestParam(value = "payexCpgCode", required = true) String payexCpgCode,
			@RequestParam(value = "retailerCode", required = true) String retailerCode,
			@RequestParam(value = "divCode", required = false) String divCode,
			@RequestParam(value = "docCode", required = false) String docCode,
			@RequestParam(value = "maxAmt", required = false, defaultValue = "0") double maxAmt,
			@RequestParam(value = "minAmt", required = false, defaultValue = "0") double minAmt,
			@RequestParam(value = "docCount", required = false, defaultValue = "0") int docCount) {

		//if (!isLoged(session))return "redirect:../login";
		System.out.println("docType : "+docType+" , payexCpgCode : "+payexCpgCode+" , retailerCode : "+retailerCode
				+" , maxAmt : "+maxAmt+" , minAmt : "+minAmt+" , docCount : "+docCount);

		return qaSupportService.generateDummyInvoices(payexCpgCode,retailerCode, divCode, docCode, minAmt, maxAmt, docCount);
	}
	
	@RequestMapping(value = "/purgeData", method = RequestMethod.GET)
	public String purgeDataGet(Model model, HttpSession session,
			@RequestParam(value = "date", required = false, defaultValue = "") String date) {

		if (!isLoged(session))return "redirect:../login";
		model.addAttribute("layout", "qasupport/purgeData");
		return mainLayout;
	}

	@RequestMapping(value = "/purgeData", method = RequestMethod.POST)
	@ResponseBody
	public String purgeDataPost(Model model, HttpSession session,
			@RequestParam(value = "docType", required = true) String docType,
			@RequestParam(value = "payexCpgCode", required = false, defaultValue = "") String payexCpgCode,
			@RequestParam(value = "retailerCode", required = false, defaultValue = "") String retailerCode,
			@RequestParam(value = "payexRetailerCode", required = false, defaultValue = "") String payexRetailerCode) {

		if (!isLoged(session))return "redirect:../login";
		logger.info("docType : "+docType+" , payexCpgCode : "+payexCpgCode+" , retailerCode : "+retailerCode+" , payexRetailerCode : "+payexRetailerCode);

		JSONObject res = new JSONObject();
		try {
			// payexCpgCode and (retailerCode or payexRetailerCode) are must
			if (payexCpgCode.trim().isEmpty()) {
				res.put("status", "FAILED");
				res.put("message", "payexCpgCode is required");
				return res.toString();
			}
			if (retailerCode.replace(",", "").trim().isEmpty() && payexRetailerCode.replace(",", "").trim().isEmpty()) {
				res.put("status", "FAILED");
				res.put("message", "retailerCode or payexRetailerCode is required");
				return res.toString();
			}
			res.put("status", "SUCCESS");
			res.put("data", qaSupportService.purgeData(docType, payexCpgCode, retailerCode, payexRetailerCode));
		} catch (JSONException e) {
			logger.info(Utils.getStackTrace(e));
		}

		return res.toString();
	}

	@RequestMapping(value = "/validateCD", method = RequestMethod.GET)
	@ResponseBody
	public String validatePartPayCD(
			@RequestParam(value = "invID", required = false, defaultValue = "0") String invID) throws JSONException {
		System.out.println("invID : "+invID);
		commonService.validatePartPayCD(invID);
		return "";
	}

	@RequestMapping(value = "/export/ledger", method = RequestMethod.GET)
	public String exportLedger(Locale locale,Model model, HttpSession session,
			@RequestParam(value = "ppid", required = false, defaultValue = "") String ppids,
			@RequestParam(value = "invid", required = false, defaultValue = "") String invids,
			@RequestParam(value = "cnid", required = false, defaultValue = "") String cnids) {

		if (!isLoged(session))return "redirect:../login";

		if(!TextUtils.isBlank(ppids)){
			System.out.println("ppids : "+ppids);
			List<TransactionCore> txns = invoiceService.getTransactions(ppids);
			System.out.println("txns.size : "+txns.size());
			model.addAttribute("txns", txns);
		}

		if(!TextUtils.isBlank(invids)){
			System.out.println("invids : "+invids);
			List<TransactionCore> invs = invoiceService.getTransactions(ppids);
			System.out.println("invs.size : "+invs.size());
			model.addAttribute("invs", invs);
		}

		if(!TextUtils.isBlank(cnids)){
			System.out.println("cnids : "+cnids);
			List<TransactionCore> cns = invoiceService.getTransactions(ppids);
			System.out.println("cns.size : "+cns.size());
			model.addAttribute("cns", cns);
		}

		model.addAttribute("layout", "qasupport/exportData");
		return mainLayout;
	}

	@RequestMapping(value = "/export/ledger", method = RequestMethod.POST)
	@ResponseBody
	public String exportLedgerPost(Locale locale,Model model, HttpSession session,
			@RequestParam(value = "action", required = true, defaultValue = "") String action,
			@RequestParam(value = "data", required = false, defaultValue = "") String data) {

		logger.info("action : "+action);
		logger.info("data : "+data);

		return qaSupportService.exportLedgerData(action, data);

		//return mainLayout;
	}



}
