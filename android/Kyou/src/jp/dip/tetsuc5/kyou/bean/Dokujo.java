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

public class Dokujo implements JsonBean {

	private String url;
	private String title;
	private String image;
	
	public Dokujo(){
	}
	
	public Dokujo(String url, String title, String image){
		this.url = url;
		this.title = title;
		this.image = image;
	}
	
	public static List<Dokujo> readDokujo(String file_name) {

		List<Dokujo> dokujoList = null;
		InputStreamReader isr = null;

		try {
			isr = new InputStreamReader(new FileInputStream(file_name));
			JsonReader jsr = new JsonReader(isr);
			Gson mygson = new Gson();
			Type collectionType = new TypeToken<Collection<Dokujo>>() {
			}.getType();
			dokujoList = mygson.fromJson(jsr, collectionType);

		} catch (FileNotFoundException e) {
			// System.out.println("�t�@�C����������܂���B");
			e.printStackTrace();
		} finally {
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
					// System.out.println("���o�̓G���[�ł��B");
					e.printStackTrace();
				}
			}
		}

		return dokujoList;
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
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	
}
