package com.pm25;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.pm25.net.Config;
import com.pm25.net.Network;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    /*TAG*/
    private final static String TAG_DATA_RESULTS = "DATA_RESULTS";
    /**/
    private ArrayList<String[]> data;
    private ArrayList<String[]> filteredData;
    /*UI Widgets*/
    private ListView listView_results;
    private ListAdapter listAdapter;
    private DrawerLayout drawerLayout_illu;
    /*Menu ID*/
    private final static int MENU_ILLU = 1;
    private final static int MENU_REFRESH_RESULTS = 2;

    private final static int MENU_FILTER_RESULTS_BY_AREA_EAST = 3;
    private final static int MENU_FILTER_RESULTS_BY_AREA_NORTH = 4;
    private final static int MENU_FILTER_RESULTS_BY_AREA_SOUTH = 5;
    private final static int MENU_FILTER_RESULTS_BY_AREA_MIDLAND = 6;
    private final static int MENU_FILTER_RESULTS_BY_AREA_OTHERS = 7;
    private final static int MENU_FILTER_RESULTS_BY_AREA_ALL = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Activity調整*/
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持螢幕恆亮
        /*初始化UI Widgets*/
        setWidgets();
        /*設置Listeners*/
        setListeners();
        /*取得OpenData資料*/
        new Network(MainActivity.this, Config.GET_PM25_INFO_FROM_INTERNET).execute();
    }

    private void setListeners()
    {

    }

    private void setWidgets()
    {
        listView_results = (ListView) findViewById(R.id.listView_open_data_results);
        drawerLayout_illu = (DrawerLayout) this.findViewById(R.id.layout_drawer);
    }

    private void updateResultsList(boolean isFilteredData)
    {
        if( isFilteredData == false && data != null )
        {
            listAdapter = new ListAdapter(MainActivity.this, data);
            listView_results.setAdapter(listAdapter);
        }
        else if( isFilteredData == true && filteredData != null )
        {
            listAdapter = new ListAdapter(MainActivity.this, filteredData);
            listView_results.setAdapter(listAdapter);
        }
        Toast.makeText( MainActivity.this, "結果列表已更新!", Toast.LENGTH_LONG).show();
    }

    public void setData( ArrayList<String[]> theData )
    {
        data = theData;
        updateResultsList(false);

        /*TEST OUTPUT*/
        for( int i = 0 ; i < data.size() ; i++ )
        {
            Log.v(TAG_DATA_RESULTS, "County: " + data.get(i)[PM25Meta.DATA_INDEX_COUNTY]);
            Log.v(TAG_DATA_RESULTS, "Site: " + data.get(i)[PM25Meta.DATA_INDEX_SITE]);
            Log.v(TAG_DATA_RESULTS, "PM2.5: " + data.get(i)[PM25Meta.DATA_INDEX_PM25]);
            Log.v(TAG_DATA_RESULTS, "Time: " + data.get(i)[PM25Meta.DATA_INDEX_TIME]);
            Log.v(TAG_DATA_RESULTS, "*******************************************************");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SubMenu subMenu;
        MenuItem subMenuItem;

        subMenu = menu.addSubMenu("Filter Results By Area");
        subMenu.add(0, MENU_FILTER_RESULTS_BY_AREA_NORTH, 0, "北台灣");
        subMenu.add(0, MENU_FILTER_RESULTS_BY_AREA_MIDLAND, 0, "中台灣");
        subMenu.add(0, MENU_FILTER_RESULTS_BY_AREA_SOUTH, 0, "南台灣");
        subMenu.add(0, MENU_FILTER_RESULTS_BY_AREA_EAST, 0, "東台灣");
        subMenu.add(0, MENU_FILTER_RESULTS_BY_AREA_OTHERS, 0, "外島");
        subMenu.add(0, MENU_FILTER_RESULTS_BY_AREA_ALL, 0, "全部");
        subMenuItem = subMenu.getItem();
        subMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        subMenuItem.setIcon(R.drawable.ic_action_search_area);

        menu.add(0, MENU_REFRESH_RESULTS, 0, "Refresh Results");
        menu.findItem(MENU_REFRESH_RESULTS).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.findItem(MENU_REFRESH_RESULTS).setIcon(R.drawable.ic_action_refresh);

        menu.add(0, MENU_ILLU, 0, "Help");
        menu.findItem(MENU_ILLU).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.findItem(MENU_ILLU).setIcon(R.drawable.ic_action_help);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch(item.getItemId())
        {
            case MENU_ILLU :

                if( drawerLayout_illu != null && drawerLayout_illu.isDrawerOpen(GravityCompat.END) == false )
                {
                    drawerLayout_illu.openDrawer(GravityCompat.END);
                }
                else
                {
                    drawerLayout_illu.closeDrawers();
                }
                break;
            case MENU_REFRESH_RESULTS :
                new Network(MainActivity.this, Config.GET_PM25_INFO_FROM_INTERNET).execute();
                break;
            case MENU_FILTER_RESULTS_BY_AREA_EAST :
                filteredData = new Filter( data ).filterResultsByArea(FilterConfig.FILTER_EAST);
                updateResultsList(true);
                break;
            case MENU_FILTER_RESULTS_BY_AREA_SOUTH :
                filteredData = new Filter( data ).filterResultsByArea(FilterConfig.FILTER_SOUTH);
                updateResultsList(true);
                break;
            case MENU_FILTER_RESULTS_BY_AREA_NORTH :
                filteredData = new Filter( data ).filterResultsByArea(FilterConfig.FILTER_NORTH);
                updateResultsList(true);
                break;
            case MENU_FILTER_RESULTS_BY_AREA_MIDLAND :
                filteredData = new Filter( data ).filterResultsByArea(FilterConfig.FILTER_MIDLAND);
                updateResultsList(true);
                break;
            case MENU_FILTER_RESULTS_BY_AREA_OTHERS :
                filteredData = new Filter( data ).filterResultsByArea(FilterConfig.FILTER_OTHERS);
                updateResultsList(true);
                break;
            case MENU_FILTER_RESULTS_BY_AREA_ALL :
                updateResultsList(false);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}