package zli.safeway;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SafeWay.db";
    public static final String CONTACTS_TABLE_NAME = "contacts";
    public static final String CONTACTS_COULUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_FIRSTNAME = "firstname";
    public static final String CONTACTS_COLUMN_PHONENUMBER ="phonenumber";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE contacts" + "(id integer primary key autoincrement, name varchar(100) not null, firstname varchar(100) not null, phonenumber varchar(50) not null)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertContact(String name, String firstname, String phonenumber){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("firstname", firstname);
        contentValues.put("phonenumber",phonenumber);
        db.insert("contacts", null, contentValues);
        return true;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM contacts where id=" + id+ "", null);
        return res;
    }

    public boolean updateContact(Integer id, String name, String firstname, String phonenumber){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("firstname", firstname);
        contentValues.put("phonenumber", phonenumber);
        db.update("contacts", contentValues, "id=?", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteContact(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts", "id = ?", new String[]{Integer.toString(id)});
    }

    @SuppressLint("Range")
    public ArrayList<String> getAllContacts(){
        ArrayList<String> contactList = new ArrayList<>();
        Contact c = new Contact(CONTACTS_COULUMN_ID, CONTACTS_COLUMN_NAME, CONTACTS_COLUMN_FIRSTNAME,CONTACTS_COLUMN_PHONENUMBER);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM contacts", null);
        res.moveToFirst();

        while(!res.isAfterLast()){
            contactList.add(res.getString(res.getColumnIndex(c.getId())));
            contactList.add(res.getString(res.getColumnIndex(c.getName())));
            contactList.add(res.getString(res.getColumnIndex(c.getFirstname())));
            contactList.add(res.getString(res.getColumnIndex(c.getPhonenumber())));
            res.moveToNext();
        }
        res.close();
        return contactList;

    }

    @SuppressLint("Range")
    public ArrayList<String> getAllNumber(){
        ArrayList<String> contactNumbers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT phonenumber FROM contacts", null);
        res.moveToFirst();

        while(!res.isAfterLast()){
            contactNumbers.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_PHONENUMBER)));
            res.moveToNext();
        }
        res.close();
        return contactNumbers;
    }
}
