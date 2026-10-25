package rcom.payex.service;
import java.io.File;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.opencsv.CSVWriter;

import rcom.payex.utils.ApplicationConstant;
import rcom.payex.utils.QueryParser;
import rcom.payex.utils.Utils;

@Service
public class CsvExportService {
	
	Logger logger = LogManager.getLogger(getClass());

	@Autowired
    private JdbcTemplate jdbcTemplate;

    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TS_FMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public File exportQueriesToCsv(List<String> queries) throws Exception {

        File csv = File.createTempFile("export_", ".csv");

        CSVWriter writer = new CSVWriter(
                new FileWriter(csv),
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.DEFAULT_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END
        );

        try {

            for (String query : queries) {

                QueryParser.ParsedQuery parsed = QueryParser.parse(query);

                writer.writeNext(new String[]{"## TABLE: " + parsed.getTable()});
                writer.writeNext(parsed.getColumns().toArray(new String[0]));

                jdbcTemplate.query(query, rs -> {

                    // Iterate over all records
                    while (rs.next()) {

                        List<String> row = new ArrayList<>();

                        for (String col : parsed.getColumns()) {
                            Object val = rs.getObject(col);
                            row.add(formatValue(val));  // your formatter for dates/numbers
                        }

                        writer.writeNext(row.toArray(new String[0]), false);
                    }

                    return null; // required by extractor
                });               

                writer.writeNext(new String[]{""}); // blank line
            }

        } finally {
            writer.close();
        }

        return csv;
    }

    private String formatValue(Object v) {

        if (v == null) return "";

        if (v instanceof java.sql.Date) {
            return DATE_FMT.format((java.sql.Date) v);
        }
        if (v instanceof Timestamp) {
            return TS_FMT.format((Timestamp) v);
        }

        return v.toString();
    }
    
    public File exportQueriesToCsvWithoutColumnList(List<String> queries, String payexRetailerCode) throws Exception {

    	logger.info("exportQueriesToCsvWithoutColumnList...");
    	String filePath = ApplicationConstant.getRootDir()+ "vguard" + File.separatorChar + "CNPULL";
        File csv = new File(filePath,payexRetailerCode+"-pullData-"+Utils.date_without_time.format(new Date())+".csv");
        
        rcom.payex.service.QueryParser parser = new rcom.payex.service.QueryParser(jdbcTemplate);

        CSVWriter writer = new CSVWriter(
                new FileWriter(csv),
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.DEFAULT_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END
        );

        try {

            for (String query : queries) {
            	logger.info("query : "+query);
            	rcom.payex.service.QueryParser.ParsedQuery parsed = parser.parse(query, true);
            	logger.info("parsed.columns : "+parsed.columns.size());

                writer.writeNext(new String[]{"## TABLE: " + parsed.table});
                writer.writeNext(parsed.columns.toArray(new String[0]));
                logger.info(parsed.table+" > parsed.columns written");
               
                jdbcTemplate.query(query, rs -> {
                	//int rowCount = 0;
                    // Iterate over all records
                    while (rs.next()) {
                        List<String> row = new ArrayList<>();

                        for (String col : parsed.columns) {
                            Object val = rs.getObject(col);
                            row.add(formatValue(val));  // your formatter for dates/numbers
                        }

                        writer.writeNext(row.toArray(new String[0]), false);
                        //logger.info(rowCount++ +" > row written");
                    }

                    return null; // required by extractor
                });               
                logger.info(parsed.table+" > all rows written");
                writer.writeNext(new String[]{""}); // blank line
            }

        } finally {
            writer.close();
        }
        logger.info("exportQueriesToCsvWithoutColumnList...End...");
        return csv;
    }
}

