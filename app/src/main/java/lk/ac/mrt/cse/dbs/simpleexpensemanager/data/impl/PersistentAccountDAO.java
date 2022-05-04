package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DbHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PersistentAccountDAO implements AccountDAO {
    DbHelper dh;

    public static final String TABLE = "Accounts";
    public static final String ACCOUNT_NO = "account_no";
    public static final String BANK_NAME = "bank_name";
    public static final String ACCOUNT_HOLDER_NAME = "account_holder";
    public static final String BALANCE = "initial_balance";

    public PersistentAccountDAO(DbHelper dh) {
        this.dh = dh;
    }

    @Override
    public List<String> getAccountNumbersList() {

        return dh.getAccountNumberslList(ACCOUNT_NO,TABLE);
    }

    @Override
    public List<Account> getAccountsList() {
        return dh.getAccountsList(ACCOUNT_NO,TABLE);
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        return dh.getAccount(ACCOUNT_NO,TABLE,accountNo);

    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase database = dh.getWritableDatabase();
        ContentValues values  = new ContentValues();

        values.put(ACCOUNT_NO,account.getAccountNo());
        values.put(BANK_NAME,account.getBankName());
        values.put(ACCOUNT_HOLDER_NAME,account.getAccountHolderName());
        values.put(BALANCE,account.getBalance());

        long ret = database.insert(TABLE, null, values);
        database.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase database = dh.getWritableDatabase();

        String query = "DELETE FROM " + TABLE + " WHERE " +ACCOUNT_NO+ " = " + accountNo;
        Cursor cursor = database.rawQuery(query, null);
        cursor.close();
        database.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        String query = "SELECT "+BALANCE+" FROM " + TABLE + " WHERE "+ACCOUNT_NO+" = "+accountNo;
        SQLiteDatabase db = dh.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        double balance;
        if (cursor.moveToFirst()){
            balance = cursor.getFloat(0);
            switch (expenseType) {
                case EXPENSE:
                    balance -= amount;
                case INCOME:
                    balance+= amount;
            }
        } else {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        cursor.close();
        String query2 = "UPDATE "+ TABLE + " SET " +BALANCE +" = "+ Double.toString(balance) +" WHERE "+ ACCOUNT_NO+ " = "+ accountNo;
        Cursor cursor1 =db.rawQuery(query2,null);
        cursor1.close();
        db.close();
    }

}
