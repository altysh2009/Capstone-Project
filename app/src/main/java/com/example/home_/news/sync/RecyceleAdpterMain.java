package com.example.home_.news.sync;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.home_.news.MainActivity;
import com.example.home_.news.MyObject;
import com.example.home_.news.R;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.List;

public class RecyceleAdpterMain extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int NewsDataViewType = 0;
    private final int AddsViewType = 1;
    private ReciveClick mReciveClick;
    private List<Object> c;
    private Context context;

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
    public int getItemViewType(int position) {
        return (position % MainActivity.ITEMS_PER_AD == 0 && position != 0) ? AddsViewType
                : NewsDataViewType;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {

            case NewsDataViewType:
                Log.d("new data", "onBindViewHolder: ");
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
            Glide.with(holder.context)
                    .load(image)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.p.setVisibility(View.GONE);
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
                    .into(holder.imageView);
        }
                break;
            default:
                Log.d("ads data", "onBindViewHolder: ");
                NativeExpressAdViewHolder nativeExpressHolder =
                        (NativeExpressAdViewHolder) h;
                NativeExpressAdView adView =
                        (NativeExpressAdView) c.get(position);
                ViewGroup adCardView = (ViewGroup) nativeExpressHolder.itemView;
                // The NativeExpressAdViewHolder recycled by the RecyclerView may be a different
                // instance than the one used previously for this position. Clear the
                // NativeExpressAdViewHolder of any subviews in case it has a different
                // AdView associated with it, and make sure the AdView for this position doesn't
                // already have a parent of a different recycled NativeExpressAdViewHolder.
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
        if (c != null)
            return c.size();
        else
            return 0;
    }

    public void setdata(List<Object> cursor) {
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
        private TextView date;
        private Context context;
        private String url;
        private ProgressBar p;

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
            p = (ProgressBar) itemView.findViewById(R.id.progressBar2);
            date = (TextView) itemView.findViewById(R.id.item_date);

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

    public class NativeExpressAdViewHolder extends RecyclerView.ViewHolder {

        NativeExpressAdViewHolder(View view) {
            super(view);
        }
    }
}
