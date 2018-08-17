package com.heboot.bean.theme;

import com.heboot.base.BaseBeanEntity;
import com.heboot.entity.User;

import java.io.Serializable;

/**
 * Created by heboot on 2018/3/10.
 */

public class PostVideoChatBean implements Serializable {


    /**
     * to : 7
     * channel_key : 005AQAoAEJDNkREMENCRUIxRUEwNkVCNzAzQzQ5MUNDQUNCREU1M0RDMkU4MDkQAGV/OmHoS0dxtWiYOy+vCcatorRagdBiOwAAAAAAAA==
     * channel_name : cdp_5_7
     */

    private String channel_key;
    private String channel_name;
    private User user;

    private Integer min_time;
    private Integer coin;

    public String getChannel_key() {
        return channel_key;
    }

    public void setChannel_key(String channel_key) {
        this.channel_key = channel_key;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getMin_time() {
        return min_time;
    }

    public void setMin_time(Integer min_time) {
        this.min_time = min_time;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }
}
