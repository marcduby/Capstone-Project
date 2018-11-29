package com.doobs.invest.income.util;

import android.net.Uri;
import android.util.Log;

import com.doobs.invest.income.json.StockJsonParser;
import com.doobs.invest.income.json.bean.StockInformationBean;
import com.doobs.invest.income.json.bean.StockQuoteBean;
import com.doobs.invest.income.json.bean.StockStatsBean;
import com.doobs.invest.income.model.StockModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.Scanner;

/**
 * Network utility class to help with REST calls
 *
 * Created by mduby on 11/26/18.
 */

public class NetworkUtils {
    // constants
    public static String TAG_NAME = NetworkUtils.class.getName();

    /**
     * test the network connection
     *
     * @throws IncomeException
     */
    public static void testNetwork() throws IncomeException {
        try {
            int timeoutMilliseconds = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMilliseconds);
            sock.close();

        } catch (IOException exception) {
            String message = "Got network exception: " + exception;
            throw new IncomeException(message);
        }
    }

    /**
     * connect to the REST service and return the results of the query
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        // local vaiables
        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        String responseString = null;

        // log
        Log.i(TAG_NAME, "Making REST call to: " + url.toString());

        // open the stream
        try {
            InputStream inputStream = httpURLConnection.getInputStream();

            // get the response string
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) {
                responseString = scanner.next();
            }

        } finally {
            httpURLConnection.disconnect();
        }

        // return
        return responseString;
    }

    /**
     * returns the movie REST service server and context string
     *
     * @return
     */
    private static String getServerAndContextString() {
        // return the server and context
        return  IncomeConstants.RestServer.SERVER_NAME + "/" + IncomeConstants.RestServer.ROOT_CONTEXT + "/";
    }

    /**
     * build the stock query url for the data desired
     *
     * @param stockSymbol
     * @param dataType
     * @return
     * @throws IncomeException
     */
    public static URL getRestServiceUrl(String stockSymbol, String dataType) throws IncomeException {
        // local variables
        Uri searchUri = null;
        String serverAndContextString = null;
        URL stockQueryUrl = null;

        // build the string
        serverAndContextString = getServerAndContextString();
//        serverAndContextString = serverAndContextString + String.valueOf(movieId) + "/" + subPath;

        // build the uri
        searchUri = Uri.parse(serverAndContextString).buildUpon()
                .appendPath(IncomeConstants.RestServer.METHOD_STOCK)
                .appendPath(stockSymbol)
                .appendPath(dataType)
                .build();

        // build the url
        try {
            stockQueryUrl = new URL(searchUri.toString());

        } catch (MalformedURLException exception) {
            String errorMessage = "Got url exception: " + exception.getMessage();
            Log.e(TAG_NAME, errorMessage);
            throw new IncomeException(errorMessage);
        }

        // return
        return stockQueryUrl;
    }

    /**
     * find a stock through the REST service
     *
     * @param stockModel
     * @return
     */
    public static void updateStockFromRestCall(StockModel stockModel) {
        // TODO - implement
        URL stockQueryUrl = null;
        String restCallString = null;
        StockInformationBean stockInformationBean = null;
        StockQuoteBean stockQuoteBean = null;
        StockStatsBean stockStatsBean = null;

        // log
        Log.i(TAG_NAME, "Calling REST service for symbol: " + stockModel.getSymbol() + " and stock id: " + stockModel.getId());

        try {
            // get the stock information
            // build the URL
            stockQueryUrl = NetworkUtils.getRestServiceUrl(stockModel.getSymbol(), IncomeConstants.RestServer.DATA_COMPANY);

            // call the REST service
            restCallString = NetworkUtils.getResponseFromHttpUrl(stockQueryUrl);

            // parse the string to json
            stockInformationBean = StockJsonParser.getStockInformationFromJsonString(restCallString);

            // get the data for the stock model
            stockModel.setName(stockInformationBean.getName());
            stockModel.setIndustry(stockInformationBean.getIndustry());
            stockModel.setIssueType(stockInformationBean.getIssueType());

            // get the stock price
            // build the URL
            stockQueryUrl = NetworkUtils.getRestServiceUrl(stockModel.getSymbol(), IncomeConstants.RestServer.DATA_QUOTE);

            // call the REST service
            restCallString = NetworkUtils.getResponseFromHttpUrl(stockQueryUrl);

            // parse the string to json
            stockQuoteBean = StockJsonParser.getStockQuoteFromJsonString(restCallString);

            // get the data for the stock model
            stockModel.setPrice(stockQuoteBean.getPrice());
            stockModel.setPeRatio(stockQuoteBean.getPeRatio());
            stockModel.setPriceChange(stockQuoteBean.getPriceChange());
            stockModel.setDateString(IncomeUtils.getCurrentDateString());

            // get the stock stats
            // build the URL
            stockQueryUrl = NetworkUtils.getRestServiceUrl(stockModel.getSymbol(), IncomeConstants.RestServer.DATA_STATS);

            // call the REST service
            restCallString = NetworkUtils.getResponseFromHttpUrl(stockQueryUrl);

            // parse the string to json
            stockStatsBean = StockJsonParser.getStockStatsFromJsonString(restCallString);

            // get the data for the stock model
            stockModel.setDividend(stockStatsBean.getDividend());
            stockModel.setYield(stockStatsBean.getYield());
            stockModel.setBeta(stockStatsBean.getBeta());

        } catch (IncomeException exception) {
            // log
            Log.e(TAG_NAME, "Got error parsing the REST call: " + exception.getMessage());

        } catch (IOException exception) {
            // log
            Log.e(TAG_NAME, "Got IO error parsing the REST call: " + exception.getMessage());
        }
    }
}
