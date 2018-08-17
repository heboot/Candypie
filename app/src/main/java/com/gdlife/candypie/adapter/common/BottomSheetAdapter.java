package com.gdlife.candypie.adapter.common;

import android.view.View;
import android.view.ViewGroup;

import com.gdlife.candypie.R;
import com.gdlife.candypie.databinding.ItemSheetBinding;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/2/26.
 */

public class BottomSheetAdapter extends BaseRecyclerViewAdapter {

    private Consumer<Integer> consumer;

    public BottomSheetAdapter(List<String> listBeans, Consumer<Integer> consumer) {
        data = listBeans;
        this.consumer = consumer;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BottomSheetAdapter.ViewHolder(parent, R.layout.item_sheet);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<String, ItemSheetBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final String s, int position) {
            binding.tvName.setText(s);
            if (position == data.size() - 1) {
                binding.line.setVisibility(View.GONE);
            } else {
                binding.line.setVisibility(View.VISIBLE);
            }
            binding.getRoot().setOnClickListener((v) -> {
                Observable.create(new ObservableOnSubscribe<Integer>() {

                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        emitter.onNext(position);
                    }
                }).subscribe(consumer);
            });
        }
    }
}
