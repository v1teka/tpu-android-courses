package ru.tpu.courses.lab3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import ru.tpu.courses.lab3.adapter.StudentsAdapter;
import ru.tpu.courses.lab3.adapter.GroupsAdapter;

/**
 * Activity с полями для заполнения ФИО студента.
 */
public class AddStudentActivity extends AppCompatActivity {

    private static final String EXTRA_STUDENT = "student";
    private static Student oldStudent;

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, AddStudentActivity.class);
    }

    public static Intent newIntent(@NonNull Context context, Student student) {
        Intent ourIntent = newIntent(context);

        ourIntent.putExtra(EXTRA_STUDENT, student);
        oldStudent = student;
        return ourIntent;
    }

    public static Student getOldStudent(){
        return oldStudent;
    }

    public static Student getResultStudent(@NonNull Intent intent) {
        return intent.getParcelableExtra(EXTRA_STUDENT);
    }

    private final StudentsCache studentsCache = StudentsCache.getInstance();

    private EditText firstName;
    private EditText secondName;
    private EditText lastName;
    private Spinner groupSpinner;

    private StudentsAdapter studentsAdapter;
    private static GroupsCache groupsCache = GroupsCache.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab3_activity_add_student);

        /*
        Метод getSupportActionBar() возвращает ActionBar (полоска сверху с элементами навигации и
        заголовком). Метод setDisplayHomeAsUpEnabled позволяет добавить кнопку назад к ActionBar-у.
        Когда необходимо сильно закастомизировать ActionBar, можно воспользоваться Toolbar-ом, либо
        вообще реализовать View самому. Классы ActionBar и Toolbar позволяют реализовать большинство
        стандартных поведений соответственно design guidelines от Google.
         */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firstName = findViewById(R.id.first_name);
        secondName = findViewById(R.id.second_name);
        lastName = findViewById(R.id.last_name);
        groupSpinner = findViewById(R.id.lab3_spinner);

        ArrayAdapter<Group> adapter = new ArrayAdapter<Group>(getApplicationContext(), R.layout.lab3_item_group, R.id.group);
        adapter.addAll(groupsCache.getGroups());
        groupSpinner.setAdapter(adapter);

        Bundle arguments = getIntent().getExtras();

        if(arguments!=null){
            Student student = arguments.getParcelable(EXTRA_STUDENT);
            firstName.setText(student.firstName);
            lastName.setText(student.lastName);
            secondName.setText(student.secondName);
            if(student.groupName.length() > 0)
                selectSpinnerItemByValue(groupSpinner, student.groupName);
        }
    }

    private static void selectSpinnerItemByValue(Spinner spnr, String value) {
        SpinnerAdapter adapter = (SpinnerAdapter) spnr.getAdapter();
        for (int position = 0; position < adapter.getCount(); position++) {
            if(adapter.getItem(position).toString() == value) {
                spnr.setSelection(position);
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lab3_add_student, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        if (item.getItemId() == R.id.action_save) {
            Student student = new Student(
                    firstName.getText().toString(),
                    secondName.getText().toString(),
                    lastName.getText().toString()
            );

            Object selectedGroupName = groupSpinner.getSelectedItem();
            if(selectedGroupName != null) {
                student.groupName = selectedGroupName.toString();
            }

            if (TextUtils.isEmpty(student.firstName) ||
                    TextUtils.isEmpty(student.secondName) ||
                    TextUtils.isEmpty(student.lastName)) {
                Toast.makeText(this, R.string.lab3_error_empty_fields, Toast.LENGTH_LONG).show();
                return true;
            }

            if (studentsCache.contains(student)) {
                Toast.makeText(this, R.string.lab3_error_already_exists, Toast.LENGTH_LONG).show();
                return true;
            }

            Intent data = new Intent();
            data.putExtra(EXTRA_STUDENT, student);
            setResult(RESULT_OK, data);

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
