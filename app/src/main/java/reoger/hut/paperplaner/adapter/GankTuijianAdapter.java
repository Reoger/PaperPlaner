package reoger.hut.paperplaner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import reoger.hut.paperplaner.R;
import reoger.hut.paperplaner.bean.GankXiatuijian;
import reoger.hut.paperplaner.interfaces.OnRecyclerViewOnClickListener;

/**
 * Created by 24540 on 2017/5/10.
 */

public class GankTuijianAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<GankXiatuijian.ResultsBean> list;
    private LayoutInflater inflater;


    private static final int TYPE_NORMAL = 0x00;
    private static final int TYPE_FOOTER = 0x02;
    private static final int TYPE_NO_IMG = 0x03;

    private OnRecyclerViewOnClickListener listener;

    public GankTuijianAdapter(Context context, List<GankXiatuijian.ResultsBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_NORMAL:
                return new NormalViewHolder(inflater.inflate(R.layout.home_list_item_layout, parent, false), listener);
            case TYPE_NO_IMG:
                return new NoImgViewHolder(inflater.inflate(R.layout.home_list_item_without_image, parent, false), listener);
            case TYPE_FOOTER:
                return new FooterViewHolder(inflater.inflate(R.layout.list_footer, parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if ( !(holder instanceof FooterViewHolder)) {
            GankXiatuijian.ResultsBean item = list.get(position);
            if (holder instanceof GankTuijianAdapter.NormalViewHolder) {

                Glide.with(context)
                        .load(item.getImages().get(0))
                        .asBitmap()
                        .placeholder(R.drawable.placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .error(R.drawable.internet_error)
                        .centerCrop()
                        .into(((NormalViewHolder)holder).ivHeadlineImg);
                ((NormalViewHolder)holder).tvTitle.setText(item.getDesc());

            } else if (holder instanceof NoImgViewHolder) {

                ((NoImgViewHolder)holder).tvTitle.setText(item.getDesc());

            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size()) {
            return TYPE_FOOTER;
        }
        if (list.get(position).getImages()==null) {
            return TYPE_NO_IMG;
        }
        return TYPE_NORMAL;
    }

    public void setItemClickListener(OnRecyclerViewOnClickListener listener){
        this.listener = listener;
    }


    public class NormalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView ivHeadlineImg;
        TextView tvTitle;

        OnRecyclerViewOnClickListener listener;

        public NormalViewHolder(View itemView, OnRecyclerViewOnClickListener listener) {
            super(itemView);
            ivHeadlineImg = (ImageView) itemView.findViewById(R.id.imageViewCover);
            tvTitle = (TextView) itemView.findViewById(R.id.textViewTitle);

            this.listener = listener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null){
                listener.OnItemClick(view, getLayoutPosition());
            }
        }
    }

    public class NoImgViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;

        OnRecyclerViewOnClickListener listener;

        public NoImgViewHolder(View itemView, OnRecyclerViewOnClickListener listener) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.textViewTitle);

            this.listener = listener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null){
                listener.OnItemClick(view, getLayoutPosition());
            }
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

}
