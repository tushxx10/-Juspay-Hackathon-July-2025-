package JusPay;

import java.util.*;

    class TestClass1 {
        public static void main(String args[]) throws Exception {
            Scanner sc = new Scanner(System.in);
            int n = sc.nextInt();
            int m = sc.nextInt();
            int q = sc.nextInt();

            LockingTree1 tree = new LockingTree1(n, m, sc);

            for (int i = 0; i < q; i++) {
                int type = sc.nextInt();
                String name = sc.next();
                int uid = sc.nextInt();
                System.out.println(tree.operate(type, name, uid));
            }
            sc.close();
        }
    }

    class LockingTree1 {
        int n, m;
        String[] names;
        Map<String, Integer> nameToIndex;
        int[] lockState;              // -1 = unlocked, else uid
        int[] parent;
        int[] lockedDescendants;      // Number of locked nodes in subtree
        List<List<Integer>> children;

        public LockingTree1(int n, int m, Scanner sc) {
            this.n = n;
            this.m = m;

            names = new String[n];
            nameToIndex = new HashMap<>();
            lockState = new int[n];
            parent = new int[n];
            lockedDescendants = new int[n];
            children = new ArrayList<>();

            Arrays.fill(lockState, -1);

            for (int i = 0; i < n; i++) {
                children.add(new ArrayList<>());
            }

            // Read node names and build mappings
            for (int i = 0; i < n; i++) {
                names[i] = sc.next();
                nameToIndex.put(names[i], i);
            }

            // Build tree structure
            for (int i = 1; i < n; i++) {
                parent[i] = (i - 1) / m;
                children.get(parent[i]).add(i);
            }
            parent[0] = -1;
        }

        private boolean hasLockedAncestor(int idx) {
            while (parent[idx] != -1) {
                idx = parent[idx];
                if (lockState[idx] != -1) return true;
            }
            return false;
        }

        private boolean hasLockedDescendant(int idx) {
            return lockedDescendants[idx] > 0;
        }

        private void updateLockedDescendants(int idx, int delta) {
            while (idx != -1) {
                lockedDescendants[idx] += delta;
                idx = parent[idx];
            }
        }

        private boolean collectDescendantLocks(int idx, int uid, List<Integer> lockedList) {
            boolean found = false;
            for (int child : children.get(idx)) {
                boolean result = collectDescendantLocks(child, uid, lockedList);
                if (!result && lockedDescendants[child] > 0) return false;
                found |= result;
            }
            if (lockState[idx] != -1) {
                if (lockState[idx] != uid) return false;
                lockedList.add(idx);
                return true;
            }
            return found;
        }

        public String lock(String name, int uid) {
            int idx = nameToIndex.get(name);
            if (lockState[idx] != -1 || hasLockedAncestor(idx) || hasLockedDescendant(idx))
                return "false";
            lockState[idx] = uid;
            updateLockedDescendants(idx, 1);
            return "true";
        }

        public String unlock(String name, int uid) {
            int idx = nameToIndex.get(name);
            if (lockState[idx] == uid) {
                lockState[idx] = -1;
                updateLockedDescendants(idx, -1);
                return "true";
            }
            return "false";
        }

        public String upgrade(String name, int uid) {
            int idx = nameToIndex.get(name);
            if (lockState[idx] != -1 || hasLockedAncestor(idx)) return "false";

            List<Integer> lockedNodes = new ArrayList<>();
            if (!collectDescendantLocks(idx, uid, lockedNodes)) return "false";

            for (int node : lockedNodes) {
                lockState[node] = -1;
                updateLockedDescendants(node, -1);
            }

            lockState[idx] = uid;
            updateLockedDescendants(idx, 1);
            return "true";
        }
        public String operate(int type, String name, int uid) {
            switch (type) {
                case 1: return lock(name, uid);
                case 2: return unlock(name, uid);
                case 3: return upgrade(name, uid);
                default: return "false";
            }
        }
    }

