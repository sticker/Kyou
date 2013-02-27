package jp.dip.tetsuc5.kyou.logic;

import java.util.List;

import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;

import jp.dip.tetsuc5.kyou.bean.Girlmen;
import jp.dip.tetsuc5.kyou.util.Constants;
import jp.dip.tetsuc5.kyou.util.FileUtil;
import jp.dip.tetsuc5.kyou.util.MyAsyncHttpClient;

public class GirlmenDownload {

	/** jsonファイルのURL. */
	static final String DOWNLOAD_FILE_URL = Constants.URL_GIRLMEN;

	static final String FILE_LOCAL_PATH = Constants.FILE_GIRLMEN;

	/*-----------------------------------------------------------------------*/

	public boolean execute() {
		
		try {
			// TODO ディレクトリ作成
			// SD カード/パッケージ名 ディレクトリ生成
			FileUtil.mkdir(Constants.GIRLMEN_PATH);
			MyAsyncHttpClient client = new MyAsyncHttpClient();

			client.get(DOWNLOAD_FILE_URL, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {
					// レスポンスを保存
					FileUtil.writeFile(response.getBytes(),
							Constants.FILE_GIRLMEN);

					List<Girlmen> girlmenList = Girlmen
							.read(Constants.FILE_GIRLMEN);

					MyAsyncHttpClient getter = new MyAsyncHttpClient();

					for (final Girlmen girlmen : girlmenList) {

						Log.d("KyouDebug", girlmen.getUrl());

						// 画像ファイルを取得して保存
						String[] parts = girlmen.getImage().split("/");
						final String fileName = parts[parts.length - 1];
						getter.get(girlmen.getImage(),
								new BinaryHttpResponseHandler() {
									@Override
									public void onSuccess(byte[] image) {
										Log.d("KyouDebug", girlmen.getImage());

										FileUtil.writeFile(image,
												Constants.GIRLMEN_PATH
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
