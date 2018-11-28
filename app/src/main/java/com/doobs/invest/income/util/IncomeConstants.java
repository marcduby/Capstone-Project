package com.doobs.invest.income.util;

/**
 * Constants class to hold all the constant keys
 *
 * Created by mduby on 11/15/18.
 */

public class IncomeConstants {

    public class RestServer {
        // server information
        public static final String SERVER_NAME                      = "https://api.iextrading.com";
        public static final String ROOT_CONTEXT                     = "1.0";

        // methods
        public static final String METHOD_STOCK                     = "stock";

        // data
        public static final String DATA_STATS                       = "stats";
        public static final String DATA_QUOTE                       = "quote";
        public static final String DATA_COMPANY                     = "company";
    }

    public class RestCodes {
        public class IssueType {
            public static final String COMMON_STOCK                 = "cs";
            public static final String ETF                          = "et";
        }

        public class Industry {
            public static final String DEFAULT                      = "Multiple";
        }

        public class IssueTypeDescription {
            public static final String COMMON_STOCK                 = "Common stock";
            public static final String ETF                          = "Exchange traded fund";
            public static final String NOT_AVAILABLE                = "N/A";
        }
    }

    public class Database {
        public static final String TABLE_NAME_PORTFOLIO             = "inc_portfolio";
        public static final String TABLE_NAME_HOLDING               = "inc_holding";
        public static final String TABLE_NAME_STOCK_INFORMATION     = "inc_stock_information";

        public static final String DATABASE_NAME                    = "income_database";
    }

    public class ExtraKeys {
        public static final String PORTFOLIO_ID                     = "portfolioId";
    }

    public static class JsonKeys {
        public static class Stock {
            public final static String SYMBOL_KEY           = "symbol";
            public final static String NAME_KEY             = "companyName";
            public final static String INDUSTRY_KEY         = "sector";
            public final static String DESCRIPTION_KEY      = "description";
            public final static String ISSUE_TYPE_KEY       = "issueType";
        }

        public static class Dividend {
            public final static String AMOUNT_KEY           = "amount";
        }

        public static class Quote {
            public final static String SYMBOL_KEY           = "symbol";
            public final static String PRICE_KEY            = "latestPrice";
            public final static String DATE_KEY             = "latestTime";
            public final static String PRICE_CHANGE_KEY     = "change";
            public final static String PE_RATIO_KEY         = "peRatio";
        }

        public static class Financials {
            public final static String SYMBOL_KEY           = "symbol";
            public final static String BETA_KEY             = "beta";
            public final static String DIVIDEND_KEY         = "dividendRate";
            public final static String YIELD_KEY            = "dividendYield";
            public final static String PRICE_TO_SALES_KEY   = "priceToSales";
            public final static String PRICE_TO_BOOK_KEY    = "priceToBook";
            public final static String REVENUE_PER_HARE_KEY = "revenuePerShare";
        }
    }
}
