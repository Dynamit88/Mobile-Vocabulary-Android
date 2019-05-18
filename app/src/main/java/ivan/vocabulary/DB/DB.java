/**
 * @author: Ivan Mykolenko
 * @date: 24.04.2019
 */
package ivan.vocabulary.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The DB class manages interactions with the SQLite database.
 */
public class DB extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Vocabularies";
    private static final String VOCAB_METADATA_TABLE = "vocab_metadata";
    private static final String META_KEY_ID = "_id";
    private static final String META_KEY_NAME = "name";
    private static final String META_KEY_TABLE_NAME = "t_name";
    private static final String WORD_KEY_ID = "_id";
    private static final String WORD_KEY_WORD = "word";
    private static final String WORD_KEY_MEANINGS = "meanings";
    private static DB instance = null;
    private Context context;


    /**
     * Constructor should be private to prevent direct instantiation.
     * Make call to static factory method "getInstance()" instead.
     */
    private DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context.getApplicationContext();
    }

    /**
     * Use this function to get instance of the DB controller. Can pass null argument to get instance once the DB was initiated.
     * Using the application context will ensure that you don't accidentally leak an Activities.
     * http://developer.android.com/resources/articles/avoiding-memory-leaks.html
     */
    public static DB getInstance(@Nullable Context context) {
        if (instance == null && context != null) {
            instance = new DB(context);
        }
        return instance;
    }

    @Override
    public void onCreate (SQLiteDatabase database) {}

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {}


    /**
     * Creates new vocabulary table
     * @param name (String) - name of the table
     */
    public void createVocabulary(String name){
        if(name != "") {
            SQLiteDatabase db = instance.getWritableDatabase();
            String tableName = removeSpecialChars(name);
            // Save metadata
            ContentValues values = new ContentValues();
            values.put(META_KEY_NAME, name);
            values.put(META_KEY_TABLE_NAME, tableName);
            db.insert(VOCAB_METADATA_TABLE, null, values);

            //Create new vocab table
            db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + " (" + WORD_KEY_ID + " INTEGER PRIMARY KEY, " + WORD_KEY_WORD + " TEXT, " + WORD_KEY_MEANINGS + " TEXT)");
            db.close();
        }
    }

    /**
     * Function to amend vocabulary name.
     * @param id (int) - id of the vocabulary in the database
     * @param newName (String) - new name
     */
    public void changeVocabName(int id, String newName){
        SQLiteDatabase db = instance.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(META_KEY_NAME, newName);
        db.update(VOCAB_METADATA_TABLE, values, WORD_KEY_ID + "=?",new String []{String.valueOf(id)});
        db.close();
    }

    /**
     * Deleting a vocab fro the database.
     * @param tableName(String) - table name
     */
    public void deleteVocabulary(String tableName){
        if(tableName != "" && tableName != null) {
            SQLiteDatabase db = instance.getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS " + tableName);
            db.execSQL("DELETE FROM " + VOCAB_METADATA_TABLE + " WHERE " + META_KEY_TABLE_NAME + "='" + tableName + "'");
            db.close();
        }
    }

    /**
     * Returns list of all the existing vocabs in the system.
     * @return (ArrayList<String>) list of vocabulary names
     */
    public  ArrayList<String> getAllVacabNames (){
        SQLiteDatabase db = instance.getWritableDatabase();
        ArrayList<String> vocabs = new ArrayList<String>();
        Cursor c = db.rawQuery("SELECT " + META_KEY_NAME + " FROM VOCAB_METADATA_TABLE", null);
        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                vocabs.add(c.getString(0));
                c.moveToNext();
            }
        }
        c.close();
        db.close();
        return vocabs;
    }

    /**
     * Returns table names of all the existing vocabs in the system.
     * @return (ArrayList<String>) list of vocabulary table names
     */
    public ArrayList<String> getAllVacabTables (){
        SQLiteDatabase db = instance.getWritableDatabase();
        ArrayList<String> vocabs = new ArrayList<String>();
        Cursor c = db.rawQuery("SELECT " + META_KEY_TABLE_NAME + " FROM VOCAB_METADATA_TABLE", null);
        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                vocabs.add(c.getString(0));
                c.moveToNext();
            }
        }
        c.close();
        db.close();
        return vocabs;
    }

    /**
     * Returns list of arrays with names and table names of all the existing vocabs in the system.
     * @return (ArrayList<String[]>) list of vocabulary names and their table names
     */
    public ArrayList<String[]> getVocabNamesAndTablesArrayList (){
        String q = "SELECT * FROM " + VOCAB_METADATA_TABLE;
        SQLiteDatabase db = instance.getReadableDatabase();
        Cursor cursor = db.rawQuery(q, null);

        ArrayList<String[]> vocabs = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while ( !cursor.isAfterLast() ) {
                vocabs.add(new String[] {cursor.getString(0), cursor.getString(1),cursor.getString(2)});
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return vocabs;
    }

    /**
     * Returns list of Vocabulary objects with names and table names of all the existing vocabs in the system.
     * @return (ArrayList<Vocabulary>) list of vocabulary objects
     */
    public ArrayList<Vocabulary> getVocabNamesAndTablesObjects (){
        String q = "SELECT * FROM " + VOCAB_METADATA_TABLE;
        SQLiteDatabase db = instance.getReadableDatabase();
        Cursor cursor = db.rawQuery(q, null);

        ArrayList<Vocabulary> vocabs = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while ( !cursor.isAfterLast() ) {
                vocabs.add(new Vocabulary(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2)));
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return vocabs;
    }

    /**
     * Returns a Cursor with names and table names of all the existing vocabs in the system.
     * @return (Cursor) of vocabulary names and their table names
     */
    public Cursor getVocabNamesAndTablesCursor (){
        String q = "SELECT * FROM " + VOCAB_METADATA_TABLE;
        SQLiteDatabase db = instance.getReadableDatabase();
        return db.rawQuery(q, null);
    }

    /**
     * Returns a map with tables and names of all the existing vocabs in the system.
     * @return (Map<String, String>) map of vocabulary names and their table names
     */
    public Map<String, String> getVocabNamesAndTablesMap (){
        String q = "SELECT * FROM " + VOCAB_METADATA_TABLE;
        SQLiteDatabase db = instance.getReadableDatabase();
        Cursor cursor = db.rawQuery(q, null);
        Map<String, String> map = new HashMap<>();
        if (cursor.moveToFirst()) {
            while ( !cursor.isAfterLast() ) {
                map.put(cursor.getString(1),cursor.getString(2));
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return map;
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////WORDS/////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Add word to a vocab
     * @param word (Word) - word to add
     * @param tableName (String) - name of the vocab table
     */
    public void addWord (Word word, String tableName){
        SQLiteDatabase db = instance.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WORD_KEY_WORD, word.getWord());
        values.put(WORD_KEY_MEANINGS, word.getMeanings());
        db.insert(tableName, null,values);
        db.close();
    }

    /**
     * Retrieving words form a vocab
     * @param id (int) - id of the word
     * @param tableName (String) - name of the vocab table
     * @return (Word)
     */
    public Word getWord (int id, String tableName){
        SQLiteDatabase db = instance.getReadableDatabase();
        Cursor cursor = db.query(tableName, new String[]{WORD_KEY_ID, WORD_KEY_WORD, WORD_KEY_MEANINGS}, WORD_KEY_ID + "=?", new String []{String.valueOf(id)}, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        Word word = new Word(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
        cursor.close();
        db.close();
        return word;
    }


    /**
     * Retrieve words from a vocabulary in ascending order.
     * @param tableName (String) - table name
     * @return (List<Word>) list of word objects
     */
    public List<Word> getWords(String tableName){
        List<Word> words = new ArrayList<>();
        String q = "SELECT * FROM " + tableName + " ORDER BY " + WORD_KEY_WORD + " ASC";
        SQLiteDatabase db = instance.getWritableDatabase();
        Cursor cursor = db.rawQuery(q, null);
        if(cursor.moveToFirst()){
            do{
                words.add(new Word(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return words;
    }

    /**
     * Update a word record.
     * @param id (int) - id of the word
     * @param updatedWord (Word) - Word object with new data
     * @param tableName (String) - vocab table name
     */
    public void updateWord(int id, Word updatedWord, String tableName){
        SQLiteDatabase db = instance.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WORD_KEY_WORD, updatedWord.getWord());
        values.put(WORD_KEY_MEANINGS, updatedWord.getMeanings());
        db.update(tableName, values, WORD_KEY_ID + "=?",new String []{String.valueOf(id)});
        db.close();
    }

    /**
     * Remove word from the db.
     * @param word (Word) - object to be removed
     * @param tableName (String) - vocab table
     */
    public void deleteWord(Word word, String tableName){
        SQLiteDatabase db = instance.getWritableDatabase();
        db.delete(tableName, WORD_KEY_ID +"=?",new String []{String.valueOf(word.getId())});
        db.close();
    }

    /**
     * Remove word from the db.
     * @param id (int) - if of a record to be removed
     * @param tableName (String) - vocab table
     */
    public void deleteWord(int id, String tableName){
        SQLiteDatabase db = instance.getWritableDatabase();
        db.delete(tableName, WORD_KEY_ID +"=?", new String []{String.valueOf(id)});
        db.close();
    }

    /**
     * Returns the number of words in a vocab
     * @param tableName (String) - vocab table
     * @return (int) number of records
     */
    public int getWordsCount (String tableName){
        String q = "SELECT * FROM " + tableName;
        SQLiteDatabase db = instance.getReadableDatabase();
        Cursor cursor = db.rawQuery(q, null);
        cursor.close();
        db.close();
        return cursor.getCount();
    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////MISC/////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Creates all the necessary tables for the system to work correctly.
     */
    public void initiateTheDB_system (){
        SQLiteDatabase db = instance.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + VOCAB_METADATA_TABLE + " (" + META_KEY_ID + " INTEGER PRIMARY KEY, " + META_KEY_NAME + " TEXT, " + META_KEY_TABLE_NAME + " TEXT)");
        db.close();
    }


    /**
     * Helper function to support DB table naming policy.
     * @param s (String) input
     * @return (String) output
     */
    private String removeSpecialChars(String s){
        String result = s;
        if(result.length() > 15){
            result = result.substring(0,14);
        }
        return result.replaceAll("[-+.^:,]","").replaceAll("\\s+","_");
    }


    /**
     * Returns names of all the existing tables in the system.
     * @return (ArrayList<String>) - a list of all the tables in the database.
     */
    public ArrayList<String> getAllTableNames (){
        SQLiteDatabase db = instance.getWritableDatabase();
        ArrayList<String> vocabs = new ArrayList<String>();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type ='table'", null);
        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                vocabs.add(c.getString(0));
                c.moveToNext();
            }
        }
        c.close();
        db.close();
        return vocabs;
    }

    /**
     * Function to clear a database table.
     * @param tableName
     */
    public void clearTable (String tableName){
        SQLiteDatabase db = instance.getWritableDatabase();
        db.execSQL("DELETE FROM "+ tableName);
        db.close();
    }




}
