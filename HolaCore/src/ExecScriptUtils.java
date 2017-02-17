import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import static jdk.nashorn.internal.objects.Global.print;

/**
 * Created by betsy on 2/17/17.
 */
public class ExecScriptUtils {

    /**
     * exec task script.
     */
    public static void exec(String cmd){
        Runtime run = Runtime.getRuntime();//返回与当前 Java 应用程序相关的运行时对象
        try {
            Process p = run.exec(cmd);// 启动另一个进程来执行命令
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
            String lineStr;
            while ((lineStr = inBr.readLine()) != null)
                //获得命令执行后在控制台的输出信息
                System.out.println(lineStr);// 打印输出信息
            //检查命令是否执行失败。
            if (p.waitFor() != 0) {
                if (p.exitValue() == 1)//p.exitValue()==0表示正常结束，1：非正常结束
                    throw new ExceptionInInitializerError("freeline failed when read project info with script: {}'.format(args)");
            }else{
                System.out.println("freeline init success");
            }
            inBr.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}