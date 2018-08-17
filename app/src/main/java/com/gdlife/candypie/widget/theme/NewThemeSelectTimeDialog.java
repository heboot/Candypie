package com.gdlife.candypie.widget.theme;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.databinding.LayoutNewThemeTimeBinding;
import com.gdlife.candypie.serivce.ThemeService;
import com.heboot.bean.config.ConfigBean;
import com.heboot.bean.config.MyConfigSelectBean;
import com.heboot.utils.DateUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/2/2.
 */

public class NewThemeSelectTimeDialog extends Dialog {
    public NewThemeSelectTimeDialog(Context context) {
        super(context);
    }

    public NewThemeSelectTimeDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {

        private Context context;

        private Consumer<MyConfigSelectBean> observer;

        private ConfigBean.ServiceItemsConfigBean.ListBean listBean;

        private MyConfigSelectBean myConfigSelectBean;

        private long startTime;

        public Builder(Context context, Consumer<MyConfigSelectBean> observer, ConfigBean.ServiceItemsConfigBean.ListBean listBean) {
            this.context = context;
            this.observer = observer;
            this.listBean = listBean;
        }

        public NewThemeSelectTimeDialog create() {


            List<String> todayHours = ThemeService.getServiceTodayHours(listBean);

            List<String> todaySes = ThemeService.getServiceTodayMins();

            List<String> days;

            if (todayHours == null || todayHours.size() == 0) {
                days = ThemeService.getServiceDays(true);
            } else {
                days = ThemeService.getServiceDays(false);
            }


            List<String> hours = ThemeService.getServiceHours(listBean);

            List<String> mins = ThemeService.getServiceMins();

            List<String> du = ThemeService.getServiceDurations(listBean);


            final NewThemeSelectTimeDialog dialog = new NewThemeSelectTimeDialog(context, R.style.zhezhao_dialog);

            LayoutNewThemeTimeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_new_theme_time, null, false);


            binding.includeTop.tvTitme.setText(MAPP.mapp.getString(R.string.choose_time));

            binding.rv1.setOffset(1);
            binding.rv1.setItems(days);
            binding.rv1.setConsumer(new Consumer<String>() {
                @Override
                public void accept(String s) throws Exception {
                    if (todayHours != null && binding.rv1.getSeletedIndex() > 0) {
                        binding.rv2.setItems(hours);
                    } else if (todayHours != null && binding.rv1.getSeletedIndex() == 0) {
                        binding.rv2.setItems(todayHours);
                    } else {
                        binding.rv2.setItems(hours);
                    }
                    if (todayHours != null && binding.rv2.getSeletedIndex() > 0) {
                        binding.rv3.setItems(mins);
                    } else if (todayHours != null && binding.rv1.getSeletedIndex() == 0 && binding.rv2.getSeletedIndex() == 0) {
                        binding.rv3.setItems(todaySes);
                    }else {
                        binding.rv3.setItems(mins);
                    }

                    setTime(binding, days.size());
                }
            });

            binding.rv2.setOffset(1);
            binding.rv2.setItems(todayHours == null || todayHours.size() == 0 ? hours : todayHours);
            binding.rv2.setConsumer(new Consumer<String>() {
                @Override
                public void accept(String s) throws Exception {
                    if ((todayHours != null && binding.rv2.getSeletedIndex() > 0) || todaySes == null || todaySes.size() == 0) {
                        binding.rv3.setItems(mins);
                    } else if (todayHours != null && binding.rv1.getSeletedIndex() == 0 && binding.rv2.getSeletedIndex() == 0) {
                        binding.rv3.setItems(todaySes);
                    }
                    setTime(binding, days.size());
                }
            });

            binding.rv3.setOffset(1);
            binding.rv3.setItems(todaySes == null || todaySes.size() == 0 ? mins : todaySes);
            binding.rv3.setConsumer(new Consumer<String>() {
                @Override
                public void accept(String s) throws Exception {
                    setTime(binding, days.size());
                }
            });


            binding.rv4.setOffset(1);
            binding.rv4.setItems(du);
            binding.rv4.setConsumer(new Consumer<String>() {
                @Override
                public void accept(String s) throws Exception {
//                    binding.tvTimeTitle3.setText(
//                            ThemeService.getServiceEndText(
//                                    listBean,
//                                    binding.rv1.getSeletedIndex(),
//                                    binding.rv2.getSeletedIndex(),
//                                    binding.rv3.getSeletedIndex(),
//                                    binding.rv4.getSeletedIndex()));

                    setTime(binding, days.size());

                }
            });

            setTime(binding, days.size());

            binding.includeTop.vClose.setOnClickListener((v) -> {
                dialog.dismiss();
            });

            binding.includeTop.ivOk.setOnClickListener((v) -> {
                Observable.create(new ObservableOnSubscribe<MyConfigSelectBean>() {

                    @Override
                    public void subscribe(ObservableEmitter<MyConfigSelectBean> emitter) throws Exception {

                        setTime(binding, days.size());

                        dialog.dismiss();

                        myConfigSelectBean = new MyConfigSelectBean();

                        myConfigSelectBean.setRv1SelectedIndex(binding.rv1.getSeletedIndex());
                        myConfigSelectBean.setRv1SelectedText(binding.rv1.getSeletedItem());

                        myConfigSelectBean.setRv2SelectedIndex(binding.rv2.getSeletedIndex());
                        myConfigSelectBean.setRv2SelectedText(binding.rv2.getSeletedItem());

                        myConfigSelectBean.setRv3SelectedIndex(binding.rv3.getSeletedIndex());
                        myConfigSelectBean.setRv3SelectedText(binding.rv3.getSeletedItem());

                        myConfigSelectBean.setRv4SelectedIndex(binding.rv4.getSeletedIndex());
                        myConfigSelectBean.setRv4SelectedText(binding.rv4.getSeletedItem());

                        myConfigSelectBean.setSelectedText(
//                                binding.rv1.getSeletedItem() + binding.rv2.getSeletedItem() + binding.rv3.getSeletedItem() +
//                                DateUtil.getHoursByDuration(listBean.getTime_price().get(binding.rv4.getSeletedIndex()).getTime()) + MAPP.mapp.getString(R.string.hours)
                                ThemeService.newThemeSelectedText(startTime, startTime + listBean.getTime_price().get(binding.rv4.getSeletedIndex()).getTime() * 60 * 1000) + "，" + DateUtil.getHoursByDuration(listBean.getTime_price().get(binding.rv4.getSeletedIndex()).getTime()) + MAPP.mapp.getString(R.string.hours)
                        );

                        myConfigSelectBean.setStart_time(startTime);

                        emitter.onNext(myConfigSelectBean);
                    }
                }).subscribe(observer);
            });

            dialog.addContentView(binding.getRoot(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
            layoutParams.gravity = Gravity.BOTTOM;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
            dialog.getWindow().setAttributes(layoutParams);

            return dialog;
        }

        private void setTime(LayoutNewThemeTimeBinding binding, int daysSize) {
            binding.tvTimeTitle3.setText(
                    ThemeService.getServiceEndText3(
                            listBean,
                            binding.rv1.getSeletedIndex(),
                            binding.rv2.getSeletedItem(),
                            binding.rv3.getSeletedItem(),
                            binding.rv4.getSeletedItem()));
            startTime = ThemeService.getServiceStartTime(binding.rv1.getSeletedIndex(),
                    binding.rv2.getSeletedItem(),
                    binding.rv3.getSeletedItem(), daysSize);// binding.rv1.getItems().size()
        }
    }


}
