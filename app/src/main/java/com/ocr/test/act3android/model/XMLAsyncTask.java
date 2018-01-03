package com.ocr.test.act3android.model;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Class to handle concurrent Tasks to fetch RSS feeds
 * @author bob
 * @version: 01/01/18
 */
//https://openclassrooms.com/courses/developpez-une-application-pour-android/affichez-du-contenu-de-l-internet
public class XMLAsyncTask  extends AsyncTask<String,Void,Document> {

    private final static String BUNDLE_XMLASYNCTASK = "XMLASYNTASK";

    interface DocumentConsumer {

        void setXMLDocument(Document document);
    }

    private DocumentConsumer _consumer;
    private int nbSeconds = 0;

    // the consumer here being called is the ADAPTER
    public XMLAsyncTask(DocumentConsumer cons, int nb) {

        _consumer = cons;

        nbSeconds = nb;
        Log.i(BUNDLE_XMLASYNCTASK,"Constructor called - filling consumer data");

    }

    @Override
    protected Document doInBackground(String... params) {

        try {

            Log.i(BUNDLE_XMLASYNCTASK,"Entering background process - sleeping first :");
            // testing purpose to mimic internet connection
            Thread.sleep(nbSeconds * 1000);

            URL url = new URL(params[0]);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream stream = conn.getInputStream();

            Log.i(BUNDLE_XMLASYNCTASK,"Reading url document");

            try {

                Log.i("XMLAsyncTask","Returning parsed document from stream using DocumentBuilderFactory - sec " + nbSeconds);
                //https://www.jmdoudoux.fr/java/dej/chap-dom.htm
                return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
            }
            finally {
                Log.i(BUNDLE_XMLASYNCTASK,"Closing the stream");
                stream.close();
            }

        }
        catch (InterruptedException e) {
            Log.e(BUNDLE_XMLASYNCTASK, "Thread interrupted" , e);
            return null;
        }
        catch (Exception e) {

            Log.e(BUNDLE_XMLASYNCTASK, "Exception while downloading" , e);
            throw new RuntimeException(e);
        }


    }

    @Override
    protected void onPostExecute(Document result) {

        Log.i(BUNDLE_XMLASYNCTASK,"Async Task ended - fill the consumer with document (iow : the adapter is retrieving the doc !)");
        _consumer.setXMLDocument(result);

    }
}

