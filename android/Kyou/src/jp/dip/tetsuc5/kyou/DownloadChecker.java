package jp.dip.tetsuc5.kyou;

import java.util.List;

import jp.dip.tetsuc5.kyou.bean.Dokujo;
import jp.dip.tetsuc5.kyou.bean.Girlmen;
import jp.dip.tetsuc5.kyou.bean.Matome;
import jp.dip.tetsuc5.kyou.bean.Recipe;
import jp.dip.tetsuc5.kyou.util.Constants;
import jp.dip.tetsuc5.kyou.util.FileUtil;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class DownloadChecker extends IntentService {

	static final String TAG = "DownloadChecker";

	public DownloadChecker() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {


		Log.d("DownloadChecker","onHandleInstent start");
		
		try {

			Bundle bundle = intent.getExtras();
			if (bundle == null) {
				Log.d(TAG, "bundle == null");
				return;
			}

			int target = bundle.getInt("target");
			if (target == Constants.REQ_CODE_GIRLMEN) {
				checkGirlmen();
			}
			if (target == Constants.REQ_CODE_DOKUJO) {
				checkDokujo();
			}
			if (target == Constants.REQ_CODE_RECIPE) {
				checkRecipe();
			}
			if (target == Constants.REQ_CODE_TENKI) {
				checkTenki();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkGirlmen() {
		
		Log.d("checkGirlmen","check start");
		
		boolean check = false;
		while (!check) {

			if (!FileUtil.isExists(Constants.FILE_GIRLMEN)) {
				check = false;
				continue;
			}

			boolean image_check = true;

			List<Girlmen> girlmenList = Girlmen.read(Constants.FILE_GIRLMEN);
			for (Girlmen girlmen : girlmenList) {
				// 画像ファイル名取得
				String[] parts = girlmen.getImage().split("/");
				final String fileName = parts[parts.length - 1];

				if (!FileUtil.isExists(Constants.GIRLMEN_PATH + fileName)) {
					image_check = false;
				}
			}
			check = image_check;
		}
		
		Log.d("checkGirlmen","check finish");
		sendBroadcast(Constants.REQ_CODE_GIRLMEN);
	}

	private void checkRecipe() {
		
		Log.d("checkRecipe","check start");
		
		boolean check = false;
		while (!check) {

			if (!FileUtil.isExists(Constants.FILE_RECIPE)) {
				check = false;
				continue;
			} else {
				check = true;
			}
		}

		Log.d("checkRecipe","check finish");
		sendBroadcast(Constants.REQ_CODE_RECIPE);
	}

	private void checkDokujo() {
		
		Log.d("checkDokujo","check start");
		
		boolean check = false;
		while (!check) {

			if (!FileUtil.isExists(Constants.FILE_DOKUJO)) {
				check = false;
				continue;
			}

			boolean image_check = true;
			List<Dokujo> dokujoList = Dokujo.read(Constants.FILE_DOKUJO);
			for (Dokujo dokujo : dokujoList) {
				// 画像ファイル名取得
				String[] parts = dokujo.getImage().split("/");
				final String fileName = parts[parts.length - 1];

				if (!FileUtil.isExists(Constants.DOKUJO_PATH + fileName)) {
					image_check = false;
				}
			}

			if (!FileUtil.isExists(Constants.FILE_MATOME)) {
				check = false;
				continue;
			}
			List<Matome> matomeList = Matome.read(Constants.FILE_MATOME);
			for (Matome matome : matomeList) {
				// 画像ファイル名取得
				String[] parts = matome.getImage().split("/");
				String fileName_before = parts[parts.length - 1];
				// NAVERまとめの画像ファイルは動的取得のようなので、
				// キーとなる部分を抜き出してファイル名にする
				fileName_before = fileName_before.substring(fileName_before
						.indexOf("tbn%3A") + 6);
				final String fileName = fileName_before.substring(0,
						fileName_before.indexOf("&"));

				if (!FileUtil.isExists(Constants.MATOME_PATH + fileName)) {
					image_check = false;
				}
			}
			
			if (!FileUtil.isExists(Constants.FILE_MERRY)) {
				check = false;
				continue;
			} else {
				check = true;
			}
			
			check = image_check;
		}

		Log.d("checkDokujo","check finish");
		sendBroadcast(Constants.REQ_CODE_DOKUJO);
	}
	
	private void checkTenki() {
		
		Log.d("checkTenki","check start");
		
		boolean check = false;
		while (!check) {

			if (!FileUtil.isExists(Constants.FILE_TENKI)) {
				check = false;
				continue;
			} else {
				check = true;
			}
		}

		Log.d("checkTenki","check finish");
		sendBroadcast(Constants.REQ_CODE_TENKI);
	}

	protected void sendBroadcast(int target_result) {
		Intent broadcastIntent = new Intent();
		broadcastIntent.putExtra("target_result", target_result);
		broadcastIntent.setAction("DOWNLOAD_PROGRESS_ACTION");
		getBaseContext().sendBroadcast(broadcastIntent);
	}
}
