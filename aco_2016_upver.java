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
    int TASK[],LAYER[],MACHINE_SIZE[],SPEED[];  //machine_sizeはDouble型だったがintに変更
    int TASK_SIZE[][],TASK_VOLUME[][],F_TASK[][];

    try{
      while((str == br.readLine()) != null){
        switch(str){
          case "RUN":
            RUN       = Integer.parseInt(br.readLine());//br1
            break;
          case "ANT":
            ANT       = Integer.parseInt(br.readLine());//br1
            break;
          case "SEDAI":
            SEDAI      = Integer.parseInt(br.readLine());//br1
            break;
          case "JOB":
            JOB       = Integer.parseInt(br.readLine());//削除
            break;
          case "LAYER_MAX":
            LAYER_MAX = Integer.parseInt(br.readLine());//
            break;
          case "TASK_MAX":
            TASK_MAX  = Integer.parseInt(br.readLine());//削除
            break;
          case "MACHINE":
            MACHINE   = Integer.parseInt(br.readLine());//br1 or br2
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

      //TASK          = new int[JOB];
      TASK_SIZE     = new int[JOB][TASK_MAX];
      TASK_VOLUME   = new int[JOB][TASK_MAX];
      LAYER         = new int[JOB];
      F_TASK        = new int[JOB][LAYER_MAX+1];
      MACHINE_SIZE  = new int[MACHINE];
      SPEED         = new int[MACHINE];

      //初期化
      TASK_MAX = 0;

      while((str = br2.readLine()) != null){
        switch(str){
          case "TASK":
            String[] temp = br2.readLine().split(",",0);
            TASK = new int[temp.length];
            JOB = temp.length;
            for(job_i=0;job_i<JOB;job_i++){
              TASK[job_i] = Integer.parseInt(temp[job_i]);
              TASK_MAX += TASK[job_i];
            }
            //確認用println
            System.out.println("TASK_MAX = " + );
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

    double syori_pheromon[][][]   = new double[JOB][MACHINE][TASK_MAX];
    double haichi_pheromon[][][]  = new double[JOB][TASK_MAX];
    double machine_pheromon[][][] = new double[JOB][MACHINE][TASK_MAX];

    //どうしてdouble[JOB][TASK_MAX][TASK_MAX]なのだろう
    double syori_prob   = new double[JOB][TASK_MAX][TASK_MAX];
    double haichi_prob  = new double[JOB][TASK_MAX];
    double machine_prob = new double[JOB][MACHINE][TASK_MAX];

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

      double best_min_time = SEDAI;//ベスト戦略のための変数
      int layer_endtime[]    = new int[JOB];
      int layer_number[]     = new int[JOB];
      int sigma[]            = new int[JOB];
      int machine_endtime[]  = new int[MACHINE];
      

      //外部出力ファイルオープン
      PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(filename+filename_ext)));
      for(int sedai_i=0;sedai_i<SEDAI;sedai_i++){

        int ant_i,task_j;
        int syori_select[][][][]   = new int[ANT][JOB][TASK_MAX][TASK_MAX];
        int haichi_select[][]      = new int[ANT][JOB];
        int machine_select[][][]   = new int[ANT][JOB][TASK_MAX];//selsect_machine -> machine_select
        int job_select[][]         = new int[ANT][TASK_MAX];
        int task_select[][][]      = new int[ANT][JOB][TASK_MAX];

        int haichi_num_task[] = new int[JOB];
        int haichi_job[][]    = new int[ANT][TASK_MAX];
        int haichi_task[][]   = new int[ANT][TASK_MAX];



        //選択行列の初期化？？？？ 処理関係の初期化という認識で合ってるのか
        for(ant_i=0;ant_i<ANT;ant_i++){
          //処理セレクト配列の初期化
          for(job_i=0;job_i<JOB;job_i++){
            for(task_i=0;task_i<TASK;task_i++){
              for(task_j=0;task_j<TASK;task_j++){
                syori_select[ant_i][job_i][task_i][task_j] = 0;
              }
            }

            //layerjobは一時的変数
            int layerjob            = LAYER[job_i];
            F_TASK[job_i][0]        = 0;
            F_TASK[job_i][layerjob] = TASK[job_i];

            //制約条件の定義？()
            for(layer_i=0;layer_i<LAYER[job_i];layer_i++){
              for(task_i=F_TASK[job_i][layer_i];task_i<F_TASK[job_i][layer_i+1];task_i++){
                for(task_j=F_TASK[job_i][layer_i];task_j<F_TASK[job_i][layer_i+1];task_j++){
                  syori_select[ant_i][job_i][task_i][task_j] = 1;
                }
              }
            }
          }

          //↓の変数ってなんだっけ
          int haichi_job_select[][][] = new int[ANT][JOB][TASK_MAX];

          for(job_i=0;job_i<JOB;job_i++){
            for(haichi_i=0;haichi<TASK_MAX;haichi_i++){
              haichi_job_select[ant_i][job_i][haichi_i] = 1;
            }
          }
        }

        int task_list[][] = new int [JOB][TASK_MAX];

        //処理、配置、マシーンの決定
        for(ant_i=0;ant_i<ANT;ant_i++){

          //処理順の決定
          for(job_i=0;job_i<JOB;job++){
            for(syori_i=0;syori_i<TASK[job_i];syori_i++){

              double sum = 0.0;
              for(task_i=0;task_i<TASK[job_i];task_i++){
                sum += syori_pheromon[job_i][syori_i][task_i] * syori_select[ant_i][job_i][syori_i][task_i];
              }
              for(task_i=0;task_i<TASK[job_i];task_i++){
                syori_prob[job_i][syori_i][task_i] = syori_pheromon[job_i][syori_i][task_i]
                                                   * syori_select[ant_i][job_i][syori_i][task_i] / sum;
              }

              double rand   = Math.random();
              double count  = 0.0;
              for(task_i=0;<TASK[job_i];task_i){
                count += syori_prob[job_i][syori_i][task_i];
                if(count>rand) break;
              }
              select_task[ant_i][job_i][syori_i]  = task_i;
              task_list[job_i][syori_i]           = task_i;

              for(task_j=syori_i;task_j<TASK[job_i];task_j){
                syori_select[ant_i][job_i][task_i][task_j] = 0;
              }
            }
          }
          //処理順決定終了


          //配置決定
          for(job_i=0;job_i<JOB;job_i++){
            haichi_select[ant_i][job_i] = 1;
            haichi_num_task[job_i] = 0;
          }
          for(haichi_i=0;haichi_i<TASK_MAX;haichi_i++){
            
            double sum = 0.0;
            for(job_i=0;job_i<JOB;job_i++){
              sum += haichi_pheromon[job_i][haichi_i] * haichi_select[ant_i][job_i];
            }
            for(job_i=0;job_i<JOB;job_i++){
              haichi_prob[job_i][haichi_i] = haichi_pheromon[job_i][haichi_i] * haichi_select[ant_i][job_i] / sum;
            }

            double rand = Math.random();
            double count = 0.0;
            for(job_i=0;job_i<JOB;job_i){
              count += haichi_prob[job_i][haichi_i];
              if(count>rand)break;//ここでjob_iのループを強制的に終了させ、そのjob_iを下で使う 
            }

            job_select[ant_i][haichi_i] = job_i;

            int temp = haichi_num_task[job_i];
            hichi_task[ant_i][haichi_i] = task_list[job_i][temp];
            haichi_job[ant_i][haichi_i] = job_i;
            haichi_num_task[job_i]++;
            if(haichi_num_task[job_i] >= TASK[job_i]){
              for(int i=haichi_i;i<TASK_MAX;i++){
                haichi_job_select[ant_i][job_i][i] = 0;
              }
              haichi_select[ant_i][job_i] = 0;
            }
          }
          //配置終了


          //マシーン割当
          for(job_i=0;job_i<JOB;job_i++){
            for(task_i=0;task_i<TASK[job_i];task_i++){
              double sum = 0.0;
             for(machine_i=0;machine_i<MACHINE;machine_i++){
                sum += machine_pheromon[job_i][task_i][machine_i];
              }
              for(machine_i=0;machine_i<MACHINE;machine_i++){
                machine_prob[job_i][task_i][machine_i] = machine_pheromon[job_i][task_i][machine_i] / sum;
              }
              double rand = Math.random();
              double count = 0.0;
              int machine_j;
              for(machine_j=0;machine_j<MACHINE;machine_j++){
                count += machine_prob[job_i][task_i][machine_j];
                if(count>r)break;
              }
              machine_select[ant_i][job_i][task_i] = machine_j;
            }
          }

          int haichi_machine[][] = new int[ANT][TASK_MAX];

          //haihi_machineの代入
          for(haichi_i=0;haichi_i<TASK_MAX;haichi_i++){
            int temp_task = haichi_taks[ant_i][haichi_i];
            int temp_job  = haihi_job[ant_i][haichi_i];
            haichi_machine[ant_i][haichi_i] = machine_selecta[ant_i][temp_job][temp_task]; 
          }
          //マシーン割当終了
          //処理、配置、マシーンの決定終了


          //ガントチャート開始
          //必要配列の初期化
          for(job_i=0;job_i<JOB;job_i++){
            layer_endtime[job_i]    = -1;
            layer_number[job_i]     = 0;
            haichi_num_task[job_i]  = 0;
            sigma[job_i]            = 0;
          }
          for(machie_i=0;machie_i<MACHINE;machie_i++){

          }

        }//(antループ終了)
        
      }//SEDAILOOP

    }//RUN END

  }
}
