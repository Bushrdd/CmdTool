package com.bushrdd.cmdtool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainFrame extends JFrame {

    public static JTextArea mTextResult;
    public static JTextField mTextIp;
    public static JTextField mTextOrder;

    public MainFrame() {
        setTitle("adb");
        setSize(500, 350);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // setVisible(true);
    }

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();

        //ip连接
        JPanel connectView = new JPanel();
        connectView.setLayout(new FlowLayout(FlowLayout.LEFT));
        mTextIp = new JTextField();
        mTextIp.setPreferredSize(new Dimension(150, 25));
        JButton btnConnect = new JButton("连接");
        BtnClickListener btnConnectListener = new BtnClickListener("connect");
        btnConnect.addActionListener(btnConnectListener);
        JButton btnScreen = new JButton("屏幕控制");
        BtnClickListener btnScreenListener = new BtnClickListener("screen");
        btnScreen.addActionListener(btnScreenListener);
        connectView.add(mTextIp);
        connectView.add(btnConnect);
        connectView.add(btnScreen);
        //快捷按钮
        JPanel ctrlView = new JPanel();
        ctrlView.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton btnInstall = new JButton("安装认证apk");
        BtnClickListener btnInstallListener = new BtnClickListener("install");
        btnInstall.addActionListener(btnInstallListener);
        JButton btnAuth = new JButton("网络认证");
        BtnClickListener btnAuthListener = new BtnClickListener("auth");
        btnAuth.addActionListener(btnAuthListener);
        JButton btnSetting = new JButton("打开设置页面");
        BtnClickListener btnSettingListener = new BtnClickListener("setting");
        btnSetting.addActionListener(btnSettingListener);
        JButton btnLauncher = new JButton("打开固件后台");
        BtnClickListener btnLauncherListener = new BtnClickListener("launcher");
        btnLauncher.addActionListener(btnLauncherListener);
        ctrlView.add(btnInstall);
        ctrlView.add(btnAuth);
        ctrlView.add(btnSetting);
        ctrlView.add(btnLauncher);
        //自定义命令
        JPanel orderView = new JPanel();
        orderView.setLayout(new FlowLayout(FlowLayout.LEFT));
        mTextOrder = new JTextField();
        mTextOrder.setPreferredSize(new Dimension(400, 25));
        JButton btnExecute = new JButton("执行");
        BtnClickListener btnExecuteListener = new BtnClickListener("execute");
        btnExecute.addActionListener(btnExecuteListener);
        orderView.add(mTextOrder);
        orderView.add(btnExecute);
        //执行结果
        mTextResult = new JTextArea();
        JScrollPane jsp = new JScrollPane(mTextResult);    //将文本域放入滚动窗口
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

    static class BtnClickListener implements ActionListener {
        private String id;

        public BtnClickListener(String id) {
            this.id = id;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (id) {
                case "connect":
                    executeOrder("adb disconnect", false);
                    String ip = mTextIp.getText();
                    if (ip.contains("192.168.")) {
                        executeOrder("adb connect " + ip, false);
                    } else {
                        executeOrder("adb connect 192.168." + ip, false);
                    }
                    break;
                case "screen":
                    executeOrder("cmd.exe /k start H:/工作文件/android/scrcpy.exe", true);
                    break;
                case "install":
                    executeOrder("cmd.exe /k start H:/Codes/bats/安装认证APK.bat", true);
                    break;
                case "auth":
                    executeOrder("cmd.exe /k start H:/Codes/bats/网络认证.bat", true);
                    break;
                case "setting":
                    executeOrder("cmd.exe /k start H:/Codes/bats/打开设置页面.bat", true);
                    break;
                case "launcher":
                    executeOrder("cmd.exe /k start H:/Codes/bats/打开固件后台.bat", true);
                    break;
                case "execute":
                    executeOrder(mTextOrder.getText(), false);
                    break;
            }
        }

        private void executeOrder(String order, boolean openWindow) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                Process process = Runtime.getRuntime().exec(order);
                if (!openWindow) {
                    InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
                    InputStreamReader errStreamReader = new InputStreamReader(process.getErrorStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    BufferedReader errReader = new BufferedReader(errStreamReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    while ((line = errReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    mTextResult.append(stringBuilder.toString() + '\n');
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
