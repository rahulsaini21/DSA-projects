import java.io.*;
import java.util.*;

public class Othello {
    int turn;
    int winner;
    int board[][];
    //add required class variables here

    public Othello(String filename) throws Exception {
        File file = new File(filename);
        Scanner sc = new Scanner(file);
        turn = sc.nextInt();
        board = new int[8][8];
        for(int i = 0; i < 8; ++i) {
            for(int j = 0; j < 8; ++j){
                board[i][j] = sc.nextInt();
            }
        }
        winner = -1;
        //Student can choose to add preprocessing here
    }

    //add required helper functions here
    private int max(int a, int b){
        if(a>b){
            return a;
        }
        return b;
    }
    private int min(int a, int b){
        if(a<b){
            return a;
        }
        return b;
    }

    private boolean isVaild(int[][] copyBoard, int row, int col, int pturn){
        if(copyBoard[row][col] != -1){
            return false;
        }

        int[] hm = {-1,-1, 0, 1, 1, 1, 0,-1};
        int[] vm = {0, -1, -1, -1, 0, 1, 1, 1};

        for(int i=0; i<8; i++){
            int dr = row+vm[i];
            int dv = col+hm[i];

            boolean other = false;
            while (dr >= 0 && dr < 8 && dv >= 0 && dv < 8){
                if (copyBoard[dr][dv] == -1) {
                    break;
                } else if (copyBoard[dr][dv] == pturn) {
                    if (other) {
                        return true;
                    } else {
                        break;
                    }
                } else {
                    other = true;
                    dr += vm[i];
                    dv += hm[i];
                }
            }

        }

        return false;

    }

    private ArrayList<int[]> moves(int[][] copyBoard,int pturn){
        ArrayList<int[]> cubes = new ArrayList<>();

        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(isVaild(copyBoard, i, j, pturn)){
                    cubes.add(new int[] {i, j});
                }
            }
        }

        return cubes;
    }

    private boolean makeMove(int copyboard[][],int row, int col, int pturn, int changeto){

        int[] hm = {-1,-1, 0, 1, 1, 1, 0,-1};
        int[] vm = {0, -1, -1, -1, 0, 1, 1, 1};
        copyboard[row][col] = changeto;
        for(int i=0; i<8; i++){
            int dr = row+vm[i];
            int dv = col+hm[i];

            boolean other = false;
            while (dr >= 0 && dr < 8 && dv >= 0 && dv < 8){
                if (copyboard[dr][dv] == -1) {
                    break;
                } else if (copyboard[dr][dv] == pturn) {
                    if (other) {
                        // System.out.println("true");
                        dr -= vm[i];
                        dv -= hm[i];
                        // System.out.println("dr "+dr+" "+dv);
                        while(dr >= 0 && dr < 8 && dv >= 0 && dv < 8 & copyboard[dr][dv]!=pturn){
                            copyboard[dr][dv] = changeto;
                            dr -= vm[i];
                            dv -= hm[i];
                        }
                        // return true;
                        break;
                    } else {
                        break;
                    }
                } else {
                    other = true;
                    // copyboard[dr][dv] = changeto;
                    dr += vm[i];
                    dv += hm[i];
                }
            }
            // System.out.println("out while ");

        }

        return false;
    }

    private int minMax(int copyBoard[][], int depth, boolean maxP, int pturn, int[] cordi){
        // System.out.println("called minMax "+ depth+ " "+pturn);
        
        // ArrayList<int[]> checkMove = moves(copyBoard, pturn);

        if( depth == 0 ){
            // System.out.println("return val \t \t"+ boardScore(copyBoard));
            return boardScore(copyBoard);
        }
        // for(int i=0; i<checkMove.size(); i++){
        //     System.out.print(checkMove.get(i)[0]+" "+checkMove.get(i)[1]+ " \t");
        // }
        // System.out.println();


        // for(int i=0; i<8;i++){
        //     for(int j=0; j<8; j++){
        //         System.out.print(copyBoard[i][j]+" ");
        //     }
        //     System.out.println();
        // }
        if(pturn == 0){
            // System.out.println("inside if , turn "+ pturn);
            int bm= -1;
            int maxVal = Integer.MIN_VALUE+ 64*2;
            int pVal;
            ArrayList<int[]> moves = moves(copyBoard, pturn);
            // if(moves.size() == 0){

            // }
            // System.out.println("moves size "+ moves.size() );
            // for(int i=0; i<moves.size(); i++){
            //     System.out.print(moves.get(i)[0]+" "+moves.get(i)[1]+ " \t");
            // }
            // System.out.println();
            if(moves.size() == 0) return minMax(copyBoard, depth-1, true, 1, cordi);

            for(int i=0; i<moves.size(); i++){
                // int[][] copyBoard = board;
                int[][] tempBoard = new int[8][8];
                for(int k=0; k<8; k++){
                    for(int j=0; j<8; j++){
                        tempBoard[k][j] = copyBoard[k][j];
                    }
                }
                // System.out.println("bestMax "+ moves.get(i)[0]+","+moves.get(i)[1]);
                makeMove(tempBoard, moves.get(i)[0], moves.get(i)[1], 0, 0);
                pVal = minMax(tempBoard, depth-1, false, 1, cordi);
                // if(pturn == 0){
                    
                // }else{
                //     makeMove(tempBoard, moves.get(i)[0], moves.get(i)[1], 1, 1);

                //     pVal = minMax(tempBoard, depth-1, false, 0, cordi);

                // }

                // maxVal = max(maxVal, pVal);
                if( maxVal < pVal){
                    // System.out.println("update bm "+ i);
                    bm = i;
                    maxVal = pVal;
                }   
            }
            // System.out.println("bestmove  "+ bm);

            cordi[0] = moves.get(bm)[0];
            cordi[1] = moves.get(bm)[1];
            // System.out.println("bestif "+ cordi[0]+","+cordi[1]);

            return maxVal;
        }

        else{
            // System.out.println("inside else, turn "+ pturn);
            int bm = -1;
            int minVal = Integer.MAX_VALUE- 64*2;
            int pVal;
            ArrayList<int[]> moves = moves(copyBoard, pturn);
            // System.out.println("moves size "+ moves.size() );

            // for(int i=0; i<moves.size(); i++){
            //     System.out.print(moves.get(i)[0]+" "+moves.get(i)[1]+ "\t");
            // }
            // System.out.println();
            if(moves.size() == 0) return minMax(copyBoard, depth-1, true, 0, cordi);
            for(int i=0; i<moves.size(); i++){

                int[][] tempBoard = new int[8][8];
                for(int k=0; k<8; k++){
                    for(int j=0; j<8; j++){
                        tempBoard[k][j] = copyBoard[k][j];
                    }
                }

                // System.out.println("bestMin "+ moves.get(i)[0]+","+moves.get(i)[1]);
                makeMove(tempBoard, moves.get(i)[0], moves.get(i)[1], 1, 1);
                pVal = minMax(tempBoard, depth-1, true, 0, cordi);
                // if(pturn == 0){
                    
                // }else{
                //     makeMove(tempBoard, moves.get(i)[0], moves.get(i)[1], 1,1);
                //     pVal = minMax(tempBoard, depth-1, true, 0, cordi);

                // }

                // minVal = min(minVal, pVal);

                if( minVal > pVal){
                    // System.out.println("update bm "+ i);
                    bm = i;
                    minVal = pVal;

                }
            }
            // System.out.println(bm+ " "+ moves.size());

            cordi[0] = moves.get(bm)[0];
            cordi[1] = moves.get(bm)[1];
            
            // System.out.println("bestmove  "+ bm);
            // System.out.println("bestelse "+ cordi[0]+","+cordi[1]);
            return minVal;
        }

    }




    public int boardScore() {
        /* Complete this function to return num_black_tiles - num_white_tiles if turn = 0, 
         * and num_white_tiles-num_black_tiles otherwise. 
        */
        // System.out.println("boardScore called ");
        int cW = 0;
        int cB = 0;

        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(this.board[i][j] == 1){
                    cW++;
                }
                else if(this.board[i][j] == 0){
                    cB++;
                }
            }
        }
        if(this.turn == 1) return cW-cB;
        return cB-cW;
    }

    private int boardScore(int[][] copyBoard) {
        /* Complete this function to return num_black_tiles - num_white_tiles if turn = 0, 
         * and num_white_tiles-num_black_tiles otherwise. 
        */
        int cW = 0;
        int cB = 0;

        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(copyBoard[i][j] == 1){
                    cW++;
                }
                else if(copyBoard[i][j] == 0){
                    cB++;
                }
            }
        }

        return cB-cW;

    }

    public int bestMove(int k) {
        /* Complete this function to build a Minimax tree of depth k (current board being at depth 0),
         * for the current player (siginified by the variable turn), and propagate scores upward to find
         * the best move. If the best move (move with max score at depth 0) is i,j; return i*8+j
         * In case of ties, return the smallest integer value representing the tile with best score.
         * 
         * Note: Do not alter the turn variable in this function, so that the boardScore() is the score
         * for the same player throughout the Minimax tree.
        */
        // System.out.println("print k "+k );
        // System.out.println("called best move "+ k);
        // for(int i=0; i<8; i++){
        //     for(int j=0; j<8; j++){
        //         if(board[i][j] == -1) System.out.print("- ");
        //         else System.out.print(board[i][j]+" ");
        //     }
        //     System.out.println();
        // }

        int[] cordi = {-1, -1};
        minMax(board, k, false, turn, cordi);
        // if(turn == 1){
            
        // }else{
        //     minMax(board, k, true, turn, cordi);

        // }
        
        if(cordi[0] == -1) return -1;

        return 8*cordi[0]+cordi[1];
    }

    public ArrayList<Integer> fullGame(int k) {
        /* Complete this function to compute and execute the best move for each player starting from
         * the current turn using k-step look-ahead. Accordingly modify the board and the turn
         * at each step. In the end, modify the winner variable as required.
         */
        // return new ArrayList<Integer>();
        ArrayList<Integer> fg = new ArrayList<>();
        ArrayList<int[]> move = moves(board, turn);
        if(move.size() == 0){
            if(turn == 0){
                move =moves(board, 1);
            }
            else{
                move = moves(board, 0);
            }
        }



        while(move.size() != 0){
            int[] cordi = {-1, -1};
            minMax(board, k, false, turn, cordi);
 
            fg.add((8*cordi[0]+cordi[1]));
            makeMove(board, cordi[0], cordi[1], turn, turn);

            if(turn == 0) turn =1;
            else turn =0;
            move = moves(board, turn );

            if(move.size() == 0){
                if(turn == 0) turn =1;
                else turn =0;
                move = moves(board, turn );
           }
            
        }

        if(boardScore(board)>=0){
            this.winner = 0;
        }else{
            this.winner = 1;
        }
        // System.out.println("winner "+ this.winner + " "+boardScore());
        return fg;
    }

    public int[][] getBoardCopy() {
        int copy[][] = new int[8][8];
        for(int i = 0; i < 8; ++i)
            System.arraycopy(board[i], 0, copy[i], 0, 8);
        return copy;
    }

    public int getWinner() {
        return winner;
    }

    public int getTurn() {
        return turn;
    }

    // public static void main(String[] args) throws Exception {
    //     Othello ot = new Othello("E:/SEMESTERS/Semester 6th/COL106/Assignments/A6/v2/A6_starter_code_v2/Q2/test1.txt");

    //     // ArrayList<int[]> move = ot.moves(ot.board, 1);
    //     // for(int i=0; i<move.size(); i++){
    //     //     System.out.println(move.get(i)[0]+" "+move.get(i)[1]);
    //     // }   
    //     int[] cordi = {-1,-1};
    //     // ArrayList<Integer> move = ot.fullGame(4);
    //     // System.out.println(move);
    //     ot.minMax(ot.board, 4, true,1, cordi);
        
    //     System.out.println("bestr "+ cordi[0]+","+cordi[1]);
    //     // System.out.println("bestr " );
    //     // ot.makeMove(ot.board, 2, 4, 1,1);
    //     // for(int k=0; k<8;k++){
    //     //     for(int j=0; j<8; j++){
    //     //         System.out.print(ot.board[k][j]+" ");
    //     //     }
    //     //     System.out.println();
    //     // }

              
    // }
}