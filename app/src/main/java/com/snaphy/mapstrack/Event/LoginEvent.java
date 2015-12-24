package com.snaphy.mapstrack.Event;

/**
 * Created by Ravi-Gupta on 12/23/2015.
 */
public class LoginEvent {
    private final boolean isLogin;

    public LoginEvent(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public boolean isLogin() {
        return isLogin;
    }
}
