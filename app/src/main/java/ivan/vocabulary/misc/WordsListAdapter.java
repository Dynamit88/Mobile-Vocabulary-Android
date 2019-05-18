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
import ivan.vocabulary.DB.Word;
import ivan.vocabulary.R;

/**
 * Custom RecyclerView adapter class to display a list of words in a vocabulary activity.
 */
public class WordsListAdapter extends RecyclerView.Adapter<WordsListAdapter.WordsViewHolder>  {
    private List<Word> words;
    private View.OnClickListener onItemClickListener;


    /**
     * ViewHolder to manage item XML view of the list.
     * We  pass the instance of ViewHolder via itemView to the listener using setTag().
     */
    public class WordsViewHolder extends RecyclerView.ViewHolder {
        private TextView word, meaning;
        public WordsViewHolder(View view) {
            super(view);
            view.setTag(this);
            view.setOnClickListener(onItemClickListener);
            word = view.findViewById(R.id.word);
            meaning = view.findViewById(R.id.meaning);
        }
    }

    /**
     * Constructor
     * @param words (List<Word>) - a list of Word objects to be displayed
     * @param clickListener (View.OnClickListener) - callback on list item click
     */
    public WordsListAdapter(List<Word> words, View.OnClickListener clickListener){
        this.words = words;
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
    public WordsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item, parent, false);
        return new WordsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WordsViewHolder holder, int position) {
        Word word = words.get(position);
        holder.word.setTextSize(Helper.getCurrentFontSize());
        holder.word.setText(word.getWord());
        String[] meanings = word.getArrayOfMeanings();

        if(meanings != null && meanings[0] != null){
            holder.meaning.setTextSize(Helper.getCurrentFontSize()-2);
            holder.meaning.setText(meanings[0]);
        }

    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public List<Word> getVocabularies() {
        return words;
    }

    public void setData (List<Word> vocabularies){
        this.words = vocabularies;
        this.notifyDataSetChanged();
    }

    public Word getWord(int position) {
        return words.get(position);
    }

    public void setWord (int position, Word word){
        words.set(position,word);
        notifyItemChanged(position);
    }

    public void removeWord(int position){
        words.remove(position);
    }

    public void restoreItem(Word word, int position) {
        words.add(position, word);
        notifyItemInserted(position);
    }
}
