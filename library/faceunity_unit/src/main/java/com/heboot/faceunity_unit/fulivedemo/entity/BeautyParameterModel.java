package com.heboot.faceunity_unit.fulivedemo.entity;


import android.content.Context;

import com.faceunity.entity.Filter;
import com.heboot.faceunity_unit.R;
import com.heboot.utils.PreferencesUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 美颜参数SharedPreferences记录,目前仅以保存数据，可改造为以SharedPreferences保存数据
 * Created by tujh on 2018/3/7.
 */

public abstract class BeautyParameterModel {

    public static final String TAG = BeautyParameterModel.class.getSimpleName();


    public static boolean isHeightPerformance = false;

    public static final String sStrFilterLevel = "FilterLevel_";
    public static Map<String, Float> sFilterLevel = new HashMap<>();


//    public static Filter sFilterName = FilterEnum.nature_beauty.filter();

    public static Filter sFilterName = FilterEnum.nature_beauty.filter();

    public static float sSkinDetect = 1.0f;//精准磨皮
    public static float sHeavyBlur = 0.0f;//美肤类型
    public static float sHeavyBlurLevel = 0.7f;//磨皮
    public static float sBlurLevel = 0.7f;//磨皮
    public static float sColorLevel = 0.5f;//美白
    public static float sRedLevel = 0.5f;//红润
    public static float sEyeBright = 0.0f;//亮眼
    public static float sToothWhiten = 0.0f;//美牙

    public static float sFaceShape = 4.0f;//脸型
    public static float sFaceShapeLevel = 1.0f;//程度
    public static float sEyeEnlarging = 0.4f;//大眼
    public static float sEyeEnlargingOld = 0.4f;//大眼
    public static float sCheekThinning = 0.4f;//瘦脸
    public static float sCheekThinningOld = 0.4f;//瘦脸
    public static float sIntensityChin = 0.3f;//下巴
    public static float sIntensityForehead = 0.3f;//额头
    public static float sIntensityNose = 0.5f;//瘦鼻
    public static float sIntensityMouth = 0.4f;//嘴形


//    public static boolean isOpen(int checkId) {
//        if (checkId == R.id.beauty_box_skin_detect) {
//            return !isHeightPerformance && sSkinDetect == 1;
//        } else if (checkId == R.id.beauty_box_heavy_blur) {
//            return (isHeightPerformance || sHeavyBlur == 1) ? sHeavyBlurLevel > 0 : sBlurLevel > 0;
//        } else if (checkId == R.id.beauty_box_blur_level) {
//            return sHeavyBlurLevel > 0;
//        } else if (checkId == R.id.beauty_box_color_level) {
//            return sColorLevel > 0;
//        } else if (checkId == R.id.beauty_box_red_level) {
//            return sRedLevel > 0;
//        } else if (checkId == R.id.beauty_box_eye_bright) {
//            return !isHeightPerformance && sEyeBright > 0;
//        } else if (checkId == R.id.beauty_box_tooth_whiten) {
//            return !isHeightPerformance && sToothWhiten != 0;
//        } else if (checkId == R.id.beauty_box_face_shape) {
//            return (!isHeightPerformance || sFaceShape != 4) && sFaceShape != 3;
//        } else if (checkId == R.id.beauty_box_eye_enlarge) {
//            if (sFaceShape == 4)
//                return sEyeEnlarging > 0;
//            else
//                return sEyeEnlargingOld > 0;
//        } else if (checkId == R.id.beauty_box_cheek_thinning) {
//            if (sFaceShape == 4)
//                return sCheekThinning > 0;
//            else
//                return sCheekThinningOld > 0;
//        } else if (checkId == R.id.beauty_box_intensity_chin) {
//            return !isHeightPerformance && sIntensityChin != 0.5;
//        } else if (checkId == R.id.beauty_box_intensity_forehead) {
//            return !isHeightPerformance && sIntensityForehead != 0.5;
//        } else if (checkId == R.id.beauty_box_intensity_nose) {
//            return !isHeightPerformance && sIntensityNose > 0;
//        } else if (checkId == R.id.beauty_box_intensity_mouth) {
//            return !isHeightPerformance && sIntensityMouth != 0.5;
//        } else {
//            return true;
//        }
//    }


    public static boolean isOpen(Context context, int checkId) {
        if (checkId == R.id.beauty_box_skin_detect) {
            if (PreferencesUtils.getFloat(context, BeautyKey.sSkinDetect.toString(), 0f) != 0f) {
                sSkinDetect = PreferencesUtils.getFloat(context, BeautyKey.sSkinDetect.toString(), 0f);
            }
//            return !isHeightPerformance && sSkinDetect == 1;
            return sSkinDetect == 1;
        } else if (checkId == R.id.beauty_box_heavy_blur) {
            if (PreferencesUtils.getFloat(context, BeautyKey.sHeavyBlur.toString(), 0f) != 0f) {
                sHeavyBlur = PreferencesUtils.getFloat(context, BeautyKey.sHeavyBlur.toString(), 0f);
            }
            if (PreferencesUtils.getFloat(context, BeautyKey.sHeavyBlurLevel.toString(), 0f) != 0f) {
                sHeavyBlurLevel = PreferencesUtils.getFloat(context, BeautyKey.sHeavyBlurLevel.toString(), 0f);
            }
            if (PreferencesUtils.getFloat(context, BeautyKey.sBlurLevel.toString(), 0f) != 0f) {
                sBlurLevel = PreferencesUtils.getFloat(context, BeautyKey.sBlurLevel.toString(), 0f);
            }
            return (isHeightPerformance || sHeavyBlur == 1) ? sHeavyBlurLevel > 0 : sBlurLevel > 0;
        } else if (checkId == R.id.beauty_box_blur_level) {
            if (PreferencesUtils.getFloat(context, BeautyKey.sHeavyBlurLevel.toString(), 0f) != 0f) {
                sHeavyBlurLevel = PreferencesUtils.getFloat(context, BeautyKey.sHeavyBlurLevel.toString(), 0f);
            }
            return sHeavyBlurLevel > 0;
        } else if (checkId == R.id.beauty_box_color_level) {
            if (PreferencesUtils.getFloat(context, BeautyKey.sColorLevel.toString(), 0f) != 0f) {
                sColorLevel = PreferencesUtils.getFloat(context, BeautyKey.sColorLevel.toString(), 0f);
            }
            return sColorLevel > 0;
        } else if (checkId == R.id.beauty_box_red_level) {
            if (PreferencesUtils.getFloat(context, BeautyKey.sRedLevel.toString(), 0f) != 0f) {
                sRedLevel = PreferencesUtils.getFloat(context, BeautyKey.sRedLevel.toString(), 0f);
            }
            return sRedLevel > 0;
        } else if (checkId == R.id.beauty_box_eye_bright) {
            if (PreferencesUtils.getFloat(context, BeautyKey.sEyeBright.toString(), 0f) != 0f) {
                sEyeBright = PreferencesUtils.getFloat(context, BeautyKey.sEyeBright.toString(), 0f);
            }
            return !isHeightPerformance && sEyeBright > 0;
        } else if (checkId == R.id.beauty_box_tooth_whiten) {
            if (PreferencesUtils.getFloat(context, BeautyKey.sToothWhiten.toString(), 0f) != 0f) {
                sToothWhiten = PreferencesUtils.getFloat(context, BeautyKey.sToothWhiten.toString(), 0f);
            }
            return !isHeightPerformance && sToothWhiten != 0;
        } else if (checkId == R.id.beauty_box_face_shape) {
            if (PreferencesUtils.getFloat(context, BeautyKey.sFaceShape.toString(), 0f) != 0f) {
                sFaceShape = PreferencesUtils.getFloat(context, BeautyKey.sFaceShape.toString(), 0f);
            }
            return (!isHeightPerformance || sFaceShape != 4) && sFaceShape != 3;
        } else if (checkId == R.id.beauty_box_eye_enlarge) {
            if (PreferencesUtils.getFloat(context, BeautyKey.sFaceShape.toString(), 0f) != 0f) {
                sFaceShape = PreferencesUtils.getFloat(context, BeautyKey.sFaceShape.toString(), 0f);
            }
            if (sFaceShape == 4) {
                if (PreferencesUtils.getFloat(context, BeautyKey.sEyeEnlarging.toString(), 0f) != 0f) {
                    sEyeEnlarging = PreferencesUtils.getFloat(context, BeautyKey.sEyeEnlarging.toString(), 0f);
                }
                return sEyeEnlarging > 0;
            } else {
                if (PreferencesUtils.getFloat(context, BeautyKey.sEyeEnlargingOld.toString(), 0f) != 0f) {
                    sEyeEnlargingOld = PreferencesUtils.getFloat(context, BeautyKey.sEyeEnlargingOld.toString(), 0f);
                }
                return sEyeEnlargingOld > 0;
            }
        } else if (checkId == R.id.beauty_box_cheek_thinning) {
            if (PreferencesUtils.getFloat(context, BeautyKey.sFaceShape.toString(), 0f) != 0f) {
                sFaceShape = PreferencesUtils.getFloat(context, BeautyKey.sFaceShape.toString(), 0f);
            }
            if (sFaceShape == 4) {
                if (PreferencesUtils.getFloat(context, BeautyKey.sCheekThinning.toString(), 0f) != 0f) {
                    sCheekThinning = PreferencesUtils.getFloat(context, BeautyKey.sCheekThinning.toString(), 0f);
                }
                return sCheekThinning > 0;
            } else {
                if (PreferencesUtils.getFloat(context, BeautyKey.sCheekThinningOld.toString(), 0f) != 0f) {
                    sCheekThinningOld = PreferencesUtils.getFloat(context, BeautyKey.sCheekThinningOld.toString(), 0f);
                }
                return sCheekThinningOld > 0;
            }
        } else if (checkId == R.id.beauty_box_intensity_chin) {
            if (PreferencesUtils.getFloat(context, BeautyKey.sIntensityChin.toString(), 0f) != 0f) {
                sIntensityChin = PreferencesUtils.getFloat(context, BeautyKey.sIntensityChin.toString(), 0f);
            }
            return !isHeightPerformance && sIntensityChin != 0.5;
        } else if (checkId == R.id.beauty_box_intensity_forehead) {
            if (PreferencesUtils.getFloat(context, BeautyKey.sIntensityForehead.toString(), 0f) != 0f) {
                sIntensityForehead = PreferencesUtils.getFloat(context, BeautyKey.sIntensityForehead.toString(), 0f);
            }
            return !isHeightPerformance && sIntensityForehead != 0.5;
        } else if (checkId == R.id.beauty_box_intensity_nose) {
            if (PreferencesUtils.getFloat(context, BeautyKey.sIntensityNose.toString(), 0f) != 0f) {
                sIntensityNose = PreferencesUtils.getFloat(context, BeautyKey.sIntensityNose.toString(), 0f);
            }
            return !isHeightPerformance && sIntensityNose > 0;
        } else if (checkId == R.id.beauty_box_intensity_mouth) {
            if (PreferencesUtils.getFloat(context, BeautyKey.sIntensityMouth.toString(), 0f) != 0f) {
                sIntensityMouth = PreferencesUtils.getFloat(context, BeautyKey.sIntensityMouth.toString(), 0f);
            }
            return !isHeightPerformance && sIntensityMouth != 0.5;
        } else {
            return true;
        }
    }


    //自带的getValue方法
//    public static float getValue(int checkId) {
//        if (checkId == R.id.beauty_box_skin_detect) {
//            return isHeightPerformance ? 0 : sSkinDetect;
//        } else if (checkId == R.id.beauty_box_heavy_blur) {
//            return (isHeightPerformance || sHeavyBlur == 1) ? sHeavyBlurLevel : sBlurLevel;
//        } else if (checkId == R.id.beauty_box_blur_level) {
//            return sHeavyBlurLevel;
//        } else if (checkId == R.id.beauty_box_color_level) {
//            return sColorLevel;
//        } else if (checkId == R.id.beauty_box_red_level) {
//            return sRedLevel;
//        } else if (checkId == R.id.beauty_box_eye_bright) {
//            return !isHeightPerformance ? sEyeBright : 0;
//        } else if (checkId == R.id.beauty_box_tooth_whiten) {
//            return !isHeightPerformance ? sToothWhiten : 0;
//        } else if (checkId == R.id.beauty_box_face_shape) {
//            return isHeightPerformance && sFaceShape == 4 ? 3 : sFaceShape;
//        } else if (checkId == R.id.beauty_box_eye_enlarge) {
//            if (!isHeightPerformance && sFaceShape == 4)
//                return sEyeEnlarging;
//            else
//                return sEyeEnlargingOld;
//        } else if (checkId == R.id.beauty_box_cheek_thinning) {
//            if (!isHeightPerformance && sFaceShape == 4)
//                return sCheekThinning;
//            else
//                return sCheekThinningOld;
//        } else if (checkId == R.id.beauty_box_intensity_chin) {
//            return !isHeightPerformance ? sIntensityChin : 0.5f;
//        } else if (checkId == R.id.beauty_box_intensity_forehead) {
//            return !isHeightPerformance ? sIntensityForehead : 0.5f;
//        } else if (checkId == R.id.beauty_box_intensity_nose) {
//            return !isHeightPerformance ? sIntensityNose : 0;
//        } else if (checkId == R.id.beauty_box_intensity_mouth) {
//            return !isHeightPerformance ? sIntensityMouth : 0.5f;
//        } else {
//            return 0;
//        }
//    }

    public static float getValue(Context context, int checkId) {
        if (checkId == R.id.beauty_box_skin_detect) {
            if (PreferencesUtils.getFloat(context, BeautyKey.sSkinDetect.toString(), 0f) != 0f) {
                sSkinDetect = PreferencesUtils.getFloat(context, BeautyKey.sSkinDetect.toString(), 0f);
            }
            return isHeightPerformance ? 0 : sSkinDetect;
        } else if (checkId == R.id.beauty_box_heavy_blur) {
            if (PreferencesUtils.getFloat(context, BeautyKey.sHeavyBlurLevel.toString(), 0f) != 0f) {
                sHeavyBlurLevel = PreferencesUtils.getFloat(context, BeautyKey.sHeavyBlurLevel.toString(), 0f);
            }
            if (PreferencesUtils.getFloat(context, BeautyKey.sBlurLevel.toString(), 0f) != 0f) {
                sBlurLevel = PreferencesUtils.getFloat(context, BeautyKey.sBlurLevel.toString(), 0f);
            }
            return (isHeightPerformance || sHeavyBlur == 1) ? sHeavyBlurLevel : sBlurLevel;
        } else if (checkId == R.id.beauty_box_blur_level) {
            if (PreferencesUtils.getFloat(context, BeautyKey.sHeavyBlurLevel.toString(), 0f) != 0f) {
                sHeavyBlurLevel = PreferencesUtils.getFloat(context, BeautyKey.sHeavyBlurLevel.toString(), 0f);
            }
            return sHeavyBlurLevel;
        } else if (checkId == R.id.beauty_box_color_level) {
            if (PreferencesUtils.getFloat(context, BeautyKey.sColorLevel.toString(), 0f) != 0f) {
                sColorLevel = PreferencesUtils.getFloat(context, BeautyKey.sColorLevel.toString(), 0f);
            }
            return sColorLevel;
        } else if (checkId == R.id.beauty_box_red_level) {
            if (PreferencesUtils.getFloat(context, BeautyKey.sRedLevel.toString(), 0f) != 0f) {
                sRedLevel = PreferencesUtils.getFloat(context, BeautyKey.sRedLevel.toString(), 0f);
            }
            return sRedLevel;
        } else if (checkId == R.id.beauty_box_eye_bright) {
            if (PreferencesUtils.getFloat(context, BeautyKey.sEyeBright.toString(), 0f) != 0f) {
                sEyeBright = PreferencesUtils.getFloat(context, BeautyKey.sEyeBright.toString(), 0f);
            }
            return !isHeightPerformance ? sEyeBright : 0;
        } else if (checkId == R.id.beauty_box_tooth_whiten) {
            if (PreferencesUtils.getFloat(context, BeautyKey.sToothWhiten.toString(), 0f) != 0f) {
                sToothWhiten = PreferencesUtils.getFloat(context, BeautyKey.sToothWhiten.toString(), 0f);
            }
            return !isHeightPerformance ? sToothWhiten : 0;
        } else if (checkId == R.id.beauty_box_face_shape) {
            if (PreferencesUtils.getFloat(context, BeautyKey.sFaceShape.toString(), 0f) != 0f) {
                sFaceShape = PreferencesUtils.getFloat(context, BeautyKey.sFaceShape.toString(), 0f);
            }
            return isHeightPerformance && sFaceShape == 4 ? 3 : sFaceShape;
        } else if (checkId == R.id.beauty_box_eye_enlarge) {
            if (!isHeightPerformance && sFaceShape == 4) {
                if (PreferencesUtils.getFloat(context, BeautyKey.sEyeEnlarging.toString(), 0f) != 0f) {
                    sEyeEnlarging = PreferencesUtils.getFloat(context, BeautyKey.sEyeEnlarging.toString(), 0f);
                }
                return sEyeEnlarging;
            } else {
                if (PreferencesUtils.getFloat(context, BeautyKey.sEyeEnlargingOld.toString(), 0f) != 0f) {
                    sEyeEnlargingOld = PreferencesUtils.getFloat(context, BeautyKey.sEyeEnlargingOld.toString(), 0f);
                }
                return sEyeEnlargingOld;
            }
        } else if (checkId == R.id.beauty_box_cheek_thinning) {
            if (!isHeightPerformance && sFaceShape == 4) {
                if (PreferencesUtils.getFloat(context, BeautyKey.sCheekThinning.toString(), 0f) != 0f) {
                    sCheekThinning = PreferencesUtils.getFloat(context, BeautyKey.sCheekThinning.toString(), 0f);
                }
                return sCheekThinning;
            } else {
                if (PreferencesUtils.getFloat(context, BeautyKey.sCheekThinningOld.toString(), 0f) != 0f) {
                    sCheekThinningOld = PreferencesUtils.getFloat(context, BeautyKey.sCheekThinningOld.toString(), 0f);
                }
                return sCheekThinningOld;
            }
        } else if (checkId == R.id.beauty_box_intensity_chin) {
            if (PreferencesUtils.getFloat(context, BeautyKey.sIntensityChin.toString(), 0f) != 0f) {
                sIntensityChin = PreferencesUtils.getFloat(context, BeautyKey.sIntensityChin.toString(), 0f);
            }
            return !isHeightPerformance ? sIntensityChin : 0.5f;
        } else if (checkId == R.id.beauty_box_intensity_forehead) {
            if (PreferencesUtils.getFloat(context, BeautyKey.sIntensityForehead.toString(), 0f) != 0) {
                sIntensityForehead = PreferencesUtils.getFloat(context, BeautyKey.sIntensityForehead.toString(), 0f);
            }
            return !isHeightPerformance ? sIntensityForehead : 0.5f;
        } else if (checkId == R.id.beauty_box_intensity_nose) {
            if (PreferencesUtils.getFloat(context, BeautyKey.sIntensityNose.toString(), 0f) != 0) {
                sIntensityNose = PreferencesUtils.getFloat(context, BeautyKey.sIntensityNose.toString(), 0f);
            }
            return !isHeightPerformance ? sIntensityNose : 0;
        } else if (checkId == R.id.beauty_box_intensity_mouth) {
            if (PreferencesUtils.getFloat(context, BeautyKey.sIntensityMouth.toString(), 0f) != 0) {
                sIntensityMouth = PreferencesUtils.getFloat(context, BeautyKey.sIntensityMouth.toString(), 0f);
            }
            return !isHeightPerformance ? sIntensityMouth : 0.5f;
        } else {
            return 0;
        }
    }

    public static void setValue(Context context, int checkId, float value) {
        if (checkId == R.id.beauty_box_skin_detect) {
            sSkinDetect = value;
            PreferencesUtils.putFloat(context, BeautyKey.sSkinDetect.toString(), value);
        } else if (checkId == R.id.beauty_box_heavy_blur) {
            if (isHeightPerformance || sHeavyBlur == 1) {
                sHeavyBlurLevel = value;
                PreferencesUtils.putFloat(context, BeautyKey.sHeavyBlurLevel.toString(), value);
            } else {
                sBlurLevel = value;
                PreferencesUtils.putFloat(context, BeautyKey.sBlurLevel.toString(), value);
            }

        } else if (checkId == R.id.beauty_box_blur_level) {
            sHeavyBlurLevel = value;
            PreferencesUtils.putFloat(context, BeautyKey.sHeavyBlurLevel.toString(), value);
        } else if (checkId == R.id.beauty_box_color_level) {
            sColorLevel = value;
            PreferencesUtils.putFloat(context, BeautyKey.sColorLevel.toString(), value);
        } else if (checkId == R.id.beauty_box_red_level) {
            sRedLevel = value;
            PreferencesUtils.putFloat(context, BeautyKey.sRedLevel.toString(), value);
        } else if (checkId == R.id.beauty_box_eye_bright) {
            sEyeBright = value;
            PreferencesUtils.putFloat(context, BeautyKey.sEyeBright.toString(), value);
        } else if (checkId == R.id.beauty_box_tooth_whiten) {
            sToothWhiten = value;
            PreferencesUtils.putFloat(context, BeautyKey.sToothWhiten.toString(), value);
        } else if (checkId == R.id.beauty_box_face_shape) {
            sFaceShape = value;
            PreferencesUtils.putFloat(context, BeautyKey.sFaceShape.toString(), value);
        } else if (checkId == R.id.beauty_box_eye_enlarge) {
            if (!isHeightPerformance && sFaceShape == 4) {
                sEyeEnlarging = value;
                PreferencesUtils.putFloat(context, BeautyKey.sEyeEnlarging.toString(), value);
            } else {
                sEyeEnlargingOld = value;
                PreferencesUtils.putFloat(context, BeautyKey.sEyeEnlargingOld.toString(), value);
            }

        } else if (checkId == R.id.beauty_box_cheek_thinning) {
            if (!isHeightPerformance && sFaceShape == 4) {
                sCheekThinning = value;
                PreferencesUtils.putFloat(context, BeautyKey.sCheekThinning.toString(), value);
            } else {
                sCheekThinningOld = value;
                PreferencesUtils.putFloat(context, BeautyKey.sCheekThinningOld.toString(), value);
            }
        } else if (checkId == R.id.beauty_box_intensity_chin) {
            sIntensityChin = value;
            PreferencesUtils.putFloat(context, BeautyKey.sIntensityChin.toString(), value);
        } else if (checkId == R.id.beauty_box_intensity_forehead) {
            sIntensityForehead = value;
            PreferencesUtils.putFloat(context, BeautyKey.sIntensityForehead.toString(), value);
        } else if (checkId == R.id.beauty_box_intensity_nose) {
            sIntensityNose = value;
            PreferencesUtils.putFloat(context, BeautyKey.sIntensityNose.toString(), value);
        } else if (checkId == R.id.beauty_box_intensity_mouth) {
            sIntensityMouth = value;
            PreferencesUtils.putFloat(context, BeautyKey.sIntensityMouth.toString(), value);
        }
    }


    public static void setHeavyBlur(boolean isHeavy) {
        sHeavyBlur = isHeavy ? 1.0f : 0;
    }
}
