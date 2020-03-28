package ru.tpu.courses.lab3;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.core.util.ObjectsCompat;

public class Student extends StudentGroupListItem implements Parcelable {

	@NonNull
	public String firstName;
	@NonNull
	public String secondName;
	@NonNull
	public String lastName;

	public String groupName;

	public Student(@NonNull String firstName, @NonNull String secondName, @NonNull String lastName) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.secondName = secondName;
		this.groupName = "";
	}

	protected Student(Parcel in) {
		firstName = in.readString();
		lastName = in.readString();
		secondName = in.readString();
		groupName = in.readString();
	}

	public static final Creator<Student> CREATOR = new Creator<Student>() {
		@Override
		public Student createFromParcel(Parcel in) {
			return new Student(in);
		}

		@Override
		public Student[] newArray(int size) {
			return new Student[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(firstName);
		dest.writeString(lastName);
		dest.writeString(secondName);
		dest.writeString(groupName);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Student)) return false;
		Student student = (Student) o;
		return lastName.equals(student.lastName) &&
				firstName.equals(student.firstName) &&
				secondName.equals(student.secondName);
	}

	@Override
	public int hashCode() {
		return ObjectsCompat.hash(lastName, firstName, secondName, groupName);
	}

	@Override
	public int getType() {
		return TYPE_STUDENT;
	}
}
