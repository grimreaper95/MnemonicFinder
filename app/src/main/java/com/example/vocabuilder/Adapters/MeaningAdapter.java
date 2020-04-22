package com.example.vocabuilder.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.vocabuilder.Models.MeaningModel;
import com.example.vocabuilder.R;

import java.util.List;

public class MeaningAdapter extends PagerAdapter {
    List<MeaningModel> meaningModel;
    private LayoutInflater layoutInflater;
    private Context context;


    public MeaningAdapter(List<MeaningModel> meaningModel, Context context){
        this.meaningModel = meaningModel;
        this.context = context;
    }

    @Override
    public int getCount() {
        return meaningModel.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }




    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        TextView meaningString;
        View view = layoutInflater.inflate(R.layout.meaning_card_layout, container, false);
        meaningString = view.findViewById(R.id.meaningStr);
        meaningString.setText(meaningModel.get(position).getMeaning());
        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((View) object);
    }
}
