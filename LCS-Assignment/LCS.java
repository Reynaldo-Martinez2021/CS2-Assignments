/* Reynaldo Martinez
 * Dr. Andrew Steinberg
 * COP3503 Fall 2022
 * Programming Assignment 4
 */

public class LCS {
    private String firstString;
    private String secondString;
    private int dp[][];

    public LCS(String first, String second) {
        this.firstString = first;
        this.secondString = second;

        dp = new int [firstString.length() + 1] [secondString.length() + 1];
    }

    // method to implement long common subsequence with dynamic programming
    public void lcsDynamicSol() {
        // nested loop to traverse the 2D array dp
        for (int i = 0; i <= firstString.length(); ++i) {
            for (int j = 0; j <= secondString.length(); ++j) {
                // set the first row and column of 2D array to 0's
                if (i == 0 || j == 0)
                    dp [i][j] = 0;
                // case 1: Xm = Ym
                else if (firstString.charAt(i - 1) == secondString.charAt(j - 1))
                    // take the value of the diagonal square and add one
                    dp [i][j] = dp [i - 1][j - 1] + 1;
                // case 2 & 3: Xm ≠ Ym
                else
                    // take max value between the left index (dp [i][j - 1]) and the top index (dp [i - 1][j])
                    dp [i][j] = max(dp [i - 1][j], dp [i][j - 1]);
            }
        }
    }

    public int max(int a, int b) {
        return (a >= b) ? a : b;
    }

    /* method to traverse the 2D array and print the longest common subsequence from the dp array
     * 
     * One caveat for getLCS method:
     * to traverse the dp array correctly for our case 2 we must check if the top index (dp [i - 1][j]) ">=" to
     * the left index (dp [i][j - 1]). Traversal through the array will be different if we used ">" because of how the operator is evaluated.
     * In class Dr. Steiny said when the max function is called if both values are the same the top value is chosen so we must use ">=" to produce the same
     * LCS as shown in class
     */
    public String getLCS() {
        StringBuilder sb = new StringBuilder();

        int i = firstString.length();
        int j = secondString.length();

        // while loop to traverse from bottom right most index (dp [m][n]) of array to the top left most index (dp [0][0])
        while (i > 0 && j > 0) {
            // case 1: in which the letter from first string is equal to letter from second string
            if(firstString.charAt(i - 1) == secondString.charAt(j - 1)) {
                // append the letter to the first index of our string builder so the LCS string is formatted in correct positions
                sb.insert(0, firstString.charAt(i - 1));

                // decrease i and j since we moved diagonal (dp [i - 1][j - 1])
                i--;
                j--;
            // case 2: must check if top value is greater than or equal to left value
            } else if (dp[i - 1][j] >= dp[i][j - 1]) {
                // decrease i by one if we moved up in the array
                i--;
            // case 3: the left value is larger than right value so traverse to the left
            } else {
                // decrease j by one if we moved left in the array
                j--;
            }
            
        }
        
        return sb.toString();
    }
}
