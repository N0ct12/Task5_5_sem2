import java.util.Objects;
import java.util.Stack;

public class Main {
    public static void main(String[] args) throws Exception {
        MyBinTree<Integer> tree = new MyBinTree<>();
        tree.insertNode(6);
        tree.insertNode(8);
        tree.insertNode(5);
        tree.insertNode(8);
        tree.insertNode(2);
        tree.insertNode(9);
        tree.insertNode(7);
        tree.insertNode(4);
        tree.insertNode(10);
        tree.insertNode(3);
        tree.insertNode(1);
        tree.printTree();
        int max = -1;
        Stack<MyBinTree<Integer>.MyTreeNode> stack =  BinaryTreeAlgorithms.postOrderDelVisit(tree.getRoot(), (value, level) -> {
            System.out.println(value + " (уровень " + level + ")");
        });
        while (!stack.empty()){
            System.out.println(stack.pop().getValue());
            tree.deleteNode(stack.pop().getValue());
        }
    }

}