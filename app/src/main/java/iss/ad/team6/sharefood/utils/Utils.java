package iss.ad.team6.sharefood.utils;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

import java.util.Map;

public class Utils {
    public static String parseGetUrl(String url, Map<String, String> params){
        StringBuilder builder = new StringBuilder(url);
        if (params!=null){
            int num = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (num == 0){
                    builder.append("?"+entry.getKey()+"="+entry.getValue());
                    num++;
                }else {
                    builder.append("&"+entry.getKey()+"="+entry.getValue());
                }
            }
        }

        return builder.toString();
    }
    //newly add for parse api/{userId} format
    public static String parseGetUrls(String url, Map<String, String> params){
        StringBuilder builder = new StringBuilder(url);
        if (params!=null){
            int num = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (num == 0){
                    builder.append("/"+entry.getValue());
                    num++;
                }else {
                    builder.append("");
                }
            }
        }

        return builder.toString();
    }
    public static ColorStateList getColorStateList(int normal, int pressed, int selected, int unable) {
        int[] colors = new int[]{pressed, pressed, selected, unable, normal};
        int[][] states = new int[5][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_selected};
        states[3] = new int[]{-android.R.attr.state_enabled};
        states[4] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }
    /**
     * 获取图片selector
     *
     * @return stateListDrawable
     */
    public static StateListDrawable getDrawableSelector(Drawable selected, Drawable unSelect) {
        return getStateListDrawable(selected, selected, selected, unSelect);
    }

    /**
     * 获取图片selector
     *
     * @return stateListDrawable
     */
    public static StateListDrawable getStateListDrawable(Drawable perssed, Drawable focused, Drawable selected, Drawable unabled) {
        StateListDrawable bg = new StateListDrawable();
        bg.addState(new int[]{android.R.attr.state_pressed}, perssed);
        bg.addState(new int[]{android.R.attr.state_focused}, focused);
        bg.addState(new int[]{android.R.attr.state_selected}, selected);
        bg.addState(new int[]{}, unabled);
        return bg;
    }

}
