package com.jonh.easyenglish.Util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.jonh.easyenglish.R;
import com.jonh.easyenglish.domain.Vocabulario;

import java.util.ArrayList;

/**
 * Created by jonh on 30/05/16.
 */
public class VocabArrayAdapter extends ArrayAdapter<Vocabulario> {

    Activity ctx;
    //Vocabulario[] objects;
    ArrayList<Vocabulario> objects;
    public VocabArrayAdapter(Activity ctx, ArrayList<Vocabulario> objects){
        super(ctx,0,objects);
        this.ctx = ctx;
        this.objects= objects;
    }

    @Override
    public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
        return getView(position, cnvtView, prnt);
    }
    @Override
    public View getView(int pos, View cnvtView, ViewGroup prnt) {
        LayoutInflater inflater = (LayoutInflater)ctx.getLayoutInflater();
        //getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View myView = inflater.inflate(R.layout.vocabulary_list_item, null);

        TextView txtEnglish = (TextView) myView.findViewById(R.id.txt_english);
        TextView txtSpanish = (TextView) myView.findViewById(R.id.txt_spanish);

        txtEnglish.setText(objects.get(pos).getEnglish());
        txtSpanish.setText(objects.get(pos).getSpanish());

        return myView;
    }
}
