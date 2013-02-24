package jp.dip.tetsuc5.kyou;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;

import jp.dip.tetsuc5.kyou.bean.Dokujo;
import jp.dip.tetsuc5.kyou.bean.Girlmen;
import jp.dip.tetsuc5.kyou.util.Constants;
import jp.dip.tetsuc5.kyou.util.FileUtil;
import jp.dip.tetsuc5.kyou.util.MyAsyncHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * HTTPサーバからファイルをレジュームダウンロードするサンプル.
 */
public class GirlmenDownloadActivity extends Activity
{
	/** jsonファイルのURL. */
	static final String DOWNLOAD_FILE_URL
	= Constants.URL_GIRLMEN;
	
	static final String FILE_LOCAL_PATH = Constants.FILE_GIRLMEN;
	
	/*-----------------------------------------------------------------------*/

    /** Called when the activity is first created. */
    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        this.setContentView (R.layout.activity_main);
        
        Log.d("KyouDebug", "GirlMenDownloadActivity start!");
        
        //TODO ディレクトリ作成
     // SD カード/パッケージ名 ディレクトリ生成  
        File outDir = new File(Constants.GIRLMEN_PATH);  
        // パッケージ名のディレクトリが SD カードになければ作成します。  
        if (outDir.exists() == false) {
            outDir.mkdirs();  
        }  
        
        MyAsyncHttpClient client = new MyAsyncHttpClient();
        
        client.get(DOWNLOAD_FILE_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
            	// レスポンスを保存
            	FileUtil.writeFile(response.getBytes(), Constants.FILE_GIRLMEN);

        		List<Girlmen> girlmenList = Girlmen.readGirlmen(Constants.FILE_GIRLMEN);	

                MyAsyncHttpClient getter = new MyAsyncHttpClient();

        		for(final Girlmen girlmen : girlmenList) {

        			Log.d("KyouDebug", girlmen.getUrl());
        			
        			// 画像ファイルを取得して保存
        			String[] parts = girlmen.getImage().split("/");
        			final String fileName = parts[parts.length -1];
        			getter.get(girlmen.getImage(), new BinaryHttpResponseHandler() {
        				@Override
        	            public void onSuccess(byte[] image) {
                			Log.d("KyouDebug", girlmen.getImage());
                			
        					FileUtil.writeFile(image, Constants.GIRLMEN_PATH + fileName);
        				}
        			});
        		}
            }
        });
        
        finish();
    }
}