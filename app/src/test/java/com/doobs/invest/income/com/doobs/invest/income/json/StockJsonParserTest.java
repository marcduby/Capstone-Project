package com.doobs.invest.income.com.doobs.invest.income.json;

import android.util.JsonReader;

import com.doobs.invest.income.json.StockJsonParser;
import com.doobs.invest.income.model.StockBean;
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
        StockBean stockBean = null;

        // get the test string
        inputStream = this.getClass().getClassLoader().getResourceAsStream("stock.json");
        assertNotNull(inputStream);
        inputJsonString = this.getStringFromStream(inputStream);
        assertNotNull(inputJsonString);

        // get the stock bean
        try {
            stockBean = StockJsonParser.parseString(inputJsonString);

        } catch (IncomeException exception) {
            fail("Got error: " + exception.getMessage());
        }

        // test
        assertNotNull(stockBean);
        assertNotNull(stockBean.getSymbol());
        assertNotNull(stockBean.getName());
        assertNotNull(stockBean.getDescription());
        assertNotNull(stockBean.getIndustry());
        assertNotNull(stockBean.getIssueType());
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
        } catch (IOException exception) {
        }
        return out.toString();    }
}
