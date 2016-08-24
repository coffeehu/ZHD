package com.hc.zhdaily.util;

import android.util.Log;

/**
 * Created by hc on 2016/8/19.
 *
 *
 * 评论的 time 的值为这种模式的（1471578002 之类），需要解析（未完成）
 * 1471578002  0819 11：40

 1471577825  0819 11：37

 1471577686  11：34

 1471577381   11：29


 1471488414  20160818 10：46


 1494 min

 0819  00：56    1471539371

 */
public class GetTime {
    public static String getTime(int time){
        int baseTime = 1471536011; // 0819 00：00
        int baseMon = 8, baseDay = 19, baseHour = 0, baseMin = 0;
        int result = time - baseTime;
        String stringTime = null;

       // Log.d("GetTimeTest","time ="+time+",baseTime ="+baseTime+",result ="+result);
       // Log.d("GetTimeTest","double result / 60 ="+(double)result/60+",int result / 60 ="+(int)result/60);
        if( result > 0){
            int min = result / 60;
            //float min = (float) (Math.round(tmpmin/10))/10; //方法1 ，保留小数点后 1 位。round 四舍五入
           // Log.d("GetTimeTest","double tmpmin ="+tmpmin+",int tmpmin ="+tmpmin2+",min ="+min);
            double tmphour = (double) (min / 60);
            float hour = (float) (Math.round(tmphour/10))/10;
            Log.d("GetTimeTest","min ="+min+"hour ="+hour);



            double day = tmphour / 24;


            if (  min > 0 && min <60 ){
                baseMin += min;
                return  stringTime = baseMon+"月"+baseDay+"日"+" "+"0"+baseHour+":0"+baseMin;
            }else if( hour>0 && hour < 24) {
                String stringhour = hour+"";
                String a = stringhour.substring(0,1);
                String b = stringhour.substring(1,1);
                //int a1 = Integer.parseInt(a);
                //int b1 = Integer.parseInt(b);
               // baseHour += a1;
               // baseMin += b1;
                Log.d("GetTimeTest","hour ="+stringhour+",a ="+a+",b ="+b);
                return  stringTime = baseMon+"月"+baseDay+"日"+" "+"0"+baseHour+":0"+baseMin;
            }else {
                return time+"";
            }
        }else {
            return time+"";
        }
    }

}
