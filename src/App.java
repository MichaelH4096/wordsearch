import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Random;
import java.nio.file.Files;
import java.nio.file.Paths;


public class App {
    public static void main(String[] args) throws Exception {


    //Gets user params
    Scanner sc = new Scanner(System.in);
    System.out.print("How many rows? ");
    int numRows = sc.nextInt();
    System.out.print("How many columns? ");
    int numColumns = sc.nextInt();
    System.out.print("How long should each word at least be? ");
    int userLength = sc.nextInt();
     
    //creates board of characters
    char[][] board= new char[numRows][numColumns];
    for(int i = 0; i < numRows; i++) {
        board[i] = randomRowChar(numColumns);
    }

    //prints out board of characters
    for(char[] i : board) {
        for(char j : i) {
            System.out.print(j + "  ");
        }
        System.out.print("\n");
    }
    
    //Gets all valid words from words.txt
    String[] dictionary = new String(Files.readAllBytes(Paths.get(".\\words.txt"))).split("[\\n\\s]+"); //reads the words.txt file, then puts every word into a String array
    
    
    ArrayList<String> validWords = new ArrayList<String>(); //Arraylist is used here so COllections.sort() can be used for easy sorting
    HashSet<String> allSubstrings = new HashSet<>(); //Hashset is used here to prevent duplicate substrings from appearing


    HashMap<Integer, String> horizontalStrings = new HashMap<>();
    HashMap<Integer, String> verticalStrings = new HashMap<>();
    HashMap<Integer, String> antiDiagonalStrings = new HashMap<>();
    HashMap<Integer, String> mainDiagonalStrings = new HashMap<>();

    //going through the entire board
    for(int i = 0; i < numRows; i++) {
        for(int j = 0; j < numColumns; j++) {

            horizontalStrings = getString(i, board[i][j], horizontalStrings); //a horizontal row can be identified by its i value
            verticalStrings = getString(j, board[i][j], verticalStrings); //a vertical column can be identified by its j value
            antiDiagonalStrings = getString(i+j, board[i][j], antiDiagonalStrings);//a diagonal going from top right to bottom left can be identified by its i+j value
            mainDiagonalStrings = getString(j-i, board[i][j], mainDiagonalStrings);//a diagonal going from top left to bottom right can be identified by its j-i value


        }
    }
     
    //adding every possible substring going left 
    for(Integer key : horizontalStrings.keySet()) {
        allSubstrings.addAll(getAllSubstring(horizontalStrings.get(key), new HashSet<String>()));
    }
     
    //adding every possible substring going down
    for(Integer key : verticalStrings.keySet()) {
        allSubstrings.addAll(getAllSubstring(verticalStrings.get(key), new HashSet<String>()));
    }

    //adding every possible substring going down-right
    for(Integer key : mainDiagonalStrings.keySet()) {
        allSubstrings.addAll(getAllSubstring(mainDiagonalStrings.get(key), new HashSet<String>()));
    }

    //adding every possible substring going down-left
    for(Integer key : antiDiagonalStrings.keySet()) {
        allSubstrings.addAll(getAllSubstring(antiDiagonalStrings.get(key), new HashSet<String>()));
    }

    //adds all substrings going left, up, up-left, up-right
    allSubstrings.addAll(reverseAllStrings(allSubstrings));


    //checking if each substring is a valid word or not
    for(String substr : allSubstrings) {
        if(substr.length() >= userLength) {
            for(String word : dictionary) {
                if(word.equals(substr)) {
                    validWords.add(word);
                    break;
                }
            }
        }
    }

    //sorting validWords
    Collections.sort(validWords, (word1, word2) -> { //if the lambda expression returns a positive int, word1 goes before word2. if it returns a negative, words2 goes before word1
        if(word1.length() == word2.length()) {
            return word1.compareTo(word2); //sorting alphabetically if lengths are equal
        }
        else {
            return Integer.compare(word1.length(), word2.length()); //sorting by length
        }
    });


    //prints out every word in validWords
    for(String word : validWords) {
        System.out.println(word);
    }

    sc.close();
    }


    //Creates a 1D array of random characters
    public static char[] randomRowChar(int arrLength) {
        Random random = new Random();
        char[] consonants = {'B','C','D','F','G','H','J','K','L','M','N','P','Q','R','S','T','V','W','X','Y','Z'};
        char[] vowels = {'A','E','I','O','U'};
        char[] rowChar = new char[arrLength];

        //filling rowChar with random characters
        for(int i = 0; i < arrLength; i++) {
            if(random.nextFloat() >= 0.3) { //70% chance of it being a consonant
                rowChar[i] = consonants[random.nextInt(21)]; 
            }
            else {
                 rowChar[i] = vowels[random.nextInt(5)];
            }
        }
        return rowChar;
    }


    //Gets all possible substrings given main string. uses recursion
    public static HashSet<String> getAllSubstring(String str, HashSet<String> list) {
        if(str.equals("")) { //base case
            return list;
        }
        for(int i = 0; i < str.length(); i++) {
            list.add(str.substring(0, i+1)); //adds every substring that starts with the first character in string
        }
        str = str.substring(1); //deletes first character from string
        return getAllSubstring(str, list); //recursively calls until string is empty
    }



    //updates the string of a given row/column/diagonal
    public static HashMap<Integer, String> getString(int key, char character, HashMap<Integer, String> data) {
        if(data.get(key) == null) {//prevents null values 
            data.put(key, "");
        }
        data.put(key, data.get(key) + character); //updates the string
        return data;
    }

    public static String reverseString(String str) {
        return new StringBuilder(str).reverse().toString();
    }
    
    public static HashSet<String> reverseAllStrings(HashSet<String> data) {
        HashSet<String> reverseData = new HashSet<String>();

        //iterating through data, reversing each item and adding it to reverseData
        for(String string : data) { 
            reverseData.add(reverseString(string));
        }
        return reverseData;
    }
}
  