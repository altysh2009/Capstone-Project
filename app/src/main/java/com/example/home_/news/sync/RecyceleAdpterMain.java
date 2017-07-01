package com.example.home_.news.sync;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.home_.news.R;
import com.example.home_.news.data.NewsContract;

public class RecyceleAdpterMain extends RecyclerView.Adapter<RecyceleAdpterMain.RecycelHolder> {
    ReciveClick mReciveClick;
    private Cursor c;
    private Context context;

    public RecyceleAdpterMain(ReciveClick r) {
        mReciveClick = r;
    }
    @Override
    public RecycelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_main, parent, false);
        return new RecycelHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycelHolder holder, int position) {
        if (c.moveToNext()) {
            String auther_Name = c.getString(c.getColumnIndex(NewsContract.NewsArticles.Author));

            String description = c.getString(c.getColumnIndex(NewsContract.NewsArticles.Descrption));
            String sourceName = c.getString(c.getColumnIndex(NewsContract.NewsArticles.Source_Name));
            String title = c.getString(c.getColumnIndex(NewsContract.NewsArticles.Title));
            String image = c.getString(c.getColumnIndex(NewsContract.NewsArticles.Image_Url));
            if (auther_Name == null || auther_Name.equals("") || auther_Name.equals("null"))
                auther_Name = "Anonymous";
            if (auther_Name.contains("http:"))
                holder.autherName.setTextColor(Color.BLUE);
            else holder.autherName.setTextColor(Color.BLACK);

            Log.d(auther_Name, "onBindViewHolder: ");


            holder.autherName.setText(auther_Name);
            holder.description.setText(description);
            holder.sourceName.setText(sourceName);
            holder.title.setText(title);
            holder.url = c.getString(c.getColumnIndex(NewsContract.NewsArticles.Url));
            Glide.with(holder.context)
                    .load(c.getString(c.getColumnIndex(NewsContract.NewsArticles.Image_Url)))

                    .into(holder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        if (c != null)
        return c.getCount();
        else
            return 0;
    }

    public void setdata(Cursor cursor) {
        c = cursor;
        notifyDataSetChanged();
    }

    public interface ReciveClick {
        public void theClickedItem(String thumb);

        public void theClickedItem(String thumb, int id);

    }

    class RecycelHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView title;
        private TextView description;
        private TextView sourceName;
        private TextView autherName;
        private Context context;
        private String url;

        public RecycelHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            imageView = (ImageView) itemView.findViewById(R.id.item_image);
            title = (TextView) itemView.findViewById(R.id.item_article_title);
            description = (TextView) itemView.findViewById(R.id.item_article_descrption);
            sourceName = (TextView) itemView.findViewById(R.id.item_source);
            sourceName.setTextColor(Color.BLUE);
            autherName = (TextView) itemView.findViewById(R.id.item_auther);
            autherName.setOnClickListener(this);
            title.setOnClickListener(this);
            description.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.item_article_title:
                    mReciveClick.theClickedItem(url);
                    break;
                case R.id.item_image:
                    break;
                case R.id.item_article_descrption:
                    description.setMaxLines(5);
                    break;
                case R.id.item_auther:
                    mReciveClick.theClickedItem(autherName.getText().toString(), R.id.item_auther);
                    break;


            }


        }
    }
}
