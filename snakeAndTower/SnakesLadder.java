import java.io.*;
import java.util.*;

public class SnakesLadder   {
	// extends AbstractSnakeLadders
	int N, M;
	int snakes[];
	int ladders[];

	Integer forward[];

	Integer forward2[];

	Integer ladders_start[];
	Integer ladders_end[];

	
	public SnakesLadder(String name)throws Exception{
		File file = new File(name);
		BufferedReader br = new BufferedReader(new FileReader(file));
		N = Integer.parseInt(br.readLine());
        
        M = Integer.parseInt(br.readLine());

	    snakes = new int[N];
		ladders = new int[N];
	    for (int i = 0; i < N; i++){
			snakes[i] = -1;
			ladders[i] = -1;
		}

		for(int i=0;i<M;i++){
            String e = br.readLine();
            StringTokenizer st = new StringTokenizer(e);
            int source = Integer.parseInt(st.nextToken());
            int destination = Integer.parseInt(st.nextToken());

			if(source<destination){
				ladders[source] = destination;
			}
			else{
				snakes[source] = destination;
			}
        }


		//added my me
		forward = new Integer[N+1];
		forward2 = new Integer[N+1];
		





		Queue<Integer> q = new LinkedList<>();
		
		int visited[] = new int[N+1];
		for(int i=0; i<N+1; i++){
			visited[i] = 0;
			

		}
		for(int i=0; i<N+1; i++){
			forward[i] = Integer.MAX_VALUE-2*N;
			forward2[i] = Integer.MAX_VALUE-2*N;

		}
		q.add(0);
		int count =1;
		visited[0] = 1;
		forward[0] = 0;
		while(!q.isEmpty()){
			int size = q.size();
			// System.out.println("size "+ size);

			for(int i=0; i<size ; i++){
				int temp = q.remove();
				// System.out.println("parent "+ temp+" "+ count);

				for(int j=1; j<=6 && temp+j< N+1; j++){
					// System.out.println("parent child"+ (temp+j));
					if(visited[temp+j] == 0){
						forward[temp+j] = count;
						boolean sl = false;
						visited[temp+j] = 1;
						// System.out.println("checking snake or ladder..... "+ (temp+j) );
						int temp2 = temp+j;
						while(temp2<N && (ladders[temp2] != -1 || snakes[temp2] != -1)){
							sl = true;
							if(ladders[temp2] != -1){
								// System.out.println("found ladder "+ temp2);
								temp2 = ladders[temp2];
								if(visited[temp2]==0){

									visited[temp2] = 1;

									forward[temp2] = count;

								}
							}else{
								// System.out.println("found snake "+ temp2);

								temp2 = snakes[temp2];
								if(visited[temp2]==0){

									visited[temp2] = 1;

									forward[temp2] = count;

								}
								
							}

							
						}
						if(sl == false) {
							q.add(temp+j);
						}
						else {
							q.add(temp2);
							sl = false;
						}
					}
				}
				
				
				
			}
			count++;
		}

		// for(int i=0; i<N+1; i++){
		// 	System.out.println("("+ i+ ","+forward[i]+")");
		// }
		// System.out.println();
		// System.out.println("count "+ count);




		int visited2[]  = new int[N+1];
		for(int i=0 ; i<N+1; i++){
			visited2[i] = 0;
		}

		Queue<Integer> q2 = new LinkedList<>();
		q2.add(100);
		count =1;
		visited2[100] = 1;
		forward2[100] = 0;

		int[] ladders2 = new int[N];
		int[] snakes2 = new int[N];

		for(int i=0; i<N; i++){
			ladders2[i] = -1;
			snakes2[i] = -1;
		}

		int pos_lad = 0;
		ladders_start = new Integer[N];
		ladders_end = new Integer[N];

		for(int i=0; i<N; i++){
			ladders_start[i] = -1;
			ladders_end[i] = -1;
		}
		for(int i=0; i<N; i++){
			if(ladders[i] != -1){
				ladders2[ladders[i]] = i;
				ladders_start[pos_lad] = i;
				ladders_end[pos_lad] = ladders[i];
				pos_lad++;
			}
		}


		for(int i=0; i<N; i++){
			if(snakes[i] != -1){
				snakes2[snakes[i]] = i;
			}
		}

		while(!q2.isEmpty()){
			int size = q2.size();

			for(int i=0; i<size ; i++){
				int temp = q2.remove();

				for(int j=1; j<=6 && temp-j >= 0; j++){
					if(visited2[temp-j] == 0){
						forward2[temp-j] = count;
						boolean sl = false;
						visited2[temp-j] = 1;
						int temp2 = temp-j;
						while(temp2 >=0 && (ladders2[temp2] != -1 || snakes2[temp2] != -1)){
							sl = true;

							if(ladders2[temp2] != -1){
								temp2 = ladders2[temp2];
								if(visited2[temp2]==0){

									visited2[temp2] = 1;

									forward2[temp2] = count;

								}
							}else{

								temp2 = snakes2[temp2];
								if(visited2[temp2]==0){

									visited2[temp2] = 1;

									forward2[temp2] = count;

								}
								
							}

							
						}
						if(sl == false) {
							q2.add(temp-j);
						}
						else {
							q2.add(temp2);
							sl = false;
						}
					}
				}
				
				
				
			}
			count++;
		}



	}
    
	public int OptimalMoves()
	{
		/* Complete this function and return the minimum number of moves required to win the game. */
		
		// System.out.println("optimal " +forward[N]);
		if(forward[N] == Integer.MAX_VALUE-2*N) return -1;
		return forward[N];
	}

	public int Query(int x, int y)
	{
		/* Complete this function and 
			return +1 if adding a snake/ladder from x to y improves the optimal solution, 
			else return -1. */
			// System.out.println("query "+ x+" "+y);
			int y_to_N = forward2[y];
			int zero_x = forward[x];
			
			int query = y_to_N + zero_x;
			if(query< forward[N]) return 1;
			return -1;
	}

	public int Query2(int x, int y)
	{
		/* Complete this function and 
			return +1 if adding a snake/ladder from x to y improves the optimal solution, 
			else return -1. */
			// System.out.println("q2 "+x+" "+ y);
			int y_to_N = forward2[y];
			int zero_x = forward[x];
			
			int query = y_to_N + zero_x;
			// System.out.println("query "+ query);
			if(query< forward[N]) return query;
			return forward[N];
	}

	public int[] FindBestNewSnake()
	{
		int result[] = {-1, -1};
		/* Complete this function and 
			return (x, y) i.e the position of snake if adding it increases the optimal solution by largest value,
			if no such snake exists, return (-1, -1) */
			// System.out.println("ladders intitals ");
			// for(int i=0; i<ladders_end.length; i++){
			// 	if(ladders_start[i] == -1) break;
			// 	System.out.println(ladders_start[i]+ " "+ ladders_end[i]);
			// }
			int moves = forward[N];
			if(moves == -1) moves = 101;
			for(int i=0; i<ladders_end.length; i++){
				int start = ladders_end[i];
				if(start == -1) break;
				while(snakes[start] != -1){
					start = snakes[start];
				}
				if(ladders[start] != -1){
					continue;
				}
				// System.out.println("");
				for(int j=0; j<ladders_start.length; j++){
					if(ladders_start[j] == -1){
						break;
					}
					
					System.out.println("best snake "+ start + " "+ ladders_start[j]);
					// && snakes[ladders_end[i]] == -1
					if(i!= j && ladders_start[j]< start ){
						// System.out.println("QUERY "+ ladders_end[i]+" "+ ladders_start[j]);
						int query = Query2(start, ladders_start[j]);
						System.out.println("qu "+ query+ " mo "+ moves);
						if( query < moves){
							System.out.println("updated ");
							moves = query;
							result[0] = start;
							result[1] = ladders_start[j];
						}
					}
				}
			}

			System.out.println("res "+ result[0]+" "+ result[1]);

		return result;
	}

	public static void main(String[] args) {
		try{
		SnakesLadder sl = new SnakesLadder("E:/SEMESTERS/Semester 6th/COL106/Assignments/A5/v3/A5_starter_code/Q2/input2.txt");
		System.out.println(sl.OptimalMoves());
		System.out.println(sl.Query(56 ,92 ));
		System.out.println(sl.Query(7, 48));

		sl.FindBestNewSnake();
		}catch(Exception e){
			System.out.println(e);
		}

	}

   
}