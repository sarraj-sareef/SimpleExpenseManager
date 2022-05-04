package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DbHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;


public class PersistentTransactionDAO implements TransactionDAO{
    DbHelper dbHelper;

    public static final String TABLE = "Transactions";

    public static final String TRANSACTION_ID = "id";

    public static final String DATE = "date";
    public static final String EXPENSE_TYPE = "expense_type";
    public static final String AMOUNT = "amount";
    public static final String ACCOUNT_NO = "account_no";

    public PersistentTransactionDAO(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        String date_pattern = "MM/dd/yyyy";
        DateFormat dateFormat = new SimpleDateFormat(date_pattern);
        String string_date = dateFormat.format(date);
        String type = expenseType.toString();


        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DATE, string_date);
        values.put(ACCOUNT_NO, accountNo);
        values.put(EXPENSE_TYPE, type);
        values.put(AMOUNT, amount);

        db.insert(TABLE, null, values);
        db.close();

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> resultList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE;
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(query,null);

        if (cursor.moveToFirst()){
            do{
                String id = cursor.getString(0);
                Date date = null;
                try {
                    date = new SimpleDateFormat("dd/MM/yyyy").parse(cursor.getString(1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String accNo = cursor.getString(2);
                ExpenseType expenseType = ExpenseType.valueOf(cursor.getString(3));
                double amount = cursor.getDouble(4);

                Transaction tx = new Transaction(date,accNo,expenseType,amount);
                resultList.add(tx);

            }while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return resultList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions = getAllTransactionLogs();

        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        return transactions.subList(size - limit, size);
    }

}
