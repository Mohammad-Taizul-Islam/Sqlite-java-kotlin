package com.example.todolist;

import android.provider.BaseColumns;

public class Tasks {

    public static final String DATABASE_NAME="com.example.todolist.db";
    public static final int DATABASE_VERSION=1;

    public class TaskEntry implements BaseColumns {

        public static final String TABLE_NAME="tasks";

        public static final String _ID="id";


        public static final String COL_TITLE="title";

    }
}
