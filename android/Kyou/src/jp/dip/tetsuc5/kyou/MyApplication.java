package jp.dip.tetsuc5.kyou;

import jp.dip.tetsuc5.kyou.BaseActivity.OnActivityResultCallback;
import android.app.Application;
import android.util.SparseArray;

/**
 * Activity  いつでも破棄される可能性があるので Application で callbacks を保持
 * 但し確実ではない via http://stackoverflow.com/questions/4585627/android-application-class-lifecycle
 *
 * AndroidManifest.xml への設定を忘れずに
 */
public final class MyApplication extends Application {
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