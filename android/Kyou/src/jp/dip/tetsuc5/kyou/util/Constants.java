package jp.dip.tetsuc5.kyou.util;

import java.io.File;

import android.app.Activity;
import android.os.Environment;

public class Constants {

	public static String APP_NAME = "Kyou";
	public static String SD_FILE_PATH = Environment.getExternalStorageDirectory ().getAbsolutePath () + File.separator + APP_NAME + File.separator;

	public static String DOKUJO_PATH = SD_FILE_PATH + "dokujo" + File.separator;
	public static String MEIGEN_PATH = SD_FILE_PATH + "meigen" + File.separator;
	public static String GIRLMEN_PATH = SD_FILE_PATH + "girlmen" + File.separator;
	
	public static String FILE_NAME_MEIGEN = "meigen.json";
	public static String FILE_MEIGEN = MEIGEN_PATH + FILE_NAME_MEIGEN;
	public static String FILE_NAME_DOKUJO = "dokujo.json";
	public static String FILE_DOKUJO = DOKUJO_PATH + FILE_NAME_DOKUJO;
	public static String FILE_NAME_GIRLMEN = "girlmen.json";
	public static String FILE_GIRLMEN = GIRLMEN_PATH + FILE_NAME_GIRLMEN;

}
