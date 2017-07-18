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

import java.util.ArrayList;
import java.util.List;

import reoger.hut.paperplaner.R;
import reoger.hut.paperplaner.bean.ZhiHuLatest;
import reoger.hut.paperplaner.interfaces.OnRecyclerViewOnClickListener;

/**
 * Created by 24540 on 2017/5/6.
 */

public class ZhihuNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private final LayoutInflater mInflater;
    private List<ZhiHuLatest.StoriesBean> mlist = new ArrayList<>();
    private OnRecyclerViewOnClickListener mListener;

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_FOOTER = 1;

    public ZhihuNewsAdapter(Context context, List<ZhiHuLatest.StoriesBean> mlist) {
        this.mContext = context ;
        mInflater = LayoutInflater.from(context);
        this.mlist = mlist;
    }

    public void addData(List<ZhiHuLatest.StoriesBean> mlist){
        this.mlist.addAll(mlist);
       notifyDataSetChanged();
    }

    public void setItemClickListenter(OnRecyclerViewOnClickListener listenter){
        this.mListener = listenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_NORMAL:
                return new NormalViewHolder(mInflater.inflate(R.layout.home_list_item_layout,parent,false),mListener);
            case TYPE_FOOTER:
                return new FooterViewHolder(mInflater.inflate(R.layout.list_footer,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof NormalViewHolder ){

            ZhiHuLatest.StoriesBean storiesBean = mlist.get(position);

            if(storiesBean.getImages().get(0) == null){
                ((NormalViewHolder)holder).itemImg.setImageResource(R.drawable.placeholder);
            }else {
                Glide.with(mContext)
                        .load(storiesBean.getImages().get(0))
                        .asBitmap()
                        .placeholder(R.drawable.placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .error(R.drawable.placeholder)
                        .centerCrop()
                        .into(((NormalViewHolder)holder).itemImg);
            }

            ((NormalViewHolder)holder).tvLatestNewsTitle.setText(storiesBean.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return mlist.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == mlist.size()){
            return ZhihuNewsAdapter.TYPE_FOOTER;
        }else{
            return ZhihuNewsAdapter.TYPE_NORMAL;
        }
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView itemImg;
        private TextView tvLatestNewsTitle;
        private OnRecyclerViewOnClickListener listener;

        public NormalViewHolder(View itemView, OnRecyclerViewOnClickListener listener) {
            super(itemView);
            itemImg = (ImageView) itemView.findViewById(R.id.imageViewCover);
            tvLatestNewsTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
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

    public class FooterViewHolder extends RecyclerView.ViewHolder{

        public FooterViewHolder(View itemView) {
            super(itemView);
        }

    }

}
