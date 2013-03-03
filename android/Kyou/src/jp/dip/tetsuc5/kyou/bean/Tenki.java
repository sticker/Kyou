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

public class Tenki {

	private String date;
	private String dateLabel;
	private String telop;

	public Tenki() {
	}

	public Tenki(String date, String dateLabel, String telop) {
		this.date = date;
		this.dateLabel = dateLabel;
		this.telop = telop;
	}

	public static List<Tenki> read(String file_name) {

		List<Tenki> list = null;
		InputStreamReader isr = null;

		try {
			isr = new InputStreamReader(new FileInputStream(file_name));
			JsonReader jsr = new JsonReader(isr);
			Gson mygson = new Gson();
			Type collectionType = new TypeToken<Collection<Tenki>>() {
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTelop() {
		return telop;
	}

	public void setTelop(String telop) {
		this.telop = telop;
	}

	public String getDateLabel() {
		return dateLabel;
	}

	public void setDateLabel(String dateLabel) {
		this.dateLabel = dateLabel;
	}

}