package com.gdlife.candypie.serivce;

import android.content.Context;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.databinding.IncludeNodataInfoBinding;
import com.gdlife.candypie.databinding.IncludeNodataInfoOrderBinding;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.bean.user.TagsChildBean;
import com.heboot.utils.ViewUtils;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.QMUIFloatLayout;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButtonDrawable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/3/14.
 */

public class UIService {

    private CompoundButton.OnCheckedChangeListener commentTagCheckChangeListener;

    private ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


    /**
     * @param tipText
     * @return
     */
    public View getEmptyView(String tipText) {
        IncludeNodataInfoBinding includeNodataInfoBinding = DataBindingUtil.inflate(LayoutInflater.from(MAPP.mapp), R.layout.include_nodata_info, null, false);
        includeNodataInfoBinding.tvNodateIntro.setText(tipText);
        return includeNodataInfoBinding.getRoot();
    }

    /**
     * @param tipText
     * @return
     */
    public View getEmptyViewByOrder(String tipText) {
        IncludeNodataInfoOrderBinding includeNodataInfoBinding = DataBindingUtil.inflate(LayoutInflater.from(MAPP.mapp), R.layout.include_nodata_info_order, null, false);
        includeNodataInfoBinding.tvNodateIntro.setText(tipText);
        return includeNodataInfoBinding.getRoot();
    }


    /**
     * 获取已经选中的标签ID集合
     *
     * @return
     */
    public List<String> getSelectedTagsBeanIDList(QMUIFloatLayout floatLayout) {

        List<String> list = new ArrayList<>();

        if (floatLayout.getChildCount() > 0) {

            for (int i = 0; i < floatLayout.getChildCount(); i++) {
                if (((CheckBox) floatLayout.getChildAt(i)).isChecked()) {
                    if (floatLayout.getChildAt(i).getTag() != null) {
                        list.add(((TagsChildBean) floatLayout.getChildAt(i).getTag()).getId());
                    }
                }

            }
        }

        return list;

    }

    /**
     * 获取已经选中的标签ID集合
     *
     * @return
     */
    public List<String> getSelectedTagsBeanIDListByMeet(QMUIFloatLayout floatLayout) {

        List<String> list = new ArrayList<>();

        if (floatLayout.getChildCount() > 0) {

            for (int i = 0; i < floatLayout.getChildCount(); i++) {
                if (floatLayout.getChildAt(i).getTag() != null) {
                    list.add(((TagsChildBean) floatLayout.getChildAt(i).getTag()).getId());
                }

            }
        }

        return list;

    }


    /**
     * 获取已经选中的标签集合
     *
     * @return
     */
    public List<TagsChildBean> getSelectedTagsBean(QMUIFloatLayout floatLayout) {

        List<TagsChildBean> list = new ArrayList<>();

        if (floatLayout.getChildCount() > 0) {

            for (int i = 0; i < floatLayout.getChildCount(); i++) {
                if (((CheckBox) floatLayout.getChildAt(i)).isChecked()) {
                    if (floatLayout.getChildAt(i).getTag() != null) {
                        list.add((TagsChildBean) floatLayout.getChildAt(i).getTag());
                    }
                }

            }
        }

        return list;

    }


    /**
     * 初始化标签布局
     *
     * @param tagsChildBeanList
     * @param floatLayout
     * @param verticalSpacing
     * @param horizontalSpacing
     * @param enable
     * @param childHeight
     */
    public void initTagsLayout(List<TagsChildBean> tagsChildBeanList, QMUIFloatLayout floatLayout, int verticalSpacing, int horizontalSpacing, boolean enable, int childHeight, List<String> selectedTags) {
        if (tagsChildBeanList == null || tagsChildBeanList.size() <= 0) {
            return;
        }

        floatLayout.removeAllViews();

        floatLayout.setChildVerticalSpacing(verticalSpacing);
        floatLayout.setChildHorizontalSpacing(horizontalSpacing);

        for (TagsChildBean tagsChildBean : tagsChildBeanList) {
            if (selectedTags != null) {
                floatLayout.addView(getCommentTag(floatLayout.getContext(), enable, tagsChildBean, childHeight, selectedTags.indexOf(tagsChildBean.getId()) > -1));
            } else {
                floatLayout.addView(getCommentTag(floatLayout.getContext(), enable, tagsChildBean, childHeight, false));
            }

        }

    }


    public void initTagsLayout(List<TagsChildBean> tagsChildBeanList, QMUIFloatLayout floatLayout, int verticalSpacing, int horizontalSpacing, boolean enable, int childHeight, List<String> selectedTags, Consumer checkedConsumer) {
        if (tagsChildBeanList == null || tagsChildBeanList.size() <= 0) {
            return;
        }

        floatLayout.removeAllViews();

        floatLayout.setChildVerticalSpacing(verticalSpacing);
        floatLayout.setChildHorizontalSpacing(horizontalSpacing);

        for (TagsChildBean tagsChildBean : tagsChildBeanList) {
            if (selectedTags != null) {
                floatLayout.addView(getCommentTag(floatLayout.getContext(), enable, tagsChildBean, childHeight, selectedTags.indexOf(tagsChildBean.getId()) > -1, checkedConsumer));
            } else {
                floatLayout.addView(getCommentTag(floatLayout.getContext(), enable, tagsChildBean, childHeight, false, checkedConsumer));
            }

        }

    }


    /**
     * 初始化选择完约会态度后的布局
     *
     * @param tagsChildBeanList
     * @param floatLayout
     * @param verticalSpacing
     * @param horizontalSpacing
     */
    public void initMeetSelectedTagsLayout(List<TagsChildBean> tagsChildBeanList, QMUIFloatLayout floatLayout, int verticalSpacing, int horizontalSpacing) {
        if (tagsChildBeanList == null || tagsChildBeanList.size() <= 0) {
            return;
        }

        floatLayout.removeAllViews();

        floatLayout.setChildHorizontalSpacing(verticalSpacing);
        floatLayout.setChildVerticalSpacing(horizontalSpacing);

        for (TagsChildBean tagsChildBean : tagsChildBeanList) {

            floatLayout.addView(getMeetPriviewTag(floatLayout.getContext(), tagsChildBean));

        }

    }

    /**
     * 生成评论的标签View
     * 选择视频特色也通用
     *
     * @param context
     * @param enable  个人主页这种页面 enable 为false 不可点
     *                预览的时候高度为30  选择的时候高度为36 设置不同的背景和高度
     * @return
     */
    private CheckBox getCommentTag(Context context, boolean enable, TagsChildBean tagsChildBean, int height, boolean checked) {
        CheckBox checkBox = new CheckBox(context);
        checkBox.setButtonDrawable(null);

        //如果可用 是选择状态 给大圆角背景
        if (enable) {
            checkBox.setBackgroundResource(R.drawable.selector_comment_tags_bg);
        } else {
            //只是纯看模式 给小圆角背景
            checkBox.setBackgroundResource(R.drawable.selector_comment_tags_pre_bg);
        }

        checkBox.setLayoutParams(layoutParams);
        checkBox.setEnabled(enable);
        checkBox.setGravity(Gravity.CENTER);
        checkBox.setTag(tagsChildBean);
        checkBox.setTextColor(ContextCompat.getColor(context, R.color.color_898A9E));
        checkBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.x16));
        if (!StringUtils.isEmpty(tagsChildBean.getNums())) {
            checkBox.setText(tagsChildBean.getContent() + " (" + tagsChildBean.getNums() + ")");
        } else {
            checkBox.setText(tagsChildBean.getContent());
        }

        ViewUtils.setViewHeight(checkBox, height);
        QMUIViewHelper.setPaddingLeft(checkBox, context.getResources().getDimensionPixelSize(R.dimen.x10));
        QMUIViewHelper.setPaddingRight(checkBox, context.getResources().getDimensionPixelSize(R.dimen.x10));

        /**
         * 设置字体颜色监听
         */
        if (commentTagCheckChangeListener == null) {
            commentTagCheckChangeListener = new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        buttonView.setTextColor(ContextCompat.getColor(context, R.color.theme_color));
                    } else {
                        buttonView.setTextColor(ContextCompat.getColor(context, R.color.color_898A9E));
                    }
                }
            };
        }
        checkBox.setOnCheckedChangeListener(commentTagCheckChangeListener);
        if (enable) {
            checkBox.setChecked(checked);
        } else {
            //不可用的状态下就是 浏览状态 直接给选中状态的颜色
            checkBox.setChecked(true);
        }

        return checkBox;
    }

    /**
     * 生成评论的标签View
     * 选择视频特色也通用
     *
     * @param context
     * @param enable  个人主页这种页面 enable 为false 不可点
     *                预览的时候高度为30  选择的时候高度为36 设置不同的背景和高度
     * @return
     */
    private CheckBox getCommentTag(Context context, boolean enable, TagsChildBean tagsChildBean, int height, boolean checked, Consumer checkedConsumer) {
        CheckBox checkBox = new CheckBox(context);
        checkBox.setButtonDrawable(null);

        //如果可用 是选择状态 给大圆角背景
        if (enable) {
            checkBox.setBackgroundResource(R.drawable.selector_comment_tags_bg);
        } else {
            //只是纯看模式 给小圆角背景
            checkBox.setBackgroundResource(R.drawable.selector_comment_tags_pre_bg);
        }

        checkBox.setLayoutParams(layoutParams);
        checkBox.setEnabled(enable);
        checkBox.setGravity(Gravity.CENTER);
        checkBox.setTag(tagsChildBean);
        checkBox.setTextColor(ContextCompat.getColor(context, R.color.color_898A9E));
        checkBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.x16));
        checkBox.setText(tagsChildBean.getContent());
        ViewUtils.setViewHeight(checkBox, height);
        QMUIViewHelper.setPaddingLeft(checkBox, context.getResources().getDimensionPixelSize(R.dimen.x10));
        QMUIViewHelper.setPaddingRight(checkBox, context.getResources().getDimensionPixelSize(R.dimen.x10));

        /**
         * 设置字体颜色监听
         */
        if (commentTagCheckChangeListener == null) {
            commentTagCheckChangeListener = new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        buttonView.setTextColor(ContextCompat.getColor(context, R.color.theme_color));
                    } else {
                        buttonView.setTextColor(ContextCompat.getColor(context, R.color.color_898A9E));
                    }
                    if (checkedConsumer != null) {
                        try {
                            checkedConsumer.accept("");
                        } catch (Exception e) {
                        }
                    }
                }
            };
        }
        checkBox.setOnCheckedChangeListener(commentTagCheckChangeListener);
        if (enable) {
            checkBox.setChecked(checked);
        } else {
            //不可用的状态下就是 浏览状态 直接给选中状态的颜色
            checkBox.setChecked(true);
        }

        return checkBox;
    }


    /**
     * 获取约会态度 选择后的 展示的标签
     *
     * @param context
     * @param tagsChildBean
     * @return
     */
    private TextView getMeetPriviewTag(Context context, TagsChildBean tagsChildBean) {
        TextView qmuiRoundButton = new TextView(context);
//        qmuiRoundButton.setTag(tagsChildBean);

        QMUIRoundButtonDrawable qmuiRoundButtonDrawable = new QMUIRoundButtonDrawable();
        if (StringUtils.isEmpty(tagsChildBean.getBg_color())) {
            qmuiRoundButtonDrawable.setBgData(ColorStateList.valueOf(context.getResources().getColor(R.color.theme_color)));
        } else {
            qmuiRoundButtonDrawable.setBgData(ColorStateList.valueOf(Color.parseColor(tagsChildBean.getBg_color())));
        }
        if (!StringUtils.isEmpty(tagsChildBean.getFont_color())) {
            qmuiRoundButton.setTextColor(Color.parseColor(tagsChildBean.getFont_color()));
        } else {
            qmuiRoundButton.setTextColor(Color.WHITE);
        }
        qmuiRoundButton.setBackground(qmuiRoundButtonDrawable);
        qmuiRoundButton.setGravity(Gravity.CENTER);
        qmuiRoundButton.setLayoutParams(layoutParams);

        qmuiRoundButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.x14));
        ViewUtils.setViewHeight(qmuiRoundButton, context.getResources().getDimensionPixelOffset(R.dimen.y24));
        QMUIViewHelper.setPaddingLeft(qmuiRoundButton, context.getResources().getDimensionPixelSize(R.dimen.x10));
        QMUIViewHelper.setPaddingRight(qmuiRoundButton, context.getResources().getDimensionPixelSize(R.dimen.x10));
        qmuiRoundButton.setText(tagsChildBean.getContent());
        return qmuiRoundButton;
    }

    public void setToolbarRightTextdStyle(TextView textView, boolean bold, int color) {

        if (bold) {
            //android中为textview动态设置字体为粗体
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            //设置不为加粗
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }

        textView.setTextColor(color);

    }


    public GradientDrawable getOrderOptionBG(String selectorColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(QMUIDisplayHelper.dp2px(MAPP.mapp, 40));
        gradientDrawable.setColor(Color.parseColor(selectorColor));
        return gradientDrawable;
    }


}
