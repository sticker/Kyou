package jp.dip.tetsuc5.kyou;

import java.util.Calendar;

import jp.dip.tetsuc5.kyou.logic.DokujoDownload;
import jp.dip.tetsuc5.kyou.logic.GirlmenDownload;
import jp.dip.tetsuc5.kyou.logic.MatomeDownload;
import jp.dip.tetsuc5.kyou.logic.RecipeDownload;
import android.content.Context;

/**
 * 常駐型サービスのサンプル。定期的にログ出力する。
 * 
 * @author id:language_and_engineering
 * 
 */
public class DownloadDaemon extends BaseDownloadDaemon {

	// 画面から常駐を解除したい場合のために，常駐インスタンスを保持
	public static BaseDownloadDaemon activeService;

	public DokujoDownload dokujoDownload = new DokujoDownload();
	public GirlmenDownload girlmenDownload = new GirlmenDownload();
	public MatomeDownload matomeDownload = new MatomeDownload();
	public RecipeDownload recipeDownload = new RecipeDownload();

	@Override
	protected long getIntervalMS() {
		return 1000 * 10;
	}

	@Override
	protected void execTask() {
		activeService = this;

		// ※もし毎回の処理が重い場合は，メインスレッドを妨害しないために
		// ここから下を別スレッドで実行する。

		dokujoDownload.execute();
		girlmenDownload.execute();
		matomeDownload.execute();
		recipeDownload.execute();
		
		// 次回の実行について計画を立てる
		makeNextPlan();
	}

	@Override
	public void makeNextPlan() {
		
		long now = System.currentTimeMillis();
		Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(now);
//        cal.add(Calendar.DATE, 1); //翌日
//        cal.set(Calendar.HOUR_OF_DAY, 4); //4時
//        cal.set(Calendar.MINUTE, 0);
        cal.add(Calendar.MINUTE, 5);
        cal.set(Calendar.SECOND, 0);
		this.scheduleNextTime(cal);
	}

	/**
	 * もし起動していたら，常駐を解除する
	 */
	public static void stopResidentIfActive(Context context) {
		if (activeService != null) {
			activeService.stopResident(context);
		}
	}

}