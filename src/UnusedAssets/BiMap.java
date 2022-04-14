package UnusedAssets;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class BiMap {
	
	Map<Object, Object> top;
	Map<Object, Object> bot;
	
	public BiMap() {
		top = new HashMap<>();
		bot = new HashMap<>();
	}
	
	public int size() { return top.size(); }
	public boolean isEmpty() { return top.isEmpty(); }
	
	public Object getKey(Object value) { return bot.get(value); }
	public Object getValue(Object key) { return top.get(key); }
	
	public Object putKeyValue(Object key, Object value) {
		bot.put(value, key);
		return top.put(key, value);
	}
	public Object putValueKey(Object value, Object key) {
		top.put(key, value);
		return bot.put(value, key);
	}

	public Object remove(Object key) {
		return bot.remove(top.remove(key));
	}

	public void clear() {
		top.clear();
		bot.clear();
	}

	public Set<Object> keySet() {
		return top.keySet();
	}

	public Set<Object> valueSet() {
		return bot.keySet();
	}

	public Set<Entry<Object, Object>> entrySet() {
		return top.entrySet();
	}

	public boolean contains(Object key) {
		return top.containsKey(key) || bot.containsKey(key);
	}

}
