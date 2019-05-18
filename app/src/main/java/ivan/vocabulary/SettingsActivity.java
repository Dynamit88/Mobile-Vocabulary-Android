/**
 * @author: Ivan Mykolenko
 * @date: 24.04.2019
 */
package ivan.vocabulary;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import ivan.vocabulary.misc.Helper;

/**
 * This class manages the settings activity of the application.
 */
public class SettingsActivity extends AppCompatActivity {
    private NumberPicker numbrPicker;
    private SharedPreferences prefs = null;
    private Button restore;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = this;
        prefs = getSharedPreferences("ivan.vocabulary", MODE_PRIVATE);

        // Number picker
        numbrPicker = findViewById(R.id.number_picker);
        numbrPicker.setMinValue(1);
        numbrPicker.setMaxValue(5);
        numbrPicker.setValue(prefs.getInt("fontSize",3));
        numbrPicker.setWrapSelectorWheel(true);
        numbrPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                prefs.edit().putInt("fontSize", newVal).commit();
                Helper.setCurrentFontSize(newVal);
                Helper.msg(context,"Saved");
            }
        });
        //Restore button
        restore = findViewById(R.id.restoreDefaultsBtn);
        restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.edit().putInt("fontSize", 3).commit();
                numbrPicker.setValue(3);
                Helper.setCurrentFontSize(3);
                Helper.msg(context,"Restored");
            }
        });

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
