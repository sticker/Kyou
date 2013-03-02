package jp.dip.tetsuc5.kyou.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

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

	public static ArrayList<String> searchFiles(String dir_path, String expr,
			boolean search_subdir) {
		final File dir = new File(dir_path);

		ArrayList<String> find_files = new ArrayList<String>();
		final File[] files = dir.listFiles();
		if (null != files) {
			for (int i = 0; i < files.length; ++i) {
				if (!files[i].isFile()) {
					if (search_subdir) {
						ArrayList<String> sub_files = searchFiles(
								files[i].getPath(), expr, search_subdir);
						find_files.addAll(sub_files);
					}
					continue;
				}

				final String filename = files[i].getName();
				if ((null == expr) || filename.matches(expr)) {
					// find_files.add(dir.getPath() + filename);
					find_files.add(filename);
				}
			}
		}
		return find_files;
	}

	public static boolean rmFile(String filePath) {
		try {
			File file = new File(filePath);
			file.delete();
			return true;
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
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
