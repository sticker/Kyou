package jp.dip.tetsuc5.kyou.logic;

import java.util.List;

import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;

import jp.dip.tetsuc5.kyou.bean.Matome;
import jp.dip.tetsuc5.kyou.util.Constants;
import jp.dip.tetsuc5.kyou.util.FileUtil;
import jp.dip.tetsuc5.kyou.util.MyAsyncHttpClient;

public class MatomeDownload {

	/** jsonファイルのURL. */
	static final String DOWNLOAD_FILE_URL = Constants.URL_MATOME;

	static final String FILE_LOCAL_PATH = Constants.FILE_MATOME;

	/*-----------------------------------------------------------------------*/

	public static boolean execute() {
		try {
			// TODO ディレクトリ作成
			// SD カード/パッケージ名 ディレクトリ生成
			FileUtil.mkdir(Constants.MATOME_PATH);

			MyAsyncHttpClient client = new MyAsyncHttpClient();

			client.get(DOWNLOAD_FILE_URL, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {
					// レスポンスを保存
					FileUtil.writeFile(response.getBytes(),
							Constants.FILE_MATOME);

					List<Matome> matomeList = Matome
							.read(Constants.FILE_MATOME);

					MyAsyncHttpClient getter = new MyAsyncHttpClient();

					for (final Matome matome : matomeList) {

						Log.d("KyouDebug", matome.getTitle());

						// 画像ファイルを取得して保存
						String[] parts = matome.getImage().split("/");
						String fileName_before = parts[parts.length - 1];
						//NAVERまとめの画像ファイルは動的取得のようなので、
						//キーとなる部分を抜き出してファイル名にする
						fileName_before = fileName_before.substring(
								fileName_before.indexOf("tbn%3A")+6);
						final String fileName = fileName_before.substring(0,
								fileName_before.indexOf("&"));
						getter.get(matome.getImage(),
								new BinaryHttpResponseHandler() {
									@Override
									public void onSuccess(byte[] image) {
										Log.d("KyouDebug", matome.getImage());

										FileUtil.writeFile(image,
												Constants.MATOME_PATH
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
