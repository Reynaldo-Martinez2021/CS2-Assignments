/* Reynaldo Martinez
   Dr. Steinberg
   COP3503 Fall 2022
   Programming Assignment 1 
*/
import java.io.*;

public class Game {
    private int[][] chessBoard; 
    private char[] computerizedMoves; // stores the computers moves

    private Knight knight; // stores row and col position, and board size
    private int playersMoveCounter; // counter to determine winner at end of game
    private int computersMoveCounter; // tracks the position in char array for computer

    public Game(int boardSize, String fileName) {
        this.chessBoard = new int[boardSize][boardSize];
        this.knight = new Knight(chessBoard.length);
        readMoves(fileName);
    }

    // method to read in the computers text files
    public void readMoves(String fileName) {
        StringBuilder sb = new StringBuilder();
        try (FileReader fr = new FileReader(fileName); BufferedReader br = new BufferedReader(fr)) {
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            computerizedMoves = new char[sb.length()];
            sb.getChars(0, sb.length(), computerizedMoves, 0);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // wrapper method to call appropriate player method
    public int play(int playerToWin) {
        this.computersMoveCounter = this.playersMoveCounter = 0;
        knight.setKnightPositionToZero();

        if (playerToWin == 1)
            return playerOneWins();
        else
            return playerTwoWins();
    }

    // method to handle player 1 strategy
    public int playerOneWins() {
        // check boundaries to determine end of game
        while (knight.checkBoundaries() == 0) {
            playersMoveCounter++;

            // even is players 2 turn & odd is players 1 turn
            if (playersMoveCounter % 2 == 0) {
                knight.processKnightMove(computerizedMoves[computersMoveCounter++]);
            } else {
                checkForWinningMove(1);
            }
        }

        // return 1 if playersMoveCounter is odd else return 2
        return (playersMoveCounter % 2 != 0 ? 1 : 2);
    }

    // method to handle player 2 strategy
    public int playerTwoWins() {
        // check boundaries to determine end of game
        while (knight.checkBoundaries() == 0) {            
            playersMoveCounter++;

            // even is players 2 turn & odd is players 1 turn
            if (playersMoveCounter % 2 == 0) {
                checkForWinningMove(2);
            } else {
                checkForWinningMove(1);
                computersMoveCounter++; // increase computer counter because player 1 strategy
            }
        }
        // return 1 if playersMoveCounter is odd else return 2
        return (playersMoveCounter % 2 != 0 ? 1 : 2);
    }

    // method to determine the winning move for both player 1 and player 2
    public void checkForWinningMove(int playerTwoWin) {
        // if knight is one 'd' move away
        if (knight.getRowPosition() == knight.getBoardSize() - 2 && knight.getColumnPosition() == knight.getBoardSize() - 2)
            knight.processKnightMove('d');

        // if knight is one 'r' move away
        else if (knight.getRowPosition() == knight.getBoardSize() - 1 && knight.getColumnPosition() == knight.getBoardSize() - 2)
            knight.processKnightMove('r');

        // if knight is one 'b' move away
        else if (knight.getRowPosition() == knight.getBoardSize() - 2 && knight.getColumnPosition() == knight.getBoardSize() - 1)
            knight.processKnightMove('b');
            
        // if knight isn't one move away call the respective player function
        else{
            if (playerTwoWin == 1)
                playerOnesNextMove();
            else
                playerTwosNextMove();
        }
    }
    

    // method to determine players 1 next move
    public void playerOnesNextMove() {
        // if knight isn't in the 'deadzone' then move opposite of the current player 2 move
        if (knight.getColumnPosition() < knight.getBoardSize() - 3)
            knight.processKnightMove(knight.moveKnightOppositeDirection(computerizedMoves[computersMoveCounter]));

        // if knight is in 'deadzone' and player 2 next move is 'd' then move 'r'
        else if (computerizedMoves[computersMoveCounter] == 'd')            
            knight.processKnightMove('r');

        // if player 2 next move isn't diagonal move opposite of player 2 move
        else
            knight.processKnightMove(knight.moveKnightOppositeDirection(computerizedMoves[computersMoveCounter]));
    }

    // method to determine players 2 next move
    public void playerTwosNextMove() {
        // if knight isn't in the 'deadzone' then move current player 2 piece
        if (knight.getColumnPosition() < knight.getBoardSize() - 3) 
            knight.processKnightMove(computerizedMoves[computersMoveCounter - 1]);
        
        // if the knights row = column and the next move to make is 'd' then move 'b'
        else if (knight.getColumnPosition() == knight.getRowPosition() && computerizedMoves[computersMoveCounter] == 'd')
            knight.processKnightMove('b');

        // if the knights row position is 2 away from border and the next move is to go 'd' or 'r' then make move 'b' 
        else if (knight.getRowPosition() == knight.getBoardSize() - 2 && (computerizedMoves[computersMoveCounter] == 'd' || computerizedMoves[computersMoveCounter] == 'r'))
            knight.processKnightMove('b');

        // if the knights column is 2 away from border and the next move is 'b' or 'd' then move 'r'
        else if (knight.getColumnPosition() == knight.getBoardSize() - 2 && (computerizedMoves[computersMoveCounter] == 'b' || computerizedMoves[computersMoveCounter] == 'd'))
            knight.processKnightMove('r');

        // else move the knight according to computer moves counter
        else
            knight.processKnightMove(computerizedMoves[computersMoveCounter]);
        
    }



}

// Knight class to handle moving the knight piece 
// and determining is the knight can make valid moves
class Knight {
    private int rowPosition;
    private int columnPosition;
    private int boardSize;

    public Knight(int boardSize) {
        this.boardSize = boardSize;
    }

    // method to set the knight to [0][0] at the start of the game
    public void setKnightPositionToZero() {
        this.rowPosition = 0;
        this.columnPosition = 0;
    }

    public int getRowPosition() {
        return rowPosition;
    }

    public int getColumnPosition() {
        return columnPosition;
    }

    public int getBoardSize() {
        return boardSize;
    }

    // method to determine if the knight piece is at the bottom right corner
    public int checkBoundaries() {
        return (rowPosition < boardSize - 1 || columnPosition < boardSize - 1 ? 0 : 1);
    } 

    // method to start the process of moving the knight piece
    public void processKnightMove(char currentMove) {
        int res = checkForInvalidMoves();

        if (res == 2) {
            moveKnight('b'); // only valid move is down
        } else if (res == 1) {
            moveKnight('r'); // only valid move is right
        } else {
            moveKnight(currentMove); // knight is at position to make any move
        }
    }

    // method to check if the knight is at a position to make an invalid move
    private int checkForInvalidMoves() {
        // if knight is at the rightmost column return 2
        if (rowPosition < boardSize - 1 && columnPosition == boardSize - 1)
            return 2;

        // if knight is at bottom most row return 1
        else if (rowPosition == boardSize - 1 && columnPosition < boardSize - 1)
            return 1;

        else 
            return 0;
        
    }

    // method to return the strategic opposite knight direction
    public char moveKnightOppositeDirection(char currentMove) {
        if (currentMove == 'r') {
            return 'b';
        } else if (currentMove == 'b') {
            return 'r';
        } else {
            return 'd';
        }
    }

    // method to move the knight on the board
    private void moveKnight(char currentMove) {
        if (currentMove == 'r') {
            columnPosition++;
        } else if (currentMove == 'b') {
            rowPosition++;
        } else {
            columnPosition++;
            rowPosition++;
        }
    }
}