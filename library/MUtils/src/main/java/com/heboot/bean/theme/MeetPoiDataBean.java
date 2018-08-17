package com.heboot.bean.theme;

import com.heboot.base.BaseBeanEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by heboot on 2018/2/26.
 */

public class MeetPoiDataBean extends BaseBeanEntity {
    /**
     * list : [{"business_id":23631327,"photo_urls":"http://qcloud.dpfile.com/pc/gPP3IBbhIezXx59dOW6fzfCqkd-r9UWX_wqNLbKW894dvtM1-my-fcr4D_rZXr3htOnd3gXQdDYlAqlaVaAFeZ0rYYyiRo_EhzufqWWjTjs.jpg","s_photo_urls":"http://qcloud.dpfile.com/pc/gPP3IBbhIezXx59dOW6fzfCqkd-r9UWX_wqNLbKW894dvtM1-my-fcr4D_rZXr3htOnd3gXQdDYlAqlaVaAFeZ0rYYyiRo_EhzufqWWjTjs.jpg","comments":8486,"name":"tomacado花厨(三里屯店)","avg_rating":5,"avg_price":181,"telephone":"18610023354","address":"东三环北路27号嘉铭中心B1","lng":116.45414,"lat":39.927055,"review_all_url":"https://m.dianping.com/shop/23631327/review_all"},{"business_id":97569570,"photo_urls":"https://img.meituan.net/msmerchant/01ee14d11e07a47e5d944bb52a042f7c426258.jpg%40120w_90h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","s_photo_urls":"https://img.meituan.net/msmerchant/01ee14d11e07a47e5d944bb52a042f7c426258.jpg%40278w_200h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","comments":289,"name":"The Cheesecake Factory 芝乐坊餐厅(王府中環店)","avg_rating":4.5,"avg_price":239,"telephone":"010-65251238","address":"王府井大街269号王府中環东座4层412-419号商铺","lng":116.40465,"lat":39.91129,"review_all_url":"https://m.dianping.com/shop/97569570/review_all"},{"business_id":3525907,"photo_urls":"https://img.meituan.net/msmerchant/994806ea9eee81f3bd29f8d8e0c3880d313068.png%40120w_90h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","s_photo_urls":"https://img.meituan.net/msmerchant/994806ea9eee81f3bd29f8d8e0c3880d313068.png%40278w_200h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","comments":1830,"name":"木马童话黑暗餐厅","avg_rating":4.5,"avg_price":349,"telephone":"010-66160336","address":"西单北大街109号西西友谊酒店八层","lng":116.36688,"lat":39.913162,"review_all_url":"https://m.dianping.com/shop/3525907/review_all"},{"business_id":93507832,"photo_urls":"https://img.meituan.net/msmerchant/a1210b01fc3c694828449125c0381b251425782.jpg%40120w_90h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","s_photo_urls":"https://img.meituan.net/msmerchant/a1210b01fc3c694828449125c0381b251425782.jpg%40278w_200h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","comments":1652,"name":"筑底食堂","avg_rating":4.5,"avg_price":122,"telephone":"010-65013997","address":"工人体育场北路8号三里屯SOHO1号商场B1-126","lng":116.44729,"lat":39.93148,"review_all_url":"https://m.dianping.com/shop/93507832/review_all"},{"business_id":97329886,"photo_urls":"https://img.meituan.net/msmerchant/e637f5b1064ff5dff2005e1f361db659529831.jpg%40120w_90h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","s_photo_urls":"https://img.meituan.net/msmerchant/e637f5b1064ff5dff2005e1f361db659529831.jpg%40278w_200h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","comments":651,"name":"tomacado花厨(王府中環店)","avg_rating":5,"avg_price":191,"telephone":"18600038056","address":"王府井大街269号王府中环东座4层402号商铺","lng":116.40497,"lat":39.911087,"review_all_url":"https://m.dianping.com/shop/97329886/review_all"},{"business_id":27311183,"photo_urls":"http://p0.meituan.net/apiback/645dadea4aefba15d65e989e9915ff0730594.jpg","s_photo_urls":"http://p0.meituan.net/apiback/645dadea4aefba15d65e989e9915ff0730594.jpg%40278w_200h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","comments":6678,"name":"伊豆野菜村(金地中心店)","avg_rating":5,"avg_price":146,"telephone":"010-85713003","address":"建国路91号金地中心C座3层-L310","lng":116.468544,"lat":39.90798,"review_all_url":"https://m.dianping.com/shop/27311183/review_all"},{"business_id":77267478,"photo_urls":"https://img.meituan.net/msmerchant/dc96f033433dbc3dd58d25c905779dfa8643190.jpg%40120w_90h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","s_photo_urls":"https://img.meituan.net/msmerchant/dc96f033433dbc3dd58d25c905779dfa8643190.jpg%40278w_200h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","comments":3138,"name":"Bad Farmers & Our Bakery","avg_rating":4,"avg_price":65,"telephone":"010-84472821","address":"三里屯路太古里北区N8楼一层","lng":116.448074,"lat":39.937313,"review_all_url":"https://m.dianping.com/shop/77267478/review_all"},{"business_id":69719009,"photo_urls":"http://p0.meituan.net/msmerchant/71782a585dca750d83b082f9128508fc84286.jpg","s_photo_urls":"http://p0.meituan.net/msmerchant/71782a585dca750d83b082f9128508fc84286.jpg%40278w_200h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","comments":6539,"name":"第六季自助餐厅(西三环店)","avg_rating":5,"avg_price":254,"telephone":"010-68403333","address":"西三环北路21号久凌大厦北楼二层（北外东门往南100米","lng":116.30301,"lat":39.95212,"review_all_url":"https://m.dianping.com/shop/69719009/review_all"},{"business_id":72348890,"photo_urls":"https://img.meituan.net/msmerchant/a95f67d9d72cab930b6e15b22d8b3964761507.jpg%40120w_90h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","s_photo_urls":"https://img.meituan.net/msmerchant/a95f67d9d72cab930b6e15b22d8b3964761507.jpg%40278w_200h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","comments":3089,"name":"SPACELAB失重餐厅(蓝色港湾店)","avg_rating":4.5,"avg_price":145,"telephone":"010-59056269","address":"朝阳公园路6号蓝色港湾12号","lng":116.46965,"lat":39.94718,"review_all_url":"https://m.dianping.com/shop/72348890/review_all"},{"business_id":59431302,"photo_urls":"http://p0.meituan.net/dpmerchantalbum/af1c37f2dc814032292fceb73fc48b00451369.jpg","s_photo_urls":"http://p0.meituan.net/dpmerchantalbum/af1c37f2dc814032292fceb73fc48b00451369.jpg%40278w_200h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","comments":5438,"name":"成都葫芦娃一家人火锅(三里屯店)","avg_rating":5,"avg_price":154,"telephone":"010-67777456","address":"三里屯工体东路丙2号中国红街3号楼301室","lng":116.44456,"lat":39.930977,"review_all_url":"https://m.dianping.com/shop/59431302/review_all"},{"business_id":93389432,"photo_urls":"https://img.meituan.net/msmerchant/e651e730511cc4db0fefa290f72afb892572782.jpg%40120w_90h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","s_photo_urls":"https://img.meituan.net/msmerchant/e651e730511cc4db0fefa290f72afb892572782.jpg%40278w_200h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","comments":838,"name":"行运打边炉","avg_rating":4.5,"avg_price":261,"telephone":"010-64160590","address":"三元里街16号","lng":116.45004,"lat":39.95107,"review_all_url":"https://m.dianping.com/shop/93389432/review_all"},{"business_id":32333630,"photo_urls":"http://p1.meituan.net/msmerchant/2446456843aa8e5ba129227dbdeda858586970.jpg","s_photo_urls":"http://p1.meituan.net/msmerchant/2446456843aa8e5ba129227dbdeda858586970.jpg%40278w_200h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","comments":9703,"name":"四季民福烤鸭店(故宫店)","avg_rating":5,"avg_price":150,"telephone":"010-65267369","address":"南池子大街11号故宫东门旁","lng":116.396645,"lat":39.91321,"review_all_url":"https://m.dianping.com/shop/32333630/review_all"},{"business_id":77589258,"photo_urls":"https://img.meituan.net/msmerchant/2bd36028cd0c77e06df26edf5384baea425851.jpg%40120w_90h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","s_photo_urls":"https://img.meituan.net/msmerchant/2bd36028cd0c77e06df26edf5384baea425851.jpg%40278w_200h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","comments":799,"name":"熔时·FIRESIDE(世界城店)","avg_rating":5,"avg_price":164,"telephone":"010-85999580","address":"金汇路9号E座1层E132-E222-E232","lng":116.44613,"lat":39.91641,"review_all_url":"https://m.dianping.com/shop/77589258/review_all"},{"business_id":20942012,"photo_urls":"https://img.meituan.net/msmerchant/2e4e29867ea70aa43a405315a7d358b577534.jpg%40120w_90h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","s_photo_urls":"https://img.meituan.net/msmerchant/2e4e29867ea70aa43a405315a7d358b577534.jpg%40278w_200h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","comments":13640,"name":"局气(西单店)","avg_rating":4.5,"avg_price":97,"telephone":"010-68085088","address":"太仆寺街21号府右街宾馆1层","lng":116.37398,"lat":39.911026,"review_all_url":"https://m.dianping.com/shop/20942012/review_all"},{"business_id":95372984,"photo_urls":"https://img.meituan.net/msmerchant/a536be03f199e53c5296f8d58c9ef915102190.jpg%40120w_90h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","s_photo_urls":"https://img.meituan.net/msmerchant/a536be03f199e53c5296f8d58c9ef915102190.jpg%40278w_200h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","comments":1648,"name":"奈雪の茶(西单大悦城店)","avg_rating":4.5,"avg_price":44,"telephone":"010-66517920","address":"西单北大街131号西单大悦城购物中心3F-20/21A/21B","lng":116.36659,"lat":39.909065,"review_all_url":"https://m.dianping.com/shop/95372984/review_all"},{"business_id":13745584,"photo_urls":"https://img.meituan.net/msmerchant/a62e2324a53fde0a5e2a19b41bded4c02602506.jpg%40120w_90h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","s_photo_urls":"https://img.meituan.net/msmerchant/a62e2324a53fde0a5e2a19b41bded4c02602506.jpg%40278w_200h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","comments":4158,"name":"大董(工体店)","avg_rating":4.5,"avg_price":390,"telephone":"010-65511808","address":"工人体育场东路工人体育场东门北侧","lng":116.44223,"lat":39.929707,"review_all_url":"https://m.dianping.com/shop/13745584/review_all"},{"business_id":8291516,"photo_urls":"http://qcloud.dpfile.com/pc/fVmuXfAL1PqWSwWDgtuYnbZ9LACsYdXQHQrwkv3BRDT_m4Yu_x3NrizsC_tDjGgktOnd3gXQdDYlAqlaVaAFeZ0rYYyiRo_EhzufqWWjTjs.jpg","s_photo_urls":"http://qcloud.dpfile.com/pc/fVmuXfAL1PqWSwWDgtuYnbZ9LACsYdXQHQrwkv3BRDT_m4Yu_x3NrizsC_tDjGgktOnd3gXQdDYlAqlaVaAFeZ0rYYyiRo_EhzufqWWjTjs.jpg","comments":14609,"name":"满恒记火锅","avg_rating":5,"avg_price":90,"telephone":"010-66517188","address":"平安里西大街14号","lng":116.361435,"lat":39.930714,"review_all_url":"https://m.dianping.com/shop/8291516/review_all"},{"business_id":513329,"photo_urls":"http://qcloud.dpfile.com/pc/NuJU8VrAnLRQ7Ins1JyZ3G1E676q9-Ytd7EVq8T4iYYjJnc0UtuLqmobEMP_5CzwtOnd3gXQdDYlAqlaVaAFeZ0rYYyiRo_EhzufqWWjTjs.jpg","s_photo_urls":"http://qcloud.dpfile.com/pc/NuJU8VrAnLRQ7Ins1JyZ3G1E676q9-Ytd7EVq8T4iYYjJnc0UtuLqmobEMP_5CzwtOnd3gXQdDYlAqlaVaAFeZ0rYYyiRo_EhzufqWWjTjs.jpg","comments":18536,"name":"聚宝源(牛街北口店)","avg_rating":4.5,"avg_price":106,"telephone":"010-83545602","address":"牛街西里商业1号楼5-2号","lng":116.35708,"lat":39.8854,"review_all_url":"https://m.dianping.com/shop/513329/review_all"},{"business_id":20842827,"photo_urls":"http://p1.meituan.net/apiback/65e393bd7e6e2b6b3743f94b8b075c36504457.jpg","s_photo_urls":"http://p1.meituan.net/apiback/65e393bd7e6e2b6b3743f94b8b075c36504457.jpg%40278w_200h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","comments":13362,"name":"温野菜日式火锅","avg_rating":5,"avg_price":146,"telephone":"010-64165600","address":"新中街甲3号蜂巢剧场B1层","lng":116.43263,"lat":39.93892,"review_all_url":"https://m.dianping.com/shop/20842827/review_all"},{"business_id":2809750,"photo_urls":"https://img.meituan.net/msmerchant/ebe86a19ed57f1df915c0db15f20fb0a2232592.jpg%40120w_90h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","s_photo_urls":"https://img.meituan.net/msmerchant/ebe86a19ed57f1df915c0db15f20fb0a2232592.jpg%40278w_200h_1e_1c_1l_80q%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20","comments":2969,"name":"北京亮China Grill","avg_rating":4.5,"avg_price":1083,"telephone":"010-85671098","address":"建国门外大街2号北京柏悦酒店66层","lng":116.45305,"lat":39.906048,"review_all_url":"https://m.dianping.com/shop/2809750/review_all"}]
     * total : 137905
     * pageSize : 20
     * totalPages : 100
     * sp : 1
     * zone_config : {"id":131,"name":"北京","items":[{"id":0,"name":"热门商圈","pid":131,"items":[{"id":131,"name":"全部商圈","pid":131},{"id":1311465,"name":"建外大街","pid":13114},{"id":1311466,"name":"朝外大街","pid":13114},{"id":1311467,"name":"朝阳公园/团结湖","pid":13114},{"id":1311469,"name":"亮马桥/三元桥","pid":13114},{"id":1311470,"name":"亚运村","pid":13114},{"id":1311471,"name":"望京","pid":13114},{"id":1311472,"name":"劲松/潘家园","pid":13114},{"id":1312078,"name":"大望路","pid":13114},{"id":1312578,"name":"国贸","pid":13114},{"id":1312579,"name":"双井","pid":13114},{"id":1312580,"name":"三里屯","pid":13114},{"id":1312583,"name":"酒仙桥","pid":13114},{"id":1312871,"name":"十里堡","pid":13114},{"id":13122998,"name":"青年路","pid":13114},{"id":1311488,"name":"中关村","pid":13117},{"id":1311489,"name":"五道口","pid":13117},{"id":1311493,"name":"公主坟/万寿路","pid":13117},{"id":1312588,"name":"五棵松","pid":13117},{"id":1311475,"name":"王府井/东单","pid":13115},{"id":1311481,"name":"西单","pid":13116}]},{"id":13114,"name":"朝阳区","pid":131,"items":[{"id":13114,"name":"全部朝阳区","pid":13114},{"id":1312580,"name":"三里屯","pid":13114},{"id":1311471,"name":"望京","pid":13114},{"id":1312578,"name":"国贸","pid":13114},{"id":1311466,"name":"朝外大街","pid":13114},{"id":1311470,"name":"亚运村","pid":13114},{"id":1311469,"name":"亮马桥/三元桥","pid":13114},{"id":1312078,"name":"大望路","pid":13114},{"id":1312579,"name":"双井","pid":13114},{"id":1312871,"name":"十里堡","pid":13114},{"id":1311467,"name":"朝阳公园/团结湖","pid":13114},{"id":1311465,"name":"建外大街","pid":13114},{"id":1312583,"name":"酒仙桥","pid":13114},{"id":13122998,"name":"青年路","pid":13114},{"id":1311472,"name":"劲松/潘家园","pid":13114},{"id":1312584,"name":"管庄","pid":13114},{"id":1311473,"name":"安贞","pid":13114},{"id":13122997,"name":"太阳宫","pid":13114},{"id":1311474,"name":"朝阳其它","pid":13114},{"id":1311468,"name":"左家庄","pid":13114},{"id":13123019,"name":"常营","pid":13114},{"id":1312870,"name":"北苑家园","pid":13114},{"id":1312581,"name":"对外经贸","pid":13114},{"id":1312586,"name":"十八里店","pid":13114},{"id":13123003,"name":"百子湾","pid":13114},{"id":13123010,"name":"蓝色港湾","pid":13114},{"id":13123002,"name":"工人体育场","pid":13114},{"id":1317509,"name":"东坝","pid":13114},{"id":13123015,"name":"小营","pid":13114},{"id":13123001,"name":"慈云寺/八里庄","pid":13114},{"id":13122996,"name":"四惠","pid":13114},{"id":13123017,"name":"大屯","pid":13114},{"id":13123005,"name":"双桥","pid":13114},{"id":13112015,"name":"定福庄","pid":13114},{"id":13123006,"name":"北京欢乐谷","pid":13114},{"id":13112013,"name":"马泉营","pid":13114},{"id":13122999,"name":"石佛营","pid":13114},{"id":13123020,"name":"798/大山子","pid":13114},{"id":13123013,"name":"十里河","pid":13114},{"id":13123009,"name":"霄云路","pid":13114},{"id":13123014,"name":"立水桥","pid":13114},{"id":13123000,"name":"甜水园","pid":13114},{"id":13123007,"name":"高碑店","pid":13114},{"id":13123012,"name":"姚家园","pid":13114},{"id":13123016,"name":"北沙滩","pid":13114},{"id":13123004,"name":"传媒大学/二外","pid":13114},{"id":13123018,"name":"小庄/红庙","pid":13114},{"id":13112012,"name":"孙河","pid":13114},{"id":13123011,"name":"燕莎/农业展览馆","pid":13114},{"id":13170191,"name":"芍药居","pid":13114},{"id":13170269,"name":"草房","pid":13114},{"id":13123008,"name":"北京东站","pid":13114},{"id":13170525,"name":"游娱联盟壹号基地","pid":13114},{"id":13183302,"name":"世贸天阶","pid":13114},{"id":13183304,"name":"东大桥","pid":13114},{"id":13181430,"name":"朝阳公园","pid":13114}]},{"id":13117,"name":"海淀区","pid":131,"items":[{"id":13117,"name":"全部海淀区","pid":13117},{"id":1311489,"name":"五道口","pid":13117},{"id":1311488,"name":"中关村","pid":13117},{"id":1311493,"name":"公主坟/万寿路","pid":13117},{"id":1312588,"name":"五棵松","pid":13117},{"id":1311490,"name":"北太平庄","pid":13117},{"id":1311494,"name":"紫竹桥","pid":13117},{"id":1311996,"name":"魏公村","pid":13117},{"id":1311491,"name":"苏州桥","pid":13117},{"id":1311495,"name":"航天桥","pid":13117},{"id":1312872,"name":"远大路","pid":13117},{"id":1312589,"name":"清河","pid":13117},{"id":1312587,"name":"双榆树","pid":13117},{"id":1311492,"name":"北下关","pid":13117},{"id":1311496,"name":"上地","pid":13117},{"id":1311497,"name":"颐和园","pid":13117},{"id":1311498,"name":"海淀其它","pid":13117},{"id":13123034,"name":"万柳","pid":13117},{"id":1317510,"name":"香山","pid":13117},{"id":13123033,"name":"人民大学","pid":13117},{"id":13123032,"name":"四季青","pid":13117},{"id":13123029,"name":"大钟寺","pid":13117},{"id":13123030,"name":"知春路","pid":13117},{"id":13123031,"name":"西三旗","pid":13117},{"id":13123988,"name":"军博","pid":13117},{"id":13123035,"name":"学院桥","pid":13117},{"id":13170131,"name":"田村","pid":13117},{"id":13123989,"name":"农业大学西区","pid":13117}]},{"id":13115,"name":"东城区","pid":131,"items":[{"id":13115,"name":"全部东城区","pid":13115},{"id":1311475,"name":"王府井/东单","pid":13115},{"id":1311504,"name":"崇文门","pid":13115},{"id":1312066,"name":"东直门","pid":13115},{"id":13123023,"name":"南锣鼓巷/鼓楼东大街","pid":13115},{"id":13123024,"name":"北新桥/簋街","pid":13115},{"id":1311478,"name":"安定门","pid":13115},{"id":1311477,"name":"东四","pid":13115},{"id":1312591,"name":"和平里","pid":13115},{"id":1311479,"name":"朝阳门","pid":13115},{"id":1311476,"name":"建国门/北京站","pid":13115},{"id":1312590,"name":"广渠门","pid":13115},{"id":1311505,"name":"天坛","pid":13115},{"id":13123022,"name":"雍和宫/地坛","pid":13115},{"id":1312875,"name":"沙子口","pid":13115},{"id":13123021,"name":"东四十条","pid":13115},{"id":1312874,"name":"左安门","pid":13115},{"id":13123026,"name":"沙滩/美术馆灯市口","pid":13115},{"id":13123025,"name":"光明楼/龙潭湖","pid":13115}]},{"id":13116,"name":"西城区","pid":131,"items":[{"id":13116,"name":"全部西城区","pid":13116},{"id":1311481,"name":"西单","pid":13116},{"id":1311484,"name":"西直门/动物园","pid":13116},{"id":1312595,"name":"什刹海","pid":13116},{"id":1311503,"name":"前门","pid":13116},{"id":1311482,"name":"复兴门","pid":13116},{"id":1311486,"name":"地安门","pid":13116},{"id":1311500,"name":"广外大街","pid":13116},{"id":1311483,"name":"阜成门","pid":13116},{"id":1311485,"name":"新街口","pid":13116},{"id":1312593,"name":"西四","pid":13116},{"id":1312594,"name":"月坛","pid":13116},{"id":1311501,"name":"宣武门","pid":13116},{"id":1311499,"name":"广内大街","pid":13116},{"id":1312597,"name":"虎坊桥","pid":13116},{"id":1312596,"name":"牛街","pid":13116},{"id":1312873,"name":"德外大街","pid":13116},{"id":1312876,"name":"菜市口","pid":13116},{"id":13123027,"name":"陶然亭","pid":13116},{"id":13123028,"name":"南菜园/白纸坊","pid":13116}]},{"id":13120,"name":"丰台区","pid":131,"items":[{"id":13120,"name":"全部丰台区","pid":13120},{"id":1311507,"name":"方庄","pid":13120},{"id":1311508,"name":"六里桥/丽泽桥","pid":13120},{"id":1311995,"name":"洋桥/木樨园","pid":13120},{"id":1312877,"name":"刘家窑","pid":13120},{"id":13123039,"name":"马家堡/角门","pid":13120},{"id":1312878,"name":"青塔","pid":13120},{"id":1312880,"name":"草桥","pid":13120},{"id":1317041,"name":"大红门","pid":13120},{"id":1317040,"name":"花乡","pid":13120},{"id":1317506,"name":"公益西桥","pid":13120},{"id":1312881,"name":"看丹桥","pid":13120},{"id":1312879,"name":"开阳里","pid":13120},{"id":1312592,"name":"北大地","pid":13120},{"id":1311509,"name":"丰台其它","pid":13120},{"id":1311994,"name":"右安门","pid":13120},{"id":1317507,"name":"云岗","pid":13120},{"id":13123036,"name":"北京西站/六里桥","pid":13120},{"id":1317508,"name":"卢沟桥","pid":13120},{"id":13123040,"name":"丽泽桥/丰管路","pid":13120},{"id":13123038,"name":"夏家胡同/纪家庙","pid":13120},{"id":13125600,"name":"总部基地","pid":13120},{"id":13123037,"name":"分钟寺/成寿寺","pid":13120},{"id":13170132,"name":"宋家庄","pid":13120},{"id":13170610,"name":"槐房万达广场","pid":13120},{"id":13170275,"name":"石榴庄","pid":13120}]},{"id":1315952,"name":"大兴区","pid":131,"items":[{"id":1315952,"name":"全部大兴区","pid":1315952},{"id":1317043,"name":"西红门","pid":1315952},{"id":1315959,"name":"亦庄","pid":1315952},{"id":1315961,"name":"黄村","pid":1315952},{"id":1315960,"name":"旧宫","pid":1315952},{"id":13170633,"name":"庞各庄","pid":1315952}]},{"id":1315950,"name":"昌平区","pid":131,"items":[{"id":1315950,"name":"全部昌平区","pid":1315950},{"id":1315953,"name":"回龙观","pid":1315950},{"id":1315955,"name":"昌平镇","pid":1315950},{"id":1315954,"name":"天通苑","pid":1315950},{"id":1317042,"name":"小汤山","pid":1315950},{"id":13123043,"name":"北七家","pid":1315950},{"id":13123044,"name":"沙河","pid":1315950},{"id":13123042,"name":"南口镇","pid":1315950}]},{"id":1315951,"name":"通州区","pid":131,"items":[{"id":1315951,"name":"全部通州区","pid":1315951},{"id":1315957,"name":"梨园","pid":1315951},{"id":1315958,"name":"新华大街","pid":1315951},{"id":13123045,"name":"通州北苑","pid":1315951},{"id":1317521,"name":"九棵树","pid":1315951},{"id":1315956,"name":"果园","pid":1315951},{"id":13125907,"name":"马驹桥","pid":1315951},{"id":13123990,"name":"武夷花园","pid":1315951},{"id":13164881,"name":"宋庄","pid":1315951},{"id":13164883,"name":"物资学院","pid":1315951},{"id":13164882,"name":"西集","pid":1315951},{"id":13170618,"name":"次渠","pid":1315951}]},{"id":131328,"name":"石景山区","pid":131,"items":[{"id":131328,"name":"全部石景山区","pid":131328},{"id":1311926,"name":"鲁谷","pid":131328},{"id":1311924,"name":"古城/八角","pid":131328},{"id":1311923,"name":"苹果园","pid":131328},{"id":1311927,"name":"石景山其它","pid":131328},{"id":1312882,"name":"模式口","pid":131328}]},{"id":1319158,"name":"顺义区","pid":131,"items":[{"id":1319158,"name":"全部顺义区","pid":1319158},{"id":13123041,"name":"顺义","pid":1319158},{"id":1312585,"name":"首都机场","pid":1319158},{"id":13164877,"name":"后沙峪","pid":1319158},{"id":13112016,"name":"国展","pid":1319158},{"id":13164878,"name":"马坡牛栏山","pid":1319158},{"id":13164880,"name":"石园","pid":1319158},{"id":13164879,"name":"南彩","pid":1319158}]},{"id":13127615,"name":"怀柔区","pid":131,"items":[{"id":13127615,"name":"全部怀柔区","pid":13127615},{"id":13164866,"name":"雁栖开发区","pid":13127615},{"id":13164947,"name":"渤海镇/慕田峪长城","pid":13127615},{"id":13164858,"name":"商业街","pid":13127615},{"id":13164859,"name":"京北大世界","pid":13127615},{"id":13164860,"name":"斜街","pid":13127615},{"id":13164865,"name":"桥梓镇","pid":13127615},{"id":13164862,"name":"东关","pid":13127615},{"id":13164861,"name":"下园","pid":13127615},{"id":13164863,"name":"富乐大街","pid":13127615},{"id":13164864,"name":"庙城","pid":13127615}]},{"id":1319157,"name":"房山区","pid":131,"items":[{"id":1319157,"name":"全部房山区","pid":1319157},{"id":13112011,"name":"良乡","pid":1319157},{"id":13167342,"name":"城关镇","pid":1319157},{"id":13130781,"name":"长阳镇","pid":1319157},{"id":13167346,"name":"窦店镇","pid":1319157},{"id":13167376,"name":"十渡镇","pid":1319157},{"id":13167350,"name":"燕山","pid":1319157},{"id":13167349,"name":"阎村镇","pid":1319157},{"id":13167384,"name":"青龙湖镇","pid":1319157},{"id":13167374,"name":"河北镇","pid":1319157}]},{"id":13127614,"name":"门头沟区","pid":131,"items":[{"id":13127614,"name":"全部门头沟区","pid":13127614}]},{"id":131434,"name":"密云区","pid":131,"items":[{"id":131434,"name":"全部密云区","pid":131434},{"id":13165439,"name":"密云镇","pid":131434},{"id":13165445,"name":"溪翁庄镇","pid":131434},{"id":13165441,"name":"十里堡镇","pid":131434},{"id":13165435,"name":"古北口镇","pid":131434},{"id":13165442,"name":"石城镇","pid":131434},{"id":13165443,"name":"太师屯镇","pid":131434},{"id":13165440,"name":"穆家峪镇","pid":131434},{"id":13165446,"name":"新城子镇","pid":131434},{"id":13165430,"name":"不老屯镇","pid":131434},{"id":13165436,"name":"河南寨镇","pid":131434},{"id":13165431,"name":"大城子镇","pid":131434},{"id":13165444,"name":"西田各庄镇","pid":131434},{"id":13165429,"name":"北庄镇","pid":131434},{"id":13165437,"name":"巨各庄镇","pid":131434},{"id":13165434,"name":"高岭镇","pid":131434},{"id":13165438,"name":"经济开发区","pid":131434},{"id":13165433,"name":"冯家峪镇","pid":131434},{"id":13165432,"name":"东邵渠镇","pid":131434},{"id":13127617,"name":"密云县其他","pid":131434}]},{"id":13127616,"name":"平谷区","pid":131,"items":[{"id":13127616,"name":"全部平谷区","pid":13127616}]},{"id":131435,"name":"延庆区","pid":131,"items":[{"id":131435,"name":"全部延庆区","pid":131435},{"id":13165454,"name":"千家店镇","pid":131435},{"id":13165450,"name":"井庄镇","pid":131435},{"id":13165451,"name":"旧县镇","pid":131435},{"id":13165458,"name":"延庆镇","pid":131435},{"id":13165460,"name":"张山营镇","pid":131435},{"id":13165447,"name":"八达岭镇","pid":131435},{"id":13165452,"name":"康庄镇","pid":131435},{"id":13165459,"name":"永宁镇","pid":131435},{"id":13165449,"name":"大庄科乡","pid":131435},{"id":13165455,"name":"沈家营镇","pid":131435},{"id":13165448,"name":"大榆树镇","pid":131435},{"id":13165461,"name":"珍珠泉乡","pid":131435},{"id":13165453,"name":"刘斌堡乡","pid":131435},{"id":13165456,"name":"四海镇","pid":131435},{"id":13127618,"name":"延庆县其他","pid":131435}]}]}
     * nearby_config : {"id":"-1000","name":"附近","items":[{"id":500,"name":"0.5km"},{"id":1000,"name":"1km"},{"id":2000,"name":"2km"},{"id":5000,"name":"5km"}]}
     * sort_config : [{"id":1,"name":"智能排序"},{"id":2,"name":"星级最高"},{"id":7,"name":"离我最近"},{"id":9,"name":"人均最高"}]
     */

    private ZoneConfigBean zone_config;
    private NearbyConfigBean nearby_config;
    private List<ListBean> list;
    private List<SortConfigBean> sort_config;

    public ZoneConfigBean getZone_config() {
        return zone_config;
    }

    public void setZone_config(ZoneConfigBean zone_config) {
        this.zone_config = zone_config;
    }

    public NearbyConfigBean getNearby_config() {
        return nearby_config;
    }

    public void setNearby_config(NearbyConfigBean nearby_config) {
        this.nearby_config = nearby_config;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<SortConfigBean> getSort_config() {
        return sort_config;
    }

    public void setSort_config(List<SortConfigBean> sort_config) {
        this.sort_config = sort_config;
    }

    public static class ZoneConfigBean {
        /**
         * id : 131
         * name : 北京
         * items : [{"id":0,"name":"热门商圈","pid":131,"items":[{"id":131,"name":"全部商圈","pid":131},{"id":1311465,"name":"建外大街","pid":13114},{"id":1311466,"name":"朝外大街","pid":13114},{"id":1311467,"name":"朝阳公园/团结湖","pid":13114},{"id":1311469,"name":"亮马桥/三元桥","pid":13114},{"id":1311470,"name":"亚运村","pid":13114},{"id":1311471,"name":"望京","pid":13114},{"id":1311472,"name":"劲松/潘家园","pid":13114},{"id":1312078,"name":"大望路","pid":13114},{"id":1312578,"name":"国贸","pid":13114},{"id":1312579,"name":"双井","pid":13114},{"id":1312580,"name":"三里屯","pid":13114},{"id":1312583,"name":"酒仙桥","pid":13114},{"id":1312871,"name":"十里堡","pid":13114},{"id":13122998,"name":"青年路","pid":13114},{"id":1311488,"name":"中关村","pid":13117},{"id":1311489,"name":"五道口","pid":13117},{"id":1311493,"name":"公主坟/万寿路","pid":13117},{"id":1312588,"name":"五棵松","pid":13117},{"id":1311475,"name":"王府井/东单","pid":13115},{"id":1311481,"name":"西单","pid":13116}]},{"id":13114,"name":"朝阳区","pid":131,"items":[{"id":13114,"name":"全部朝阳区","pid":13114},{"id":1312580,"name":"三里屯","pid":13114},{"id":1311471,"name":"望京","pid":13114},{"id":1312578,"name":"国贸","pid":13114},{"id":1311466,"name":"朝外大街","pid":13114},{"id":1311470,"name":"亚运村","pid":13114},{"id":1311469,"name":"亮马桥/三元桥","pid":13114},{"id":1312078,"name":"大望路","pid":13114},{"id":1312579,"name":"双井","pid":13114},{"id":1312871,"name":"十里堡","pid":13114},{"id":1311467,"name":"朝阳公园/团结湖","pid":13114},{"id":1311465,"name":"建外大街","pid":13114},{"id":1312583,"name":"酒仙桥","pid":13114},{"id":13122998,"name":"青年路","pid":13114},{"id":1311472,"name":"劲松/潘家园","pid":13114},{"id":1312584,"name":"管庄","pid":13114},{"id":1311473,"name":"安贞","pid":13114},{"id":13122997,"name":"太阳宫","pid":13114},{"id":1311474,"name":"朝阳其它","pid":13114},{"id":1311468,"name":"左家庄","pid":13114},{"id":13123019,"name":"常营","pid":13114},{"id":1312870,"name":"北苑家园","pid":13114},{"id":1312581,"name":"对外经贸","pid":13114},{"id":1312586,"name":"十八里店","pid":13114},{"id":13123003,"name":"百子湾","pid":13114},{"id":13123010,"name":"蓝色港湾","pid":13114},{"id":13123002,"name":"工人体育场","pid":13114},{"id":1317509,"name":"东坝","pid":13114},{"id":13123015,"name":"小营","pid":13114},{"id":13123001,"name":"慈云寺/八里庄","pid":13114},{"id":13122996,"name":"四惠","pid":13114},{"id":13123017,"name":"大屯","pid":13114},{"id":13123005,"name":"双桥","pid":13114},{"id":13112015,"name":"定福庄","pid":13114},{"id":13123006,"name":"北京欢乐谷","pid":13114},{"id":13112013,"name":"马泉营","pid":13114},{"id":13122999,"name":"石佛营","pid":13114},{"id":13123020,"name":"798/大山子","pid":13114},{"id":13123013,"name":"十里河","pid":13114},{"id":13123009,"name":"霄云路","pid":13114},{"id":13123014,"name":"立水桥","pid":13114},{"id":13123000,"name":"甜水园","pid":13114},{"id":13123007,"name":"高碑店","pid":13114},{"id":13123012,"name":"姚家园","pid":13114},{"id":13123016,"name":"北沙滩","pid":13114},{"id":13123004,"name":"传媒大学/二外","pid":13114},{"id":13123018,"name":"小庄/红庙","pid":13114},{"id":13112012,"name":"孙河","pid":13114},{"id":13123011,"name":"燕莎/农业展览馆","pid":13114},{"id":13170191,"name":"芍药居","pid":13114},{"id":13170269,"name":"草房","pid":13114},{"id":13123008,"name":"北京东站","pid":13114},{"id":13170525,"name":"游娱联盟壹号基地","pid":13114},{"id":13183302,"name":"世贸天阶","pid":13114},{"id":13183304,"name":"东大桥","pid":13114},{"id":13181430,"name":"朝阳公园","pid":13114}]},{"id":13117,"name":"海淀区","pid":131,"items":[{"id":13117,"name":"全部海淀区","pid":13117},{"id":1311489,"name":"五道口","pid":13117},{"id":1311488,"name":"中关村","pid":13117},{"id":1311493,"name":"公主坟/万寿路","pid":13117},{"id":1312588,"name":"五棵松","pid":13117},{"id":1311490,"name":"北太平庄","pid":13117},{"id":1311494,"name":"紫竹桥","pid":13117},{"id":1311996,"name":"魏公村","pid":13117},{"id":1311491,"name":"苏州桥","pid":13117},{"id":1311495,"name":"航天桥","pid":13117},{"id":1312872,"name":"远大路","pid":13117},{"id":1312589,"name":"清河","pid":13117},{"id":1312587,"name":"双榆树","pid":13117},{"id":1311492,"name":"北下关","pid":13117},{"id":1311496,"name":"上地","pid":13117},{"id":1311497,"name":"颐和园","pid":13117},{"id":1311498,"name":"海淀其它","pid":13117},{"id":13123034,"name":"万柳","pid":13117},{"id":1317510,"name":"香山","pid":13117},{"id":13123033,"name":"人民大学","pid":13117},{"id":13123032,"name":"四季青","pid":13117},{"id":13123029,"name":"大钟寺","pid":13117},{"id":13123030,"name":"知春路","pid":13117},{"id":13123031,"name":"西三旗","pid":13117},{"id":13123988,"name":"军博","pid":13117},{"id":13123035,"name":"学院桥","pid":13117},{"id":13170131,"name":"田村","pid":13117},{"id":13123989,"name":"农业大学西区","pid":13117}]},{"id":13115,"name":"东城区","pid":131,"items":[{"id":13115,"name":"全部东城区","pid":13115},{"id":1311475,"name":"王府井/东单","pid":13115},{"id":1311504,"name":"崇文门","pid":13115},{"id":1312066,"name":"东直门","pid":13115},{"id":13123023,"name":"南锣鼓巷/鼓楼东大街","pid":13115},{"id":13123024,"name":"北新桥/簋街","pid":13115},{"id":1311478,"name":"安定门","pid":13115},{"id":1311477,"name":"东四","pid":13115},{"id":1312591,"name":"和平里","pid":13115},{"id":1311479,"name":"朝阳门","pid":13115},{"id":1311476,"name":"建国门/北京站","pid":13115},{"id":1312590,"name":"广渠门","pid":13115},{"id":1311505,"name":"天坛","pid":13115},{"id":13123022,"name":"雍和宫/地坛","pid":13115},{"id":1312875,"name":"沙子口","pid":13115},{"id":13123021,"name":"东四十条","pid":13115},{"id":1312874,"name":"左安门","pid":13115},{"id":13123026,"name":"沙滩/美术馆灯市口","pid":13115},{"id":13123025,"name":"光明楼/龙潭湖","pid":13115}]},{"id":13116,"name":"西城区","pid":131,"items":[{"id":13116,"name":"全部西城区","pid":13116},{"id":1311481,"name":"西单","pid":13116},{"id":1311484,"name":"西直门/动物园","pid":13116},{"id":1312595,"name":"什刹海","pid":13116},{"id":1311503,"name":"前门","pid":13116},{"id":1311482,"name":"复兴门","pid":13116},{"id":1311486,"name":"地安门","pid":13116},{"id":1311500,"name":"广外大街","pid":13116},{"id":1311483,"name":"阜成门","pid":13116},{"id":1311485,"name":"新街口","pid":13116},{"id":1312593,"name":"西四","pid":13116},{"id":1312594,"name":"月坛","pid":13116},{"id":1311501,"name":"宣武门","pid":13116},{"id":1311499,"name":"广内大街","pid":13116},{"id":1312597,"name":"虎坊桥","pid":13116},{"id":1312596,"name":"牛街","pid":13116},{"id":1312873,"name":"德外大街","pid":13116},{"id":1312876,"name":"菜市口","pid":13116},{"id":13123027,"name":"陶然亭","pid":13116},{"id":13123028,"name":"南菜园/白纸坊","pid":13116}]},{"id":13120,"name":"丰台区","pid":131,"items":[{"id":13120,"name":"全部丰台区","pid":13120},{"id":1311507,"name":"方庄","pid":13120},{"id":1311508,"name":"六里桥/丽泽桥","pid":13120},{"id":1311995,"name":"洋桥/木樨园","pid":13120},{"id":1312877,"name":"刘家窑","pid":13120},{"id":13123039,"name":"马家堡/角门","pid":13120},{"id":1312878,"name":"青塔","pid":13120},{"id":1312880,"name":"草桥","pid":13120},{"id":1317041,"name":"大红门","pid":13120},{"id":1317040,"name":"花乡","pid":13120},{"id":1317506,"name":"公益西桥","pid":13120},{"id":1312881,"name":"看丹桥","pid":13120},{"id":1312879,"name":"开阳里","pid":13120},{"id":1312592,"name":"北大地","pid":13120},{"id":1311509,"name":"丰台其它","pid":13120},{"id":1311994,"name":"右安门","pid":13120},{"id":1317507,"name":"云岗","pid":13120},{"id":13123036,"name":"北京西站/六里桥","pid":13120},{"id":1317508,"name":"卢沟桥","pid":13120},{"id":13123040,"name":"丽泽桥/丰管路","pid":13120},{"id":13123038,"name":"夏家胡同/纪家庙","pid":13120},{"id":13125600,"name":"总部基地","pid":13120},{"id":13123037,"name":"分钟寺/成寿寺","pid":13120},{"id":13170132,"name":"宋家庄","pid":13120},{"id":13170610,"name":"槐房万达广场","pid":13120},{"id":13170275,"name":"石榴庄","pid":13120}]},{"id":1315952,"name":"大兴区","pid":131,"items":[{"id":1315952,"name":"全部大兴区","pid":1315952},{"id":1317043,"name":"西红门","pid":1315952},{"id":1315959,"name":"亦庄","pid":1315952},{"id":1315961,"name":"黄村","pid":1315952},{"id":1315960,"name":"旧宫","pid":1315952},{"id":13170633,"name":"庞各庄","pid":1315952}]},{"id":1315950,"name":"昌平区","pid":131,"items":[{"id":1315950,"name":"全部昌平区","pid":1315950},{"id":1315953,"name":"回龙观","pid":1315950},{"id":1315955,"name":"昌平镇","pid":1315950},{"id":1315954,"name":"天通苑","pid":1315950},{"id":1317042,"name":"小汤山","pid":1315950},{"id":13123043,"name":"北七家","pid":1315950},{"id":13123044,"name":"沙河","pid":1315950},{"id":13123042,"name":"南口镇","pid":1315950}]},{"id":1315951,"name":"通州区","pid":131,"items":[{"id":1315951,"name":"全部通州区","pid":1315951},{"id":1315957,"name":"梨园","pid":1315951},{"id":1315958,"name":"新华大街","pid":1315951},{"id":13123045,"name":"通州北苑","pid":1315951},{"id":1317521,"name":"九棵树","pid":1315951},{"id":1315956,"name":"果园","pid":1315951},{"id":13125907,"name":"马驹桥","pid":1315951},{"id":13123990,"name":"武夷花园","pid":1315951},{"id":13164881,"name":"宋庄","pid":1315951},{"id":13164883,"name":"物资学院","pid":1315951},{"id":13164882,"name":"西集","pid":1315951},{"id":13170618,"name":"次渠","pid":1315951}]},{"id":131328,"name":"石景山区","pid":131,"items":[{"id":131328,"name":"全部石景山区","pid":131328},{"id":1311926,"name":"鲁谷","pid":131328},{"id":1311924,"name":"古城/八角","pid":131328},{"id":1311923,"name":"苹果园","pid":131328},{"id":1311927,"name":"石景山其它","pid":131328},{"id":1312882,"name":"模式口","pid":131328}]},{"id":1319158,"name":"顺义区","pid":131,"items":[{"id":1319158,"name":"全部顺义区","pid":1319158},{"id":13123041,"name":"顺义","pid":1319158},{"id":1312585,"name":"首都机场","pid":1319158},{"id":13164877,"name":"后沙峪","pid":1319158},{"id":13112016,"name":"国展","pid":1319158},{"id":13164878,"name":"马坡牛栏山","pid":1319158},{"id":13164880,"name":"石园","pid":1319158},{"id":13164879,"name":"南彩","pid":1319158}]},{"id":13127615,"name":"怀柔区","pid":131,"items":[{"id":13127615,"name":"全部怀柔区","pid":13127615},{"id":13164866,"name":"雁栖开发区","pid":13127615},{"id":13164947,"name":"渤海镇/慕田峪长城","pid":13127615},{"id":13164858,"name":"商业街","pid":13127615},{"id":13164859,"name":"京北大世界","pid":13127615},{"id":13164860,"name":"斜街","pid":13127615},{"id":13164865,"name":"桥梓镇","pid":13127615},{"id":13164862,"name":"东关","pid":13127615},{"id":13164861,"name":"下园","pid":13127615},{"id":13164863,"name":"富乐大街","pid":13127615},{"id":13164864,"name":"庙城","pid":13127615}]},{"id":1319157,"name":"房山区","pid":131,"items":[{"id":1319157,"name":"全部房山区","pid":1319157},{"id":13112011,"name":"良乡","pid":1319157},{"id":13167342,"name":"城关镇","pid":1319157},{"id":13130781,"name":"长阳镇","pid":1319157},{"id":13167346,"name":"窦店镇","pid":1319157},{"id":13167376,"name":"十渡镇","pid":1319157},{"id":13167350,"name":"燕山","pid":1319157},{"id":13167349,"name":"阎村镇","pid":1319157},{"id":13167384,"name":"青龙湖镇","pid":1319157},{"id":13167374,"name":"河北镇","pid":1319157}]},{"id":13127614,"name":"门头沟区","pid":131,"items":[{"id":13127614,"name":"全部门头沟区","pid":13127614}]},{"id":131434,"name":"密云区","pid":131,"items":[{"id":131434,"name":"全部密云区","pid":131434},{"id":13165439,"name":"密云镇","pid":131434},{"id":13165445,"name":"溪翁庄镇","pid":131434},{"id":13165441,"name":"十里堡镇","pid":131434},{"id":13165435,"name":"古北口镇","pid":131434},{"id":13165442,"name":"石城镇","pid":131434},{"id":13165443,"name":"太师屯镇","pid":131434},{"id":13165440,"name":"穆家峪镇","pid":131434},{"id":13165446,"name":"新城子镇","pid":131434},{"id":13165430,"name":"不老屯镇","pid":131434},{"id":13165436,"name":"河南寨镇","pid":131434},{"id":13165431,"name":"大城子镇","pid":131434},{"id":13165444,"name":"西田各庄镇","pid":131434},{"id":13165429,"name":"北庄镇","pid":131434},{"id":13165437,"name":"巨各庄镇","pid":131434},{"id":13165434,"name":"高岭镇","pid":131434},{"id":13165438,"name":"经济开发区","pid":131434},{"id":13165433,"name":"冯家峪镇","pid":131434},{"id":13165432,"name":"东邵渠镇","pid":131434},{"id":13127617,"name":"密云县其他","pid":131434}]},{"id":13127616,"name":"平谷区","pid":131,"items":[{"id":13127616,"name":"全部平谷区","pid":13127616}]},{"id":131435,"name":"延庆区","pid":131,"items":[{"id":131435,"name":"全部延庆区","pid":131435},{"id":13165454,"name":"千家店镇","pid":131435},{"id":13165450,"name":"井庄镇","pid":131435},{"id":13165451,"name":"旧县镇","pid":131435},{"id":13165458,"name":"延庆镇","pid":131435},{"id":13165460,"name":"张山营镇","pid":131435},{"id":13165447,"name":"八达岭镇","pid":131435},{"id":13165452,"name":"康庄镇","pid":131435},{"id":13165459,"name":"永宁镇","pid":131435},{"id":13165449,"name":"大庄科乡","pid":131435},{"id":13165455,"name":"沈家营镇","pid":131435},{"id":13165448,"name":"大榆树镇","pid":131435},{"id":13165461,"name":"珍珠泉乡","pid":131435},{"id":13165453,"name":"刘斌堡乡","pid":131435},{"id":13165456,"name":"四海镇","pid":131435},{"id":13127618,"name":"延庆县其他","pid":131435}]}]
         */

        private int id;
        private String name;
        private List<ItemsBeanX> items;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ItemsBeanX> getItems() {
            return items;
        }

        public void setItems(List<ItemsBeanX> items) {
            this.items = items;
        }

        public static class ItemsBeanX {
            /**
             * id : 0
             * name : 热门商圈
             * pid : 131
             * items : [{"id":131,"name":"全部商圈","pid":131},{"id":1311465,"name":"建外大街","pid":13114},{"id":1311466,"name":"朝外大街","pid":13114},{"id":1311467,"name":"朝阳公园/团结湖","pid":13114},{"id":1311469,"name":"亮马桥/三元桥","pid":13114},{"id":1311470,"name":"亚运村","pid":13114},{"id":1311471,"name":"望京","pid":13114},{"id":1311472,"name":"劲松/潘家园","pid":13114},{"id":1312078,"name":"大望路","pid":13114},{"id":1312578,"name":"国贸","pid":13114},{"id":1312579,"name":"双井","pid":13114},{"id":1312580,"name":"三里屯","pid":13114},{"id":1312583,"name":"酒仙桥","pid":13114},{"id":1312871,"name":"十里堡","pid":13114},{"id":13122998,"name":"青年路","pid":13114},{"id":1311488,"name":"中关村","pid":13117},{"id":1311489,"name":"五道口","pid":13117},{"id":1311493,"name":"公主坟/万寿路","pid":13117},{"id":1312588,"name":"五棵松","pid":13117},{"id":1311475,"name":"王府井/东单","pid":13115},{"id":1311481,"name":"西单","pid":13116}]
             */

            private int id;
            private String name;
            private int pid;
            private List<ItemsBean> items;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getPid() {
                return pid;
            }

            public void setPid(int pid) {
                this.pid = pid;
            }

            public List<ItemsBean> getItems() {
                return items;
            }

            public void setItems(List<ItemsBean> items) {
                this.items = items;
            }

            public static class ItemsBean {
                /**
                 * id : 131
                 * name : 全部商圈
                 * pid : 131
                 */

                private int id;
                private String name;
                private int pid;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getPid() {
                    return pid;
                }

                public void setPid(int pid) {
                    this.pid = pid;
                }
            }
        }
    }

    public static class NearbyConfigBean {
        /**
         * id : -1000
         * name : 附近
         * items : [{"id":500,"name":"0.5km"},{"id":1000,"name":"1km"},{"id":2000,"name":"2km"},{"id":5000,"name":"5km"}]
         */

        private String id;
        private String name;
        private List<ItemsBeanXX> items;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ItemsBeanXX> getItems() {
            return items;
        }

        public void setItems(List<ItemsBeanXX> items) {
            this.items = items;
        }

        public static class ItemsBeanXX {
            /**
             * id : 500
             * name : 0.5km
             */

            private int id;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

    public static class ListBean implements Serializable {
        /**
         * business_id : 23631327
         * photo_urls : http://qcloud.dpfile.com/pc/gPP3IBbhIezXx59dOW6fzfCqkd-r9UWX_wqNLbKW894dvtM1-my-fcr4D_rZXr3htOnd3gXQdDYlAqlaVaAFeZ0rYYyiRo_EhzufqWWjTjs.jpg
         * s_photo_urls : http://qcloud.dpfile.com/pc/gPP3IBbhIezXx59dOW6fzfCqkd-r9UWX_wqNLbKW894dvtM1-my-fcr4D_rZXr3htOnd3gXQdDYlAqlaVaAFeZ0rYYyiRo_EhzufqWWjTjs.jpg
         * comments : 8486
         * name : tomacado花厨(三里屯店)
         * avg_rating : 5
         * avg_price : 181
         * telephone : 18610023354
         * address : 东三环北路27号嘉铭中心B1
         * lng : 116.45414
         * lat : 39.927055
         * review_all_url : https://m.dianping.com/shop/23631327/review_all
         */

        private String business_id;
        private String photo_urls;
        private String s_photo_urls;
        private int comments;
        private String name;
        private Double avg_rating;
        private String avg_price;
        private String telephone;
        private String address;
        private double lng;
        private double lat;
        private String review_all_url;
        private String city_id;
        private String categories;

        public String getBusiness_id() {
            return business_id;
        }

        public void setBusiness_id(String business_id) {
            this.business_id = business_id;
        }

        public String getPhoto_urls() {
            return photo_urls;
        }

        public void setPhoto_urls(String photo_urls) {
            this.photo_urls = photo_urls;
        }

        public String getS_photo_urls() {
            return s_photo_urls;
        }

        public void setS_photo_urls(String s_photo_urls) {
            this.s_photo_urls = s_photo_urls;
        }

        public int getComments() {
            return comments;
        }

        public void setComments(int comments) {
            this.comments = comments;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getAvg_rating() {
            return avg_rating;
        }

        public void setAvg_rating(double avg_rating) {
            this.avg_rating = avg_rating;
        }

        public String getAvg_price() {
            return avg_price;
        }

        public void setAvg_price(String avg_price) {
            this.avg_price = avg_price;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public String getReview_all_url() {
            return review_all_url;
        }

        public void setReview_all_url(String review_all_url) {
            this.review_all_url = review_all_url;
        }

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public String getCategories() {
            return categories;
        }

        public void setCategories(String categories) {
            this.categories = categories;
        }
    }

    public static class SortConfigBean {
        /**
         * id : 1
         * name : 智能排序
         */

        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
