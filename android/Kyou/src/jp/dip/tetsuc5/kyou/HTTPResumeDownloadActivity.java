package jp.dip.tetsuc5.kyou;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

/**
 * HTTPサーバからファイルをレジュームダウンロードするサンプル.
 */
public class HTTPResumeDownloadActivity extends Activity
{
	/** ダウンロードを行うためのファイルのURL. */
	static final String DOWNLOAD_FILE_URL
	= "http://tetsuc5.dip.jp/Kyou/dokujo.json";

	/** ローカルに保存するファイルのパス */
	static final String FILE_LOCAL_PATH
	= Environment.getExternalStorageDirectory ().getAbsolutePath () + File.separator + "dokujo.json";

	/*-----------------------------------------------------------------------*/

    /** Called when the activity is first created. */
    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        this.setContentView (R.layout.activity_main);

		AsyncDownloadTask task = new AsyncDownloadTask ();
		task.execute (0);
		
		Intent i = new Intent(getApplicationContext(),MainActivity.class);
		startActivity(i);
    }

	/*-----------------------------------------------------------------------*/

	/** ダウンロード処理を行うタスク */
	private class AsyncDownloadTask extends AsyncTask <Integer, Integer, Integer>
	{
		@Override
		public void onPreExecute ()
		{
			// Spinner Progress Dialog を表示
			this . progressDialog = new ProgressDialog (HTTPResumeDownloadActivity . this);
			this . progressDialog . setMessage ("Downloading");
			this . progressDialog . setButton (  
				DialogInterface . BUTTON_NEGATIVE,  
				"Cancel",  
				new DialogInterface . OnClickListener () {
					public void onClick (DialogInterface dialog, int which) {  
						AsyncDownloadTask . this . cancel (false);
					}
				}  
			); 
			this . progressDialog . setIndeterminate (false);
			this . progressDialog . setProgressStyle (ProgressDialog . STYLE_HORIZONTAL);
			this . progressDialog . setMax (0);
			this . progressDialog . setProgress (0);
			this . progressDialog . show ();
		}
		
		/*-------------------------------------------------------------------*/
		
		@Override
		public Integer doInBackground (Integer...ARGS)
		{
			// ダウンロード用のテンポラリファイルオブジェクト
			File temporaryFile = new File (FILE_LOCAL_PATH + ".tmp");
			if( temporaryFile.exists () )
			{
				this . downloadFileSize = 0;
				this . downloadFileSizeCount = (int) temporaryFile.length ();
			}
			else {
				this . downloadFileSize = 0;
				this . downloadFileSizeCount = 0;

				// ダウンロード用に新規ファイルを作成する
				try { temporaryFile.createNewFile (); }
				catch( IOException exception ) {
					exception.printStackTrace ();
				}
			}

			// 完了フラグ
			boolean downloadComplete = false;
			boolean downloadCancel = false;
			try {
				// ダウンロードするファイルのサイズを取得する
				{
					URL url = new URL (DOWNLOAD_FILE_URL);
					HttpURLConnection httpURLConnection = (HttpURLConnection) url . openConnection ();

					httpURLConnection . setRequestMethod ("HEAD");
					httpURLConnection . connect ();
					if( httpURLConnection . getResponseCode () == 200 ) {
						this.downloadFileSize = httpURLConnection . getContentLength ();
					}
				}
				// 実際のダウンロード処理
				{
					URL url = new URL (DOWNLOAD_FILE_URL);
					HttpURLConnection httpURLConnection = (HttpURLConnection) url . openConnection ();

					httpURLConnection.setRequestMethod ("GET");
					httpURLConnection.setRequestProperty (
						"Range",
						String.format (
							"bytes=%d-%d",
							this.downloadFileSizeCount,
							this.downloadFileSize
						)
					);
					httpURLConnection.connect ();

					// データのダウンロードを開始
					if( (httpURLConnection.getResponseCode () == 200)
					||	(httpURLConnection.getResponseCode () == 206)
					){
						// HTTP 通信の内容をファイルに保存するためのストリームを生成
						InputStream inputStream = httpURLConnection.getInputStream ();
						FileOutputStream fileOutputStream = (new FileOutputStream (temporaryFile, true));

						byte[] buffReadBytes = new byte[4096];
						for( int
						sizeReadBytes = inputStream.read (buffReadBytes); sizeReadBytes != -1;
						sizeReadBytes = inputStream.read (buffReadBytes)
						){
							// ファイルに書き出し
							fileOutputStream.write (
								buffReadBytes, 0,
								sizeReadBytes
							);

							// 進捗状況を更新する処理
							this.downloadFileSizeCount += sizeReadBytes;
							this.publishProgress (this.downloadFileSizeCount);

							// キャンセルされているのであばフラグを立てて抜ける
							if( this.isCancelled () ) {
								downloadCancel = true;
								break;
							}
						}
						// ダウンロードの完了フラグを立てる
						downloadComplete = true;
					}
				}
			}
			catch( MalformedURLException exception ) { exception.printStackTrace (); }
			catch( ProtocolException exception ) { exception.printStackTrace (); }
			catch( IOException exception ) { exception.printStackTrace (); }
			finally {
				// ダウンロードが無事に完了しているのであればリネームする.
				if( true == downloadComplete ) {
					if( false == downloadCancel ) {
						temporaryFile.renameTo (new File (FILE_LOCAL_PATH));
					}
				}
			}

			return 0;
		}

		/*-------------------------------------------------------------------*/

		@Override
		protected void onProgressUpdate (Integer... values) {
			this.progressDialog.setMax (this.downloadFileSize);
			this.progressDialog.setProgress (this.downloadFileSizeCount);
		}

		/*-------------------------------------------------------------------*/

		@Override
		public void onPostExecute (Integer result)
		{
			// ProgressDialog の削除
			if( null != this.progressDialog ) {
				this.progressDialog.dismiss ();
				this.progressDialog = null;
			}
		}

		/*-------------------------------------------------------------------*/

		ProgressDialog progressDialog = null;
		Integer downloadFileSize = 0;
		Integer downloadFileSizeCount = 0;
	}
}

