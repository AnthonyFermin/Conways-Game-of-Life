package nyc.c4q.AnthonyFermin;

import java.io.File;
import java.util.Scanner;

/**
 * Created by c4q-anthonyf on 4/18/15.
 */
public class Main
{

    public static void printBoard(char[][] board, final int ROWS, final int COLUMNS, AnsiTerminal terminal){

        terminal.setTextColor(AnsiTerminal.Color.CYAN);

        for(int i = 1; i < ROWS; i++){

            for(int j = 1; j < COLUMNS; j++){

                terminal.moveTo( i, j );
                terminal.write(Character.toString(board[i][j]) );
            }
        }
    }

    public static char[][] clearBoard(char[][] board){

        for (int i = 0; i <  board.length; i++)
        {
            for(int j = 0; j < board[i].length; j++){
                board[i][j] = ' ';
            }
        }
        return board;
    }

    public static boolean shouldPopulate(int row, int col, char organism, char[][] board){

        int neighborCount = 0;

        // top left
        if(board[row-1][col-1] == organism){
            neighborCount++;
        }
        // top
        if(board[row-1][col] == organism){
            neighborCount++;
        }
        // top right
        if(board[row-1][col+1] == organism){
            neighborCount++;
        }
        // right
        if(board[row][col+1] == organism){
            neighborCount++;
        }
        // bottom right
        if(board[row+1][col+1] == organism){
            neighborCount++;
        }
        // bottom
        if(board[row+1][col] == organism){
            neighborCount++;
        }
        // bottom left
        if(board[row+1][col-1] == organism){
            neighborCount++;
        }
        // left
        if(board[row][col-1] == organism){
            neighborCount++;
        }

        if(board[row][col] == organism && (neighborCount == 2 || neighborCount == 3)){
            return true;
        }else {
            return board[row][col] != organism && (neighborCount == 3); // if spot has no organism and has 3 neighbors return true, else return false
        }

    }

    public static boolean boardsEqual(char[][] prevBoard, char[][] nextBoard){
        final int ROWSIZE = prevBoard.length - 2;
        final int COLSIZE = prevBoard[0].length - 2;


        for(int row = 1; row < ROWSIZE; row++){

            for(int col = 1; col < COLSIZE; col++){
                if(prevBoard[row][col] != nextBoard[row][col]){
                    return false;
                }
            }

        }

        return true;

    }

    public static char[][] iterateBoard(char[][] board){
        char[][] nextBoard = new char[board.length][board[0].length];
        clearBoard(nextBoard);
        char organism = 'X';
        char deadOrganism = '.';

        for(int row = 1; row < board.length - 2; row++){

            for(int col = 1; col < board[0].length -2; col++){

                // checks to see if current cell in current position should live or die
                if(shouldPopulate(row, col, organism, board)){
                    nextBoard[row][col] = organism;
                }else{
                    nextBoard[row][col] = deadOrganism;
                }
            }
        }
        return nextBoard;

    }

    public static void main(String[] args)
    {
        final int ROWS = 25;
        final int COLUMNS = 75;

        char[][] board = new char[ROWS+2][COLUMNS+2];

        board = clearBoard(board);

        Scanner consoleReader = new Scanner(System.in);

        System.out.print ("Which file do you want to open? (ie: life5.dat): ");

        String filename = "/Users/c4q-anthonyf/Desktop/accesscode/GameOfLife/src/nyc/c4q/AnthonyFermin/" + consoleReader.next();
        File file = new File(filename);
        Scanner fileReader = null;

        try
        {
            fileReader = new Scanner (file);
        }
        catch (Exception e)
        {
            System.out.print("file " + file + " does not exist");
            System.exit(0);
        }

        for (int i = 1; i <= ROWS; i++)
        {
            String line = fileReader.nextLine();

            for(int j = 1; j <= line.length(); j++){
                board[i][j] = line.charAt(j-1);
            }
        }

        // Create the terminal.
        final AnsiTerminal terminal = new AnsiTerminal();

        // When the program shuts down, reset the terminal to its original state.
        // This code makes sure the terminal is reset even if you kill your
        // program by pressing Control-C.
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            public void run()
            {
                terminal.showCursor();
                terminal.reset();
                terminal.scroll(1);
                terminal.moveTo(ROWS, 0);
            }
        });

        // Clear the screen to black.
        terminal.setBackgroundColor(AnsiTerminal.Color.BLACK);
        terminal.clear();
        // Don't show the cursor.
        terminal.hideCursor();

        while(true)
        {
            char[][] prevBoard = board;
            printBoard(board, ROWS, COLUMNS, terminal);
            board = iterateBoard(board);

            if(boardsEqual(prevBoard, board)){
                break;
            }

            try {
                Thread.sleep(100);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
