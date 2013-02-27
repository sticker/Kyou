package jp.dip.tetsuc5.kyou.logic;

import java.util.List;

import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;

import jp.dip.tetsuc5.kyou.bean.Dokujo;
import jp.dip.tetsuc5.kyou.util.Constants;
import jp.dip.tetsuc5.kyou.util.FileUtil;
import jp.dip.tetsuc5.kyou.util.MyAsyncHttpClient;

public class DokujoDownload {

	/** jsonファイルのURL. */
	static final String DOWNLOAD_FILE_URL = Constants.URL_DOKUJO;

	static final String FILE_LOCAL_PATH = Constants.FILE_DOKUJO;

	/*-----------------------------------------------------------------------*/

	public boolean execute(){
		
		try {
			// TODO ディレクトリ作成
			// SD カード/パッケージ名 ディレクトリ生成
			FileUtil.mkdir(Constants.DOKUJO_PATH);

			MyAsyncHttpClient client = new MyAsyncHttpClient();

			client.get(DOWNLOAD_FILE_URL, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {
					// レスポンスを保存
					FileUtil.writeFile(response.getBytes(),
							Constants.FILE_DOKUJO);

					List<Dokujo> dokujoList = Dokujo
							.read(Constants.FILE_DOKUJO);

					MyAsyncHttpClient getter = new MyAsyncHttpClient();

					for (final Dokujo dokujo : dokujoList) {

						Log.d("KyouDebug", dokujo.getTitle());

						// 画像ファイルを取得して保存
						String[] parts = dokujo.getImage().split("/");
						final String fileName = parts[parts.length - 1];
						getter.get(dokujo.getImage(),
								new BinaryHttpResponseHandler() {
									@Override
									public void onSuccess(byte[] image) {
										Log.d("KyouDebug", dokujo.getImage());

										FileUtil.writeFile(image,
												Constants.DOKUJO_PATH
														+ fileName);
									}
								});
					}
				}
			});
			
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			
		}
	}
}
