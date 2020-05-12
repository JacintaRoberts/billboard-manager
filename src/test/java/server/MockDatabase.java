package server;

import java.util.*;

public class MockDatabase<K, V> extends AbstractMap<K, ArrayList<V>> implements Iterable<V> {

    private HashMap<K, ArrayList<V>> internal = new HashMap<K, ArrayList<V>>();

    public class MapListIterator<V> implements Iterator<V>
    {
        int key = 0;
        int value = 0;
        ArrayList<ArrayList<V>> keys;
        ArrayList<V> currentValues;
        Iterator<V> currentValuesIterator;

        public MapListIterator()
        {
            ArrayList a = new ArrayList(internal.values());
            Collections.sort(a, (ArrayList<V> z, ArrayList<V> x) -> x.size() - z.size());
            keys = a;
            currentValues = keys.get(key);
            currentValuesIterator = currentValues.iterator();
        }

        @Override
        public boolean hasNext() {
            return key < keys.size() - 1  || currentValuesIterator.hasNext();
        }

        @Override
        public V next() {
            if(!currentValuesIterator.hasNext())
            {
                ArrayList<V> newValuesSet = keys.get(++key);
                currentValuesIterator = newValuesSet.iterator();
            }
            return currentValuesIterator.next();
        }
    }

    public Iterator<V> iterator() {
        return new MapListIterator<V>();
    }

    public String addValue(K key, V value) {
        String dbResponse = "Fail: Username Already Taken";
        if (!containsKey(key)) {
            internal.put(key, new ArrayList<>());
            dbResponse = "Pass: User Created";
        }
        get(key).add(value);
        return dbResponse;
    }

    @Override
    public Set<Entry<K, ArrayList<V>>> entrySet() {
        return internal.entrySet();
    }

    // Method to print out all elements in the DatabaseMock Object
    public void showAll() {
        for (K name: internal.keySet()){
            String key = name.toString();
            String value = internal.get(name).toString();
            System.out.println(key + " " + value);
        }
    }
}