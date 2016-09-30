import java.io.*;
import java.util.*;

public class RandNumGeneration{
  public static void main(String args[]) throws IOException{
    System.out.println("ファイル名を入力");

    Random rnd = new Random();

    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    String filename = input.readLine();

    System.out.println("JOB数を入力");
    int JOB = Integer.parseInt(input.readLine());

    System.out.println("TASKを入力");
    int TASK = Integer.parseInt(input.readLine());


    System.out.println("LAYERを入力");
    int LAYER = Integer.parseInt(input.readLine());

    System.out.println("MACHINEの数を入力");
    int MACHINE = Integer.parseInt(input.readLine());

    try{
      PrintWriter pw = new PrintWriter(new BufferedWriter (new FileWriter(filename)));

      //TASKの記述文
      pw.println("TASK");
      pw.print(TASK);
      for(int i=1;i<JOB;i++){
        pw.print(","+TASK);
      }
      pw.println();

      //TASK_SIZEの記述文
      pw.println("TASK_SIZE");
      for(int i=0;i<JOB;i++){
        int TASK_SIZE = rnd.nextInt(51)+50;//(50~100)
        pw.print(TASK_SIZE);
        for(int j=1;j<TASK;j++){
          TASK_SIZE = rnd.nextInt(51)+50;
          pw.print(","+TASK_SIZE);
        }
        pw.println();
      }

      //TASK_VOLUMEの記述文
      pw.println("TASK_VOLUME");
      for(int i=0;i<JOB;i++){
        int TASK_VOLUME = rnd.nextInt(111)+100;//(100~210)
        pw.print(TASK_VOLUME);
        for(int j=1;j<TASK;j++){
          TASK_VOLUME = rnd.nextInt(111)+100;
          pw.print(","+TASK_VOLUME);
        }
        pw.println();
      }

      //LAYERの記述文
      pw.println("LAYER");
      pw.print(LAYER);
      for(int i=1;i<JOB;i++){
        pw.print(","+LAYER);
      }
      pw.println();

      //F_TASKの記述文
      pw.println("F_TASK");
      for(int i=0;i<JOB;i++){
        pw.print(0);
        for(int j=1;j<LAYER;j++){
          int F_TASK = (int)TASK/LAYER*j;//小数点切り捨て
          pw.print(","+F_TASK);
        }
        pw.println();
      }

      //MACHINE_SIZEの記述文
      pw.println("MACHINE_SIZE");
      int MACHINE_SIZE =(rnd.nextInt(4)+6)*10;
      pw.print(MACHINE_SIZE);
      for(int i=1;i<MACHINE;i++){
        MACHINE_SIZE =(rnd.nextInt(4)+6)*10;
        pw.print(","+MACHINE_SIZE);
      }
      pw.println();

      //SPEEDの記述文
      pw.println("SPEED");
      int SPEED =(rnd.nextInt(4)+6)*10;
      pw.print(SPEED);
      for(int i=1;i<MACHINE;i++){
        SPEED =(rnd.nextInt(4)+6)*10;
        pw.print(","+SPEED);
      }
      pw.println();

      pw.close();

    } catch (IOException ex){
      System.out.println(ex);
    }

  }
}