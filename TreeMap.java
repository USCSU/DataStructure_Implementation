package Google2015;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Created by Trace_route on 3/4/15.
 */
public class TreeMap<K,V> {
    transient Entry<K,V> root;
    transient int size;
    transient Comparator<K> comparator;

    public TreeMap(){
        comparator = null;
    }
    public TreeMap(Comparator<K> comparator){
        this.comparator = comparator;
    }
    public int getHeight(Entry<K,V> node){
        return node == null ? -1:node.height;
    }
  class Entry<K,V>{
      private K key;
      private V value;

      Entry<K,V> left;
      Entry<K,V> right;
      int height;
      public Entry(K key, V value){
          this.key = key;
          this.value = value;
      }
      public boolean equals(Object o){
          Entry<K,V> e = (Entry<K,V>)o;
          return e.getKey().equals(key)&&e.getValue().equals(value);
      }
      public int hashCode(){
          int keyHash = key==null?0:key.hashCode();
          int valueHash = value==null?0:value.hashCode();
          return keyHash^valueHash;
      }
      public K getKey(){
          return key;
      }
      public void setKey(K key){
          this.key = key;
      }
      public V getValue(){
          return this.value;
      }
      public void setValue(V value){
          this.value = value;
      }
  }
    private Entry<K,V> leftLeft(Entry<K,V> cur){
        Entry<K,V> node = cur.left;
        cur.left = node.right;
        node.right = cur;
        cur.height = Math.max(getHeight(cur.left),getHeight(cur.right))+1;
        node.height = Math.max(getHeight(node.left),cur.height)+1;
        return node;
    }
    private Entry<K,V> leftRight(Entry<K,V> cur){
        cur.left = rightRight(cur.left);
        return leftLeft(cur);
    }
    private Entry<K,V> rightRight(Entry<K,V> cur){
        Entry<K,V> node = cur.right;
        cur.right = node.left;
        node.left = cur;
        cur.height = Math.max(getHeight(cur.left),getHeight(cur.right))+1;
        node.height = Math.max(getHeight(node.right),cur.height)+1;
        return node;
    }
    private Entry<K,V> rightLeft(Entry<K,V> cur){
        cur.right = leftLeft(cur.right);
        return rightRight(cur);
    }
    private Entry<K,V> balance(Entry<K,V> node){
        if(node == null) return node;
        if(getHeight(node.left) - getHeight(node.right) > 1){
            if(getHeight(node.left.left) > getHeight(node.left.right)){ //left -left
                node = leftLeft(node);
            }else{ //left - right
                node = leftRight(node);
            }
        }else if(getHeight(node.right) - getHeight(node.left)> 1){
            if(getHeight(node.right.left) > getHeight(node.right.right)){ // right-left
                node = rightLeft(node);
            }else{ //right - right
                node = rightRight(node);
            }
        }
        node.height = Math.max(getHeight(node.left),getHeight(node.right))+1;
        return node;
    }
    private Entry<K,V> insert(K key, V value, Entry<K,V> node){
        if(node == null) return new Entry<K, V>(key,value);
        int compare = 0;
        if(comparator == null){ // default comparatable sorting --- no comparator
            Comparable<K> k = (Comparable<K>) key;
             compare = k.compareTo(node.getKey());
        }else
             compare = comparator.compare(key,node.getKey());

        if(compare < 0){
            node.left = insert(key,value,node.left);
        }else if(compare>0){
            node.right = insert(key,value,node.right);
        }else{
            node.setValue(value);
        }
        return balance(node);
    }
    public void put(K key, V value){
        if(!containsKey(key)) size++;
        root = insert(key,value,root);
    }

    private Entry<K,V> getFirstEntry(Entry<K,V> cur){
        if(cur.left == null) return cur;
        return getFirstEntry(cur.left);

    }
    private Entry<K,V> getLastEntry(Entry<K,V> cur){
        if(cur.right == null) return cur;
        return getLastEntry(cur.right);
    }
    private boolean contains(K key, Entry<K,V> cur){
        if(cur == null) return false;
        int compare = 0;
        if(comparator == null){
            Comparable<K> k = (Comparable<K>)key;
             compare = k.compareTo(cur.getKey());
        }else{
            compare = comparator.compare(key,cur.getKey());
        }
        if(compare> 0){
            return contains(key, cur.right);
        }else if(compare <0){
            return contains(key, cur.left);
        }else
            return true;
    }
    private V get(K key, Entry<K,V> cur){
        if(cur == null) return null;
        int compare = 0;
        if(comparator == null){
            Comparable<K> k = (Comparable<K>) key;
            compare = k.compareTo(cur.getKey());
        }else{
            compare=comparator.compare(key,cur.getKey());
        }
        if(compare  > 0){
            return get(key,cur.right);
        }else if(compare < 0){
            return get(key,cur.left);
        }else
            return cur.getValue();
    }
    public V get(K key){
        return get(key,root);
    }
    public boolean containsKey(K key){
        return contains(key,root);
    }
    private Entry<K,V> remove(K key, Entry<K,V> cur){
        if(cur == null) return null;
        int compare = 0;
        if(comparator == null){
            compare = ((Comparable<K>)(key)).compareTo(cur.getKey());
        }else
            compare = comparator.compare(key,cur.getKey());

        if(compare > 0){
            cur.right = remove(key,cur.right);
        }else if(compare < 0){
            cur.left = remove(key,cur.left);
        }else{
            if(cur.left!=null && cur.right!=null){
                Entry<K,V> min = getFirstEntry(cur.right);
                cur.setValue(min.getValue());
                cur.setKey(min.getKey());
                cur.right = remove(min.getKey(),cur.right);
            }else{
                cur = cur.left==null?cur.right:cur.left;
            }
        }
        return cur;
    }
    public void remove(K key){
        if(!containsKey(key)) size--;
        root = remove(key,root);
    }
    public LinkedList<String> traversePreOrder(Entry<K,V> root){
        LinkedList<String> ret = new LinkedList<String>();
        if(root == null) return ret;
        ret.add(root.key + " --> " + root.value);
        ret.addAll(traversePreOrder(root.left));
        ret.addAll(traversePreOrder(root.right));
        return ret;
    }
    public LinkedList<String> traverseInOrder(Entry<K,V> root){
        LinkedList<String> ret = new LinkedList<String>();
        if(root == null) return ret;
        ret.addAll(traverseInOrder(root.left));
        ret.add(root.key + " --> " + root.value);
        ret.addAll(traverseInOrder(root.right));
        return ret;
    }
    public static void main(String[] args){
//        int[] array =  {1,5,2,3,4,6,7,8,9,11};
//        TreeMapTest tree = new TreeMapTest();
//        int index = 1;
//
//        for(int i:array){
//            tree.put(i,index++);
//        }
//        System.out.println(tree.containsKey(21)?"21 is existed":"21 is not existed");
//        System.out.println(tree.containsKey(4)?"4 is existed":"4 is not existed");
//        tree.remove(4);
//        System.out.println(tree.containsKey(4)?"4 is existed":"4 is not existed");
//        System.out.println(tree.traversePreOrder(tree.root));
//        System.out.println(tree.traverseInOrder(tree.root));
//        System.out.println(tree.get(3));
        Comparator<String> cmp = new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        };
         TreeMap<String, Integer> treemap = new TreeMap<String, Integer>(cmp);
        String[] strs = {"this","this","what's","wht","problem","this","this","this","is"};
        for(String str:strs) {
            if (treemap.containsKey(str)) {
                System.out.println(treemap.get(str));
                treemap.put(str, treemap.get(str) + 1);
            } else {
                treemap.put(str, 1);
            }
        }
        System.out.println(treemap.traversePreOrder(treemap.root));
        System.out.println(treemap.traverseInOrder(treemap.root));
        System.out.println(treemap.size);

    }
}
