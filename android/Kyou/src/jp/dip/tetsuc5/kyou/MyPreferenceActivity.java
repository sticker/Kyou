package jp.dip.tetsuc5.kyou;

import java.util.ArrayList;
import java.util.List;

import jp.dip.tetsuc5.kyou.bean.TenkiArea;
import jp.dip.tetsuc5.kyou.util.Constants;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

public class MyPreferenceActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener{

	public PreferenceScreen screenParent;
	public PreferenceScreen screenArea;
	
	public ArrayList<String> mChangedKeys;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		// 設定画面の情報を保存するファイル名を設定
//		PreferenceManager pfm = getPreferenceManager();
//		pfm.setSharedPreferencesName(Constants.PREF_NAME);

		// Load the preferences from an XML resource
		// addPreferencesFromResource(R.xml.preference);

		// 一番の親
		screenParent = getPreferenceManager()
				.createPreferenceScreen(this);
		screenParent.setTitle(R.string.setting_parent);
		screenParent.setKey("setting_parent");

		// 子：地域設定
		screenArea = getPreferenceManager()
				.createPreferenceScreen(this);
		screenArea.setKey("setting_area");
		screenArea.setTitle(R.string.setting_area);
		// 現在地をサマリーに表示
//		SharedPreferences preferences = getSharedPreferences(
//				getString(R.string.app_name), MODE_PRIVATE);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String area = preferences.getString("area", "default");
		screenArea.setSummary(area);

		List<TenkiArea> tenkiAreaList = TenkiArea
				.read(Constants.FILE_TENKIAREA);
		for (TenkiArea tenkiArea : tenkiAreaList) {

			ListPreference listPref = new ListPreference(this);
			listPref.setDialogTitle(R.string.setting_area_dialog);
			listPref.setTitle(tenkiArea.getPref());
			listPref.setSummary("");

			listPref.setEntries(tenkiArea.getCity());
			listPref.setEntryValues(tenkiArea.getId());
			listPref.setKey("area");

			screenArea.addPreference(listPref);
		}

		screenParent.addPreference(screenArea);

		setPreferenceScreen(screenParent);
	}
	
	
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,  String key) {

		PreferenceScreen screenArea = (PreferenceScreen)getPreferenceScreen().findPreference("setting_area");
		
		
		//サマリを設定する
		String pref = sharedPreferences.getString(key, "default");
//		if("area".equals(pref)) {
//			screenArea.setSummary(pref);
//		}
		Log.d("MyPreferenceActivity", "YES!!!!:" + pref);
		screenArea.setSummary(pref);
		setPreferenceScreen(screenParent);
		
		}

    @Override
    public void onBackPressed() {
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String area = preferences.getString("area", "default");
		screenArea.setSummary(area);
		Log.d("MyPreferenceActivity", "onBackPressed!:" + area);
		
        Intent data = new Intent();
        data.putStringArrayListExtra("results", mChangedKeys);
        setResult(RESULT_OK, data);
        super.onBackPressed();
    }
    
	@Override
	protected void onResume() {
	    super.onResume();
	    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	    
	    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String area = preferences.getString("area", "default");
		Log.d("MyPreferenceActivity", "onResume!:" + area);
		screenArea.setSummary(area);
		setPreferenceScreen(screenParent);

	}
	 
	@Override
	protected void onPause() {
		Log.d("MyPreferenceActivity", "onPause");
	    super.onPause();
	    getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	} 
}
