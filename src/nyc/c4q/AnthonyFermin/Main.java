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

    public static void main(String[] args)
    {
        final int ROWS = 25;
        final int COLUMNS = 75;

        char[][] board = new char[ROWS+1][COLUMNS+1];

        Scanner consoleReader = new Scanner(System.in);

        System.out.print ("Which file do you want to open?");

        String filename = "/Users/c4q-anthonyf/Desktop/accesscode/unit-1-bootcamp/resources/" + consoleReader.next();
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

            for(int j = 1; j < line.length(); j++){
                board[i][j] = line.charAt(j);
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

        printBoard(board, ROWS, COLUMNS, terminal);
    }
}
