package ru.tpu.courses.lab2;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.widget.LinearLayout;
import 	android.widget.Toast;
import android.widget.TextView;

import androidx.annotation.Px;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Lab2ViewsContainer extends LinearLayout {

    private int minViewsCount;
    private List<Double> viewsValues;

    public Lab2ViewsContainer(Context context) {
        this(context, null);
    }

    public Lab2ViewsContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Lab2ViewsContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Lab2ViewsContainer, defStyleAttr, 0);

        minViewsCount = a.getInt(R.styleable.Lab2ViewsContainer_lab2_minViewsCount, 0);
        if (minViewsCount < 0) {
            throw new IllegalArgumentException("minViewsCount can't be less than 0");
        }
        a.recycle();

        initializeValues();
    }


    public void initializeValues(){
        setViewsValues(initialValues(minViewsCount));
    }

    private Random rand = new Random();
    private double randomValue(){
        return Math.round(rand.nextDouble()*100)/10.;
    }

    private double[] initialValues(int count){
        double[] initialValues = new double[count];
        for(int i=0;i<count;i++)
            initialValues[i] = randomValue();

        return initialValues;
    }

    public void addValue() {
        double val = randomValue();
        viewsValues.add(val);
        double[] newValues = new double[viewsValues.size()];
        for(int i=0;i<newValues.length;i++)
            newValues[i] = viewsValues.get(i);
        setViewsValues(newValues);
    }

    public void addViewValue(double viewValue, int number, boolean max) {
        Lab2View lab2View = new Lab2View(getContext());
        lab2View.setValue(viewValue);
        lab2View.setMax(max);
        lab2View.setTitle("Запись номер "+number);
        addView(lab2View);
    }

    public void setViewsValues(double[] values) {
        removeAllViews();
        viewsValues = new ArrayList<>();
        double maxValue = getMaxValue(values);
        for (int i = 0; i < values.length; i++) {
            viewsValues.add(values[i]);
            addViewValue(values[i], i+1, (values[i] == maxValue));
        }
    }

    public double[] getViewsValues() {
        double[] target = new double[viewsValues.size()];
        for (int i = 0; i < target.length; i++) {
            target[i] = viewsValues.get(i);
        }
        return target;
    }

    private double getMaxValue(double[] values){
        int maxIndex = 0;
        int i=0;
        for(double value : values){
            if(value > values[maxIndex]){
                maxIndex = i;
            }
            i++;
        }
        return values[maxIndex];
    }
}