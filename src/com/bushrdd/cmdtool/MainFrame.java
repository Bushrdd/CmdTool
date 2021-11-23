package com.bushrdd.cmdtool;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("adb");
        setSize(500,350);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // setVisible(true);
    }

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();

        //ip连接
        JPanel connectView = new JPanel();
        connectView.setLayout(new FlowLayout(FlowLayout.LEFT));
        JTextField ip = new JTextField();
        ip.setPreferredSize(new Dimension(150, 25));
        JButton btnConnect = new JButton("连接");
        JButton btnScreen = new JButton("屏幕控制");
        connectView.add(ip);
        connectView.add(btnConnect);
        connectView.add(btnScreen);
        //快捷按钮
        JPanel ctrlView = new JPanel();
        ctrlView.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton btnInstall = new JButton("安装认证apk");
        JButton btnAuth = new JButton("网络认证");
        JButton btnSetting = new JButton("打开设置页面");
        JButton btnLauncher = new JButton("打开固件后台");
        ctrlView.add(btnInstall);
        ctrlView.add(btnAuth);
        ctrlView.add(btnSetting);
        ctrlView.add(btnLauncher);
        //自定义命令
        JPanel orderView = new JPanel();
        orderView.setLayout(new FlowLayout(FlowLayout.LEFT));
        JTextField order = new JTextField();
        order.setPreferredSize(new Dimension(400, 25));
        JButton btnExecute = new JButton("执行");
        orderView.add(order);
        orderView.add(btnExecute);
        //执行结果
        JTextArea textResult = new JTextArea();
        JScrollPane jsp=new JScrollPane(textResult);    //将文本域放入滚动窗口
        jsp.setPreferredSize(new Dimension(0, 200));

        Box mainBox = Box.createVerticalBox();
        mainBox.add(Box.createVerticalStrut(5));
        mainBox.add(connectView);
        mainBox.add(ctrlView);
        mainBox.add(orderView);
        mainBox.add(jsp);
        mainFrame.add(mainBox);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }
}
