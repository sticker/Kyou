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
import jp.dip.tetsuc5.kyou.bean.JsonBean;
import jp.dip.tetsuc5.kyou.util.Constants;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

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
	private Button btn_draw;
	private TextView txt_dokujo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
     
		setContentView(R.layout.activity_main);

		btn_download = (Button) findViewById(R.id.btn_download);
		btn_draw = (Button) findViewById(R.id.btn_draw);

		btn_download.setOnClickListener(this);
		btn_draw.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == btn_download) {
			Intent i = new Intent(getApplicationContext(),
					DokujoDownloadActivity.class);
			startActivity(i);
		}
		if (v == btn_draw) {
			Intent i = new Intent(getApplicationContext(),
					DokujoActivity.class);
			Log.d("KyouDebug", "btn_draw before!");
			startActivity(i);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}


	
	//失敗
	public List readJsonFileToList(String file_name, JsonBean bean) {

		List<JsonBean> jsonList = null;
		InputStreamReader isr = null;

		try {
			isr = new InputStreamReader(new FileInputStream(file_name));
			JsonReader jsr = new JsonReader(isr);
			Gson mygson = new Gson();
			Type collectionType = new TypeToken<Collection<JsonBean>>() {
			}.getType();
			jsonList = mygson.fromJson(jsr, collectionType);

		} catch (FileNotFoundException e) {
			// System.out.println("ファイルが見つかりません。");
			e.printStackTrace();
		} finally {
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
					// System.out.println("入出力エラーです。");
					e.printStackTrace();
				}
			}
		}

		return jsonList;
	}

}
