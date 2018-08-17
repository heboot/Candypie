package com.gdlife.candypie.activitys.video;

import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;

import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.ActivityVideoFrameBinding;
import com.gdlife.candypie.utils.FrameUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.SDCardUtils;
import com.heboot.utils.LogUtil;
import com.heboot.utils.ValueUtils;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heboot on 2018/1/27.
 */

public class VideoFrameActivity extends BaseActivity<ActivityVideoFrameBinding> {


    private String videoPath;//视频路径
//    private String videoPath2 = "/sdcard/testptho";//视频路径

    private List<String> frames = new ArrayList();

    @Override
    public void initUI() {

        binding.rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


    }

    @Override
    public void initData() {

        videoPath = getIntent().getStringExtra(MKey.PATH);

        File file = new File(videoPath);
        LogUtil.e(TAG, file.exists() + "");
        FrameUtils frameUtils = new FrameUtils();
        try {
            frames = frameUtils.getFrams(videoPath, SDCardUtils.getRootPathPrivateFrame());
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (frames != null && frames.size() > 0) {
//                UCrop.of(Uri.parse(frames.get(0)), Uri.parse(videoPath2 + "/ccc.jpg")).startByFrame(this);

            ValueUtils.currentVideoPath = videoPath;

            UCrop.Options options = new UCrop.Options();

            options.setShowCropGrid(false);
            options.setFreeStyleCropEnabled(false);
            options.setHideBottomControls(true);
            options.setToolbarTitle("裁剪");
            options.setToolbarColor(Color.TRANSPARENT);
            options.setToolbarWidgetColor(Color.WHITE);


            options.getOptionBundle().putSerializable("frames", (Serializable) frames);

            UCrop.of(Uri.fromFile(new File(frames.get(0))), ImageUtils.getCropPhotoUri())
                    .withOptions(options)
                    .withAspectRatio(1, 1)
                    .startByFrame(this);
        }


    }

    @Override
    public void initListener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_frame;
    }
}
