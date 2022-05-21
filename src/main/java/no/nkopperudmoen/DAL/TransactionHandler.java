package no.nkopperudmoen.DAL;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayDeque;

public class TransactionHandler {
    private static boolean isQueueRunning = false;
    private static final ArrayDeque<PreparedStatement> transactionQueue = new ArrayDeque<>();

    public static void addToQueue(PreparedStatement statement) {
        transactionQueue.add(statement);
        if (!isQueueRunning) {
            runQueue();
        }
    }

    private static void runQueue() {
        isQueueRunning = true;
        while (!transactionQueue.isEmpty()) {
            try {
                PreparedStatement statement = transactionQueue.pop();
                statement.execute();
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                isQueueRunning = false;
            }
        }
        isQueueRunning = false;
    }
}
