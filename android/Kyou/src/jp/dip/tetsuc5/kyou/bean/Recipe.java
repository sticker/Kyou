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

public class Recipe {

	private String url;
	private String title;

	public Recipe() {
	}

	public Recipe(String url, String title) {
		this.url = url;
		this.title = title;
	}

	public static List<Recipe> read(String file_name) {

		List<Recipe> list = null;
		InputStreamReader isr = null;

		try {
			isr = new InputStreamReader(new FileInputStream(file_name));
			JsonReader jsr = new JsonReader(isr);
			Gson mygson = new Gson();
			Type collectionType = new TypeToken<Collection<Recipe>>() {
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
