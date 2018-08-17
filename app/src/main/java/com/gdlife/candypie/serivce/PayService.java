package com.gdlife.candypie.serivce;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;

import com.alipay.sdk.app.PayTask;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.PayType;
import com.gdlife.candypie.common.RechargeType;
import com.gdlife.candypie.databinding.LayoutPayTypesBinding;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.heboot.bean.pay.ServicePaymentBeanModel;
import com.heboot.event.PayEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.DateUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/3/12.
 */

public class PayService {

    private IWXAPI api;

    public boolean installWX(Context context) {
        if (api == null || !api.registerApp(MValue.WX_APPID)) {
            api = WXAPIFactory.createWXAPI(context, MValue.WX_APPID);
            return api.isWXAppInstalled();
        }
        return api.isWXAppInstalled();
    }


    /**
     * 根据优惠券 账户余额 和 应该支付的金额 计算 最终要支付的金额
     *
     * @return
     */
    public float getPayMoney(float couponMoney, float balance, float amout) {
        int result = 0;
        //有券 并且券比支付金额大 可以免费
        if (couponMoney > 0 && couponMoney >= amout) {
            return 0;
        }
        //有券 有余额
        else if (couponMoney < amout && balance > 0) {
            return amout - couponMoney - balance <= 0 ? 0 : amout - couponMoney - balance;
        }
        //没券 有余额
        else if (couponMoney < amout && balance > 0) {
            return amout - balance <= 0 ? 0 : amout - balance;
        }
        //没券 没余额
        else if (couponMoney == 0 && balance <= 0) {
            return amout;
        }
        //有券 没余额
        else if (couponMoney < amout && balance <= 0) {
            return amout - couponMoney <= 0 ? 0 : amout - couponMoney;
        }
        return 0;
    }


    /**
     * 账户金额应该抵多少
     *
     * @param couponMoney
     * @param amout
     * @return
     */
    public float getBalanceMoney(float couponMoney, float amout) {
        if (amout - couponMoney <= 0) {
            return 0;
        } else {
            return covertCouponValue(UserService.getInstance().getUser().getBalance()) >= (amout - couponMoney) ? amout - couponMoney : covertCouponValue(UserService.getInstance().getUser().getBalance());
        }

    }


    /**
     * 检查阿里的支付回调
     *
     * @param stringStringMap
     * @param rechargeType
     */
    public void checkAliPayResult(Map<String, String> stringStringMap, RechargeType rechargeType) {
        if (!StringUtils.isEmpty(stringStringMap.get("resultStatus"))) {
            int resultStatus = Integer.parseInt(stringStringMap.get("resultStatus"));
            if (resultStatus == 9000) {
                if (rechargeType == null) {
                    RxBus.getInstance().post(PayEvent.PaySUCEvent);
                } else {
                    if (rechargeType == RechargeType.GOLD_VIP) {
                        RxBus.getInstance().post(PayEvent.RechargeGoldVipSUCEvent);
                    } else {
                        RxBus.getInstance().post(PayEvent.RechargeSUCEvent);
                    }

                }
            } else {
//                ToastUtils.showToast("支付取消");
            }
        }
//
    }


    /**
     * 转换
     *
     * @param value
     * @return
     */
    public float covertCouponValue(String value) {
        return Float.parseFloat(value);
    }

    /**
     * 设置支付选项的显示
     */
    public void setPayTypesUI(LayoutPayTypesBinding binding, List<String> types) {
        if (types != null) {
            for (String s : types) {
                if (s.toUpperCase().equals(PayType.ALIPAY.toString())) {
                    binding.includeAli.getRoot().setVisibility(View.VISIBLE);
                    binding.lineAli.setVisibility(View.VISIBLE);
                } else if (s.toUpperCase().equals(PayType.WEIXIN.toString())) {
                    if (installWX(binding.getRoot().getContext())) {
                        binding.includeWx.getRoot().setVisibility(View.VISIBLE);
                        binding.lineWx.setVisibility(View.VISIBLE);
                    } else {
                        binding.includeWx.getRoot().setVisibility(View.GONE);
                        binding.lineWx.setVisibility(View.GONE);
                    }
                }
            }
        }
    }


    /**
     * 设置支付选项的事件
     *
     * @param binding
     */
    public void setPayTypesListener(LayoutPayTypesBinding binding, boolean useBalance) {
        binding.includeAli.getRoot().setOnClickListener((v) -> {
            if (binding.includeWx.cbCheck.isChecked()) {
                binding.includeWx.cbCheck.setChecked(false);
            }
            binding.includeAli.cbCheck.setChecked(true);
        });
        binding.includeWx.getRoot().setOnClickListener((v) -> {
            if (binding.includeAli.cbCheck.isChecked()) {
                binding.includeAli.cbCheck.setChecked(false);
            }
            binding.includeWx.cbCheck.setChecked(true);
        });
        binding.includeAli.cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (binding.includeWx.cbCheck.isChecked()) {
                        binding.includeWx.cbCheck.setChecked(false);
                    }
                    binding.includeAli.cbCheck.setChecked(true);
                } else {
                    if (binding.includeWx.cbCheck.isChecked()) {
                        binding.includeWx.cbCheck.setChecked(false);
                    }
                    binding.includeAli.cbCheck.setChecked(true);
                }

            }
        });
        binding.includeWx.cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (binding.includeAli.cbCheck.isChecked()) {
                        binding.includeAli.cbCheck.setChecked(false);
                    }
                    binding.includeWx.cbCheck.setChecked(true);
                } else {
                    if (binding.includeAli.cbCheck.isChecked()) {
                        binding.includeAli.cbCheck.setChecked(false);
                    }
                    binding.includeWx.cbCheck.setChecked(true);
                }

            }
        });
    }

    /**
     * 调用第三方支付
     */
    public void doPay(PayType payType, Activity activity, ServicePaymentBeanModel orderInfo, Consumer<Map<String, String>> sub) {
        if (payType == PayType.ALIPAY) {
            toAliPay(activity, orderInfo.getPayment_config().getPayment_params(), sub);
        } else if (payType == PayType.WEIXIN) {
            toWXPay(orderInfo.getPayment_config());
        }
    }

    public void toAliPay(Activity activity, String orderInfo, Consumer<Map<String, String>> sub) {
        Observable.create(new ObservableOnSubscribe<Map<String, String>>() {

            @Override
            public void subscribe(ObservableEmitter<Map<String, String>> e) throws Exception {
                PayTask payTask = new PayTask(activity);
                Map<String, String> maps = payTask.payV2(orderInfo, true);
                sub.accept(maps);
            }

        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe();
    }

    public void toWXPay(ServicePaymentBeanModel.PaymentConfigBean model) {//PayConfigBean model
        if (api == null || !api.registerApp(model.getAppid())) {
            api = WXAPIFactory.createWXAPI(MAPP.mapp, model.getAppid());
            api.registerApp(model.getAppid());
        }
        PayReq request = new PayReq();
        request.appId = model.getAppid();
        request.partnerId = model.getPartnerid();
        request.prepayId = model.getPrepayid();
        request.packageValue = model.getPackageData();
        request.nonceStr = model.getNoncestr();
        request.timeStamp = model.getTimestamp();
        request.sign = model.getSign();
        api.sendReq(request);
    }


    public String getCashDateStr(List<String> week) {
        String result = "每周";
        String weekstr = "";

        if (week != null && week.size() > 0 && week.size() == 1) {
            for (String we : week) {
                switch (we) {
                    case "1":
                        weekstr = weekstr + "一";
                        break;
                    case "2":
                        weekstr = weekstr + "二";
                        break;
                    case "3":
                        weekstr = weekstr + "三";
                        break;
                    case "4":
                        weekstr = weekstr + "四";
                        break;
                    case "5":
                        weekstr = weekstr + "五";
                        break;
                    case "6":
                        weekstr = weekstr + "六";
                        break;
                    case "7":
                        weekstr = weekstr + "日";
                        break;
                }
            }
            return result + weekstr + "可提现";
        } else if (week != null && week.size() > 0 && week.size() == 2) {
            boolean appenFlag = false;
            for (String we : week) {
                switch (we) {
                    case "1":
                        if (!appenFlag) {
                            weekstr = weekstr + "一";
                        } else {
                            weekstr = weekstr + ",一";
                        }
                        appenFlag = true;
                        break;
                    case "2":
                        if (!appenFlag) {
                            weekstr = weekstr + "二";
                        } else {
                            weekstr = weekstr + ",二";
                        }
                        appenFlag = true;
                        break;
                    case "3":
                        if (!appenFlag) {
                            weekstr = weekstr + "三";
                        } else {
                            weekstr = weekstr + ",三";
                        }
                        appenFlag = true;
                        break;
                    case "4":
                        if (!appenFlag) {
                            weekstr = weekstr + "四";
                        } else {
                            weekstr = weekstr + ",四";
                        }
                        appenFlag = true;
                        break;
                    case "5":
                        if (!appenFlag) {
                            weekstr = weekstr + "五";
                        } else {
                            weekstr = weekstr + ",五";
                        }
                        appenFlag = true;
                        break;
                    case "6":
                        if (!appenFlag) {
                            weekstr = weekstr + "六";
                        } else {
                            weekstr = weekstr + ",六";
                        }
                        appenFlag = true;
                        break;
                    case "7":
                        if (!appenFlag) {
                            weekstr = weekstr + "日";
                        } else {
                            weekstr = weekstr + ",日";
                        }
                        appenFlag = true;
                        break;
                }
            }
            return result + weekstr + "可提现";
        }
        return null;

    }


    public static int getTurntableNeedCoin(int coinAmount, int turnAmount) {
        int outime = 0;
        int result = MValue.currentVideoTime - 180;
        if (result > 0) {
            if (result < 60) {
                outime = 1;
            } else {
                outime = (int) Math.ceil(result / 60);
            }
        }else{
             return turnAmount;
        }
        return outime * coinAmount + turnAmount;
    }


}
