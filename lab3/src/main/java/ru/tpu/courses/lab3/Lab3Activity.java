package ru.tpu.courses.lab3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import ru.tpu.courses.lab3.adapter.GroupStudentAdapter;

public class Lab3Activity extends AppCompatActivity implements GroupStudentAdapter.onStudentClickListener{

    private static final int REQUEST_STUDENT_ADD = 1;
    private static final int REQUEST_STUDENT_EDIT = 2;
    private static final int REQUEST_GROUP_ADD = 3;
    private static String TAG = "Lab3Activity";
    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, Lab3Activity.class);
    }

    private final StudentsCache studentsCache = StudentsCache.getInstance();
    private final GroupsCache groupsCache = GroupsCache.getInstance();

    private RecyclerView list;
    private FloatingActionButton fab;
    private FloatingActionButton fabGroup;

    private GroupStudentAdapter groupStudentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.lab3_title, getClass().getSimpleName()));

        setContentView(R.layout.lab3_activity);
        list = findViewById(android.R.id.list);
        fab = findViewById(R.id.fab);
        fabGroup = findViewById(R.id.fab_group);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);

        list.setAdapter(groupStudentAdapter = new GroupStudentAdapter(this));
        groupStudentAdapter.setData(studentsCache.getStudents(), groupsCache.getGroups());

        fab.setOnClickListener(
                v -> startActivityForResult(
                        AddStudentActivity.newIntent(this),
                        REQUEST_STUDENT_ADD
                )
        );

        fabGroup.setOnClickListener(
                v -> startActivityForResult(
                        AddGroupActivity.newIntent(this),
                        REQUEST_GROUP_ADD
                )
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == REQUEST_GROUP_ADD){
                Group group = AddGroupActivity.getResultGroup(data);
                groupsCache.addGroup(group);
                return;
            }else{
                Student student = AddStudentActivity.getResultStudent(data);
                if(requestCode == REQUEST_STUDENT_EDIT){
                    Student oldStudent = AddStudentActivity.getOldStudent();
                    studentsCache.removeStudent(oldStudent);
                }
                studentsCache.addStudent(student);
            }

            groupStudentAdapter.setData(studentsCache.getStudents(), groupsCache.getGroups());
            groupStudentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStudentClick(int hashCode) {
        Student student = studentsCache.getByHashCode(hashCode);
        startActivityForResult(
                AddStudentActivity.newIntent(this, student),
                REQUEST_STUDENT_EDIT
        );
    }
}
