package com.heboot.bean.rank;

import com.heboot.base.BaseBeanEntity;
import com.heboot.entity.User;

import java.util.List;

public class RankBean extends BaseBeanEntity {

    private String rank_title;

    private List<User> week;
    private List<User> month;
    private List<User> day;

    public String getRank_title() {
        return rank_title;
    }

    public void setRank_title(String rank_title) {
        this.rank_title = rank_title;
    }

    public List<User> getWeek() {
        return week;
    }

    public void setWeek(List<User> week) {
        this.week = week;
    }

    public List<User> getMonth() {
        return month;
    }

    public void setMonth(List<User> month) {
        this.month = month;
    }

    public List<User> getDay() {
        return day;
    }

    public void setDay(List<User> day) {
        this.day = day;
    }
}
