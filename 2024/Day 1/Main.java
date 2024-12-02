import java.util.*;
import java.io.*;

class Main {
    public static HashMap<Integer, Integer> numberCounts = new HashMap<>();

    public static ArrayList<ArrayList<Integer>> getList(String fileName){
        File f = new File(fileName);
        ArrayList<Integer> leftArray = new ArrayList<>();
        ArrayList<Integer> rightArray = new ArrayList<>(); 
        ArrayList<ArrayList<Integer>> ret = new ArrayList<>(Arrays.asList(leftArray, rightArray));
        try {
            Scanner s = new Scanner(f);
            int i = 0;
            String array = "left";
            while (s.hasNextInt()) {     
                switch (array) {
                    case "left": 
                        leftArray.add(s.nextInt());
                        array = "right";
                        break;
                    case "right": 
                        rightArray.add(s.nextInt());
                        array = "left";
                        i++;
                        break;
                }
            }
        }catch (FileNotFoundException e){
            System.out.println("File not found");
            System.exit(0);
        }
        return ret;
    }

    public static int totalDifference(ArrayList<ArrayList<Integer>> intArray){
        // Sort both list first
        intArray.get(0).sort(null);
        intArray.get(1).sort(null);
        int index = 0;
        int sum = 0;
        while (index < intArray.get(0).size()){
            sum += Math.abs(intArray.get(0).get(index) - intArray.get(1).get(index));
            index++;
        }       
        return sum;
    }

    public static int getSimilarityScores(ArrayList<ArrayList<Integer>> intArray){
        // Assuming lists are already sorted from part 1
        int sum = 0;
        int j = 0; // index for second list. 
        for (int i: intArray.get(0)){
            if (numberCounts.containsKey(i)){
                sum += numberCounts.get(i);
            } else {
                int count = 0;
                while (intArray.get(1).get(j) <= i){
                    if (intArray.get(1).get(j) == i){
                        count++; 
                    }
                    j++;
                }
                int multiplied = i * count;
                sum += multiplied;
                // save this in the hashmap to be accessed later
                numberCounts.put(i, multiplied);
            }
        }
        return sum;
    }

    public static void main(String[] args){
        // Part 1
        ArrayList<ArrayList<Integer>> intArray = getList("input.txt");
        int total = totalDifference(intArray);
        System.out.printf("Total difference is %d.\n", total);
        // Part 2
        int totalSimilarity = getSimilarityScores(intArray);
        System.out.printf("Total similarity is %d.\n", totalSimilarity);
    }
}