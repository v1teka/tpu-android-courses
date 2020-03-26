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
        }
    }


    /**
     * Переопределив этот метод мы можем добавить действия в меню ActionBar-а. Это иконки справа.
     * Задаются они обычно через XML в ресурсах типа menu и инфлейтятся по аналогии с View.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lab3_add_student, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Этот метод вызывается когда пользователь нажимает на любую из созданных ранее меню.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Если пользователь нажал "назад", то просто закрываем Activity
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        // Если пользователь нажал "Сохранить"
        if (item.getItemId() == R.id.action_save) {
            // Создаём объект студента из введенных
            Student student = new Student(
                    firstName.getText().toString(),
                    secondName.getText().toString(),
                    lastName.getText().toString()
            );

            // Проверяем, что все поля были указаны
            if (TextUtils.isEmpty(student.firstName) ||
                    TextUtils.isEmpty(student.secondName) ||
                    TextUtils.isEmpty(student.lastName)) {
                // Класс Toast позволяет показать системное уведомление поверх всего UI
                Toast.makeText(this, R.string.lab3_error_empty_fields, Toast.LENGTH_LONG).show();
                return true;
            }

            // Проверяем, что точно такого же студента в списке нет
            if (studentsCache.contains(student)) {
                Toast.makeText(this, R.string.lab3_error_already_exists, Toast.LENGTH_LONG).show();
                return true;
            }

            // Сохраняем Intent с инфорамцией от этой Activity, который будет передан в onActivityResult
            // вызвавшей его Activity.
            Intent data = new Intent();
            // Сохраяем объект студента. Для того, чтобы сохранить объект класса, он должен реализовывать
            // интерфейс Parcelable или Serializable, т.к. Intent передаётся в виде бинарных данных
            data.putExtra(EXTRA_STUDENT, student);
            // Указываем resultCode и сам Intent, которые будут переданы вызвавшей нас Activity в методе
            // onActivityResult
            setResult(RESULT_OK, data);
            // Закрываем нашу Activity
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
