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

import java.time.Duration;

import ru.tpu.courses.lab3.adapter.StudentsAdapter;
import ru.tpu.courses.lab3.adapter.GroupsAdapter;

/**
 * <b>RecyclerView, взаимодействие между экранами. Memory Cache.</b>
 * <p>
 * View, добавленные в {@link android.widget.ScrollView} отрисовываются все разом, при этом выводится
 * пользователю только та часть, до которой доскроллил пользователь. Соответственно, это замедляет
 * работу приложения и в случае с особо большим количеством View может привести к
 * {@link OutOfMemoryError}, краша приложение, т.к. система не может уместить все View в памяти.
 * </p>
 * <p>
 * {@link RecyclerView} - компонент для работы со списками, содержащими большое количество данных,
 * который призван исправить эту проблему. Это точно такой же {@link android.view.ViewGroup}, как и
 * ScrollView, но он содержит только те View, которые сейчас видимы пользователю. Работать с ним
 * намного сложнее, чем с ScrollView, поэтому если известно, что контент на экране статичен и не
 * содержит много элементов, то для простоты лучше воспользоваться ScrollView.
 * </p>
 * <p>
 * Для работы RecyclerView необходимо подключить отдельную библиотеку (см. build.gradle)
 * </p>
 */
public class Lab3Activity extends AppCompatActivity implements StudentsAdapter.onStudentClickListener{

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

    private StudentsAdapter studentsAdapter;
    private GroupsAdapter groupsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.lab3_title, getClass().getSimpleName()));

        setContentView(R.layout.lab3_activity);
        list = findViewById(android.R.id.list);
        fab = findViewById(R.id.fab);
        fabGroup = findViewById(R.id.fab_group);

        /*
        Здесь идёт инициализация RecyclerView. Первое, что необходимо для его работы, это установить
        реализацию LayoutManager-а. Он содержит логику размещения View внутри RecyclerView. Так,
        LinearLayoutManager, который используется ниже, располагает View последовательно, друг за
        другом, по аналогии с LinearLayout-ом. Из альтернатив можно например использовать
        GridLayoutManager, который располагает View в виде таблицы. Необходимость написания своего
        LayoutManager-а возникает очень редко и при этом является весьма сложным процессом, поэтому
        рассматриваться в лабораторной работе не будет.
         */
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);

        /*
        Следующий ключевой компонент - это RecyclerView.Adapter. В нём описывается вся информация,
        необходимая для заполнения RecyclerView. В примере мы выводим пронумерованный список
        студентов, подробнее о работе адаптера в документации к классу StudentsAdapter.
         */
        list.setAdapter(studentsAdapter = new StudentsAdapter(this));
        studentsAdapter.setStudents(studentsCache.getStudents());

        groupsAdapter = new GroupsAdapter();

        /*
        При нажатии на кнопку мы переходим на Activity для добавления студента. Обратите внимание,
        что здесь используется метод startActivityForResult. Этот метод позволяет организовывать
        передачу данных обратно от запущенной Activity. В нашем случае, после закрытия AddStudentActivity,
        у нашей Activity будет вызван метод onActivityResult, в котором будут данные, которые мы
        указали перед закрытием AddStudentActivity.
         */
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

    /**
     * Этот метод вызывается после того, как мы ушли с запущенной с помощью метода
     * {@link #startActivityForResult(Intent, int)} Activity.
     *
     * @param requestCode переданный в метод startActivityForResult requestCode, для случаев,
     *                    когда с нашей активитизапускается несколько различных активити. По этому
     *                    идентификатору мы их различаем.
     * @param resultCode  идентификатор, описывающий, с каким результатом запущенная активити была
     *                    завершена. Если пользователь просто закрыл Activity, то по умолчанию будет
     *                    {@link #RESULT_CANCELED}.
     * @param data        даные переданные нам от запущенной Activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == REQUEST_GROUP_ADD){
                Group group = AddGroupActivity.getResultGroup(data);
                groupsCache.addGroup(group);
                groupsAdapter.setGroups(groupsCache.getGroups());
                return;
            }

            Student student = AddStudentActivity.getResultStudent(data);
            int change = 2;
            Log.d(TAG, "onActivityResult: "+student.firstName);
            if(requestCode == REQUEST_STUDENT_EDIT){
                Student oldStudent = AddStudentActivity.getOldStudent();
                studentsCache.removeStudent(oldStudent);
                change=0;
            }
            studentsCache.addStudent(student);
            studentsAdapter.setStudents(studentsCache.getStudents());
            //studentsAdapter.notifyItemRangeInserted(studentsAdapter.getItemCount() - change, change);
            studentsAdapter.notifyDataSetChanged();
            list.scrollToPosition(studentsAdapter.getItemCount() - 1);
        }
    }

    @Override
    public void onStudentClick(int id) {
        int position = id/2;
        Student student = studentsCache.getStudents().get(position);
        startActivityForResult(
                AddStudentActivity.newIntent(this, student),
                REQUEST_STUDENT_EDIT
        );
    }
}
