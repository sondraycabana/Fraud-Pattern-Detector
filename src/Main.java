
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        // Generate transactions using TransactionGenerator
        Queue<Transaction> transactions = TransactionGenerator.generateTransactions();

        // Process transactions and detect fraudulent activities
        FraudDetectionSystem fraudDetectionSystem = new FraudDetectionSystem();

        // Process transactions and detect fraudulent activities
        for (Transaction transaction : transactions) {
            fraudDetectionSystem.processTransaction(transaction);
        }
    }
}
