package com.gdlife.candypie.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.gdlife.candypie.R;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.TagsDialogType;
import com.gdlife.candypie.databinding.DialogServiceBinding;
import com.gdlife.candypie.databinding.LayoutChooseTagsBinding;
import com.gdlife.candypie.databinding.LayoutNewThemeDialogTopBinding;
import com.gdlife.candypie.databinding.LayoutOrderCommentSucBinding;
import com.gdlife.candypie.serivce.ThemeService;
import com.gdlife.candypie.serivce.UIService;
import com.gdlife.candypie.serivce.UserInfoService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.serivce.VipService;
import com.gdlife.candypie.utils.CheckUtils;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.bean.user.TagsChildBean;
import com.heboot.utils.LogUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/2/2.
 */

public class ChooseTagsDialog extends Dialog {

    private LayoutOrderCommentSucBinding layoutOrderCommentSucBinding;

    private LayoutNewThemeDialogTopBinding layoutNewThemeDialogTopBinding;

    private LayoutChooseTagsBinding layoutChooseTagsBinding;

    private Consumer<List<TagsChildBean>> observer;

    public Consumer<List<TagsChildBean>> getObserver() {
        return observer;
    }

    public void setObserver(Consumer<List<TagsChildBean>> observer) {
        this.observer = observer;
    }

    public ChooseTagsDialog(Context context) {
        super(context);
    }

    public ChooseTagsDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {

        private Context context;

        private Consumer<List<TagsChildBean>> observer;

//        private ChooseTagsDialog dialog;

        private String title, subTitle;

        private TagsDialogType type;

        private UIService uiService;

        private List<TagsChildBean> tagsChildBeanList;

        private List<String> selectedTags;


        /**
         * 创建弹窗
         *
         * @param context
         * @param observer
         * @param title
         */
        public Builder(Context context, String subTitle, UIService uiService, List<TagsChildBean> tagsChildBeanList, Consumer<List<TagsChildBean>> observer, String title, TagsDialogType tagsDialogType) {
            this.context = context;
            this.observer = observer;
            this.title = title;
            this.type = tagsDialogType;
            this.uiService = uiService;
            this.tagsChildBeanList = tagsChildBeanList;
            this.subTitle = subTitle;
        }

        public Builder(Context context, String subTitle, UIService uiService, List<TagsChildBean> tagsChildBeanList, Consumer<List<TagsChildBean>> observer, String title, TagsDialogType tagsDialogType, List<String> selectedTags) {
            this.context = context;
            this.observer = observer;
            this.title = title;
            this.type = tagsDialogType;
            this.uiService = uiService;
            this.tagsChildBeanList = tagsChildBeanList;
            this.subTitle = subTitle;
            this.selectedTags = selectedTags;
        }

        public ChooseTagsDialog create() {


            ChooseTagsDialog dialog = new ChooseTagsDialog(context, R.style.QMUI_TipDialog);

            LayoutChooseTagsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_choose_tags, null, false);

            binding.includeTop.tvTitme.setText(title);

            dialog.setObserver(observer);


            if (type == TagsDialogType.CHOOSE_MEET) {
                uiService.initTagsLayout(tagsChildBeanList, binding.qflContainer, context.getResources().getDimensionPixelOffset(R.dimen.y18), context.getResources().getDimensionPixelOffset(R.dimen.x14), true, context.getResources().getDimensionPixelOffset(R.dimen.y36), selectedTags);
            } else if (type == TagsDialogType.CHOOSE_VIDEO_TAG) {
                uiService.initTagsLayout(tagsChildBeanList, binding.qflContainer, context.getResources().getDimensionPixelOffset(R.dimen.y18), context.getResources().getDimensionPixelOffset(R.dimen.x14), true, context.getResources().getDimensionPixelOffset(R.dimen.y36), selectedTags);
            }
            /**
             * 如果是订单评价，需要显示下面的按钮 以及按钮的变灰 变亮 事件
             */
            else if (type == TagsDialogType.CHOOSE_COMMENT_TAG) {
                binding.includeTop.ivOk.setVisibility(View.GONE);
                binding.includeTop.vOk.setVisibility(View.GONE);
                binding.btnBottom.setVisibility(View.VISIBLE);
                dialog.setLayoutOrderCommentSucBinding(binding.includeCommentSuc);
                dialog.setLayoutNewThemeDialogTopBinding(binding.includeTop);
                dialog.setLayoutChooseTagsBinding(binding);

                dialog.getLayoutChooseTagsBinding().btnBottom.setBackgroundResource(R.drawable.bg_rect_bottom_button);
                uiService.initTagsLayout(tagsChildBeanList, binding.qflContainer, context.getResources().getDimensionPixelOffset(R.dimen.y18), context.getResources().getDimensionPixelOffset(R.dimen.x14), true, context.getResources().getDimensionPixelOffset(R.dimen.y36), selectedTags, null);
                binding.btnBottom.setOnClickListener((v) -> {
                    if (uiService.getSelectedTagsBean(binding.qflContainer) == null || uiService.getSelectedTagsBean(binding.qflContainer).size() <= 0) {
                        DialogUtils.getInfolDialog(context, "至少选择一个标签", true).show();
                    } else {
                        Observable.create(new ObservableOnSubscribe<List<TagsChildBean>>() {

                            @Override
                            public void subscribe(ObservableEmitter<List<TagsChildBean>> emitter) throws Exception {
                                dialog.getObserver().accept(uiService.getSelectedTagsBean(binding.qflContainer));
                            }
                        }).subscribe(dialog.getObserver());
                    }
                });
            }

            if (!StringUtils.isEmpty(subTitle)) {
                binding.tvSubTitle.setText(subTitle);
                binding.tvSubTitle.setVisibility(View.VISIBLE);
            } else {
                binding.tvSubTitle.setVisibility(View.GONE);
            }


            binding.includeTop.ivOk.setOnClickListener((v) -> {
                dialog.dismiss();
                Observable.create(new ObservableOnSubscribe<List<TagsChildBean>>() {

                    @Override
                    public void subscribe(ObservableEmitter<List<TagsChildBean>> emitter) throws Exception {
                        dialog.getObserver()
                                .accept(uiService.getSelectedTagsBean(binding.qflContainer));
                    }
                }).subscribe(dialog.getObserver());

            });

            binding.includeTop.vClose.setOnClickListener((v) -> {
                dialog.dismiss();
                Observable.create(new ObservableOnSubscribe<List<TagsChildBean>>() {

                    @Override
                    public void subscribe(ObservableEmitter<List<TagsChildBean>> emitter) throws Exception {
                        dialog.getObserver().accept(null);
                    }
                }).subscribe(dialog.getObserver());
            });

            dialog.addContentView(binding.getRoot(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
            layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
            dialog.getWindow().setAttributes(layoutParams);
            return dialog;
        }

        public void destory() {
        }


    }

    public void setLayoutChooseTagsBinding(LayoutChooseTagsBinding layoutChooseTagsBinding) {
        this.layoutChooseTagsBinding = layoutChooseTagsBinding;
    }

    public void setLayoutNewThemeDialogTopBinding(LayoutNewThemeDialogTopBinding layoutNewThemeDialogTopBinding) {
        this.layoutNewThemeDialogTopBinding = layoutNewThemeDialogTopBinding;
    }

    public void setLayoutOrderCommentSucBinding(LayoutOrderCommentSucBinding layoutOrderCommentSucBinding) {
        this.layoutOrderCommentSucBinding = layoutOrderCommentSucBinding;
    }

    public LayoutChooseTagsBinding getLayoutChooseTagsBinding() {
        return layoutChooseTagsBinding;
    }

    /**
     * 订单评论成功的View
     *
     * @param coinText
     * @param titleTipText
     */
    public void showSucView(String coinText, String titleTipText) {
        if (layoutOrderCommentSucBinding != null) {
            layoutOrderCommentSucBinding.getRoot().setVisibility(View.VISIBLE);
            if (layoutChooseTagsBinding != null) {
                layoutChooseTagsBinding.btnBottom.setVisibility(View.GONE);
            }
            if (!StringUtils.isEmpty(coinText)) {
                layoutOrderCommentSucBinding.tvCoinTip.setText(coinText);
                layoutOrderCommentSucBinding.tvCoinTip.setVisibility(View.VISIBLE);
            } else {
                layoutOrderCommentSucBinding.tvCoinTip.setVisibility(View.GONE);
            }
            if (layoutNewThemeDialogTopBinding != null) {
                layoutNewThemeDialogTopBinding.ivOk.setVisibility(View.GONE);
                layoutNewThemeDialogTopBinding.vOk.setVisibility(View.GONE);
            }
            if (!StringUtils.isEmpty(titleTipText)) {
                layoutNewThemeDialogTopBinding.tvSubtitle.setText(titleTipText);
                layoutNewThemeDialogTopBinding.tvSubtitle.setVisibility(View.VISIBLE);
            }
            layoutOrderCommentSucBinding.tvRecommend.setOnClickListener((v) -> {
                this.dismiss();
                UserService.getInstance().doRecommendFriend();
            });
            layoutOrderCommentSucBinding.ivRecommend.setOnClickListener((v) -> {
                this.dismiss();
                UserService.getInstance().doRecommendFriend();
            });
        }
    }


}
