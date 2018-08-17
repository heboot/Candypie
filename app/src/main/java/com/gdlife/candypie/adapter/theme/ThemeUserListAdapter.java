package com.gdlife.candypie.adapter.theme;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.alibaba.fastjson.JSON;
import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.theme.ThemeUserListActivity;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.databinding.ItemThemeUserListBinding;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.entity.User;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;
import com.heboot.utils.LogUtil;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.lang.ref.WeakReference;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by heboot on 2018/2/1.
 */

public class ThemeUserListAdapter extends BaseRecyclerViewAdapter<User> {

    private int max;

    private Set<Integer> selectedIds = new HashSet<>();

    private CheckBox oldCheckBox;

    private QMUITipDialog tipDialog;

    private WeakReference<ThemeUserListActivity> weakReference;

    private boolean isVideo;

    public ThemeUserListAdapter(WeakReference<ThemeUserListActivity> weakReference, int max, boolean isVideo) {
        this.weakReference = weakReference;
        this.isVideo = isVideo;
//        if (mdata == null || mdata.size() == 0) {

//        }
        tipDialog = DialogUtils.getFailDialog(weakReference.get(), weakReference.get().getString(R.string.max_select) + max + weakReference.get().getString(R.string.unit_person), true);
        this.max = max;
    }

    public void addUser(User s) {
        data.add(s);
        notifyItemInserted(data.size());
    }

    public void setMax(int max) {
        this.max = max;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ThemeUserListAdapter.ViewHolder(parent, R.layout.item_theme_user_list);
    }

    public Set<Integer> getSelectedIds() {
        return selectedIds;
    }

    private class ViewHolder extends BaseRecyclerViewHolder<User, ItemThemeUserListBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final User s, int position) {

            if (data.size() == 1 && !StringUtils.isEmpty(data.get(0).getPwd()) && data.get(0).getPwd().equals("99899")) {
                ImageUtils.showImage(binding.qivHead, R.drawable.icon_mtp_server);
                binding.setUser(s);
                binding.tvInfo.setText(binding.getRoot().getContext().getString(R.string.app_zhuli_tip));
                binding.cbCheck.setVisibility(View.GONE);
            } else {
                if (isVideo) {
                    binding.cbCheck.setVisibility(View.GONE);
                    binding.ivTovideochat.setVisibility(View.VISIBLE);
                    binding.tvTovideocaht.setVisibility(View.VISIBLE);
                    binding.ivTovideochat.setOnClickListener((v) -> {
                        selectedIds.clear();
                        selectedIds.add(s.getId());
                        weakReference.get().doSelect(true);
                    });
                } else {
                    binding.cbCheck.setVisibility(View.VISIBLE);
                    binding.ivTovideochat.setVisibility(View.GONE);
                    binding.tvTovideocaht.setVisibility(View.GONE);
                }

                binding.setUser(s);

                UserService.getUserInfo(binding.tvInfo, s);

                binding.getRoot().setOnClickListener((v) -> {
                    IntentUtils.toHomepageActivity(v.getContext(), MValue.FROM_OTHER, s, MValue.HOMEPAG_FROM.CHOOSE_USER, null);
                });

                if (max > 1) {
                    binding.cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            // TODO: 2018/3/9 弹窗
                            if (isChecked) {
                                if (selectedIds.size() >= max) {
                                    tipDialog.show();
                                    return;
                                }
                                selectedIds.add(s.getId());
                                weakReference.get().checkSelect(selectedIds.size());
                            } else {
                                weakReference.get().checkSelect(selectedIds.size());
                                selectedIds.remove(s.getId());
                            }
                        }
                    });
                } else if (max == 1) {
                    binding.cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            if (isChecked) {
                                if (oldCheckBox != null) {
                                    selectedIds.remove(s.getId());
                                    oldCheckBox.setChecked(false);
                                    oldCheckBox.setSelected(false);
                                }
                                binding.cbCheck.setChecked(true);
                                binding.cbCheck.setSelected(true);
                                selectedIds.add(s.getId());
                                oldCheckBox = binding.cbCheck;
                                weakReference.get().checkSelect(selectedIds.size());

                            }


                        }
                    });
                }
            }

            if (selectedIds != null && selectedIds.size() > 0) {
                for (int id : selectedIds) {
                    if (id == s.getId().intValue()) {
                        binding.cbCheck.setChecked(true);
                    }
                }
            }


        }
    }

    public void doSelect(int uid) {
        if (selectedIds.size() >= max) {
//            tipDialog.show();
            return;
        }
        selectedIds.add(uid);
//        weakReference.get().checkSelect(selectedIds.size());
        notifyDataSetChanged();
    }


}
