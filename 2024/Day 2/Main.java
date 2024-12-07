import java.util.*;
import java.io.*;

class Main {
    public static ArrayList<ArrayList<Integer>> getList(String fileName){
        File f = new File(fileName);
        ArrayList<ArrayList<Integer>> ret = new ArrayList<>();
        try {
            Scanner fileScanner = new Scanner(f);
            while (fileScanner.hasNextLine()){
                Scanner lineScanner = new Scanner(fileScanner.nextLine());
                ArrayList<Integer> currentReport = new ArrayList<>();
                while (lineScanner.hasNextInt()){
                    currentReport.add(lineScanner.nextInt());
                }
                ret.add(currentReport);
            }
        }catch (FileNotFoundException e){
            System.out.println("File not found");
            System.exit(0);
        }
        return ret;
    }

    public static boolean checkReversed(ArrayList<Integer> report){
        ArrayList<Integer> reverseClone = new ArrayList<>(report);
        Collections.reverse(reverseClone);
        System.out.println("Before: " + report.toString() + " and After: " + reverseClone.toString());
        return isSafe(reverseClone, true, true);
    }

    public static boolean isSafe(ArrayList<Integer> report, boolean dampenerOn, boolean reversed){
        // create a clone for removing elements as needed
        ArrayList<Integer> reportClone = new ArrayList<>(report);
        // determine if it is increasing/decreasing by checking first 2 element (3 if dampener is on)
        String order = "";
        int firstElement = reportClone.get(0);
        int secondElement = reportClone.get(1);
        if (firstElement < secondElement) order = "increasing";
        else if (firstElement > secondElement) order = "decreasing";
        else if (!dampenerOn) return false; // levels cannot be the same
        boolean tolerate = true;
        int i = 0;
        int j = 1;
        while (j < reportClone.size()){
            switch (order){
                case "increasing":
                    if (reportClone.get(i) >= reportClone.get(j) || reportClone.get(j) - reportClone.get(i) > 3){
                        if (dampenerOn){
                            if (tolerate) {
                                reportClone.remove(j);
                                tolerate = false;
                                continue;
                            } else {
                                if (!reversed) {
                                    return checkReversed(report);
                                }
                            }
                        } 
                        return false;
                    }
                    break;
                case "decreasing":
                    if (reportClone.get(i) <= reportClone.get(j) || reportClone.get(i) - reportClone.get(j) > 3){
                        if (dampenerOn){
                            if (tolerate){
                                reportClone.remove(j);
                                tolerate = false;
                                continue;
                            } else {
                                if (!reversed) {
                                    return checkReversed(report);
                                }
                            }
                        } 
                        return false;
                    }
                    break;
                default:
                    if (!reversed) {
                        return checkReversed(report);
                    }
                    break;
            }
            i++;
            j++;
        }
        return true;
    }

    public static int safeCount(ArrayList<ArrayList<Integer>> reports, boolean dampenerOn){
        int count = 0;
        for (ArrayList<Integer> report: reports){
            if (isSafe(report, dampenerOn, false)){
                count++;
            }
        }
        return count;
    }
    public static void main(String[] args){
        ArrayList<ArrayList<Integer>> arrayList = getList("input.txt");
        System.out.printf("The number of safe reports: %d\n", safeCount(arrayList, false));
        System.out.printf("The number of safe reports (dampener on): %d\n", safeCount(arrayList, true));
    }
}