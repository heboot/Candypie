package com.heboot.bean.pay;

import java.util.List;

/**
 * Created by heboot on 2018/3/26.
 */

public class InComeListBean {


    /**
     * title : 2018/03
     * items : [{"title":"约会退款","amount":"200.00","create_time":1521716402},{"title":"约会退款","amount":"200.00","create_time":1521716222},{"title":"约会退款","amount":"200.00","create_time":1521716102},{"title":"约会退款","amount":"200.00","create_time":1521716041},{"title":"约会退款","amount":"200.00","create_time":1521714842},{"title":"发约付款-余额付款","amount":"-200.00","create_time":1521712114},{"title":"发约付款-余额付款","amount":"-200.00","create_time":1521711923},{"title":"发约付款-余额付款","amount":"-200.00","create_time":1521711755},{"title":"发约付款-余额付款","amount":"-200.00","create_time":1521711712},{"title":"发约付款-余额付款","amount":"-200.00","create_time":1521710402},{"title":"约会退款","amount":"20.00","create_time":1521709801},{"title":"发约付款-第三方付款","amount":"-200.00","create_time":1521708461},{"title":"发约付款-第三方付款","amount":"-20.00","create_time":1521708033},{"title":"视频消费返还","amount":"600.00","create_time":1521707821},{"title":"视频消费钻石","amount":"-600.00","create_time":1521707159},{"title":"视频消费返还","amount":"600.00","create_time":1521703682},{"title":"视频消费钻石","amount":"-600.00","create_time":1521702964},{"title":"视频消费钻石","amount":"-600.00","create_time":1521702767},{"title":"约会退款","amount":"200.00","create_time":1521702721},{"title":"发约付款-第三方付款","amount":"-200.00","create_time":1521702692},{"title":"约会退款","amount":"200.00","create_time":1521702602},{"title":"发约付款-第三方付款","amount":"-200.00","create_time":1521702562},{"title":"约会退款","amount":"200.00","create_time":1521702482},{"title":"发约付款-第三方付款","amount":"-200.00","create_time":1521702443},{"title":"约会退款","amount":"200.00","create_time":1521702061},{"title":"发约付款-第三方付款","amount":"-200.00","create_time":1521702014},{"title":"充值钻石-第三方付款","amount":"-1998.00","create_time":1521701533},{"title":"充值钻石","amount":"199800.00","create_time":1521701533},{"title":"约会退款","amount":"200.00","create_time":1521605882},{"title":"约会退款","amount":"200.00","create_time":1521605461},{"title":"约会退款","amount":"200.00","create_time":1521605282},{"title":"约会退款","amount":"200.00","create_time":1521605101},{"title":"约会退款","amount":"200.00","create_time":1521604981},{"title":"约会退款","amount":"200.00","create_time":1521604922},{"title":"约会退款","amount":"200.00","create_time":1521604861},{"title":"约会退款","amount":"200.00","create_time":1521604682},{"title":"约会退款","amount":"200.00","create_time":1521604562},{"title":"约会退款","amount":"200.00","create_time":1521604561},{"title":"约会退款","amount":"200.00","create_time":1521604501},{"title":"约会退款","amount":"20.00","create_time":1521603241},{"title":"约会退款","amount":"200.00","create_time":1521602890},{"title":"发约付款-第三方付款","amount":"-200.00","create_time":1521601960},{"title":"发约付款-第三方付款","amount":"-200.00","create_time":1521601569},{"title":"发约付款-第三方付款","amount":"-200.00","create_time":1521601172},{"title":"发约付款-第三方付款","amount":"-200.00","create_time":1521600920},{"title":"发约付款-第三方付款","amount":"-200.00","create_time":1521600736},{"title":"发约付款-第三方付款","amount":"-200.00","create_time":1521600603},{"title":"发约付款-第三方付款","amount":"-200.00","create_time":1521600599},{"title":"发约付款-第三方付款","amount":"-200.00","create_time":1521600540},{"title":"发约付款-第三方付款","amount":"-200.00","create_time":1521600306}]
     */

    private String title;
    private List<ItemsBean> items;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * title : 约会退款
         * amount : 200.00
         * create_time : 1521716402
         */

        private String title;
        private String amount;
        private String create_time;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}
