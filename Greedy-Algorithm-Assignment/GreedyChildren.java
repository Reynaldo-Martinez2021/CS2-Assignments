/* Reynaldo Martinez
 * Dr. Steinberg
 * COP3503 Fall 2022
 * Programming Assignment 3
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class GreedyChildren {
    private ArrayList<Integer> greedyFactors;
    private ArrayList<Integer> sweetnessFactors;
    
    private int happyChildren;
    private int angryChildren;

    private int piecesOfCandy;
    private int childrenAmount;
    private String childrenTextFile;
    private String candyTextFile;
    
    public GreedyChildren(int candy, int children, String childrenText, String candyText) {
        this.piecesOfCandy = candy;
        this.childrenAmount = children;
        this.childrenTextFile = childrenText;
        this.candyTextFile = candyText;

        happyChildren = 0;
        angryChildren = 0;

        greedyFactors = new ArrayList<Integer>(childrenAmount);
        sweetnessFactors = new ArrayList<Integer>(piecesOfCandy);

        readTextFile(childrenTextFile, greedyFactors);
        readTextFile(candyTextFile, sweetnessFactors);
    }

    // method to read a text file into an ArrayList
    private void readTextFile(String fileName, ArrayList<Integer> arr) {
        try (FileReader fr = new FileReader(fileName); BufferedReader br = new BufferedReader(fr)) {
            String line = null;

            while ((line = br.readLine()) != null) {
                arr.add(Integer.parseInt(line));
            }

            Collections.sort(arr, Collections.reverseOrder()); // sort the arrayList in reverse order before beginning greedyCandy();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /* Method to implement the greedy solution in maximizing the number of children getting a proper piece of candy
     *
     * List of assumptions:
     * 1. The number of candy pieces to hand out will always be greater than the number of children that are trick or treating
     * 2. It is possible that some children can have the same greedy factor
     * 3. It is poosible that some of the candy pieces can have the same sweetness factor
     * 4. Each greedy child can only recieve one piece of candy only
     * 5. Candy pieces CANNOT be broken into fractions. You can only give a whole piece of candy
     */ 
    public void greedyCandy() {
        int i = 0;
        
        // while loop for the amount of children
        while (i < childrenAmount) {
            // if the greedy factor is higher than any piece of candy we 'waste' our smallest piece of candy
            if (greedyFactors.get(0) > sweetnessFactors.get(0)) {
                greedyFactors.remove(0);
                sweetnessFactors.remove(sweetnessFactors.size() - 1);
                angryChildren++;
            } else {
                greedyFactors.remove(0);
                sweetnessFactors.remove(0);
                happyChildren++;
            }

            i++;
        }
    }

    // method that displays the desired output
    public void display() {
        System.out.println("There are " + happyChildren + " happy children.");
        System.out.println("There are " + angryChildren + " angry children.");
    }
}