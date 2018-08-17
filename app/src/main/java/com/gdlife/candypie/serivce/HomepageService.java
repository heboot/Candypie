package com.gdlife.candypie.serivce;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heboot on 2018/3/1.
 */

public class HomepageService {


    public void initCommentTags() {

    }


    /**
     * 获取可选的身高数据
     *
     * @return
     */
    public static List<String> getChooseHeights() {
        int minheight = ConfigService.getInstance().getMinHeight();
        int maxheight = ConfigService.getInstance().getMaxHeight();
        List<String> heights = new ArrayList<>();
        while (minheight <= maxheight) {
            heights.add(minheight + "");
            minheight = minheight + 1;
        }
        return heights;
    }

    /**
     * 获取可选的体重数据
     *
     * @return
     */
    public static List<String> getChooseKGs() {
        int minheight = ConfigService.getInstance().getMinWeight();
        int maxheight = ConfigService.getInstance().getMaxWeight();
        List<String> heights = new ArrayList<>();
        while (minheight <= maxheight) {
            heights.add(minheight + "");
            minheight = minheight + 1;
        }
        return heights;
    }

    public static ColorDrawable getHomepageCommentRatingColor(String color) {
        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setColor(Color.parseColor(color));
        return colorDrawable;
    }

}
