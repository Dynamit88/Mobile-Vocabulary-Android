/**
 * @author: Ivan Mykolenko
 * @date: 24.04.2019
 */
package ivan.vocabulary.misc;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import ivan.vocabulary.DB.Vocabulary;

/**
 * Custom adapter class to display a list of vocabularies in the export activity.
 */
public class ExportSpinnerAdapter extends ArrayAdapter<Vocabulary> {
    private final LayoutInflater mInflater;
    private final int mResource;

    /**
     * Constructor
     * @param context (Context) - context
     * @param resource (int) - XML layout resource file
     * @param vocabs (List<Vocabulary>) - list of vocabularies to be displayed
     */
    public ExportSpinnerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Vocabulary> vocabs) {
        super(context, resource, 0, vocabs);
        mInflater = LayoutInflater.from(context);
        mResource = resource;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(mResource, parent, false);
        TextView item_name = view.findViewById(android.R.id.text1);
        item_name.setText(getItem(position).getName());
        return view;
    }
}