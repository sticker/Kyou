package jp.dip.tetsuc5.kyou.util;

import com.loopj.android.http.AsyncHttpClient;

public class MyAsyncHttpClient extends AsyncHttpClient {
	
	public MyAsyncHttpClient(){
		
		setUserAgent("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.0; Trident/5.0)");
		setTimeout(1000);
		 
	}

}
