package com.example.a2048;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/***
 * The adapter class for the RecyclerView
 */
class PegAdapter extends RecyclerView.Adapter<PegAdapter.PegViewHolder>  {

    //Member variables
    private GradientDrawable mGradientDrawable;
    private ArrayList<PegBoard> boards;
    private Context mContext;

    PegAdapter(Context context, ArrayList<PegBoard> PegData) {
        this.mContext = context;
        this.boards = PegData;

        //Prepare gray placeholder
        mGradientDrawable = new GradientDrawable();
        mGradientDrawable.setColor(Color.GRAY);

        //Make the placeholder same size as the images
        Drawable drawable = ContextCompat.getDrawable
                (mContext,R.drawable.peg_type1);
        if(drawable != null) {
            mGradientDrawable.setSize(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }
    }

    @Override
    public PegViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PegViewHolder(mContext, LayoutInflater.from(mContext).
                inflate(R.layout.list_item, parent, false), mGradientDrawable);
    }

    @Override
    public void onBindViewHolder(PegViewHolder holder, int position) {

        //Get the current sport
        PegBoard currentBoard = boards.get(position);

        //Bind the data to the views
        holder.bindTo(currentBoard);

    }

    @Override
    public int getItemCount() {
        return boards.size();
    }

    static class PegViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        //Member Variables for the holder data
        private TextView mTitleText;
        private TextView mInfoText;
        private ImageView mBoardImage;
        private Context mContext;
        private GradientDrawable mGradientDrawable;
        private PegBoard mBoard;

        PegViewHolder(Context context, View itemView, GradientDrawable gradientDrawable) {
            super(itemView);

            //Initialize the views
            mTitleText = (TextView)itemView.findViewById(R.id.title);
            mInfoText = (TextView)itemView.findViewById(R.id.subTitle);
            mBoardImage = (ImageView)itemView.findViewById(R.id.boardImage);

            mContext = context;
            mGradientDrawable = gradientDrawable;

            //Set the OnClickListener to the whole view
            itemView.setOnClickListener(this);
        }

        void bindTo(PegBoard currentBoard){
            //Populate the textviews with data
            mTitleText.setText(currentBoard.getTitle());
            mInfoText.setText(currentBoard.getInfo());

            mBoard = currentBoard;

            //Load the images into the ImageView using the Glide library
            Glide.with(mContext).load(currentBoard.
                    getImageResource()).placeholder(mGradientDrawable).into(mBoardImage);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.putExtra("Board_type",mBoard.getTitle());
            intent.setClass(mContext, SplashPeg.class);
            mContext.startActivity(intent);
        }
    }
}