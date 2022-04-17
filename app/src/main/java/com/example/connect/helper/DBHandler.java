package com.example.connect.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "userinfo";
    SharedPreferences sharedPreferences;

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_NAME = "userdata";

    // below variable is for our id column.
    private static final String ID_COL = "id";

    // below variable is for our course name column
    private static final String NAME_COL = "name";

    // below variable id for our course duration column.
    private static final String ACCOUNT_COL = "account";

    // below variable for our course description column.
    private static final String DATE_COL = "date";

    // below variable is for our course tracks column.
    private static final String REFERENCE_COL = "reference";
    private  static  final String USER_COL = "userid";
    private  static  final String AMOUNT_COL = "amt";

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + ACCOUNT_COL + " TEXT,"
                + AMOUNT_COL + " TEXT,"
                + USER_COL + " TEXT,"
                + DATE_COL + " TEXT,"
                + REFERENCE_COL + " TEXT)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }

    // this method is use to add new course to our sqlite database.
    public void add_information(String username, String account_number, String date, String reference,String userid,String amount) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(NAME_COL, username);
        values.put(ACCOUNT_COL, account_number);
        values.put(DATE_COL, date);
        values.put(REFERENCE_COL, reference);
        values.put(USER_COL,userid);
        values.put(AMOUNT_COL,amount);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

 @SuppressLint("Range")
 public ArrayList<userinfo>   getUser(String id) {
     String sql = "select * from userdata where userid=?";
     Cursor cursor = getReadableDatabase().rawQuery(sql, new String[]{id});
     ArrayList<userinfo> userinfoArrayList = new ArrayList<>();
     cursor.moveToNext();

     if(cursor !=null) {

         while (cursor.moveToNext()) {
             userinfo uinfo = new userinfo();
             uinfo.setId(cursor.getString(cursor.getColumnIndex("id")));
             uinfo.setName(cursor.getString(cursor.getColumnIndex("name")));
             uinfo.setAccount(cursor.getString(cursor.getColumnIndex("account")));
             uinfo.setReference(cursor.getString(cursor.getColumnIndex("reference")));
             uinfo.setAmount(cursor.getString(cursor.getColumnIndex("amt")));

             userinfoArrayList.add(uinfo);


         }
     }
     cursor.close();
     return userinfoArrayList;
     }



    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> GetUserByUserId(String userid){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NAME, new String[]{NAME_COL, AMOUNT_COL, ACCOUNT_COL,REFERENCE_COL}, USER_COL+ "=?",new String[]{String.valueOf(userid)},null, null, null, null);
        if (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            user.put("name",cursor.getString(cursor.getColumnIndex(NAME_COL)));
            user.put("amt",cursor.getString(cursor.getColumnIndex(AMOUNT_COL)));
            user.put("account",cursor.getString(cursor.getColumnIndex(ACCOUNT_COL)));
            user.put("reference",cursor.getString(cursor.getColumnIndex(REFERENCE_COL)));
            userList.add(user);
        }
        return  userList;
    }
}
