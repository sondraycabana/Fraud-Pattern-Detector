import java.util.LinkedList;
import java.util.Queue;

public class TransactionGenerator {
    public static Queue<Transaction> generateTransactions() {
        Queue<Transaction> transactionsQueue = new LinkedList<>();

        transactionsQueue.add(new Transaction(1617906000, 150.00, "user1", "serviceA"));
        transactionsQueue.add(new Transaction(1617906060, 4500.00, "user2", "serviceB"));
        transactionsQueue.add(new Transaction(1617906120, 75.00, "user1", "serviceC"));
        transactionsQueue.add(new Transaction(1617906180, 3000.00, "user3", "serviceA"));
        transactionsQueue.add(new Transaction(1617906240, 200.00, "user1", "serviceB"));
        transactionsQueue.add(new Transaction(1617906300, 4800.00, "user2", "serviceC"));
        transactionsQueue.add(new Transaction(1617906360, 100.00, "user4", "serviceA"));
        transactionsQueue.add(new Transaction(1617906420, 4900.00, "user3", "serviceB"));
        transactionsQueue.add(new Transaction(1617906480, 120.00, "user1", "serviceD"));
        transactionsQueue.add(new Transaction(1617906540, 5000.00, "user3", "serviceC"));

        transactionsQueue.add(new Transaction(1617906000, 150.00, "user5", "serviceA"));
        transactionsQueue.add(new Transaction(1617906060, 4500.00, "user6", "serviceB"));
        transactionsQueue.add(new Transaction(1617906120, 75.00, "user5", "serviceC"));
        transactionsQueue.add(new Transaction(1617906180, 3000.00, "user7", "serviceA"));
        transactionsQueue.add(new Transaction(1617906240, 200.00, "user5", "serviceB"));
        transactionsQueue.add(new Transaction(1617906300, 4800.00, "user6", "serviceC"));
        transactionsQueue.add(new Transaction(1617906360, 100.00, "user8", "serviceA"));
        transactionsQueue.add(new Transaction(1617906420, 4900.00, "user7", "serviceB"));
        transactionsQueue.add(new Transaction(1617906480, 120.00, "user5", "serviceD"));
        transactionsQueue.add(new Transaction(1617906540, 5000.00, "user7", "serviceC"));

        return transactionsQueue;
    }
}
