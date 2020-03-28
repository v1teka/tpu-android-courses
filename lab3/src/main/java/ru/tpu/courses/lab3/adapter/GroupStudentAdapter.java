package ru.tpu.courses.lab3.adapter;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.tpu.courses.lab3.Group;
import ru.tpu.courses.lab3.Student;
import ru.tpu.courses.lab3.StudentGroupListItem;

public class GroupStudentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_GROUP = 0;
    public static final int TYPE_STUDENT = 1;

    private List<StudentGroupListItem> studentGroups = new ArrayList<>();
    private onStudentClickListener mOnStudentClickListener;

    public GroupStudentAdapter(onStudentClickListener listener){
        mOnStudentClickListener = listener;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_STUDENT:
                return new StudentHolder(parent, mOnStudentClickListener);
            case TYPE_GROUP:
                return new GroupHolder(parent);
        }
        throw new IllegalArgumentException("unknown viewType = " + viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_STUDENT:
                Student student = (Student) studentGroups.get(position);
                StudentHolder studentHolder = (StudentHolder) holder;
                studentHolder.setStudent(student);
                break;
            case TYPE_GROUP:
                Group group = (Group) studentGroups.get(position);
                GroupHolder groupHolder = (GroupHolder) holder;
                groupHolder.group.setText(group.groupName);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return studentGroups.size();
    }

    @Override
    public int getItemViewType(int position) {
        return studentGroups.get(position).getType();
    }

    public interface onStudentClickListener{
        void onStudentClick(int id);
    }

    public void setData(List<Student> students, List<Group> groups){
        this.studentGroups.clear();
        for (Group group: groups
             ) {
            this.studentGroups.add(group);
            for (Student student:students
                 ) {
                if(student.groupName.equals(group.groupName)){
                    this.studentGroups.add(student);
                }
            }
        }

        for (Student student: students){
            if(student.groupName == "")
            this.studentGroups.add(student);
        }
    }
}


