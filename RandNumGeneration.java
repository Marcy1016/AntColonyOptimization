import java.io.*;

public class RandNumGeneration{
  public static void main(String args[]) throws IOException{
    System.out.println("ファイル名を入力");

    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    String filename = input.readLine();

    System.out.println(filename);
  }
}