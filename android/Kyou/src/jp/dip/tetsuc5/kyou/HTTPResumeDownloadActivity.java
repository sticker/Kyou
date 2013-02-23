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
 * HTTP�T�[�o����t�@�C�������W���[���_�E�����[�h����T���v��.
 */
public class HTTPResumeDownloadActivity extends Activity
{
	/** �_�E�����[�h���s�����߂̃t�@�C����URL. */
	static final String DOWNLOAD_FILE_URL
	= "http://tetsuc5.dip.jp/Kyou/dokujo.json";

	/** ���[�J���ɕۑ�����t�@�C���̃p�X */
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

	/** �_�E�����[�h�������s���^�X�N */
	private class AsyncDownloadTask extends AsyncTask <Integer, Integer, Integer>
	{
		@Override
		public void onPreExecute ()
		{
			// Spinner Progress Dialog ��\��
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
			// �_�E�����[�h�p�̃e���|�����t�@�C���I�u�W�F�N�g
			File temporaryFile = new File (FILE_LOCAL_PATH + ".tmp");
			if( temporaryFile.exists () )
			{
				this . downloadFileSize = 0;
				this . downloadFileSizeCount = (int) temporaryFile.length ();
			}
			else {
				this . downloadFileSize = 0;
				this . downloadFileSizeCount = 0;

				// �_�E�����[�h�p�ɐV�K�t�@�C�����쐬����
				try { temporaryFile.createNewFile (); }
				catch( IOException exception ) {
					exception.printStackTrace ();
				}
			}

			// �����t���O
			boolean downloadComplete = false;
			boolean downloadCancel = false;
			try {
				// �_�E�����[�h����t�@�C���̃T�C�Y���擾����
				{
					URL url = new URL (DOWNLOAD_FILE_URL);
					HttpURLConnection httpURLConnection = (HttpURLConnection) url . openConnection ();

					httpURLConnection . setRequestMethod ("HEAD");
					httpURLConnection . connect ();
					if( httpURLConnection . getResponseCode () == 200 ) {
						this.downloadFileSize = httpURLConnection . getContentLength ();
					}
				}
				// ���ۂ̃_�E�����[�h����
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

					// �f�[�^�̃_�E�����[�h���J�n
					if( (httpURLConnection.getResponseCode () == 200)
					||	(httpURLConnection.getResponseCode () == 206)
					){
						// HTTP �ʐM�̓��e���t�@�C���ɕۑ����邽�߂̃X�g���[���𐶐�
						InputStream inputStream = httpURLConnection.getInputStream ();
						FileOutputStream fileOutputStream = (new FileOutputStream (temporaryFile, true));

						byte[] buffReadBytes = new byte[4096];
						for( int
						sizeReadBytes = inputStream.read (buffReadBytes); sizeReadBytes != -1;
						sizeReadBytes = inputStream.read (buffReadBytes)
						){
							// �t�@�C���ɏ����o��
							fileOutputStream.write (
								buffReadBytes, 0,
								sizeReadBytes
							);

							// �i���󋵂��X�V���鏈��
							this.downloadFileSizeCount += sizeReadBytes;
							this.publishProgress (this.downloadFileSizeCount);

							// �L�����Z������Ă���̂ł��΃t���O�𗧂ĂĔ�����
							if( this.isCancelled () ) {
								downloadCancel = true;
								break;
							}
						}
						// �_�E�����[�h�̊����t���O�𗧂Ă�
						downloadComplete = true;
					}
				}
			}
			catch( MalformedURLException exception ) { exception.printStackTrace (); }
			catch( ProtocolException exception ) { exception.printStackTrace (); }
			catch( IOException exception ) { exception.printStackTrace (); }
			finally {
				// �_�E�����[�h�������Ɋ������Ă���̂ł���΃��l�[������.
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
			// ProgressDialog �̍폜
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

