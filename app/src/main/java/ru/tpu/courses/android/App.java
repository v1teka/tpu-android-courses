package ru.tpu.courses.android;

import android.app.Application;

import ru.tpu.courses.lab3.Group;
import ru.tpu.courses.lab3.GroupsCache;
import ru.tpu.courses.lab3.Student;
import ru.tpu.courses.lab3.StudentsCache;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // region lab3
        GroupsCache groupsCache = GroupsCache.getInstance();
        groupsCache.addGroup(new Group("Без группы"));
        // endregion lab3
    }
}
