/** Reynaldo Martinez
 *  Dr. Steinberg
 *  COP3503 Fall 2022
 *  Programming Assignment 2
 */

import java.io.*;
import java.util.ArrayList;

public class MagicMaze {
    private char[][] maze;
    private int mazeRows;
    private int mazeColumns;

    private Position character;     // varaible to track character movement through maze
    private Position exitSquare;    // variable contains location of the exit square
    private boolean hasWon;         // flag variable to determine if character reached 'X'

    private ArrayList<Teleporter> teleporters; // contains the locations of all teleporters in the maze
    

    public MagicMaze(String mazeNumber, int rows, int columns) {
        this.maze = new char[rows][columns];
        this.mazeRows = rows;
        this.mazeColumns = columns;
        hasWon = false;
        this.character = new Position(mazeRows - 1, 0);

        initializeTeleporters(); // initializeTeleporters takes 0.0012005 seconds
        readMaze(mazeNumber);   // readMaze takes 0.000514 seconds 
        findTeleporters();     // findMagicSquares takes 0.0000282 - 0.0000672 seconds
    }

    // method to initialize the arraylist to the maximum number of teleporters
    private void initializeTeleporters() {
        teleporters = new ArrayList<Teleporter>();
        for (int i = 0; i < 10; ++i) {
            teleporters.add(i, new Teleporter());
        }
    }

    // method to read the given maze file and transfer its contents into a 2d char
    private void readMaze(String mazeNumber) {
        try (FileReader fr = new FileReader(mazeNumber); BufferedReader br = new BufferedReader(fr)) {
            StringBuilder sb = new StringBuilder();
            String line;
            int textFileRow = 0;
            
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.getChars(0, sb.length(), maze[textFileRow++], 0);
                sb.setLength(0);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // method to run an n^2 algorithm for finding each teleporter in a given maze
    private void findTeleporters() {
        for (int i = 0; i < mazeRows; i++) {
            for (int j = 0; j < mazeColumns; j++) {
                // if the current spot is an 'X' create a new position for the exit square
                if (maze[i][j] == 'X') {
                    exitSquare = new Position(i, j);
                } else if (maze[i][j] != '*' && maze[i][j] != '@') { // if the current spot is a teleporter create a new teleporter with the i and j variables
                    teleporters.get(Character.getNumericValue(maze[i][j])).addTeleporter(i, j);
                }
            }
        }
    }

    // method wrapper for the recursive method solveMagicMazeR
    public boolean solveMagicMaze() {
        solveMagicMazeR();
        return hasWon;
    }

    // method recursive method for solving a magic maze
    private void solveMagicMazeR() {
        // if the current character position is the exit square set hasWon to true
        if (isExitSquare(character.getRowPosition(), character.getColumnPosition())) {
            hasWon = true;
        } 

        // check if the characters current position is a safe place to move
        if (isSafe(character.getRowPosition(), character.getColumnPosition())) {

            // check to see if the character's current square is a teleporter
            if (isTeleporter(character.getRowPosition(), character.getColumnPosition())) {

                // create a temporary position to hold the current teleporters rowPosition and columnPosition
                Position temp = new Position(character.getRowPosition(), character.getColumnPosition());

                // move character to the next teleporter
                teleporters.get(Character.getNumericValue(maze[temp.getRowPosition()][temp.getColumnPosition()])).moveToOtherTeleporter(character);

                // mark our territory so the program knows we have been to this spot
                maze[temp.getRowPosition()][temp.getColumnPosition()] = 'm'; 

                // recursive call to save the next teleporters location
                solveMagicMazeR();

                // after backtracking set the character to the previous teleporter location
                character.setPosition(temp.getRowPosition(), temp.getColumnPosition());

            } else { // set the current position to 'm' to mark our territory
                maze[character.getRowPosition()][character.getColumnPosition()] = 'm'; 
            }

            // Move DOWN
            character.moveCharacter(2, hasWon); // move character down
            solveMagicMazeR();
            character.moveCharacter(0, hasWon); // backtrack (move character up)

            // Move LEFT
            character.moveCharacter(3, hasWon); // move character left
            solveMagicMazeR();
            character.moveCharacter(1, hasWon); // backtrack (move character right)

            // Move UP
            character.moveCharacter(0, hasWon); // move character up
            solveMagicMazeR();
            character.moveCharacter(2, hasWon); // backtrack (move character down)

            // Move RIGHT
            character.moveCharacter(1, hasWon); // move character right
            solveMagicMazeR();
            character.moveCharacter(3, hasWon); // backtrack (move character left)
        }
    }


    // method for checking if the character is currently on the exitSquare
    private boolean isExitSquare(int x, int y) {
        return (exitSquare.getRowPosition() == x && exitSquare.getColumnPosition() == y);
    }

    // method for checking if the current move is allowed in the game restrictions
    private boolean isSafe(int x, int y) {      
        return (x > -1 && x < mazeRows && y > -1 && y < mazeColumns && maze[x][y] != '@' && maze[x][y] != 'm' && !isExitSquare(x, y));
    }

    // method for checking if the current position in the maze is a numerical value signifying a teleporter
    private boolean isTeleporter(int x, int y) {
        return (Character.getNumericValue(maze[x][y]) > -1 && Character.getNumericValue(maze[x][y]) < 10);
    }

}

// Class to hold two different position variables to signify both teleporters
class Teleporter {
    private Position firstTeleporter;
    private Position secondTeleporter;

    public void addTeleporter(int x, int y) {
        if (checkForNullTeleporter()) {
            firstTeleporter = new Position(x, y);
        } else {
            secondTeleporter = new Position(x, y);
        }
    }

    public boolean checkForNullTeleporter() {
        return (this.firstTeleporter == null);
    }

    public void moveToOtherTeleporter(Position character) {
        if (character.getRowPosition() == firstTeleporter.getRowPosition() && character.getColumnPosition() == firstTeleporter.getColumnPosition()) {
            character.setPosition(secondTeleporter.getRowPosition(), secondTeleporter.getColumnPosition());
        } else {
            character.setPosition(firstTeleporter.getRowPosition(), firstTeleporter.getColumnPosition());
        }
    }
    
}

// a class to hold the rowPosition and columnPosition of a given spot
class Position {
    private int rowPosition;
    private int columnPosition;

    public Position(int x, int y) {
        this.rowPosition = x;
        this.columnPosition = y;
    }

    public int getRowPosition() {
        return rowPosition;
    }

    public int getColumnPosition() {
        return columnPosition;
    }

    public void setPosition(int x, int y) {
        this.rowPosition = x;
        this.columnPosition = y;
    }

    // method to move the character based on int move passed
    // if the variable hasWon is true then character has finished the maze and no other moves are needed
    public void moveCharacter(int move, boolean hasWon) {
        if (hasWon == true) {
            return;
        }

        switch(move) {
            case 0:
                moveCharacterUp();
                break;
            
            case 1:
                moveCharacterRight();
                break;

            case 2:
                moveCharacterDown();
                break;
            
            default:
                moveCharacterLeft();
                break;
        }
    }

    private void moveCharacterUp() {
        this.rowPosition -= 1;
    }

    private void moveCharacterRight() {
        this.columnPosition += 1;
    }

    private void moveCharacterDown() {
        this.rowPosition += 1;
    }

    private void moveCharacterLeft() {
        this.columnPosition -= 1;
    }
}