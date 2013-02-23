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
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			
			return false;
    }
    
    public boolean writeFile2(byte[] binary, String fileName) {
    	try {
    		FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
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
