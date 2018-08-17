package com.gdlife.candypie.adapter.user;

import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.user.UserYueAttitudeActivity;
import com.gdlife.candypie.databinding.LayoutYueAttritudeItemBinding;
import com.heboot.bean.user.TagsChildBean;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class UserYueAttitudeAdapter extends BaseRecyclerViewAdapter {


    private List<String> ids;

    private int max ;

    private List<String> currentSelectedIds = new ArrayList<>();

    private WeakReference<UserYueAttitudeActivity> weakReference;

    public UserYueAttitudeAdapter(WeakReference<UserYueAttitudeActivity> weakReference, List<String> ids, List<TagsChildBean> mdata, int maxSelect) {
        data = mdata;
        this.ids = ids;
        this.max = maxSelect;
        this.currentSelectedIds.addAll(ids);
        this.weakReference = weakReference;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserYueAttitudeAdapter.ViewHolder(parent, R.layout.layout_yue_attritude_item);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<TagsChildBean, LayoutYueAttritudeItemBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final TagsChildBean s, int position) {

            binding.tvText.setText(s.getContent());

            if(currentSelectedIds != null){
                if(currentSelectedIds.indexOf(s.getId()) > -1){
                    binding.tvText.setChecked(true);
                    binding.tvText.setSelected(true);
                    binding.tvText.setBackgroundResource(R.drawable.bg_ellipse_yue_attitude_fouse);
                    binding.tvText.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(),R.color.theme_color));
                }else{
                    binding.tvText.setChecked(false);
                    binding.tvText.setSelected(false);
                    binding.tvText.setBackgroundResource(R.drawable.bg_ellipse_yue_attitude_normal);
                    binding.tvText.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(),R.color.color_898A9E));
                }
            }else{
                binding.tvText.setChecked(false);
                binding.tvText.setSelected(false);
                binding.tvText.setBackgroundResource(R.drawable.bg_ellipse_yue_attitude_normal);
                binding.tvText.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(),R.color.color_898A9E));
            }

            binding.tvText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        if(currentSelectedIds.size() == max){
                            //提示最多选择两个
                            weakReference.get().tipMaxSelect(max);
                        }else{
                            currentSelectedIds.add(s.getId());
//                            notifyDataSetChanged();
                            weakReference.get().updateui();
                        }
                    }else{
                        currentSelectedIds.remove(s.getId());
                        weakReference.get().updateui();
                    }
                }
            });

        }
    }

    public List<String> getCurrentSelectedIds() {
        return currentSelectedIds;
    }
}
