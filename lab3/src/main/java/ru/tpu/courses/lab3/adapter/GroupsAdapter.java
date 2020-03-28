package ru.tpu.courses.lab3.adapter;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.tpu.courses.lab3.Group;

public class GroupsAdapter{

    public static final int TYPE_NUMBER = 0;
    public static final int TYPE_GROUP = 1;

    private List<Group> groups = new ArrayList<>();


    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

}
