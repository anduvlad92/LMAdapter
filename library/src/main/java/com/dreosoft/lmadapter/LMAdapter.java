package com.dreosoft.lmadapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by andrei_vlad on 4/9/2016.
 */
public abstract class LMAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<T> data;
    private final int loadingViewID;
    private static final int LOADING_VIEW_TYPE = -18;

    public LMAdapter(List<T> data, int loadingViewID) {
        this.data = data;
        this.loadingViewID = loadingViewID;
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType  == LOADING_VIEW_TYPE) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(loadingViewID, parent, false);
            return new LoadingHolder(view);
        }else{
            return onLMCreateViewholder(parent,viewType);
        }
    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(!(holder instanceof LoadingHolder)){
            onLMBindViewHolder(holder,position);
        }
    }




    static final class LoadingHolder extends RecyclerView.ViewHolder {
        LoadingHolder(View itemView) {
            super(itemView);
        }
    }


    public final void notifyRequestFinished(List<T> newItems){
        if(data != null) {
            if(data.size()>0) {
                int position = data.size() - 1;
                data.remove(position);
                notifyItemRemoved(position);
                if (newItems != null){
                    if(newItems.size() > 0) {
                        notifyNewItemsInserted(newItems);
                    }
                }
            }
        }
    }



    public final void notifyRequestStarted(){
        if(data != null){
            data.add(null);
            notifyItemInserted(data.size()-1);
        }
    }


    private final void notifyNewItemsInserted(List<T> newItems){
        int startPosition = data.size();
        data.addAll(newItems);
        notifyItemRangeInserted(startPosition,newItems.size());
    }


    @Override
    public final int getItemViewType(int position) {
        return data.get(position) != null ? getLMItemViewType(position) : LOADING_VIEW_TYPE;
    }


    public abstract int getLMItemViewType(int position);
    public abstract RecyclerView.ViewHolder onLMCreateViewholder(ViewGroup parent, int viewType);
    public abstract void onLMBindViewHolder(RecyclerView.ViewHolder holder,int position);

}
