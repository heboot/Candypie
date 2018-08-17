package com.heboot.bean.config;

/**
 * Created by heboot on 2018/2/8.
 */

public class StartPageConfigEntity {
    /**
     * id : 1
     * title : 电影《闪光少女》搞笑艺考预热暑期档 安乐影业加持打造下一站天后
     * img : http://127.0.0.1/candypie/public/uploads/images/20180208/9d3c045e2af93fc8858f30f74fe8d2b4.jpg
     * second : 3
     * link : http://www.yihaodache.com
     * start_time : 1518019200
     * end_time : 1519920000
     */

    private int id;
    private String title;
    private String img;
    private int second;
    private String link;
    private long start_time;
    private long end_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }
}
