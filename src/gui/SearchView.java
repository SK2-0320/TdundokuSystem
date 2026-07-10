package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import controller.BookSearchController;
import domain.Book;
import storage.BookRepository;

public class SearchView extends JFrame {
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

    private BookSearchController controller;
    private JTextField keywordField;
    private DefaultTableModel tableModel;
    private JTable resultTable;

    public SearchView(BookSearchController controller) {
        this.controller = controller;
        initializeFrame();
        initializeComponents();
    }

    // ウィンドウの基本設定を行う。
    private void initializeFrame() {
        setTitle("書籍検索");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(920, 520);
        setLocationRelativeTo(null);
    }

    // 検索欄、結果表、戻るボタンを配置する。
    private void initializeComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 12));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        mainPanel.add(createSearchPanel(), BorderLayout.NORTH);
        mainPanel.add(createTableScrollPane(), BorderLayout.CENTER);
        mainPanel.add(createBottomPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    // 画面上部の検索欄を作成する。
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));

        keywordField = new JTextField();
        keywordField.setPreferredSize(new Dimension(300, 32));

        JButton searchButton = new JButton("検索");
        JButton clearButton = new JButton("入力内容をクリア");

        searchButton.addActionListener(e -> searchBooks());
        clearButton.addActionListener(e -> clearSearch());

        searchPanel.add(new JLabel("検索キーワード"));
        searchPanel.add(keywordField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);

        return searchPanel;
    }

    // 検索結果を表示する表を作成する。
    private JScrollPane createTableScrollPane() {
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        resultTable = new JTable(tableModel);
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setColumnWidths();

        return new JScrollPane(resultTable);
    }

    // 表の列幅を調整する。
    private void setColumnWidths() {
        int[] widths = {110, 160, 140, 90, 70, 100, 100, 60, 220};

        for (int i = 0; i < widths.length; i++) {
            resultTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
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

    // 検索条件を取得し、結果を表に表示する。
    private void searchBooks() {
        String keyword = keywordField.getText().trim();
        ArrayList<Book> results = controller.searchBooks(keyword, "");

        displayBooks(results);

        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "該当する書籍が見つかりませんでした",
                    "検索結果",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // JTableの内容を検索結果で更新する。
    private void displayBooks(ArrayList<Book> books) {
        tableModel.setRowCount(0);

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

    // 検索欄と検索結果を初期状態に戻す。
    private void clearSearch() {
        keywordField.setText("");
        tableModel.setRowCount(0);
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
            BookSearchController controller = new BookSearchController(repository);
            SearchView searchView = new SearchView(controller);
            searchView.setVisible(true);
        });
    }
}
