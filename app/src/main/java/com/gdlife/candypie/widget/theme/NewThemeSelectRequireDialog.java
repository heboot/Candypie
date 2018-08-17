package com.gdlife.candypie.widget.theme;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.databinding.LayoutNewThemeRequireBinding;
import com.gdlife.candypie.serivce.ThemeService;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.bean.config.ConfigBean;
import com.heboot.bean.config.MyConfigSelectBean;
import com.heboot.bean.theme.ServiceReqParamsBean;
import com.heboot.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/2/2.
 */

public class NewThemeSelectRequireDialog extends Dialog {
    public NewThemeSelectRequireDialog(Context context) {
        super(context);
    }

    public NewThemeSelectRequireDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {

        private Context context;

        private int currentSelect = 0;

        private LayoutNewThemeRequireBinding binding;

        private ObjectAnimator animator;

        private Consumer<MyConfigSelectBean> observer;

        private MyConfigSelectBean myConfigSelectBean;

        private ConfigBean.RequireIdsBean requireIdsBean;

        private boolean isVideo;

        private int nums = 1;

        private boolean v1;

        public Builder(Context context, Consumer<MyConfigSelectBean> observer, ConfigBean.RequireIdsBean requireIdsBean, boolean isVideo, boolean v1) {
            this.context = context;
            this.observer = observer;
            this.requireIdsBean = requireIdsBean;
            this.isVideo = isVideo;
            this.v1 = v1;

        }

        public NewThemeSelectRequireDialog create() {


            final NewThemeSelectRequireDialog dialog = new NewThemeSelectRequireDialog(context, R.style.zhezhao_dialog);

            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_new_theme_require, null, false);

            binding.includeTop.tvTitme.setText(MAPP.mapp.getString(R.string.choose_req));

            if (requireIdsBean != null) {
                binding.tvWineTitle.setVisibility(View.VISIBLE);
                binding.tvWineTitle.setText(requireIdsBean.getContent());
            } else {
                binding.sbWine.setVisibility(View.GONE);
                binding.tvWineTitle.setVisibility(View.GONE);
            }

            if (v1) {
                binding.includeNums.getRoot().setVisibility(View.GONE);
                binding.tvNumsTitle.setVisibility(View.GONE);

                binding.includeSex.getRoot().setVisibility(View.GONE);
                binding.tvSexTitle.setVisibility(View.GONE);
            }

            if (isVideo) {
                binding.includeNums.getRoot().setVisibility(View.GONE);
                binding.tvNumsTitle.setVisibility(View.GONE);

                binding.sbWine.setVisibility(View.GONE);
                binding.tvWineTitle.setVisibility(View.GONE);
            }

            binding.includeSex.tvMan.setOnClickListener((v) -> {
                doAni(binding.includeSex.v1, 1);
            });
            binding.includeSex.tvWoman.setOnClickListener((v) -> {
                doAni(binding.includeSex.v1, 0);

            });
            binding.includeSex.tvOther.setOnClickListener((v) -> {
                doAni(binding.includeSex.v1, 2);
            });

            binding.includeTop.vClose.setOnClickListener((v) -> {
                dialog.dismiss();
            });

            binding.includeNums.tvJia.setOnClickListener((v) -> {
                nums = nums + 1;
                binding.includeNums.tvNum.setText("" + nums);
            });

            binding.includeNums.tvJian.setOnClickListener((v) -> {
                if (nums > 1) {
                    nums = nums - 1;
                    binding.includeNums.tvNum.setText("" + nums);
                }
            });

            binding.includeSex.tvWoman.setSelected(true);


            binding.includeTop.ivOk.setOnClickListener((v) -> {
                String sex = "不限";
                switch (currentSelect) {
                    case 0:
                        sex = "女";
                        break;
                    case 1:
                        sex = "男";
                        break;
                    case 2:
                        sex = "不限";
                        break;
                }
                String finalSex = sex;
                Observable.create(new ObservableOnSubscribe<MyConfigSelectBean>() {

                    @Override
                    public void subscribe(ObservableEmitter<MyConfigSelectBean> emitter) throws Exception {
                        dialog.dismiss();


                        myConfigSelectBean = new MyConfigSelectBean();
                        ServiceReqParamsBean serviceReqParamsBean = new ServiceReqParamsBean();
                        if (requireIdsBean != null) {
                            myConfigSelectBean.setRequireIdsBean(requireIdsBean);
//                            serviceReqParamsBean.setExtend(JSON.toJSONString(requireIdsBean));
//                            if (binding.sbWine.isChecked()) {
                            Map map = new HashMap();
                            map.put("" + requireIdsBean.getId(), (binding.sbWine.isChecked() ? "1" : "0"));
//                            serviceReqParamsBean.setExtend("{\"" + requireIdsBean.getId() + "\"" + ":" + (binding.sbWine.isChecked() ? "1" : "0") + "}");
//                            }
                            LogUtil.e("测试2", JSON.toJSONString(map));

                            serviceReqParamsBean.setExtend(JSON.toJSONString(map));

                        }

                        if (v1) {
                            String s = "";
                            if (binding.sbWine.getVisibility() == View.VISIBLE && binding.tvWineTitle.getVisibility() == View.VISIBLE) {
                                s = binding.sbWine.isChecked() ? requireIdsBean.getContent() : "";
                            }
                            s = s + (binding.sbNoname.isChecked() ? MAPP.mapp.getString(R.string.privacy) : "");

                            if (StringUtils.isEmpty(s)) {
                                s = "无要求";
                            }
                            myConfigSelectBean.setSelectedText(s);
                        } else {
                            serviceReqParamsBean.setNums(Integer.parseInt(binding.includeNums.tvNum.getText().toString()));

                            serviceReqParamsBean.setSex(currentSelect);

                            serviceReqParamsBean.setPrivacy(binding.sbNoname.isChecked() ? 1 : 0);

                            myConfigSelectBean.setServiceReqParamsBean(serviceReqParamsBean);

                            String s = ThemeService.getRequireStr(
                                    Integer.parseInt(binding.includeNums.tvNum.getText().toString()),
                                    currentSelect,
                                    binding.sbNoname.isChecked() ? 1 : 0,
                                    binding.sbWine.isChecked() ? requireIdsBean.getContent() : ""
                            );

                            myConfigSelectBean.setSelectedText(s);
                        }


                        emitter.onNext(myConfigSelectBean);

                    }
                }).subscribe(observer);
            });

            binding.includeTop.ivOk.performClick();

            dialog.addContentView(binding.getRoot(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
            layoutParams.gravity = Gravity.BOTTOM;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
            dialog.getWindow().setAttributes(layoutParams);


            return dialog;
        }

        public void doAni(View v, int goSelect) {
            if (goSelect == currentSelect) {
                return;
            }

            if (animator != null) {
                animator.setDuration(200);
            }

            switch (currentSelect) {
                case 0:
                    if (goSelect == 1) {
                        animator = ObjectAnimator.ofFloat(v, "x", v.getX(), v.getX() + binding.includeSex.v1.getWidth());
                        animator.start();
                    } else if (goSelect == 2) {
                        animator = ObjectAnimator.ofFloat(v, "x", v.getX(), v.getX() + (binding.includeSex.v1.getWidth() * 2));
                        animator.start();
                    }
                    break;
                case 1:
                    if (goSelect == 0) {
                        animator = ObjectAnimator.ofFloat(v, "x", v.getX(), v.getX() - binding.includeSex.v1.getWidth());
                        animator.start();
                    } else if (goSelect == 2) {
                        animator = ObjectAnimator.ofFloat(v, "x", v.getX(), v.getX() + binding.includeSex.v1.getWidth());
                        animator.start();
                    }
                    break;
                case 2:
                    if (goSelect == 0) {
                        animator = ObjectAnimator.ofFloat(v, "x", v.getX(), 0);
                        animator.start();
                    } else if (goSelect == 1) {
                        animator = ObjectAnimator.ofFloat(v, "x", v.getX(), v.getX() + -binding.includeSex.v1.getWidth());
                        animator.start();
                    }
                    break;
            }

            if (animator.getListeners() == null) {
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (currentSelect == 0) {
                            binding.includeSex.tvWoman.setSelected(true);
                            binding.includeSex.tvMan.setSelected(false);
                            binding.includeSex.tvOther.setSelected(false);
                        } else if (currentSelect == 1) {
                            binding.includeSex.tvWoman.setSelected(false);
                            binding.includeSex.tvMan.setSelected(true);
                            binding.includeSex.tvOther.setSelected(false);
                        } else if (currentSelect == 2) {
                            binding.includeSex.tvWoman.setSelected(false);
                            binding.includeSex.tvMan.setSelected(false);
                            binding.includeSex.tvOther.setSelected(true);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }

            currentSelect = goSelect;
        }
    }


}
