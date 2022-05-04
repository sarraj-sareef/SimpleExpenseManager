package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

public class PersistentDemoExpenseManager extends ExpenseManager{
    MainActivity context;

    public PersistentDemoExpenseManager(MainActivity context) {
        this.context=context;
        try {
            setup();
        } catch (ExpenseManagerException e) {
            System.out.println(e);
        }

    }

    @Override
    public void setup() throws ExpenseManagerException {
        DbHelper databaseHelper = new DbHelper(this.context);
        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(databaseHelper);

        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = new PersistentAccountDAO(databaseHelper);
        setAccountsDAO(persistentAccountDAO);

    }


}
