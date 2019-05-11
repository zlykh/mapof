package prosto.package;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

@SuppressWarnings("unchecked")
public class MapUtils {

    public static final BiFunction noopFn = (ex, cur) -> {
        throw new RuntimeException("existing mapping " + ex.toString());
    };
    
    public static final BiFunction removeFn = (ex, cur) -> null;
    public static final BiFunction keepFn = (ex, cur) -> ex;
    public static final BiFunction replaceFn = (ex, cur) -> cur;

    public static void set(Object src, BiFunction remapFn, Object... path) {//Map<Object, Object>
        recursiveMerge(src, 0, mapof(), remapFn, path);
    }

    public static void set(Object src, Object val, BiFunction remapFn, Object... path) {//Map<Object, Object>
        recursiveMerge(src, 0, val, remapFn, path);
    }

    public static Object remove(Object src, Object... path) {//Map<Object, Object>
        return recursiveRemove(src, 0, path);
    }

    @SuppressWarnings("unchecked")
    private static Object recursiveRemove(Object submap, int idx, Object... path) {
        if (!asmap(submap).containsKey(path[idx])) {
            return null;
        }

        if (idx == path.length - 1) {
            return asmap(submap).remove(path[idx]);
        }

        return recursiveRemove(asmap(submap).get(path[idx]), idx + 1, path);
    }


    private static void recursiveMerge(Object submap, int idx, Object val, BiFunction remapFn, Object... path) {
        if (idx == path.length - 1) {
            asmap(submap).merge(path[idx], val, remapFn);
            return;
        }

        asmap(submap).putIfAbsent(path[idx], mapof());
        recursiveMerge(asmap(submap).get(path[idx]), idx + 1, val, remapFn, path);
    }

    public static Object search(Object map, Object... path) {
        return search(map, 0, path);
    }

    public static boolean exists(Object map, Object... path) {
        var result = search(map, 0, path);
        if (result != null) {
            if (result instanceof Map) {
                return !asmap(result).isEmpty();
            }
            if (result instanceof Set) {
                return !asset(result).isEmpty();
            }
            if (result instanceof List) {
                return !aslist(result).isEmpty();
            }

            return true;
        }

        return false;
    }

    public static Object searchcpy(Object map, Object... path) {
        return searchcpy(map, 0, path);
    }

    private static Object search(Object map, int idx, Object... path) {
        if (idx == path.length - 1) {
            if (map instanceof Map) {
                return asmap(map).get(path[idx]);
            }

            return map;
        }

        asmap(map).putIfAbsent(path[idx], mapof());
        return search(asmap(map).get(path[idx]), idx + 1, path);
    }

    private static Object searchcpy(Object map, int idx, Object... path) {
        if (idx == path.length - 1) {
            return mapcpy(asmap(map).get(path[idx]));
        }

        asmap(map).putIfAbsent(path[idx], mapof());
        return search(asmap(map).get(path[idx]), idx + 1, path);
    }

    public static Map asmap(Object obj) {
        return (Map) obj;
    }

    public static Set asset(Object obj) {
        return (Set) obj;
    }

    public static List aslist(Object obj) {
        return (List) obj;
    }

    public static Map<Object, Object> mapof(Object key, Object val, Object... kv) {
        if (kv == null || kv.length % 2 != 0) {
            throw new IllegalArgumentException("Supply even number of varargs");
        }

        Map<Object, Object> result = mapof(key, val);

        for (int i = 1; i < kv.length; i += 2) {
            Object k = kv[i - 1];
            Object v = kv[i];
            result.put(k, v);
        }

        return result;
    }

    public static Map<Object, Object> mapof(Object key, Object val) {
        return new ConcurrentHashMap<>() {{
            put(key, val);
        }};
    }

    public static Map<Object, Object> mapof() {
        return new ConcurrentHashMap<>();
    }

    public static HashSet<Object> setof() {
        return new HashSet<>();
    }

    public static ArrayList<Object> listof() {
        return new ArrayList<>();
    }

    public static Object mapcpy(Object from) {
        return new ConcurrentHashMap<>((Map) from);
    }

    public static Map<Object, Object> mapofUnmod(Object key, Object val, Object... kv) {
        if (kv == null || kv.length % 2 != 0) {
            throw new IllegalArgumentException("Supply even number of varargs");
        }

        Map<Object, Object> result = mapof(key, val);

        for (int i = 1; i < kv.length; i += 2) {
            Object k = kv[i - 1];
            Object v = kv[i];
            result.put(k, v);
        }

        return Collections.unmodifiableMap(result);
    }
}

