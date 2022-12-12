package cn.yuuha;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.File;
import java.util.Enumeration;
import java.util.Objects;

public class GUI {
    private static final Logger logger = LogManager.getLogger(GUI.class);

    public static void createAndShowGUI() {
        Font font = new Font("黑体", Font.PLAIN, 15);
        initGlobalFont(font);

        JFrame frame = new JFrame("战网账号切换器");
        frame.setLayout(null);
        frame.setSize(516, 239);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(null);

        JLabel label1 = new JLabel("战网启动器：", SwingConstants.CENTER);
        label1.setBounds(0, 10, 100, 30);
        panel.add(label1);

        JTextField textField1 = new JTextField();
        textField1.setEditable(false);
        textField1.setBounds(100, 10, 320, 30);
        if (Configs.switcherConfigs.getProperty("launcher") != null) {
            textField1.setText(Configs.switcherConfigs.getProperty("launcher"));
        }
        panel.add(textField1);

        JButton button1 = new JButton("选择");
        button1.setBounds(425, 10, 70, 30);
        button1.addActionListener(e -> {
            selectBNPath(frame, textField1);
        });
        panel.add(button1);

        JLabel label2 = new JLabel("已存账号：", SwingConstants.CENTER);
        label2.setBounds(0, 60, 100, 30);
        panel.add(label2);

        JComboBox<String> comboBox = new JComboBox<>(Configs.accounts.getAccounts().toArray(new String[0]));
        comboBox.setBounds(100, 60, 200, 30);
        panel.add(comboBox);

        JButton button2 = new JButton("新增");
        button2.setBounds(320, 60, 70, 30);
        button2.addActionListener(e -> {
            addNewAccount(frame, comboBox);
        });
        panel.add(button2);

        JButton button3 = new JButton("删除");
        button3.setBounds(410, 60, 70, 30);
        button3.addActionListener(e -> {
            deleteAccount(frame, comboBox);
        });
        panel.add(button3);

        JLabel label3 = new JLabel("登录地区：", SwingConstants.CENTER);
        label3.setBounds(0, 110, 100, 30);
        panel.add(label3);

        ButtonGroup buttonGroup = new ButtonGroup();
        JRadioButton radioButton1 = new JRadioButton("中国");
        radioButton1.setBounds(115, 110, 70, 30);
        radioButton1.setActionCommand("CN");
        JRadioButton radioButton2 = new JRadioButton("亚洲");
        radioButton2.setBounds(215, 110, 70, 30);
        radioButton2.setActionCommand("TW");
        JRadioButton radioButton3 = new JRadioButton("美洲");
        radioButton3.setBounds(315, 110, 70, 30);
        radioButton3.setActionCommand("US");
        JRadioButton radioButton4 = new JRadioButton("欧洲");
        radioButton4.setBounds(415, 110, 70, 30);
        radioButton4.setActionCommand("EU");
        if (Configs.switcherConfigs.getProperty("region") != null) {
            switch (Configs.switcherConfigs.getProperty("region")) {
                case "CN":
                    radioButton1.setSelected(true);
                    break;
                case "TW":
                    radioButton2.setSelected(true);
                    break;
                case "US":
                    radioButton3.setSelected(true);
                    break;
                case "EU":
                    radioButton4.setSelected(true);
                    break;
                default:
            }
        } else {
            radioButton1.setSelected(true);
        }
        buttonGroup.add(radioButton1);
        buttonGroup.add(radioButton2);
        buttonGroup.add(radioButton3);
        buttonGroup.add(radioButton4);
        panel.add(radioButton1);
        panel.add(radioButton2);
        panel.add(radioButton3);
        panel.add(radioButton4);

        JButton button4 = new JButton("登录");
        button4.setBounds(200, 160, 100, 30);
        button4.addActionListener(e -> {
            try {
                if (!textField1.getText().equals("")) {
                    if (comboBox.getSelectedItem() != null) {
                        Configs.accounts.setFirst((String) comboBox.getSelectedItem());
                        Configs.saveAccounts();
                        Configs.switcherConfigs.setProperty("region", buttonGroup.getSelection().getActionCommand());
                        Configs.saveProperties();
                        Runtime.getRuntime().exec("taskkill /f /t /im \"Battle.net.exe\"");
                        Runtime.getRuntime().exec("\"" + textField1.getText() + "\"" + " --setregion=" + buttonGroup.getSelection().getActionCommand());
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(
                                frame,
                                "请选择账号！",
                                "警告",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            frame,
                            "请选择暴雪战网启动器路径！",
                            "警告",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            } catch (Exception exception) {
                if (logger.isErrorEnabled()) {
                    logger.error(exception);
                }
            }
        });
        panel.add(button4);

        frame.setContentPane(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        if (!Configs.isConfigFileExists) {
            JOptionPane.showMessageDialog(
                    frame,
                    "请先启动一次暴雪战网",
                    "警告",
                    JOptionPane.WARNING_MESSAGE
            );
            frame.dispose();
        }
    }

    private static void deleteAccount(JFrame frame, JComboBox<String> comboBox) {
        if (!Objects.equals(comboBox.getSelectedItem(), null)) {
            int result = JOptionPane.showConfirmDialog(
                    frame,
                    "确认删除账号 " + comboBox.getSelectedItem() + " 吗？",
                    "提示",
                    JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.OK_OPTION) {
                Configs.accounts.remove((String) comboBox.getSelectedItem());
                comboBox.setModel(new DefaultComboBoxModel<>(Configs.accounts.getAccounts().toArray(new String[0])));
            }
        } else {
            JOptionPane.showMessageDialog(
                    frame,
                    "没有选中账号！",
                    "警告",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private static void addNewAccount(JFrame frame, JComboBox<String> comboBox) {
        JFrame addFrame = new JFrame("新增账号");
        addFrame.setLayout(null);
        addFrame.setSize(456, 89);
        addFrame.setResizable(false);
        addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(null);

        JLabel label = new JLabel("请输入账号：", SwingConstants.CENTER);
        label.setBounds(0, 10, 130, 30);
        panel.add(label);

        JTextField textField = new JTextField();
        textField.setBounds(130, 10, 200, 30);
        panel.add(textField);

        JButton button = new JButton("确定");
        button.setBounds(350, 10, 70, 30);
        button.addActionListener(e -> {
            Configs.accounts.addFirst(textField.getText());
            comboBox.setModel(new DefaultComboBoxModel<>(Configs.accounts.getAccounts().toArray(new String[0])));
            addFrame.dispose();
        });
        panel.add(button);

        addFrame.setContentPane(panel);
        addFrame.setLocationRelativeTo(frame);
        addFrame.setVisible(true);
    }

    private static void selectBNPath(JFrame frame, JTextField textField) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("C:\\"));
        fileChooser.changeToParentDirectory();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                return f.getName().equals("Battle.net Launcher.exe");
            }

            @Override
            public String getDescription() {
                return "暴雪战网启动器(Battle.net Launcher.exe)";
            }
        });
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            textField.setText(file.getAbsolutePath());
            Configs.switcherConfigs.setProperty("launcher", file.getAbsolutePath());
        }
    }

    private static void initGlobalFont(Font font) {
        FontUIResource fontRes = new FontUIResource(font);
        for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }
}
