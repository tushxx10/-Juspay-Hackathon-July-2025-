# -Juspay-Hackathon-July-2025-

Tree of Space – Locking & Unlocking N-ary Tree

A concurrency-safe locking system on an M-ary tree structure, built as part of the Juspay Hackathon Challenge. It supports locking, unlocking, and upgrading locks on nodes, ensuring ancestor-descendant conflict checks and thread safety via ReentrantLock.

Problem Statement
Given a generic M-ary tree with N nodes, implement the following operations:

Lock(X, uid) – Locks node X for user uid if no ancestor/descendant is locked.

Unlock(X, uid) – Unlocks node X if it's currently locked by uid.

Upgrade(X, uid) – Promotes lock to an ancestor if all locked descendants are by the same user and no conflict exists.

Each operation must return true on success, else false.

Key Features
Lock/Unlock/Upgrade operations

Efficient ancestor and descendant tracking

Thread-safe using ReentrantLock

Designed for concurrent environments

Handles queries dynamically

Example Input & Output
Input Tree:

              World
           /         \
       Asia          Africa
       /   \        /       \
    China  India SouthAfrica  Egypy
 
Sample Queries:
1 China 9      → true
1 India 9      → true
3 Asia 9       → true
2 India 9      → false
2 Asia 9       → true

How It Works
Each node tracks:

Lock status

Locked user ID

Count of locked descendants

Parent and children references

On lock() and unlock():

Validates no locked ancestor or descendant (or same uid if unlocking)

Atomically updates lock state and propagation counts

On upgrade():

Ensures all locked descendants are by uid

Verifies no ancestor/descendant conflict
