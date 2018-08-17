package com.heboot.bean.luckypan;

import com.heboot.base.BaseBeanEntity;

import java.util.List;

public class TurntableConfigChildBean   {


    /**
     * user_turntable_id : 1000
     * status : 0
     * status_message : 发起
     * items : [{"type":"gift","goods_id":2,"radian":72,"goods":{"id":2,"img":"http://127.0.0.1/candypie/public/static/admin/img/none.png","price":500,"title":"得到"}},{"type":"action","goods_id":2,"radian":36,"goods":{"id":2,"img":"http://127.0.0.1/candypie/public/uploads/images/20180511/6105b3937edf8daee6fa8ac106955f15.png","title":"真心话"}},{"type":"gift","goods_id":3,"radian":72,"goods":{"id":3,"img":"http://127.0.0.1/candypie/public/static/admin/img/none.png","price":800,"title":"得到"}},{"type":"action","goods_id":3,"radian":36,"goods":{"id":3,"title":"你要 唱一首歌"}},{"type":"gift","goods_id":7,"radian":72,"goods":{"id":7,"img":"http://127.0.0.1/candypie/public/static/admin/img/none.png","price":4500,"title":"得到"}},{"type":"action","goods_id":1,"radian":72,"goods":{"id":1,"title":"你来 做妩媚动作"}}]
     */

    private String user_turntable_id;
    private int status;
    private String status_message;
    private List<ItemsBean> items;

    public String getUser_turntable_id() {
        return user_turntable_id;
    }

    public void setUser_turntable_id(String user_turntable_id) {
        this.user_turntable_id = user_turntable_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * type : gift
         * goods_id : 2
         * radian : 72
         * goods : {"id":2,"img":"http://127.0.0.1/candypie/public/static/admin/img/none.png","price":500,"title":"得到"}
         */

        private String type;
        private int goods_id;
        private int radian;
        private GoodsBean goods;


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(int goods_id) {
            this.goods_id = goods_id;
        }

        public int getRadian() {
            return radian;
        }

        public void setRadian(int radian) {
            this.radian = radian;
        }

        public GoodsBean getGoods() {
            return goods;
        }

        public void setGoods(GoodsBean goods) {
            this.goods = goods;
        }

        public static class GoodsBean {
            /**
             * id : 2
             * img : http://127.0.0.1/candypie/public/static/admin/img/none.png
             * price : 500
             * title : 得到
             */

            private int id;
            private String img;
            private int price;
            private String title;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
