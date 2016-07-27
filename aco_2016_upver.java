import java.util.*;
import java.awt.*;
import java.io.*;

public class aco_2016_upver {
  public static void main(String args[]) throws IOException{
  
    // ファイル読み込みのための宣言、代入
    br = new BufferdReader(new FileReader(args[0]));
    br1 = new BufferdReader(new FileReader(args[1]));
    
    String str;
    int RUN,ANT,SEDAI,JOB,LAYER_MAX,TASK_MAX,MACHINE;

    int job_i,task_i,layer_i,machine_i,sedai_i;
    
    Double  INITIAL_PHEROMON,PARAMETER_A,PARAMETER_B,
            EVAPO_SYORI,EVAPO_HAICHI,EVAPO_MACHINE;

    //try文の内で使用するので、try文の外にて宣言 
    int TASK[],LAYER[],MACHINE_SIZE[],SPEED[];  //machine_sizeはDouble型だったがintに変更
    int TASK_SIZE[][],TASK_VOLUME[][],F_TASK[][];

    try{
      while((str = br.readLine()) != null){
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
          case "LAYER_MAX":
            LAYER_MAX = Integer.parseInt(br.readLine());//br1
            break;
          case "MACHINE":
            MACHINE   = Integer.parseInt(br.readLine());//br1
            break;
          case "INITIAL_PHEROMON":
            INITIAL_PHEROMON  = Double.parseDouble(br.readLine());
            break;
          case "PARAMETER_A":
            PARAMETER_A       = Double.parseDouble(br.readLine());
            break;
          case "PARAMETER_B":
            PARAMETER_B       = Double.parseDouble(br.readLine());
            break;
          case "EVAPO_SYORI":
            EVAPO_SYORI       = Double.parseDouble(br.readLine());
            break;
          case "EVAPO_HAICHI":
            EVAPO_HAICHI      = Double.parseDouble(br.readLine());
            break;
          case "EVAPO_MACHINE":
            EVAPO_MACHINE     = Double.parseDouble(br.readLine());
            break;
        }
      }

      TASK_SIZE     = new int[JOB][TASK_MAX];
      TASK_VOLUME   = new int[JOB][TASK_MAX];
      LAYER         = new int[JOB];
      F_TASK        = new int[JOB][LAYER_MAX+1];
      MACHINE_SIZE  = new int[MACHINE];
      SPEED         = new int[MACHINE];

      //初期化
      TASK_MAX = 0;

      while((str = br1.readLine()) != null){
        switch(str){
          case "TASK":
            String[] temp = br1.readLine().split(",",0);
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
              temp = br1.readLine().split(",",0);
              TASK_SIZE[job_i] = new int[temp.length];
              for(task_i=0;task_i<TASK[job_i];task_i++){
                TASK_SIZE[job_i][task_i] = Integer.parseInt(temp[task_i]);
              }
            }
            break;
          case "TASK_VOLUME":
            for(job_i=0;job_i<JOB;job_i++){
              for(task_i=0;task_i<TASK[job_i];task_i++){
                TASK_VOLUME[job_i][task_i] = Integer.parseInt(br1.readLine());
              }
            }
            break;
          case "LAYER":
            for(job_i=0;job_i<JOB;job_i++){
              LAYER[job_i] = Integer.parseInt(br1.readLine());
            }
            break;
          case "F_TASK":
            for(job_i=0;job_i<JOB;job_i++){
              for(layer_i=0;layer_i<LAYER_MAX;layer_i++){
                F_TASK[job_i][layer_i] = Integer.parseInt(br1.readLine());
              }
            }
            break;
          case "MACHINE_SIZE":
            for(machine_i=0;machine_i<MACHINE;machine_i++){
              MACHINE_SIZE[machine_i] = Integer.parseInt(br1.readLine());
            }
            break;
          case "SPEED":
            for(machine_i=0;machine_i<MACHINE;machine_i++){
              SPEED[machine_i] = Integer.parseInt(br1.readLine());
            }
            break;
        }
      }
    }

    finally{
      br.close();
      br1.close();
    }

    //外部出力のファイル名、拡張子の宣言・設定
    String filename     = "result_upver("+args[0].replace(".txt", "_")
                                         +args[1].replace(".txt", "_");
    String filename_ext = ".csv";


    int run_i,syori_i,haichi_i;

    double syori_pheromon[][][]   = new double[JOB][MACHINE][TASK_MAX];
    double haichi_pheromon[][]  = new double[JOB][TASK_MAX];
    double machine_pheromon[][][] = new double[JOB][MACHINE][TASK_MAX];

    //どうしてdouble[JOB][TASK_MAX][TASK_MAX]なのだろう
    double syori_prob[][][]   = new double[JOB][TASK_MAX][TASK_MAX];
    double haichi_prob[][]  = new double[JOB][TASK_MAX];
    double machine_prob[][][] = new double[JOB][MACHINE][TASK_MAX];

    //外部出力用の配列準備
    double result_pmax[][] = new double [RUN][SEDAI+1];
    double result_pmin[][] = new double [RUN][SEDAI+1];
    double result_pave[][] = new double [RUN][SEDAI+1];
    int result_machine[]   = new int [RUN];
    int result_job[]       = new int [RUN];
    int result_task[]      = new int [RUN];
    int result_ant[]       = new int [RUN];
    int best_haichi_job[][]     = new int[SEDAI+1][TASK_MAX];
    int best_haichi_task[][]    = new int[SEDAI+1][TASK_MAX];
    int best_haichi_machine[][] = new int[SEDAI+1][TASK_MAX];


    //タスク総数の宣言・代入 
    int total_task = 0;
    for(job_i=0;job_i<JOB;job_i++){
      total_task += TASK[job_i];
    }



    /*-*-*-*-試行回数のループ-*-*-*-*/
    for(run_i=0;run_i<RUN;run_i++){
      //処理順ノードの初期化
      for(job_i=0;job_i<JOB;job_i++){
        for(syori_i=0;syori_i<TASK[job_i];syori_i++){
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
        for(task_i=0;task_i<TASK[job_i];task_i++){
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
      int haichi_num_task[]  = new int[JOB];

      int task_time[]    = new int[TASK_MAX];
      int task_endtime[] = new int[TASK_MAX];
      
      //外部出力ファイルオープン
      PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(filename+filename_ext)));
      for(sedai_i=0;sedai_i<SEDAI;sedai_i++){

        int ant_i,task_j;
        int syori_select[][][][]   = new int[ANT][JOB][TASK_MAX][TASK_MAX];
        int haichi_select[][]      = new int[ANT][JOB];
        int machine_select[][][]   = new int[ANT][JOB][TASK_MAX];//selsect_machine -> machine_select
        int job_select[][]         = new int[ANT][TASK_MAX];
        int task_select[][][]      = new int[ANT][JOB][TASK_MAX];
        
        int haichi_job[][]     = new int[ANT][TASK_MAX];
        int haichi_task[][]    = new int[ANT][TASK_MAX];
        int haichi_machine[][] = new int[ANT][TASK_MAX];

        int latest_endtime[] = new int[ANT];
        int haichi_job_select[][][] = new int[ANT][JOB][TASK_MAX];

        double disp_pheromon[] = new double[ANT];



        //選択行列の初期化？？？？ 処理関係の初期化という認識で合ってるのか
        for(ant_i=0;ant_i<ANT;ant_i++){
          //処理セレクト配列の初期化
          for(job_i=0;job_i<JOB;job_i++){
            for(task_i=0;task_i<TASK[job_i];task_i++){
              for(task_j=0;task_j<TASK[job_i];task_j++){
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

          for(job_i=0;job_i<JOB;job_i++){
            for(haichi_i=0;haichi_i<TASK_MAX;haichi_i++){
              haichi_job_select[ant_i][job_i][haichi_i] = 1;
            }
          }
        }

        int task_list[][] = new int [JOB][TASK_MAX];
        

        //処理、配置、マシンの決定
        for(ant_i=0;ant_i<ANT;ant_i++){

          //処理順の決定
          for(job_i=0;job_i<JOB;job_i++){
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
              for(task_i=0;task_i<TASK[job_i];task_i++){
                count += syori_prob[job_i][syori_i][task_i];
                if(count>rand) break;
              }
              task_select[ant_i][job_i][syori_i]  = task_i;
              task_list[job_i][syori_i]           = task_i;

              for(task_j=syori_i;task_j<TASK[job_i];task_j++){
                syori_select[ant_i][job_i][task_i][task_j] = 0;
              }
            }
          }
          //処理順決定終了


          //配置決定
          for(job_i=0;job_i<JOB;job_i++){
            haichi_select[ant_i][job_i] = 1;
            haichi_num_task[job_i]      = 0;
          }
          for(haichi_i=0;haichi_i<TASK_MAX;haichi_i++){
            
            double sum = 0.0;
            for(job_i=0;job_i<JOB;job_i++){
              sum += haichi_pheromon[job_i][haichi_i] * haichi_select[ant_i][job_i];
            }
            for(job_i=0;job_i<JOB;job_i++){
              haichi_prob[job_i][haichi_i] = haichi_pheromon[job_i][haichi_i] * haichi_select[ant_i][job_i] / sum;
            }

            double rand  = Math.random();
            double count = 0.0;
            for(job_i=0;job_i<JOB;job_i++){
              count += haichi_prob[job_i][haichi_i];
              if(count>rand)break;//ここでjob_iのループを強制的に終了させ、そのjob_iを下で使う 
            }

            job_select[ant_i][haichi_i] = job_i;

            int temp = haichi_num_task[job_i];
            haichi_task[ant_i][haichi_i] = task_list[job_i][temp];
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


          //マシン割当
          for(job_i=0;job_i<JOB;job_i++){
            for(task_i=0;task_i<TASK[job_i];task_i++){
              double sum = 0.0;
             for(machine_i=0;machine_i<MACHINE;machine_i++){
                sum += machine_pheromon[job_i][task_i][machine_i];
              }
              for(machine_i=0;machine_i<MACHINE;machine_i++){
                machine_prob[job_i][task_i][machine_i] = machine_pheromon[job_i][task_i][machine_i] / sum;
              }
              double rand  = Math.random();
              double count = 0.0;
              int machine_j;
              for(machine_j=0;machine_j<MACHINE;machine_j++){
                count += machine_prob[job_i][task_i][machine_j];
                if(count>rand)break;
              }
              machine_select[ant_i][job_i][task_i] = machine_j;
            }
          }

          //haihi_machineの代入
          for(haichi_i=0;haichi_i<TASK_MAX;haichi_i++){
            int temp_task = haichi_task[ant_i][haichi_i];
            int temp_job  = haichi_job[ant_i][haichi_i];
            haichi_machine[ant_i][haichi_i] = machine_select[ant_i][temp_job][temp_task]; 
          }
          //マシン割当終了
          //処理、配置、マシンの決定終了


          //ガントチャート開始
          //必要配列の初期化
          for(job_i=0;job_i<JOB;job_i++){
            layer_endtime[job_i]    = -1;
            layer_number[job_i]     = 0;
            haichi_num_task[job_i]  = 0;
            sigma[job_i]            = 0;
          }
          for(machine_i=0;machine_i<MACHINE;machine_i++){
            machine_endtime[machine_i] = -1;
          }

          for(haichi_i=0;haichi_i<TASK_MAX;haichi_i++){
            int temp_task    = haichi_task[ant_i][haichi_i];
            int temp_job     = haichi_job[ant_i][haichi_i];
            int temp_machine = haichi_machine[ant_i][haichi_i];
            int temp_laynum  = layer_number[temp_job];
            task_time[haichi_i] = (int)Math.ceil(TASK_VOLUME[temp_job][temp_task] / SPEED[temp_machine]);
            int temp_maxTime    = Math.max(layer_endtime[temp_job],machine_endtime[temp_machine]);
            task_endtime[haichi_i]        = temp_maxTime + task_time[haichi_i];
            machine_endtime[temp_machine] = task_endtime[haichi_i];

            sigma[temp_job] = Math.max(sigma[temp_job],task_endtime[haichi_i]);
            haichi_num_task[temp_job]++;

            if(haichi_num_task[temp_job] == F_TASK[temp_laynum+1][temp_job]){
              layer_endtime[temp_job] = sigma[temp_job];
              layer_number[temp_job]++;
            }
          }

          latest_endtime[ant_i] = 0;
          
          for(job_i=0;job_i<JOB;job_i++){
            if(layer_endtime[job_i]>latest_endtime[ant_i]){
              latest_endtime[ant_i] = layer_endtime[job_i];
            } 
          }
          disp_pheromon[ant_i] = PARAMETER_A - PARAMETER_B * latest_endtime[ant_i];
          if(disp_pheromon[ant_i] <= 1.0){
            disp_pheromon[ant_i] = 1.0;
          }
          //ガントチャート終了  
        }//(antループ終了)

        //ここから10数行は6/22追加分である
        double min_latest_endtime = layer_endtime[0];
        int min_ant = 0;
        for(ant_i=1;ant_i<ANT;ant_i++){
          if(min_latest_endtime > latest_endtime[ant_i]){
            min_latest_endtime = latest_endtime[ant_i];
            min_ant            = ant_i;
          }
        }
        
        if(best_min_time > min_latest_endtime){
          best_min_time = min_latest_endtime;
          if(sedai_i > 100){
            disp_pheromon[min_ant] *= 3;
          }
        }
        //ここまで6/22追加分
        
        //フェロモン更新
        //処理ノードの更新
        for(job_i=0;job_i<JOB;job_i++){
          for(syori_i=0;syori_i<TASK[job_i];syori_i++){
            for(task_i=0;task_i<TASK[job_i];task_i++){
              for(ant_i=0;ant_i<ANT;ant_i++){//antが内側にあるのは意味があるのだろうか
                syori_pheromon[job_i][syori_i][task_i] 
                  *= (1.0 - syori_select[ant_i][job_i][syori_i][task_i] * EVAPO_SYORI / ANT);
              }
              for(ant_i=0;ant_i<ANT;ant_i++){//antが内側にあるのは意味があるのだろうか
                if(task_i == task_select[ant_i][job_i][syori_i]){
                  syori_pheromon[job_i][syori_i][task_i] += disp_pheromon[ant_i];
                }
              }
            }
          }
        }
        //配置ノードの更新
        for(haichi_i=0;haichi_i<TASK_MAX;haichi_i++){
          for(job_i=0;job_i<JOB;job_i++){
            for(ant_i=0;ant_i<ANT;ant_i++){
              haichi_pheromon[job_i][haichi_i]
                *= (1.0 - haichi_job_select[ant_i][haichi_i][job_i] * EVAPO_HAICHI / ANT);
            }
          }
          for(job_i=0;job_i<JOB;job_i++){
            for(ant_i=0;ant_i<ANT;ant_i++){
              if(job_i == job_select[ant_i][haichi_i]){
                haichi_pheromon[job_i][haichi_i] += disp_pheromon[ant_i];
              }
            }
          }
        }
        //マシン割当の更新
        for(job_i=0;job_i<JOB;job_i++){
          for(task_i=0;task_i<TASK_MAX;task_i++){
            for(machine_i=0;machine_i<MACHINE;machine_i++){
              machine_pheromon[job_i][task_i][machine_i]
                *= (1.0 - EVAPO_MACHINE);
            }
          }
          for(task_i=0;task_i<TASK[job_i];task_i++){
            for(machine_i=0;machine_i<MACHINE;machine_i++){
              for(ant_i=0;ant_i<ANT;ant_i++){
                if(machine_i == selsect_machine[ant_i][job_i][task_i]){
                  machine_pheromon[job_i][task_i][machine_i] += disp_pheromon[ant_i];
                }
              }
            }
          }
        } 
        //フェロモン更新終了

        //外部出力開始
        int best_ant;
        double pmax[] = new double[SEDAI+1];
        double pmin[] = new double[SEDAI+1];
        double pave[] = new double[SEDAI+1];

        pmax[sedai_i] = -10000.0;
        pmin[sedai_i] = 10000.0;
        pave[sedai_i] = 0.0;

        //最大値、最小値、平均のif代入
        for(ant_i=0;ant_i<ANT;ant_i++){
          if(pmax[sedai_i]<latest_endtime[ant_i]){
            pmax[sedai_i] = latest_endtime[ant_i];
          }
          if(pmin[sedai_i]>latest_endtime[ant_i]){
            pmin[sedai_i] = latest_endtime[ant_i];
            best_ant      = ant_i;
          }
          pave[sedai_i] += latest_endtime[ant_i];
        }
        pave[sedai_i] = pave[sedai_i] / ANT;

        System.out.println("SEDAI " + sedai_i + " Generation best = " + pmin[sedai_i]);
        System.out.println("SEDAI " + sedai_i + " Generation bad  = " + pmax[sedai_i]);
        System.out.println("SEDAI " + sedai_i + " Generation ave  = " + pave[sedai_i] + "\n");

        if(sedai_i == 0){
          pw.println("Machine = " + machine_i + "," + "Job = " + job_i + "," + "Task = " + task_i + "," + "Ant = " + ant_i + ",");
          pw.println("Sedai " + "," + " Best " + "," + "," + " Bad " +"," + " Average ");
          //println("");を上にあったprint => printlnに変更した
          result_ant[run_i]     = ant_i;
          result_job[run_i]     = job_i;
          result_task[run_i]    = task_i;
          result_machine[run_i] = machine_i;
        }
        pw.println(sedai_i + " , " + pmin[sedai_i] + " , " + pmax[sedai_i] + " , " + pave[sedai_i] + " , ");
        //println("");を上にあったprint => printlnに変更した
        result_pmin[run_i][sedai_i] = pmin[sedai_i];
        result_pmax[run_i][sedai_i] = pmax[sedai_i];
        result_pave[run_i][sedai_i] = pave[sedai_i];

        for(haichi_i=0;haichi_i<TASK_MAX;haichi_i++){
          best_haichi_job[sedai_i][haichi_i]     = haichi_job[best_ant][haichi_i];
          best_haichi_task[sedai_i][haichi_i]    = haichi_task[best_ant][haichi_i];
          best_haichi_machine[sedai_i][haichi_i] = haichi_machine[best_ant][haichi_i];
        }
      }//SEDAILOOP

      //最後のガントチャート？？
      //初期化はじめ
      for(job_i=0;job_i<JOB;job_i++){
        layer_endtime[job_i]    = -1;
        layer_number[job_i]     = 0;
        haichi_num_task[job_i]  = 0;
        sigma[job_i]            = 0;
      }
      for(machine_i=0;machine_i<MACHINE;machine_i++){
        machine_endtime[machine_i] = -1;
      }//おわり

      pw.println("haichi "+","+" Machine "+","+" Job "+","+" Task"+","+" Start Time "+","+" End Time");
      //println("");を上にあったprint => printlnに変更した
      for(haichi_i=0;haichi_i<TASK_MAX;haichi_i++){
        //best_haichi_XXX[SEDAI][haichi_i] SEDAI => あってるの？？？？ 
        int temp_Bjob     = best_haichi_job[SEDAI][haichi_i];
        int temp_Btask    = best_haichi_task[SEDAI][haichi_i];
        int temp_Bmachine = best_haichi_machine[SEDAI][haichi_i];
        int temp_laynum   = layer_number[temp_Bjob];
        task_time[haichi_i]    = (int)Math.ceil(TASK_VOLUME[temp_Bjob][temp_Btask] / SPEED[temp_Bmachine]);
        int temp_maxTime       = (int)Math.max(layer_endtime[temp_Bjob],machine_endtime[temp_Bmachine]);
        int temp_startTime     = temp_maxTime + 1;
        task_endtime[haichi_i] = temp_maxTime + task_time[haichi_i];
        machine_endtime[temp_Bmachine] = task_endtime[haichi_i];
        sigma[temp_Bjob]       = Math.max(sigma[temp_Bjob],task_endtime[haichi_i]);
        
        haichi_num_task[temp_Bjob]++;
        if(haichi_num_task[temp_Bjob] == F_TASK[temp_laynum+1][temp_Bjob]){
          layer_endtime[temp_Bjob] = sigma[temp_Bjob];
          layer_number[temp_Bjob]++;
        }
        pw.println("");
        pw.print(haichi_i + " , " + best_haichi_machine[SEDAI][haichi_i] + " , " 
                                  + best_haichi_job[SEDAI][haichi_i] + " , " 
                                  + best_haichi_task[SEDAI][haichi_i] + " , " 
                                  + temp_startTime + " , " + task_endtime[haichi_i] + " , ");
      }
      pw.close();
    }//RUN END

    //result外部出力部
    PrintWriter result_pw = new PrintWriter(new BufferedWriter(new FileWriter("other" + filename + filename_ext)));

    result_pw.println("RUN, MACHINE, JOB, TASK, ANT");
    for(run_i=0;run_i<RUN;run_i++){
      result_pw.println(run_i + " , " + result_machine[run_i] + " , " + result_job[run_i] + " , " + result_task[run_i] + " , " + result_ant[run_i]); 
    }
    result_pw.println("");

    //min
    result_pw.println("min output");
    result_pw.print("SEDAI ");
    for(run_i=0;run_i<RUN;run_i++){
      result_pw.print(", run = " + run_i);
    }
    for(sedai_i=0;sedai_i<=SEDAI;sedai_i++){
      result_pw.println("");
      result_pw.print(sedai_i);
      for(run_i=0;run_i<RUN;run_i++){
        result_pw.print(" , " + result_pmin[run_i][sedai_i]);
      }
    }
    result_pw.println("");
    result_pw.println("");
    
    //max
    result_pw.println("max output");
    result_pw.print("SEDAI ");
    for(run_i=0;run_i<RUN;run_i++){
      result_pw.print(", run = " + run_i);
    }
    for(sedai_i=0;sedai_i<=SEDAI;sedai_i++){
      result_pw.println("");
      result_pw.print(sedai_i);
      for(run_i=0;run_i<RUN;run_i++){
        result_pw.print(" , " + result_pmax[run_i][sedai_i]);
      }
    }
    result_pw.println("");
    result_pw.println("");

    //ave
    result_pw.println("ave output");
    result_pw.print("SEDAI ");
    for(run_i=0;run_i<RUN;run_i++){
      result_pw.print(", run = " + run_i);
    }
    for(sedai_i=0;sedai_i<=SEDAI;sedai_i++){
      result_pw.println("");
      result_pw.print(sedai_i);
      for(run_i=0;run_i<RUN;run_i++){
        result_pw.print(" , " + result_pave[run_i][sedai_i]);
      }
    }
    result_pw.close();
    
  }
}
