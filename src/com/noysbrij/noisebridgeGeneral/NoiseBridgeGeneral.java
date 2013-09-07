package com.noysbrij.noisebridgeGeneral;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.webkit.*;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.concurrent.*;
import org.apache.cordova.*;
import org.apache.cordova.api.*;
import com.noysbrij.noisebridgeGeneral.*;
import com.loopj.android.http.*;
//import com.metamage.noisegate.*;

//import com.noysbrij.noisebridgeGeneral.R;
//import com.noysbrij.noisebridgeGeneral.*;

import android.support.v4.view.*;
import java.lang.ref.*;
import android.net.*;
import java.util.*;

public class NoiseBridgeGeneral extends Activity implements CordovaInterface
{
    //Cordova bits
	private CordovaPlugin activityResultCallback;
	private Object activityResultKeepRunning;
	private Object keepRunning;
	private final ExecutorService threadPool = Executors.newCachedThreadPool();
	CordovaWebView webView;
	//tag
	private final String TAG = getClass().getSimpleName();
	//number of views in the pagerview
	private final static int NUMBER_OF_PAGES = 3;
	//URL for pony ( just music for now)
	private static String ponyURL= "http://pony.noise/";
//	the pager view
	private ViewPager pagerView;
//	adapter what holds the content in the view
	private LayoutsAdapter adapter;
	//ticvkets
	Tickets tickets;
	// sucwessful json read updates listview
	private ReadJson readJson;
	private JsonRequest jsonRequest;
	private TicketsAdapter ticketsAdapter;
	protected ListView listView;
//	stuff from metamage, maybe a subclass?
//	private Noisegate noisegate;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);	
		readJson = new ReadJson();
//		noisegate = new Noisegate(getActivity(), getContext());
//		requestWindowFeature( Window.FEATURE_NO_TITLE );
		adapter = new LayoutsAdapter();
		jsonRequest = new JsonRequest();
		pagerView = (ViewPager) findViewById(R.id.pager_view);
		pagerView.setAdapter(adapter);
		listView = (ListView)findViewById(R.id.list_view);
		jsonRequest.getTickets();
//	http://pony.noise/juke/common
//		String htmlPage = getHtmlFromAsset(getString(R.string.game_page));
//		if (htmlPage != null)
//		{
//			webView.loadDataWithBaseURL(URL, htmlPage, "text/html", "UTF-8", URL);
//		}
//		else
//		{Log.e(TAG, "no html string");}
//		htmlPage = null;	

	}

	public class JsonRequest {
		public void getTickets() {
			AsyncHttpClient client = new AsyncHttpClient();

			client.get("http://noiseapp.herokuapp.com/tickets.json", new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
					//	System.out.println(response);
						try
						{
							tickets = readJson.readTickets( new ByteArrayInputStream(response.getBytes("UTF-8")) );
							//sort the tickets
							//set flag for existence of tickets
						}
						catch (IOException e)
						{}
					}
				});
		}
	}
	private class LayoutsAdapter extends PagerAdapter
	{

		@Override
		public int getCount()
		{
			return NUMBER_OF_PAGES;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) 
		{
			return (view==((View)object));
		}

		@Override
		public void destroyItem(ViewGroup collection, int position, Object view)
		{
			collection.removeView((View) view);
		}

		@Override
		public Object instantiateItem(ViewGroup collection, int position)
		{

			View layout = null;
			LayoutInflater inflater = (LayoutInflater) 
				collection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			switch (position){
				case 0:
				    //on level with webview_layout
		//			layout = inflater.inflate(R.layout.layout_noisegate, null);
					
		//			noisegate.onCreated();
//					getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
//					layout = inflater.inflate(R.layout.noisegate_layout, null);
//					final Resources resources = getResources();
//
//					Data.normalColor  = resources.getInteger( R.color.normal_control  );
//					Data.pressedColor = resources.getInteger( R.color.pressed_control );
//				
//					liveKeypad = findViewById( R.id.live_keypad );
//					fakeKeypad = findViewById( R.id.fake_keypad );
//
//					eraseKey = (Button) findViewById( R.id.erase );
//					enterKey = (Button) findViewById( R.id.enter );
//
//				//	eraseKey.setCounterpart( R.id._X );
//	                Log.e(TAG, "id: "+ R.id._X);
//
//					final TextView text = (TextView) findViewById( R.id.terminal );
//
//					tty = new Teletype(text);
//
//					tty.delayInput( inputDelay );
//
//					tty.input( getString( R.string.input ) );
		//		    WeakReference<View> noisegateView = new WeakReference<View>((View)layout.findViewById(R.id.noisegate_window));
				//	((ViewPager)collection).addView(layout, 0);
					getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
					layout = inflater.inflate(R.layout.layout_tickets, null);
					
				case 1:
								
					getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
					
					layout = inflater.inflate(R.layout.layout_webview, null);
					WeakReference<WebView> webViewRef = new WeakReference<WebView>((WebView)layout.findViewById(R.id.webview_window));

					webViewRef.get().getSettings().setJavaScriptEnabled(true);
					WebSettings webSettings0 = webViewRef.get().getSettings();

					webSettings0.setJavaScriptEnabled(true);
					webSettings0.setBuiltInZoomControls(false);
					webViewRef.get().requestFocusFromTouch();
                //    webViewRef.get().addJavascriptInterface(new jsInterface(getContext()), "Android");
					webViewRef.get().setWebViewClient(new WebViewClient()
						{
							public boolean shouldOverrideUrlLoading(WebView view, String url){
								if (url.startsWith("http:") || url.startsWith("https:")){
									return false;
								}
								Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
								startActivity(intent);
								return true;
							}

						});
					webViewRef.get().setWebChromeClient(new WebChromeClient());
			        webViewRef.get().loadUrl(ponyURL+getString(R.string.juke_page));
					((ViewPager)collection).addView(layout, 0);
		            break;
				case 2:
					
					getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

					layout = inflater.inflate(R.layout.layout_webview, null);
					WeakReference<WebView> webViewRef2 = new WeakReference<WebView>((WebView)layout.findViewById(R.id.webview_window));

					webViewRef2.get().getSettings().setJavaScriptEnabled(true);
					WebSettings webSettings2 = webViewRef2.get().getSettings();

					webSettings2.setJavaScriptEnabled(true);
					webSettings2.setBuiltInZoomControls(false);
					webViewRef2.get().requestFocusFromTouch();
					//    webViewRef.get().addJavascriptInterface(new jsInterface(getContext()), "Android");
					webViewRef2.get().setWebViewClient(new WebViewClient()
						{
							public boolean shouldOverrideUrlLoading(WebView view, String url){
								if (url.startsWith("http:") || url.startsWith("https:")){
									return false;
								}
								Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
								startActivity(intent);
								return true;
							}

						});
					webViewRef2.get().setWebChromeClient(new WebChromeClient());
			        webViewRef2.get().loadUrl(getString(R.string.wiki_page));
					((ViewPager)collection).addView((View)layout, 0);
		            break;
				case 3:
					
			}
			return layout;
		}
		@Override
        public void finishUpdate(ViewGroup arg0) {}

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {}

        @Override
        public Parcelable saveState() {
			return null;
        }

        @Override
        public void startUpdate(ViewGroup arg0) {}
	}
		
//usethisfor a view that has browsing		
//	@Override 
//	public void onPause()
//	{
//    	Method pause = null; // Pauses the webview. 
//    	try
//		{ 
//		    pause = WebView.class.getMethod("onPause"); 
//	    } 
//		catch (SecurityException e)
//		{ } 
//		catch (NoSuchMethodException e)
//		{ } 
//		if (pause != null)
//		{
//			try
//			{ pause.invoke(webView); 
//			} 
//			catch (InvocationTargetException e)
//			{ } 
//			catch (IllegalAccessException e)
//			{ } 
//		} 
//		else
//		{ 
//			// No such method. Stores the current URL. 
//		    suspendUrl = webView.getUrl(); // And loads a URL without any processing. 
//			webView.loadUrl("file:///android_asset/nothing.html"); 
//		} 
//		super.onPause(); 
//	}

//and this
//	@Override 
//	public void onResume()
//	{ 
//	    super.onResume(); 
//		Method resume = null; // Resumes the webview. 
//		try
//		{ 
//		    resume = WebView.class.getMethod("onResume"); 
//		} 
//		catch (SecurityException e)
//		{ } 
//		catch (NoSuchMethodException e)
//		{ } 
//		if (resume != null)
//		{ 
//		    try
//			{ 
//			    resume.invoke(webView); 
//			} 
//			catch (InvocationTargetException e)
//			{ } 
//			catch (IllegalAccessException e)
//			{ } 
//		} 
//		else if (webView != null)
//		{ // No such method. Restores the suspended URL. 
//		    if (suspendUrl == null)
//			{ 
//				webView.loadUrl(ponyURL+getString(R.string.juke_page));
//				
////				String htmlPage = getHtmlFromAsset(getString(R.string.game_page));
////				if (htmlPage != null)
////				{
////					webView.loadDataWithBaseURL(URL, htmlPage, "text/html", "UTF-8", URL);
////				}
////				else
////				{Log.e(TAG, "no html string");}
////				htmlPage = null;	
//			} 
//			else
//			{ 
//			    webView.loadUrl(suspendUrl); 
//			} 
//		}
//	}
		
		
	private class TicketsAdapter extends ArrayAdapter<Ticket>
	{

		private ArrayList<Ticket> tickets;

		public TicketsAdapter(Context context, int textViewResourceId, ArrayList<Ticket> tickets)
		{
			super(context, textViewResourceId, tickets);
			this.tickets = tickets;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View v = convertView;
			if (v == null)
			{
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row, null);
			}
			Ticket o = tickets.get(position);
			if (o != null)
			{
				TextView tt = (TextView) v.findViewById(R.id.toptext);
				TextView bt = (TextView) v.findViewById(R.id.bottomtext);
				if (tt != null)
				{
					tt.setText(R.string.complexity_label + o.complexity);                            }
				if (bt != null)
				{
					bt.setText(R.string.created_at_label + o.created_at);
				}
			}
			return v;
		}
	}

	public class webViewClient extends WebViewClient
	{
		public void onScaleChanged(WebView view, float oldScale, float newScale)
		{


		}

		public void onPageFinished(WebView view, String url)
		{
		 //	webView.loadDataWithBaseURL(URL, "javascript:run();", "text/html", "UTF-8", URL);
			//start the scripts here
		//	webView.loadUrl("javascript:runThree();");
	//	    webView.loadUrl("javascript:vars();");
		//	webView.loadUrl("javascript:init();");
		//	webView.loadUrl("javascript:animate();");
	//		webView.loadUrl("javascript:run();");
		//	webView.loadUrl("javascript:echo('hello werld');");
			//			webView.loadUrl("javascript:echo('hello werld');");
		//	LOG.e(TAG, "js init");
		}
	}

	public class webChromeClient extends WebChromeClient
	{
		public boolean onConsoleMessage(ConsoleMessage cm)
		{ 
			Log.d(TAG, cm.message() + " -- From line " + cm.lineNumber() + " of " + cm.sourceId()); 
			return true; 
		}
	}

	public class jsInterface
	{
		int testCounter = 0;
		// these fcns are exposed to the WebView 
		private Context cAppView;
		public jsInterface(Context appView)
		{
			cAppView = appView;
		}
		@JavascriptInterface
		public void doEchoTest(String echo)
		{
			Toast mToast = Toast.makeText(cAppView, echo, Toast.LENGTH_SHORT);
		    mToast.show();	
		}

		@JavascriptInterface
		public void webviewClose()
		{
			moveTaskToBack(true);
		}
		@JavascriptInterface
		public void threadCounter(int counter)
		{
			if (testCounter == 0)
			{
				testCounter = counter;
			}
			else
			{
				testCounter = 7;
			}
			Log.d(TAG, "counter called, testCounter = " + testCounter);
		}
	}
//	private String getHtmlFromAsset(String htmlAsset) {
//		InputStream is;
//		StringBuilder builder = new StringBuilder();
//        String htmlString = null;
//        try {
//			is = getAssets().open(htmlAsset);
//			if (is != null) {
//				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//				String line;
//                while ((line = reader.readLine()) != null) {
//				    builder.append(line);
//                }
//                htmlString = builder.toString();
//			}
//		} catch (IOException e) {e.printStackTrace();}
//
//        return htmlString;
//	}	
	public void displayTickets()
	{

		try
		{
			Log.i(TAG, "Debug #1");
			this.ticketsAdapter = new TicketsAdapter(this, R.layout.row, tickets.tickets);
			ticketsAdapter.notifyDataSetChanged();
			listView.setAdapter(ticketsAdapter);
			//	textId.setText(R.string.textIdLabel + "" + result.profileId.get(Integer.parseInt(enterId.getText().toString())).matches.get(5).score);
		}
		catch (NumberFormatException e)
		{
			Log.i(TAG, "Debug #5");
			Toast.makeText(this, R.string.bad_int, Toast.LENGTH_SHORT).show();
		}
	}	
	
	public void onStop()
	{
		super.onStop();
	}

	public static String getApplicationName(Context context)
	{ 
	    int stringId = context.getApplicationInfo().labelRes; 
		return context.getString(stringId); 
	}
	

	@Override
	public Activity getActivity()
	{
		return this;
	}

	@Override
	public void setActivityResultCallback(CordovaPlugin plugin)
	{
		this.activityResultCallback = plugin;
	}

	public void startActivityForResult(CordovaPlugin command, Intent intent, int requestCode)
	{
		this.activityResultCallback = command;
		this.activityResultKeepRunning = this.keepRunning;

// If multitasking turned on, then disable it for activities that return results
		if (command != null)
		{
			this.keepRunning = false;
		}

// Start activity
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	public void cancelLoadUrl()
	{
		//	no op
	}

	@Override
	public Object onMessage(String id, Object data)
	{
		LOG.d("is", "onMessage(" + id + "," + data + ")");
		if ("exit".equals(id))
		{
			super.finish();
		}
		return null;
	}

	@Override
	public ExecutorService getThreadPool()
	{
		return threadPool;
	}

	@Override
	@Deprecated
	public Context getContext()
	{
		return this;
	}
	
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
		//MenuItemCompat.setShowAsAction(menu.findItem(R.menu.options), 1);
		return true;
	}
	
/********noisegate functions*******/
//	private void unlockWithKey( CharSequence urlEncodedKey )
//	{
//		final String urlString = noisegateUrlBase + urlEncodedKey;
//
//		new GetAndDiscardUrlTask( this, urlString ).execute();
//	}
//
//	public void call( IOException exception )
//	{
//		fadeSubviews( fakeKeypad, 0 );
//
//		tty.stopBlinking();
//
//		tty.append( getString( R.string.complete ) );
//
//		if ( exception != null )
//		{
//			tty.setText( getString( R.string.exception ) );
//		}
//	}
//
//	private void fadeSubviews( View v, int toAlpha )
//	{
//		if ( v instanceof ViewGroup )
//		{
//			ViewGroup vg = (ViewGroup) v;
//
//			final int n = vg.getChildCount();
//
//			for ( int i = 0;  i < n;  ++i )
//			{
//				fadeSubviews( vg.getChildAt( i ), toAlpha );
//			}
//		}
//		else
//		{
//			F.setKeyColor( (Button) v, getResources().getInteger( R.color.disabled_control ) );
//
//			F.fadeViewToAlpha( v, toAlpha );
//		}
//	}
//
//	private void updateText()
//	{
//		tty.startBlinking();
//
//		tty.setText( getString( R.string.input ) + code );
//	}
//
//	public void onNumericKey( View v )
//	{
//		if ( code.length() == 0 )
//		{
//			F.fadeViewToAlpha( eraseKey, 1 );
//			F.fadeViewToAlpha( enterKey, 1 );
//		}
//
//		Button key = (Button) v;
//
//		code += key.getText();
//
//		updateText();
//	}
//
//	public void onEraseKey( View v )
//	{
//		if ( code.length() != 0 )
//		{
//			code = code.substring( 0, code.length() - 1 );
//
//			if ( code.length() == 0 )
//			{
//				F.fadeViewToAlpha( eraseKey, 0 );
//				F.fadeViewToAlpha( enterKey, 0 );
//			}
//		}
//
//		updateText();
//	}
//
//	public void onEnterKey( View v )
//	{
//		updateText();
//
//		tty.input( "\n\n" );
//
//		if ( code.length() != 0 )
//		{
//			fadeSubviews( fakeKeypad, 1 );
//
//			unlockWithKey( code );
//		}
//	}
//    @Override
//	protected Dialog onCreateDialog(int id) {
//		super.onCreateDialog(id);
//		Dialog dialog = null;
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//		switch(id) {
//			case DIALOG_LOAD_AND_PARSE_FILE:
//			    
//				builder.setTitle("Choose your file");
//				if(mFileList == null) {
//					Log.e(TAG, "Showing file picker before loading the file list");
//					dialog = builder.create();
//					return dialog;
//				}
//				builder.setItems(mFileList, new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int which) {
//							mChosenFile = mFileList[which];
//							// can do stuff with the file here toott
//							Log.d(TAG, mChosenFile + " selected");
//							File file = new File(mPath, mChosenFile);
//							if(file.isDirectory()){
//								mPath = file;
//								loadFileList();
//								onCreateDialog(1010);
//							} else {
//							//	ParseText(file);
//								webView.loadUrl("file:///"+file.getAbsolutePath());
//								webView.loadUrl("javascript:echo('"+PATH+"');");
//							}
//							
//		    			}
//					});
//				break;
//		}
//		dialog = builder.show();
//		return dialog;
//	}
}
	
	
	

