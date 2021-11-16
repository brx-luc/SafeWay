package zli.safeway;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditNumberActivity extends AppCompatActivity {

    private Button editButton;
    private Button deleteButton;
    private DBHelper db;
    private Integer id;
    private TableLayout table;
    int state = 0;
    TableRow row;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_number);

        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);
        table = findViewById(R.id.table);
        table.setStretchAllColumns(true);
        fillTable();
    }

    public void editContact(View v){
        Intent intent = new Intent(this, UpdateNumberActivity.class);
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
                row.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        rowColor(finalRow);
                        Toast.makeText(getApplicationContext(), " "+id + " ", Toast.LENGTH_SHORT).show();
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
                //id = Integer.parseInt(row.getChildAt(1).toString());
                state = 1;
                break;
            case 1:
                row.setBackgroundColor(Color.LTGRAY);
                state = 0;
                break;
        }

    }


}