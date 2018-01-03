package com.ocr.test.act3android.model;

import android.app.AlertDialog;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import com.ocr.test.act3android.R;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * Adapter to handle the data and bind it to the recyclerView
 * @author : bob
 * @version : 20/12/17
 */
public class RSSAdapter extends RecyclerView.Adapter<RSSAdapter.ArticleViewHolder> implements XMLAsyncTask.DocumentConsumer {

    /**
     * Interface to handle the Loading of url in another fragment
     */
    public interface URLLoader {
        void load(String title, String link);
    }


    final private static String BUNDLE_RSS_ADAPTER = "RSSAdapter";
    final private static String BUNDLE_RSS_ADAPTER_VH = "RSSAdapterViewHolder";

    private Document _doc = null;

    // define background color differently based on URLs parameters for testing purposes tracking the ending tasks;
    private int[] curColor = {Color.CYAN,Color.YELLOW};
    private int ci = 0; // index of our colors array

    private final URLLoader _urlLoader;

    /**
     * Constructor class
     * @param urlLoader : URLLoader
     */
    public RSSAdapter(URLLoader urlLoader) {

        _urlLoader = urlLoader;
    }


    @Override
    public int getItemCount() {
        // Log.i("RSSAdapter","Retrieving onItemCount()");


        if (_doc != null) {
            Log.i(BUNDLE_RSS_ADAPTER,"Retrieving onItemCount() " + _doc.getElementsByTagName("item").getLength());
            return _doc.getElementsByTagName("item").getLength();

        }
        else
            return 0;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.i(BUNDLE_RSS_ADAPTER,"Calling onCreateViewHolder");

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.article_cell,parent, false);

        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {

        Log.i(BUNDLE_RSS_ADAPTER,"Calling onBindViewHolder - bind the doc from the adapter to the view holder");

        Element item = (Element)_doc.getElementsByTagName("item").item(position);

        holder.setElement(item);
    }

    @Override
    public void setXMLDocument(Document doc) {

        /* TODO : Add elements to doc with the submitted one - if possible order by date
        * and change color depending on source of RSS :*/
        try {
            if (_doc != null) {
                ++ci;
                _doc = concatXmlDocuments(_doc, doc);
                Log.i(BUNDLE_RSS_ADAPTER, "Next Call set Document with color " + ci );
            }
            else {
                _doc = doc;
                Log.i(BUNDLE_RSS_ADAPTER, "First time calling set Document with color " + ci );

            }
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            Log.e(BUNDLE_RSS_ADAPTER, "Exception while concatening document" , e);
        }
        notifyDataSetChanged();

        Log.i(BUNDLE_RSS_ADAPTER,"setXMLDocument from interface / XMLAsyncTask - notify the changes to the adpater.");
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {

        private final TextView _title;

        private Element _curElt;

        public ArticleViewHolder(final View itemView) {

            super(itemView);
            _title = ((TextView) itemView.findViewById(R.id.title));

            // retrieving content of item to launch webview in other fragment :
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                String title = _curElt.getElementsByTagName("title").item(0).getTextContent();
                String link  = _curElt.getElementsByTagName("link").item(0).getTextContent();

                Log.i(BUNDLE_RSS_ADAPTER_VH,"OnClick event to load the url in other view");

                _urlLoader.load(title,link);
                }
            });


            Log.i(BUNDLE_RSS_ADAPTER_VH,"Article View Holder call passing the view in parameters - setting the title");
        }


        public void setElement(Element elt) {
            _curElt = elt;
            _title.setText(elt.getElementsByTagName("title").item(0).getTextContent() +
                    elt.getElementsByTagName("pubDate").item(0).getTextContent());

            _title.setBackgroundColor(curColor[ci]);
            Log.i("Adapter_AViewHolder","we set the current element with color : " + ci);

        }
    }

    // Method to concatenate 2 or more document
    public Document concatXmlDocuments(Document... xmlDoc)
            throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document result = builder.newDocument();

        // root element from rss feed : channel :
        Element rootElement = result.createElement("channel");
        result.appendChild(rootElement);

        for(Document is : xmlDoc) {
            org.w3c.dom.Element root = is.getDocumentElement();
            NodeList childNodes = root.getChildNodes();
            for(int i = 0; i < childNodes.getLength(); i++) {
                Node importNode = result.importNode(childNodes.item(i), true);
                rootElement.appendChild(importNode);
            }
        }
        return result;
    }

}
