package jp.dip.tetsuc5.kyou.util;

import java.io.File;

import android.os.Environment;

public class Constants {

	public static String ID_ADMOB = "a15131d33ae8203";
	
	public static int REQ_CODE_GIRLMEN = 1;
	public static int REQ_CODE_DOKUJO = 2;
	public static int REQ_CODE_MATOME = 3;
	public static int REQ_CODE_RECIPE = 4;
	public static int REQ_CODE_MERRY = 5;
	public static int REQ_CODE_TENKI = 6;

	public static int OK = 0;
	public static int NG = 1;

	public static String APP_NAME = "Kyou";
//	public static String PREF_NAME = "setting";
	
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
	public static String RECIPE_PATH = SD_FILE_PATH + "recipe" + File.separator;
	public static String MERRY_PATH = SD_FILE_PATH + "merry" + File.separator;
	public static String TENKI_PATH = SD_FILE_PATH + "tenki" + File.separator;

	public static String FILE_NAME_MEIGEN = "meigen.json";
	public static String FILE_MEIGEN = MEIGEN_PATH + FILE_NAME_MEIGEN;
	public static String FILE_NAME_DOKUJO = "dokujo.json";
	public static String FILE_DOKUJO = DOKUJO_PATH + FILE_NAME_DOKUJO;
	public static String FILE_NAME_GIRLMEN = "girlmen.json";
	public static String FILE_GIRLMEN = GIRLMEN_PATH + FILE_NAME_GIRLMEN;
	public static String FILE_NAME_MATOME = "matome.json";
	public static String FILE_MATOME = MATOME_PATH + FILE_NAME_MATOME;
	public static String FILE_NAME_RECIPE = "recipe.json";
	public static String FILE_RECIPE = RECIPE_PATH + FILE_NAME_RECIPE;
	public static String FILE_NAME_MERRY = "merry.json";
	public static String FILE_MERRY = MERRY_PATH + FILE_NAME_MERRY;
	public static String FILE_NAME_TENKIAREA = "tenki-area.json";
	public static String FILE_TENKIAREA = TENKI_PATH + FILE_NAME_TENKIAREA;
	public static String FILE_NAME_TENKI = "tenki.json";
	public static String FILE_TENKI = TENKI_PATH + FILE_NAME_TENKI;

	public static String URL_MEIGEN = "http://tetsuc5.dip.jp/cgi-bin/Kyou/meigen-json.cgi";
	public static String URL_GIRLMEN = "http://tetsuc5.dip.jp/Kyou/girlmen.json";
	public static String URL_DOKUJO = "http://tetsuc5.dip.jp/Kyou/dokujo.json";
	public static String URL_MATOME = "http://tetsuc5.dip.jp/Kyou/matome.json";
	public static String URL_RECIPE = "http://tetsuc5.dip.jp/Kyou/recipe.json";
	public static String URL_MERRY = "http://tetsuc5.dip.jp/Kyou/merry.json";
	public static String URL_TENKIAREA = "http://tetsuc5.dip.jp/Kyou/tenki-area.json";
//	public static String URL_TENKI = "http://weather.livedoor.com/forecast/webservice/json/v1?";
	public static String URL_TENKI = "http://tetsuc5.dip.jp/cgi-bin/Kyou/tenki.cgi?";
	
	public static String TENKI_DEFAULT_AREA = "130010"; //東京

	public static int TEXTSIZE_TOPDATE = 60;
	public static int TEXTSIZE_MEIGEN = 50;
	public static int IMAGESIZE_TITLE = 70;
	public static int IMAGESIZE_DOKUJO = 100;
	
	public static String FONT_TOPDATE = "GAU_Root_Nomal.ttf";
	public static String FONT_MEIGEN = "uzura.ttf";

}
