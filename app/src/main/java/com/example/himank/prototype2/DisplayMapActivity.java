package com.example.himank.prototype2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class DisplayMapActivity extends AppCompatActivity {

    ArrayList<Integer> x1, y1, x2, y2;
    ArrayList<Integer> path;
    TextView tv;
    int source;
    ArrayList<Pair<Integer,Integer>> floorTrans;
    ArrayList<Integer> floorImg;
    private ZoomableImageView zm;

    int curFloor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_map);
        tv = (TextView) findViewById(R.id.tvOutput);

        path = getIntent().getIntegerArrayListExtra(MainActivity.PATH_EXTRAS);

        if(path==null || path.size()==0 )
        {
            finish();
        }

        source = path.get(0);
        path.set(0,0);

        x1=new ArrayList<>();
        y1=new ArrayList<>();
        x2=new ArrayList<>();
        y2=new ArrayList<>();

        floorTrans = new ArrayList<>();
        floorImg  = new ArrayList<>();

        fillArrays();
        zm = (ZoomableImageView) findViewById(R.id.ivChunky);

        curFloor = 2;

        setBitmap();
    }

    int getFloor(int n)
    {
        if(n==0) n = source;
        else if(n==source) n=0;

        for(int i=0; i<floorTrans.size(); i++)
        {
            if(floorTrans.get(i).first<=n && n<=floorTrans.get(i).second)
                return i;
        }
        return 2;
    }

    private void setBitmap() {
        Bitmap bm = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), floorImg.get(curFloor)));
        bm = editThisBitmap(bm);
        zm.setImageBitmap(bm);
    }

    void fillArrays()
    {
        InputStream inputStream = getResources().openRawResource(R.raw.newsampleone);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        Scanner sc = new Scanner(reader);
        while (sc.hasNextInt()) {
            int x1t = sc.nextInt();
            int y1t = sc.nextInt();
            int x2t = sc.nextInt();
            int y2t = sc.nextInt();
            x1.add(x1t);
            y1.add(y1t);
            x2.add(x2t);
            y2.add(y2t);
        }
        for(int i=0;i<500;i++) {
            x1.add(0); x2.add(0); y1.add(0); y2.add(0);
        }
        int temp = x1.get(0);
        x1.set(0, x1.get(source));
        x1.set(source, temp);

        temp = y1.get(0);
        y1.set(0, y1.get(source));
        y1.set(source, temp);

        temp = x2.get(0);
        x2.set(0, x2.get(source));
        x2.set(source, temp);

        temp = y2.get(0);
        y2.set(0, y2.get(source));
        y2.set(source, temp);


        sc.close();

        floorTrans.add(new Pair<Integer, Integer>(137, 213));
        floorTrans.add(new Pair<Integer, Integer>(74, 136));
        floorTrans.add(new Pair<Integer, Integer>(0, 73));

        floorImg.add(0);
        floorImg.add(0);
        floorImg.add(R.raw.fuckyou);
    }

    private Bitmap editThisBitmap(Bitmap bm) {
        Bitmap workingBitmap = Bitmap.createBitmap(bm);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        Paint paint = new Paint();
        paint.setStrokeWidth(10);
        paint.setColor(Color.RED);
        float cHei = (float) (canvas.getHeight()/1080.0);
        float cWidth = (float) (canvas.getWidth()/1920.0);

        //if(getFloor(path.get(0))==curFloor)
        //    canvas.drawLine(x1.get(path.get(0))*cWidth,y1.get(path.get(0))*cHei,x2.get(path.get(0))*cWidth,y2.get(path.get(0)) * cHei, paint);

        if(getFloor(path.get(path.size()-1))==curFloor)
            canvas.drawLine(x1.get(path.get(path.size()-1))*cWidth,y1.get(path.get(path.size()-1))*cHei,x2.get(path.get(path.size()-1))*cWidth,y2.get(path.get(path.size()-1))*cHei,paint);

        int cum = 0;
        boolean showing=false;
        for(int i=0;i<path.size()-1; i++) {
            if(getFloor(path.get(i+1))!=curFloor || getFloor(path.get(i))!=curFloor) {
                if(showing) {
                    canvas.drawLine(x2.get(path.get(i)) * cWidth, y2.get(path.get(i)) * cHei, x1.get(path.get(i)) * cWidth, y1.get(path.get(i)) * cHei, paint);
                }
                showing=false;
                continue;
            }else{
                if(!showing){
                    canvas.drawLine(x2.get(path.get(i)) * cWidth, y2.get(path.get(i)) * cHei, x1.get(path.get(i)) * cWidth, y1.get(path.get(i)) * cHei, paint);
                }
                showing=true;
            }

            canvas.drawCircle(x2.get(path.get(i)) * cWidth, y2.get(path.get(i)) * cHei, 20, paint);
            canvas.drawLine(x2.get(path.get(i)) * cWidth, y2.get(path.get(i)) * cHei, x2.get(path.get(i + 1)) * cWidth, y2.get(path.get(i + 1)) * cHei, paint);
            tv.setText(tv.getText()+"("+x2.get(path.get(i)) +", " +y2.get(path.get(i))+"): "+path.get(i)+"\n");
            if(Math.abs(x2.get(path.get(i))*cWidth-x2.get(path.get(i+1))*cWidth)+cum >= 80*cWidth) {
                float x = (x2.get(path.get(i))*cWidth + x2.get(path.get(i+1))*cWidth)/2;
                float y = (y2.get(path.get(i))*cWidth + y2.get(path.get(i+1))*cWidth)/2;
                //drawArrow(canvas, x2.get(path.get(i + 1)) > x2.get(path.get(i)), x, y, cHei, cWidth, paint);
                cum=0;
            }else
                cum+=Math.abs(x2.get(path.get(i))*cWidth-x2.get(path.get(i+1))*cWidth);
        }

        //canvas.drawLine(418*cWidth,272*cHei,1128*cWidth,398*cHei, paint);
        return mutableBitmap;
    }

    void drawArrow(Canvas canvas, boolean isRight, float x, float y, float ch, float cw, Paint p)
    {
        Log.d("Map", "arrow drawn: " + x + " , " + y);
        if(isRight)
        {
            canvas.drawLine(x, y, (x-21)*cw, (y+13)*ch, p);
            canvas.drawLine(x, y, (x-16)*cw, (y-22)*ch, p);

        }else
        {
            canvas.drawLine(x, y, (x+6)*cw, (y+20)*ch, p);
            canvas.drawLine(x, y, (x+18)*cw, (y-10)*ch, p);
        }
    }
}
