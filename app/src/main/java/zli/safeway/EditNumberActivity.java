package zli.safeway;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class EditNumberActivity extends AppCompatActivity {

    private Button editButton;
    private Button deleteButton;
    private DBHelper db;
    private Integer id;
    private TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_number);

        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);
        table = findViewById(R.id.table);
        table.setStretchAllColumns(true);
        table.setClickable(true);
        fillTable();
    }

    public void editContact(View v){

    }

    public void deleteContact(View v){
        db = new DBHelper(this);
        db.deleteContact(id);
    }

    public void fillTable(){
        TableRow row;
        TextView t1, t2, t3, t4;
        db = new DBHelper(this);
        ArrayList<String> data = db.getAllContacts();


        for(int i = 0; i < data.size()/4; i++){
            row = new TableRow(this);

           /* t2 = new TextView(this);
            t3 = new TextView(this);
            t4 = new TextView(this);*/
            for(int j = 0; j < 4; j++){
                t1 = new TextView(this);
                t1.setText(data.get(4*i+j));

                row.addView(t1);
            }

            /*t2.setText(data.get(i));
            t3.setText(data.get(i));
            t4.setText(data.get(i));

            row.addView(t2);
            row.addView(t3);
            row.addView(t4);*/
            table.addView(row);
        }

    }

}