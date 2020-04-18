package ru.tpu.courses.lab5;

public class Repo {
    public String fullName;
    public String url;
    public String description;

    @Override
    public String toString() {
        return "Repo{" +
                "fullName='" + fullName + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}