package com.gdlife.candypie.activitys.user;

import android.view.KeyEvent;
import android.view.View;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.TagsDialogType;
import com.gdlife.candypie.databinding.ActivitySetPriceBinding;
import com.gdlife.candypie.serivce.AuthService;
import com.gdlife.candypie.serivce.UIService;
import com.gdlife.candypie.serivce.UserInfoService;
import com.gdlife.candypie.serivce.user.SetPriceService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.widget.dialog.ChooseTagsDialog;
import com.gdlife.candypie.widget.setprice.SetPriceCoinDialog;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.user.SetPriceInitBean;
import com.heboot.bean.user.TagsChildBean;
import com.heboot.dialog.TipCustomOneDialog;
import com.heboot.event.MeEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.LogUtil;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.List;

import dagger.MapKey;
import io.reactivex.functions.Consumer;

public class SetPriceActivity extends BaseActivity<ActivitySetPriceBinding> {

    private SetPriceService setPriceService = new SetPriceService();

    private SetPriceCoinDialog setPriceCoinDialog;

    private Consumer<Integer> consumer;

    private SetPriceInitBean setPriceInitBean;

    private String currentVideoPrice;

    private ChooseTagsDialog chooseTagsDialog;

    private UIService uiService = new UIService();

    private Consumer<List<TagsChildBean>> tagsConsumer;

    private List<String> selectTagIds;

    private List<String> savedTagIds;

    private boolean fromIndex = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_price;
    }

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.tvTitle.setText("设置价格");

    }

    @Override
    public void initData() {

        fromIndex = getIntent().getExtras().getBoolean(MKey.FROM);

        initSettingData();
    }


    /**
     * 初始化设置数据
     */
    private void initSettingData() {
        setPriceService.preUserMeetTags(new HttpObserver<SetPriceInitBean>() {
            @Override
            public void onSuccess(BaseBean<SetPriceInitBean> baseBean) {
                setPriceInitBean = baseBean.getData();


                if (baseBean.getData().getVideo_chat_config() != null) {
                    if (!SetPriceActivity.this.isDestroyed()) {
                        ImageUtils.showAvatar(binding.ivImgVideo, setPriceInitBean.getVideo_chat_config().getIcon());
                        if (!StringUtils.isEmpty(baseBean.getData().getVideo_chat_config().getPrice())) {
                            currentVideoPrice = baseBean.getData().getVideo_chat_config().getPrice();
                            setUIPrice();
                        }
                    }
//                    setPriceCoinDialog = new SetPriceCoinDialog.Builder(SetPriceActivity.this, setPriceService.getSetVideoCoinPriceList(baseBean.getData().getVideo_tags_config().getItems()), consumer).create();
//                    setPriceCoinDialog.show();
                }

                if (baseBean.getData().getVideo_tags_config() != null) {
                    selectTagIds = UserInfoService.getSelectedIds(baseBean.getData().getVideo_tags_config().getSelect_items());
                    if (selectTagIds != null && selectTagIds.size() > 0) {
                        savedTagIds = selectTagIds;
                        initTagsPreView(setPriceInitBean.getVideo_tags_config().getSelect_items());
                    }
                    ImageUtils.showAvatar(binding.ivImgVideoTags, setPriceInitBean.getVideo_tags_config().getIcon());
                }
            }

            @Override
            public void onError(BaseBean<SetPriceInitBean> baseBean) {

            }
        });
    }


    /**
     *
     */
    private void setUIPrice() {
        binding.tvGoSetVideoPrice.setVisibility(View.GONE);
        binding.llytSelectedPrice.setVisibility(View.VISIBLE);
        binding.tvVideoPrice.setText(currentVideoPrice);
    }

    private void setVideoTags() {
        setPriceService.edit_video_tags(selectTagIds, new HttpObserver<BaseBeanEntity>() {
            @Override
            public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                savedTagIds = selectTagIds;
                tipDialog = DialogUtils.getSuclDialog(SetPriceActivity.this, baseBean.getMessage(), true);
                tipDialog.show();
                RxBus.getInstance().post(MeEvent.REFRESH_ME_BY_SET_VIDEO_PRICE);
            }

            @Override
            public void onError(BaseBean<BaseBeanEntity> baseBean) {

            }
        });
    }

    /**
     * 设置视频价格
     *
     * @param price
     */
    private void setServicePrice(String price) {
        setPriceService.set_service_price(price, new HttpObserver<BaseBeanEntity>() {
            @Override
            public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                currentVideoPrice = price;
                setUIPrice();
                tipDialog = DialogUtils.getSuclDialog(SetPriceActivity.this, baseBean.getMessage(), true);
                tipDialog.show();
                RxBus.getInstance().post(MeEvent.REFRESH_ME_BY_SET_VIDEO_PRICE);
            }

            @Override
            public void onError(BaseBean<BaseBeanEntity> baseBean) {

            }
        });
    }

    @Override
    public void initListener() {

        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            if (checkSelected()) {
                finish();
            }
        });

        binding.vSetVideoPrice.setOnClickListener((v) -> {
            if (setPriceInitBean == null) {
                return;
            }
            setPriceCoinDialog = new SetPriceCoinDialog.Builder(SetPriceActivity.this, setPriceInitBean.getVideo_chat_config().getTip(), setPriceService.getSetVideoCoinPriceList(setPriceInitBean.getVideo_chat_config().getItems()), consumer).create();
            setPriceCoinDialog.show();
        });

        binding.vSetVideoTag.setOnClickListener((v) -> {
            if (setPriceInitBean == null) {
                return;
            }

            chooseTagsDialog = new ChooseTagsDialog.Builder(this, setPriceInitBean.getVideo_tags_config().getTip(), uiService, setPriceInitBean.getVideo_tags_config().getItems(), tagsConsumer, "视频特色标签", TagsDialogType.CHOOSE_VIDEO_TAG, selectTagIds).create();
            chooseTagsDialog.show();
        });

        tagsConsumer = new Consumer<List<TagsChildBean>>() {
            @Override
            public void accept(List<TagsChildBean> tagsChildBeans) throws Exception {
                if (tagsChildBeans != null && tagsChildBeans.size() > 0) {
                    selectTagIds = UserInfoService.getSelectedIds(tagsChildBeans);
                    initTagsPreView(tagsChildBeans);

                    setVideoTags();
                }
            }
        };


        consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                if (integer != null) {
                    setServicePrice(setPriceInitBean.getVideo_chat_config().getItems().get(integer).getContent());
                }
            }
        };
    }

    private void initTagsPreView(List<TagsChildBean> tagsChildBeans) {
        uiService.initTagsLayout(tagsChildBeans, binding.qflContainer, getResources().getDimensionPixelOffset(R.dimen.y14), getResources().getDimensionPixelOffset(R.dimen.x12), false, getResources().getDimensionPixelOffset(R.dimen.y30), selectTagIds);
    }

    private boolean checkSelected() {
        if (StringUtils.isEmpty(currentVideoPrice)) {
            TipCustomOneDialog tipCustomOneDialog = new TipCustomOneDialog.Builder(this, "你还没有设置视频聊天价格", getString(R.string.iknow)).create();
            tipCustomOneDialog.show();
            return false;
        } else if (savedTagIds == null) {
            TipCustomOneDialog tipCustomOneDialog = new TipCustomOneDialog.Builder(this, "你还没有选择视频特色标签", getString(R.string.iknow)).create();
            tipCustomOneDialog.show();
            return false;
        } else if (!StringUtils.isEmpty(currentVideoPrice) && savedTagIds != null && savedTagIds.size() > 0) {
            return true;
        }
        return false;
    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (checkSelected()) {
//                if (fromIndex) {
////                    IntentUtils.toIndexServicerContainerActivity(this, null);
//                    AuthService.newOrderToMessageRobPage();
//                    finish();
//                } else {
//                    finish();
//                }
//
//            }
//        }
//        return true;
//    }

}
