package jp.dip.tetsuc5.kyou.util;

import java.io.File;

import android.os.Environment;

public class Constants {

	public static int REQ_CODE_GIRLMEN = 1;
	public static int REQ_CODE_DOKUJO = 2;
	public static int REQ_CODE_MATOME = 3;

	public static int OK = 0;
	public static int NG = 1;

	public static String APP_NAME = "Kyou";
	public static String SD_FILE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ APP_NAME + File.separator;
	// public static String SD_FILE_PATH = "/sdcard" + File.separator + APP_NAME
	// + File.separator;

	public static String DOKUJO_PATH = SD_FILE_PATH + "dokujo" + File.separator;
	public static String MEIGEN_PATH = SD_FILE_PATH + "meigen" + File.separator;
	public static String GIRLMEN_PATH = SD_FILE_PATH + "girlmen"
			+ File.separator;
	public static String MATOME_PATH = SD_FILE_PATH + "matome" + File.separator;

	public static String FILE_NAME_MEIGEN = "meigen.json";
	public static String FILE_MEIGEN = MEIGEN_PATH + FILE_NAME_MEIGEN;
	public static String FILE_NAME_DOKUJO = "dokujo.json";
	public static String FILE_DOKUJO = DOKUJO_PATH + FILE_NAME_DOKUJO;
	public static String FILE_NAME_GIRLMEN = "girlmen.json";
	public static String FILE_GIRLMEN = GIRLMEN_PATH + FILE_NAME_GIRLMEN;
	public static String FILE_NAME_MATOME = "matome.json";
	public static String FILE_MATOME = MATOME_PATH + FILE_NAME_MATOME;

	public static String URL_MEIGEN = "http://tetsuc5.dip.jp/cgi-bin/Kyou/meigen-json.cgi";
	public static String URL_GIRLMEN = "http://tetsuc5.dip.jp/Kyou/girlmen.json";
	public static String URL_DOKUJO = "http://tetsuc5.dip.jp/Kyou/dokujo.json";
	public static String URL_MATOME = "http://tetsuc5.dip.jp/Kyou/matome.json";
	
	
	
	public static int TEXTSIZE_MEIGEN = 50;
	public static int IMAGESIZE_TITLE = 70;
	public static int IMAGESIZE_DOKUJO = 100;

}
