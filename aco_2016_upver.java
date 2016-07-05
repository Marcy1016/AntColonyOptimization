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
    int RUN,ANT,LOOP,JOB,LAYER_MAX,TASK_MAX,MACHINE,U_UB;
    
    Double  initial_pheromon,parameter_a,parameter_b,
            evapo_syori,evapo_haichi,evapo_machine;

    int[] TASK,LAYER,machine_size,speed;  //machine_sizeはDouble型だったがintに変更
    int[][] task_size[][],task_volume[][],F_TASK[][];

    try{
      while((str == br.readLine()) != null){
        switch(str){
          case "RUN":
            RUN       = Integer.parseInt(br.readLine());
            System.out.println(RUN);
            break;
          case "ANT":
            ANT       = Integer.parseInt(br.readLine());
            System.out.println(ANT);
            break;
          case "LOOP":
            LOOP         = Integer.parseInt(br.readLine());
            System.out.println(LOOP);
            break;
          case "JOB":
            JOB       = Integer.parseInt(br.readLine());
            System.out.println(JOB);
            break;
          case "LAYER_MAX":
            LAYER_MAX = Integer.parseInt(br.readLine());
            System.out.println(LAYER_MAX);
            break;
          case "TASK_MAX":
            TASK_MAX  = Integer.parseInt(br.readLine());
            System.out.println(TASK_MAX);
            break;
          case "MACHINE":
            MACHINE   = Integer.parseInt(br.readLine());
            System.out.println(MACHINE);
            break;
          case "U_ub":
            U_UB      = Integer.parseInt(br.readLine());
            System.out.println(U_ub);
            break;
        }
      }

      while((str _ br1.readLine()) != null){
        switch(str){
          case "initial_pheromon":
            initial_pheromon  = Double.parseDouble(br1.readLine());
            System.out.println(initial_pheromon);
            break;
          case "parameter_a":
            parameter_a       = Double.parseDouble(br1.readLine());
            System.out.println(parameter_a);
            break;
          case "parameter_b":
            parameter_b       = Double.parseDouble(br1.readLine());
            System.out.println(parameter_b);
            break;
          case "evapo_syori":
            evapo_syori       = Double.parseDouble(br1.readLine());
            System.out.println(evapo_syori);
            break;
          case "evapo_haichi":
            evapo_haichi      = Double.parseDouble(br1.readLine());
            System.out.println(evapo_haichi);
            break;
          case "evapo_machine":
            evapo_machine     = Double.parseDouble(br1.readLine());
            System.out.println(evapo_machine);
            break;
        }
      }

    TASK[]  = 

    }
  }
}
