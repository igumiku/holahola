package com.tuzi80.holahola;

import com.tuzi80.holahola.Init;
import com.tuzi80.holahola.utils.CommonUtil;

/**
 * com.tuzi80.holahola.main.class
 * Created by betsy on 2/17/17.
 */
public class main {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        if (CommonUtil.isContain(args, "initHola")) {
            Init.init();
        }
    }
}
