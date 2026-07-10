package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import controller.BookEditDeleteController;
import domain.Book;
import storage.BookRepository;

public class BookEditDeleteView extends JFrame {
    private static final String[] COLUMN_NAMES = {
        "ISBNコード",
        "書籍名",
        "著者名",
        "ステータス",
        "価格",
        "登録日",
        "購入日",
        "評価",
        "レビュー"
    };

    private BookEditDeleteController controller;
    private BookRepository repository;
    private ArrayList<Book> books;
    private DefaultTableModel tableModel;
    private JTable bookTable;

    public BookEditDeleteView(
            BookEditDeleteController controller,
            BookRepository repository) {
        this.controller = controller;
        this.repository = repository;
        initializeFrame();
        initializeComponents();
        loadBooks();
    }

    // ウィンドウの基本設定を行う。
    private void initializeFrame() {
        setTitle("書籍情報の編集・削除");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(920, 540);
        setLocationRelativeTo(null);
    }

    // タイトル、一覧表、操作ボタンを配置する。
    private void initializeComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 12));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel titleLabel = new JLabel("書籍情報の編集・削除");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(createTableScrollPane(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    // 登録済み書籍を表示する表を作成する。
    private JScrollPane createTableScrollPane() {
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookTable = new JTable(tableModel);
        bookTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setColumnWidths();

        return new JScrollPane(bookTable);
    }

    // 表の列幅を調整する。
    private void setColumnWidths() {
        int[] widths = {110, 160, 140, 90, 70, 100, 100, 60, 220};

        for (int i = 0; i < widths.length; i++) {
            bookTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    // 画面下部の操作ボタンを作成する。
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton editButton = new JButton("編集");
        JButton deleteButton = new JButton("削除");
        JButton updateButton = new JButton("更新");
        JButton backButton = new JButton("戻る");

        editButton.addActionListener(e -> editSelectedBook());
        deleteButton.addActionListener(e -> deleteSelectedBook());
        updateButton.addActionListener(e -> loadBooks());
        backButton.addActionListener(e -> dispose());

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(backButton);

        return buttonPanel;
    }

    // Repositoryから最新の一覧を取得して表を更新する。
    private void loadBooks() {
        tableModel.setRowCount(0);
        books = repository.findAllBooks();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Book book : books) {
            String purchaseDate = "";
            if (book.getPurchaseDate() != null) {
                purchaseDate = dateFormat.format(book.getPurchaseDate());
            }

            tableModel.addRow(new Object[] {
                nullToEmpty(book.getIsbnCode()),
                nullToEmpty(book.getBookName()),
                nullToEmpty(book.getWriterName()),
                nullToEmpty(book.getStatus()),
                book.getPrice(),
                nullToEmpty(book.getRegisteredDate()),
                purchaseDate,
                book.getRating(),
                nullToEmpty(book.getReview())
            });
        }
    }

    // 選択中の書籍を編集する。
    private void editSelectedBook() {
        Book selectedBook = getSelectedBook();
        if (selectedBook == null) {
            showSelectBookMessage();
            return;
        }

        showEditDialog(selectedBook);
    }

    // 選択中の書籍を確認後に削除する。
    private void deleteSelectedBook() {
        Book selectedBook = getSelectedBook();
        if (selectedBook == null) {
            showSelectBookMessage();
            return;
        }

        int result = JOptionPane.showConfirmDialog(
                this,
                "選択した書籍を削除しますか？",
                "削除確認",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            controller.deleteBook(selectedBook);
            loadBooks();
        }
    }

    // JTableで選択されているBookを取得する。
    private Book getSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow < 0) {
            return null;
        }

        int modelRow = bookTable.convertRowIndexToModel(selectedRow);
        if (modelRow < 0 || modelRow >= books.size()) {
            return null;
        }

        return books.get(modelRow);
    }

    // 編集用ダイアログを表示する。
    private void showEditDialog(Book book) {
        JTextField isbnCodeField = new JTextField(nullToEmpty(book.getIsbnCode()));
        JTextField bookNameField = new JTextField(nullToEmpty(book.getBookName()));
        JTextField writerNameField = new JTextField(nullToEmpty(book.getWriterName()));
        JComboBox<String> statusComboBox = new JComboBox<String>(new String[] {"未読", "読書中", "読了"});
        JTextField priceField = new JTextField(String.valueOf(book.getPrice()));
        JTextField registeredDateField = new JTextField(nullToEmpty(book.getRegisteredDate()));
        JTextField purchaseDateField = new JTextField(formatDate(book.getPurchaseDate()));
        JComboBox<Integer> ratingComboBox = new JComboBox<Integer>(new Integer[] {0, 1, 2, 3, 4, 5});
        JTextArea reviewArea = new JTextArea(nullToEmpty(book.getReview()), 4, 24);

        statusComboBox.setSelectedItem(book.getStatus());
        ratingComboBox.setSelectedItem(book.getRating());
        reviewArea.setLineWrap(true);
        reviewArea.setWrapStyleWord(true);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(4, 4, 4, 4);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        addFormRow(formPanel, constraints, 0, "ISBNコード", isbnCodeField);
        addFormRow(formPanel, constraints, 1, "書籍名", bookNameField);
        addFormRow(formPanel, constraints, 2, "著者名", writerNameField);
        addFormRow(formPanel, constraints, 3, "ステータス", statusComboBox);
        addFormRow(formPanel, constraints, 4, "価格", priceField);
        addFormRow(formPanel, constraints, 5, "登録日", registeredDateField);
        addFormRow(formPanel, constraints, 6, "購入日（yyyy-MM-dd）", purchaseDateField);
        addFormRow(formPanel, constraints, 7, "評価", ratingComboBox);
        addFormRow(formPanel, constraints, 8, "レビュー", new JScrollPane(reviewArea));

        int result = JOptionPane.showConfirmDialog(
                this,
                formPanel,
                "書籍情報の編集",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            updateBookFromInput(
                    book,
                    isbnCodeField,
                    bookNameField,
                    writerNameField,
                    statusComboBox,
                    priceField,
                    registeredDateField,
                    purchaseDateField,
                    ratingComboBox,
                    reviewArea);
        }
    }

    // 編集フォームの1行を追加する。
    private void addFormRow(JPanel panel, GridBagConstraints constraints, int row, String labelText, java.awt.Component inputComponent) {
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.0;
        panel.add(new JLabel(labelText), constraints);

        constraints.gridx = 1;
        constraints.gridy = row;
        constraints.weightx = 1.0;
        inputComponent.setPreferredSize(new Dimension(260, inputComponent.getPreferredSize().height));
        panel.add(inputComponent, constraints);
    }

    // 入力内容でBookを更新する。
    private void updateBookFromInput(
            Book book,
            JTextField isbnCodeField,
            JTextField bookNameField,
            JTextField writerNameField,
            JComboBox<String> statusComboBox,
            JTextField priceField,
            JTextField registeredDateField,
            JTextField purchaseDateField,
            JComboBox<Integer> ratingComboBox,
            JTextArea reviewArea) {
        if (bookNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "書籍名を入力してください", "入力エラー", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Integer price = parsePrice(priceField.getText().trim());
        if (price == null) {
            return;
        }

        Date purchaseDate = parsePurchaseDate(purchaseDateField.getText().trim());
        if (!purchaseDateField.getText().trim().isEmpty() && purchaseDate == null) {
            return;
        }

        book.setIsbnCode(isbnCodeField.getText().trim());
        book.setBookName(bookNameField.getText().trim());
        book.setWriterName(writerNameField.getText().trim());
        book.setStatus((String) statusComboBox.getSelectedItem());
        book.setPrice(price);
        book.setRegisteredDate(registeredDateField.getText().trim());
        book.setPurchaseDate(purchaseDate);
        book.setRating((Integer) ratingComboBox.getSelectedItem());
        book.setReview(reviewArea.getText());

        controller.updateBook(book);
        loadBooks();
    }

    private Integer parsePrice(String priceText) {
        if (priceText.isEmpty()) {
            return 0;
        }

        try {
            return Integer.parseInt(priceText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "価格は整数で入力してください", "入力エラー", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private Date parsePurchaseDate(String purchaseDateText) {
        if (purchaseDateText.isEmpty()) {
            return null;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        try {
            return dateFormat.parse(purchaseDateText);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "購入日はyyyy-MM-dd形式で入力してください", "入力エラー", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private String formatDate(Date date) {
        if (date == null) {
            return "";
        }

        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    private String nullToEmpty(String value) {
        if (value == null) {
            return "";
        }

        return value;
    }

    private void showSelectBookMessage() {
        JOptionPane.showMessageDialog(this, "書籍を選択してください", "確認", JOptionPane.INFORMATION_MESSAGE);
    }
}
