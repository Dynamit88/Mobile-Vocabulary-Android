/**
 * @author: Ivan Mykolenko
 * @date: 24.04.2019
 */
package ivan.vocabulary.DB;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * Class to facilitate work with vocabulary records retrieved from the database.
 */
public class Word {
    private int id;
    private String word, meanings;
    private boolean undoDelition = false;

    public Word(){}

    public Word(String word){
        this.word = word;
    }

    /**
     * Constructor
     * @param word (String) - word
     * @param meanings(String) string of meanings separated with comas
     */
    public Word(String word, String  meanings){
        this.word = word;
        this.meanings = verifyMeanings(meanings);
    }

    /**
     * Constructor
     * @param word (String) - word
     * @param meanings (ArrayList<String>) list of meanings
     */
    public Word(String word, ArrayList<String> meanings){
        this.word = word;
        this.meanings = verifyMeanings(meanings);
    }


    /**
     * Used to retrieve words from the database
     * @param id (int) - database record id
     * @param word (String) - word
     * @param meanings(String) string of meanings separated with comas
     */
    public Word(int id, String word, String meanings){
        this.id = id;
        this.word = word;
        this.meanings = meanings;
    }


    /**
     * Helper function to verify correctness of each meaning entry in the meanings string.
     * @param meanings (String) input
     * @return (String) output
     */
    private String verifyMeanings (String meanings){
        String result = "";
        String [] meaningsArray = meanings.split(",");
        int i = 0;
        int l = meaningsArray.length-1;
        for(;i<l;i++){
            String trimmed = meaningsArray[i].trim();
            if(!trimmed.matches("")){
                result += trimmed + ",";
            }
        }
        result += meaningsArray[l]; //append last string without coma
        return result;
    }


    /**
     * Helper function to verify correctness of each meaning entry in the meanings list.
     * @param meanings (ArrayList<String>) input
     * @return (String) output
     */
     private String verifyMeanings (ArrayList<String> meanings){
            int i = 0;
            String result = "";

             for(; i < meanings.size()-1;i++){ // Trim each meaning
                 String m = meanings.get(i).trim().replace(",", "");
                 if(!m.equals("")){ // Check that it is not empty
                     result += m + ",";
                 }
             }
            result += meanings.get(meanings.size()-1); //append last string without coma
            return result;
        }


    /**
     * Splits string into an array and returns the array
     * @return (String[]) - array of meanings
     */
    public String[] getArrayOfMeanings(){
        if(!meanings.equals("")) {
            return meanings.split(",");
        }
        return null;
    }
    /**
     * Splits string into a list and returns it
     * @return (ArrayList<String>) - list of meanings
     */
    public ArrayList<String> getListOfMeanings(){
        if(!meanings.equals("")) {
            return  new ArrayList<String>((Arrays.asList(meanings.split(","))));
        }
        return null;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeanings() {
        return meanings;
    }

    public void setMeanings(String meanings) {
        this.meanings = meanings;
    }
    public boolean getUndo (){
        return undoDelition;
    }
    public void undo (){
        undoDelition = true;
    }
}
