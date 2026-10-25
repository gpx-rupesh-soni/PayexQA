package rcom.payex.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ExportQueryGeneratorService {
	
	Logger logger = LogManager.getLogger(getClass());
	
	public List<String> exportQueriesForVGFifoPullOutstanding(List<String> buyerCodes, boolean withMaster) {	
		
		List<String> queries = new ArrayList<String>();
		
		if(withMaster) {
			// list ret_dist
			queries.add("SELECT * FROM RET_DIST_MAP_VGUARD WHERE PAYEX_RETAILER_CODE IN("+inClauseByList(buyerCodes)+")");
			
			// list of retailer master
			queries.add("SELECT * FROM RETAILER_MASTER WHERE PAYEX_RETAILER_CODE IN("+inClauseByList(buyerCodes)+")");
			
			// list of retailer setting
			queries.add("SELECT * FROM RETAILER_SETTING WHERE PAYEX_RETAILER_CODE IN("+inClauseByList(buyerCodes)+")");
		}
		
		// list of all open CNs
		queries.add("SELECT * FROM RETAILER_CREDIT_NOTE WHERE PAYEX_CPG_CODE = 'CPGVGUARD' AND PAYEX_RETAILER_CODE IN("+inClauseByList(buyerCodes)+") AND CN_STATUS = 0");
		
		// list of all open INVs
		queries.add("SELECT * FROM PAYEX_CENTRAL_INVOICES WHERE PAYEX_CPG_CODE = 'CPGVGUARD' AND PAYEX_RETAILER_CODE IN("+inClauseByList(buyerCodes)+") AND INVOICE_TYPE IN(0,4) AND PROCESS_STATUS<3 AND ACTIVE_FLAG=0");
		
		// list of all partial paid INVs
		//queries.add("SELECT * FROM PAYEX_CENTRAL_INVOICES where PAYEX_CPG_CODE = 'CPGVGUARD' AND PAYEX_RETAILER_CODE IN("+inClauseByList(buyerCodes)+") AND INVOICE_TYPE IN(0,4) AND PROCESS_STATUS = 80 AND (PAYEX_RETAILER_CODE, FINANCIAL_YEAR, CPG_INVOICE_NUMBER) in (SELECT PAYEX_RETAILER_CODE, FINANCIAL_YEAR, CPG_INVOICE_NUMBER FROM PAYEX_CENTRAL_INVOICES WHERE PAYEX_CPG_CODE = 'CPGVGUARD' AND PAYEX_RETAILER_CODE IN("+inClauseByList(buyerCodes)+") AND INVOICE_TYPE IN(0,4) AND PROCESS_STATUS<3 AND ACTIVE_FLAG=0)");
		String sql =
			    "SELECT pci.* " +
			    "FROM PAYEX_CENTRAL_INVOICES pci " +
			    "WHERE pci.PAYEX_CPG_CODE = 'CPGVGUARD' " +
			    "AND pci.INVOICE_TYPE IN (0,4) " +
			    "AND pci.PROCESS_STATUS = 80 " +
			    "AND PAYEX_RETAILER_CODE IN("+inClauseByList(buyerCodes)+") " +
			    "AND EXISTS ( " +
			    "    SELECT 1 " +
			    "    FROM PAYEX_CENTRAL_INVOICES pci2 " +
			    "    WHERE pci2.PAYEX_RETAILER_CODE = pci.PAYEX_RETAILER_CODE " +
			    "      AND pci2.FINANCIAL_YEAR = pci.FINANCIAL_YEAR " +
			    "      AND pci2.CPG_INVOICE_NUMBER = pci.CPG_INVOICE_NUMBER " +
			    "      AND pci2.PAYEX_CPG_CODE = 'CPGVGUARD' " +
			    "      AND pci2.INVOICE_TYPE IN (0,4) " +
			    "      AND pci2.PROCESS_STATUS < 3 " +
			    "      AND pci2.ACTIVE_FLAG = 0 " +
			    ") ";
		logger.info("exportQueriesForVGFifoPullOutstanding > sql : "+ sql);
		queries.add(sql);
		return queries;
	}
	
	public List<String> exportQueriesForVGFifoPullOutstanding(boolean withMaster) {	
		
		String inClause = "select distinct payex_retailer_code from retailer_credit_note where payex_cpg_code = 'CPGVGUARD'and cn_status = 0 and payex_retailer_code in (select payex_retailer_code from ret_dist_map_vguard where cn_auto_pull > 0)";
		List<String> queries = new ArrayList<String>();
		
		if(withMaster) {
			// list ret_dist
			queries.add("SELECT * FROM RET_DIST_MAP_VGUARD WHERE PAYEX_RETAILER_CODE IN("+inClause+")");
			
			// list of retailer master
			queries.add("SELECT * FROM RETAILER_MASTER WHERE PAYEX_RETAILER_CODE IN("+inClause+")");
			
			// list of retailer setting
			queries.add("SELECT * FROM RETAILER_SETTING WHERE PAYEX_RETAILER_CODE IN("+inClause+")");
		}
		
		// list of all open CNs
		queries.add("SELECT * FROM RETAILER_CREDIT_NOTE WHERE PAYEX_CPG_CODE = 'CPGVGUARD' AND PAYEX_RETAILER_CODE IN("+inClause+") AND CN_STATUS = 0");
		
		// list of all open INVs
		queries.add("SELECT * FROM PAYEX_CENTRAL_INVOICES WHERE PAYEX_CPG_CODE = 'CPGVGUARD' AND PAYEX_RETAILER_CODE IN("+inClause+") AND INVOICE_TYPE IN(0,4) AND PROCESS_STATUS<3 AND ACTIVE_FLAG=0");
		
		// list of all partial paid INVs
		//queries.add("SELECT * FROM PAYEX_CENTRAL_INVOICES where PAYEX_CPG_CODE = 'CPGVGUARD' AND PAYEX_RETAILER_CODE IN("+inClause+") AND INVOICE_TYPE IN(0,4) AND PROCESS_STATUS = 80 AND (PAYEX_RETAILER_CODE, FINANCIAL_YEAR, CPG_INVOICE_NUMBER) in (SELECT PAYEX_RETAILER_CODE, FINANCIAL_YEAR, CPG_INVOICE_NUMBER FROM PAYEX_CENTRAL_INVOICES WHERE PAYEX_CPG_CODE = 'CPGVGUARD' AND PAYEX_RETAILER_CODE IN("+inClause+") AND INVOICE_TYPE IN(0,4) AND PROCESS_STATUS<3 AND ACTIVE_FLAG=0)");
		
		String sql =
			    "SELECT pci.* " +
			    "FROM PAYEX_CENTRAL_INVOICES pci " +
			    "WHERE pci.PAYEX_CPG_CODE = 'CPGVGUARD' " +
			    "AND pci.INVOICE_TYPE IN (0,4) " +
			    "AND pci.PROCESS_STATUS = 80 " +
			    "AND EXISTS ( " +
			    "    SELECT 1 " +
			    "    FROM PAYEX_CENTRAL_INVOICES pci2 " +
			    "    WHERE pci2.PAYEX_RETAILER_CODE = pci.PAYEX_RETAILER_CODE " +
			    "      AND pci2.FINANCIAL_YEAR = pci.FINANCIAL_YEAR " +
			    "      AND pci2.CPG_INVOICE_NUMBER = pci.CPG_INVOICE_NUMBER " +
			    "      AND pci2.PAYEX_CPG_CODE = 'CPGVGUARD' " +
			    "      AND pci2.INVOICE_TYPE IN (0,4) " +
			    "      AND pci2.PROCESS_STATUS < 3 " +
			    "      AND pci2.ACTIVE_FLAG = 0 " +
			    ") " +
			    "AND EXISTS ( " +
			    "    SELECT 1 " +
			    "    FROM retailer_credit_note rcn " +
			    "    WHERE rcn.payex_retailer_code = pci.payex_retailer_code " +
			    "      AND rcn.payex_cpg_code = 'CPGVGUARD' " +
			    "      AND rcn.cn_status = 0 " +
			    "      AND EXISTS ( " +
			    "          SELECT 1 " +
			    "          FROM ret_dist_map_vguard rdm " +
			    "          WHERE rdm.payex_retailer_code = rcn.payex_retailer_code " +
			    "            AND rdm.cn_auto_pull > 0 " +
			    "      ) " +
			    ")";
		
		logger.info("exportQueriesForVGFifoPullOutstanding > sql : "+ sql);
		queries.add(sql);
		
		return queries;
	}
	
	private String inClauseByList(List<String> buyerCodes) {
		if (buyerCodes == null || buyerCodes.isEmpty()) {
	        return "";
	    }

	    return buyerCodes.stream()
	            .map(code -> "'" + code + "'")   // wrap each value in double quotes
	            .collect(Collectors.joining(",")); // join with comma
	}
}
