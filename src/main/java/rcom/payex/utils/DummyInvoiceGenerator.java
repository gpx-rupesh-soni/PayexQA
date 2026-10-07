package rcom.payex.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import rcom.payex.model.CentralInvoices;

public class DummyInvoiceGenerator {

	private static final int[] CPG_INVOICE_CREDIT_OPTIONS = { 30, 40, 45 };
	private static final int INVOICE_DATE_MAX_DAYS_BACK = 5;

	private static final Random RANDOM = new Random();

	public static List<CentralInvoices> generateDummyInvoices(String payexCpgCode, String payexDistributorCode,
			String distributorName, String payexRetailerCode, String retailerCode, String divCode, String docCode,
			Integer viewType, String invoiceSettings, double minAmt, double maxAmt, List<String> areaCodes,
			int docCount) {

		SimpleDateFormat invoiceInfoBillDateFormat = new SimpleDateFormat("dd-MM-yy");

		List<CentralInvoices> dummyInvoices = new ArrayList<>();

		for (int i = 0; i < docCount; i++) {

			CentralInvoices inv = new CentralInvoices();

			inv.setPAYEX_CPG_CODE(payexCpgCode);
			inv.setPAYEX_DISTRIBUTOR_CODE(payexDistributorCode);
			inv.setDISTRIBUTOR_NAME(distributorName);
			inv.setPAYEX_RETAILER_CODE(payexRetailerCode);
			inv.setRETAILER_CODE(retailerCode);
			inv.setRETAILER_TAG(retailerCode);
			inv.setDIV_CODE(divCode);
			inv.setDOC_CODE(docCode);
			inv.setVIEW_TYPE(viewType);
			inv.setINVOICE_SETTINGS(invoiceSettings);

			Calendar invoiceDateCal = Calendar.getInstance();
			invoiceDateCal.add(Calendar.DATE, -RANDOM.nextInt(INVOICE_DATE_MAX_DAYS_BACK + 1));
			Date cpgInvoiceDate = invoiceDateCal.getTime();
			int financialYear = invoiceDateCal.get(Calendar.YEAR);

			String cpgInvoiceNumber = "" + randomUpperLetter() + randomUpperLetter()
					+ (financialYear%100) + String.format("%06d", RANDOM.nextInt(1000000));

			String payexInvoiceNumber = payexDistributorCode + "-" + retailerCode + "-" + cpgInvoiceNumber + "-"
					+ (financialYear%100) + "I";

			int cpgInvoiceCredit = CPG_INVOICE_CREDIT_OPTIONS[RANDOM.nextInt(CPG_INVOICE_CREDIT_OPTIONS.length)];

			Calendar dueDateCal = (Calendar) invoiceDateCal.clone();
			dueDateCal.add(Calendar.DATE, cpgInvoiceCredit);

			double cpgInvoiceAmount = Math.round(minAmt + RANDOM.nextDouble() * (maxAmt - minAmt));
			double taxableAmount = Math.round(cpgInvoiceAmount * 0.84);

			String areaCode = areaCodes.get(RANDOM.nextInt(areaCodes.size()));

			inv.setCPG_INVOICE_NUMBER(cpgInvoiceNumber);
			inv.setFINANCIAL_YEAR(financialYear);
			inv.setPAYEX_INVOICE_NUMBER(payexInvoiceNumber);
			inv.setCPG_INVOICE_CREDIT(cpgInvoiceCredit);
			inv.setINVOICE_INFO_EXTRA(areaCode + "#" + divCode);

			inv.setCPG_INCVOICE_DATE(cpgInvoiceDate);
			inv.setDATE_TO_DUE(dueDateCal.getTime());
			inv.setCPG_INVOICE_AMOUNT(cpgInvoiceAmount);
			inv.setTOTAL_AMOUNT_DUE(cpgInvoiceAmount);
			inv.setTAXABLE_AMOUNT(taxableAmount);
			inv.setINVOICE_INFO(cpgInvoiceNumber + "&" + invoiceInfoBillDateFormat.format(cpgInvoiceDate) + "&"
					+ cpgInvoiceAmount);

			Timestamp createDate = new Timestamp(System.currentTimeMillis());
			inv.setCREATE_DATE(createDate);
			inv.setUPDATE_DATE(createDate);
			inv.setDATE_TO_PRESENT(createDate);

			dummyInvoices.add(inv);
		}

		return dummyInvoices;
	}

	private static char randomUpperLetter() {
		return (char) ('A' + RANDOM.nextInt(26));
	}

}
