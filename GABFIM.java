package krimp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Zohaib
 * Genetic Algorithm for Efficient Descriptive Pattern Mining
 */
public class GABFIM {
     public static void main(String[] args) throws IOException {
        int sizeOfOriginalDB;
        int randPatterns = 20;
        int minimumItemValue = 0;
        int maximumItemValue = 0;
        int numberOfIterations=20;
        
        List<Integer> randomPairsGA = new ArrayList();
       for (int i=0; i<randPatterns; i++)
           randomPairsGA.add(i);
        Collections.shuffle(randomPairsGA);
        String inpp = "penDigits.txt";
        ArrayList<Set<Integer>> sequence = new ArrayList<>();
        sequence = readItemsetsFromFile(inpp);

        // Set to store all unique items in the dataset
        Set<Integer> population = new HashSet<>();
        // Iterate through each set in the sequence and add its items to allItems
        for (Set<Integer> set : sequence) {
            population.addAll(set);
        }
sizeOfOriginalDB = sizeInBits(sequence);
//System.out.println(sizeOfOriginalDB);

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
 List<List<Integer>> randomPairs = new ArrayList<>();
 long startTimeLocal = System.currentTimeMillis();
        for (int i = 0; i < randPatterns; i++) {
            List<Integer> randomSet = new ArrayList<>();
            while (randomSet.size() < randPatterns) {
                int randomNumber = (int) (Math.random() * (randPatterns - 0) + 0);
                if (!randomSet.contains(randomNumber)) {
                    randomSet.add(randomNumber);
                }
            }
//            Set<Integer> randomSet = generateRandomSet(10, 0, 9);
            randomPairs.add(randomSet);
        }
//        System.out.println(randomPairs.size());

        ArrayList<Set<Integer>> CT = new ArrayList<>();
        CT = generateNPatterns(population, randPatterns, longestItemSet);
        
        //length of code table
        int CTL = (int) CTLength(CT);

        for (int j=0; j<randPatterns;j=j+2){
            for (int k = 1; k <= numberOfIterations; k++) {
            int P1Index = randomPairsGA.get(j);
            int P2Index = randomPairsGA.get(j+1);
            Set<Integer> P1Sol = CT.get(P1Index);
            Set<Integer> P2Sol = CT.get(P2Index);
            
            //crossover operator
            float CRB = coverFunction(sequence, P1Sol,P2Sol, sizeOfOriginalDB );
            Set<Integer>[] crossoverResult = newCrossOver(sequence, CT.get(P1Index), CT.get(P2Index));
            Set<Integer> C1Sol = crossoverResult[0];
            Set<Integer> C2Sol = crossoverResult[1];
            
            //mutation operator
            C1Sol = mutate(C1Sol, population);
            C2Sol = mutate(C2Sol, population);
            float CRA = coverFunction(sequence, C1Sol,C2Sol, sizeOfOriginalDB );
            
            //updating solution based on compression value
            if (CRA < CRB)
            {
                CT.set(P1Index, C1Sol);
                CT.set(P2Index, C2Sol);
              }
        }
            
        }
            //get final result
            getFinalCompression(sequence,CT,sizeOfOriginalDB, CTL );
            long endTimeLocal = System.currentTimeMillis();

            float localDuration = (endTimeLocal - startTimeLocal) / 1000F;
            System.out.println(":time taken: " + localDuration + " :seconds");
    }
    
     public static Set<Integer>[] newCrossOver(ArrayList<Set<Integer>> sequence, Set<Integer> P1, Set<Integer> P2) {
        int cutOffPoint;
        int minLength = Integer.MAX_VALUE;
        if (P1.size() < P2.size()) {
                    minLength = P1.size();
                } else {
                    minLength = P2.size();
                }
                if (minLength == 2) {
                    cutOffPoint = 2;
                } else {
                    Random random = new Random();
                    cutOffPoint = Math.max(2, random.nextInt(Math.max(1, minLength - 1)) + 1);
                }
                 Set<Integer>[] crossoverResult = performCrossover(P1, P2, cutOffPoint);
                 return crossoverResult;
    }
      private static Set<Integer>[] performCrossover(Set<Integer> parent1, Set<Integer> parent2, int cutoff) {
        Set<Integer>[] crossoverResult = new Set[2];
        crossoverResult[0] = new HashSet<>();
        crossoverResult[1] = new HashSet<>();

        // Copy elements up to the cutoff point from parent1 to child1
        for (int i = 0; i < cutoff; i++) {
            crossoverResult[0].add(getElement(parent1, i));
        }

        // Copy elements after the cutoff point from parent2 to child1
        for (int i = cutoff; i < parent2.size(); i++) {
            crossoverResult[0].add(getElement(parent2, i));
        }

        // Copy elements up to the cutoff point from parent2 to child2
        for (int i = 0; i < cutoff; i++) {
            crossoverResult[1].add(getElement(parent2, i));
        }

        // Copy elements after the cutoff point from parent1 to child2
        for (int i = cutoff; i < parent1.size(); i++) {
            crossoverResult[1].add(getElement(parent1, i));
        }

        return crossoverResult;
    }
      private static Integer getElement(Set<Integer> set, int index) {
        return set.stream().skip(index).findFirst().orElse(null);
    }
      
      public static float coverFunction(ArrayList<Set<Integer>> sequence, Set<Integer> C1, Set<Integer> C2, int sizeOfOriginalDB) {
        ArrayList<Set<Integer>> dummy = new ArrayList<>();
        for (Set<Integer> originalSet : sequence) {
            Set<Integer> copiedSet = new HashSet<>(originalSet);
            dummy.add(copiedSet);
        }
        for (Set<Integer> set : dummy) {
            //            if (set.containsAll(C1))
                set.removeAll(C1);
        }
                for (Set<Integer> set : dummy) {
//            if (set.containsAll(C2))
                set.removeAll(C2);
        }
        int sizeAAfter = sizeInBits(dummy);
        float CR = sizeOfOriginalDB / (float) sizeAAfter;
        float CP = sizeAAfter / (float) sizeOfOriginalDB * 100;
        return CP;
    }
          public static Set<Integer> mutate(Set<Integer> C, Set<Integer> allItems) {
        if (C.isEmpty() || allItems.isEmpty()) {
            return C;
        }

        List<Integer> itemList = new ArrayList<>(C);
        int randomIndex = new Random().nextInt(itemList.size());
        int itemToReplace = itemList.get(randomIndex);

        List<Integer> allItemsList = new ArrayList<>(allItems);
        int newItem = allItemsList.get(new Random().nextInt(allItemsList.size()));

        C.remove(itemToReplace);
        C.add(newItem);

        return C;
    }
          public static void getFinalCompression(ArrayList<Set<Integer>> sequence, ArrayList<Set<Integer>> nRandomPatternsGenerated, int sizeOfOriginalDB, int CTL ){
               ArrayList<Set<Integer>> dummy = new ArrayList<>();
        for (Set<Integer> originalSet : sequence) {
            Set<Integer> copiedSet = new HashSet<>(originalSet);
            dummy.add(copiedSet);
        }
        for (Set<Integer> pattern : nRandomPatternsGenerated) {
for (Set<Integer> set : dummy) {
                set.removeAll(pattern);

        }
        }        
         int sizeAAfter = sizeInBits(dummy);
        float CR = sizeOfOriginalDB / (float) sizeAAfter;
        float CP = (sizeAAfter + CTL) / (float) sizeOfOriginalDB * 100;
        CP =CP;
            System.out.println("Compression %: " + CP);

          }
    public static ArrayList<Set<Integer>> generateNPatterns(Set<Integer> allItems, int randPatterns, int longestItemSet) {
        Random random = new Random();
        ArrayList<Set<Integer>> nRandomPatternsGenerated = new ArrayList<>();
        boolean flag = true;
        for (int i = 1; i <= randPatterns ; i++) {
            Set<Integer> randomItemSet = new HashSet<Integer>();
            int randomNumberOfItemSet = random.nextInt((longestItemSet) - 3) + 3;
            int randomItem = 0;
            while (randomItemSet.size() != randomNumberOfItemSet) {
                randomItem = new ArrayList<>(allItems).get(new Random().nextInt(allItems.size()));
                randomItemSet.add(randomItem);
            }

            nRandomPatternsGenerated.add(randomItemSet);

        }
        return nRandomPatternsGenerated;
    }
        public static int sizeInBits(ArrayList<Set<Integer>> data) {
        int totalBits = 0;

        // Overhead of ArrayList
//        totalBits += 32; // Assuming ArrayList overhead is 32 bits
        for (Set<Integer> set : data) {
            // Overhead of HashSet
//            totalBits += 32; // Assuming HashSet overhead is 32 bits

            for (Integer number : set) {
                // Size of each Integer in bits
                totalBits += Integer.SIZE;
            }
        }

        return totalBits;

    }
            public static ArrayList<Set<Integer>> readItemsetsFromFile(String filePath) throws IOException {
        ArrayList<Set<Integer>> seq = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            ArrayList<Set<Integer>> sequence = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                Set<Integer> itemset = new HashSet<>();
                String[] items = line.split(" ");
                for (String item : items) {
                    int value = Integer.parseInt(item);
                    if (value == -2) {
                        // End of itemset
                        seq.add(new HashSet<>(itemset));
//                        itemset.clear();
                    }
                    if (value == -1) {
                        continue;
                        // End of itemset
//                        itemset.clear();
                    } else {
                        itemset.add(value);
                    }
                }
            }
        }
        return seq;

    }  
        private static long CTLength(ArrayList<Set<Integer>> CT) {
        int overheadPerSet = 32; // Assume some overhead per set (adjust as needed)
        int sizePerInteger = 32; // Size per Integer in bits

        int totalSize = 0;

        for (Set<Integer> set : CT) {
            int numIntegers = set.size();
            int setSize = overheadPerSet + (numIntegers * sizePerInteger);
            totalSize += setSize;
        }

        return totalSize;
    }
}
