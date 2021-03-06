package com.example.home_.news.sync;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.home_.news.MyAdd;
import com.example.home_.news.MyObject;
import com.example.home_.news.R;
import com.example.home_.news.data.NewsContract;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class RecyceleAdpterMain extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final int AddsViewType = 1;
    private final int NewsDataViewType = 0;
    private ArrayList<String> addIndex;
    private ReciveClick mReciveClick;
    private List<Object> c;
    private Context context;
    private Boolean scrolling = true;

    public RecyceleAdpterMain(ReciveClick r) {
        mReciveClick = r;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        switch (viewType) {
            case NewsDataViewType:
                LayoutInflater inflater2 = LayoutInflater.from(context);
                View view = inflater2.inflate(R.layout.list_item_main, parent, false);
                return new RecycelHolder(view);
            default:
                LayoutInflater inflater = LayoutInflater.from(context);
                View nativeExpressLayoutView = inflater.inflate(R.layout.native_express_add_view, parent, false);
                return new NativeExpressAdViewHolder(nativeExpressLayoutView);

        }



    }

    @Override
    public long getItemId(int position) {
        return c.get(position).hashCode();
    }

    @Override
    public int getItemViewType(int position) {
        return (c.get(position) instanceof MyAdd) ? AddsViewType
                : NewsDataViewType;
        //       return NewsDataViewType;

    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder h, int position) {
        int viewType = getItemViewType(position);
        Log.d(TAG, "getAddsIndex: " + position);
        switch (viewType) {

            case NewsDataViewType:
                // Log.d("new data", "onBindViewHolder: ");
                if (c != null) {
                    final RecycelHolder holder = (RecycelHolder) h;
                    MyObject m = (MyObject) c.get(position);
                    String auther_Name = m.getAuther_Name();

                    String description = m.getDescription();
                    String sourceName = m.getSourceName();
                    String title = m.getTitle();
                    String image = m.getImage();
            if (auther_Name == null || auther_Name.equals("") || auther_Name.equals("null"))
                auther_Name = "Anonymous";
            if (auther_Name.contains("http:"))
                holder.autherName.setTextColor(Color.BLUE);
            else holder.autherName.setTextColor(Color.BLACK);

                    //Log.d(auther_Name, "onBindViewHolder: ");


            holder.autherName.setText(auther_Name);
            holder.description.setText(description);
            holder.sourceName.setText(sourceName);
            holder.title.setText(title);
                    holder.url = m.getUrl();
                    holder.date.setText(m.getDate());
                    holder.p.setVisibility(View.VISIBLE);
                    // if(scrolling)
            Glide.with(holder.context)
                    .load(image)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.p.setVisibility(View.GONE);
                            holder.imageView.setImageResource(R.drawable.error);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.p.setVisibility(View.GONE);
                            return false;
                        }

                    })
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .skipMemoryCache(true)
                    .error(R.drawable.error)

                    .into(holder.imageView);

                    //  else holder.p.setVisibility(View.GONE);
        }
                break;
            default:
                // Log.d("ads data", "onBindViewHolder: ");

                NativeExpressAdViewHolder nativeExpressHolder =
                        (NativeExpressAdViewHolder) h;
                MyAdd m = (MyAdd) c.get(position);
                NativeExpressAdView adView =
                        m.getAdView();
                ViewGroup adCardView = (ViewGroup) nativeExpressHolder.itemView;

                if (adCardView.getChildCount() > 0) {
                    adCardView.removeAllViews();
                }
                if (adView.getParent() != null) {
                    ((ViewGroup) adView.getParent()).removeView(adView);
                }

                // Add the Native Express ad to the native express ad view.
                adCardView.addView(adView);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (c != null) {
            // Log.d(c.size() + " ", "getItemCount: ");
            return c.size();
        }

        else
            return 0;
    }

    public void setdata(List<Object> cursor, ArrayList<String> addIndex) {

        c = cursor;
        this.addIndex = addIndex;
        notifyDataSetChanged();
    }



    public Boolean getScrolling() {
        return scrolling;
    }

    public void setScrolling(Boolean scrolling) {
        this.scrolling = scrolling;
    }

    public interface ReciveClick {
        void theClickedItem(String thumb);

        void theClickedItem(String thumb, int id);

        void share(String thumb, int type);

        void share(String thumb, int type, String title);

    }

    private class RecycelHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView title;
        private TextView description;
        private TextView sourceName;
        private TextView autherName;
        private TextView date;
        private Context context;
        private String url;
        private ProgressBar p;
        private ImageButton imageButton;

        public RecycelHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            imageView = (ImageView) itemView.findViewById(R.id.item_image);
            imageView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.item_article_title);
            description = (TextView) itemView.findViewById(R.id.item_article_descrption);
            sourceName = (TextView) itemView.findViewById(R.id.item_source);
            sourceName.setTextColor(Color.BLUE);
            sourceName.setOnClickListener(this);
            autherName = (TextView) itemView.findViewById(R.id.item_auther);
            autherName.setOnClickListener(this);
            title.setOnClickListener(this);
            description.setOnClickListener(this);
            p = (ProgressBar) itemView.findViewById(R.id.progressBar2);
            date = (TextView) itemView.findViewById(R.id.item_date);
            imageButton = (ImageButton) itemView.findViewById(R.id.booo);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(context, imageButton);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.buttom_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.share_face:
                                    //handle menu1 click
                                    mReciveClick.share(url, R.id.share_face);
                                    break;
                                case R.id.share_gmail:
                                    mReciveClick.share(url, R.id.share_gmail, title.getText().toString());

                                    //handle menu2 click
                                    break;
                                case R.id.share_twitter:
                                    mReciveClick.share(url, R.id.share_twitter);
                                    //handle menu3 click
                                    break;
                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    popup.show();

                }
            });

        }


        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.item_article_title:
                    mReciveClick.theClickedItem(url);
                    break;
                case R.id.item_image:
                    mReciveClick.theClickedItem(url);
                    break;
                case R.id.item_article_descrption:
                    description.setMaxLines(5);
                    break;
                case R.id.item_auther:
                    mReciveClick.theClickedItem(autherName.getText().toString(), R.id.item_auther);
                    break;
                case R.id.item_source:
                    Cursor c = context.getContentResolver().query(NewsContract.sources, null, NewsContract.NewsSources.News_Sources_Name + " =? ", new String[]{sourceName.getText().toString()}, null);
                    String t = null;
                    if (c != null) {
                        while (c.moveToNext())
                            t = c.getString(c.getColumnIndex(NewsContract.NewsSources.Url));
                        if (t != null && t.length() != 0 && t.contains("http"))
                            mReciveClick.theClickedItem(t);
                        c.close();
                    }


            }


        }


    }

    private class NativeExpressAdViewHolder extends RecyclerView.ViewHolder {

        NativeExpressAdViewHolder(View view) {
            super(view);
        }
    }
}
