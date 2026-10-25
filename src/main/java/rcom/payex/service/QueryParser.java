package rcom.payex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSetMetaData;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class QueryParser {

	@Autowired
    private JdbcTemplate jdbcTemplate;

    public QueryParser(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final Pattern TABLE_PATTERN =
            Pattern.compile("from\\s+([a-zA-Z0-9_\\.]+)", Pattern.CASE_INSENSITIVE);

    public ParsedQuery parse(String query, boolean columnsFromOutput) throws Exception {

        String normalized = query.replaceAll("\\s+", " ").trim();

        Matcher m = TABLE_PATTERN.matcher(normalized);
        if (!m.find()) throw new RuntimeException("Cannot detect table in: " + query);

        String table = m.group(1).replaceAll("\"", "").trim();

        // If SELECT *
        if (columnsFromOutput || normalized.toLowerCase().contains("select *")) {
            List<String> cols = fetchColumnsUsingMetadata(query);
            return new ParsedQuery(table, cols);
        }

        // Extract column list between SELECT and FROM
        String insideSelect = normalized
                .substring(normalized.toLowerCase().indexOf("select") + 6,
                        normalized.toLowerCase().indexOf("from"))
                .trim();

        List<String> columns = new ArrayList<>();
        for (String c : insideSelect.split(",")) {
            columns.add(c.trim().replaceAll(".*\\.", "")); // remove alias prefix
        }

        return new ParsedQuery(table, columns);
    }


    private List<String> fetchColumnsUsingMetadata(String query) {
        return jdbcTemplate.query(query, rs -> {
            ResultSetMetaData meta = rs.getMetaData();
            List<String> cols = new ArrayList<>();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                cols.add(meta.getColumnName(i));
            }
            return cols;
        });
    }

    public static class ParsedQuery {
        public final String table;
        public final List<String> columns;

        public ParsedQuery(String t, List<String> c) {
            this.table = t;
            this.columns = c;
        }
    }
}
