package com.bushrdd.cmdtool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class MainFrame extends JFrame {

    public static MainFrame sMainFrame;

    public static JTextArea sTextResult;
    public static JTextField sTextIp;
    public static JTextField sTextOrder;

    public static SystemTray sTray = SystemTray.getSystemTray();


    public MainFrame() {
        setTitle("adb");
        setSize(500, 350);
        setResizable(false);
        // setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // setVisible(true);
    }

    public static void main(String[] args) {
        sMainFrame = new MainFrame();
        //ip连接
        JPanel connectView = new JPanel();
        connectView.setLayout(new FlowLayout(FlowLayout.LEFT));
        sTextIp = new JTextField();
        sTextIp.setPreferredSize(new Dimension(150, 25));
        JButton btnConnect = new JButton("连接");
        BtnClickListener btnConnectListener = new BtnClickListener("connect");
        btnConnect.addActionListener(btnConnectListener);
        JButton btnScreen = new JButton("屏幕控制");
        BtnClickListener btnScreenListener = new BtnClickListener("screen");
        btnScreen.addActionListener(btnScreenListener);
        connectView.add(sTextIp);
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
        sTextOrder = new JTextField();
        sTextOrder.setPreferredSize(new Dimension(400, 25));
        JButton btnExecute = new JButton("执行");
        BtnClickListener btnExecuteListener = new BtnClickListener("execute");
        btnExecute.addActionListener(btnExecuteListener);
        orderView.add(sTextOrder);
        orderView.add(btnExecute);
        //执行结果
        sTextResult = new JTextArea();
        JScrollPane jsp = new JScrollPane(sTextResult);    //将文本域放入滚动窗口
        jsp.setPreferredSize(new Dimension(0, 200));

        Box mainBox = Box.createVerticalBox();
        mainBox.add(Box.createVerticalStrut(5));
        mainBox.add(connectView);
        mainBox.add(ctrlView);
        mainBox.add(orderView);
        mainBox.add(jsp);
        sMainFrame.add(mainBox);
        sMainFrame.setLocationRelativeTo(null);
        sMainFrame.setVisible(true);
        sMainFrame.addWindowListener(new WindowAdapter() { // 窗口关闭事件
            public void windowClosing(WindowEvent e) {
                sMainFrame.setVisible(false);
                // miniTray();
            }
        });
        miniTray();
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
                    String ip = sTextIp.getText();
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
                    executeOrder(sTextOrder.getText(), false);
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
                    sTextResult.append(stringBuilder.toString() + '\n');
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    // 窗口最小化到任务栏托盘
    private static void miniTray() {
        try {
            // 托盘图标
            URL url = MainFrame.class.getResource("cat.png");
            ImageIcon trayImg = new ImageIcon(url);
            // 创建弹出菜单
            PopupMenu popupMenu = new PopupMenu();
            MenuItem exitItem = new MenuItem("退出");
            exitItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            popupMenu.add(exitItem);
            TrayIcon trayIcon = new TrayIcon(trayImg.getImage(), "cmdTool", popupMenu);
            trayIcon.setImageAutoSize(true);
            trayIcon.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    // 单击 1 双击 2
                    if (e.getButton() == 1 && e.getClickCount() == 1) {
                        sMainFrame.setVisible(true);
                        sMainFrame.setExtendedState(JFrame.NORMAL);
                        sMainFrame.toFront();
                    }
                }
            });
            sTray.add(trayIcon);
        } catch (AWTException e1) {
            e1.printStackTrace();
        }
    }
}
