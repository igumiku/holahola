/**
 * Created by betsy on 2/17/17.
 */
public class main {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        for (int i = 0; i < args.length; i++) {
            if(args[i].equals("initHola")){
                Init.init();
            }
        }
    }
}
