package UnusedAssets;

import java.util.Set;

import com.collections.DependencyTree;
import com.collections.DependencyTree.Pos;

/**
 * Extension of BiMap abstract class to bypass generic typInteger restrictions
 * 
 * @author viney
 *
 * @param <K>
 * @param <V>
 */
public class PosMap extends BiMap {
	
	public PosMap() {
		super();
	}
	
	public Pos put(Integer key, Pos value) {
		return (Pos)super.putKeyValue(key, value);
	}
	
	public Integer put(Pos key, Integer value) {
		return (Integer)(super.putValueKey(value, key));
	}
	
	public Pos get(Integer key) {
		return (Pos)super.getValue(key);
	}
	
	@SuppressWarnings("unchecked")
	public Integer get(Pos value) {
		return (Integer)super.getKey(value);
	}
	
	@SuppressWarnings("unchecked")
	public Set<Integer> keySet() {
		return super.keySet();
	}
	
	@SuppressWarnings("unchecked")
	public Set<Pos> valueSet() {
		return super.valueSet();
	}
	
}
