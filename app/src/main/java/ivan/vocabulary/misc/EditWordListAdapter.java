/**
 * @author: Ivan Mykolenko
 * @date: 24.04.2019
 */
package ivan.vocabulary.misc;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import ivan.vocabulary.R;

/**
 * Custom adapter class to display a list of meaning in the word edit dialog.
 */
public class EditWordListAdapter  extends ArrayAdapter<String> {
    private final LayoutInflater mInflater;
    private ArrayList<String> meanings;
    private Context context;


    public EditWordListAdapter(Context context, ArrayList<String> meanings) {
        super(context, R.layout.edit_word_item, meanings);
        this.meanings = meanings;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }


 @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String meaning = getItem(position).trim(); // Get the data item for this position
        View view = mInflater.inflate(R.layout.edit_word_item, parent, false);
        // Lookup view for data population
        EditText meaningInput = view.findViewById(R.id.word);
        meaningInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String newText = s.toString().trim();
                meanings.set(position, newText);
            }
        });

        // Set up remove meaning button
        Button removeBtn = view.findViewById(R.id.btnRemove);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meanings.remove(position);
                notifyDataSetChanged();
            }
        });
        meaningInput.setText(meaning);
        return view;
    }

    public ArrayList<String> getItems(){
        return meanings;
    }

}