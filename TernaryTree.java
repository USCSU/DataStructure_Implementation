package Google2015;
     class TriNode{
        int val;
        TriNode left;
        TriNode mid;
        TriNode right;
        public TriNode(int val){
            this.val = val;
        }
    }
public class TernaryTree{
    private TriNode root;
    TernaryTree(){
        root = null;
    }
    public TriNode getRoot(){
        return root;
    }
    public TriNode insert(int[] vals){
        TriNode root = getRoot();
        if(vals == null || vals.length ==0) return root;
        for(int val:vals){
            root = insert(val,root);
        }
        return root;
    }
    public TriNode insert(int val, TriNode node){
        if(node == null) {
            node = new TriNode(val);
            return node;
        }
        if(val < node.val){
            node.left = insert(val,node.left);
        }else if(val == node.val)
            node.mid = insert(val,node.mid);
        else
            node.right = insert(val,node.right);
        return node;
    }
    public void traverse(TriNode root){
        if(root == null) return;
        System.out.print(root.val +" --> ");
        traverse(root.left);
        traverse(root.mid);
        traverse(root.right);
    }
    public TriNode destroy(TriNode root){
        if(root == null) return root;
        root.left = destroy(root.left);
        root.mid = destroy(root.mid);
        root.right = destroy(root.right);
        root = null;
        return root;
    }
    public TriNode deleteNode(TriNode root, int val){
        if(root == null) return root;
        if(val < root.val){
            root.left = deleteNode(root.left,val);
        }else if(val > root.val){
            root.right = deleteNode(root.right,val);
        }else{ // val == root.val
            if(root.mid!=null){
                root.mid = null;
//                return deleteNode(root,val);
            }else {
                if(root.left == null){
                    root = root.right;
                }else{
                    TriNode temp = root.right;
                    root = root.left;
                    root.right = temp;
                }
            }
        }
        return root;
    }

    public static void main(String[] args) {
        int[] list = {5, 4, 9, 5,  2, 2,7}; //test case from document
//        int[] list = {1,1,2,2,3,3,4}; //test case from document
        System.out.println("Insert element into Tri-Tree:");
        TernaryTree tree = new TernaryTree();
        TriNode root =  tree.insert(list);
        tree.traverse(root);
//        root = tree.destroy(root);
//        System.out.println("\nAfter destroy:");
        int[] list1 = {2,7,2,9,4,7,9,5,5};
        for(int item:list1){
            root = tree.deleteNode(root,item);
            System.out.println();
            tree.traverse(root);
        }
    }


}
