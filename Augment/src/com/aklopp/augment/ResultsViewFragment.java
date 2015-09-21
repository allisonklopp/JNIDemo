package com.aklopp.augment;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Class for the fragment displaying the results of the search query on the selected term.
 * Created by allisonklopp on 9/12/15.
 */
public class ResultsViewFragment extends Fragment {
	// The prefix proceeding a search term
	private static final String SEARCH_PREFIX = "https://www.google.com/search?q=";

	// The webview to display the search query
	private WebView mWebView;

	// Displays the error when page doesn't load or isn't loaded.
	private RelativeLayout mErrorView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		final View rootView = inflater.inflate(R.layout.fragment_resultsview, container, false);

		mWebView = (WebView) rootView.findViewById(R.id.webView);

		// Override the default so that it doesn't redirect to the web browser app
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				// Do something
				mWebView.setVisibility(View.GONE);
				mErrorView.setVisibility(View.VISIBLE);
				((TextView)rootView.findViewById(R.id.error_text)).setText("Page failed to load.\n"
						+ "Error: " + description +
						"\nError code: " + errorCode +
						"\nURL: " + failingUrl);
			}
		});
		
		mErrorView = (RelativeLayout) rootView.findViewById(R.id.error_view);

		return rootView;
	}

	/**
	 * Displays the result of a search query in a web browser in the webview.
	 * @param searchTerm
	 */
	public void runSearchQuery(String searchTerm)
	{
		mWebView.setVisibility(View.VISIBLE);
		mErrorView.setVisibility(View.GONE);
		mWebView.loadUrl(SEARCH_PREFIX + searchTerm);
	}
}
