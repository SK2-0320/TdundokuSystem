package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import controller.BookRegisterController;

public class MainView extends JFrame {
    private static final int WINDOW_WIDTH = 420;
    private static final int WINDOW_HEIGHT = 520;
    private static final int BUTTON_WIDTH = 260;
    private static final int BUTTON_HEIGHT = 42;

    private BookRegisterController registerController;

    public MainView(BookRegisterController registerController) {
        this.registerController = registerController;
        initializeFrame();
        initializeComponents();
    }

    // ウィンドウの基本設定を行う。
    private void initializeFrame() {
        setTitle("積読管理システム");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                confirmExit();
            }
        });
    }

    // メインメニューの部品を配置する。
    private void initializeComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 24));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(36, 40, 36, 40));

        JLabel titleLabel = new JLabel("積読管理システム", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(7, 1, 0, 12));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(createRegisterButton());
        buttonPanel.add(createMenuButton("書籍を検索", false));
        buttonPanel.add(createMenuButton("書籍一覧を表示", false));
        buttonPanel.add(createMenuButton("書籍情報を編集・削除", false));
        buttonPanel.add(createMenuButton("積読ステータスを変更", false));
        buttonPanel.add(createMenuButton("統計情報を表示", false));
        buttonPanel.add(createMenuButton("終了", true));

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    // 書籍登録画面を開くボタンを作成する。
    private JButton createRegisterButton() {
        JButton button = createBaseButton("書籍を登録");
        button.addActionListener(e -> {
            RegisterView registerView = new RegisterView(registerController);
            registerView.setVisible(true);
        });
        return button;
    }

    // メニュー用ボタンを作成し、仮処理または終了処理を設定する。
    private JButton createMenuButton(String text, boolean exitButton) {
        JButton button = createBaseButton(text);

        if (exitButton) {
            button.addActionListener(e -> confirmExit());
        } else {
            button.addActionListener(e -> showNotImplementedMessage());
        }

        return button;
    }

    // ボタンの共通設定を行う。
    private JButton createBaseButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.setFont(new Font("SansSerif", Font.PLAIN, 15));
        return button;
    }

    // 未実装機能の仮メッセージを表示する。
    private void showNotImplementedMessage() {
        JOptionPane.showMessageDialog(
                this,
                "この機能は今後実装します",
                "お知らせ",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // 終了前に確認ダイアログを表示する。
    private void confirmExit() {
        int result = JOptionPane.showConfirmDialog(
                this,
                "アプリケーションを終了しますか？",
                "終了確認",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
