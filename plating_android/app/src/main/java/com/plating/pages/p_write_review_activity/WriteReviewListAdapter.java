package com.plating.pages.p_write_review_activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.plating.R;
import com.plating.application.Constant;
import com.plating.network.RequestURL;
import com.plating.network.VolleySingleton;
import com.plating.object.WriteReviewRow;

import java.util.ArrayList;

/**
 * Created by cheehoonha on 10/6/14.
 */
public class WriteReviewListAdapter extends RecyclerView.Adapter<WriteReviewListAdapter.WriteReviewViewHolder> {

    public String LOG_TAG = Constant.APP_NAME + "WriteReviewListAdapter";
    private Context mContext;
    private LayoutInflater inflater;
    public ArrayList<WriteReviewRow> data = new ArrayList<>();

    public ImageLoader imageLoader;

    public WriteReviewListAdapter(Context context, ArrayList<WriteReviewRow> data) {
        Log.d(LOG_TAG, "Start: WriteReviewListAdapter");

        mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data;

        imageLoader = VolleySingleton.getInstance().getmImageLoader();
    }

    @Override
    public WriteReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.d(LOG_TAG, "onCreateViewHolder: Start");

        View view = inflater.inflate(R.layout.p_write_review_list_row, viewGroup, false);
        WriteReviewViewHolder holder = new WriteReviewViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final WriteReviewViewHolder viewHolder, final int position) {
        WriteReviewRow writeReviewRow = data.get(position);
        Log.d(LOG_TAG, writeReviewRow.toString());

        viewHolder.menuName.setText(writeReviewRow.menuName.replace(".", "\n"));

        // Load image with volley for food image
        VolleySingleton.getInstance().loadImageToImageView(viewHolder.menuImage, RequestURL.DAILY_MENU_IMAGE_URL + writeReviewRow.menuImageUrl);

/*
            LayerDrawable stars = (LayerDrawable) holder.RB_star.getProgressDrawable();
            stars.getDrawable(0).setColorFilter(Color.parseColor("#B6B6B6"), PorterDuff.Mode.SRC_ATOP); // 빈별
            stars.getDrawable(1).setColorFilter(Color.parseColor("#E26F23"), PorterDuff.Mode.SRC_ATOP); // ..
            stars.getDrawable(2).setColorFilter(Color.parseColor("#E26F23"), PorterDuff.Mode.SRC_ATOP); // 찬 별
*/

        viewHolder.ratingBar.setRating(writeReviewRow.rating);
        viewHolder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                data.get(position).rating = rating;
            }
        });

        // If comment already exists, display the comment
        if(writeReviewRow.comment != null && !writeReviewRow.comment.equals("null")) {
            viewHolder.comment.setText(writeReviewRow.comment);
        } else {
            viewHolder.comment.setText("");
        }


        viewHolder.comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                data.get(position).comment = s.toString();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public ArrayList<WriteReviewRow> getAllReviewsWrittenSoFar() {
        return data;
    }

    public void updateData(ArrayList<WriteReviewRow> data) {
        this.data = data;
    }

    class WriteReviewViewHolder extends RecyclerView.ViewHolder {
        TextView menuName;
        ImageView menuImage;
        RatingBar ratingBar;
        EditText comment;

        public WriteReviewViewHolder(View itemView) {
            super(itemView);
            menuName = (TextView) itemView.findViewById(R.id.menu_name);
            menuImage = (ImageView) itemView.findViewById(R.id.menu_image);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating_bar);
            comment = (EditText) itemView.findViewById(R.id.comment);
        }
    }
}
