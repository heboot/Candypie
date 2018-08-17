package com.heboot.faceunity_unit.fulivedemo.entity;

import com.faceunity.entity.Effect;
import com.heboot.faceunity_unit.R;

import java.util.ArrayList;

/**
 * Created by tujh on 2018/1/30.
 */

public enum EffectEnum {

    EffectNone("none", R.drawable.ic_delete_all, "none", 1, Effect.EFFECT_TYPE_NONE, ""),

    Effect_fengya_ztt_fu("fengya_ztt_fu", R.drawable.ic_delete_all, "normal/fengya_ztt_fu.bundle", 4, Effect.EFFECT_TYPE_NORMAL, ""),
    Effect_hudie_lm_fu("hudie_lm_fu", R.drawable.ic_delete_all, "normal/hudie_lm_fu.bundle", 4, Effect.EFFECT_TYPE_NORMAL, ""),
    Effect_touhua_ztt_fu("touhua_ztt_fu", R.drawable.ic_delete_all, "normal/touhua_ztt_fu.bundle", 4, Effect.EFFECT_TYPE_NORMAL, ""),
    Effect_juanhuzi_lm_fu("juanhuzi_lm_fu", R.drawable.ic_delete_all, "normal/juanhuzi_lm_fu.bundle", 4, Effect.EFFECT_TYPE_NORMAL, ""),
    Effect_mask_hat("mask_hat", R.drawable.ic_delete_all, "normal/mask_hat.bundle", 4, Effect.EFFECT_TYPE_NORMAL, ""),
    Effect_yazui("yazui", R.drawable.ic_delete_all, "normal/yazui.bundle", 4, Effect.EFFECT_TYPE_NORMAL, ""),
    Effect_yuguan("yuguan", R.drawable.ic_delete_all, "normal/yuguan.bundle", 4, Effect.EFFECT_TYPE_NORMAL, ""),

    Effect_afd("afd", R.drawable.ic_delete_all, "ar/afd.bundle", 4, Effect.EFFECT_TYPE_AR, ""),
    Effect_baozi("baozi", R.drawable.ic_delete_all, "ar/baozi.bundle", 4, Effect.EFFECT_TYPE_AR, ""),
    Effect_tiger("tiger", R.drawable.ic_delete_all, "ar/tiger.bundle", 4, Effect.EFFECT_TYPE_AR, ""),
    Effect_xiongmao("xiongmao", R.drawable.ic_delete_all, "ar/xiongmao.bundle", 4, Effect.EFFECT_TYPE_AR, ""),
    Effect_armesh("armesh", R.drawable.ic_delete_all, "ar/armesh.bundle", 4, Effect.EFFECT_TYPE_AR, ""),
    Effect_armesh_ex("armesh_ex", R.drawable.ic_delete_all, "ar/armesh_ex.bundle", 4, Effect.EFFECT_TYPE_AR, "AR面具高精度版"),

    Effect_mask_liudehua("mask_liudehua", R.drawable.ic_delete_all, "change/mask_liudehua.bundle", 4, Effect.EFFECT_TYPE_FACE_CHANGE, ""),
    Effect_mask_linzhiling("mask_linzhiling", R.drawable.ic_delete_all, "change/mask_linzhiling.bundle", 4, Effect.EFFECT_TYPE_FACE_CHANGE, ""),
    Effect_mask_luhan("mask_luhan", R.drawable.ic_delete_all, "change/mask_luhan.bundle", 4, Effect.EFFECT_TYPE_FACE_CHANGE, ""),
    Effect_mask_guocaijie("mask_guocaijie", R.drawable.ic_delete_all, "change/mask_guocaijie.bundle", 4, Effect.EFFECT_TYPE_FACE_CHANGE, ""),
    Effect_mask_huangxiaoming("mask_huangxiaoming", R.drawable.ic_delete_all, "change/mask_huangxiaoming.bundle", 4, Effect.EFFECT_TYPE_FACE_CHANGE, ""),
    Effect_mask_matianyu("mask_matianyu", R.drawable.ic_delete_all, "change/mask_matianyu.bundle", 4, Effect.EFFECT_TYPE_FACE_CHANGE, ""),
    Effect_mask_tongliya("mask_tongliya", R.drawable.ic_delete_all, "change/mask_tongliya.bundle", 4, Effect.EFFECT_TYPE_FACE_CHANGE, ""),

    Effect_future_warrior("future_warrior", R.drawable.ic_delete_all, "expression/future_warrior.bundle", 4, Effect.EFFECT_TYPE_EXPRESSION, "张嘴试试"),
    Effect_jet_mask("jet_mask", R.drawable.ic_delete_all, "expression/jet_mask.bundle", 4, Effect.EFFECT_TYPE_EXPRESSION, "鼓腮帮子"),
    Effect_sdx2("sdx2", R.drawable.ic_delete_all, "expression/sdx2.bundle", 1, Effect.EFFECT_TYPE_EXPRESSION, "皱眉试试"),
    Effect_luhantongkuan_ztt_fu("luhantongkuan_ztt_fu", R.drawable.ic_delete_all, "expression/luhantongkuan_ztt_fu.bundle", 4, Effect.EFFECT_TYPE_EXPRESSION, "眨一眨眼"),
    Effect_qingqing_ztt_fu("qingqing_ztt_fu", R.drawable.ic_delete_all, "expression/qingqing_ztt_fu.bundle", 4, Effect.EFFECT_TYPE_EXPRESSION, "嘟嘴试试"),
    Effect_xiaobianzi_zh_fu("xiaobianzi_zh_fu.bundle", R.drawable.ic_delete_all, "expression/xiaobianzi_zh_fu.bundle", 4, Effect.EFFECT_TYPE_EXPRESSION, "微笑触发"),
    Effect_xiaoxueshen_ztt_fu("xiaoxueshen_ztt_fu", R.drawable.ic_delete_all, "expression/xiaoxueshen_ztt_fu.bundle", 4, Effect.EFFECT_TYPE_EXPRESSION, "吹气触发"),

    Effect_chiji_lm_fu("chiji_lm_fu", R.drawable.ic_delete_all, "background/chiji_lm_fu.bundle", 1, Effect.EFFECT_TYPE_BACKGROUND, ""),
    Effect_ice_lm_fu("ice_lm_fu", R.drawable.ic_delete_all, "background/ice_lm_fu.bundle", 1, Effect.EFFECT_TYPE_BACKGROUND, ""),
    Effect_jingongmen_zh_fu("jingongmen_zh_fu", R.drawable.ic_delete_all, "background/jingongmen_zh_fu.bundle", 1, Effect.EFFECT_TYPE_BACKGROUND, ""),
    Effect_gufeng_zh_fu("gufeng_zh_fu", R.drawable.ic_delete_all, "background/gufeng_zh_fu.bundle", 1, Effect.EFFECT_TYPE_BACKGROUND, ""),
    Effect_men_ztt_fu("men_ztt_fu", R.drawable.ic_delete_all, "background/men_ztt_fu.bundle", 1, Effect.EFFECT_TYPE_BACKGROUND, ""),
    Effect_hez_ztt_fu("hez_ztt_fu", R.drawable.ic_delete_all, "background/hez_ztt_fu.bundle", 1, Effect.EFFECT_TYPE_BACKGROUND, "转头触发"),

    Effect_fu_lm_koreaheart("fu_lm_koreaheart", R.drawable.ic_delete_all, "gesture/fu_lm_koreaheart.bundle", 4, Effect.EFFECT_TYPE_GESTURE, "单手手指比心"),
    Effect_fu_zh_baoquan("fu_zh_baoquan", R.drawable.ic_delete_all, "gesture/fu_zh_baoquan.bundle", 4, Effect.EFFECT_TYPE_GESTURE, "双手抱拳"),
    Effect_fu_zh_hezxiong("fu_zh_hezxiong", R.drawable.ic_delete_all, "gesture/fu_zh_hezxiong.bundle", 4, Effect.EFFECT_TYPE_GESTURE, "双手合十"),
    Effect_fu_ztt_live520("fu_ztt_live520", R.drawable.ic_delete_all, "gesture/fu_ztt_live520.bundle", 4, Effect.EFFECT_TYPE_GESTURE, "双手比心"),
    Effect_ssd_thread_thumb("ssd_thread_thumb", R.drawable.ic_delete_all, "gesture/ssd_thread_thumb.bundle", 4, Effect.EFFECT_TYPE_GESTURE, "竖个拇指"),
    Effect_ssd_thread_six("ssd_thread_six", R.drawable.ic_delete_all, "gesture/ssd_thread_six.bundle", 4, Effect.EFFECT_TYPE_GESTURE, "比个六"),
    Effect_ssd_thread_cute("ssd_thread_cute", R.drawable.ic_delete_all, "gesture/ssd_thread_cute.bundle", 4, Effect.EFFECT_TYPE_GESTURE, "双拳靠近脸颊卖萌"),

//    Effect_portraitLighting_effect_0("PortraitLighting_effect_0", R.drawable.portrait_lighting_effect_0, "portrait_light/PortraitLighting_effect_0.bundle", 1, Effect.EFFECT_TYPE_PORTRAIT_LIGHT, ""),
//    Effect_portraitLighting_effect_1("PortraitLighting_effect_1", R.drawable.portrait_lighting_effect_1, "portrait_light/PortraitLighting_effect_1.bundle", 1, Effect.EFFECT_TYPE_PORTRAIT_LIGHT, ""),
//    Effect_portraitLighting_effect_2("PortraitLighting_effect_2", R.drawable.portrait_lighting_effect_2, "portrait_light/PortraitLighting_effect_2.bundle", 1, Effect.EFFECT_TYPE_PORTRAIT_LIGHT, ""),
//    Effect_portraitLighting_effect_3("PortraitLighting_effect_3", R.drawable.portrait_lighting_effect_3, "portrait_light/PortraitLighting_effect_3.bundle", 1, Effect.EFFECT_TYPE_PORTRAIT_LIGHT, ""),
//    Effect_portraitLighting_X_rim("PortraitLighting_X_rim", R.drawable.portrait_lighting_x_rim, "portrait_light/PortraitLighting_X_rim.bundle", 1, Effect.EFFECT_TYPE_PORTRAIT_LIGHT, ""),
//    Effect_portraitLighting_X_studio("PortraitLighting_X_studio", R.drawable.portrait_lighting_x_studio, "portrait_light/PortraitLighting_X_studio.bundle", 1, Effect.EFFECT_TYPE_PORTRAIT_LIGHT, ""),

    Effect_baimao_Animoji("baimao_Animoji", R.drawable.ic_delete_all, "animoji/baimao_Animoji.bundle", 1, Effect.EFFECT_TYPE_ANIMOJI, ""),
    Effect_chaiquan_Animoji("chaiquan_Animoji", R.drawable.ic_delete_all, "animoji/chaiquan_Animoji.bundle", 1, Effect.EFFECT_TYPE_ANIMOJI, ""),
    Effect_douniuquan_Animoji("douniuquan_Animoji", R.drawable.ic_delete_all, "animoji/douniuquan_Animoji.bundle", 1, Effect.EFFECT_TYPE_ANIMOJI, ""),
    Effect_kulutou_Animoji("kulutou_Animoji", R.drawable.ic_delete_all, "animoji/kulutou_Animoji.bundle", 1, Effect.EFFECT_TYPE_ANIMOJI, ""),
    Effect_maotouying_Animoji("maotouying_Animoji", R.drawable.ic_delete_all, "animoji/maotouying_Animoji.bundle", 1, Effect.EFFECT_TYPE_ANIMOJI, ""),

    Effect_picasso_e1("picasso_e1", R.drawable.ic_delete_all, "portrait_drive/picasso_e1.bundle", 1, Effect.EFFECT_TYPE_PORTRAIT_DRIVE, ""),
    Effect_picasso_e2("picasso_e2", R.drawable.ic_delete_all, "portrait_drive/picasso_e2.bundle", 1, Effect.EFFECT_TYPE_PORTRAIT_DRIVE, ""),
    Effect_picasso_e3("picasso_e3", R.drawable.ic_delete_all, "portrait_drive/picasso_e3.bundle", 1, Effect.EFFECT_TYPE_PORTRAIT_DRIVE, ""),

    Effect_facewarp2("facewarp2", R.drawable.ic_delete_all, "facewarp/facewarp2.bundle", 4, Effect.EFFECT_TYPE_FACE_WARP, ""),
    Effect_facewarp3("facewarp3", R.drawable.ic_delete_all, "facewarp/facewarp3.bundle", 4, Effect.EFFECT_TYPE_FACE_WARP, ""),
    Effect_facewarp4("facewarp4", R.drawable.ic_delete_all, "facewarp/facewarp4.bundle", 4, Effect.EFFECT_TYPE_FACE_WARP, ""),
    Effect_facewarp5("facewarp5", R.drawable.ic_delete_all, "facewarp/facewarp5.bundle", 4, Effect.EFFECT_TYPE_FACE_WARP, ""),
    Effect_facewarp6("facewarp6", R.drawable.ic_delete_all, "facewarp/facewarp6.bundle", 4, Effect.EFFECT_TYPE_FACE_WARP, ""),

    Effect_douyin_old("douyin_01", R.drawable.ic_delete_all, "musicfilter/douyin_01.bundle", 4, Effect.EFFECT_TYPE_MUSIC_FILTER, ""),
    Effect_douyin("douyin_02", R.drawable.ic_delete_all, "musicfilter/douyin_02.bundle", 4, Effect.EFFECT_TYPE_MUSIC_FILTER, "");

    private String bundleName;
    private int resId;
    private String path;
    private int maxFace;
    private int effectType;
    private String description;

    EffectEnum(String name, int resId, String path, int maxFace, int effectType, String description) {
        this.bundleName = name;
        this.resId = resId;
        this.path = path;
        this.maxFace = maxFace;
        this.effectType = effectType;
        this.description = description;
    }

    public String bundleName() {
        return bundleName;
    }

    public int resId() {
        return resId;
    }

    public String path() {
        return path;
    }

    public int maxFace() {
        return maxFace;
    }

    public int effectType() {
        return effectType;
    }

    public String description() {
        return description;
    }

    public Effect effect() {
        return new Effect(bundleName, resId, path, maxFace, effectType, description);
    }

    public static ArrayList<Effect> getEffectsByEffectType(int effectType) {
        ArrayList<Effect> effects = new ArrayList<>();
        effects.add(EffectNone.effect());
        for (EffectEnum e : EffectEnum.values()) {
            if (e.effectType == effectType) {
                effects.add(e.effect());
            }
        }
        return effects;
    }
}
