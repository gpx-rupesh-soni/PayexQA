package rcom.payex.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import rcom.payex.model.CentralInvoices;
import rcom.payex.model.CommonLedgerData;
import rcom.payex.model.CommonLedgerMasterData;
import rcom.payex.model.PAYEX_CENTRAL_INVOICES;

public class CentralInvoiceUtil {
	
	Logger logger = LogManager.getLogger(CentralInvoiceUtil.class);
	
	public static CentralInvoices getCentralInvoice(String retailerCode, String payexRetailerCode, String payexCpgCode,
			String payexDistributorCode, Integer routeCode, Integer agentCode, String invoiceNumber,
			String payexInvoiceNumber, long invoiceDate, long dueDate, double invoiceAmount, Double taxableValue,
			Double outstandingAmount, String distName,
			String invoiceInfo, String invoiceSetting, String divisionCode, String invoiceExtraInfo, Integer inViewType,
			Integer dnViewType, String dnSetting, int invoiceType, int hasMakerChecker, int creditDays,Integer financialYear) {

		DateFormat invoiceInfoBillDateFormat = new SimpleDateFormat("dd-MM-yy");

		CentralInvoices payexCentralInvoice = new CentralInvoices();

		payexCentralInvoice.setRETAILER_CODE(retailerCode);
		payexCentralInvoice.setRETAILER_TAG(retailerCode);
		payexCentralInvoice.setPAYEX_CPG_CODE(payexCpgCode);
		payexCentralInvoice.setPAYEX_DISTRIBUTOR_CODE(payexDistributorCode.trim());
		payexCentralInvoice.setPAYEX_RETAILER_CODE(payexRetailerCode);
		payexCentralInvoice.setROUTE_CODE(routeCode);
		payexCentralInvoice.setAGENT_CODE(agentCode);
		payexCentralInvoice.setCPG_INVOICE_NUMBER(invoiceNumber);
		payexCentralInvoice.setPAYEX_INVOICE_NUMBER(payexInvoiceNumber);
		payexCentralInvoice.setCPG_INCVOICE_DATE(new Timestamp(invoiceDate));
		payexCentralInvoice.setDATE_TO_PRESENT(new Timestamp(new Date().getTime()));
		if (dueDate == 0)
			payexCentralInvoice.setDATE_TO_DUE((new Timestamp(invoiceDate)));
		else
			payexCentralInvoice.setDATE_TO_DUE(new Timestamp(dueDate));
		payexCentralInvoice.setCPG_INVOICE_AMOUNT(invoiceAmount);
		payexCentralInvoice.setTAXABLE_AMOUNT(taxableValue);
		payexCentralInvoice.setTOTAL_AMOUNT_DUE(outstandingAmount);
		payexCentralInvoice.setCREATE_DATE(new Timestamp(new Date().getTime()));
		payexCentralInvoice.setUPDATE_DATE(new Timestamp(new Date().getTime()));
		payexCentralInvoice.setDISTRIBUTOR_NAME(distName);
		
		payexCentralInvoice.setINVOICE_TYPE(invoiceType);
		payexCentralInvoice.setINVOICE_SETTINGS(invoiceSetting);
		payexCentralInvoice.setFINANCIAL_YEAR(financialYear);
		
		
		payexCentralInvoice.setDIV_CODE(divisionCode);
		payexCentralInvoice.setINVOICE_INFO_EXTRA(invoiceExtraInfo);

		if (payexCentralInvoice.getINVOICE_TYPE() == 0) {
			payexCentralInvoice.setVIEW_TYPE(inViewType);
		} else {
			payexCentralInvoice.setVIEW_TYPE(dnViewType);
		}

		if ( (payexCpgCode == "CPGVGUARD") ) {
			payexCentralInvoice.setHAS_MAKER_CHECKER(hasMakerChecker);
		}

		if ( payexCpgCode.equals("CPGVGUARD") || payexCpgCode.equals("CPGBRGSTN")) {
			payexCentralInvoice.setCPG_INVOICE_CREDIT(creditDays);
		}

		return payexCentralInvoice;

	}
	
	
	public void getLedgerData(CommonLedgerData ledgerData, CommonLedgerMasterData ledgerMasterData  ) {
		
		
		Integer routeCode = 0;
		Integer agentCode = 0;
		
		PAYEX_CENTRAL_INVOICES payexCentralInvoice;

		
		String invoiceExtraInfo = "";
		String invoiceInfo = "";
		

		
		String sequenceFlag = "";
		Integer activeFlag;
		
		//????invoiceExtraInfo = billFile.getINTERNAL_REF_NO();
		//????invoiceExtraInfo = billFile.getINTERNAL_REF_NO();
		//????invoiceInfo = billFile.getINVOICE_NUMBER() + "&"+ Utils.billDateFmt.format(billFile.getINVOICE_DATE()) + "&" + invoiceAmount;
		//invoiceSetting = distributorSetting.getINVOICE_SETTINGS();
		//dnSetting = distributorSetting.getDN_SETTINGS();

		

		routeCode = 0;
		agentCode = 0;
		//inViewType = distributorSetting.getIN_VIEW_TYPE();
		//dnViewType = distributorSetting.getDN_VIEW_TYPE();		

		payexCentralInvoice = utilCenteralService(ledgerMasterData.retailerCode, ledgerMasterData.payexRetailerCode,
				ledgerMasterData.payexCpgCode, ledgerMasterData.payexDistributorCode, routeCode, agentCode,
				ledgerData.invoiceNumber, ledgerData.payexInvoiceNumber, ledgerData.invoiceDate.getTime(), ledgerData.dueDate.getTime(), ledgerData.invoiceAmount, ledgerData.taxableValue,
				ledgerData.outstandingAmount, ledgerMasterData.distName, invoiceInfo, ledgerMasterData.invoiceSetting, ledgerData.divisionCode, invoiceExtraInfo,
				ledgerMasterData.inViewType, ledgerMasterData.dnViewType, ledgerMasterData.dnSetting, ledgerData.invoiceType, 0, ledgerMasterData.creditDays);
		payexCentralInvoice.setSEQUENCE_FLAG(sequenceFlag);
		///????payexCentralInvoice.setAREA_CODE(billFile.getCD_STATUS());
		///????payexCentralInvoice.setTcs(billFile.getTcs());

		//?????if (!isPartialPaidInvoice && billFile.getINVOICE_TYPE().equalsIgnoreCase("U")) {
		//?????	payexCentralInvoice.setACTIVE_FLAG(2);
		//?????}
		//?????payexInvoiceNumberToPayexCentralInvoicePojoMap.put(payexInvoiceNumber, payexCentralInvoice);
	}
	
	
	public static PAYEX_CENTRAL_INVOICES utilCenteralService(String retailerCode, String payexRetailerCode, String payexCpgCode,
			String payexDistributorCode, Integer routeCode, Integer agentCode, String invoiceNumber,
			String payexInvoiceNumber, long invoiceDate, long dueDate, double invoiceAmount, double taxableValue,
			double outstandingAmount, String distName,
			String invoiceInfo, String invoiceSetting, String divisionCode, String invoiceExtraInfo, Integer inViewType,
			Integer dnViewType, String dnSetting, String invoiceType, int hasMakerChecker, int creditDays) {
		
		return utilCenteralService(retailerCode, payexRetailerCode, payexCpgCode, payexDistributorCode, routeCode, agentCode, invoiceNumber, 
				payexInvoiceNumber, invoiceDate, dueDate, invoiceAmount, taxableValue, outstandingAmount, distName, invoiceInfo, invoiceSetting, 
				divisionCode, invoiceExtraInfo, inViewType, dnViewType, dnSetting, invoiceType, hasMakerChecker, creditDays, 0);
	}
	
	public static PAYEX_CENTRAL_INVOICES utilCenteralService(String retailerCode, String payexRetailerCode, String payexCpgCode,
			String payexDistributorCode, Integer routeCode, Integer agentCode, String invoiceNumber,
			String payexInvoiceNumber, long invoiceDate, long dueDate, double invoiceAmount, double taxableValue,
			double outstandingAmount, String distName,
			String invoiceInfo, String invoiceSetting, String divisionCode, String invoiceExtraInfo, Integer inViewType,
			Integer dnViewType, String dnSetting, String invoiceType, int hasMakerChecker, int creditDays,int financialYear) {

		DateFormat invoiceInfoBillDateFormat = new SimpleDateFormat("dd-MM-yy");

		PAYEX_CENTRAL_INVOICES payexCentralInvoice = new PAYEX_CENTRAL_INVOICES();

		payexCentralInvoice.setRETAILER_CODE(retailerCode);
		payexCentralInvoice.setRETAILER_TAG(retailerCode);
		payexCentralInvoice.setPAYEX_CPG_CODE(payexCpgCode);
		payexCentralInvoice.setPAYEX_DISTRIBUTOR_CODE(payexDistributorCode.trim());
		payexCentralInvoice.setPAYEX_RETAILER_CODE(payexRetailerCode);
		payexCentralInvoice.setROUTE_CODE(routeCode);
		payexCentralInvoice.setAGENT_CODE(agentCode);
		payexCentralInvoice.setCPG_INVOICE_NUMBER(invoiceNumber);
		payexCentralInvoice.setPAYEX_INVOICE_NUMBER(payexInvoiceNumber);
		payexCentralInvoice.setCPG_INCVOICE_DATE(new Timestamp(invoiceDate));
		payexCentralInvoice.setDATE_TO_PRESENT(new Timestamp(new Date().getTime()));
		if (dueDate == 0)
			payexCentralInvoice.setDATE_TO_DUE((new Timestamp(invoiceDate)));
		else
			payexCentralInvoice.setDATE_TO_DUE(new Timestamp(dueDate));
		payexCentralInvoice.setCPG_INVOICE_AMOUNT(invoiceAmount);
		payexCentralInvoice.setTAXABLE_AMOUNT(taxableValue);
		payexCentralInvoice.setTOTAL_AMOUNT_DUE(outstandingAmount);
		payexCentralInvoice.setCREATE_DATE(new Timestamp(new Date().getTime()));
		payexCentralInvoice.setUPDATE_DATE(new Timestamp(new Date().getTime()));
		payexCentralInvoice.setDISTRIBUTOR_NAME(distName);
		
		if(financialYear>0) {
		if (invoiceType.equalsIgnoreCase("I") || invoiceType.equalsIgnoreCase("C") || invoiceType.equalsIgnoreCase("D"))
			payexCentralInvoice.setFINANCIAL_YEAR(financialYear);
		}
		payexCentralInvoice.setINVOICE_INFO(
				invoiceNumber + "&" + invoiceInfoBillDateFormat.format(invoiceDate) + "&" + invoiceAmount);
		if (invoiceType.equalsIgnoreCase("D")) {
			payexCentralInvoice.setINVOICE_SETTINGS(dnSetting);
			payexCentralInvoice.setINVOICE_TYPE(ApplicationConstant.DEBIT_NOTE_INVOICE_TYPE);
		} else {
			payexCentralInvoice.setINVOICE_TYPE(ApplicationConstant.INV_INVOICE_TYPE);
			payexCentralInvoice.setINVOICE_SETTINGS(invoiceSetting);
		}
		payexCentralInvoice.setDIV_CODE(divisionCode);
		payexCentralInvoice.setINVOICE_INFO_EXTRA(invoiceExtraInfo);

		if (payexCentralInvoice.getINVOICE_TYPE() == 0) {
			payexCentralInvoice.setVIEW_TYPE(inViewType);
		} else {
			payexCentralInvoice.setVIEW_TYPE(dnViewType);
		}

		if ( (payexCpgCode == "CPGVGUARD") ) {
			payexCentralInvoice.setHAS_MAKER_CHECKER(hasMakerChecker);
		}

		if ( payexCpgCode.equals("CPGVGUARD") || payexCpgCode.equals("CPGBRGSTN")) {
			payexCentralInvoice.setCPG_INVOICE_CREDIT(creditDays);
		}

		return payexCentralInvoice;

	}

}
