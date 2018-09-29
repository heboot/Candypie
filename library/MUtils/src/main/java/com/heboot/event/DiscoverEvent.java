package com.heboot.event;

import android.support.v4.app.Fragment;
import android.view.View;

import com.heboot.entity.User;

import java.util.List;

public class DiscoverEvent {

    public static final String DISCOVER_TO_VIDEOPAGE_EVENT = "DISCOVER_TO_VIDEOPAGE_EVENT";

    public static final String DISCOVER_TO_USERINFO_EVENT = "DISCOVER_TO_USERINFO_EVENT";

    public static final String DISCOVER_PAUSE_PLAY_EVENT = "DISCOVER_PAUSE_PLAY_EVENT";

    public static final String DISCOVER_PAUSE_PLAY_EVENT_VIDEOS = "DISCOVER_PAUSE_PLAY_EVENT_VIDEOS";

    public static final String DISCOVER_PAUSE_PLAY_EVENT_BY_USER_VIDEOS = "DISCOVER_PAUSE_PLAY_EVENT_BY_USER_VIDEOS";

    public static final String DISCOVER_PAUSE_PLAY_EVENT_BY_HOMEOTHER_USER_VIDEOS = "DISCOVER_PAUSE_PLAY_EVENT_BY_HOMEOTHER_USER_VIDEOS";

    public static class DiscoverUpdateUserEvent {
        private User user;

        public DiscoverUpdateUserEvent(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }

    public static class DiscoverNextpageEvent {
        private User user;
        private View v;
        private Fragment fragment;

        public Fragment getFragment() {
            return fragment;
        }

        public DiscoverNextpageEvent(User user, Fragment fragment) {
            this.user = user;
            this.fragment = fragment;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public View getV() {
            return v;
        }

        public void setV(View v) {
            this.v = v;
        }

        public DiscoverNextpageEvent(User user, View v) {
            this.user = user;
            this.v = v;
        }
    }

    public static class DiscoverUpdatePositionEvent {
        private int position;

        public DiscoverUpdatePositionEvent(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }
    }

    public static class DiscoverUpdateDatasEvent {

        private List<User> usres;

        private int position;

        private int currentSp;

        public DiscoverUpdateDatasEvent(List<User> usres, int po, int sp) {
            this.usres = usres;
            this.position = po;
            this.currentSp = sp;
        }

        public int getCurrentSp() {
            return currentSp;
        }

        public List<User> getUsres() {
            return usres;
        }

        public int getPosition() {
            return position;
        }
    }


}
