package com.pm25.net;

import android.os.AsyncTask;
import android.util.Log;

import com.pm25.MainActivity;
import com.pm25.PM25Meta;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class Network extends AsyncTask<Void, Void, Void>
{
    /*TAG*/
    private final static String TAG_NETWORK = "NETWORK";
    private final static String TAG_PARSE_DATA = "PARSE_DATA";
    /**/
    private int operation;
    private MainActivity mainThread;
    /**/
    private ArrayList<String[]> data;

    public Network( MainActivity ref, int theOperation)
    {
        mainThread = ref;
        operation = theOperation;
    }

    @Override
    protected Void doInBackground(Void... params)
    {
        if( operation == Config.GET_PM25_INFO_FROM_INTERNET )
        {
            parseXML();
        }

        return null;
    }

    private void parseXML()
    {
        /*Initial Data Structure*/
        data = new ArrayList<String[]>();
        String[] intermediateData = null;
        /*Flag*/
        boolean countyFlag = false;
        boolean siteNameFlag = false;
        boolean pm25Flag = false;
        boolean timeFlag = false;

        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            parser.setInput( new InputStreamReader(getWebContent()));

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                switch ( eventType )
                {
                    case XmlPullParser.START_TAG :
                        if( parser.getName().equals(PM25Meta.ELEM_NAME_COUNTY) )
                        {
                            countyFlag = true;
                        }
                        else if( parser.getName().equals(PM25Meta.ELEM_NAME_SITE) )
                        {
                            siteNameFlag = true;

                            intermediateData = new String[PM25Meta.DATA_NUMBER];
                        }
                        else if( parser.getName().equals(PM25Meta.ELEM_NAME_PM25) )
                        {
                            pm25Flag = true;
                        }
                        else if( parser.getName().equals(PM25Meta.ELEM_NAME_TIME) )
                        {
                            timeFlag = true;
                        }
                        break;
                    case XmlPullParser.TEXT :
                        if( countyFlag == true )
                        {
                            if (intermediateData != null)
                            {
                                intermediateData[PM25Meta.DATA_INDEX_COUNTY]  = parser.getText();
                            }
                        }
                        else if( siteNameFlag == true )
                        {
                            if (intermediateData != null)
                            {
                                intermediateData[PM25Meta.DATA_INDEX_SITE]  = parser.getText();
                            }
                        }
                        else if( pm25Flag == true )
                        {
                            if (intermediateData != null)
                            {
                                intermediateData[PM25Meta.DATA_INDEX_PM25]  = parser.getText();
                            }
                        }
                        else if( timeFlag == true )
                        {
                            if (intermediateData != null)
                            {
                                intermediateData[PM25Meta.DATA_INDEX_TIME]  = parser.getText();
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG :
                        if( parser.getName().equals(PM25Meta.ELEM_NAME_COUNTY) )
                        {
                            countyFlag = false;
                        }
                        else if( parser.getName().equals(PM25Meta.ELEM_NAME_SITE) )
                        {
                            siteNameFlag = false;
                        }
                        else if( parser.getName().equals(PM25Meta.ELEM_NAME_PM25) )
                        {
                            pm25Flag = false;
                        }
                        else if( parser.getName().equals(PM25Meta.ELEM_NAME_TIME) )
                        {
                            timeFlag = false;

                            if( intermediateData != null && intermediateData.length == PM25Meta.DATA_NUMBER )
                            {
                                data.add(intermediateData);
                            }
                        }
                        break;
                    default :
                        break;
                }
                eventType = parser.next();
            }
        }
        catch ( XmlPullParserException e )
        {
            Log.e(TAG_PARSE_DATA, e.getMessage());
        }
        catch ( IOException e )
        {
            Log.e(TAG_PARSE_DATA, e.getMessage());
        }
    }

    private InputStream getWebContent()
    {
        try
        {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet method = new HttpGet(new URI(Config.OPEN_DATA_URI));
            HttpResponse res = client.execute(method);
            return res.getEntity().getContent();
        }
        catch ( IOException e )
        {
            Log.e(TAG_NETWORK, e.getMessage() );
        }
        catch( URISyntaxException e )
        {
            Log.e(TAG_NETWORK, e.getMessage() );
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        super.onPostExecute(aVoid);

        if( operation == Config.GET_PM25_INFO_FROM_INTERNET )
        {
            if( data != null )
            {
                mainThread.setData(data);
            }
        }
    }
}