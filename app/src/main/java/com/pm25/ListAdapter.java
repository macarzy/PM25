package com.pm25;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pm25.PM25Meta;
import com.pm25.R;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter
{
    private ArrayList<String[]> data;
    private LayoutInflater myInflater;

    public ListAdapter(Context ref, ArrayList<String[]> theData )
    {
        myInflater = LayoutInflater.from(ref);
        data = theData;
    }

    @Override
    public int getCount()
    {
        return data.size();
    }

    @Override
    public Object getItem(int position)
    {
        return data.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = myInflater.inflate(R.layout.list_open_data_results, null);

        ImageView imageView_status = (ImageView) convertView.findViewById(R.id.imageView_status);
        TextView textView_county = (TextView) convertView.findViewById(R.id.textView_County);
        TextView textView_site = (TextView) convertView.findViewById(R.id.textView_Site);
        TextView textView_pm25 = (TextView) convertView.findViewById(R.id.textView_PM25);
        TextView textView_time = (TextView) convertView.findViewById(R.id.textView_time);

        if( data.get(position)[PM25Meta.DATA_INDEX_PM25] != null )
        {
            int pm25Value = Integer.parseInt(data.get(position)[PM25Meta.DATA_INDEX_PM25]);

            if (pm25Value >= PM25Meta.GREEN_MIN && pm25Value <= PM25Meta.GREEN_MAX) {
                imageView_status.setImageResource(R.drawable.green);
            } else if (pm25Value >= PM25Meta.YELLOW_MIN && pm25Value <= PM25Meta.YELLOW_MAX) {
                imageView_status.setImageResource(R.drawable.yellow);
            } else if (pm25Value >= PM25Meta.RED_MIN && pm25Value <= PM25Meta.RED_MAX) {
                imageView_status.setImageResource(R.drawable.red);
            } else if (pm25Value >= PM25Meta.PURPLE_MIN) {
                imageView_status.setImageResource(R.drawable.purple);
            }
        }
        textView_county.setText(data.get(position)[PM25Meta.DATA_INDEX_COUNTY]);
        textView_site.setText(data.get(position)[PM25Meta.DATA_INDEX_SITE]);
        textView_pm25.setText(data.get(position)[PM25Meta.DATA_INDEX_PM25]);
        textView_time.setText(data.get(position)[PM25Meta.DATA_INDEX_TIME]);

        return convertView;
    }
}
