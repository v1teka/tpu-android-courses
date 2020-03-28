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
import ru.tpu.courses.lab3.Student;

public class StudentHolder extends RecyclerView.ViewHolder{

    private static String TAG = "StudentHolder";
    public final TextView student;
    private int studentId;
    GroupStudentAdapter.onStudentClickListener onStudentClickListener;

    public StudentHolder(ViewGroup parent, GroupStudentAdapter.onStudentClickListener listener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.lab3_item_student, parent, false));
        student = itemView.findViewById(R.id.student);

        this.onStudentClickListener = listener;

        student.setOnClickListener(view -> {
            onStudentClickListener.onStudentClick(studentId);
        });
    }

    public void setStudent(Student student){
        studentId = student.hashCode();
        this.student.setText(
                student.lastName + " " + student.firstName + " " + student.secondName
        );
    }
}
