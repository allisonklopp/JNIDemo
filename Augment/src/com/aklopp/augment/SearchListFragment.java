package com.aklopp.augment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/** 
 * The fragment to display the list of search terms available.
 * Created by allisonklopp on 9/12/15.
 */
public class SearchListFragment extends Fragment {
    private String[] mList;

	public SearchListFragment(String[] list) {
		// TODO Auto-generated constructor stub
    	this.mList = list;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_listview, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview);
        
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mList);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String searchItem = ((TextView)view.findViewById(android.R.id.text1)).getText().toString();
                System.out.println("SEARCH FOR: " + searchItem);

                Intent intent = new Intent(Constants.SEARCH_TERM_INTENT_ACTION);
                intent.putExtra(Constants.SEARCH_TERM_INTENT_EXTRA,searchItem);
                getActivity().sendBroadcast(intent);
            }
        });
        return rootView;
    }
}
