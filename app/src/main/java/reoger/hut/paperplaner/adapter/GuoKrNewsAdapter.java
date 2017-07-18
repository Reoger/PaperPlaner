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
import reoger.hut.paperplaner.bean.GuokrHandpickNews;
import reoger.hut.paperplaner.interfaces.OnRecyclerViewOnClickListener;

/**
 * Created by 24540 on 2017/5/7.
 */

public class GuoKrNewsAdapter  extends RecyclerView.Adapter<GuoKrNewsAdapter.GuokrPostViewHolder>{

    private final Context context;
    private final LayoutInflater inflater;
    private List<GuokrHandpickNews.result> list;

    private OnRecyclerViewOnClickListener mListener;


    public GuoKrNewsAdapter(Context context,List<GuokrHandpickNews.result> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public GuokrPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.home_list_item_layout,parent,false);

        return new GuokrPostViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(GuokrPostViewHolder holder, int position) {
        GuokrHandpickNews.result item = list.get(position);

        Glide.with(context)
                .load(item.getHeadline_img_tb())
                .asBitmap()
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.drawable.placeholder)
                .centerCrop()
                .into(holder.ivHeadlineImg);

        holder.tvTitle.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItemClickListener(OnRecyclerViewOnClickListener listener){
        this.mListener = listener;
    }


    public class GuokrPostViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {

        ImageView ivHeadlineImg;
        TextView tvTitle;

        OnRecyclerViewOnClickListener listener;

        public GuokrPostViewHolder(View itemView, OnRecyclerViewOnClickListener listener) {
            super(itemView);

            ivHeadlineImg = (ImageView) itemView.findViewById(R.id.imageViewCover);
            tvTitle = (TextView) itemView.findViewById(R.id.textViewTitle);

            this.listener = listener;

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.OnItemClick(v,getLayoutPosition());
            }
        }

    }
}
