package krimp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import static krimp.ZohaibExperiment.checkAndRemove;
import static krimp.ZohaibExperiment.readItemsetsFromFile;
import static krimp.ZohaibExperiment.sizeInBits;

/**
 *
 * @author HP ElliteBook
 */
public class experiment {

    public static void main(String[] args) throws IOException {
        int sizeOfOriginalDB;
        int randPatterns = 10;
        int minimumItemValue = 0;
        int maximumItemValue = 0;
//        //        String inpp = "inpBig.txt";
        String inpp = "heart.txt";

        ArrayList<Set<Integer>> sequence = new ArrayList<>();
        sequence = readItemsetsFromFile(inpp);
        Set<Integer> allItems = new HashSet<>();

        // Iterate through each set in the sequence and add its items to allItems
        for (Set<Integer> set : sequence) {
            allItems.addAll(set);
        }
        sizeOfOriginalDB = sizeInBits(sequence);
//        System.out.println(allItems);
//        System.out.print(sequence);
        int longestItemSet = 0;
        for (Set<Integer> set : sequence) {
            int max = Collections.max(set);
            int min = Collections.min(set);
            if (max > maximumItemValue) {
                maximumItemValue = max;
            }
            if (minimumItemValue == 0) {
                minimumItemValue = min;
            } else if (min < maximumItemValue) {
                minimumItemValue = min;
            }
            if (set.size() > longestItemSet) {
                longestItemSet = set.size();
            }
        }
        long startTime = System.currentTimeMillis();

        List<List<Integer>> randomPairs = new ArrayList<>();
        for (int i = 0; i < randPatterns; i++) {
            List<Integer> randomSet = new ArrayList<>();
            while (randomSet.size() < randPatterns) {
                int randomNumber = (int) (Math.random() * (randPatterns - 0) + 0);
//            for (List<Integer> ll : randomSet)
                if (!randomSet.contains(randomNumber)) {
                    randomSet.add(randomNumber);
                }
            }
//            Set<Integer> randomSet = generateRandomSet(10, 0, 9);
            randomPairs.add(randomSet);
        }
//        System.out.println(allItems);
        ArrayList<Set<Integer>> finalResult = new ArrayList<>();
        for (int k = 1; k <= 20; k++) {
            long startTimeLocal = System.currentTimeMillis();

            ArrayList<Set<Integer>> nRandomPatternsGenerated = new ArrayList<>();
            nRandomPatternsGenerated = generateNPatterns(allItems, randPatterns, longestItemSet);
//        System.out.println(nRandomPatternsGenerated);

//        for (int i = 0; i < 10; i++) {
//            System.out.println(randomPairs.get(i));
//            for (int j = 0; j < 10; j = j + 2) {
//                System.out.print(randomPairs.get(i).get(j)+" ");
//                System.out.println(randomPairs.get(i).get(j+1));
//
//            }
//
//        }
            Set<Integer> bestItemSet = crossOver(sequence, nRandomPatternsGenerated, randomPairs, randPatterns, sizeOfOriginalDB);
//            System.out.println(bestItemSet);
            finalResult.add(bestItemSet);
            finalResult = compression(sequence, finalResult, sizeOfOriginalDB);
            long endTimeLocal = System.currentTimeMillis();

            float localDuration = (endTimeLocal - startTimeLocal) / 1000F;
            System.out.println(":time taken: " + localDuration + " :seconds");
        }
        long endTime = System.currentTimeMillis();
        float duration = (endTime - startTime) / 1000F;
        System.out.println("Total time taken: " + duration + " seconds");

//        System.out.println(finalResult);
//        finalResult = compression(sequence, finalResult, sizeOfOriginalDB);
//        List<List<Integer>> randomPairs2 = new ArrayList<>();
//        int c = 0;
//        for (List<Integer> set : randomPairs) {
//            List <Integer> num  = new ArrayList<>();
////            System.out.print("Set: ");
//            int fn = -1, sn = -1;
//            for (Integer number : set) {
//                if (fn == -1) {
//                    fn = number;
//                } else {
//                    sn = number;
//                }
//                c++;
//                if (c == 2) {
//                    c = 0;
//                    num.add(fn);
//                    num.add(sn);
//                    randomPairs2.add(num);
////                    System.out.println(fn + " " + sn);
//                    
//                    fn = -1;
//                    sn = -1;
//
//                }
//            }
////            System.out.println();
//        }
////        System.out.println(randomPairs2);
    }

    public static Set<Integer> crossOver(ArrayList<Set<Integer>> sequence, ArrayList<Set<Integer>> nRandomPatternsGenerated, List<List<Integer>> randomPairs, int randPatterns, int sizeOfOriginalDB) {
//        ArrayList<Set<Integer>> mutatedOffspring = new ArrayList<>();
//        System.out.println(nRandomPatternsGenerated);

//        Set<Integer> set1 = nRandomPatternsGenerated.get(0);
//        Set<Integer> set2 = nRandomPatternsGenerated.get(1);
//        System.out.println(set1 + ", " + set2);
//        int minLength = Integer.MAX_VALUE;
//        if (set1.size() < set2.size()) {
//            minLength = set1.size();
//        } else {
//            minLength = set2.size();
//        }
//        Random random = new Random();
//        int cutOffPoint = random.nextInt(minLength - 2) + 2;
//        System.out.println("Cuttof points is : " + cutOffPoint);
//        // Create temporary sets to perform the swap
//        Set<Integer> temp1 = new HashSet<>(set1);
//        Set<Integer> temp2 = new HashSet<>(set2);
//        set1.clear();
//        set2.clear();
//        set1.addAll(temp1.stream().limit(cutOffPoint).toList());
//        set1.addAll(temp2.stream().skip(cutOffPoint).toList());
//        set2.addAll(temp2.stream().limit(cutOffPoint).toList());
//        set2.addAll(temp1.stream().skip(cutOffPoint).toList());
//        System.out.println(nRandomPatternsGenerated);
//        System.out.println(set1 + ", " + set2);
//        if (cutOffPoint == 0) {
//            cutOffPoint = 1;
//        }
//        System.out.println(nRandomPatternsGenerated);
//        System.out.println(randomPairs.size());
//        System.out.println(randomPairs.get(0));\
        int bestItemSet = -1;
        float CR = 0;
        float BCR = 0;
        Set<Integer> globalMaxItemSet = new HashSet<>();
//        System.out.println(globalMaxItemSet.isEmpty());
        for (int i = 0; i < randPatterns; i++) {
//            CR = checkAndRemove(sequence, nRandomPatternsGenerated.get(i), sizeOfOriginalDB);
//            if (BCR == 0) {
//                BCR = CR;
//                bestItemSet = i;
//            } else {
//                if (BCR < CR) {
//                    BCR = CR;
//                    bestItemSet = i;
//                }
//            }
//            System.out.println(CR+", "+nRandomPatternsGenerated.get(i));
            for (int j = 0; j < randPatterns; j = j + 2) {
                //crossover 
                Set<Integer> set1 = nRandomPatternsGenerated.get(j);
                Set<Integer> set2 = nRandomPatternsGenerated.get(j + 1);
                float itemSet1CompressionBefore = ccheckAndRemove(sequence, set1, sizeOfOriginalDB);
                float itemSet2CompressionBefore = ccheckAndRemove(sequence, set2, sizeOfOriginalDB);
                int minLength = Integer.MAX_VALUE;
                if (set1.size() < set2.size()) {
                    minLength = set1.size();
                } else {
                    minLength = set2.size();
                }
                int cutOffPoint;
                if (minLength == 2) {
                    cutOffPoint = 2;
                } else {
                    Random random = new Random();
                    cutOffPoint = random.nextInt(minLength - 2) + 2;
                }

//                cutOffPoint=2;
//                System.out.println("Cuttof points is : " + cutOffPoint);
                // Create temporary sets to perform the swap
                Set<Integer> temp1 = new HashSet<>(set1);
                Set<Integer> temp2 = new HashSet<>(set2);
                Set<Integer> temp3 = new HashSet<>(set1);
                Set<Integer> temp4 = new HashSet<>(set2);
//                set1.clear();
//                set2.clear();
                temp3.addAll(temp1.stream().limit(cutOffPoint).toList());
                temp3.addAll(temp2.stream().skip(cutOffPoint).toList());
                temp4.addAll(temp2.stream().limit(cutOffPoint).toList());
                temp4.addAll(temp1.stream().skip(cutOffPoint).toList());
                Set<Integer> localMaxItemSet = new HashSet<>();
                float itemSet1CompressionAfter = ccheckAndRemove(sequence, temp3, sizeOfOriginalDB);
                float itemSet2CompressionAfter = ccheckAndRemove(sequence, temp4, sizeOfOriginalDB);
                if (itemSet1CompressionAfter > itemSet2CompressionAfter) {
                    localMaxItemSet = temp3;
                } else {
                    localMaxItemSet = temp4;
                }
                if (itemSet1CompressionBefore > itemSet2CompressionBefore) {
                    localMaxItemSet = set1;
                } else {
                    localMaxItemSet = set2;
                }
                if (localMaxItemSet != set1 || localMaxItemSet != set2) {
                    set1.clear();
                    set2.clear();
                    set1.addAll(temp1.stream().limit(cutOffPoint).toList());
                    set1.addAll(temp2.stream().skip(cutOffPoint).toList());
                    set2.addAll(temp2.stream().limit(cutOffPoint).toList());
                    set2.addAll(temp1.stream().skip(cutOffPoint).toList());
                }
                if (globalMaxItemSet.isEmpty()) {
                    globalMaxItemSet = localMaxItemSet;
                }
                if (ccheckAndRemove(sequence, localMaxItemSet, sizeOfOriginalDB) > ccheckAndRemove(sequence, globalMaxItemSet, sizeOfOriginalDB)) {
                    globalMaxItemSet = localMaxItemSet;
                }
//                System.out.println(localMaxItemSet+", "+checkAndRemove(sequence, localMaxItemSet, sizeOfOriginalDB));
            }

        }
//System.out.println("BEST CR : "+BCR+"\nBEST ITEMSET : "+nRandomPatternsGenerated.get(bestItemSet));
//                System.out.println(globalMaxItemSet+", "+checkAndRemove(sequence, globalMaxItemSet, sizeOfOriginalDB));
        return globalMaxItemSet;

    }

    public static ArrayList<Set<Integer>> generateNPatterns(Set<Integer> allItems, int randPatterns, int longestItemSet) {
        Random random = new Random();
        ArrayList<Set<Integer>> nRandomPatternsGenerated = new ArrayList<>();
        boolean flag = true;
//        Set<Integer> randomItemSet = new HashSet<Integer>();
//        randomItemSet.add(null);
        for (int i = 1; i <= randPatterns + 10; i++) {
            Set<Integer> randomItemSet = new HashSet<Integer>();
            int randomNumberOfItemSet = random.nextInt((longestItemSet) - 3) + 3;
//            System.out.println(randomNumberOfItemSet);
            int randomItem = 0;
            while (randomItemSet.size() != randomNumberOfItemSet) {
//                int randomItem = random.nextInt(maxNumber + 1 - minNumber) + minNumber;
                randomItem = new ArrayList<>(allItems).get(new Random().nextInt(allItems.size()));
//                System.out.println(randomItem);
//                flag = true;
//                                    System.out.println("abc");
//                for (Set<Integer> set : nRandomPatternsGenerated) {
//                    System.out.println("abc");
//
//                    if (set.contains(randomItem)) {
//                        flag = false;
//                        break;
//                    }
//                }
//                if (flag == false) {
//                    continue;
//                }
                randomItemSet.add(randomItem);
            }

            nRandomPatternsGenerated.add(randomItemSet);

        }
//        System.out.println(nRandomPatternsGenerated);
//        for (int i = 0; i < 10; i++) {
//            System.out.println(nRandomPatternsGenerated.get(i));
//        }
//        for (int i = 0; i < randPatterns; i++) {
//
//            int FN = (int) (Math.random() * (randPatterns - 0 + 1) + 0);
//            int SN = (int) (Math.random() * (randPatterns - 0 + 1) + 0);
//
//        }
//for (Set<Integer> originalSet : nRandomPatternsGenerated) {
//            Set<Integer> copiedSet = new HashSet<>(originalSet);
//            System.out.println(copiedSet);
//        }
        return nRandomPatternsGenerated;
    }

    public static float ccheckAndRemove(ArrayList<Set<Integer>> sequence, Set<Integer> itemSet, int sizeOfOriginalDB) {
//        System.out.println(sequence);
//        System.out.println(patterns);
        ArrayList<Set<Integer>> dummy = new ArrayList<>();
        for (Set<Integer> originalSet : sequence) {
            Set<Integer> copiedSet = new HashSet<>(originalSet);
            dummy.add(copiedSet);
        }
//            System.out.print(dummy + "\n");
        for (Set<Integer> set : dummy) {
//                System.out.print("set is " + set + "\n");
//                System.out.print("Pattern to remove is " + randomPatternsGenerated.get(j) + "\n");
            set.removeAll(itemSet);
//                System.out.print("DB after removing pattern" + dummy + "\n");

        }
        int sizeAAfter = sizeInBits(dummy);
//            System.out.println("Size after removing the pattern  (" + patterns.get(j) + ") : " + sizeAAfter);
        float CR = sizeOfOriginalDB / (float) sizeAAfter;
        float CP = sizeAAfter / (float) sizeOfOriginalDB * 100;
//            System.out.print("Compression Ratio : " + CR + "(Saved "+(sizeOfOriginalDB-sizeAAfter)+" bits) \n");
//            System.out.println("Compression %: " + CP);

        return CR;
    }

    public static ArrayList<Set<Integer>> compression(ArrayList<Set<Integer>> sequence, ArrayList<Set<Integer>> patterns, int sizeOfOriginalDB) {
//        System.out.println(sequence);
//        System.out.println(patterns);
        ArrayList<Set<Integer>> dummy = new ArrayList<>();
        for (Set<Integer> originalSet : sequence) {
            Set<Integer> copiedSet = new HashSet<>(originalSet);
            dummy.add(copiedSet);
        }
        for (int j = 0; j < patterns.size(); j++) {
//            System.out.print(dummy + "\n");
            for (Set<Integer> set : dummy) {
//                System.out.print("set is " + set + "\n");
//                System.out.print("Pattern to remove is " + randomPatternsGenerated.get(j) + "\n");
                set.removeAll(patterns.get(j));
//                System.out.print("DB after removing pattern" + dummy + "\n");

            }
        }
        int sizeAAfter = sizeInBits(dummy);
//            System.out.println("Size after removing the pattern  (" + patterns.get(j) + ") : " + sizeAAfter);
        float CR = sizeOfOriginalDB / (float) sizeAAfter;
        float CP = sizeAAfter / (float) sizeOfOriginalDB * 100;
//        System.out.print("CR : " + CR + "(Saved " + (sizeOfOriginalDB - sizeAAfter) + " bits) \n");
//        System.out.println("CP: " + CP);
        System.out.print("CR: " + CR + " :CP: " + CP + " :Saved: " + (sizeOfOriginalDB - sizeAAfter));

        return patterns;
    }

}

//This file convert a basic text file into the format tailored for this code
//   public static void main(String[] args) {
//        // Provide the path to your dataset file
//        String inputFilePath = "pumbstars.txt";
//        String outputFilePath = "pumbstart.txt"; // Change this to your desired output file path
//
//        try {
//            // Open the input file using FileReader
//            FileReader inputFileReader = new FileReader(new File(inputFilePath));
//            BufferedReader bufferedReader = new BufferedReader(inputFileReader);
//
//            // Open the output file using FileWriter and PrintWriter
//            FileWriter outputWriter = new PrintWriter(outputFileWriter);
//FileWriter = new FileWriter(new File(outputFilePath));
//            PrintWriter print
//            // Read the file line by line and write to the output file
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                String[] items = line.split(" ");
//
//                // Append "-1" between every item and "-1 -2" at the end
//                StringBuilder modifiedLine = new StringBuilder();
//                for (String item : items) {
//                    modifiedLine.append(item).append(" -1 ");
//                }
//                modifiedLine.append("-2");
//
//
//                // Print each line to the console
////                System.out.println(line);
//
//                // Write each line to the output file
//                printWriter.println(modifiedLine);
//            }
//
//            // Close the readers and writers
//            bufferedReader.close();
//            printWriter.close();
//
//            System.out.println("Output saved to: " + outputFilePath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

