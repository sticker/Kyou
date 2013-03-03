package jp.dip.tetsuc5.kyou.logic;

import android.view.Gravity;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import jp.dip.tetsuc5.kyou.util.Constants;
import jp.dip.tetsuc5.kyou.util.FileUtil;
import jp.dip.tetsuc5.kyou.util.MyAsyncHttpClient;

public class MerryDownload {

	/** jsonファイルのURL. */
	static final String DOWNLOAD_FILE_URL = Constants.URL_MERRY;

	static final String FILE_LOCAL_PATH = Constants.FILE_MERRY;

	/*-----------------------------------------------------------------------*/

	public static boolean execute() {
		
		try {
			// TODO ディレクトリ作成
			// SD カード/パッケージ名 ディレクトリ生成
			FileUtil.mkdir(Constants.MERRY_PATH);

			MyAsyncHttpClient client = new MyAsyncHttpClient();

			client.get(DOWNLOAD_FILE_URL, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {
					// レスポンスを保存
					FileUtil.writeFile(response.getBytes(),
							Constants.FILE_MERRY);
				}
			});
			
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
