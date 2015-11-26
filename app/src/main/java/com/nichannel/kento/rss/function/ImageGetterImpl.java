package com.nichannel.kento.rss.function;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Html;

/**
 * Created by Kento on 15/11/26.
 */
public class ImageGetterImpl implements Html.ImageGetter {

    Context mContext = null;

    public ImageGetterImpl(Context context){
        mContext = context;
    }

    @Override
    public Drawable getDrawable(String source) {
        // sourceにimgタグの src=""で指定した内容が渡される

        Resources res = mContext.getResources();
        // 画像のリソースIDを取得
        int id = res.getIdentifier(source, "drawable", mContext.getPackageName());

        // リソースIDから Drawable のインスタンスを取得
        Drawable d = res.getDrawable(id);

        // 取得した元画像のサイズを取得し、必要なら、表示画像のサイズを調整する
        int w = d.getIntrinsicWidth();
        int h = d.getIntrinsicHeight();
        if(source.equals("ic_launcher")){
            w = (int)(w*0.58f);
            h = (int)(h*0.58f);
        }
        d.setBounds(0, 0, w, h);

        return d;
    }
}
