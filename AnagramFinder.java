import java.io.*;
import java.util.Iterator;

/**
 * Class that finds the anagrams to a given word using a specified data structure and dictionary file and prints them.
 * @author Jessica Fu
 * @version 1.0.1 December 15, 2023
 */
public class AnagramFinder {
    private static String[] dictionary;
    private static MyMap<String,MyList<String>> dataStructure;

    /**
     * Creates a data structure (a map of type AVLTree, BST, or Hash) with the given dictionary
     * that organizes each lexicographically-sorted key and its list of anagrams.
     * The structure of each node/element is "<String,MyList<String>>".
     * @param dictFile the .txt file of the dictionary that will be referred against
     * @param structureType the data structure you would like to use (avl, bst, or hash)
     */
    public static void createMap(File dictFile, String structureType) throws IOException {
        parseDictionary(dictFile);
        insert(structureType);
    }

    /**
     * Parses through the given file and then sets class variable "dictionary" to the newly-created
     * String array.
     * @param dictFile the .txt file of the dictionary that will be parsed through.
     */
    private static void parseDictionary(File dictFile) throws IOException {
        // creating dictionary, and dictionary size (in case resizing is necessary)
        int currentDictSize = 10000;
        String[] localDictionary = new String[currentDictSize];

        BufferedReader reader = new BufferedReader(new FileReader(dictFile));
        String line;
        int count = 0;

        // making an array of all the words from the dictionary file!
        while ((line = reader.readLine()) != null) {
            // resizing array in case it is too small!!
            if (count >= currentDictSize) {
                // picked an arbitrary number, based on the number 466550.
                // could maybe make this number bigger in case we want to make it run faster (but with a tradeoff for space)
                currentDictSize += 5000;
                String[] tempDictionary = new String[currentDictSize];
                System.arraycopy(localDictionary, 0, tempDictionary,0,currentDictSize-5000);
                localDictionary = tempDictionary;
            }
            // adding to array
            localDictionary[count] = line;
            count ++;
        }

        // set class dictionary value!
        dictionary = localDictionary;
    }

    /**
     * Inserts all the words into the data structure given by the structureType.
     * @param structureType the data structure you would like to use (avl, bst, or hash)
     */
    private static void insert(String structureType) {
        switch (structureType) {
            case "avl":
                insertAVL();
                break;
            case "bst":
                insertBST();
                break;
            case "hash":
                insertHash();
                break;
        }
    }

    /**
     * Helper method that inserts all the words into an AVLTree.
     */
    private static void insertAVL() {
        MyMap<String,MyList<String>> avlTree = new AVLTreeMap<>();

        // iterate through every word in dictionary
        for (String word : dictionary) {
            if (word == null) {
                break;
            }

            // make anagram list
            MyList<String> anagramList = new MyLinkedList<>();

            // now to get the key !
            String key = sortLetters(word.toLowerCase());

            // going to check the tree if the value exists, and if so to update the list
            if (avlTree.get(key) != null) {
                anagramList = avlTree.get(key);

                // check whether the word already exists on the anagram list!
                // if it does, return; no need to insert it again.
                for (Iterator<String> iter = avlTree.get(key).iterator(); iter.hasNext(); ) {
                    String i = iter.next();
                    if (i.equals(word)) {
                        return;
                    }
                }
            }

            // adding word to linked list!
            anagramList.add(word);

            // inserting the key-value pairing into the avl tree.
            avlTree.put(key, anagramList);
        }

        // setting our generic dataStructure to our newly made avlTree :)
        dataStructure = avlTree;
    }

    /**
     * Helper method that inserts all the words into a BSTMap.
     */
    private static void insertBST() {
        MyMap<String,MyList<String>> bst = new BSTMap<>();

        // iterating through every word in dictionary
        for (String word : dictionary) {
            if (word == null) {
                break;
            }

            // make anagram list
            MyList<String> anagramList = new MyLinkedList<>();

            // now to get the key !
            String key = sortLetters(word.toLowerCase());

            // going to check the tree if the value exists, and if so to update the list
            if (bst.get(key) != null) {
                anagramList = bst.get(key);

                // check whether the word already exists on the anagram list!
                // if it does, return; no need to insert it again.
                for (Iterator<String> iter = bst.get(key).iterator(); iter.hasNext(); ) {
                    String i = iter.next();
                    if (i.equals(word)) {
                        return;
                    }
                }
            }

            // adding word to linked list!
            anagramList.add(word);

            // inserting the key-value pairing into the bst.
            bst.put(key, anagramList);
        }

        // setting our generic dataStructure to our newly made bst :)
        dataStructure = bst;
    }

    /**
     * Helper method that inserts all the words into a HashMap.
     */
    private static void insertHash() {
        MyMap<String,MyList<String>> hashTable = new MyHashMap<>();

        // iterating through every word in dictionary
        for (String word : dictionary) {
            if (word == null) {
                break;
            }

            // make anagram list
            MyList<String> anagramList = new MyLinkedList<>();

            // now to get the key !
            String key = sortLetters(word.toLowerCase());

            // going to check the tree if the value exists, and if so to update the list
            if (hashTable.get(key) != null) {
                anagramList = hashTable.get(key);

                // check whether the word already exists on the anagram list!
                // if it does, return; no need to insert it again.
                for (Iterator<String> iter = hashTable.get(key).iterator(); iter.hasNext(); ) {
                    String i = iter.next();
                    if (i.equals(word)) {
                        return;
                    }
                }
            }

            // adding word to linked list!
            anagramList.add(word);

            // inserts pair into hashTable.
            hashTable.put(key, anagramList);
        }

        // setting our generic dataStructure to our newly made hashTable :)
        dataStructure = hashTable;
    }

    /**
     * Sorts the given word lexicographically and without case sensitivity, using a modified insertion sort.
     * @param word the word that is to be sorted
     * @return the sorted, lowercase string (to be used as a key in the insert() methods!)
     */
    private static String sortLetters(String word) {
        char[] arr = new char[word.length()];

        // splitting word into char array
        for (int i = 0; i < word.length(); i++) {
            arr[i] = word.charAt(i);
        }

        // modified version of insertionSort we used in class!
        arr = (char[]) insertionSort(arr);

        // returns the pair of key and word (NOTABLY: NOT LINKED LIST! INSERTION WILL HAPPEN LATER)
        // also checks null condition!
        return String.copyValueOf(arr != null ? arr : new char[0]);

    }

    /**
     * A modified version of insertion sort used in class, with two possible ways of running,
     * either with a string array or a char array.
     * @param obj the array that is to be sorted.
     * @return a sorted list of either type string or char
     */
    private static Object insertionSort(Object obj) {

        // if we're sorting the final array of strings
        if (obj instanceof String[]) {
            String[] arr = (String[]) obj;

            // modified version of insertion sort we used in class :)!
            for (int i = 1, len = arr.length; i < len; ++i) {
                int k;
                String current = arr[i];
                for (k = i - 1; k >= 0 && arr[k].compareTo(current) > 0; --k) {
                    arr[k + 1] = arr[k];
                }
                arr[k + 1] = current;
            }

            return arr;

        } else if (obj instanceof char[]) { // if we're sorting key letters
            char[] arr = (char[]) obj;

            // modified version of insertion sort we used in class :)!
            for (int i = 1, len = arr.length; i < len; ++i) {
                int k;
                char current = arr[i];
                for (k = i - 1; k >= 0 && arr[k] > current; --k) {
                    arr[k + 1] =  arr[k];
                }
                arr[k + 1] = current;
            }

            return arr;
        }

        // if somehow we're using it improperly?
        return null;
    }

    /**
     * Gets the anagrams of the given word and returns + prints out the words
     * The print and return statements both return arrays that exclude the original word
     * and the list is lexicographically sorted (case-sensitive)
     * @param word the word that is to be sorted
     * @return an array of lexicographically sorted anagrams
     */
    public static String[] getAnagrams(String word) {
        // get key of our given word!
        String givenKey = sortLetters(word.toLowerCase());
        MyList<String> anagrams = dataStructure.get(givenKey);

        // check if no anagrams are found
        // either if anagrams == null, is empty, or if there is only one word (which is itself!)
        if (anagrams == null || anagrams.isEmpty() || (anagrams.size() == 1 && anagrams.get(0).equals(word))) {
            System.out.println("No anagrams found.");
            return null;
        }

        // make an array of length of linked list!
        String[] anagramsList = new String[anagrams.size()];

        // turning linked list into array!
        int j = 0;
        for (Iterator<String> it = anagrams.iterator(); it.hasNext(); ) {
            String i = it.next();

            // remove the word from anagrams (if it is in the list!)
            // and resize the anagramsList so there are no null values!
            if (i.equals(word)) {
                String[] newAnagramsList = new String[anagramsList.length-1];
                System.arraycopy(anagramsList, 0, newAnagramsList,0, anagramsList.length - 1);
                anagramsList = newAnagramsList;
                continue;
            }

            anagramsList[j] = i;
            j++;
        }

        // sorting array lexicographically using insertion sort done in class
        anagramsList = (String[]) insertionSort(anagramsList);

        // printing out the anagrams !! woohoo
        if (anagramsList != null) {
            for (String anagram : anagramsList) {
                System.out.println(anagram);
            }
        }

        // returning
        return anagramsList;
    }


    public static void main(String[] args) throws IOException {
        // validating input
        if (args.length != 3) {
            System.out.println("Usage: java AnagramFinder <word> <dictionary file> <bst|avl|hash>");
            System.exit(1);
        }

        File dictionary = new File(args[1]);
        if (!dictionary.exists()) {
            System.out.println("Error: Cannot open file '" + args[1] +"' for input.");
            System.exit(1);
        }

        if (!(args[2].equals("avl") || args[2].equals("bst") || args[2].equals("hash"))) {
            System.out.println("Error: Invalid data structure '" + args[2] + "' received.");
            System.exit(1);
        }

        String word = args[0];
        String structureType = args[2];

        // creating respective data structure with given dictionary and structure type
        try {
            createMap(dictionary, structureType);
        } catch (IOException e) {
            System.out.println("Error: An I/O error occurred reading '" + args[1] + "'.");
            System.exit(1);
        }

        // getting the anagrams of said word
        getAnagrams(word);

    }
}


