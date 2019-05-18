/**
 * @author: Ivan Mykolenko
 * @date: 24.04.2019
 */
package ivan.vocabulary.misc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import ivan.vocabulary.R;

import static android.support.v7.widget.helper.ItemTouchHelper.Callback;

/**
 * This class controls what happens when a user swipes left or right on a list item.
 */
 public class SwipeController extends Callback {
    private Context context;
    private SwipeControllerActions swipeActions;
    private Paint clearPaint;
    private ColorDrawable background;
    private int colorRed, colorBlue;
    private Drawable binIcon, editIcon;
    private int binIconWidth, binIconHeight, editIconWidth, editIconHeight;


    /**
     * Constructor
     * @param context (Context) - context
     * @param swipeActions (SwipeControllerActions) - object that implements swipe right and left callbacks
     */
    public SwipeController(Context context, SwipeControllerActions swipeActions) {
        this.context = context;
        this.swipeActions = swipeActions;
        background = new ColorDrawable();
        colorRed = Color.parseColor("#b80f0a");
        colorBlue = Color.parseColor("#0089FF");
        clearPaint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        // Icons
        binIcon = ContextCompat.getDrawable(context, R.drawable.bin);
        binIconWidth = binIcon.getIntrinsicWidth();
        binIconHeight = binIcon.getIntrinsicHeight();

        editIcon = ContextCompat.getDrawable(context, R.drawable.edit);
        editIconWidth = binIcon.getIntrinsicWidth();
        editIconHeight = binIcon.getIntrinsicHeight();
    }




     /**
      * Called when a ViewHolder is swiped by the user.
      * @param viewHolder (RecyclerView.ViewHolder) - XML view holder
      * @param direction (int) - direction of swipe motion
      */
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if(direction == 4) { // Swipe left
            swipeActions.onSwipeLeft(viewHolder.getAdapterPosition());
        }
        else if(direction == 8){ // Swipe right
            swipeActions.onSwipeRight(viewHolder.getAdapterPosition());
        }
    }

     /**
      * Called by ItemTouchHelper on RecyclerView's onDraw callback. Visual effects to swipe motion.
      * @param c (Canvas) - canvas
      * @param recyclerView (RecyclerView)
      * @param viewHolder (RecyclerView.ViewHolder)
      * @param dX (float) - current position of the view
      * @param dY (float) - current position of the view
      * @param actionState (int) - state of the view
      * @param isCurrentlyActive (boolean) - is active?
      */
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView; // Container
        int itemHeight = itemView.getHeight();

        // Check if swipe is cancelled
        boolean isCancelled = dX == 0 && !isCurrentlyActive;
        if (isCancelled) {
            clearCanvas(c, (float) itemView.getRight(), (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            return;
        }

        if(dX < 0){ //Swipe left
            // Draw red background
            background.setColor(colorRed);
            background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            background.draw(c);

            // Calculate icon position
            int deleteIconTop = itemView.getTop() + (itemHeight - binIconHeight) / 2;
            int deleteIconMargin = (itemHeight - binIconHeight) / 2;
            int deleteIconLeft = itemView.getRight() - deleteIconMargin - binIconWidth;
            int deleteIconRight = itemView.getRight() - deleteIconMargin;
            int deleteIconBottom = deleteIconTop + binIconHeight;

            // Draw icon
            binIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
            binIcon.draw(c);

        }
        else if(dX > 0){ //Swipe right
            // Draw red background
            background.setColor(colorBlue);
            background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getRight() + (int) dX, itemView.getBottom());
            background.draw(c);

            // Calculate icon size
            int editIconTop = itemView.getTop() + (itemHeight - editIconHeight) / 2;
            int editIconMargin = (itemHeight - editIconHeight) / 2;
            int editIconLeft = itemView.getLeft() + editIconMargin ;
            int editIconRight = itemView.getLeft() + editIconMargin + editIconWidth;
            int editIconBottom = editIconTop + editIconHeight;

            // Draw icon
            editIcon.setBounds(editIconLeft, editIconTop, editIconRight, editIconBottom);
            editIcon.draw(c);
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    /**
    * Returns the fraction that the user should move the View to be considered as swiped.
     * We’ve set the Swipe threshold to 0.7. That means if the row is swiped less than 70%, the onSwipe method won’t be triggered.
    */
    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.7f;
    }


    /**
     * Used to clear canvas
     * @param c (Canvas) - canvas
     * @param left (float)
     * @param top (float)
     * @param right (float)
     * @param bottom (float)
     */
     private void clearCanvas(Canvas c, float left, float top, float right, float bottom) {
         c.drawRect(left, top, right, bottom, clearPaint);
     }























     private void msg (String msg){
         Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
     }


     @Override
     /**
      * Get swipe direction: left? right?
      */
     public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
         return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
     }

     /**
      * Called when ItemTouchHelper wants to move the dragged item from its old position to the new position.
      * @param recyclerView
      * @param viewHolder
      * @param viewHolder1
      * @return
      */
     @Override
     public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
         return false;
     }
 }