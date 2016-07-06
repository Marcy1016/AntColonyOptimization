import java.util.*;
import java.io.*;
import java.awt.*;
import java.util.regex.*;

public class aco_2016_up{
		// Grid
	static int RUN;
	static int run;
	static int JOB;		//Number of jobs
	static int job, jobi;	//Job's number
	static int TASK[];			//Number of tasks
	static int task,taski,taskj,beta; //Task's number
	static int task_size[][]; //Task's size 
	static int task_volume[][];	//Task processing amount
	static int LAYER[]; //Layer number of precedence constraint
	static int layerjob; //Temporary value of LAYER[]
	static int LAYER_MAX;
	static int layer;	//Layer number
	static int F_TASK[][];	//Smallest task number among the task group belonging to the each layer 
	static int TASK_MAX;
	static int MACHINE;	//Number of machines
	static int machine, machinei;	//Machine number
	static double machine_size[]; //Machine's size
	static int speed[];	//Processing speed of machine
	
		// ACO
	static int K, k;   //The final number of generations, Generation number
	static int ANT, ant;	//Number of ants 
	static double initial_pheromon;	//Initial accumulation pheromone
	static double wariate_pheromon[][][];	//Accumulation pheromone of assignment node
	static int syori;	//Processing order number
	static double syori_pheromon[][][];	//Accumulation pheromone of processing order node
	static int U, U_ub;	//Task total number
	static int haichi;	//Arrangement order number
	static double haichi_pheromon[][];	//Accumulation pheromone of arrangement order node
	static int syori_select[][][][];	//Task selection matrix 
	static double sum;	//The sum of the accumulated pheromone
	static int best_ant;	//The best of ant
	static double r;	//Uniform random number of [0,1] interval
	static double count;	//Cumulative total of node selection probability
	static double syori_prob[][][];	//Processing order node selection probability
	static int select_task[][][];	//Roulette of selected node (task)
	static int task_list[][];	//Tasks list
	static int haichi_select[][];	//Job selection vector(0 or 1)
	static int haichi_num_task[];	//Deployed number of tasks
	static int haichinumtask;
	static double haichi_prob[][];	//Arrangement order node selection probability
	static int select_job[][];	//Roulette of selected node (Job)
	static int haichi_machine[][];	//Machine numbers are arranged in order of arrangement
	static int haichimachine;
	static int haichi_task[][];	//Task numbers are arranged in order of arrangement
	static int haichitask;
	static int haichi_job[][];	//Job numbers are arranged in order of arrangement
	static int haichijob;
	static double wariate_prob[][][];	//Assignment node selection probability
	static int select_machine[][][];	//Roulette of selected node (Machine)
	static int sigma[];	// Temporary value of processing end time of the job
	static int layer_endtime[];	//Processing end time of the layer
	static int layer_number[];	//Layer number
	static int layernumber;
	static int machine_endtime[];	//Processing end time of the machine
	static int task_time[];	//Task processing time
	static int temp;	//Temporary storage of the larger value
	static int task_starttime[];	//Processing start time of task
	static int task_endtime[];	//Processing end time of the task
	static int latest_endtime[];	//Latest processing End Time => Evaluation value
	
	static double disp_pheromon[];	//Dispersal pheromone,Spraying of pheromone
	static double parameter_a, parameter_b; //Parameters in the conversion formula from the evaluation value to application rate
	static double evapo_syori, evapo_haichi, evapo_wariate;  //Evaporation rate of (processing order node,arrangement order node,Assignment node)
	static double pmax[];	//Worst evaluation value
	static double pmin[];	//Best evaluation value
	static double pave[];	//Average rating value
	static int best_haichi_task[][];	//Arrangement order task number of the best solution
	static int besthaichitask;
	static int best_haichi_job[][];	//Arrangement order job number of the best solution
	static int besthaichijob;
	static int best_haichi_machine[][];	//Arrangement order machine number of the best solution
	static int besthaichimachine;
	
	//Output for all result
	static double result_pmax[][];
	static double result_pmin[][];
	static double result_pave[][];
	static int result_machine[];
	static int result_job[];
	static int result_task[];
	static int result_ant[];
	static BufferedReader br, br1, br2;
	static String str;
	
	//main
	public static void main(String args[]) throws IOException{

	br  = new BufferedReader(new FileReader(args[0]));
	br1 = new BufferedReader(new FileReader(args[1]));
	br2 = new BufferedReader(new FileReader(args[2]));

	try 
	{
    	while ((str = br.readLine()) != null)
    	{
    	    switch (str)
    	    {
    	    	case "ANT":
    	    		ANT = Integer.parseInt(br.readLine());
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

    	while ((str = br1.readLine()) != null)
    	{
    		switch (str)
    		{
    			case "initial_pheromon":
    	    		initial_pheromon = Double.parseDouble(br1.readLine());
    	    		System.out.println(initial_pheromon);
        			break;
        		case "parameter_a":
    	    		parameter_a = Double.parseDouble(br1.readLine());
    	    		System.out.println(parameter_a);
        			break;
        		case "parameter_b":
    	    		parameter_b = Double.parseDouble(br1.readLine());
    	    		System.out.println(parameter_b);
        			break;
        		case "evapo_syori":
    	    		evapo_syori = Double.parseDouble(br1.readLine());
    	    		System.out.println(evapo_syori);
        			break;
        		case "evapo_haichi":
    	    		evapo_haichi = Double.parseDouble(br1.readLine());
    	    		System.out.println(evapo_haichi);
        			break;
        		case "evapo_wariate":
    	    		evapo_wariate = Double.parseDouble(br1.readLine());
    	    		System.out.println(evapo_wariate);
        			break;
        	}
    	}
	
	//ANT =20;		// Ant number
	//K   =10000;      // Generation number
	//JOB =3;		//Job number	
	RUN =1;
	
	//LAYER_MAX        =4;
	//TASK_MAX         =30;
	//MACHINE          =5;
	//U_ub             =140;
	//initial_pheromon =100.0;
	//parameter_a      =100.0;
	//parameter_b      =2.0;
	
	//evapo_syori   =0.2;
	//evapo_haichi  =0.2;
	//evapo_wariate =0.2;
	
	wariate_pheromon    =new double[MACHINE][TASK_MAX][JOB];
	syori_pheromon      =new double[TASK_MAX][TASK_MAX][JOB];
	haichi_pheromon     =new double[JOB][U_ub];
	syori_select        =new int[TASK_MAX][TASK_MAX][JOB][ANT];
	syori_prob          =new double[TASK_MAX][TASK_MAX][JOB];
	select_task         =new int[TASK_MAX][JOB][ANT];
	task_list           =new int[TASK_MAX][JOB];
	haichi_select       =new int[JOB][ANT];
	haichi_num_task     =new int[JOB];
	haichi_prob         =new double[JOB][U_ub];
	select_job          =new int[U_ub][ANT];
	haichi_machine      =new int[U_ub][ANT];
	haichi_task         =new int[U_ub][ANT];
	haichi_job          =new int[U_ub][ANT];
	wariate_prob        =new double[MACHINE][TASK_MAX][JOB];
	select_machine      =new int[TASK_MAX][JOB][ANT];
	layer_endtime       =new int[JOB];
	layer_number        =new int[JOB];
	sigma               =new int[JOB];
	machine_endtime     =new int[MACHINE];
	task_time           =new int[U_ub];
	task_starttime      =new int[U_ub];
	task_endtime        =new int[U_ub];
	latest_endtime      =new int[ANT];
	disp_pheromon       =new double[ANT];
	pmax                =new double[K+1];
	pmin                =new double[K+1];
	pave                =new double[K+1];
	best_haichi_task    =new int[U_ub][K+1];
	best_haichi_job     =new int[U_ub][K+1];
	best_haichi_machine =new int[U_ub][K+1];
	
	result_pmax    = new double [RUN][K+1];
	result_pmin    = new double [RUN][K+1];
	result_pave    = new double [RUN][K+1];
	result_machine = new int [RUN];
	result_job     = new int [RUN];
	result_task    = new int [RUN];
	result_ant     = new int [RUN];
	
	//task_size    =new int[TASK_MAX][JOB];
	task_volume  =new int[TASK_MAX][JOB];
	
	LAYER        =new int[JOB];
	F_TASK       =new int[LAYER_MAX+1][JOB];	
	machine_size =new double[MACHINE];
	speed        =new int[MACHINE];


	//TASK_MAXの初期化
	TASK_MAX = 0;

	while ((str = br2.readLine()) != null)
	{
		switch (str)
		{
			case "TASK":
				String[] temp = br2.readLine().split(",",0);
				TASK = new int[temp.length];
				JOB = temp.length;
				for (job=0;job<temp.length;job++)
				{
					TASK[job] = Integer.parseInt(temp[job]);
					System.out.println(TASK[job]);
					TASK_MAX += TASK[job];/*これ大事*/
				}
				break;
			case "task_size":
				for (job=0;job<JOB;job++)
				{
					temp = br2.readLine().split(",",0);
					task_size[job] = new int[temp.length];
					for (task=0;task<TASK[job];task++)
					{
						task_size[task][job] = Integer.parseInt(temp[task]);
						System.out.println(task_size[task][job]);
					}
				}
				break;
			case "task_volume":
				for (job=0;job<JOB;job++)
				{
					for (task=0;task<TASK[job];task++)
					{
						task_volume[task][job] = Integer.parseInt(br2.readLine());
						System.out.println(task_volume[task][job]);
					}
				}
				break;
			case "LAYER":
				for (job=0;job<JOB;job++)
				{
					LAYER[job] = Integer.parseInt(br2.readLine());
					System.out.println(LAYER[job]);
				}
				break;
			case "F_TASK":
				for (job=0;job<JOB;job++)
				{
					for (layer=0;layer<LAYER_MAX;layer++)
					{
						F_TASK[layer][job] = Integer.parseInt(br2.readLine());
						System.out.println(F_TASK[layer][job]);
					}
				}
				break;
			case "machine_size":
				for (machine=0;machine<MACHINE;machine++)
				{
					machine_size[machine] = Double.parseDouble(br2.readLine());
					System.out.println(machine_size[machine]);
				}
				break;
			case "speed":
				for (machine=0;machine<MACHINE;machine++)
				{
					speed[machine] = Integer.parseInt(br2.readLine());
					System.out.println(speed[machine]);
				}
				break;
		}
	}


	}
	finally 
	{
    	br.close();
    	br1.close();
    	br2.close();
	}

	
//	String filename     = "result_upver("+args[0].replace(".txt", "_")+args[1].replace(".txt", "_")+args[2].replace(".txt", ")");//"result(20,0.2,#2Anew_add)";
	String filename = "upver";
	String filename_ext = ".csv" ;
	
	//Initialization of nodes
	//start Initialization of the assignment node
	for(run=0;run<RUN;run++){
		for(job=0;job<JOB;job++){
			for(task=0;task<TASK[job];task++){
				for(machine=0;machine<MACHINE;machine++){
					if(task_size[task][job]<=machine_size[machine]){
						wariate_pheromon[machine][task][job]=initial_pheromon;
					}
					else{
							wariate_pheromon[machine][task][job]=0.0;
					}	
				}
			}
		}//end
	
	//start initialization of the processing order node 
		for(job=0; job<JOB;job++){
			for(syori=0; syori<TASK[job];syori++){
				for(taski=0; taski<TASK[job];taski++){
					syori_pheromon[taski][syori][job]=initial_pheromon;
				//System.out.println(" initial_pheromon= "+initial_pheromon);
				}
			}
		}//end
		//start Initialization of the arrangement order node
		U=0;
		for(job=0; job<JOB;job++){
			U+=TASK[job];
		}
		for(haichi=0; haichi<U;haichi++){
			for(job=0;job<JOB;job++){ 
				haichi_pheromon[job][haichi]=initial_pheromon;
			}
		}//end
		
		double best_min_time = K; // 6.22

	// file open
	PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(filename+filename_ext)));	
		for(k=0;k<=K;k++){
		//Initialization of the selection matrix
			
			int haichi_job_select[][][] = new int[JOB][U_ub][ANT]; //6/29
			//haichi_job_select[JOB][U_ub][ANT]
			for(ant=0;ant<ANT;ant++){
				for(job=0;job<JOB;job++){
					for(haichi=0;haichi<U_ub;haichi++){
						haichi_job_select[job][haichi][ant]=1;
					}
				}
			}
			
			
			for(ant=0;ant<ANT;ant++){	//start Setting Initialization of the selection matrix
				for(job=0;job<JOB;job++){
					for(taskj=0;taskj<TASK[job];taskj++){
						for(taski=0;taski<TASK[job];taski++){
							syori_select[taski][taskj][job][ant]=0;
						}
					}
					layerjob=LAYER[job];
					F_TASK[0][job]=0;
					F_TASK[layerjob][job]=TASK[job];
		
					for(layer=0;layer<LAYER[job];layer++){
						for(taskj=F_TASK[layer][job];taskj<F_TASK[layer+1][job];taskj++){
							for(taski=F_TASK[layer][job];taski<F_TASK[layer+1][job];taski++){
								syori_select[taski][taskj][job][ant]=1;
							}
						}
					}
				}//end
			}//end
			
			for(ant=0;ant<ANT;ant++){		
			//Processing order node
				for(job=0;job<JOB;job++){
					for(syori=0;syori<TASK[job];syori++){
					// Start the calculation of selection probability
						sum=0.0;
						for(task=0;task<TASK[job];task++){
							sum+=syori_pheromon[task][syori][job]*syori_select[task][syori][job][ant];
						}
						for(task=0;task<TASK[job];task++){
							syori_prob[task][syori][job]=syori_pheromon[task][syori][job]
							*syori_select[task][syori][job][ant]/sum;
						}//end 
						// modification of roulette selection and selection matrix
						//from here roulette selection
						r=Math.random();
						count=0.0;
						for(taski=0;taski<TASK[job];taski++){
							count+=syori_prob[taski][syori][job];
							if(count>r)break;
						}//end of roulette selection
						select_task[syori][job][ant]=taski; //Selection task is taski 
						task_list[syori][job]=taski; //Registration to the task list of the selected task
						// from here modification of the selection matrix
						for(taskj=syori;taskj<TASK[job];taskj++){
							syori_select[taski][taskj][job][ant]=0;
						}//end 
					//}//end
					}
		
				}// processing order node is end	
		
				
				
			//Arrangement order node
				for(job=0;job<JOB;job++){
			
					haichi_select[job][ant]=1;
					haichi_num_task[job]=0;
				}
				for(haichi=0;haichi<U;haichi++){
					sum=0.0;//Start the calculation of selection probability
					for(job=0;job<JOB;job++){
						sum+=haichi_pheromon[job][haichi]*haichi_select[job][ant];
					}
					for(job=0;job<JOB;job++){				
						haichi_prob[job][haichi]=haichi_pheromon[job][haichi]*haichi_select[job][ant]/sum;
					}//end of the calculation
					//from here roulette selection
					r=Math.random();
					count=0.0;
					for(jobi=0;jobi<JOB;jobi++){
						count+=haichi_prob[jobi][haichi];
						if(count>r)break;
					}//end of roulette selection
		
					select_job[haichi][ant]=jobi;	
					//Sequence of tasks and jobs at arrangement order 
					haichinumtask            = haichi_num_task[jobi];
					haichi_task[haichi][ant] =task_list[haichinumtask][jobi];
					haichi_job[haichi][ant]  =jobi;
					haichi_num_task[jobi]++;
					if(haichi_num_task[jobi] >= TASK[jobi]){
						for(int i=haichi;i<U;i++){
							haichi_job_select[jobi][i][ant]=0;
						}
					haichi_select[jobi][ant]=0;				
					}
				}//Arrangement order node is end
			//Assignment node
				for(job=0;job<JOB;job++){
					for(task=0;task<TASK[job];task++){
						sum=0.0;//Start the calculation of selection probability
						for(machine=0;machine<MACHINE;machine++){
							sum+=wariate_pheromon[machine][task][job];
						}
						for(machine=0;machine<MACHINE;machine++){
						wariate_prob[machine][task][job]=wariate_pheromon[machine][task][job]/sum;
						}//end of the calculation
						//from here roulette selection
						r=Math.random();
						count=0.0;
						for(machinei=0;machinei<MACHINE;machinei++){
							count+=wariate_prob[machinei][task][job];
							if(count>r)break;
						}//end of roulette selection
						select_machine[task][job][ant]=machinei;
					}
				}
				//start sequence of machine at arrangement order 
				for(haichi=0;haichi<U;haichi++){
					haichitask=haichi_task[haichi][ant];
					haichijob=haichi_job[haichi][ant];
					haichi_machine[haichi][ant]=select_machine[haichitask][haichijob][ant];	
				}//end
				//Assignment node is end
				
			//Gantt charts
				for(job=0;job<JOB;job++){
					layer_endtime[job]   =-1;
					layer_number[job]    =0;
					haichi_num_task[job] =0;
					sigma[job]           =0;
				}
				for(machine=0;machine<MACHINE;machine++){
					machine_endtime[machine] =-1;
				}

				for(haichi=0;haichi<U;haichi++){
				
					haichitask        =haichi_task[haichi][ant];
					haichijob         =haichi_job[haichi][ant];
					haichimachine     =haichi_machine[haichi][ant];
					layernumber       =layer_number[haichijob];
					task_time[haichi] =(int)Math.ceil(task_volume[haichitask][haichijob]
					/speed[haichimachine]);
					
					temp                           =Math.max(layer_endtime[haichijob],machine_endtime[haichimachine]);
					task_starttime[haichi]         =temp+1;
					task_endtime[haichi]           =temp+task_time[haichi];
					machine_endtime[haichimachine] =task_endtime[haichi];
					
					sigma[haichijob] =Math.max(sigma[haichijob],task_endtime[haichi]);
					haichi_num_task[haichijob]++;
					
					if(haichi_num_task[haichijob]==F_TASK[layernumber+1][haichijob]){
						layer_endtime[haichijob] =sigma[haichijob];
						layer_number[haichijob]++;
					}
				}
				latest_endtime[ant]=0;
				for(job=0;job<JOB;job++){
					if(layer_endtime[job]>latest_endtime[ant]){
						latest_endtime[ant]=layer_endtime[job];
					}
				}
				disp_pheromon[ant]=parameter_a-parameter_b*latest_endtime[ant];
				if(disp_pheromon[ant]<=1.0){
				disp_pheromon[ant]=1.0;  // 6/22
				}//gantt charts is end
			}//ant loop is finish
			
			

			//6/22
			double min_latest_endtime = latest_endtime[0];
			int min_ant = 0;
			for(ant=1;ant<ANT;ant++){
				if(min_latest_endtime > latest_endtime[ant]){
					min_latest_endtime = latest_endtime[ant];
					min_ant = ant;
				}
			}
			
			if(best_min_time > min_latest_endtime){
				best_min_time = min_latest_endtime;
				if(k > 100){
					disp_pheromon[min_ant] *= 3;
				}
			}
		//Pheromone update
			//from here processing order node's update
			for(job=0;job<JOB;job++){
				for(syori=0;syori<TASK[job];syori++){
					for(task=0;task<TASK[job];task++){
						for(ant=0;ant<ANT;ant++){	//from here start the evaporation of node accumulation pheromone
							syori_pheromon[task][syori][job]
								*=(1.0-syori_select[task][syori][job][ant]*evapo_syori/ANT);//syori_select[task][syori][job][ant]
						}//end the evaporation of node
						for(ant=0;ant<ANT;ant++){//from here Pheromone spray to the node
							if(task==select_task[syori][job][ant]){
								syori_pheromon[task][syori][job]+=disp_pheromon[ant];
							}	
						}
					}	
				}
			}			
			for(haichi=0;haichi<U;haichi++){
				for(job=0;job<JOB;job++){
					for(ant=0;ant<ANT;ant++){
						haichi_pheromon[job][haichi]*=(1.0-haichi_job_select[job][haichi][ant]*evapo_haichi/ANT);
					}
				}
				for(job=0;job<JOB;job++){
					for(ant=0;ant<ANT;ant++){
						if(job==select_job[haichi][ant]){
							haichi_pheromon[job][haichi]+=disp_pheromon[ant];
						}
					}
				}
			}
			for(job=0;job<JOB;job++){
				for(task=0;task<TASK[job];task++){
					for(machine=0;machine<MACHINE;machine++){
						wariate_pheromon[machine][task][job]*=(1.0-evapo_wariate);
					}
				}
				for(task=0;task<TASK[job];task++){
					for(machine=0;machine<MACHINE;machine++){
						for(ant=0;ant<ANT;ant++){
							if(machine==select_machine[task][job][ant]){
								wariate_pheromon[machine][task][job]+=disp_pheromon[ant];
							}
						}
					}
				}
			}

			pmax[k] =-10000.0;
			pmin[k] =10000.0;
			pave[k] =0.0;
		
			for(ant=0;ant<ANT;ant++){
				if(pmax[k]<latest_endtime[ant]){
					pmax[k]=latest_endtime[ant];
				}
				if(pmin[k]>latest_endtime[ant]){
					pmin[k]  =latest_endtime[ant];
					best_ant =ant;
				}
				pave[k]+=latest_endtime[ant];
			}
			pave[k]=pave[k]/ANT;
			
			System.out.println("K " +k+" Generation best = "+pmin[k]);
			System.out.println("K " +k+" Generation bad  = "+pmax[k]);
			System.out.println("K " +k+" Generation ave  = "+pave[k]+"\n");
		
			if(k==0){
				pw.println("Machine= " + machine + "," +"Job= "+job+","+ "Task= "+task+ "," + "Ant= "+ant+ "," );
				pw.print("K"+","+" Best" +","+" Bad"+" ,"+" Average");		
				pw.println("");
				result_machine[run] =machine;
				result_job[run]     =job;
				result_task[run]    =task;
				result_ant[run]     =ant;
			}
			pw.print(k+"," +pmin[k]+ "," +pmax[k]+ ","+pave[k]+ ",");
			pw.println("");	
			result_pmin[run][k] =pmin[k];
			result_pmax[run][k] =pmax[k];
			result_pave[run][k] =pave[k];
		
		
			for(haichi=0;haichi<U;haichi++){  
				best_haichi_task[haichi][k]    =haichi_task[haichi][best_ant];
				best_haichi_job[haichi][k]     =haichi_job[haichi][best_ant];
				best_haichi_machine[haichi][k] =haichi_machine[haichi][best_ant];
			
			}
	
		}//k close
	
	
//gantt chart of the best solution in the final generation 
		for(job=0;job<JOB;job++){
			layer_endtime[job]   =-1;
			layer_number[job]    =0;
			haichi_num_task[job] =0;
			sigma[job]           =0;
	
		}
		for(machine=0;machine<MACHINE;machine++){
			machine_endtime[machine] =-1;
		}
		pw.print("haichi "+","+" Machine "+","+" Job "+","+" Task"+","+" Start Time "+","+" End Time");
		pw.println("");	
		for(haichi=0;haichi<U;haichi++){
			besthaichitask                     =best_haichi_task[haichi][K];	
			besthaichijob                      =best_haichi_job[haichi][K];
			besthaichimachine                  =best_haichi_machine[haichi][K];
			layernumber                        =layer_number[besthaichijob];
			task_time[haichi]                  =(int)Math.ceil(task_volume[besthaichitask][besthaichijob]/speed[besthaichimachine]);
			temp                               =(int)Math.max(layer_endtime[besthaichijob],machine_endtime[besthaichimachine]);
			task_starttime[haichi]             =temp+1;
			task_endtime[haichi]               =temp+task_time[haichi];
			machine_endtime[besthaichimachine] =task_endtime[haichi];
			sigma[besthaichijob]               =Math.max(sigma[besthaichijob],task_endtime[haichi]);
		
			haichi_num_task[besthaichijob]++;
			if(haichi_num_task[besthaichijob]==F_TASK[layernumber+1][besthaichijob]){
			layer_endtime[besthaichijob] =sigma[besthaichijob];
			layer_number[besthaichijob]++;
			}		
			pw.println("");
			pw.print(haichi+" , " +best_haichi_machine[haichi][K]+" , " +best_haichi_job[haichi][K]+" , " +best_haichi_task[haichi][K]+" , " +task_starttime[haichi]+" , " +task_endtime[haichi]+" , ");
		}
		pw.close();
	}//end of run
	
	PrintWriter result_pw=new PrintWriter(new BufferedWriter(new FileWriter("other"+filename+filename_ext)));	
	
	result_pw.println("RUN, MACHINE, JOB, TASK, ANT");
	for(run=0; run<RUN; run++){
		result_pw.println(run + "," + result_machine[run] + "," + result_job[run] + "," + result_task[run] + "," + result_ant[run]);
	}
	result_pw.println(" ");
	
	//min
	result_pw.println("min output");
	result_pw.print("K ");
	for(run=0; run<RUN; run++){
		result_pw.print(", run=" + run);
	}
	for(k=0; k<=K; k++){
		result_pw.println(" ");
		result_pw.print( k );
		for(run=0; run<RUN; run++){
			result_pw.print(","+ result_pmin[run][k]);
		}
	}
	
	result_pw.println(" ");
	result_pw.println(" ");
	//max
	result_pw.println("max output");
	result_pw.print("K ");
	for(run=0; run<RUN; run++){
		result_pw.print(", run=" + run);
	}
	for(k=0; k<=K; k++){
		result_pw.println(" ");
		result_pw.print( k );
		for(run=0; run<RUN; run++){
			result_pw.print(","+ result_pmax[run][k]);
		}
	}
	
	result_pw.println(" ");
	result_pw.println(" ");
	//Ave
	result_pw.println("ave output");
	result_pw.print("K ");
	for(run=0; run<RUN; run++){
		result_pw.print(", run=" + run);
	}
	for(k=0; k<=K; k++){
		result_pw.println(" ");
		result_pw.print( k );
		for(run=0; run<RUN; run++){
			result_pw.print(","+ result_pave[run][k]);
		}
	}
	
	result_pw.close();
	
} // main close
} // class close



