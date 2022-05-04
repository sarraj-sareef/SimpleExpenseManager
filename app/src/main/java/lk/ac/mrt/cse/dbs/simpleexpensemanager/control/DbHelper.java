package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;


public class DbHelper extends SQLiteOpenHelper{
    public DbHelper(@Nullable Context context) {

        super(context, "190569J.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String accountTableQuery = "CREATE TABLE Accounts (account_no TEXT PRIMARY KEY, bank_name TEXT NOT NULL, account_holder_name TEXT NOT NULL, balance REAL NOT NULL)";
        String transactionTableQuery = "CREATE TABLE Transactions (id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT NOT NULL, account_no TEXT NOT NULL, expense_type TEXT NOT NULL, amount REAL NOT NULL, FOREIGN KEY(account_no) REFERENCES Account(account_no))";
        sqLiteDatabase.execSQL(accountTableQuery);
        sqLiteDatabase.execSQL(transactionTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS Accounts;");
        db.execSQL("DROP TABLE IF EXISTS Transactions;");
        onCreate(db);
    }
    public List<String> getAccountNumberslList(String ACCOUNT_NO,String TABLE){
        List<String> accountList = new ArrayList<>();

        String query = "SELECT "+ ACCOUNT_NO +" FROM " + TABLE ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()){
            do{
                String accNo = cursor.getString(0);
                accountList.add(accNo);

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return accountList;
    }

    public List<Account> getAccountsList(String ACCOUNT_NO,String TABLE) {
        List<Account> accountList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()){
            do{
                String accNo = cursor.getString(0);
                String bankName = cursor.getString(1);
                String userName = cursor.getString(2);
                double balance = cursor.getDouble(3);

                Account acc = new Account(accNo,bankName,userName,balance);
                accountList.add(acc);

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return accountList;
    }
    public Account getAccount(String ACCOUNT_NO,String TABLE,String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+ TABLE + " WHERE " + ACCOUNT_NO + " = " + accountNo;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.isNull(0) == false) {
            String accNo = cursor.getString(0);
            String bankName = cursor.getString(1);
            String userName = cursor.getString(2);
            double balance = cursor.getDouble(3);

            Account acc = new Account(accNo,bankName,userName,balance);
            return acc;
        }

        String errorMessage = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(errorMessage);

    }

}
