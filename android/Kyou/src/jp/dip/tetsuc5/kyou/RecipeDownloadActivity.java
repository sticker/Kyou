package jp.dip.tetsuc5.kyou;

import java.util.List;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;

import jp.dip.tetsuc5.kyou.bean.Recipe;
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
public class RecipeDownloadActivity extends Activity {
	/** jsonファイルのURL. */
	static final String DOWNLOAD_FILE_URL = Constants.URL_RECIPE;

	static final String FILE_LOCAL_PATH = Constants.FILE_RECIPE;

	/*-----------------------------------------------------------------------*/

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);

		Log.d("KyouDebug", "RecipeDownloadActivity start!");

		try {
			// TODO ディレクトリ作成
			// SD カード/パッケージ名 ディレクトリ生成
			FileUtil.mkdir(Constants.RECIPE_PATH);
			MyAsyncHttpClient client = new MyAsyncHttpClient();

			client.get(DOWNLOAD_FILE_URL, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {
					// レスポンスを保存
					FileUtil.writeFile(response.getBytes(),
							Constants.FILE_RECIPE);

					Toast toast = Toast.makeText(getApplicationContext(),
							"Download成功！[Recipe]", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP | Gravity.RIGHT, 0, 0);
					toast.show();
				}
			});

			setResult(Constants.OK);
			finish();
		} catch (Exception e) {
			e.printStackTrace();
			Toast toast = Toast.makeText(getApplicationContext(),
					"Download失敗！[Recipe]", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.TOP | Gravity.RIGHT, 0, 0);
			toast.show();
			
			setResult(Constants.NG);
			finish();
		}
	}
}