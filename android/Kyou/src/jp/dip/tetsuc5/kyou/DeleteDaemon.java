package jp.dip.tetsuc5.kyou;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.dip.tetsuc5.kyou.bean.Dokujo;
import jp.dip.tetsuc5.kyou.bean.Girlmen;
import jp.dip.tetsuc5.kyou.bean.Matome;
import jp.dip.tetsuc5.kyou.bean.Recipe;
import jp.dip.tetsuc5.kyou.logic.DokujoDownload;
import jp.dip.tetsuc5.kyou.logic.GirlmenDownload;
import jp.dip.tetsuc5.kyou.logic.MatomeDownload;
import jp.dip.tetsuc5.kyou.logic.RecipeDownload;
import jp.dip.tetsuc5.kyou.util.Constants;
import jp.dip.tetsuc5.kyou.util.FileUtil;
import android.content.Context;
import android.util.Log;

/**
 * 常駐型サービスのサンプル。定期的にログ出力する。
 * 
 * @author id:language_and_engineering
 * 
 */
public class DeleteDaemon extends BaseDownloadDaemon {

	// 画面から常駐を解除したい場合のために，常駐インスタンスを保持
	public static BaseDownloadDaemon activeService;

	@Override
	protected long getIntervalMS() {
		return 1000 * 10;
	}

	@Override
	protected void execTask() {
		activeService = this;

		// ※もし毎回の処理が重い場合は，メインスレッドを妨害しないために
		// ここから下を別スレッドで実行する。

		// 毒女ニュースの不要ファイルを削除
		if(FileUtil.isExists(Constants.FILE_DOKUJO)){
			
			List<Dokujo> dokujoList = Dokujo.read(Constants.FILE_DOKUJO);
			ArrayList<String> fileList = FileUtil.searchFiles(Constants.DOKUJO_PATH, ".*", false);
			for(int i = 0; i < fileList.size(); i++){
				boolean delFlg = true;
				
				// .jsonファイルだったら次へ
				if(fileList.get(i).matches(".*..json")){
					continue;
				}
				for (Dokujo dokujo : dokujoList) {
					// 画像ファイル名取得
					String[] parts = dokujo.getImage().split("/");
					final String fileName = parts[parts.length - 1];
					if(fileName.equals(fileList.get(i))){
						delFlg = false;
					}
				}
				if(delFlg) {
					FileUtil.rmFile(Constants.DOKUJO_PATH + fileList.get(i));
					Log.d("DeleteDaemon", "dokujo delete:" + fileList.get(i));
				}
			}
		}
		
		// 注目ガールの不要ファイルを削除
		if(FileUtil.isExists(Constants.FILE_GIRLMEN)){
			
			List<Girlmen> GirlmenList = Girlmen.read(Constants.FILE_GIRLMEN);
			ArrayList<String> fileList = FileUtil.searchFiles(Constants.GIRLMEN_PATH, ".*", false);
			for(int i = 0; i < fileList.size(); i++){
				boolean delFlg = true;
				
				// .jsonファイルだったら次へ
				if(fileList.get(i).matches(".*..json")){
					continue;
				}
				for (Girlmen Girlmen : GirlmenList) {
					// 画像ファイル名取得
					String[] parts = Girlmen.getImage().split("/");
					final String fileName = parts[parts.length - 1];
					if(fileName.equals(fileList.get(i))){
						delFlg = false;
					}
				}
				if(delFlg) {
					FileUtil.rmFile(Constants.GIRLMEN_PATH + fileList.get(i));
					Log.d("DeleteDaemon", "Girlmen delete:" + fileList.get(i));
				}
			}
		}
		
		// NAVERまとめの不要ファイルを削除
		if(FileUtil.isExists(Constants.FILE_MATOME)){
			
			List<Matome> MatomeList = Matome.read(Constants.FILE_MATOME);
			ArrayList<String> fileList = FileUtil.searchFiles(Constants.MATOME_PATH, ".*", false);
			for(int i = 0; i < fileList.size(); i++){
				boolean delFlg = true;
				
				// .jsonファイルだったら次へ
				if(fileList.get(i).matches(".*..json")){
					continue;
				}
				for (Matome Matome : MatomeList) {
					// 画像ファイル名取得
					String[] parts = Matome.getImage().split("/");
					final String fileName = parts[parts.length - 1];
					if(fileName.equals(fileList.get(i))){
						delFlg = false;
					}
				}
				if(delFlg) {
					FileUtil.rmFile(Constants.MATOME_PATH + fileList.get(i));
					Log.d("DeleteDaemon", "Matome delete:" + fileList.get(i));
				}
			}
		}
				
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
        cal.add(Calendar.MINUTE, 6);
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