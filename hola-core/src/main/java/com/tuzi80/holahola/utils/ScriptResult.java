package com.tuzi80.holahola.utils;

/**
 * Exec script result.
 * Created by betsy on 2/27/17.
 */
public class ScriptResult {
    private int code;
    private String output;
    private String err;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }
}
