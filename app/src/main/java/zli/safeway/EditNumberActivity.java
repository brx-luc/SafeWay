package zli.safeway;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

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
        fillTable();
    }

    public void editContact(View v){

    }

    public void deleteContact(View v){
        db = new DBHelper(this);
        db.deleteContact(id);
    }

    public void fillTable(){
        db = new DBHelper(this);
        db.getAllContacts();
    }
}