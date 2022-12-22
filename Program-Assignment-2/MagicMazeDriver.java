/*  Reynaldo Martinez
 *  COP3503 Computer Science 2
 *  Programming Assignment 2 Driver
 *  Fall 2022
 */


public class MagicMazeDriver 
{   
    public static void main(String args[]) throws Exception
    {
        
        MagicMaze maze = new MagicMaze(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        
        if(maze.solveMagicMaze())
			System.out.println(args[0] + " passed!");
		else
			System.out.println(args[0] + " did not passed!");
    }
}
