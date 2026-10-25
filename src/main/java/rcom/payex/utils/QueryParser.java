package rcom.payex.utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParser {

    private static final Pattern SELECT_PATTERN =
            Pattern.compile("select\\s+(.*?)\\s+from\\s+([\\w\\.]+)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    public static ParsedQuery parse(String sql) {
        Matcher m = SELECT_PATTERN.matcher(sql);
        if (!m.find()) {
            throw new IllegalArgumentException("Invalid SELECT query: " + sql);
        }

        String cols = m.group(1).trim();
        String table = m.group(2).trim();

        List<String> colList = new ArrayList<>();
        for (String c : cols.split(",")) {
            colList.add(c.trim());
        }

        return new ParsedQuery(table, colList);
    }

    public static class ParsedQuery {
        private final String table;
        private final List<String> columns;

        public ParsedQuery(String table, List<String> columns) {
            this.table = table;
            this.columns = columns;
        }

        public String getTable() { return table; }
        public List<String> getColumns() { return columns; }
    }
}

