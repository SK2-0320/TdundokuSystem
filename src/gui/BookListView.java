package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import controller.TsundokuPeriodController;
import domain.Book;
import storage.BookRepository;

public class BookListView extends JFrame {
    private static final String[] COLUMN_NAMES = {
        "ISBNコード",
        "書籍名",
        "著者名",
        "ステータス",
        "価格",
        "登録日",
        "購入日",
        "評価",
        "レビュー",
        "積読期間"
    };

    private BookRepository repository;
    private TsundokuPeriodController tsundokuPeriodController;
    private DefaultTableModel tableModel;
    private JTable bookTable;

    public BookListView(
            BookRepository repository,
            TsundokuPeriodController tsundokuPeriodController) {
        this.repository = repository;
        this.tsundokuPeriodController = tsundokuPeriodController;
        initializeFrame();
        initializeComponents();
        loadBooks(false);
    }

    // ウィンドウの基本設定を行う。
    private void initializeFrame() {
        setTitle("書籍一覧");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(980, 560);
        setLocationRelativeTo(null);
    }

    // タイトル、一覧表、戻るボタンを配置する。
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

        JLabel titleLabel = new JLabel("登録書籍一覧");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        JButton updateButton = new JButton("更新");
        updateButton.addActionListener(e -> loadBooks(true));

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(updateButton, BorderLayout.EAST);

        return topPanel;
    }

    // 一覧表示用の表を作成する。
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
        int[] widths = {110, 160, 140, 90, 70, 100, 100, 60, 220, 90};

        for (int i = 0; i < widths.length; i++) {
            bookTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    // 画面下部の戻るボタンを作成する。
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        JButton backButton = new JButton("戻る");
        backButton.addActionListener(e -> dispose());
        bottomPanel.add(backButton);

        return bottomPanel;
    }

    // Repositoryから最新の書籍一覧を取得して表を更新する。
    private void loadBooks(boolean showEmptyMessage) {
        tableModel.setRowCount(0);

        ArrayList<Book> books = repository.findAllBooks();
        displayBooks(books);

        if (showEmptyMessage && books.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "登録されている書籍はありません",
                    "書籍一覧",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // 書籍一覧をJTableへ表示する。
    private void displayBooks(ArrayList<Book> books) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Book book : books) {
            String purchaseDate = "";
            if (book.getPurchaseDate() != null) {
                purchaseDate = dateFormat.format(book.getPurchaseDate());
            }

            long tsundokuPeriod = tsundokuPeriodController.calculateTsundokuPeriod(book);

            tableModel.addRow(new Object[] {
                nullToEmpty(book.getIsbnCode()),
                nullToEmpty(book.getBookName()),
                nullToEmpty(book.getWriterName()),
                nullToEmpty(book.getStatus()),
                book.getPrice(),
                nullToEmpty(book.getRegisteredDate()),
                purchaseDate,
                book.getRating(),
                nullToEmpty(book.getReview()),
                tsundokuPeriod + "日"
            });
        }
    }

    private String nullToEmpty(String value) {
        if (value == null) {
            return "";
        }

        return value;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BookRepository repository = new BookRepository();
            TsundokuPeriodController tsundokuPeriodController = new TsundokuPeriodController();
            BookListView listView = new BookListView(repository, tsundokuPeriodController);
            listView.setVisible(true);
        });
    }
}
