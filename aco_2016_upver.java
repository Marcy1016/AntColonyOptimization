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
    int RUN,ANT,SEDAI,JOB,LAYER_MAX,TASK_MAX,MACHINE;

    int job_i,task_i,layer_i,machine_i;
    
    Double  INITIAL_PHEROMON,PARAMETER_A,PARAMETER_B,
            EVAPO_SYORI,EVAPO_HAICHI,EVAPO_MACHINE;

    //try文の内で使用するので、try文の外にて宣言 
    int[] TASK,LAYER,MACHINE_SIZE,SPEED;  //machine_sizeはDouble型だったがintに変更
    int[][] TASK_SIZE[][],TASK_VOLUME[][],F_TASK[][];

    try{
      while((str == br.readLine()) != null){
        switch(str){
          case "RUN":
            RUN       = Integer.parseInt(br.readLine());
            break;
          case "ANT":
            ANT       = Integer.parseInt(br.readLine());
            break;
          case "SEDAI":
            SEDAI      = Integer.parseInt(br.readLine());
            break;
          case "JOB":
            JOB       = Integer.parseInt(br.readLine());
            break;
          case "LAYER_MAX":
            LAYER_MAX = Integer.parseInt(br.readLine());
            break;
          case "TASK_MAX":
            TASK_MAX  = Integer.parseInt(br.readLine());
            break;
          case "MACHINE":
            MACHINE   = Integer.parseInt(br.readLine());
            break;
            /*
          case "U_ub":
            U_UB      = Integer.parseInt(br.readLine());
            break;
            */
        }
      }

      while((str _ br1.readLine()) != null){
        switch(str){
          case "INITIAL_PHEROMON":
            INITIAL_PHEROMON  = Double.parseDouble(br1.readLine());
            break;
          case "PARAMETER_A":
            PARAMETER_A       = Double.parseDouble(br1.readLine());
            break;
          case "PARAMETER_B":
            PARAMETER_B       = Double.parseDouble(br1.readLine());
            break;
          case "EVAPO_SYORI":
            EVAPO_SYORI       = Double.parseDouble(br1.readLine());
            break;
          case "EVAPO_HAICHI":
            EVAPO_HAICHI      = Double.parseDouble(br1.readLine());
            break;
          case "EVAPO_MACHINE":
            EVAPO_MACHINE     = Double.parseDouble(br1.readLine());
            break;
        }
      }

      TASK          = new int[JOB];
      TASK_SIZE     = new int[JOB][TASK_MAX];
      TASK_VOLUME   = new int[JOB][TASK_MAX];
      LAYER         = new int[JOB];
      F_TASK        = new int[JOB][LAYER_MAX+1];
      MACHINE_SIZE  = new int[MACHINE];
      SPEED         = new int[MACHINE];

      while((str = br2.readLine()) != null){
        case "TASK":
        String[] temp = br2.readLine().split(",",0);
        TASK = new int[temp.length];
        JOB = temp.length;
          for(job_i=0;job_i<JOB;job_i++){
            TASK[job_i] = Integer.parseInt(temp[job_i]);
            TASK_MAX += TASK[job_i];
          }
          break;
        case "TASK_SIZE":
        TASK_SIZE = new int[JOB][];
          for(job_i=0;job_i<JOB;job_i++){
            temp = br2.readLine().split(",",0);
            TASK_SIZE[job_i] = new int[temp.length];
            for(task_i=0;task_i<TASK[job_i];task_i++){
              TASK_SIZE[job_i][task_i] = Integer.parseInt(temp[task_i]);
            }
          }
          break;
        case "TASK_VOLUME":
          for(job_i=0;job_i<JOB;job_i++){
            for(task_i=0;task_i<TASK[job_i];task_i++){
              TASK_VOLUME[job_i][task_i] = Integer.parseInt(br2.readLine());
            }
          }
          break;
        case "LAYER":
          for(job_i=0;job_i<JOB;job_i++){
            LAYER[job_i] = Integer.parseInt(br2.readLine());
          }
          break;
        case "F_TASK":
          for(job_i=0;job_i<JOB;job_i++){
            for(layer_i=0;layer_i<LAYER_MAX;layer_i++){
              F_TASK[job_i][layer_i] = Integer.parseInt(br2.readLine());
            }
          }
          break;
        case "MACHINE_SIZE":
          for(machine_i=0;machine_i<MACHINE;machine_i++){
            MACHINE_SIZE[machine_i] = Integer.parseInt(br2.readLine());
          }
          break;
        case "SPEED":
          for(machine_i=0;machine_i<MACHINE;machine_i++){
            SPEED[machine_i] = Integer.parseInt(br2.readLine());
          }
          break;
      }
    }

    finally{
      br.close();
      br1.close();
      br2.close();
    }

    //外部出力のファイル名、拡張子の宣言・設定
    String filename     = "result_upver("+args[0].replace(".txt", "_")
                                         +args[1].replace(".txt", "_")
                                         +args[2].replace(".txt", ")");
    String filename_ext = ".csv";


    int run_i,syori_i,haichi_i;

    double[][][] syori_pheromon   = new double[JOB][MACHINE][TASK_MAX];
    double[][][] haichi_pheromon  = new double[JOB][TASK_MAX];
    double[][][] machine_pheromon = new double[JOB][MACHINE][TASK_MAX];

    //タスク総数の宣言・代入 
    int total_task = 0;
    for(job_i=0;job_i<JOB;job_i++){
      total_task += TASK[job_i];
    }



    /*-*-*-*-試行回数のループ-*-*-*-*/
    for(run_i=0;run_i<RUN;run_i++){
      //処理順ノードの初期化
      for(job_i=0;job_i<JOB;job_i++){
        for(syori_i;syori_i<TASK[job_i];syori_i++){
          for(task_i=0;task_i<TASK[job_i];task_i++){
            syori_pheromon[task_i][syori_i][job_i] = INITIAL_PHEROMON;
          }
        }
      }

      //配置順ノードの初期化
      for(job_i=0;job_i<JOB;job_i++){
        for(haichi_i=0;haichi_i<total_task;haichi_i++){
          haichi_pheromon[job_i][haichi_i] = INITIAL_PHEROMON;
        }
      }

      //Machine割り当てノードの初期化
      for(job_i=0;job_i<JOB;job_i++){
        for(task_i=0;task_i<TASK;task_i){
          for(machine_i=0;machine_i<MACHINE;machine_i++){
            if(TASK_SIZE[task_i][job_i] <= MACHINE_SIZE[machine_i]){
              machine_pheromon[machine_i][task_i][job_i] = INITIAL_PHEROMON;
            }else{
              machine_pheromon[machine_i][task_i][job_i] = 0.0;
            }
          }
        }
      }

      double best_min_time = SEDAI;//ベスト戦略のための変数\
      

      PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(filename+filename_ext)));
      for(int sedai_i=0;sedai_i<SEDAI;sedai_i++){

        int hai
      }



    }//RUN END


  }
}
