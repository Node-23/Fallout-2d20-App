package com.rubick.falloutrpgapp.Model;

import java.util.ArrayList;

public class UserData {

    private ArrayList<Atribute> UserAttributes;
    private boolean soundOn;

    public UserData(){

    }

    public UserData(ArrayList<Atribute> userAttributes, boolean soundOn) {
        UserAttributes = userAttributes;
        this.soundOn = soundOn;
    }

    public ArrayList<Atribute> getUserAttributes() {
        return UserAttributes;
    }

    public void setUserAttributes(ArrayList<Atribute> userAttributes) {
        UserAttributes = userAttributes;
    }

    public boolean isSoundOn() {
        return soundOn;
    }

    public void setSoundOn(boolean soundOn) {
        this.soundOn = soundOn;
    }
}
