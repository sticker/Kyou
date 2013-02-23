package jp.dip.tetsuc5.kyou;

import java.io.File;
import java.util.List;

import jp.dip.tetsuc5.kyou.bean.Dokujo;
import jp.dip.tetsuc5.kyou.util.Constants;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DokujoActivity extends Activity {

    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        this.setContentView (R.layout.activity_main);
		
        Log.d("KyouDebug", "btn_draw!");

		List<Dokujo> dokujoList = Dokujo.readDokujo(Constants.FILE_DOKUJO);
		
		LinearLayout layout;
		layout = (LinearLayout)findViewById(R.id.Parent);
        if(layout.getChildCount()>0){//子Viewが存在する場合
        	layout.removeAllViews();//動的に削除する。
        }			

//	    LinearLayout layout = new LinearLayout (this);
//	    layout.setOrientation(LinearLayout.VERTICAL);
//	    setContentView(layout);
//		txt_dokujo = (TextView) findViewById(R.id.txt_dokujo);
		
		for(Dokujo dokujo : dokujoList) {

			Log.d("KyouDebug", dokujo.getTitle());
			
			//LinerLayout
//			LinearLayout ll = (LinearLayout)getLayoutInflater().inflate(R.layout.dokujo, null);
			
			//TextView追加
			TextView tv = (TextView)getLayoutInflater().inflate(R.layout.dokujo, null);
            //TextView tv = (TextView)findViewById(R.id.TextView);
			tv.setText(dokujo.getTitle());
            //ll.addView(tv);
            //layout.addView(tv);
            
            //画像ファイル名取得
            String[] parts = dokujo.getImage().split("/");
			final String fileName = parts[parts.length -1];
            
			//ImageView
			//final ImageView iv = (ImageView)findViewById(R.id.ImageView);
			//final File f = new File(Constants.DOKUJO_PATH, fileName);
//			iv.setImageURI(Uri.fromFile(f));
//          ll.addView(iv);
			Drawable dokujo_img = Drawable.createFromPath(Constants.DOKUJO_PATH+fileName);
			dokujo_img.setBounds(0, 0, dokujo_img.getIntrinsicWidth(), dokujo_img.getIntrinsicHeight());
			tv.setCompoundDrawables(dokujo_img, null, null, null);
            //layout.addView(iv);
   
//            layout.addView(ll);
            layout.addView(tv);
            
		}
    }
}
