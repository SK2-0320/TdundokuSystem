package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import controller.StatusChangeController;
import domain.Book;
import storage.BookRepository;

public class StatusChangeView extends JFrame {
    private static final String[] COLUMN_NAMES = {
        "ISBNコード",
        "書籍名",
        "著者名",
        "現在のステータス",
        "購入日",
        "評価"
    };

    private StatusChangeController controller;
    private BookRepository repository;
    private ArrayList<Book> books;
    private DefaultTableModel tableModel;
    private JTable bookTable;

    public StatusChangeView(
            StatusChangeController controller,
            BookRepository repository) {
        this.controller = controller;
        this.repository = repository;
        initializeFrame();
        initializeComponents();
        loadBooks();
    }

    // ウィンドウの基本設定を行う。
    private void initializeFrame() {
        setTitle("積読ステータス変更");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(760, 500);
        setLocationRelativeTo(null);
    }

    // タイトル、一覧表、操作ボタンを配置する。
    private void initializeComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 12));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        mainPanel.add(createTopPanel(), BorderLayout.NORTH);
        mainPanel.add(createTableScrollPane(), BorderLayout.CENTER);
        mainPanel.add(createBottomPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    // 画面上部のタイトルと更新ボタンを作成する。
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("積読ステータス変更");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        JButton updateButton = new JButton("更新");
        updateButton.addActionListener(e -> loadBooks());

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(updateButton, BorderLayout.EAST);

        return topPanel;
    }

    // 書籍一覧を表示する表を作成する。
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
        int[] widths = {110, 180, 160, 130, 110, 70};

        for (int i = 0; i < widths.length; i++) {
            bookTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    // 画面下部の変更ボタンと戻るボタンを作成する。
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton changeButton = new JButton("変更");
        JButton backButton = new JButton("戻る");

        changeButton.addActionListener(e -> changeSelectedBookStatus());
        backButton.addActionListener(e -> dispose());

        bottomPanel.add(changeButton);
        bottomPanel.add(backButton);

        return bottomPanel;
    }

    // Repositoryから最新の書籍一覧を取得して表へ表示する。
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
                purchaseDate,
                book.getRating()
            });
        }
    }

    // 選択中の書籍のステータスを変更する。
    private void changeSelectedBookStatus() {
        Book selectedBook = getSelectedBook();
        if (selectedBook == null) {
            JOptionPane.showMessageDialog(this, "書籍を選択してください", "確認", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JComboBox<String> statusComboBox = new JComboBox<String>(new String[] {"未読", "読書中", "読了"});
        statusComboBox.setSelectedItem(selectedBook.getStatus());

        int result = JOptionPane.showConfirmDialog(
                this,
                statusComboBox,
                "ステータス変更",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String status = (String) statusComboBox.getSelectedItem();
            controller.changeStatus(selectedBook, status);
            loadBooks();
            JOptionPane.showMessageDialog(this, "ステータスを変更しました", "変更完了", JOptionPane.INFORMATION_MESSAGE);
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

    private String nullToEmpty(String value) {
        if (value == null) {
            return "";
        }

        return value;
    }
}
