package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import controller.StatisticsController;
import domain.Book;
import storage.BookRepository;

public class StatisticsView extends JFrame {
    private StatisticsController controller;
    private BookRepository repository;

    private JLabel totalBooksValueLabel;
    private JLabel unreadValueLabel;
    private JLabel readingValueLabel;
    private JLabel finishedValueLabel;
    private JLabel totalPriceValueLabel;
    private JLabel averageRatingValueLabel;

    public StatisticsView(
            StatisticsController controller,
            BookRepository repository) {
        this.controller = controller;
        this.repository = repository;
        initializeFrame();
        initializeComponents();
        updateStatistics();
    }

    // ウィンドウの基本設定を行う。
    private void initializeFrame() {
        setTitle("統計情報");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 360);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    // タイトル、統計表示、操作ボタンを配置する。
    private void initializeComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 18));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        JLabel titleLabel = new JLabel("読書統計", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(createStatisticsPanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    // 統計項目の表示欄を作成する。
    private JPanel createStatisticsPanel() {
        JPanel statisticsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(6, 8, 6, 8);
        constraints.anchor = GridBagConstraints.WEST;

        totalBooksValueLabel = new JLabel();
        unreadValueLabel = new JLabel();
        readingValueLabel = new JLabel();
        finishedValueLabel = new JLabel();
        totalPriceValueLabel = new JLabel();
        averageRatingValueLabel = new JLabel();

        addStatisticsRow(statisticsPanel, constraints, 0, "登録書籍数", totalBooksValueLabel);
        addStatisticsRow(statisticsPanel, constraints, 1, "未読", unreadValueLabel);
        addStatisticsRow(statisticsPanel, constraints, 2, "読書中", readingValueLabel);
        addStatisticsRow(statisticsPanel, constraints, 3, "読了", finishedValueLabel);
        addStatisticsRow(statisticsPanel, constraints, 4, "書籍価格の合計", totalPriceValueLabel);
        addStatisticsRow(statisticsPanel, constraints, 5, "平均評価", averageRatingValueLabel);

        return statisticsPanel;
    }

    // 統計項目の1行を追加する。
    private void addStatisticsRow(JPanel panel, GridBagConstraints constraints, int row, String itemName, JLabel valueLabel) {
        JLabel itemLabel = new JLabel(itemName + "：");
        itemLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        valueLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));

        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.0;
        panel.add(itemLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = row;
        constraints.weightx = 1.0;
        panel.add(valueLabel, constraints);
    }

    // 画面下部の更新ボタンと戻るボタンを作成する。
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton updateButton = new JButton("更新");
        JButton backButton = new JButton("戻る");

        updateButton.addActionListener(e -> updateStatistics());
        backButton.addActionListener(e -> dispose());

        buttonPanel.add(updateButton);
        buttonPanel.add(backButton);

        return buttonPanel;
    }

    // 最新の書籍一覧から統計値を再計算して表示する。
    private void updateStatistics() {
        // StatisticsControllerは現状、統計値を設定せず空のReadingStatisticsを返す仮実装。
        controller.calculateStatistics();

        ArrayList<Book> books = repository.findAllBooks();
        int totalBooks = books.size();
        int unreadCount = 0;
        int readingCount = 0;
        int finishedCount = 0;
        int totalPrice = 0;
        int ratingTotal = 0;

        for (Book book : books) {
            String status = book.getStatus();

            if ("未読".equals(status)) {
                unreadCount++;
            } else if ("読書中".equals(status)) {
                readingCount++;
            } else if ("読了".equals(status)) {
                finishedCount++;
            }

            totalPrice += book.getPrice();
            ratingTotal += book.getRating();
        }

        double averageRating = 0.0;
        if (totalBooks > 0) {
            averageRating = (double) ratingTotal / totalBooks;
        }

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        totalBooksValueLabel.setText(totalBooks + "冊");
        unreadValueLabel.setText(unreadCount + "冊");
        readingValueLabel.setText(readingCount + "冊");
        finishedValueLabel.setText(finishedCount + "冊");
        totalPriceValueLabel.setText(numberFormat.format(totalPrice) + "円");
        averageRatingValueLabel.setText(String.format("%.1f", averageRating));
    }
}
