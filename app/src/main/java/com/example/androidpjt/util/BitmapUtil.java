package com.example.androidpjt.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.InputStream;

public class BitmapUtil {
    //안드로이드 이미지의 원천 타입은 Bitmap이다.(Drawble은 리소스 이미지를 쉽게 사용하기 위해 만든 Bitmap의 서브 클래스)
    //이 함수를 호출하면 이미지를 읽어서 Bitmap으로 리턴
    //매개변수가 이미지를 식별하는 Uri
    //갤러리앱의 목록화면에서 유저가 이미지 선택, 되돌아 올때 유저 선택한 이미지의 식별자 값이 Uri

    //유저가 갤러리에서 사진을 선택한 순간 그사진파일을 읽을 수 있는 stream을 직접제공
    //그 stream에 Uri만 지전하면 읽힘(편함)
    //파일경로를 얻어 파일경로로 읽을 수 있긴 하지만 stream을 이용하는것이 더 편함
    public static Bitmap getGalleryBitmapFromStream(Context context, Uri uri) {
        try {
            //데이터이미지, 데이터사이즈가 크기 때문에 그냥 로딩하면 메모리 부족
            //데이터 사이즈를 줄여서 로딩해야함, 화면에 출력되는 사이즈를 줄이는 개념이아님
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 10; // 1/10으로 줄어서 로딩
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //문자열로 된 파일의 경로를 매개변수로 지정하면 그파일의 이미지를 읽어서 Bitmap으로 리턴
    public static Bitmap getGalleryBitmapFromFile(Context context, String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 10; // 1/10으로 줄어서 로딩
        return BitmapFactory.decodeFile(filePath, options);
    }
}
