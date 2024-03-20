import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class FraudDetectionSystem {
    // Concurrent data structures for tracking transactions
    private ConcurrentMap<String, Integer> serviceCountMap = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Double> userTotalAmountMap = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Long> userLastTransactionTimeMap = new ConcurrentHashMap<>();
    private ConcurrentSkipListSet<String> suspiciousUsers = new ConcurrentSkipListSet<>();

    // Cache for holding pending transactions to handle out-of-order events
    private BlockingQueue<Transaction> pendingTransactions = new LinkedBlockingQueue<>();

    // Assume you have all transactions stored in this list
    private List<Transaction> allTransactions = new ArrayList<>();

    // Set to keep track of users for whom alerts have been generated
    private Set<String> alertedUsers = new HashSet<>();

    // Executor service for processing transactions asynchronously
    private ExecutorService transactionExecutor = Executors.newFixedThreadPool(10);

    // Method to process a single transaction in real-time
    public void processTransaction(Transaction transaction) {
        transactionExecutor.submit(() -> {
            try {
                // Add transaction to the pending queue for out-of-order handling
                pendingTransactions.put(transaction);

                // Process pending transactions in order
                while (!pendingTransactions.isEmpty()) {
                    Transaction pendingTransaction = pendingTransactions.take();
                    handleTransaction(pendingTransaction);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Transaction processing interrupted: " + e.getMessage());
            }
        });
    }

    // Method to handle a single transaction
    private void handleTransaction(Transaction transaction) {
        String userID = transaction.getUserID();
        String serviceID = transaction.getServiceID();
        double amount = transaction.getAmount();
        long timestamp = transaction.getTimestamp();

        // Update service count for the user and service combination
        String userAndServiceKey = userID + "_" + serviceID;
        serviceCountMap.merge(userAndServiceKey, 1, Integer::sum);

        // Update total transaction amount for the user
        userTotalAmountMap.merge(userID, amount, Double::sum);

        // Check for suspicious behavior: more than 3 distinct services within 5 minutes
        if (isMoreThanMaxServicesPerUser(userID) &&
                timestamp - userLastTransactionTimeMap.getOrDefault(userID, 0L) <= 5 * 60 * 1000) {
            suspiciousUsers.add(userID);
        }

        // Check for suspicious behavior: transactions 5x above average amount in the last 24 hours
        if (isTransactionsAboveAverageAmount(transaction)) {
            suspiciousUsers.add(userID);
        }

        // Check for suspicious behavior: 'ping-pong' activity within 10 minutes
        if (isPingPongActivity(transaction)) {
            suspiciousUsers.add(userID);
        }

        // Update last transaction time for the user
        userLastTransactionTimeMap.put(userID, timestamp);

        // Generate alerts for suspicious users (in real-time)
        generateAlert(userID);
    }

    // Method to check if a user is conducting transactions in more than 3 distinct services within a 5-minute window
    private boolean isMoreThanMaxServicesPerUser(String userID) {
        return serviceCountMap.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userID + "_"))
                .count() > 3;
    }

    // Method to check if a transaction's amount is above 5x the user's average amount in the last 24 hours
    private boolean isTransactionsAboveAverageAmount(Transaction transaction) {
        String userID = transaction.getUserID();
        double amount = transaction.getAmount();

        // Retrieve recent transactions for the user from the cache or load from the main transaction list
        List<Transaction> recentTransactions = getAllTransactionsForUser(userID);

        if (recentTransactions.isEmpty()) return false; // No recent transactions for the user

        double totalAmount = recentTransactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
        double averageAmount = totalAmount / recentTransactions.size();
        return amount > 5 * averageAmount;
    }

    // Method to retrieve all transactions for a user
    private List<Transaction> getAllTransactionsForUser(String userID) {
        long twentyFourHoursAgo = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
        return allTransactions.stream()
                .filter(t -> t.getUserID().equals(userID) && t.getTimestamp() >= twentyFourHoursAgo)
                .collect(Collectors.toList());
    }

    // Method to check if a sequence of transactions indicates 'ping-pong' activity within 10 minutes
    private boolean isPingPongActivity(Transaction transaction) {
        String userID = transaction.getUserID();
        long timestamp = transaction.getTimestamp();

        if (userLastTransactionTimeMap.containsKey(userID)) {
            long lastTransactionTime = userLastTransactionTimeMap.get(userID);
            if (timestamp - lastTransactionTime <= 10 * 60 * 1000) { // within 10 minutes
                return true; // Suspicious 'ping-pong' activity detected
            }
        }
        return false;
    }

    // Method to generate an alert for suspicious users
    private void generateAlert(String userID) {
        if (suspiciousUsers.contains(userID) && !alertedUsers.contains(userID)) {
            alertedUsers.add(userID); // Add the user to the set of alerted users
            System.out.println("Suspicious behavior detected for User " + userID);
            // Additional actions to be taken for alerting or further processing
        }
    }


    // Method to stop the transaction processing executor
    public void stopTransactionExecutor() {
        transactionExecutor.shutdown();
        try {
            if (!transactionExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                transactionExecutor.shutdownNow();
                if (!transactionExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Transaction processing executor did not terminate");
                }
            }
        } catch (InterruptedException e) {
            transactionExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}