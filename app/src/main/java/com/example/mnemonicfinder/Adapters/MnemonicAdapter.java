package com.example.mnemonicfinder.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.mnemonicfinder.Models.MnemonicModel;
import com.example.mnemonicfinder.R;

import java.util.List;

public class MnemonicAdapter extends PagerAdapter {
    List<MnemonicModel> mnemonicModel;
    private LayoutInflater layoutInflater;
    private Context context;


    public MnemonicAdapter(List<MnemonicModel> model, Context context){
        this.mnemonicModel = model;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mnemonicModel.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }




    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        TextView mnemonicString;
        View view = layoutInflater.inflate(R.layout.mnemonic_card_layout, container, false);
        mnemonicString = view.findViewById(R.id.mnemonicStr);
        mnemonicString.setText(mnemonicModel.get(position).getMnemonic());
        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((View) object);
    }



}
