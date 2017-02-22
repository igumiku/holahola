package com.tuzi80.holahola.model;

/**
 * Preference.
 * Created by betsy on 2/21/17.
 */
public class Preference {
    private HolaProjectDescription mProjectDescription;
    private static Preference mInstance=new Preference();

    public static Preference getmInstance() {
        return mInstance;
    }

    public static void setmInstance(Preference mInstance) {
        Preference.mInstance = mInstance;
    }

    public HolaProjectDescription getmProjectDescription() {
        return mProjectDescription;
    }

    public void setmProjectDescription(HolaProjectDescription mProjectDescription) {
        this.mProjectDescription = mProjectDescription;
    }
}
