package jp.dip.tetsuc5.kyou.bean;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class Meigen {

	private String text;
	private String auther;

	public Meigen() {
	}

	public Meigen(String text, String auther) {
		this.text = text;
		this.auther = auther;
	}

	public static List<Meigen> read(String json) {

		List<Meigen> list = null;

		try {
			Gson mygson = new Gson();
			Type collectionType = new TypeToken<Collection<Meigen>>() {
			}.getType();
			list = mygson.fromJson(json, collectionType);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public static List<Meigen> readMeigenFile(String file_name) {

		List<Meigen> list = null;
		InputStreamReader isr = null;

		try {
			isr = new InputStreamReader(new FileInputStream(file_name));
			JsonReader jsr = new JsonReader(isr);
			Gson mygson = new Gson();
			Type collectionType = new TypeToken<Collection<Meigen>>() {
			}.getType();
			list = mygson.fromJson(jsr, collectionType);

		} catch (FileNotFoundException e) {
			// System.out.println("ファイルが見つかりません。");
			e.printStackTrace();
		} finally {
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
					// System.out.println("入出力エラーです。");
					e.printStackTrace();
				}
			}
		}

		return list;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAuther() {
		return auther;
	}

	public void setAuther(String auther) {
		this.auther = auther;
	}

}
