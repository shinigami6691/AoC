import java.io.*;

public class Main{
    public static char[][] charArray;
    /*
     * directionArray are initial for the direction. 
     * E - East, W - West, N - North S - South
     * Q - NW, R - NE, Z - SW, C - SE
     */
    public static char[] directionArray = new String("EWNSQRZC").toCharArray();
    public static int totalLength = 0;


    public static void initialiseArray(){
        // Reading and extraction
        try{
            FileReader f = new FileReader("input.txt");
            BufferedReader reader = new BufferedReader(f);
            // Get total length
            while (reader.readLine() != null){
                totalLength++;
            }
            f.close();
            reader.close();
        }catch(IOException e){
            System.out.println("Cannot read file");
            System.exit(0);
        }
        charArray = new char[totalLength][];

        //Add the data in each array
        try {
            FileReader f = new FileReader("input.txt");
            BufferedReader reader = new BufferedReader(f);
            int y = 0;
            while (y < totalLength) {
                char[] line = reader.readLine().toCharArray();
                charArray[y++] = line;
            }
            f.close();
            reader.close();
        }catch(IOException e){
            System.out.println("Cannot read file");
            System.exit(0);
        }
    }

    public static boolean outOfBounds(int row, int col){
        //check if the given row and column number will lead to index out of bounds
        //of the input character array
        return (row < 0 || row > charArray[0].length-1 || col < 0 || col > totalLength-1);
    }

    public static int[] nextCheckPoint(int startRow, int startCol, char direction){
        // return int[] containing only one int -1 if and only if it is out of bounds
        // else return 2 integers contained with int[] corresponding to the row and col numbers
        switch (direction){
            case 'E': // East
                if (outOfBounds(startRow, startCol+1)) return new int[]{-1};
                return new int[]{startRow, startCol+1};
            case 'W': // West
                if (outOfBounds(startRow, startCol-1)) return new int[]{-1};
                return new int[]{startRow, startCol-1};
            case 'N': // North
                if (outOfBounds(startRow-1, startCol)) return new int[]{-1};
                return new int[]{startRow-1, startCol};
            case 'S': // South
                if (outOfBounds(startRow+1, startCol)) return new int[]{-1};
                return new int[]{startRow+1, startCol};
            case 'Q': // North West
                if (outOfBounds(startRow-1, startCol-1)) return new int[]{-1};
                return new int[]{startRow-1, startCol-1};
            case 'R': // North East
                if (outOfBounds(startRow-1, startCol+1)) return new int[]{-1};
                return new int[]{startRow-1, startCol+1};
            case 'Z': // South West
                if (outOfBounds(startRow+1, startCol-1)) return new int[]{-1};
                return new int[]{startRow+1, startCol-1};
            case 'C': // South East
                if (outOfBounds(startRow+1, startCol+1)) return new int[]{-1};
                return new int[]{startRow+1, startCol+1};
            default:
                return new int[]{-1};
        }
    }

    public static int countXMAS(int charPos, int row, int col, char direction){
        // Part 1 to get total count of the word 'XMAS' using recursive calls.
        // Each character will have a maximum recursive depth of 8*4 = 32 which is O(1)
        if (charPos == 0){
            // first character must be 'X'. If so, check each direction (8 possible directions) 
            // and add up the total count of each direction recursively
            if (charArray[row][col] == 'X'){
                //check each direction recursively
                int count = 0;
                for (char d: directionArray){
                    int[] coordinate = nextCheckPoint(row, col, d);
                    if (!(coordinate[0] == -1)){
                        count += countXMAS(charPos+1, coordinate[0], coordinate[1], d);
                    }
                }
                return count;
            }
        } else if (charPos == 1){
            // second character must be 'M'. Continue to search in the same direction, otherwise return 0
            if (charArray[row][col] == 'M'){
                int[] coordinate = nextCheckPoint(row, col, direction);
                if (coordinate[0] == -1) return 0;
                return countXMAS(charPos+1, coordinate[0], coordinate[1], direction);
            } else {
                return 0;
            }
        } else if (charPos == 2){
            // third character must be 'A', continue to search in the same direction, otherwise return 0
            if (charArray[row][col] == 'A'){
                int[] coordinate = nextCheckPoint(row, col, direction);
                if (coordinate[0] == -1) return 0;
                return countXMAS(charPos+1, coordinate[0], coordinate[1], direction);
            } else {
                return 0;
            }
        } else if (charPos == 3){
            // last character must be 'S', return count 1 if true, otherwise 0 indicating not 'XMAS'
            // This serves as a base case
            if (charArray[row][col] == 'S'){
                return 1;
            } else {
                return 0;
            }
        }
        return 0;
    }


    public static boolean countMAS(int row, int col){
        // Part 2 - focus on the character 'A' and check for top left, top right, bottom left, bottom right
        // Only return true if all four corners are M-S or S-M diagonally
        // Time complexity : O(1)
        if (charArray[row][col] == 'A'){
            int[] topLeft = nextCheckPoint(row, col, 'Q');
            int[] bottomRight = nextCheckPoint(row, col, 'C');
            if (topLeft[0] == -1 || bottomRight[0] == -1){
                return false;
            } else {
                if (charArray[topLeft[0]][topLeft[1]] == 'M'){
                    if (charArray[bottomRight[0]][bottomRight[1]] != 'S') return false;
                } else if (charArray[topLeft[0]][topLeft[1]] == 'S'){
                    if (charArray[bottomRight[0]][bottomRight[1]] != 'M') return false;
                } else {
                    return false;
                }
            }
            int[] topRight = nextCheckPoint(row, col, 'R');
            int[] bottomLeft = nextCheckPoint(row, col, 'Z');
            if (topRight[0] == -1 || bottomLeft[0] == -1){
                return false;
            } else {
                if (charArray[topRight[0]][topRight[1]] == 'M'){
                    if (charArray[bottomLeft[0]][bottomLeft[1]] != 'S') return false;
                } else if (charArray[topRight[0]][topRight[1]] == 'S'){
                    if (charArray[bottomLeft[0]][bottomLeft[1]] != 'M') return false;
                } else {
                    return false;
                }
            }
            return true;
        } 
        return false;
    }
    public static void main(String[] args){
        initialiseArray();
        int totalCountXMAS = 0;
        int totalCountMAS = 0;
 
        for (int col = 0; col < charArray.length; col++){
            for (int row = 0; row < charArray[col].length; row++){
                totalCountXMAS += countXMAS(0, row, col, 'd');
                if (countMAS(row, col)) totalCountMAS++;
            }
        }
        System.out.printf("Total number of XMAS count is %d\n", totalCountXMAS);
        System.out.printf("Total number of MAS count is %d\n", totalCountMAS);
    }
}