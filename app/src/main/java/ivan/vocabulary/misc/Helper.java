/**
 * @author: Ivan Mykolenko
 * @date: 24.04.2019
 */
package ivan.vocabulary.misc;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ivan.vocabulary.DB.DB;
import ivan.vocabulary.DB.Word;
import ivan.vocabulary.R;

/**
 * Helper class that contains all the static methods to be used by all the modules of the system.
 */
public class Helper {
    private static int FONT_SIZE_1 = 12; //12sp
    private static int FONT_SIZE_2 = 15; //15sp
    private static int FONT_SIZE_3 = 18; //18sp
    private static int FONT_SIZE_4 = 20; //20sp
    private static int FONT_SIZE_5 = 23; //23sp
    private static int currentFontSize = FONT_SIZE_3;


    /**
     * Returns the actual font size.
     * @param s (int) font size flag
     * @return (int) font size
     */
    public static int getFontSize(int s){
        switch (s){
            case 1:
                return FONT_SIZE_1;
                case 2:
                    return FONT_SIZE_2;
                case 3:
                    return FONT_SIZE_3;
                case 4:
                    return FONT_SIZE_4;
                case 5:
                    return FONT_SIZE_5;
                default:
                    return FONT_SIZE_3;
        }
    }

    /**
     * Returns the current font size set by the user.
     * @return (int) - fort size
     */
    public static int getCurrentFontSize() {
        return getFontSize(currentFontSize);
    }

    /**
     * Set font size to be used within the application.
     * @param currentFontSize (int) - font size flag
     */
    public static void setCurrentFontSize(int currentFontSize) {
        Helper.currentFontSize = currentFontSize;
    }

    /**
     * Display toast message
     * @param context (Context) - context
     * @param msg (String) - message
     */
    public static void msg (Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Display dialog window with a message
     * @param context (Context) - context
     * @param msg (String) - message
     */
    public static void dialog (Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setCancelable(true);
        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Applies shake animation to an element.
     * @param context (Context) - context
     * @param view (View) - an element
     */
    public static void shakeError(Context context, View view) {
        Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
        view.startAnimation(shake);
    }

    ///////////////////////////TESTING///////////////////////////////////

    /**
     * Shows a dialog window with all the words from a given vocab.
     * @param context (Context) - context
     * @param tableName (String) - table name
     */
    public static void printVocabulary (Context context, String tableName){
        String fullText = "";
        List<Word> words = DB.getInstance(context).getWords(tableName);
        for(Word w: words){
            String log = "ID: " + w.getId() + " Word: " +w.getWord() + " Meanings: " + w.getMeanings() +"\n";
            fullText += log;
        }
        dialog(context,fullText);
    }

    /**
     * Shows a dialog window with all the vocabularies in the database.
     * @param context (Context) - context
     */
    public static void printAllVocabs (Context context){
        String fullText = "";
        ArrayList<String[]> vocabs = DB.getInstance(context).getVocabNamesAndTablesArrayList();
        for (String[] entry : vocabs) {
            fullText += "ID: " +entry[0]+ " Vocab: " + entry[1] + " Table: " + entry[2] + "\n";
        }
        dialog(context,fullText);
    }

    /**
     * Shows a dialog window with all the tables in the database.
     * @param context (Context) - context
     */
    public static void printAllTables (Context context) {
        String fullText = "";
        ArrayList <String> vocabs = DB.getInstance(context).getAllTableNames();
        for (String entry : vocabs) {
            fullText +="Table: " + entry + "\n";
        }
        dialog(context,fullText);
    }




    public static void populate(Context context){



        String spanishWords = "aburrido,bored\n" +
                "acostarse,to go to bed\n" +
                "amable,friendly\n" +
                "antiguo,old\n" +
                "aqua sin gas,still water \n" +
                "asistir a,to attend\n" +
                "aterrizar,to land\n" +
                "aumento,increase\n" +
                "ayudando,to help\n" +
                "bacadillos de jamon,ham sandwich\n" +
                "bajar del avion,get off plane\n" +
                "barato,cheap\n" +
                "barato,cheap\n" +
                "barrios,neighborhoods\n" +
                "bombones,lollypop\n" +
                "calamares,кальмари\n" +
                "callado,quiet person\n" +
                "cerveza,beer\n" +
                "chocolate,chocolate\n" +
                "cita,appointment\n" +
                "clima,weather\n" +
                "contabilidad,accountancy\n" +
                "contestar el telefono,to answer telephone\n" +
                "contrado,contract\n" +
                "cuarta,quarter\n" +
                "curso de formación,trainng course\n" +
                "de hecho,in fact\n" +
                "deseñador gráfico,graphics designer\n" +
                "despegar,to take off\n" +
                "despertarse,to wake up\n" +
                "divertido,funny\n" +
                "dormir,to sleep\n" +
                "edificio,a building\n" +
                "empezar,to start\n" +
                "encargado,in charge\n" +
                "ensalada,salad\n" +
                "exigente,demanding\n" +
                "exposicione,exhibition\n" +
                "extranjeros,foreign\n" +
                "ferrocarriles,railways\n" +
                "flan,pudding\n" +
                "frutas,fruits\n" +
                "ganado,to earn, to do\n" +
                "gasto,expenditure\n" +
                "hamburguesa,hamburger\n" +
                "helado,ice cream\n" +
                "hielo,ice\n" +
                "hueve,an egg\n" +
                "interurbanos,міжгородній\n" +
                "invertir,to invest\n" +
                "joyerias,jewelery\n" +
                "jueves,thursday\n" +
                "leche,milk\n" +
                "leer,to read\n" +
                "libreria,book store\n" +
                "limonada,lemonade\n" +
                "lunes,monday\n" +
                "manzana,apple\n" +
                "martes,tuesday\n" +
                "me imagino,i imagine\n" +
                "medio de transporte,means of transport\n" +
                "mejor,better\n" +
                "miércoles,wednesday\n" +
                "mundial,world\n" +
                "naranja,orange\n" +
                "nunca,never\n" +
                "ocupado,busy, occupied\n" +
                "oferta,an offer\n" +
                "omnibus,bus\n" +
                "pan,bread\n" +
                "pelicula,a film\n" +
                "perros calientes,hotdogs\n" +
                "pescado,fish\n" +
                "porteños,person from argentina\n" +
                "presupuesto,budget\n" +
                "puerto,port\n" +
                "quedamos,agreement\n" +
                "queso,cheese\n" +
                "recoger el equipaje,check-in baggage\n" +
                "recorrer,to travel\n" +
                "refresco,soft drink\n" +
                "sabado,domingo\n" +
                "sal de juntas,meeting room\n" +
                "salas,rooms\n" +
                "salchichas,sausages\n" +
                "sardinas,sardines\n" +
                "selecta,choice, select\n" +
                "sopa,soup\n" +
                "subir al avion,plane boarding\n" +
                "templado,загартовуватися, tempered\n" +
                "tienda,shop\n" +
                "torta,торт\n" +
                "té,tea\n" +
                "vago,lazy\n" +
                "variada,varied\n" +
                "ventas,sales\n" +
                "viernes,friday\n" +
                "vino tinto,red wine\n" +
                "vista,a view\n" +
                "zapateria,shoe shop\n" +
                "zumo,juice";


        String russianWords =
                "тот,that\n" +
                "быть,to be\n" +
                "весь,all, everything\n" +
                "она,she\n" +
                "ты,you\n" +
                "мы,we\n" +
                "сказать,to say\n" +
                "человек ,man, person\n" +
                "один,one, some, alone\n" +
                "только,only, merely, but\n" +
                "говорить,to say, to tell, to speak";


        String[] spanishWordsA = spanishWords.split(System.getProperty("line.separator"));


        DB.getInstance(context).createVocabulary("Spanish");
        DB.getInstance(context).createVocabulary("German");
        DB.getInstance(context).createVocabulary("Russian");
        DB.getInstance(context).createVocabulary("Dutch");
        DB.getInstance(context).createVocabulary("Technical terms");

        Scanner scanner = new Scanner(spanishWords);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String parts[] = line.split(",", 2);
            if(parts.length > 1) {
                DB.getInstance(context).addWord(new Word(parts[0], parts[1]), "Spanish");
            }
            else{
                DB.getInstance(context).addWord(new Word(parts[0]), "Spanish");
            }
        }

        scanner.close();
        scanner = new Scanner(russianWords);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String parts[] = line.split(",", 2);
            if(parts.length > 1) {
                DB.getInstance(context).addWord(new Word(parts[0], parts[1]), "Russian");
            }
            else{
                DB.getInstance(context).addWord(new Word(parts[0]), "Russian");
            }
        }

        scanner.close();
    }
}
