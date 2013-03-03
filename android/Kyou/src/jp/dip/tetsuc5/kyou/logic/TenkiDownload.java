package jp.dip.tetsuc5.kyou.logic;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import jp.dip.tetsuc5.kyou.MainActivity;
import jp.dip.tetsuc5.kyou.MyApplication;
import jp.dip.tetsuc5.kyou.util.Constants;
import jp.dip.tetsuc5.kyou.util.FileUtil;
import jp.dip.tetsuc5.kyou.util.MyAsyncHttpClient;

public class TenkiDownload {

	/** jsonファイルのURL. */
	static final String DOWNLOAD_FILE_URL = Constants.URL_TENKI;

	static final String FILE_LOCAL_PATH = Constants.FILE_TENKI;

	/*-----------------------------------------------------------------------*/

	public static boolean execute() {
		
		try {
			// TODO ディレクトリ作成
			// SD カード/パッケージ名 ディレクトリ生成
			FileUtil.mkdir(Constants.TENKI_PATH);

			String area = MyApplication.getInstance().getSharedPreferences().getString("area", "default");
			if(area == null || "default".equals(area)){
				area = Constants.TENKI_DEFAULT_AREA;
			}
			String url = DOWNLOAD_FILE_URL + "city=" + area;
			
			MyAsyncHttpClient client = new MyAsyncHttpClient();
			client.get(url, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {
					// レスポンスを保存
					FileUtil.writeFile(response.getBytes(),
							Constants.FILE_TENKI);
				}
			});
			
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
