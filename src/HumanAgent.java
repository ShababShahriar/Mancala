
import java.util.Scanner;

/**
 *
 * @author Shabab
 */
public class HumanAgent extends Agent {
    static Scanner in = new Scanner(System.in);
    public static int col;
//    public int mancala=0;
    
    public HumanAgent(String name) {
            super(name);
            // TODO Auto-generated constructor stub
    }

    @Override
    public void makeMove(Game game) {
            // TODO Auto-generated method stub
;
//            TickTackToe tttGame = (TickTackToe) game;
            Mancala mancalaGame = (Mancala) game;
            int totalColumn = mancalaGame.board[0].length-2;

            boolean first = true;
//            do
//            {
//                    if(first) {
////                        System.out.println("Insert column (1-" + totalColumn + ")");
//                        first=false;
//                    }
////                    else 
////                        System.out.println("Invalid input! Insert column (1-" + totalColumn + ") again.");
////                    row = in.nextInt();
////                    col = in.nextInt();
//                    
//            }while(!mancalaGame.isValidCell(col));
            
//            System.out.println("Move made: " + col);
            
            mancalaGame.turn = processHumanInput(mancalaGame, col);

    }
    
    /**
     * 
     * @param mancalaGame The game being played at the moment
     * @param col         The column chosen by the human agent
     * @return            opposite agent's index if it's his turn again, otherwise own index
     *                    because this will be switched in the play() method of Mancala class
     */
    
    public static int processHumanInput(Mancala mancalaGame, int col) {
        int marblesToBeMoved = mancalaGame.board[1][col];
        int j, k=0;
        mancalaGame.board[1][col] = 0;                  //empty the cell
        
        while (true) {
            if(col!=mancalaGame.board[1].length-2) {
                for(j=col+1; j<=mancalaGame.board[1].length-2 && marblesToBeMoved>0; j++, k++) {
                    if(marblesToBeMoved ==1 && mancalaGame.board[1][j] == 0) {
                        mancalaGame.agent[1].mancala += mancalaGame.board[0][j];
                        mancalaGame.board[0][j] = 0;
                    }

                    mancalaGame.board[1][j]++;
                    marblesToBeMoved--;
                }

//                }
                if(marblesToBeMoved==0) {        //job completed
                    return mancalaGame.turn;
                }
                else {                                          //some marbles still left
                    col = mancalaGame.board[1].length-2;        //move through own mancala and 
                                                                //PC agent's cells in the next loop
                }
            }
            else {                  //move through PC agent's cells in the board
                if(marblesToBeMoved==1) {      //last marble in own mancala!
                    mancalaGame.agent[1].mancala++;
                    return (mancalaGame.turn+1)%2;
                }
                
                else if(marblesToBeMoved > 1) {
                    mancalaGame.agent[1].mancala++;
                    marblesToBeMoved--;        
                }
                
                for(j=col; j>0 && marblesToBeMoved>0; j--) {
                    mancalaGame.board[0][j]++;
                    marblesToBeMoved--;
                }
                
                if(marblesToBeMoved==0) {      //job completed
                    return mancalaGame.turn;
                }
                else {                         //move through own cells in next loop
                    col = 0;                   //start from first column again in own cells
                }
            }
        }
    }
}
