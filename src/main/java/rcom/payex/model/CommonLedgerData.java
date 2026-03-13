package rcom.payex.model;

import java.util.Date;

public class CommonLedgerData {
	
	public String invoiceNumber = "";
	public String payexInvoiceNumber = "";
	public String invoiceType = "";
	public String invoiceExtraInfo = "";
	public String invoiceInfo = "";	
	public int creditDays = 0;	
	public double invoiceAmount = 0d;
	public double taxableValue = 0d;	
	public double outstandingAmount = 0d;
	public Date invoiceDate ;
	public Date dueDate ;
	public String divisionCode;
}
