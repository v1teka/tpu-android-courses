package ru.tpu.courses.lab3.adapter;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.tpu.courses.lab3.Group;
import ru.tpu.courses.lab3.Student;

public class StudentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_NUMBER = 0;
    public static final int TYPE_STUDENT = 1;
    public static final int TYPE_GROUP = 2;

    private List<Student> students = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();
    private onStudentClickListener mOnStudentClickListener;

    public StudentsAdapter(onStudentClickListener listener){
        mOnStudentClickListener = listener;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_NUMBER:
                return new NumberHolder(parent);
            case TYPE_STUDENT:
                //return new StudentHolder(parent, mOnStudentClickListener);
            case TYPE_GROUP:
                return new GroupHolder(parent);
        }
        throw new IllegalArgumentException("unknown viewType = " + viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Student student = students.get(position / 2);
        switch (getItemViewType(position)) {
            case TYPE_STUDENT:
                StudentHolder studentHolder = (StudentHolder) holder;
                studentHolder.student.setText(
                        student.lastName + " " + student.firstName + " " + student.secondName
                );
                break;
            case TYPE_GROUP:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return students.size() * 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? TYPE_NUMBER : TYPE_STUDENT;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public interface onStudentClickListener{
        void onStudentClick(int id);
    }
}


