# Event Streaming with Kafka

## Why Kafka?

Kafka solves the problem of reliable communication between services.

Without Kafka:

```text
Order Service
    ├── Shipment Service
    ├── Notification Service
    └── Analytics Service
```

Each service directly calls other services, making the system tightly coupled.

With Kafka:

```text
Order Service
      ↓
    Kafka
   ↙  ↓  ↘
Shipment Notification Analytics
```

Services communicate through events and remain independent.

---

# Core Components

## Broker

A Kafka server.

Responsibilities:

* Stores messages
* Hosts partitions
* Serves producers
* Serves consumers

Example:

```text
Broker 1
```

Local Kafka running on:

```text
localhost:9092
```

is typically a single broker.

---

## Cluster

A group of brokers.

Example:

```text
Broker 1
Broker 2
Broker 3
```

Even a single broker is technically a cluster of size 1.

---

## Topic

A named stream of events.

Example:

```text
orders
shipment-events
payment-events
```

Producer writes to a topic.

Consumer reads from a topic.

---

## Partition

A topic can be divided into partitions.

Example:

```text
orders

P0
P1
P2
```

Why partitions?

* Parallel processing
* Scalability
* Higher throughput

---

# Producer

Produces (publishes) messages to a topic.

Example:

```text
OrderCreated
```

---

## How Producer Chooses a Partition

### Without Key

Kafka distributes messages across partitions.

```text
order1 -> P0
order2 -> P1
order3 -> P2
```

---

### With Key

Example:

```text
key = orderId
```

Kafka calculates:

```text
hash(key) % numberOfPartitions
```

Result:

```text
Order 101 -> P1
Order 101 -> P1
Order 101 -> P1
```

All events for the same key go to the same partition.

This preserves ordering.

---

# Offsets

Every partition maintains its own offsets.

Example:

```text
P0

Offset 0 -> Event A
Offset 1 -> Event B
```

Offsets are partition-specific.

There is no global offset.

---

# Ordering

Kafka guarantees ordering only within a partition.

Example:

```text
P0

Created
Paid
Shipped
```

Consumer will always see:

```text
Created
Paid
Shipped
```

Kafka does NOT guarantee ordering across partitions.

---

# Consumer

Consumes messages from topics.

Consumer continuously polls Kafka.

Example:

```text
orders topic
      ↓
Shipment Service
```

---

# Consumer Group

A set of consumers working together.

Example:

```text
shipment-group

Consumer A
Consumer B
```

Kafka distributes partitions among consumers.

---

## Example

Topic:

```text
P0
P1
P2
```

Assignment:

```text
Consumer A -> P0, P1
Consumer B -> P2
```

Consumer A does NOT receive messages from P2.

Consumer B does NOT receive messages from P0 and P1.

Together they process all messages.

---

# Why Consumer Groups?

## Parallel Processing

```text
More Partitions
       ↓
More Consumers
       ↓
Higher Throughput
```

---

## Avoid Duplicate Processing

Without consumer groups:

```text
OrderCreated
     ↓
Consumer A
Consumer B
Consumer C
```

Order processed 3 times.

With consumer groups:

```text
OrderCreated
     ↓
Only one consumer processes it
```

---

# Consumer Offsets

Kafka stores committed offsets.

Internal topic:

```text
__consumer_offsets
```

Example:

```text
shipment-group

P0 -> 120
P1 -> 95
```

Meaning:

```text
Next read starts from those offsets.
```

---

# Rebalancing

Occurs when:

* Consumer joins
* Consumer leaves
* Consumer crashes

Example:

Before:

```text
Consumer A -> P0 P1 P2
```

After Consumer B joins:

```text
Consumer A -> P0 P1
Consumer B -> P2
```

Kafka automatically redistributes partitions.

---

# Replication

Used for fault tolerance.

Replication Factor:

```text
3
```

Example:

```text
P0

Leader   -> Broker 1
Follower -> Broker 2
Follower -> Broker 3
```

---

## Leader

Handles:

* Producer writes
* Consumer reads

---

## Followers

Responsibilities:

* Copy data from leader
* Become leader if leader broker fails

---

# Partitions vs Replication

Partitions:

```text
Scalability
```

Replication:

```text
Fault Tolerance
```

They solve different problems.

---

# ZooKeeper (Old Kafka)

Before KRaft:

```text
Kafka Cluster
      +
ZooKeeper Cluster
```

ZooKeeper managed:

* Broker metadata
* Broker health
* Leader election

---

# KRaft (Modern Kafka)

ZooKeeper removed.

Kafka manages metadata internally.

Think:

```text
Kafka = Messaging + Coordination
```

instead of:

```text
Kafka = Messaging
ZooKeeper = Coordination
```

---

# Spring Boot Integration

Dependency:

```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

---

## Producer

```java
KafkaTemplate
```

Used to publish messages.

---

## Consumer

```java
@KafkaListener
```

Used to consume messages.

---

# Important Points

1. Topic is divided into partitions.
2. Ordering is guaranteed only within a partition.
3. Same key always goes to the same partition.
4. Maximum consumer parallelism = number of partitions.
5. Consumer groups prevent duplicate processing.
6. Replication provides fault tolerance.
7. Leader handles reads/writes; followers replicate.
8. Kafka stores consumer offsets.
9. Rebalancing happens when consumers join/leave.
10. Modern Kafka uses KRaft instead of ZooKeeper.

