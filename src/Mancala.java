

/**
 *
 * @author Shabab
 */
public class Mancala extends Game {
    /**
    * The actual game board
    * Contains the number of marbles in each cell
    */
    public int[][] board;
    public int turn;
    public GUI gui;
    public static boolean moveMade;
	
    /**
     * 
     * @param a PC
     * @param b Human
     */
    public Mancala(Agent a, Agent b) {
            super(a,b);
            // TODO Auto-generated constructor stub

            a.setRole(0); // PC
            b.setRole(1); // Human

            name = "Mancala";

            board = new int[2][8];  //small board for testing initially
                                    //first and last columns are Mancalas, so actually 3 columns 
    }
    
    
    @Override
    void initialize(boolean fromFile) {
            // TODO Auto-generated method stub
            for (int i = 0; i < 2; i++) {
                    for (int j = 1; j < board[i].length-1; j++) {
                            board[i][j] = 4;        // 2 marbles initially; can be extended for bigger game.
                    }
            }
            gui = new GUI();
    }
    
    
    
    /**
    * The actual game loop, each player takes turn.
    * The first player is selected randomly
    */
   public void play()
   {
           updateMessage("Starting " + name + " between "+ agent[0].name+ " and "+ agent[1].name+".");
           turn = random.nextInt(2);
           moveMade = false;

           //System.out.println(agent[turn].name+ " makes the first move.");
           initialize(false);

           int latestTurn = (turn+1)%2;
           while(!isFinished())
           {
               showGameState();
                if(latestTurn == turn) {
                    updateMessage("Last marble in own mancala. " + agent[turn].name+ "'s turn again! ");
                }
                else {
                    latestTurn = turn;
                    updateMessage(agent[turn].name+ "'s turn. ");
                }
                if(turn==1) {
                    gui.btn11.setEnabled(true);
                    gui.btn12.setEnabled(true);
                    gui.btn13.setEnabled(true);
                    gui.btn14.setEnabled(true);
                    gui.btn15.setEnabled(true);
                    gui.btn16.setEnabled(true);
                    while(true) {
                        //wait for human agent
                        if(moveMade)
                            break;
                    }
                    System.out.println("Move made by Player: " + HumanAgent.col);
                }
                
                agent[turn].makeMove(this);
                moveMade = false;
                

                turn = (turn+1)%2;
           }

           if (winner != null) {
               showGameState();
               updateMessage(winner.name+ " wins!");
           }
           else	
                   updateMessage("Game drawn.");

   }

    /**
     * Called by the play method. It must update the winner variable. 
     * In this implementation, it is done inside checkForWin() function.
     */
    @Override
    boolean isFinished() {
            // TODO Auto-generated method stub
            if(checkForWin() != -1)
                    return true;
            else return false;
    }


    /**
     * Prints the current board (may be replaced/appended with by GUI)
     */
    @Override
    void showGameState() {
        // TODO Auto-generated method stub
        
        gui.updateGUI(this);
//        gui.btn01.setText("" + board[0][1]);
//        gui.btn02.setText("" + board[0][2]);
//        gui.btn03.setText("" + board[0][3]);
//        gui.btn04.setText("" + board[0][4]);
//        gui.btn05.setText("" + board[0][5]);
//        gui.btn06.setText("" + board[0][6]);
//
//        gui.btn11.setText("" + board[1][1]);
//        gui.btn12.setText("" + board[1][2]);
//        gui.btn13.setText("" + board[1][3]);
//        gui.btn14.setText("" + board[1][4]);
//        gui.btn15.setText("" + board[1][5]);
//        gui.btn16.setText("" + board[1][6]);
//
//        gui.btnPCMancala.setText("" + agent[0].mancala);
//        gui.btnHumanMancala.setText("" + agent[1].mancala);
//
//        gui.lblTurn.setText(agent[turn].name + "'s turn.");
        
        String one = (agent[0].name + "{ ");
        String two = " }" + agent[1].name;
        
        int whitespace = one.length();
        if(two.length()>whitespace)
            whitespace = two.length();
        
        System.out.println();
        for(int k=0; k<2*whitespace+1; k++) {
            System.out.print(" ");
        }
        System.out.println("-------------");
        System.out.print(agent[0].name + "{ " + agent[0].mancala);            //PC Agent's score

        for (int i = 0; i < 2; i++) 
        {
            for(int k=0; k<whitespace; k++) {
                System.out.print(" ");
    
            }
            if (i==1) {
                for(int k=0; k<whitespace+1; k++) {
                    System.out.print(" ");
                }
            }
            else {
//                System.out.print(" ");
            }
            System.out.print("| ");
            for (int j = 1; j < board[i].length-1; j++) 
            {
                System.out.print(board[i][j] + "" + " | ");
            }
            if (i==1) {
                for(int k=0; k<whitespace; k++) {
                    System.out.print(" ");
                }
                System.out.print(agent[1].mancala + " }" + agent[1].name);      //Human Agent's score
            }
            
            System.out.println();
            for(int k=0; k<2*whitespace+1; k++) {
                System.out.print(" ");
            }
            System.out.println("-------------");
        }
    }
	
    /** Loop through all cells of the board and if any one row is found to be empty then return true.
    	Otherwise the game continues.
    */
    public boolean isBoardEmpty() {

        int count=0;
		
        for (int i = 0; i < 2; i++) 
        {
            for (int j = 1; j < board[i].length-1; j++) 
            {
                if (board[i][j] == 0) 
                {
                   count++;
                }
            }
            if(count == board[i].length-2)              //whole row empty
                return true;
            else
                count=0;                //start counting empty cells in next row
        }
		
        return false;
    }
	
	
    /** Returns role of the winner
     * @return role of the winner, if game is going on, returns -1. In case of a draw, returns 2.
     */
    public int checkForWin() 
    {
    	winner = null;
    	int winRole=-1;

        if(isBoardEmpty()) {
            if(countRow(0) > 0) {          //still some marbles for PC
                agent[0].mancala += countRow(0);
            }
            else if(countRow(1) > 0) {          //still some marbles for Human
                agent[1].mancala += countRow(1);
            }
            
            if(agent[0].mancala > agent[1].mancala) {   //PC wins
                winRole = 0;
            }
            else if(agent[0].mancala < agent[1].mancala) {   //Human wins
                winRole = 1;
            }
            else {                          //Match drawn
                winRole = 2;
            }
        }
        
        if(winRole!=-1)
    	{
            if(winRole != 2) {
                winner = agent[winRole];
            }
    	}
        
        return winRole;
    }
    
    
    //Count the number of marbles in a row
    private int countRow(int row) {
        int count=0;
        for(int j=1; j<board[row].length-1; j++) {
            count += board[row][j];
        }
        return count;
    }
	
    
    public boolean isValidCell(int col)
    {
            if(col<1 ||col>board[0].length-2) return false;
            if(board[1][col]==0) return false;        //No marble to move!

            return true;
    }

    @Override
    void updateMessage(String msg) {
            // TODO Auto-generated method stub
            System.out.println(msg);
    }
}
