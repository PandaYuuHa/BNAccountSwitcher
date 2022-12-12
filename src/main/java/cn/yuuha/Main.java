package cn.yuuha;

public class Main {
    public static void main(String[] args) {
        Configs.init();
        javax.swing.SwingUtilities.invokeLater(GUI::createAndShowGUI);
    }
}