import java.util.*;

/**
 *
 * @author Shabab
 */
public class ABPruningAgent extends Agent {
    
    public ABPruningAgent(String name) {
            super(name);
            // TODO Auto-generated constructor stub
    }

    @Override
    public void makeMove(Game game) {
            // TODO Auto-generated method stub

            try {
                    Thread.sleep(1000);
            } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }
            Mancala mancalaGame = (Mancala) game;
            CellValueTuple best = max(mancalaGame, -10000, 10000, 10);
            if(best.col!=-1)
            {
                    mancalaGame.turn = processPCInput(mancalaGame, best.col);
            }

    }

    private CellValueTuple max(Mancala game, int alpha, int beta, int depth)
    {	
            CellValueTuple maxCVT = new CellValueTuple();
            maxCVT.utility = -100;

            int winner = game.checkForWin();

            //terminal check
            if(winner == 0)
            {
                    maxCVT.utility = 100; //this agent wins
                    return maxCVT;
            }
            else if(winner == 1) 
            {
                    maxCVT.utility = -100; //opponent wins
                    return maxCVT;  
            }
            else if (winner == 2)
            {
                    maxCVT.utility = 0; //draw
                    return maxCVT;  
            }
            
            else if (depth == 0) {
//               System.out.println("Whole depth traversed.");
               maxCVT.utility = heuristic(game);      
               return maxCVT;
            }

            for (int j = 1; j<game.board[0].length-1;j++)
            {
                    if(game.board[0][j]==0) continue;

                    ArrayList temp = new ArrayList();
                    for(int i=0; i<2; i++) {
                        for(int k=1; k<game.board[0].length-1; k++) {
                            temp.add(game.board[i][k]);              //saving current board state
                        }
                    }
                    int tempPCMancala = game.agent[0].mancala;
                    int tempHumanMancala = game.agent[1].mancala;
                    int tempTurn = game.turn;
                    
                    int col = j;
                    
                    int latestTurn = game.turn;
                    int result = processPCInput(game, col);
                    if(latestTurn != result) {                       //the agent gets another chance
                        latestTurn = result;
                        int v = max(game, alpha, beta, depth-1).utility;
                        if(v>maxCVT.utility)
                        {
                                maxCVT.utility=v;
                                maxCVT.col = j;
                        }
                        if(maxCVT.utility >= beta)
                            return maxCVT;
                        alpha = Math.max(alpha, maxCVT.utility);
                    }
//                    game.board[0][j] = role; //temporarily making a move
                    int v = min(game, alpha, beta, depth-1).utility;
                    if(v>maxCVT.utility)
                    {
                            maxCVT.utility=v;
                            maxCVT.col = j;
                    }
                    if(maxCVT.utility >= beta)
                        return maxCVT;
                    alpha = Math.max(alpha, maxCVT.utility);
                    
                    int count=0;
                    for(int i=0; i<2; i++) {            
                        for(int k=1; k<game.board[0].length-1; k++) {
                            game.board[i][k] = (int) temp.get(count);              // reverting back to original state
                            count++;
                        }
                    }
                    game.agent[0].mancala = tempPCMancala;
                    game.agent[1].mancala = tempHumanMancala;
                    game.turn = tempTurn;
                    
            }
            return maxCVT;

    }

    private CellValueTuple min(Mancala game, int alpha, int beta, int depth)
    {	
            CellValueTuple minCVT = new CellValueTuple();
            minCVT.utility = 100;

            int winner = game.checkForWin();

            //terminal check
            if(winner == 0)
            {
                    minCVT.utility = 100; //max wins
                    return minCVT;
            }
            else if(winner== 1) 
            {
                    minCVT.utility = -100; //min wins
                    return minCVT;  
            }
            else if (winner == 2)
            {
                    minCVT.utility = 0; //draw
                    return minCVT;  
            }
            else if (depth == 0) {
//               System.out.println("Whole depth traversed.");
               minCVT.utility = 0;      //will be replaced by heuristic function
               return minCVT;
            }

            for (int j = 1; j<game.board[0].length-1; j++)
            {
                    if(game.board[0][j]==0) continue;

                    ArrayList temp = new ArrayList();
                    for(int i=0; i<2; i++) {
                        for(int k=1; k<game.board[0].length-1; k++) {
                            temp.add(game.board[i][k]);              //saving current board state
                        }
                    }
                    int tempPCMancala = game.agent[0].mancala;
                    int tempHumanMancala = game.agent[1].mancala;
                    int tempTurn = game.turn;
                    
                    int col = j;
                    int latestTurn = game.turn;
                    int result = HumanAgent.processHumanInput(game, col);
                    if(latestTurn != result) {                       //the agent gets another chance
                        latestTurn = result;
                        int v = min(game, alpha, beta, depth-1).utility;
                        if(v<minCVT.utility)
                        {
                                minCVT.utility=v;
                                minCVT.col = j;
                        }
                        if(minCVT.utility <= alpha)
                            return minCVT;
                        beta = Math.min(beta, minCVT.utility);
                    }
//                    game.board[i][j] = minRole();
                    int v = max(game, alpha, beta, depth-1).utility;
                    if(v<minCVT.utility)
                    {
                            minCVT.utility=v;
                            minCVT.col = j;
                    }
                    if(minCVT.utility <= alpha)
                        return minCVT;
                    beta = Math.min(beta, minCVT.utility);
                    
                    int count=0;
                    for(int i=0; i<2; i++) {            
                        for(int k=1; k<game.board[0].length-1; k++) {
                            game.board[i][k] = (int) temp.get(count);              // reverting back to original state
                            count++;
                        }
                    }
                    game.agent[0].mancala = tempPCMancala;
                    game.agent[1].mancala = tempHumanMancala;
                    game.turn = tempTurn;

            }
            return minCVT;

    }

    private int processPCInput(Mancala mancalaGame, int col) {
        int marblesToBeMoved = mancalaGame.board[0][col];
        int j;
                                                        //starting simulation
        mancalaGame.board[0][col] = 0;                  //empty the cell
        
        while (true) {
            if(col!=1) {
                for(j=col-1; j>0 && marblesToBeMoved>0; j--) {
                    if(marblesToBeMoved ==1 && mancalaGame.board[0][j] == 0) {
                        mancalaGame.agent[1].mancala += mancalaGame.board[0][j];
                        mancalaGame.board[1][j] = 0;
                    }

                    mancalaGame.board[0][j]++;
                    marblesToBeMoved--;
                }

//                }
                if(marblesToBeMoved==0) {                       //job completed
                    return mancalaGame.turn;
                }
                else {                                          //some marbles still left
                    col = 1;                                    //move through own mancala and 
                                                                //Human agent's cells in the next loop
                }
            }
            else {                  //move through Human agent's cells in the board
                if(marblesToBeMoved==1) {      //last marble in own mancala!
                    mancalaGame.agent[0].mancala++;
                    return (mancalaGame.turn+1)%2;
                }
                
                else if(marblesToBeMoved > 1) {
                    mancalaGame.agent[0].mancala++;
                    marblesToBeMoved--;        
                }
                
                for(j=1; j<mancalaGame.board[1].length-1 && marblesToBeMoved>0; j++) {
                    mancalaGame.board[1][j]++;
                    marblesToBeMoved--;
                }
                
                if(marblesToBeMoved==0) {      //job completed
                    return mancalaGame.turn;
                }
                else {                                       //move through own cells in next loop
                    col = mancalaGame.board.length-1;        //start from first column again in own cells
                }
            }
        }
    }
    
    private int heuristic(Mancala game) {
        int PCScore = game.agent[0].mancala;
        int HumanScore = game.agent[1].mancala;
        for(int j=1; j<game.board[0].length; j++)
            PCScore += game.board[0][j];
        for(int j=1; j<game.board[1].length; j++)
            HumanScore += game.board[1][j];
        
        if(PCScore>HumanScore)
            return 100;
        else if(HumanScore>PCScore)
            return -100;
        return 0;
    }

    class CellValueTuple
    {
            int col, utility;
            public CellValueTuple() {
                    // TODO Auto-generated constructor stub
//                    row =-1;
                    col =-1;
            }
    }
}
