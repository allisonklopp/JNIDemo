package com.aklopp.augment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * This class provides functionality to view a list of strings obtained through a C++ library
 * and run a Google search on an element in the list, displaying the results.
 * 
 * Code project prompt:
 * Create a java / ndk project:
 * With a connection to a C++ library that return a string list
 * Display the list on an Android activity
 * On a click on an element of the list, trigger a google custom search on this string
 * Display the search results
 */
public class MainActivity extends ActionBarActivity implements ActionBar.TabListener{

    private static final String NATIVE_LIBRARY_NAME = "BlackBox";

	/**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    /**
     * Broadcast receiver to run a search if an item on the search list is clicked.
     */
    BroadcastReceiver mSearchListBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create the adapter that will return a fragment for each of the
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
    	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            	if(null != actionBar)
            		actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        mSearchListBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String searchTerm = intent.getStringExtra(Constants.SEARCH_TERM_INTENT_EXTRA);
                if(searchTerm != null && !searchTerm.equals("")) {
                    // Switch tabs
                    mViewPager.setCurrentItem(1);
                    // Initiate search term received
                    mSectionsPagerAdapter.runSearch(searchTerm);
                }
            }
        };

        // Register the receiver to listen for search event intents
        registerReceiver(mSearchListBroadcastReceiver, new IntentFilter(Constants.SEARCH_TERM_INTENT_ACTION));
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        
        // Unregister the receiver when destroying the activity
        unregisterReceiver(mSearchListBroadcastReceiver);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    	// Intentionally blank
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    	// Intentionally blank
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private static final int SEARCH_PAGE_INDEX = 0;
        private static final int RESULTS_PAGE_INDEX = 1;

		private final SearchListFragment mSearchFragment;
        private final ResultsViewFragment mResultsFragment;

        /**
         * Constructor
         * @param fm
         */
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            
            // Instantiate the fragments for the tabs
            mSearchFragment = new SearchListFragment(getList());
            mResultsFragment = new ResultsViewFragment();
        }

        /**
         * Run a search on the term that was passed into this fragment.
         * @param searchTerm
         */
        public void runSearch(String searchTerm)
        {
            mResultsFragment.runSearchQuery(searchTerm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position) {
                case SEARCH_PAGE_INDEX:
                    return mSearchFragment;
                case RESULTS_PAGE_INDEX:
                    return mResultsFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.search_tab_title);
                case 1:
                    return getString(R.string.results_tab_title);
            }
            return null;
        }
    }

    // Load the magical string list creating library
    static {
        System.loadLibrary(NATIVE_LIBRARY_NAME); // Load native library at runtime
    }
    // Native method in the c++ library
    public native String[] getList();
}
