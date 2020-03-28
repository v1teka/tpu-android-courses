package ru.tpu.courses.lab3;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.core.util.ObjectsCompat;

/**
 * Для передачи самописных объектов через {@link android.content.Intent} или
 * {@link android.os.Bundle}, необходимо реализовать интерфейс {@link Parcelable}. В нём описывается
 * как сохранить и восстановить объект используя примитивные свойства (String, int и т.д.).
 */
public class Group extends StudentGroupListItem implements Parcelable {

	@NonNull
	public String groupName;

	public Group(@NonNull String groupName) {
		this.groupName = groupName;
	}

	protected Group(Parcel in) {
		groupName = in.readString();
	}

	public static final Creator<Group> CREATOR = new Creator<Group>() {
		@Override
		public Group createFromParcel(Parcel in) {
			return new Group(in);
		}

		@Override
		public Group[] newArray(int size) {
			return new Group[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(groupName);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Group)) return false;
		Group student = (Group) o;
		return groupName.equals(student.groupName);
	}

	@Override
	public int hashCode() {
		return ObjectsCompat.hash(groupName);
	}

	@NonNull
	@Override
	public String toString() {
		return groupName;
	}

	@Override
	public int getType() {
		return TYPE_GROUP;
	}
}
