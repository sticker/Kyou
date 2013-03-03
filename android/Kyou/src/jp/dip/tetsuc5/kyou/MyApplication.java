package jp.dip.tetsuc5.kyou;

import java.io.File;
import java.util.UUID;

import jp.dip.tetsuc5.kyou.BaseActivity.OnActivityResultCallback;
import jp.dip.tetsuc5.kyou.util.Constants;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.SparseArray;

/**
 * Activity  いつでも破棄される可能性があるので Application で callbacks を保持
 * 但し確実ではない via http://stackoverflow.com/questions/4585627/android-application-class-lifecycle
 *
 * AndroidManifest.xml への設定を忘れずに
 */
public final class MyApplication extends Application {
	
	public static final String NAME = Constants.APP_NAME;
	public static final String TAG = NAME;
//	public static final int MP = ViewGroup.LayoutParams.FILL_PARENT;
//	public static final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

	// コンテキスト
	private static MyApplication instance;
	public MyApplication() {
		instance = this;
	}
	public static MyApplication getInstance() {
		return instance;
	}

	// ログ出力用
	public static void Logd(Object obj, String str) {
		android.util.Log.d(MyApplication.TAG,
				String.format("@%s - %s", obj.toString(), str));
	}
	public static void Loge(Object obj, Exception e) {
		e.printStackTrace();
		android.util.Log.e(MyApplication.TAG,
				String.format("@%s - %s", obj.toString(), e.toString()));
	}

	// ファイルシステム
	public static File getStorageDir() {
		File file = Environment.getExternalStorageDirectory();
		return new File(file, MyApplication.NAME);
	}
	public static boolean isStorageAvailable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	public static String makeUUIDString() {
		return UUID.randomUUID().toString();
	}

	// アプリ設定の取得
	public SharedPreferences getSharedPreferences(){
		return PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	}

	// DPとPXの相互変換
	public int dp2px(int dp) {
		return (int) (dp * getResources().getDisplayMetrics().density);
	}
	public int px2dp(int px) {
		return (int) (px / getResources().getDisplayMetrics().density);
	}
	
	
	
    private SparseArray<OnActivityResultCallback> _activityCallbacks 
        = new SparseArray<OnActivityResultCallback>();

    public OnActivityResultCallback getActivityCallback(int requestCode) {
        if (_activityCallbacks.indexOfKey(requestCode) >= 0) {
            OnActivityResultCallback found = _activityCallbacks.get(requestCode);
            _activityCallbacks.remove(requestCode);
            return found;
        } else {
            return null;
        }
    }

    public void putActivityCallback(int requestCode, OnActivityResultCallback callback) {
        _activityCallbacks.put(requestCode, callback);
    }

    public int getActivityCallbackNum() {
        return _activityCallbacks.size();
    }
}