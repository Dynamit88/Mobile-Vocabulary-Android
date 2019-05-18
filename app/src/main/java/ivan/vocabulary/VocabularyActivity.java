/**
 * @author: Ivan Mykolenko
 * @date: 24.04.2019
 */
package ivan.vocabulary;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import ivan.vocabulary.DB.DB;
import ivan.vocabulary.DB.Word;
import ivan.vocabulary.misc.EditWordListAdapter;
import ivan.vocabulary.misc.SwipeController;
import ivan.vocabulary.misc.SwipeControllerActions;
import ivan.vocabulary.misc.WordsListAdapter;

/**
 * This class manages the vocabulary activity of the application and displays a list of words.
 */
public class VocabularyActivity extends AppCompatActivity {
    private RecyclerView wordList;
    private DB db;
    private String name, table;
    private Button btn_addWord;
    private View.OnClickListener onItemClickListener;
    private WordsListAdapter adapter;
    private ConstraintLayout mainLayout = null;
    private EditWordListAdapter editListAdapter;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);
        wordList = findViewById(R.id.wordList);
        db = DB.getInstance(this);
        name = getIntent().getStringExtra("VOCAB_NAME");
        table = getIntent().getStringExtra("VOCAB_TABLE");
        mainLayout = findViewById(R.id.constraintLayout);
        btn_addWord = findViewById(R.id.btn_addWord);
        btn_addWord.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onCreateNewWord();
            }
        });
        setWordsListAdapter();
        setupRecyclerView();
    }


    @Override
    public void onResume(){
        super.onResume();
        setTitle(name);
    }

    /**
     * Method to set up an instance of WordsListAdapter to display a list of words in the vocabulary activity.
     */
    private void setWordsListAdapter(){
        List<Word> words = db.getWords(table); //Get data from the DB
        // Create click listener
        onItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();  // This viewHolder will have all required values.
            }
        };

        adapter = new WordsListAdapter(words, onItemClickListener);
        wordList.setAdapter(adapter); // Set adapter
    }


    /**
     * Set up RacyclerView layout so to support swipe motions and display a list of words.
     */
    private void setupRecyclerView(){
        SwipeControllerActions swipeActions = new SwipeControllerActions(){
            @Override
            public void onSwipeLeft(final int position) { // Delete item
                final Word word = adapter.getWord(position);
                adapter.removeWord(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, adapter.getItemCount());

                // Create notification
                final Snackbar snackbar = Snackbar.make(mainLayout, word.getWord() + " was removed.", Snackbar.LENGTH_LONG).setActionTextColor(Color.YELLOW);

                // Create 'Undo' button
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapter.restoreItem(word, position);
                        wordList.scrollToPosition(position);
                        word.undo(); // Set undo flag
                    }
                });

                // When notification disappears and the 'Undo' wasn't clicked
                snackbar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if(!word.getUndo()) { // if undo wasn't pressed and flag wasn't set
                            db.deleteWord(word.getId(), table); //Remove from DB
                        }
                    }
                });

                snackbar.show();
            }

            @Override
            public void onSwipeRight(int position) { // Edit item
                onEditWord(adapter.getWord(position), position);
                adapter.notifyItemChanged(position); //reset swipe
            }

        };
        // Set up the SwipeController
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(new SwipeController(this, swipeActions));
        itemTouchhelper.attachToRecyclerView(wordList);
    }






    /**
     * This method manages editing existing words in the system. Swipe right callback function.
     * @param word (Word) - object to edit
     * @param position (int) - the positing of the object in adapter.
     */
    private void onEditWord(final Word word, final int position) {
        ArrayList<String> meanings = word.getListOfMeanings();
        builder = new AlertDialog.Builder(this);
        builder.setTitle(word.getWord());

        // Set up a common view group
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Set up input fields
        final EditText wordInput = new EditText(this); // Set up the input
        wordInput.setInputType(InputType.TYPE_CLASS_TEXT); // Specify the type of input expected
        wordInput.setTextSize(20);
        wordInput.setText(word.getWord());
        layout.addView(wordInput);

        // Create list of meanings
        ListView listView = new ListView(this);
        listView.setPadding(0,30,0,0);
        editListAdapter = new EditWordListAdapter(this, meanings);
        listView.setAdapter(editListAdapter);
        layout.addView(listView);

        // Add button to create new meanings
        Button addNewMeaningBtn = new Button(this);
        addNewMeaningBtn.setText("+");
        addNewMeaningBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(addNewMeaningBtn);

        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String wordWord = wordInput.getText().toString().trim();
                ArrayList<String> meanings = editListAdapter.getItems(); // Retrieve meanings

                Word updatedWord = new Word (wordWord, meanings);
                db.updateWord(word.getId(),updatedWord, table); // Create new vocab
                // Update adapter
                adapter.setWord(position, updatedWord);
                editListAdapter = null;
                builder = null;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                editListAdapter = null;
                builder = null;
            }
        });
        addNewMeaningBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editListAdapter.insert("", editListAdapter.getCount());
            }
        });

        builder.show();

    }


    /**
     * Show new vocab prompt
     */
    private void onCreateNewWord (){
        builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.new_word_hint);

        // Set up a common view group
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Set up input fields
        final EditText wordInput = new EditText(this); // Set up the input
        wordInput.setInputType(InputType.TYPE_CLASS_TEXT); // Specify the type of input expected
        wordInput.setHint("Word");
        layout.addView(wordInput);

        final EditText meaningsInput = new EditText(this); // Set up the input
        meaningsInput.setInputType(InputType.TYPE_CLASS_TEXT); // Specify the type of input expected
        meaningsInput.setHint("Meanings");
        layout.addView(meaningsInput);

        builder.setView(layout);
        // Set up the buttons
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newWord = wordInput.getText().toString().trim();
                String meanings = meaningsInput.getText().toString();
                if(!newWord.matches("")) {
                    db.addWord(new Word(newWord, meanings), table); // Create new vocab
                    adapter.setData(db.getWords(table)); // Update list of vocabs
                }
                builder = null;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                builder = null;
            }
        });
        builder.show();

        // Focus on input field
        if(wordInput.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(wordInput, InputMethodManager.SHOW_IMPLICIT);
        }
    }



    @Override
    /**
     *  Assign our custom menu to the action bar.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.back_menu, menu);
        return true;
    }

    /**
     *  Handle action bar button clicks
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_back:
                onBackPressed();
                return true;
            default:
                // If we got here, the user's action was not recognized. Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
