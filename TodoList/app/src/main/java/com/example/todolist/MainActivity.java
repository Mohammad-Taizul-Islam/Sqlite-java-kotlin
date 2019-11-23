package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG=MainActivity.class.getName();
    private  TaskDbHelper dbHelper;
    private ListView listView;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper=new TaskDbHelper(this);
        listView=findViewById(R.id.taskList);
        uiUpdate();
        mAdapter=null;
    }

    private void uiUpdate() {
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        ArrayList<String> taskList=new ArrayList<>();
        Cursor cursor=db.query(Tasks.TaskEntry.TABLE_NAME,
                new String[]{Tasks.TaskEntry._ID,Tasks.TaskEntry.COL_TITLE},
                null,null,null,null,null);

        while (cursor.moveToNext())
        {
            int index=cursor.getColumnIndex(Tasks.TaskEntry.COL_TITLE);
            taskList.add(cursor.getString(index));
        }
        if(mAdapter==null)
        {
            mAdapter=new ArrayAdapter<>(this
            ,R.layout.item_todo,
                    R.id.task_title,
                    taskList);
            listView.setAdapter(mAdapter);
        }else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_add_task :
                add_task();
                return  true;
                default:
                    return super.onOptionsItemSelected(item);
        }

    }

    private void add_task() {
        final EditText editText=new EditText(this);
        AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("Add a new task")
                .setMessage("What do you want to do next ?")
                .setView(editText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String taskTitle=String.valueOf(editText.getText());
                        Log.d(TAG, "onClick: taskTitle");
                        SQLiteDatabase db=dbHelper.getWritableDatabase();
                        ContentValues values=new ContentValues();
                        values.put(Tasks.TaskEntry.COL_TITLE,taskTitle);
                        db.insertWithOnConflict(Tasks.TaskEntry.TABLE_NAME,null,values,SQLiteDatabase.CONFLICT_REPLACE);

                        db.close();

                        uiUpdate();
                    }
                })
                .setNegativeButton("Cancel",null)
                .create();
        dialog.show();
    }

    public void deleteTask(View view) {
        View root=(View)view.getParent();
        TextView taskTextView=root.findViewById(R.id.task_title);
        String taskTitle=String.valueOf(taskTextView.getText());
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete(Tasks.TaskEntry.TABLE_NAME,Tasks.TaskEntry.COL_TITLE + " =  ?" , new String[]{taskTitle});
        db.close();

        uiUpdate();
    }
}
