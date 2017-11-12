package com.pm25;

import java.util.ArrayList;

public class Filter
{
    private static final String[] northArea = new String[]{"基隆市", "臺北市", "新北市", "桃園市", "新竹市", "新竹縣", "宜蘭縣"};
    private static final String[] midlandArea = new String[]{"苗栗縣", "臺中市", "彰化縣", "南投縣", "雲林縣"};
    private static final String[] southArea = new String[]{"嘉義市", "嘉義縣", "臺南市", "高雄市", "屏東縣", "澎湖縣"};
    private static final String[] eastArea = new String[]{"花蓮縣", "臺東縣"};
    private static final String[] otherArea = new String[]{"金門縣", "連江縣"};

    private ArrayList<String[]> data;

    public Filter(ArrayList<String[]> theData)
    {
        data = theData;
    }

    public ArrayList<String[]> filterResultsByArea( int area )
    {
        ArrayList<String[]> results = new ArrayList<String[]>();

        switch ( area )
        {
            case FilterConfig.FILTER_EAST :
                for( int i = 0 ; i < data.size() ; i++ )
                {
                    for( int j = 0 ; j < eastArea.length ; j++ )
                    {
                        if (data.get(i)[PM25Meta.DATA_INDEX_COUNTY].contains( eastArea[j] ) == true )
                        {
                            results.add(data.get(i));
                        }
                    }
                }
                break;
            case FilterConfig.FILTER_MIDLAND :
                for( int i = 0 ; i < data.size() ; i++ )
                {
                    for( int j = 0 ; j < midlandArea.length ; j++ )
                    {
                        if (data.get(i)[PM25Meta.DATA_INDEX_COUNTY].contains( midlandArea[j] ) == true )
                        {
                            results.add(data.get(i));
                        }
                    }
                }
                break;
            case FilterConfig.FILTER_NORTH :
                for( int i = 0 ; i < data.size() ; i++ )
                {
                    for( int j = 0 ; j < northArea.length ; j++ )
                    {
                        if (data.get(i)[PM25Meta.DATA_INDEX_COUNTY].contains( northArea[j] ) == true )
                        {
                            results.add(data.get(i));
                        }
                    }
                }
                break;
            case FilterConfig.FILTER_SOUTH :
                for( int i = 0 ; i < data.size() ; i++ )
                {
                    for( int j = 0 ; j < southArea.length ; j++ )
                    {
                        if (data.get(i)[PM25Meta.DATA_INDEX_COUNTY].contains( southArea[j] ) == true )
                        {
                            results.add(data.get(i));
                        }
                    }
                }
                break;
            case FilterConfig.FILTER_OTHERS :
                for( int i = 0 ; i < data.size() ; i++ )
                {
                    for( int j = 0 ; j < otherArea.length ; j++ )
                    {
                        if (data.get(i)[PM25Meta.DATA_INDEX_COUNTY].contains( otherArea[j] ) == true )
                        {
                            results.add(data.get(i));
                        }
                    }
                }
                break;
            default:
                /*Error Operation*/
                break;
        }

        return results;
    }
}
