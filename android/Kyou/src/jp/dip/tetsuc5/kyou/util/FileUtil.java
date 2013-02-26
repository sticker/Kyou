package jp.dip.tetsuc5.kyou.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;

public class FileUtil extends Activity {

	public static boolean writeFile(byte[] binary, String fileName) {
		BufferedOutputStream bos;
		try {
			File file = new File(fileName);
			bos = new BufferedOutputStream(new FileOutputStream(file));
			bos.write(binary);
			bos.flush();
			bos.close();

			return true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static boolean isExists(String fileName) {

		try {
			File file = new File(fileName);
			if (file.exists() == true) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean mkdir(String dir) {

		try {
			File outDir = new File(dir);
			// パッケージ名のディレクトリが SD カードになければ作成します。
			if (outDir.exists() == false) {
				outDir.mkdirs();
			}

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	// ボツ
	public boolean writeFile2(byte[] binary, String fileName) {
		try {
			FileOutputStream fos = openFileOutput(fileName,
					Context.MODE_PRIVATE);
			fos.write(binary);
			fos.close();

			return true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

}
