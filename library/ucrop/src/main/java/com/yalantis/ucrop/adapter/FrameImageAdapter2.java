package com.yalantis.ucrop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.heboot.utils.FrameUtils;
import com.yalantis.ucrop.R;

import java.util.List;

/**
 * Created by heboot on 2018/1/29.
 */

public class FrameImageAdapter2 extends RecyclerView.Adapter {


    private int itemWith = 0;

    private List<String> data;

    private Context context;

    public FrameImageAdapter2(Context context, List<String> datas) {
        this.data = datas;
        this.context = context;
        this.itemWith = FrameUtils.getFrameItemWidth(context, datas.size());
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_frame_image, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Glide.with(context).load(data.get(position)).into(((ViewHolder) holder).imageView);
//        Picasso.with(context).load(data.get(position)).into(((ViewHolder) holder).imageView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;


        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_image);
        }
    }


//    private int itemWith = 0;
//
//    public FrameImageAdapter(Context context, List<String> datas) {
//        this.data = datas;
//        this.context = context;
//        this.itemWith = FrameUtils.getFrameItemWidth(context, datas.size());
//    }
//
//
//    @Override
//    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new FrameImageAdapter.ViewHolder(parent, R.layout.item_frame_image);
//    }
//
//    private class ViewHolder extends BaseRecyclerViewHolder<String, ItemFrameImageBinding> {
//
//        ViewHolder(ViewGroup parent, int layout) {
//            super(parent, layout);
//        }
//
//        @Override
//        public void onBindViewHolder(final String s, int position) {
////            ImageUtils.showImage(s, binding.ivImage);
////            Glide.with(binding.ivImage.getContext()).load(s).into(binding.ivImage);
//
//            ViewUtils.setViewWidth(binding.ivImage, itemWith);
//
//            binding.ivImage.setImageResource(R.drawable.ucrop_ic_angle);
//
////            Picasso.with(context).load(s).into(binding.ivImage);
//
//
////            Glide.with(context).load("http://img0.imgtn.bdimg.com/it/u=2260926939,1550208231&fm=27&gp=0.jpg");
//
//            //.into(binding.ivImage)
//
////            Glide.with(binding.ivImage.getContext())
////                    .load(s)
////                    .transition(DrawableTransitionOptions.withCrossFade())
//////                    .apply(options)
////                    .into(binding.ivImage);
//        }
//    }


}
