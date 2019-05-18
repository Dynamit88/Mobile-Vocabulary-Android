/**
 * @author: Ivan Mykolenko
 * @date: 24.04.2019
 */
package ivan.vocabulary;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import ivan.vocabulary.CSV.CSV;
import ivan.vocabulary.DB.DB;
import ivan.vocabulary.DB.Vocabulary;
import ivan.vocabulary.misc.ExportSpinnerAdapter;
import ivan.vocabulary.misc.Helper;

/**
 * This class manages the export activity.
 */
public class ExportActivity extends AppCompatActivity {
    private Vocabulary selectedVocab = null;
    private ExportSpinnerAdapter adapter ;
    private DB db;
    private Spinner vocabSpinner;
    private ImageButton btnExport;
    private RadioGroup radioGroup;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        context = this;
        db = DB.getInstance(this);
        vocabSpinner = findViewById(R.id.vocabSpinner);
        radioGroup = findViewById(R.id.radioGroup);
        btnExport = findViewById(R.id.btn_spinner_export);
        btnExport.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int checkedId = radioGroup.getCheckedRadioButtonId();
                boolean success = CSV.writeCSV(selectedVocab.getName(), selectedVocab);
                switch (checkedId){
                    case R.id.radioSaveLocally:
                        Helper.msg(context,success? "File has been saved.":"File wasn't saved.");
                        break;
                    case R.id.radioEmail:
                        if(success) {
                            emailFile(CSV.getLastFilePath(), selectedVocab.getName());
                        }
                        break;
                    default:
                }
            }
        });

        ArrayList<Vocabulary> vocabs = db.getVocabNamesAndTablesObjects();
        adapter = new ExportSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, vocabs);
        vocabSpinner.setAdapter(adapter);

        vocabSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedVocab = adapter.getItem(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

    }


    /**
     * Pssing generated file to the other applications installed on the device.
     * @param filePath (Sting) - path to the generated file
     * @param vocabName (String) - name of the exported file
     * @return
     */
    private void emailFile (String filePath, String vocabName){
            File file = new File(filePath);
            Uri path = Uri.fromFile(file);
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("vnd.android.cursor.dir/email"); // set the type to 'email'
            emailIntent.putExtra(Intent.EXTRA_STREAM, path); // the attachment
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, vocabName + " vocabulary"); // the mail subject
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
            file.deleteOnExit(); // Remove file afterwards
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
