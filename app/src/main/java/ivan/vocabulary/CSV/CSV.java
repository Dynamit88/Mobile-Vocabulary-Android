/**
 * @author: Ivan Mykolenko
 * @date: 24.04.2019
 */
package ivan.vocabulary.CSV;

import android.os.Environment;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import ivan.vocabulary.DB.DB;
import ivan.vocabulary.DB.Vocabulary;
import ivan.vocabulary.DB.Word;


/**
 * The CSV class manages vocabulary exports into the .csv format.
 */
public class CSV {
    private static FileWriter writer = null;
    private static File root = Environment.getExternalStorageDirectory(); // Get file system root dir
    private static String lastFilePath = null;

    /**
     * This function generates a .csv file from a Vocabulary object.
     * @param fileName (String) - the name of the future file
     * @param vocab (Vocabulary) - a vocab to export
     * @return (boolean) - return true on success.
     */
    public static boolean writeCSV(String fileName, Vocabulary vocab){
        // Create new file and open writer
        File gpxfile;
        try {
            gpxfile = new File(root, fileName + ".csv");
            writer = new FileWriter(gpxfile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        // Generate data
        String text = "";
        List<Word> words = DB.getInstance(null).getWords(vocab.getTable());
        for(Word word: words){
            text += String.format("%s,%s\n", word.getWord(),word.getMeanings());
        }

        // Write to the file
        try {
        writer.write(text);
        writer.flush();
        writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        lastFilePath = gpxfile.getPath();
        return true;
    }


    /**
     * Returns path to the previously-generated csv.
     * @return (String) path
     */
    public static String getLastFilePath() {
        return lastFilePath;
    }


}
