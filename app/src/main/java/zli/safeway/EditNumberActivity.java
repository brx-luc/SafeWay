package zli.safeway;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditNumberActivity extends AppCompatActivity {

    private Button editButton;
    private Button deleteButton;
    private Button cancelButton;
    private DBHelper db;
    private Integer id;
    private TableLayout table;
    private String firstname;
    private String name;
    private String phone;
    int state = 0;
    TableRow row;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_number);

        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);
        cancelButton = findViewById(R.id.cancelEditButton);
        table = findViewById(R.id.table);
        table.setStretchAllColumns(true);
        fillTable();
    }

    public void editContact(View v){
        Intent intent = new Intent(EditNumberActivity.this, UpdateNumberActivity.class);

        intent.putExtra("id", id.toString());
        intent.putExtra("name", name);
        intent.putExtra("firstname", firstname);
        intent.putExtra("phone", phone);

        startActivity(intent);
    }

    public void deleteContact(View v){
        db = new DBHelper(this);
        db.deleteContact(id);

        Intent intent = new Intent(null, MainActivity.class);
        startActivity(intent);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void fillTable(){
        TextView t1;
        db = new DBHelper(this);
        ArrayList<String> data = db.getAllContacts();


        for(int i = 0; i < data.size()/4; i++){
            row = new TableRow(this);
            for(int j = 0; j < 4; j++) {
                t1 = new TextView(this);
                t1.setText(data.get(4 * i + j));
                row.addView(t1);
                TableRow finalRow = row;
                int finalI = i;
                row.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        rowColor(finalRow);
                        id = Integer.parseInt(data.get(4 * finalI));
                        name = data.get(4 * finalI + 1);
                        firstname = data.get(4 * finalI + 2);
                        phone = data.get(4 * finalI + 3);
                        return false;
                    }
                });

            }
            table.addView(row);
        }

    }

    public void rowColor(TableRow row){
        switch (state){
            case 0:
                row.setBackgroundColor(Color.WHITE);
                state = 1;
                break;
            case 1:
                row.setBackgroundColor(Color.LTGRAY);
                state = 0;
                break;
        }

    }

    public void cancelEdit(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}