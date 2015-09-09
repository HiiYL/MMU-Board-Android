package com.underoneroof.mmuboard;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.underoneroof.mmuboard.Model.Subject;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Hii on 8/31/15.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Subject> mDataset;
    public FragmentActivity fragmentActivity;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mTitleTextView;
        public TextView mDescriptionTextview;
        public IMyViewHolderClicks mListener;
        public View mRootView;
        public CardView mCardView;
        public ViewHolder(View itemView, IMyViewHolderClicks listener) {
            super(itemView);
            mListener = listener;
            mTitleTextView = (TextView) itemView.findViewById(R.id.info_text);
            mDescriptionTextview = (TextView) itemView.findViewById(R.id.description_text);
            mCardView = (CardView) itemView.findViewById(R.id.card_view);
            mRootView = itemView;
            mCardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onPotato(v, getLayoutPosition());
        }
        public static interface IMyViewHolderClicks {
            public void onPotato(View caller, int position);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<Subject> myDataset,FragmentActivity fa) {
        mDataset = myDataset;
        fragmentActivity=fa;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_subject, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v, new MyAdapter.ViewHolder.IMyViewHolderClicks() {
            @Override
            public void onPotato(View caller, int position) {
                Log.d("TEST","Poh-tah-tos" + position);
                Toast.makeText(parent.getContext(), "Hello There" + position, Toast.LENGTH_SHORT).show();
            }
        });
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTitleTextView.setText(mDataset.get(position).getTitle());
        holder.mDescriptionTextview.setText(mDataset.get(position).getDescription());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}