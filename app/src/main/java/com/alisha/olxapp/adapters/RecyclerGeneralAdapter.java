package com.alisha.olxapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

public class RecyclerGeneralAdapter<T> extends RecyclerView.Adapter<RecyclerGeneralAdapter.RecyclerGeneralViewHolder> {

    private int layoutFile;
    private ArrayList<T> data;
    private OnViewBinder<T> binder;
    private OnViewTypeDecision viewTypeDecision;
    private Map<Integer,Integer> viewTypes;

    public RecyclerGeneralAdapter(int layoutFile, ArrayList<T> data, OnViewBinder<T> binder) {
        this.layoutFile = layoutFile;
        this.data = data;
        this.binder = binder;
    }

    public RecyclerGeneralAdapter(ArrayList<T> data, OnViewBinder<T> binder, OnViewTypeDecision viewTypeDecision, Map<Integer, Integer> viewTypes) {
        this.data = data;
        this.binder = binder;
        this.viewTypeDecision = viewTypeDecision;
        this.viewTypes = viewTypes;
    }

    @NonNull
    @Override
    public RecyclerGeneralViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewTypes == null){
            return new RecyclerGeneralViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutFile,parent,false));
        }else{
            return new RecyclerGeneralViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewTypes.get(viewType),parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerGeneralViewHolder holder, int position) {
        binder.onBindView(data.get(position),holder);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return viewTypeDecision.onViewType(position);
    }

    public static class RecyclerGeneralViewHolder extends RecyclerView.ViewHolder{

        RecyclerGeneralViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface OnViewBinder<T>{
        void onBindView(T data,RecyclerGeneralViewHolder viewHolder);
    }

    public interface OnViewTypeDecision{
        int onViewType(int position);
    }
}
