package jp.dip.tetsuc5.kyou;

import java.util.List;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;

import jp.dip.tetsuc5.kyou.bean.Dokujo;
import jp.dip.tetsuc5.kyou.util.Constants;
import jp.dip.tetsuc5.kyou.util.FileUtil;
import jp.dip.tetsuc5.kyou.util.MyAsyncHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

/**
 * HTTPサーバからファイルをレジュームダウンロードするサンプル.
 */
public class DokujoDownloadActivity extends Activity
{
	/** jsonファイルのURL. */
	static final String DOWNLOAD_FILE_URL
	= Constants.URL_DOKUJO;
	
	static final String FILE_LOCAL_PATH = Constants.FILE_DOKUJO;
	
	/*-----------------------------------------------------------------------*/

    /** Called when the activity is first created. */
    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        this.setContentView (R.layout.activity_main);
        
        Log.d("KyouDebug", "DokujoDownloadActivity start!");
        
        try {
        //TODO ディレクトリ作成
     // SD カード/パッケージ名 ディレクトリ生成  
        FileUtil.mkdir(Constants.DOKUJO_PATH);
        
        MyAsyncHttpClient client = new MyAsyncHttpClient();
        
        client.get(DOWNLOAD_FILE_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
            	// レスポンスを保存
            	FileUtil.writeFile(response.getBytes(), Constants.FILE_DOKUJO);

        		List<Dokujo> dokujoList = Dokujo.readDokujo(Constants.FILE_DOKUJO);	

                MyAsyncHttpClient getter = new MyAsyncHttpClient();

        		for(final Dokujo dokujo : dokujoList) {

        			Log.d("KyouDebug", dokujo.getTitle());
        			
        			// 画像ファイルを取得して保存
        			String[] parts = dokujo.getImage().split("/");
        			final String fileName = parts[parts.length -1];
        			getter.get(dokujo.getImage(), new BinaryHttpResponseHandler() {
        				@Override
        	            public void onSuccess(byte[] image) {
                			Log.d("KyouDebug", dokujo.getImage());
                			
        					FileUtil.writeFile(image, Constants.DOKUJO_PATH + fileName);
        				}
        			});
        		}
        		Toast toast = Toast.makeText(
            			getApplicationContext(), "Download成功！[Dokujo]", Toast.LENGTH_SHORT);
            		toast.setGravity(Gravity.TOP | Gravity.RIGHT, 0, 0);
            		toast.show();
            }
        });
        
        finish();
    } catch(Exception e){
    	e.printStackTrace();
    	Toast toast = Toast.makeText(
    			getApplicationContext(), "Download失敗！[Girlmen]", Toast.LENGTH_SHORT);
    		toast.setGravity(Gravity.TOP | Gravity.RIGHT, 0, 0);
    		toast.show();
    		finish();
    }
}}