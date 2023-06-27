import util.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

import static java.awt.Frame.MAXIMIZED_BOTH;

public class Main {

    public static void delLeaves() throws Exception {
        SimpleBinaryTree<Integer> tree = new SimpleBinaryTree<>(Integer::parseInt);
        tree.fromBracketNotation("66 (33 (19 (, 28), 57), 87 (79 (73), 99 (89)))");
        BinaryTreeAlgorithms.deleteByPost(tree.getRoot(), 0 , tree);
        BinaryTreeAlgorithms.preOrderVisit(tree.getRoot(), (value, level) -> {
            System.out.println(value + " (уровень " + level + ")");
        });
    }

    public static void main(String[] args) throws Exception {
        Locale.setDefault(Locale.ROOT);

        //SwingUtils.setLookAndFeelByName("Windows");
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        //SwingUtils.setDefaultFont(null, 20);
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        SwingUtils.setDefaultFont("Arial", 20);

        EventQueue.invokeLater(() -> {
            try {
                JFrame frameMain = new TreeDemoFrame();
                frameMain.setVisible(true);
                frameMain.setExtendedState(MAXIMIZED_BOTH);
            } catch (Exception ex) {
                SwingUtils.showErrorMessageBox(ex);
            }
        });
//        delLeaves();
    }
}