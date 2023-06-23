import java.util.Stack;

public class MyBinTree<T> {
    private static class IndexWrapper {
        public int index = 0;
    }
    protected class MyTreeNode {
        public T value;
        public MyTreeNode left;
        public MyTreeNode right;

        public MyTreeNode(T value, MyTreeNode left, MyTreeNode right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }

        public MyTreeNode(T value) {
            this(value, null, null);
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public MyTreeNode getLeft(){
            return this.left;
        }

        public MyTreeNode getRight(){
            return this.right;
        }

        public void setLeft(MyTreeNode left){
            this.left = left;
        }

        public void setRight(MyTreeNode right){
            this.right = right;
        }
    }
    private MyTreeNode rootNode; // корневой узел

    public MyTreeNode getRoot() {
        return rootNode;
    }

    public void Tree() { // Пустое дерево
        rootNode = null;
    }

    public MyTreeNode findNodeByValue(T value) { // поиск узла по значению
        MyTreeNode currentNode = rootNode; // начинаем поиск с корневого узла
        while (currentNode.getValue() != value) { // поиск покуда не будет найден элемент или не будут перебраны все
            if ((int)value < (int)currentNode.getValue()) { // движение влево?
                currentNode = currentNode.getLeft();
            } else { //движение вправо
                currentNode = currentNode.getRight();
            }
            if (currentNode == null) { // если потомка нет,
                return null; // возвращаем null
            }
        }
        return currentNode; // возвращаем найденный элемент
    }

    public void insertNode(T value) { // метод вставки нового элемента
        MyTreeNode newNode = new MyTreeNode(value);
        if (rootNode == null) { // если корневой узел не существует
            rootNode = newNode;// то новый элемент и есть корневой узел
        }
        else { // корневой узел занят
            MyTreeNode currentNode = rootNode; // начинаем с корневого узла
            MyTreeNode parentNode;
            while (true) // мы имеем внутренний выход из цикла
            {
                parentNode = currentNode;
                if(value == currentNode.getValue()) {   // если такой элемент в дереве уже есть, не сохраняем его
                    return;    // просто выходим из метода
                }
                else  if ((int)value < (int)currentNode.getValue()) {   // движение влево?
                    currentNode = currentNode.getLeft();
                    if (currentNode == null){ // если был достигнут конец цепочки,
                        parentNode.setLeft(newNode); //  то вставить слева и выйти из методы
                        return;
                    }
                }
                else { // Или направо?
                    currentNode = currentNode.getRight();
                    if (currentNode == null) { // если был достигнут конец цепочки,
                        parentNode.setRight(newNode);  //то вставить справа
                        return; // и выйти
                    }
                }
            }
        }
    }

    public void deleteNode(T value) // Удаление узла с заданным ключом
    {
        MyTreeNode currentNode = rootNode;
        MyTreeNode parentNode = rootNode;
        boolean isLeftChild = true;
        while (currentNode.getValue() != value) { // начинаем поиск узла
            parentNode = currentNode;
            if ((int) value < (int) currentNode.getValue()) { // Определяем, нужно ли движение влево?
                isLeftChild = true;
                currentNode = currentNode.getLeft();
            } else { // или движение вправо?
                isLeftChild = false;
                currentNode = currentNode.getRight();
            }
//            if (currentNode == null)
//                //
//        }

            if (currentNode.getLeft() == null && currentNode.getRight() == null) { // узел просто удаляется, если не имеет потомков
                if (currentNode == rootNode) // если узел - корень, то дерево очищается
                    rootNode = null;
                else if (isLeftChild)
                    parentNode.setLeft(null); // если нет - узел отсоединяется, от родителя
                else
                    parentNode.setRight(null);
            } else if (currentNode.getRight() == null) { // узел заменяется левым поддеревом, если правого потомка нет
                if (currentNode == rootNode)
                    rootNode = currentNode.getLeft();
                else if (isLeftChild)
                    parentNode.setLeft(currentNode.getLeft());
                else
                    parentNode.setRight(currentNode.getLeft());
            } else if (currentNode.getLeft() == null) { // узел заменяется правым поддеревом, если левого потомка нет
                if (currentNode == rootNode)
                    rootNode = currentNode.getRight();
                else if (isLeftChild)
                    parentNode.setLeft(currentNode.getRight());
                else
                    parentNode.setRight(currentNode.getRight());
            } else { // если есть два потомка, узел заменяется преемником
                MyTreeNode heir = receiveHeir(currentNode);// поиск преемника для удаляемого узла
                if (currentNode == rootNode)
                    rootNode = heir;
                else if (isLeftChild)
                    parentNode.setLeft(heir);
                else
                    parentNode.setRight(heir);
            }
//        return true; // элемент успешно удалён
        }
    }

    private MyTreeNode receiveHeir(MyTreeNode node) {
        MyTreeNode parentNode = node;
        MyTreeNode heirNode = node;
        MyTreeNode currentNode = node.getRight(); // Переход к правому потомку
        while (currentNode != null) // Пока остаются левые потомки
        {
            parentNode = heirNode;// потомка задаём как текущий узел
            heirNode = currentNode;
            currentNode = currentNode.getLeft(); // переход к левому потомку
        }
        // Если преемник не является
        if (heirNode != node.getRight()) // правым потомком,
        { // создать связи между узлами
            parentNode.setLeft(heirNode.getRight());
            heirNode.setRight(node.getRight());
        }
        return heirNode;// возвращаем приемника
    }

    public void printTree() { // метод для вывода дерева в консоль
        Stack<MyTreeNode> globalStack = new Stack<MyTreeNode>(); // общий стек для значений дерева
        globalStack.push(rootNode);
        int gaps = 32; // начальное значение расстояния между элементами
        boolean isRowEmpty = false;
        String separator = "-----------------------------------------------------------------";
        System.out.println(separator);// черта для указания начала нового дерева
        while (!isRowEmpty) {
            Stack<MyTreeNode> localStack = new Stack<MyTreeNode>(); // локальный стек для задания потомков элемента
            isRowEmpty = true;
            for (int j = 0; j < gaps; j++)
                System.out.print(' ');
            while (!globalStack.isEmpty()) { // пока в общем стеке есть элементы
                MyTreeNode temp = globalStack.pop(); // берем следующий, при этом удаляя его из стека
                if (temp != null) {
                    System.out.print(temp.getValue()); // выводим его значение в консоли
                    localStack.push(temp.getLeft()); // сохраняем в локальный стек, наследники текущего элемента
                    localStack.push(temp.getRight());
                    if (temp.getLeft() != null ||
                            temp.getRight() != null)
                        isRowEmpty = false;
                }
                else {
                    System.out.print("__");// - если элемент пустой
                    localStack.push(null);
                    localStack.push(null);
                }
                for (int j = 0; j < gaps * 2 - 2; j++)
                    System.out.print(' ');
            }
            System.out.println();
            gaps /= 2;// при переходе на следующий уровень расстояние между элементами каждый раз уменьшается
            while (!localStack.isEmpty())
                globalStack.push(localStack.pop()); // перемещаем все элементы из локального стека в глобальный
        }
        System.out.println(separator);// подводим черту
    }
}
