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

import controller.BookEditDeleteController;
import controller.BookRegisterController;
import controller.BookSearchController;
import controller.TsundokuPeriodController;
import storage.BookRepository;

public class MainView extends JFrame {
    private static final int WINDOW_WIDTH = 420;
    private static final int WINDOW_HEIGHT = 520;
    private static final int BUTTON_WIDTH = 260;
    private static final int BUTTON_HEIGHT = 42;

    private BookRegisterController registerController;
    private BookSearchController searchController;
    private BookEditDeleteController editDeleteController;
    private BookRepository repository;
    private TsundokuPeriodController tsundokuPeriodController;

    public MainView(
            BookRegisterController registerController,
            BookSearchController searchController,
            BookEditDeleteController editDeleteController,
            BookRepository repository,
            TsundokuPeriodController tsundokuPeriodController) {
        this.registerController = registerController;
        this.searchController = searchController;
        this.editDeleteController = editDeleteController;
        this.repository = repository;
        this.tsundokuPeriodController = tsundokuPeriodController;
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
        buttonPanel.add(createSearchButton());
        buttonPanel.add(createListButton());
        buttonPanel.add(createEditDeleteButton());
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
            BookRegisterView registerView = new BookRegisterView(registerController);
            registerView.setVisible(true);
        });
        return button;
    }

    // 書籍検索画面を開くボタンを作成する。
    private JButton createSearchButton() {
        JButton button = createBaseButton("書籍を検索");
        button.addActionListener(e -> {
            BookSearchView searchView = new BookSearchView(searchController);
            searchView.setVisible(true);
        });
        return button;
    }

    // 書籍一覧画面を開くボタンを作成する。
    private JButton createListButton() {
        JButton button = createBaseButton("書籍一覧を表示");
        button.addActionListener(e -> {
            BookListView bookListView =
                    new BookListView(repository, tsundokuPeriodController);
            bookListView.setVisible(true);
        });
        return button;
    }

    // 書籍情報の編集・削除画面を開くボタンを作成する。
    private JButton createEditDeleteButton() {
        JButton button = createBaseButton("書籍情報を編集・削除");
        button.addActionListener(e -> {
            BookEditDeleteView view =
                    new BookEditDeleteView(editDeleteController, repository);
            view.setVisible(true);
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
