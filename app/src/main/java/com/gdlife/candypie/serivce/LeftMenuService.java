package com.gdlife.candypie.serivce;

import android.view.View;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.ReportFromType;
import com.gdlife.candypie.databinding.LayoutLeftmenuHeadBinding;
import com.gdlife.candypie.serivce.theme.VideoChatService;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.PermissionUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.heboot.entity.User;
import com.heboot.utils.LogUtil;
import com.heboot.utils.ViewUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.suke.widget.SwitchButton;

/**
 * Created by heboot on 2018/3/9.
 */

public class LeftMenuService {


    private VideoChatService videoChatService;

    public void initLeftUI(LayoutLeftmenuHeadBinding binding, User user, boolean isFrist) {


        if (isFrist) {

            binding.setUser(user);

            initMenuUIFirst(binding);

            initUnreadText(binding);

            initBottomMenuFirst(binding, user);

            initAuthUI(binding);


        } else {
            binding.setUser(user);

            initUnreadText(binding);

            initAuthUI(binding);

        }

        ImageUtils.showAvatar(binding.ivHead, user == null ? "" : user.getAvatar());

    }

    private void initUnreadText(LayoutLeftmenuHeadBinding binding) {
        int unread = NIMClient.getService(MsgService.class)
                .getTotalUnreadCount();
        if (unread > 0) {
            if (unread > 99) {
                unread = 99;
            }
            binding.includeMenu3.tvTip.setText(String.valueOf(unread));
            binding.includeMenu3.tvTip.setVisibility(View.VISIBLE);
        } else {
            binding.includeMenu3.tvTip.setVisibility(View.INVISIBLE);
        }
    }

    private void initAuthUI(LayoutLeftmenuHeadBinding binding) {
        //服务者
        if (UserService.getInstance().getUser() != null && UserService.getInstance().getUser().getRole() != null && UserService.getInstance().getUser().getRole() == MValue.USER_ROLE_SERVICER && UserService.getInstance().getUser().getService_auth_status() != null && UserService.getInstance().getUser().getService_auth_status() == MValue.AUTH_STATUS_SUC) {
            showSetPriceView(binding);
            showVideoEnableView(binding);
            binding.includeBottom3.ivMenu.setText(MAPP.mapp.getString(R.string.left_service_tip_kpi));
            binding.includeBottom3.ivImg.setBackgroundResource(R.drawable.icon_left_auth);
            if (MAPP.mapp.getConfigBean().getFunction_module_switch() != null && MAPP.mapp.getConfigBean().getFunction_module_switch().getServicer_kpi() == 1) {
                binding.includeBottom3.getRoot().setVisibility(View.VISIBLE);
            } else {
                binding.includeBottom3.getRoot().setVisibility(View.GONE);
            }
            binding.includeBottom2.getRoot().setOnClickListener((v) -> {
                IntentUtils.toHTMLActivity(binding.getRoot().getContext(), "", MAPP.mapp.getConfigBean().getStatic_url_config().getServicer_help());
            });
            binding.includeBottom3.getRoot().setOnClickListener((v) -> {
                if (UserService.getInstance().checkTourist()) {
                    return;
                }
                IntentUtils.toKpiActivity(binding.getRoot().getContext());
            });
        }
        //普通用户
        else {
            hideVideoEnableView(binding);
            hideSetPriceView(binding);
            binding.includeBottom3.ivMenu.setText(MAPP.mapp.getString(R.string.left_service_tip_auth));
            binding.includeBottom3.ivImg.setBackgroundResource(R.drawable.icon_left_auth);
            binding.includeBottom2.getRoot().setOnClickListener((v) -> {
                IntentUtils.toHTMLActivity(binding.getRoot().getContext(), "", MAPP.mapp.getConfigBean().getStatic_url_config().getUser_help());
            });
            binding.includeBottom3.getRoot().setOnClickListener((v) -> {
                if (UserService.getInstance().checkTourist()) {
                    return;
                }
                AuthService.toAuthPageByIndex(v.getContext());
            });
        }
    }

    private void initMenuUIFirst(LayoutLeftmenuHeadBinding binding) {
        binding.includeMenu1.tvMenu.setText(R.string.left_menu_order_text);
        binding.includeMenu1.ivImg.setBackgroundResource(R.drawable.icon_left_order);
        binding.includeMenu2.tvMenu.setText(R.string.left_menu_fav_text);
        binding.includeMenu2.ivImg.setBackgroundResource(R.drawable.icon_left_favs);
        binding.includeMenu3.tvMenu.setText(R.string.left_menu_msg_text);
        binding.includeMenu3.ivImg.setBackgroundResource(R.drawable.icon_left_msg);
        binding.includeMenu4.tvMenu.setText(R.string.left_menu_account_text);
        binding.includeMenu4.ivImg.setBackgroundResource(R.drawable.icon_left_account);
        binding.includeMenu5.tvMenu.setText("美颜设置");
        binding.includeMenu5.ivImg.setBackgroundResource(R.drawable.icon_left_face);
        binding.includeBottom3.ivImg.setBackgroundResource(R.drawable.icon_left_auth);
        binding.includeMenu5.getRoot().setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist()) {
                return;
            }
            PermissionUtils permissionUtils = new PermissionUtils();
            if (permissionUtils.hasCameraPermission(MAPP.mapp)) {
                IntentUtils.toFaceSettingActivity(binding.getRoot().getContext());
            } else {
                permissionUtils.getCameraPermission(MAPP.mapp.getCurrentActivity());
            }

//            IntentUtils.toReportActivity(binding.getRoot().getContext(), UserService.getInstance().getUser() == null ? null : String.valueOf(UserService.getInstance().getUser().getId()), ReportFromType.FEEBACK);
        });
        binding.ivHead.setOnClickListener((v) -> {
//                IntentUtils.toNewServiceProgressActivity(binding.getRoot().getContext(),"");
            if (UserService.getInstance().checkTourist()) {
                return;
            }
            if (UserService.getInstance().getUser().getRole() == MValue.USER_ROLE_SERVICER && UserService.getInstance().getUser().getService_auth_status() != null && UserService.getInstance().getUser().getService_auth_status() == MValue.AUTH_STATUS_SUC) {
                IntentUtils.toHomepageActivity(v.getContext(), MValue.FROM_MY, UserService.getInstance().getUser(), null, null);
            } else {
                IntentUtils.toUserInfoActivity(binding.getRoot().getContext(), MValue.FROM_MY, MValue.USER_INFO_TYPE_NORMAL, UserService.getInstance().getUser(), null, null);
            }
        });
        binding.includeMenu2.getRoot().setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist()) {
                return;
            }
            IntentUtils.toUserFavsListActivity(binding.getRoot().getContext());
        });

        binding.includeMenu3.getRoot().setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist()) {
                return;
            }
            IntentUtils.toRecentContactsActivity(binding.getRoot().getContext());
        });


        binding.includeMenu4.getRoot().setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist()) {
                return;
            }
            IntentUtils.toAccountActivity(binding.getRoot().getContext());
        });

        binding.includeMenu1.getRoot().setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist()) {
                return;
            }
            IntentUtils.toUserOrderActivity(binding.getRoot().getContext());
        });

        binding.includeBottom1.getRoot().setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist()) {
                return;
            }
            IntentUtils.toSettingActivity(binding.getRoot().getContext());
        });

    }

    private void showVideoEnableView(LayoutLeftmenuHeadBinding binding) {
//        binding.includeMenuEnableVideo.getRoot().setAlpha(1f);
        binding.includeMenuEnableVideo.getRoot().setVisibility(View.VISIBLE);
        binding.includeMenuEnableVideo.tvMenu.setText("视频接听");
        binding.includeMenuEnableVideo.ivImg.setBackgroundResource(R.drawable.icon_menu_video_enable);
        if (UserService.getInstance().getUser() != null && UserService.getInstance().getUser().getVideo_chat_status() == 1) {
            binding.includeMenuEnableVideo.sbVideoEnable.setChecked(true);
        } else {
            binding.includeMenuEnableVideo.sbVideoEnable.setChecked(false);
        }
        binding.includeMenuEnableVideo.sbVideoEnable.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    if (UserService.getInstance().getUser().getVideo_chat_status() == 1) {
                        return;
                    }
                    if (videoChatService == null) {
                        videoChatService = new VideoChatService();
                    }
                    videoChatService.switch_video_chat_status(binding.includeMenuEnableVideo.sbVideoEnable);
                } else {
                    if (UserService.getInstance().getUser().getVideo_chat_status() == 0) {
                        return;
                    }
                    if (videoChatService == null) {
                        videoChatService = new VideoChatService();
                    }
                    videoChatService.switch_video_chat_status(binding.includeMenuEnableVideo.sbVideoEnable);
                }

            }
        });
    }

    private void hideVideoEnableView(LayoutLeftmenuHeadBinding binding) {
        binding.includeMenuEnableVideo.getRoot().setVisibility(View.GONE);
    }


    private void initBottomMenuFirst(LayoutLeftmenuHeadBinding binding, User user) {
        binding.includeBottom1.ivMenu.setText(R.string.page_title_setting);
        binding.includeBottom1.ivImg.setBackgroundResource(R.drawable.icon_left_setting);
        binding.includeBottom2.ivMenu.setText(R.string.left_menu_help_text);
        binding.includeBottom2.ivImg.setBackgroundResource(R.drawable.icon_left_help);
    }

    private void showSetPriceView(LayoutLeftmenuHeadBinding binding) {
        binding.includeMenuSetprice.tvMenu.setText(MAPP.mapp.getString(R.string.set_price_tip));
        binding.includeMenuSetprice.ivImg.setBackgroundResource(R.drawable.icon_left_setprice);
        binding.includeMenuSetprice.getRoot().setVisibility(View.VISIBLE);
        binding.includeMenuSetprice.getRoot().setOnClickListener((v) -> {
            IntentUtils.toSetPriceActivity(binding.getRoot().getContext(), false);
        });
    }

    private void hideSetPriceView(LayoutLeftmenuHeadBinding binding) {
        binding.includeMenuSetprice.getRoot().setVisibility(View.GONE);
    }

}
