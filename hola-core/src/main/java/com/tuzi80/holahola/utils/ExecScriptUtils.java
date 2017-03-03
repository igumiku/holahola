package com.tuzi80.holahola.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import static jdk.nashorn.internal.objects.Global.print;

/**
 * Created by betsy on 2/17/17.
 */
public class ExecScriptUtils {

    /**
     * exec task script.
     */
    public static ScriptResult exec(String cmd) {
        StringBuilder res = new StringBuilder();
        Runtime run = Runtime.getRuntime();//返回与当前 Java 应用程序相关的运行时对象
        ScriptResult map = new ScriptResult();
        try {
            Process p = run.exec(cmd);// 启动另一个进程来执行命令
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
            String lineStr;
            while ((lineStr = inBr.readLine()) != null)
                res.append(lineStr);
            //检查命令是否执行失败。
            if (p.waitFor() != 0) {
                if (p.exitValue() == 1) {//p.exitValue()==0表示正常结束，1：非正常结束
                    //throw new ExceptionInInitializerError(String.format("freeline failed when read project info with script: %s", cmd));
                    map.setCode(1);
                    map.setErr("");
                    map.setOutput(res.toString());
                    return map;
                }
            } else {
                System.out.println(cmd + " success");
            }
            inBr.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.setCode(0);
        map.setErr("");
        map.setOutput(res.toString());
        return map;
    }

    public static ScriptResult exec(String[] cmds) {
        StringBuilder cmd = new StringBuilder();
        for (int i = 0; i < cmds.length; i++) {
            if (i == cmds.length - 1) {
                cmd.append(cmds[i]);
            } else {
                cmd.append(cmds[i]);
                cmd.append(" ");
            }
        }
        return exec(cmd.toString());
    }

}
