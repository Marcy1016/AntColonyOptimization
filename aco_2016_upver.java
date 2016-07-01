import java.util.*;
import java.awt.*;
import java.io.*;

public class aco_2016_upver {
  public static void main(String args[]) throws IOException{
  
  // ファイル読み込みのための宣言、代入
    br = new BufferdReader(new FileReader(args[0]));
    br1 = new BufferdReader(new FileReader(args[1]));
    br2 = new BufferdReader(new FileReader(args[2]));
    
    String str;
    int INT;
    int
    
    try{
      while((str == br.readLine()) != null){
        switch(str){
          case "ANT":
            ANT= Integer.parseInt(br.readLine());
            System.out.println(ANT);
            break;
          case "K":
            K = Integer.parseInt(br.readLine());
            System.out.println(K);
            break;
          case "JOB":
            JOB = Integer.parseInt(br.readLine());
            System.out.println(JOB);
            break;
          case "LAYER_MAX":
            LAYER_MAX = Integer.parseInt(br.readLine());
            System.out.println(LAYER_MAX);
            break;
          case "TASK_MAX":
            TASK_MAX = Integer.parseInt(br.readLine());
            System.out.println(TASK_MAX);
            break;
          case "MACHINE":
            MACHINE = Integer.parseInt(br.readLine());
            System.out.println(MACHINE);
            break;
          case "U_ub":
            U_ub = Integer.parseInt(br.readLine());
            System.out.println(U_ub);
            break;
        }
      }
    }
  }
}
