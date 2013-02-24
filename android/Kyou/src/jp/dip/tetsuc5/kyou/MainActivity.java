package jp.dip.tetsuc5.kyou;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import jp.dip.tetsuc5.kyou.bean.Dokujo;
import jp.dip.tetsuc5.kyou.bean.Girlmen;
import jp.dip.tetsuc5.kyou.bean.Meigen;
import jp.dip.tetsuc5.kyou.util.Constants;
import jp.dip.tetsuc5.kyou.util.MyAsyncHttpClient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private Button btn_download;
	private Button btn_update;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		btn_download = (Button) findViewById(R.id.btn_download);
		btn_update = (Button) findViewById(R.id.btn_update);

		btn_download.setOnClickListener(this);
		btn_update.setOnClickListener(this);

		if (printMeigen()) {
			// 成功
			Log.d("KyouDebug:", "printMeigen success");
		} else {
			// 失敗
			Log.d("KyouDebug:", "printMeigen false");

		}
		if (printGirlmen()) {
			// 成功
			Log.d("KyouDebug:", "printGirlmen success");
		} else {
			// 失敗
			Log.d("KyouDebug:", "printGirlmen false");
		}
		if (printDokujo()) {
			// 成功
			Log.d("KyouDebug:", "printDokujo success");
		} else {
			// 失敗
			Log.d("KyouDebug:", "printDokujo false");
		}
	}

	@Override
	public void onClick(View v) {
		if (v == btn_download) {
			Intent i = new Intent(getApplicationContext(),
					DokujoDownloadActivity.class);
			startActivity(i);
			
			i = new Intent(getApplicationContext(),
					GirlmenDownloadActivity.class);
			startActivity(i);
		}
		if (v == btn_update) {
			printMeigen();
			printDokujo();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
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
					List<Meigen> meigenList = Meigen.readMeigen(response);

					LinearLayout layout;
					layout = (LinearLayout) findViewById(R.id.meigen_parent);
					if (layout.getChildCount() > 0) {// 子Viewが存在する場合
						layout.removeAllViews();// 動的に削除する。
					}

					for (Meigen meigen : meigenList) {

						Log.d("KyouDebug", meigen.getText());

						// TextView追加
						TextView tv = (TextView) getLayoutInflater().inflate(
								R.layout.meigen, null);
						tv.setText(meigen.getText() + "|" + meigen.getAuther());

						layout.addView(tv);

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
			List<Girlmen> girlmenList = Girlmen.readGirlmen(Constants.FILE_GIRLMEN);

			LinearLayout layout;
			layout = (LinearLayout) findViewById(R.id.girlmen_parent);
			if (layout.getChildCount() > 0) {// 子Viewが存在する場合
				layout.removeAllViews();// 動的に削除する。
			}

			for (Girlmen girlmen : girlmenList) {

				Log.d("KyouDebug", girlmen.getUrl());

				// TextView追加
				TextView tv = (TextView) getLayoutInflater().inflate(
						R.layout.girlmen, null);
//				tv.setText(girlmen.getTitle());
				tv.setText("名前いれる");

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
	// 毒女ニュース
	// /**********************************************************
	public boolean printDokujo() {

		try {
			List<Dokujo> dokujoList = Dokujo.readDokujo(Constants.FILE_DOKUJO);

			LinearLayout layout;
			layout = (LinearLayout) findViewById(R.id.dokujo_parent);
			if (layout.getChildCount() > 0) {// 子Viewが存在する場合
				layout.removeAllViews();// 動的に削除する。
			}

			for (Dokujo dokujo : dokujoList) {

				Log.d("KyouDebug", dokujo.getTitle());

				// TextView追加
				TextView tv = (TextView) getLayoutInflater().inflate(
						R.layout.dokujo, null);
				tv.setText(dokujo.getTitle());

				// 画像ファイル名取得
				String[] parts = dokujo.getImage().split("/");
				final String fileName = parts[parts.length - 1];

				Drawable dokujo_img = Drawable
						.createFromPath(Constants.DOKUJO_PATH + fileName);
				dokujo_img.setBounds(0, 0, dokujo_img.getIntrinsicWidth(),
						dokujo_img.getIntrinsicHeight());
				tv.setCompoundDrawables(dokujo_img, null, null, null);

				// Linkつける
				tv.setContentDescription(dokujo.getUrl());
				layout.addView(tv);
			}

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
}
