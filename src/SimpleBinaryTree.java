import java.util.function.Function;

/**
 * Реализация простейшего бинарного дерева
 */
public class SimpleBinaryTree<T> implements BinaryTree<T>{

    protected class SimpleTreeNode implements BinaryTree.TreeNode<T>{
        public T value;
        public SimpleTreeNode left;
        public SimpleTreeNode right;

        public SimpleTreeNode(T value, SimpleTreeNode left, SimpleTreeNode right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }

        public SimpleTreeNode(T value) {
            this(value, null, null);
        }


        public T getValue() {
            return value;
        }


        public SimpleTreeNode getLeft() {
            return left;
        }

        public void setLeft(SimpleTreeNode node) {
            this.left = node;
        }

        public SimpleTreeNode getRight() {
            return right;
        }

        public void setRight(SimpleTreeNode node) {
            this.right = node;
        }
    }

    protected SimpleTreeNode root = null;

    protected Function<String, T> fromStrFunc;
    protected Function<T, String> toStrFunc;

    public SimpleBinaryTree(Function<String, T> fromStrFunc, Function<T, String> toStrFunc) {
        this.fromStrFunc = fromStrFunc;
        this.toStrFunc = toStrFunc;
    }

    public SimpleBinaryTree(Function<String, T> fromStrFunc) {
        this(fromStrFunc, Object::toString);
    }

    public SimpleBinaryTree() {
        this(null);
    }

    @Override
    public SimpleTreeNode getRoot() {
        return root;
    }

    public void clear() {
        root = null;
    }

    private static class IndexWrapper {

        public int index = 0;
    }

    public static <T> int height(BinaryTree.TreeNode<T> root) {
        if (root == null) {
            return 0;
        }
        return 1 + Math.max(height(root.getLeft()), height(root.getRight()));
    }

    public SimpleTreeNode findLeafByValue(T value) { // поиск узла по значению
        SimpleTreeNode currentNode = root; // начинаем поиск с корневого узла
        while (currentNode.getValue() != value && currentNode.getLeft()!=null && currentNode.getRight()!=null) { // поиск покуда не будет найден элемент или не будут перебраны все
            if ((int)value < (int)currentNode.getValue()) { // движение влево?
                currentNode = currentNode.getLeft();
            } else { //движение вправо
                currentNode = currentNode.getRight();
            }
            if (currentNode == null) { // если потомка нет,
                return null; // возвращаем null
            }
        }
        System.out.println(currentNode.getValue());
        return currentNode; // возвращаем найденный элемент
    }

//    public void deleteNode(BinaryTree.TreeNode<T> root)
//    {
//        BinaryTree.TreeNode<T> parent = getParent(root.getValue());
//        if (parent.getLeft().getValue() == root.getValue()){
//            parent.setLeft(null);
//        } else {
//            parent.setRight(null);
//        }
//    }


    @Override
    public void deleteNode(T val)
    {
        SimpleTreeNode root = findLeafByValue(val);
        SimpleTreeNode parent = getParent(root);
        if (parent.getLeft() == root){
            parent.setLeft(null);
        } else parent.setRight(null);
    }

    public SimpleTreeNode getParent(SimpleTreeNode p){
        return parentHelper(root,p);
    }
    private SimpleTreeNode parentHelper(SimpleTreeNode currentRoot, SimpleTreeNode p) {
        if (p==root || currentRoot==null){
            return null;
        }
        else{
            if(currentRoot.left==p || currentRoot.right==p)
                return currentRoot;
            else {
                if (Integer.parseInt(currentRoot.value.toString()) < Integer.parseInt(p.value.toString())) {
                    return parentHelper(currentRoot.right,p);
                }
                else {
                    return parentHelper(currentRoot.left,p);
                }
            }
        }
    }
    private T fromStr(String s) throws Exception {
        s = s.trim();
        if (s.length() > 0 && s.charAt(0) == '"') {
            s = s.substring(1);
        }
        if (s.length() > 0 && s.charAt(s.length() - 1) == '"') {
            s = s.substring(0, s.length() - 1);
        }
        if (fromStrFunc == null) {
            throw new Exception("Не определена функция конвертации строки в T");
        }
        return fromStrFunc.apply(s);
    }

    private void skipSpaces(String bracketStr, IndexWrapper iw) {
        while (iw.index < bracketStr.length() && Character.isWhitespace(bracketStr.charAt(iw.index))) {
            iw.index++;
        }
    }

    private T readValue(String bracketStr, IndexWrapper iw) throws Exception {
        // пропуcкаем возможные пробелы
        skipSpaces(bracketStr, iw);
        if (iw.index >= bracketStr.length()) {
            return null;
        }
        int from = iw.index;
        boolean quote = bracketStr.charAt(iw.index) == '"';
        if (quote) {
            iw.index++;
        }
        while (iw.index < bracketStr.length() && (
                    quote && bracketStr.charAt(iw.index) != '"' ||
                    !quote && !Character.isWhitespace(bracketStr.charAt(iw.index)) && "(),".indexOf(bracketStr.charAt(iw.index)) < 0
               )) {
            iw.index++;
        }
        if (quote && bracketStr.charAt(iw.index) == '"') {
            iw.index++;
        }
        String valueStr = bracketStr.substring(from, iw.index);
        T value = fromStr(valueStr);
        skipSpaces(bracketStr, iw);
        return value;
    }

    private SimpleTreeNode fromBracketStr(String bracketStr, IndexWrapper iw) throws Exception {
        T parentValue = readValue(bracketStr, iw);
        SimpleTreeNode parentNode = new SimpleTreeNode(parentValue);
        if (bracketStr.charAt(iw.index) == '(') {
            iw.index++;
            skipSpaces(bracketStr, iw);
            if (bracketStr.charAt(iw.index) != ',') {
                parentNode.left = fromBracketStr(bracketStr, iw);
                skipSpaces(bracketStr, iw);
            }
            if (bracketStr.charAt(iw.index) == ',') {
                iw.index++;
                skipSpaces(bracketStr, iw);
            }
            if (bracketStr.charAt(iw.index) != ')') {
                parentNode.right = fromBracketStr(bracketStr, iw);
                skipSpaces(bracketStr, iw);
            }
            if (bracketStr.charAt(iw.index) != ')') {
                throw new Exception(String.format("Ожидалось ')' [%d]", iw.index));
            }
            iw.index++;
        }

        return parentNode;
    }

    public void fromBracketNotation(String bracketStr) throws Exception {
        IndexWrapper iw = new IndexWrapper();
        SimpleTreeNode root = fromBracketStr(bracketStr, iw);
        if (iw.index < bracketStr.length()) {
            throw new Exception(String.format("Ожидался конец строки [%d]", iw.index));
        }
        this.root = root;
    }
}
