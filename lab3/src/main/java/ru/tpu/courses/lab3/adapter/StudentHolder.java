package ru.tpu.courses.lab3.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import ru.tpu.courses.lab3.R;

public class StudentHolder extends RecyclerView.ViewHolder{

    private static String TAG = "StudentHolder";
    public final TextView student;
    StudentsAdapter.onStudentClickListener onStudentClickListener;

    public StudentHolder(ViewGroup parent, StudentsAdapter.onStudentClickListener listener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.lab3_item_student, parent, false));
        student = itemView.findViewById(R.id.student);

        this.onStudentClickListener = listener;

        student.setOnClickListener(view -> {
            onStudentClickListener.onStudentClick(getAdapterPosition());
        });
    }
}
