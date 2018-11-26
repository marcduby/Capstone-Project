package com.doobs.invest.income.util;

import android.net.Uri;
import android.util.Log;

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

}
