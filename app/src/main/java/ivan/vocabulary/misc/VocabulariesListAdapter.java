/**
 * @author: Ivan Mykolenko
 * @date: 24.04.2019
 */
package ivan.vocabulary.misc;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import ivan.vocabulary.DB.Vocabulary;
import ivan.vocabulary.R;

/**
 * Custom RecyclerView adapter class to display a list of vocabularies in the main activity.
 */
public class VocabulariesListAdapter extends RecyclerView.Adapter<VocabulariesListAdapter.VocabularyViewHolder>  {
    private List<Vocabulary> vocabularies;
    private View.OnClickListener onItemClickListener;


    /**
     * ViewHolder to manage item XML view of the list.
     * We pass the instance of ViewHolder via itemView to the listener using setTag().
     */
    public class VocabularyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        public VocabularyViewHolder(View view) {
            super(view);
            view.setTag(this);
            view.setOnClickListener(onItemClickListener);
            name =  view.findViewById(R.id.vocab_name);
        }
    }

    /**
     * Constructor
     * @param vocabularies (List<Vocabulary>) - a list of Vocabulary objects to be displayed
     * @param clickListener (View.OnClickListener) - callback on list item click
     */
    public VocabulariesListAdapter (List<Vocabulary> vocabularies, View.OnClickListener clickListener){
        this.vocabularies = vocabularies;
        this.onItemClickListener = clickListener;
    }


    /**
     * Set different click listener
     * @param clickListener (View.OnClickListener) - callback on list item click
     */
    public void setItemClickListener(View.OnClickListener clickListener) {
        onItemClickListener = clickListener;
    }


    @Override
    public VocabularyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vocabulary_item, parent, false);
        return new VocabularyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VocabularyViewHolder holder, int position) {
        Vocabulary vocab = vocabularies.get(position);
        holder.name.setText(vocab.getName());
        holder.name.setTextSize(Helper.getCurrentFontSize()); //Set font size
    }

    @Override
    public int getItemCount() {
        return vocabularies.size();
    }

    public List<Vocabulary> getVocabularies() {
        return vocabularies;
    }

    public void setData (List<Vocabulary> vocabularies){
        this.vocabularies = vocabularies;
        this.notifyDataSetChanged();
    }

    public Vocabulary getVocabulary(int position) {
        return vocabularies.get(position);
    }

    public String getVocabTableName(int position){
        return vocabularies.get(position).getTable();
    }

    public void setVocabularyName (int position, String newName){
        vocabularies.get(position).setName(newName);
        notifyItemChanged(position);
    }

    public void removeVocabulary(int position){
         vocabularies.remove(position);
    }

    public void restoreItem(Vocabulary vocab, int position) {
        vocabularies.add(position, vocab);
        notifyItemInserted(position);
    }
}
