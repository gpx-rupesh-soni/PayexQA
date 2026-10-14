package rcom.payex.utils;

import java.io.File;

public class ApplicationConstant {

	public static final Integer DEBIT_NOTE_INVOICE_TYPE = 4;
	public static final Integer INV_INVOICE_TYPE = 0;
	
	// built on each call, so GPXFILEDIR is read after Spring has set it in AppConfig
	public static String getRootDir() {
		if (AppConfig.GPXFILEDIR == null || AppConfig.GPXFILEDIR.trim().isEmpty()) {
			throw new IllegalStateException("GPXFILEDIR is not configured in config.properties");
		}
		return AppConfig.GPXFILEDIR.trim() + "GLobal PayEx File Processing/";
	}

	public static String getInvoiceDataDir() {
		return getRootDir() + File.separatorChar + "InvoiceData";
	}

	// view file folder and extension, indexed by VIEW_TYPE
	public static final String[] invoiceFolders = new String[] { "", "InvoiceFilesImg", "InvoiceFilesHtml", "InvoiceFilesPdf", "InvoiceFilesUrl" };
	public static final String[] creditNoteFolders = new String[] { "", "CreditNoteImg", "CreditNoteHtml", "CreditNotePdf", "CreditNoteUrl" };
	public static final String[] filExtensions = new String[] { "", ".png", ".html", ".pdf", ".txt" };
}
