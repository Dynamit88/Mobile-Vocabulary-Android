/**
 * @author: Ivan Mykolenko
 * @date: 24.04.2019
 */
package ivan.vocabulary;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import java.util.ArrayList;
import ivan.vocabulary.DB.DB;
import ivan.vocabulary.DB.Vocabulary;
import ivan.vocabulary.misc.Helper;
import ivan.vocabulary.misc.SwipeController;
import ivan.vocabulary.misc.SwipeControllerActions;
import ivan.vocabulary.misc.VocabulariesListAdapter;



/**
 * The class is responsible for managing main activity and the main menu of the application.
 */
public class MainActivity extends AppCompatActivity {
    private RecyclerView vocabsList;
    private VocabulariesListAdapter adapter;
    private View.OnClickListener onItemClickListener;
    private ConstraintLayout mainLayout = null;
    private SharedPreferences prefs = null;
    private DB db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("My Vocabularies");

        // Init member variables
        vocabsList =  findViewById(R.id.vocabsList);
        mainLayout = findViewById(R.id.constraintLayout);
        prefs = getSharedPreferences("ivan.vocabulary", MODE_PRIVATE);
        db = DB.getInstance(this);

        //Check if the application is launched the very first time
        if (prefs.getBoolean("firstrun", true)) {
            db.initiateTheDB_system(); // Setup the database
            prefs.edit().putBoolean("firstrun", false).commit();
            prefs.edit().putInt("fontSize", 3).commit(); // Set default font size
        }
        Helper.setCurrentFontSize(Helper.getFontSize(prefs.getInt("fontSize",3))); //Set font size preferences
        setVocabulariesListAdapter();
        setupRecyclerView();
    }


    @Override
    public void onResume(){
        super.onResume();
        adapter.notifyDataSetChanged();
    }


    /**
     * Method to set up an instance of VocabulariesListAdapter to display a list of vocabularies in the main menu.
     */
    private void setVocabulariesListAdapter(){
        ArrayList<Vocabulary> vocabs = db.getVocabNamesAndTablesObjects(); //Get data from the DB
        // Create click listener
        onItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This viewHolder will have all required values.
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                Vocabulary vocab = adapter.getVocabulary(viewHolder.getAdapterPosition());
                openVocabActivity (vocab);
            }
        };

        adapter = new VocabulariesListAdapter(vocabs, onItemClickListener);
        vocabsList.setAdapter(adapter);//Set adapter
    }

    /**
     * Set up RacyclerView layout so to support swipe motions and display a list of vocabularies.
     */
    private void setupRecyclerView() {
        // Providing an implementation for the SwipeControllerActions class and creating and instance of it.
        // This instance serves as a callback for the swipe motions handled by the RecyclerView.
        SwipeControllerActions swipeActions = new SwipeControllerActions(){
            @Override
            public void onSwipeLeft(final int position) { //Delete item
                final Vocabulary vocab = adapter.getVocabulary(position);
                adapter.removeVocabulary(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, adapter.getItemCount());

                // Create notification
                final Snackbar snackbar = Snackbar.make(mainLayout, vocab.getName() + " was removed.", Snackbar.LENGTH_LONG).setActionTextColor(Color.YELLOW);

                // Create 'Undo' button
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapter.restoreItem(vocab, position);
                        vocabsList.scrollToPosition(position);
                        vocab.undo(); // Set undo flag
                    }
                });
                // When notification disappears and the 'Undo' wasn't clicked
                snackbar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if(!vocab.getUndo()) { // if undo wasn't pressed and flag wasn't set
                            db.deleteVocabulary(vocab.getTable()); //Remove from DB
                        }
                    }
                });
                snackbar.show();
            }

            @Override
            public void onSwipeRight(int position) { // Edit item
                onEditVocab(adapter.getVocabulary(position), position);
                adapter.notifyItemChanged(position); //reset swipe
            }

        };
        // Set up the SwipeController
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(new SwipeController(this, swipeActions));
        itemTouchhelper.attachToRecyclerView(vocabsList);
    }



    /**
     * This method manages editing existing vocabularies in the system. Swipe right callback function.
     * @param vocab (Vocabulary) - object to edit
     * @param position (int) - the positing of the object in adapter.
     */
    private void onEditVocab(final Vocabulary vocab, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(vocab.getName());
        final EditText input = new EditText(this); // Set up the input
        input.setInputType(InputType.TYPE_CLASS_TEXT); // Specify the type of input expected
        input.setText(vocab.getName());
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newVocabName = input.getText().toString().trim();
                if(!newVocabName.matches("") && newVocabName != vocab.getName()) {
                    db.changeVocabName(vocab.getId(), newVocabName); // Create new vocab
                    adapter.setVocabularyName(position, newVocabName);
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    /**
     * Shows new vocabulary dialog window.
     *
     */
    private void onCreateNewVocabulary (){
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("New Vocabulary");
        final EditText input = new EditText(this); // Set up the input
        input.setInputType(InputType.TYPE_CLASS_TEXT); // Specify the type of input expected
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newVocabName = input.getText().toString().trim();
                if(!newVocabName.matches("")) {
                    db.createVocabulary(newVocabName); // Create new vocab
                    adapter.setData(db.getVocabNamesAndTablesObjects()); // Update list of vocabs
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    /**
     *  Open vocabulary activity
     * @param vocab (Vocabulary) - a vocab to be opened.
     */
    private void openVocabActivity(Vocabulary vocab){
        Intent intent = new Intent(this, VocabularyActivity.class);
        intent.putExtra("VOCAB_NAME", vocab.getName());
        intent.putExtra("VOCAB_TABLE", vocab.getTable()); //Table with words
        startActivity(intent);
    }




    @Override
    /**
     *  Assign our custom menu to the action bar.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    /**
     *  Handle action bar button clicks
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_addVocab:
                onCreateNewVocabulary();
                return true;
            case R.id.btn_export:
                startActivity(new Intent(this, ExportActivity.class));
                return true;
            case R.id.btn_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                // If we got here, the user's action was not recognized. Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }












}
