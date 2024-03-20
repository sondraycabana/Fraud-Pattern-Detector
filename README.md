# Fraud-Pattern-Detector
An application that detects fraud pattern 


## Documentation on the setup and how to run the prototype

-> Clone the project fron GitHub and Cd into the folder
-> The data used in the sytem is in-memory queues, in the TransactionGenerator class
-> Run the application
-> My java version: "17.0.7"



## Description of the algorithm

The fraud detection algorithm implemented in this project focuses on identifying suspicious behavior in real-time transactions. 
It employs several key components to address the constraints and efficiently process out-of-order events:
	Concurrency and Asynchronous Processing:
 		The system utilizes concurrent data structures and an executor service to handle multiple transactions concurrently. 
		As transactions arrive, they are processed asynchronously, allowing the system to handle a large volume of transactions efficiently.
	Transaction Tracking:
 		The system maintains concurrent data structures like ConcurrentHashMap and ConcurrentSkipListSet to track various metrics per user and service,
		 such as service counts, total transaction amounts, last transaction times, and a set of suspicious users.
	Pending Transactions Queue: 
		To handle out-of-order events, the system employs a BlockingQueue named  pendingTransactions . As transactions arrive, 
		they are added to this queue, ensuring that they are processed in the order they were received. This approach prevents 
		race conditions and ensures consistency in transaction processing.
	Real-time Alert Generation: 
		Suspicious behavior is detected in real-time based on predefined criteria such as conducting transactions in more than 3 distinct services within 5 minutes, 
		transactions 5 times above the average amount in the last 24 hours, and 'ping-pong' activity within 10 minutes. Alerts are generated immediately for suspicious users,
		providing timely detection and response to potential fraudulent activities.
	In conclusion, the algorithm efficiently addresses the constraints by leveraging concurrency, asynchronous processing, a pending transactions queue for out-of-order event handling, 
		and real-time detection criteria to detect and respond to suspicious behavior effectively in a high-throughput transaction environment. 


## Data set

[
    { "timestamp": 1617906000, "amount": 150.00, "userID": "user1", "serviceID": "serviceA" },
    { "timestamp": 1617906060, "amount": 4500.00, "userID": "user2", "serviceID": "serviceB" },
    { "timestamp": 1617906120, "amount": 75.00, "userID": "user1", "serviceID": "serviceC" },
    { "timestamp": 1617906180, "amount": 3000.00, "userID": "user3", "serviceID": "serviceA" },
    { "timestamp": 1617906240, "amount": 200.00, "userID": "user1", "serviceID": "serviceB" },
    { "timestamp": 1617906300, "amount": 4800.00, "userID": "user2", "serviceID": "serviceC" },
    { "timestamp": 1617906360, "amount": 100.00, "userID": "user4", "serviceID": "serviceA" },
    { "timestamp": 1617906420, "amount": 4900.00, "userID": "user3", "serviceID": "serviceB" },
    { "timestamp": 1617906480, "amount": 120.00, "userID": "user1", "serviceID": "serviceD" },
    { "timestamp": 1617906540, "amount": 5000.00, "userID": "user3", "serviceID": "serviceC" },
    { "timestamp": 1617906000, "amount": 150.00, "userID": "user5", "serviceID": "serviceA" },
    { "timestamp": 1617906060, "amount": 4500.00, "userID": "user6", "serviceID": "serviceB" },
    { "timestamp": 1617906120, "amount": 75.00, "userID": "user5", "serviceID": "serviceC" },
    { "timestamp": 1617906180, "amount": 3000.00, "userID": "user7", "serviceID": "serviceA" },
    { "timestamp": 1617906240, "amount": 200.00, "userID": "user5", "serviceID": "serviceB" },
    { "timestamp": 1617906300, "amount": 4800.00, "userID": "user6", "serviceID": "serviceC" },
    { "timestamp": 1617906360, "amount": 100.00, "userID": "user8", "serviceID": "serviceA" },
    { "timestamp": 1617906420, "amount": 4900.00, "userID": "user7", "serviceID": "serviceB" },
    { "timestamp": 1617906480, "amount": 120.00, "userID": "user5", "serviceID": "serviceD" },
    { "timestamp": 1617906540, "amount": 5000.00, "userID": "user7", "serviceID": "serviceC" }
]


##  Result

Suspicious behavior detected for User user1
Suspicious behavior detected for User user2
Suspicious behavior detected for User user3
Suspicious behavior detected for User user5
Suspicious behavior detected for User user6
Suspicious behavior detected for User user7
