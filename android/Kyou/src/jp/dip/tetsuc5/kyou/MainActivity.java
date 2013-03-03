package jp.dip.tetsuc5.kyou;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jp.dip.tetsuc5.kyou.bean.Dokujo;
import jp.dip.tetsuc5.kyou.bean.Girlmen;
import jp.dip.tetsuc5.kyou.bean.Matome;
import jp.dip.tetsuc5.kyou.bean.Meigen;
import jp.dip.tetsuc5.kyou.bean.Merry;
import jp.dip.tetsuc5.kyou.bean.Recipe;
import jp.dip.tetsuc5.kyou.bean.Tenki;
import jp.dip.tetsuc5.kyou.logic.DokujoDownload;
import jp.dip.tetsuc5.kyou.logic.GirlmenDownload;
import jp.dip.tetsuc5.kyou.logic.MatomeDownload;
import jp.dip.tetsuc5.kyou.logic.MerryDownload;
import jp.dip.tetsuc5.kyou.logic.RecipeDownload;
import jp.dip.tetsuc5.kyou.logic.TenkiDownload;
import jp.dip.tetsuc5.kyou.util.Constants;
import jp.dip.tetsuc5.kyou.util.FileUtil;
import jp.dip.tetsuc5.kyou.util.MyAsyncHttpClient;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

	DownloadProgressBroadcastReceiver progressReceiver;
	IntentFilter intentFilter;

	private AdView adView;
	// private Button btn_download;
	// private Button btn_update;
	private float _rate;
	private float _scale;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		registerDownloadBroadcastReceiver();

		// サイズ調整（480px横幅を想定）
		Bitmap _bm = BitmapFactory.decodeResource(getResources(),
				R.drawable.stone);
		int _w = ((WindowManager) getSystemService(WINDOW_SERVICE))
				.getDefaultDisplay().getWidth();
		_rate = (float) _bm.getHeight() / (float) _bm.getWidth();
		_scale = (float) _w / (float) _bm.getWidth();

		//トップ行の設定
		TextView top_date = (TextView)findViewById(R.id.top_date);
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("MM/dd(E)", Locale.JAPAN);
		String dateString = df.format(date);
		if(dateString.startsWith("0")) dateString = dateString.substring(1);
		dateString = dateString.replace("/0", "/");
		top_date.setText(dateString);
		top_date.setTextSize(Constants.TEXTSIZE_TOPDATE * _scale);
		
		// フォント設定
		Typeface face = Typeface.createFromAsset(getAssets(),
				Constants.FONT_TOPDATE);
		top_date.setTypeface(face);
		
		
		// 見出し行の設定
		Drawable title_img = getResources().getDrawable(R.drawable.nico);
		title_img.setBounds(0, 0, Constants.IMAGESIZE_TITLE,
				Constants.IMAGESIZE_TITLE);
		TextView title = (TextView) findViewById(R.id.meigen_title);
		title.setCompoundDrawables(title_img, null, null, null);
		title = (TextView) findViewById(R.id.girlmen_title);
		title.setCompoundDrawables(title_img, null, null, null);
		title = (TextView) findViewById(R.id.dokujo_title);
		title.setCompoundDrawables(title_img, null, null, null);
		title = (TextView) findViewById(R.id.recipe_title);
		title.setCompoundDrawables(title_img, null, null, null);

		// btn_download = (Button) findViewById(R.id.btn_download);
		// btn_update = (Button) findViewById(R.id.btn_update);
		//
		// btn_download.setOnClickListener(this);
		// btn_update.setOnClickListener(this);

		// JSONファイルが存在したら注目ガール・男子取得
		// 存在しなければまずダウンロード
		if (!FileUtil.isExists(Constants.FILE_GIRLMEN)) {
			if (GirlmenDownload.execute()) {
				Intent intent = new Intent(this, DownloadChecker.class);
				intent.putExtra("target", Constants.REQ_CODE_GIRLMEN);
				startService(intent);
			}

		} else {

			if (printGirlmen()) {
				// 成功
				Log.d("KyouDebug:", "printGirlmen success");
			} else {
				// 失敗
				Log.d("KyouDebug:", "printGirlmen false");
			}
		}

		// JSONファイルが存在したら毒女ニュース取得
		// 存在しなければまずダウンロード
		if (!FileUtil.isExists(Constants.FILE_DOKUJO)) {

			if (DokujoDownload.execute()) {
				if (MatomeDownload.execute()) {
					if (MerryDownload.execute()) {
						Intent intent = new Intent(this, DownloadChecker.class);
						intent.putExtra("target", Constants.REQ_CODE_DOKUJO);
						startService(intent);
					}
				}
			}

		} else if (!FileUtil.isExists(Constants.FILE_MATOME)) {

			if (MatomeDownload.execute()) {
				if (MerryDownload.execute()) {
					Intent intent = new Intent(this, DownloadChecker.class);
					intent.putExtra("target", Constants.REQ_CODE_DOKUJO);
					startService(intent);
				}
			}
		} else if (!FileUtil.isExists(Constants.FILE_MERRY)) {
			if (MerryDownload.execute()) {
				Intent intent = new Intent(this, DownloadChecker.class);
				intent.putExtra("target", Constants.REQ_CODE_DOKUJO);
				startService(intent);
			}
		} else {
			if (printDokujo()) {
				// 成功
				Log.d("KyouDebug:", "printDokujo success");
			} else {
				// 失敗
				Log.d("KyouDebug:", "printDokujo false");
			}
		}

		// JSONファイルが存在したらつぶやきレシピ取得
		// 存在しなければまずダウンロード
		if (!FileUtil.isExists(Constants.FILE_RECIPE)) {
			if (RecipeDownload.execute()) {
				Intent intent = new Intent(getBaseContext(),
						DownloadChecker.class);
				intent.putExtra("target", Constants.REQ_CODE_RECIPE);
				startService(intent);
			}
		} else {

			if (printRecipe()) {
				// 成功
				Log.d("KyouDebug:", "printRecipe success");
			} else {
				// 失敗
				Log.d("KyouDebug:", "printRecipe false");
			}
		}
		
		// JSONファイルが存在したら天気取得
				// 存在しなければまずダウンロード
				if (!FileUtil.isExists(Constants.FILE_TENKI)) {
					if (TenkiDownload.execute()) {
						Intent intent = new Intent(getBaseContext(),
								DownloadChecker.class);
						intent.putExtra("target", Constants.REQ_CODE_TENKI);
						startService(intent);
					}
				} else {

					if (printTenki()) {
						// 成功
						Log.d("KyouDebug:", "printTenki success");
					} else {
						// 失敗
						Log.d("KyouDebug:", "printTenki false");
					}
				}

		// 名言取得
		if (printMeigen()) {
			// 成功
			Log.d("KyouDebug:", "printMeigen success");
		} else {
			// 失敗
			Log.d("KyouDebug:", "printMeigen false");
		}
	}

	// @Override
	// public void onClick(View v) {
	// if (v == btn_download) {
	// // DokujoDownload DokujoDownload = new DokujoDownload();
	// // DokujoDownload.execute();
	// //
	// // GirlmenDownload girlmenDownload = new GirlmenDownload();
	// // girlmenDownload.execute();
	// //
	// // MatomeDownload matomeDownload = new MatomeDownload();
	// // matomeDownload.execute();
	// //
	// // RecipeDownload recipeDownload = new RecipeDownload();
	// // recipeDownload.execute();
	//
	// new DownloadDaemon().startResident(this);
	//
	// }
	// if (v == btn_update) {
	// printGirlmen();
	// printDokujo();
	// printMeigen();
	// printRecipe();
	//
	// DownloadDaemon.stopResidentIfActive(this);
	// }
	// }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_setting:
			Intent i = new Intent(getApplicationContext(),
					MyPreferenceActivity.class);
			startActivity(i);
			return true;
		case R.id.menu_refresh:
			refresh();
			return true;
		case R.id.menu_autoon:
			new DownloadDaemon().startResident(this);
			new DeleteDaemon().startResident(this);
			return true;
		case R.id.menu_autooff:
			DownloadDaemon.stopResidentIfActive(this);
			DeleteDaemon.stopResidentIfActive(this);
			return true;
		}
		return false;
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(progressReceiver);
		if(adView != null) adView.destroy();
		super.onDestroy();
	}

	public void refresh() {

		if (GirlmenDownload.execute()) {
			Intent intent = new Intent(this, DownloadChecker.class);
			intent.putExtra("target", Constants.REQ_CODE_GIRLMEN);
			startService(intent);
		}

		if (DokujoDownload.execute()) {
			if (MatomeDownload.execute()) {
				if (MerryDownload.execute()) {
					Intent intent = new Intent(this, DownloadChecker.class);
					intent.putExtra("target", Constants.REQ_CODE_DOKUJO);
					startService(intent);
				}
			}
		}

		if (RecipeDownload.execute()) {
			Intent intent = new Intent(getBaseContext(), DownloadChecker.class);
			intent.putExtra("target", Constants.REQ_CODE_RECIPE);
			startService(intent);
		}
		
		if (TenkiDownload.execute()) {
			Intent intent = new Intent(getBaseContext(), DownloadChecker.class);
			intent.putExtra("target", Constants.REQ_CODE_TENKI);
			startService(intent);
		}

		// 名言取得
		if (printMeigen()) {
			// 成功
			Log.d("KyouDebug:", "printMeigen success");
		} else {
			// 失敗
			Log.d("KyouDebug:", "printMeigen false");
		}
	}

	// /**********************************************************
	// ポジティブ名言
	// /**********************************************************
	public boolean printMeigen() {

		try {

			MyAsyncHttpClient client = new MyAsyncHttpClient();

			client.get(Constants.URL_MEIGEN, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {
					List<Meigen> meigenList = Meigen.read(response);

					LinearLayout layout;
					layout = (LinearLayout) findViewById(R.id.meigen_parent);
					if (layout.getChildCount() > 0) {// 子Viewが存在する場合
						layout.removeAllViews();// 動的に削除する。
					}

					for (Meigen meigen : meigenList) {

						Log.d("KyouDebug", meigen.getText());

						// TextView追加（本文）
						TextView tv = (TextView) getLayoutInflater().inflate(
								R.layout.meigen, null);
						tv.setText(meigen.getText());

						// フォント設定
						Typeface face = Typeface.createFromAsset(getAssets(),
								Constants.FONT_MEIGEN);
						tv.setTypeface(face);
						tv.setTextSize(Constants.TEXTSIZE_MEIGEN * _scale);

						layout.addView(tv);

						// TextView追加（作者）
						TextView tv_auther = (TextView) getLayoutInflater()
								.inflate(R.layout.meigen, null);
						tv_auther.setText(meigen.getAuther());

						// フォント設定
						tv_auther.setTypeface(face);
						tv_auther.setTextSize(Constants.TEXTSIZE_MEIGEN / 2
								* _scale);
						tv_auther.setGravity(Gravity.RIGHT);

						layout.addView(tv_auther);

					}
				}
			});

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// /**********************************************************
	// 今日の注目ガール・おしゃれ男子
	// /**********************************************************
	public boolean printGirlmen() {

		try {
			List<Girlmen> girlmenList = Girlmen.read(Constants.FILE_GIRLMEN);

			// if (girlmenList == null) {
			// Intent i = new Intent(getApplicationContext(),
			// GirlmenDownloadActivity.class);
			// startActivity(i);
			//
			// }

			LinearLayout layout;
			layout = (LinearLayout) findViewById(R.id.girlmen_parent);
			if (layout.getChildCount() > 0) {// 子Viewが存在する場合
				layout.removeAllViews();// 動的に削除する。
			}

			for (Girlmen girlmen : girlmenList) {

				Log.d("KyouDebug", girlmen.getUrl());
				Log.d("KyouDebug", girlmen.getProfile());

				// TextView追加
				TextView tv = (TextView) getLayoutInflater().inflate(
						R.layout.girlmen, null);
				// tv.setText(girlmen.getTitle());
				tv.setText(girlmen.getProfile());

				// 画像ファイル名取得
				String[] parts = girlmen.getImage().split("/");
				final String fileName = parts[parts.length - 1];

				Drawable girlmen_img = Drawable
						.createFromPath(Constants.GIRLMEN_PATH + fileName);
				girlmen_img.setBounds(0, 0, girlmen_img.getIntrinsicWidth(),
						girlmen_img.getIntrinsicHeight());
				tv.setCompoundDrawables(null, girlmen_img, null, null);

				// Linkつける
				tv.setContentDescription(girlmen.getUrl());
				layout.addView(tv);
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void onClickGirlmen(View view) {

		String url = view.getContentDescription().toString();
		Log.d("KyouDebug:onClickGirlmen", url);

		// Webブラウザの呼び出し
		Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
		startActivity(intent);
	}

	// /**********************************************************
	// 注目ニュース（毒女ニュース・NAVERまとめ）
	// /**********************************************************
	public boolean printDokujo() {

		try {
			List<Dokujo> dokujoList = Dokujo.read(Constants.FILE_DOKUJO);
			List<Matome> matomeList = Matome.read(Constants.FILE_MATOME);
			List<Merry> merryList = Merry.read(Constants.FILE_MERRY);

			// if (dokujoList == null) {
			// Intent i = new Intent(getApplicationContext(),
			// DokujoDownloadActivity.class);
			// startActivity(i);
			// }
			// if (matomeList == null) {
			// Intent i = new Intent(getApplicationContext(),
			// MatomeDownloadActivity.class);
			// startActivity(i);
			// }

			LinearLayout layout;
			layout = (LinearLayout) findViewById(R.id.dokujo_parent);
			if (layout.getChildCount() > 0) {// 子Viewが存在する場合
				layout.removeAllViews();// 動的に削除する。
			}

			// 毒女ニュース表示
			for (Dokujo dokujo : dokujoList) {

				Log.d("KyouDebug", dokujo.getTitle() + "【毒女ニュース】");

				// TextView追加
				TextView tv = (TextView) getLayoutInflater().inflate(
						R.layout.dokujo, null);
				tv.setText(dokujo.getTitle());

				// 画像ファイル名取得
				String[] parts = dokujo.getImage().split("/");
				final String fileName = parts[parts.length - 1];

				Drawable dokujo_img = Drawable
						.createFromPath(Constants.DOKUJO_PATH + fileName);
				// dokujo_img.setBounds(0, 0, dokujo_img.getIntrinsicWidth(),
				// dokujo_img.getIntrinsicHeight());
				dokujo_img.setBounds(0, 0, Constants.IMAGESIZE_DOKUJO,
						Constants.IMAGESIZE_DOKUJO);

				Drawable dokujo_site_img = getResources().getDrawable(
						R.drawable.dokujo);
				dokujo_site_img.setBounds(0, 0, Constants.IMAGESIZE_DOKUJO,
						Constants.IMAGESIZE_DOKUJO);

				tv.setCompoundDrawables(dokujo_img, null, dokujo_site_img, null);

				// Linkつける
				tv.setContentDescription(dokujo.getUrl());
				layout.addView(tv);
			}

			// NAVERまとめ表示
			for (Matome matome : matomeList) {

				Log.d("KyouDebug", matome.getTitle() + "【NAVERまとめ】");

				// TextView追加
				TextView tv = (TextView) getLayoutInflater().inflate(
						R.layout.dokujo, null);
				tv.setText(matome.getTitle());

				// 画像ファイル名取得
				String[] parts = matome.getImage().split("/");
				String fileName_before = parts[parts.length - 1];
				// NAVERまとめの画像ファイルは動的取得のようなので、
				// キーとなる部分を抜き出してファイル名にする
				fileName_before = fileName_before.substring(fileName_before
						.indexOf("tbn%3A") + 6);
				final String fileName = fileName_before.substring(0,
						fileName_before.indexOf("&"));

				Drawable matome_img = Drawable
						.createFromPath(Constants.MATOME_PATH + fileName);
				matome_img.setBounds(0, 0, Constants.IMAGESIZE_DOKUJO,
						Constants.IMAGESIZE_DOKUJO);

				Drawable matome_site_img = getResources().getDrawable(
						R.drawable.naver);
				matome_site_img.setBounds(0, 0, Constants.IMAGESIZE_DOKUJO,
						Constants.IMAGESIZE_DOKUJO);

				tv.setCompoundDrawables(matome_img, null, matome_site_img, null);

				// Linkつける
				tv.setContentDescription(matome.getUrl());
				layout.addView(tv);
			}

			// Merryニュース表示
			for (Merry merry : merryList) {

				Log.d("KyouDebug", merry.getTitle() + "【Merryニュース】");

				// TextView追加
				TextView tv = (TextView) getLayoutInflater().inflate(
						R.layout.dokujo, null);
				tv.setText(merry.getTitle());

				Drawable merry_img = getResources().getDrawable(
						R.drawable.merry_head);
				merry_img.setBounds(0, 0, Constants.IMAGESIZE_DOKUJO,
						Constants.IMAGESIZE_DOKUJO);

				Drawable merry_site_img = getResources().getDrawable(
						R.drawable.merry);
				merry_site_img.setBounds(0, 0, Constants.IMAGESIZE_DOKUJO,
						Constants.IMAGESIZE_DOKUJO);

				tv.setCompoundDrawables(merry_img, null, merry_site_img, null);

				// Linkつける
				tv.setContentDescription(merry.getUrl());
				layout.addView(tv);
			}

			adView = new AdView(this, AdSize.BANNER, Constants.ID_ADMOB);
			layout.addView(adView);
			AdRequest adRequest = new AdRequest();
			adRequest.addTestDevice("CB511WWX9M"); // Android 端末をテスト
			adView.loadAd(adRequest);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void onClickDokujo(View view) {

		String url = view.getContentDescription().toString();
		Log.d("KyouDebug:onClickDokujo", url);

		// Webブラウザの呼び出し
		Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
		startActivity(intent);
	}

	// /**********************************************************
	// つぶやきレシピ
	// /**********************************************************
	public boolean printRecipe() {

		try {
			List<Recipe> recipeList = Recipe.read(Constants.FILE_RECIPE);

			// if (recipeList == null) {
			// Intent i = new Intent(getApplicationContext(),
			// RecipeDownloadActivity.class);
			// startActivity(i);
			// }

			LinearLayout layout;
			layout = (LinearLayout) findViewById(R.id.recipe_parent);
			if (layout.getChildCount() > 0) {// 子Viewが存在する場合
				layout.removeAllViews();// 動的に削除する。
			}

			for (Recipe recipe : recipeList) {

				if (recipe.getTitle() != null) {
					Log.d("KyouDebug", recipe.getUrl());

					// TextView追加
					TextView tv = (TextView) getLayoutInflater().inflate(
							R.layout.recipe, null);
					tv.setText(recipe.getTitle());

					// Linkつける
					tv.setContentDescription(recipe.getUrl());
					layout.addView(tv);
				}
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// /**********************************************************
	// 天気
	// /**********************************************************
	public boolean printTenki() {

		try {
			List<Tenki> tenkiList = Tenki.read(Constants.FILE_TENKI);

			LinearLayout layout;
			layout = (LinearLayout) findViewById(R.id.top_parent);
			if (layout.getChildCount() > 0) {// 子Viewが存在する場合
				layout.removeAllViews();// 動的に削除する。
			}

			for (Tenki tenki : tenkiList) {

				//晴れの時だけ表示する
//				if ("晴れ".equals(tenki.getTelop())) {
					Log.d("KyouDebug", tenki.getDate() + ":" + tenki.getTelop());

					// TextView追加
					TextView tv = (TextView) getLayoutInflater().inflate(
							R.layout.tenki, null);
					tv.setText(tenki.getDateLabel() + "は" + tenki.getTelop() + " !!");

					// Linkつける
//					tv.setContentDescription(tenki.getUrl());
					
					layout.addView(tv);
//				}
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	protected void registerDownloadBroadcastReceiver() {
		progressReceiver = new DownloadProgressBroadcastReceiver();
		intentFilter = new IntentFilter();
		intentFilter.addAction("DOWNLOAD_PROGRESS_ACTION");
		registerReceiver(progressReceiver, intentFilter);
	}

	public class DownloadProgressBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// Show Progress
			Bundle bundle = intent.getExtras();
			int target_result = bundle.getInt("target_result");
			if (target_result == Constants.REQ_CODE_GIRLMEN) {
				printGirlmen();
			}
			if (target_result == Constants.REQ_CODE_DOKUJO) {
				printDokujo();
			}
			if (target_result == Constants.REQ_CODE_RECIPE) {
				printRecipe();
			}
			if (target_result == Constants.REQ_CODE_TENKI) {
				printTenki();
			}

		}
	}
}
