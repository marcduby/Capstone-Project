package com.doobs.invest.income.com.doobs.invest.income.json;

import com.doobs.invest.income.json.StockJsonParser;
import com.doobs.invest.income.json.bean.StockInformationBean;
import com.doobs.invest.income.json.bean.StockQuoteBean;
import com.doobs.invest.income.util.IncomeException;

import junit.framework.TestCase;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

/**
 * Unit test class to test the json parser
 *
 * Created by mduby on 11/17/18.
 */

public class StockJsonParserTest extends TestCase {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testStockParsing() {
        // local variables
        String inputJsonString = null;
        InputStream inputStream = null;
        StockInformationBean stockInformationBean = null;

        // get the test string
        inputStream = this.getClass().getClassLoader().getResourceAsStream("stockInformation.json");
        assertNotNull(inputStream);
        inputJsonString = this.getStringFromStream(inputStream);
        assertNotNull(inputJsonString);

        // get the stock bean
        try {
            stockInformationBean = StockJsonParser.parseString(inputJsonString);

        } catch (IncomeException exception) {
            fail("Got error: " + exception.getMessage());
        }

        // test
        assertNotNull(stockInformationBean);
        assertNotNull(stockInformationBean.getSymbol());
        assertNotNull(stockInformationBean.getName());
        assertNotNull(stockInformationBean.getDescription());
        assertNotNull(stockInformationBean.getIndustry());
        assertNotNull(stockInformationBean.getIssueType());
    }

    /**
     * test the dividend retrieval and calculation
     */
    @Test
    public void testGetYearlyDividendFromJsonString() {
        // local variables
        String inputJsonString = null;
        InputStream inputStream = null;
        Double dividend = null;

        // get the test string
        inputStream = this.getClass().getClassLoader().getResourceAsStream("dividend.json");
        assertNotNull(inputStream);
        inputJsonString = this.getStringFromStream(inputStream);
        assertNotNull(inputJsonString);

        // get the stock bean
        try {
            dividend = StockJsonParser.getYearlyDividendFromJsonString(inputJsonString);

        } catch (IncomeException exception) {
            fail("Got error: " + exception.getMessage());
        }

        // test
        assertNotNull(dividend);
        assertEquals(2.58, dividend);
    }

    /**
     * test the stock quote retrieval
     *
     */
    @Test
    public void testStockQuoteParsing() {
        // local variables
        String inputJsonString = null;
        InputStream inputStream = null;
        StockQuoteBean stockQuoteBean = null;

        // get the test string
        inputStream = this.getClass().getClassLoader().getResourceAsStream("quote.json");
        assertNotNull(inputStream);
        inputJsonString = this.getStringFromStream(inputStream);
        assertNotNull(inputJsonString);

        // get the stock bean
        try {
            stockQuoteBean = StockJsonParser.getStockQuoteFromJsonString(inputJsonString);

        } catch (IncomeException exception) {
            fail("Got error: " + exception.getMessage());
        }

        // test
        assertNotNull(stockQuoteBean);
        assertNotNull(stockQuoteBean.getSymbol());
        assertNotNull(stockQuoteBean.getDate());
        assertNotNull(stockQuoteBean.getPrice());
        assertNotNull(stockQuoteBean.getPriceChange());
        assertNotNull(stockQuoteBean.getPeRatio());
    }

    /**
     * reads the string from the stream
     *
     * @param inputStream
     * @return
     */
    protected String getStringFromStream(InputStream inputStream) {
        // local variables
        String result = null;
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        try {
            Reader in = new InputStreamReader(inputStream, "UTF-8");
            for (; ; ) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }

        } catch (UnsupportedEncodingException exception) {
            // do nothing

        } catch (IOException exception) {
            // do nothing
        }

        // return
        return out.toString();
    }
}
