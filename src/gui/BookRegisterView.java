package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import controller.BookRegisterController;
import domain.Book;
import storage.BookRepository;

public class BookRegisterView extends JFrame {
    private BookRegisterController controller;

    private JTextField isbnCodeField;
    private JTextField bookNameField;
    private JTextField writerNameField;
    private JComboBox<String> statusComboBox;
    private JTextField priceField;
    private JTextField registeredDateField;
    private JTextField purchaseDateField;
    private JComboBox<Integer> ratingComboBox;
    private JTextArea reviewArea;

    public BookRegisterView(BookRegisterController controller) {
        this.controller = controller;
        initializeFrame();
        initializeComponents();
    }

    // ウィンドウの基本設定を行う。
    private void initializeFrame() {
        setTitle("書籍登録");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 620);
        setLocationRelativeTo(null);
    }

    // 入力フォームとボタンを配置する。
    private void initializeComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 16));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(6, 6, 6, 6);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        isbnCodeField = new JTextField();
        bookNameField = new JTextField();
        writerNameField = new JTextField();
        statusComboBox = new JComboBox<String>(new String[] {"未読", "読書中", "読了"});
        priceField = new JTextField();
        registeredDateField = new JTextField();
        purchaseDateField = new JTextField();
        ratingComboBox = new JComboBox<Integer>(new Integer[] {0, 1, 2, 3, 4, 5});
        reviewArea = new JTextArea(5, 24);
        reviewArea.setLineWrap(true);
        reviewArea.setWrapStyleWord(true);

        addFormRow(formPanel, constraints, 0, "ISBNコード", isbnCodeField);
        addFormRow(formPanel, constraints, 1, "書籍名", bookNameField);
        addFormRow(formPanel, constraints, 2, "著者名", writerNameField);
        addFormRow(formPanel, constraints, 3, "ステータス", statusComboBox);
        addFormRow(formPanel, constraints, 4, "価格", priceField);
        addFormRow(formPanel, constraints, 5, "登録日", registeredDateField);
        addFormRow(formPanel, constraints, 6, "購入日（yyyy-MM-dd）", purchaseDateField);
        addFormRow(formPanel, constraints, 7, "評価", ratingComboBox);
        addFormRow(formPanel, constraints, 8, "レビュー", new JScrollPane(reviewArea));

        JPanel buttonPanel = new JPanel();
        JButton registerButton = new JButton("登録");
        JButton clearButton = new JButton("入力内容をクリア");
        JButton backButton = new JButton("戻る");

        Dimension buttonSize = new Dimension(140, 36);
        registerButton.setPreferredSize(buttonSize);
        clearButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);

        registerButton.addActionListener(e -> registerBook());
        clearButton.addActionListener(e -> clearFields());
        backButton.addActionListener(e -> dispose());

        buttonPanel.add(registerButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    // ラベルと入力部品を1行として追加する。
    private void addFormRow(JPanel panel, GridBagConstraints constraints, int row, String labelText, java.awt.Component inputComponent) {
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0.0;
        panel.add(new JLabel(labelText), constraints);

        constraints.gridx = 1;
        constraints.gridy = row;
        constraints.weightx = 1.0;
        panel.add(inputComponent, constraints);
    }

    // 入力値を検証してBookを登録する。
    private void registerBook() {
        String bookName = bookNameField.getText().trim();
        if (bookName.isEmpty()) {
            showErrorMessage("書籍名を入力してください");
            return;
        }

        Integer price = parsePrice();
        if (price == null) {
            return;
        }

        Date purchaseDate = parsePurchaseDate();
        if (!purchaseDateField.getText().trim().isEmpty() && purchaseDate == null) {
            return;
        }

        Book book = new Book();
        book.setIsbnCode(isbnCodeField.getText().trim());
        book.setBookName(bookName);
        book.setWriterName(writerNameField.getText().trim());
        book.setStatus((String) statusComboBox.getSelectedItem());
        book.setPrice(price);
        book.setRegisteredDate(registeredDateField.getText().trim());
        book.setPurchaseDate(purchaseDate);
        book.setRating((Integer) ratingComboBox.getSelectedItem());
        book.setReview(reviewArea.getText());

        controller.registerBook(book);
        JOptionPane.showMessageDialog(this, "書籍を登録しました", "登録完了", JOptionPane.INFORMATION_MESSAGE);
        clearFields();
    }

    // 価格を整数に変換する。
    private Integer parsePrice() {
        String priceText = priceField.getText().trim();
        if (priceText.isEmpty()) {
            return 0;
        }

        try {
            return Integer.parseInt(priceText);
        } catch (NumberFormatException e) {
            showErrorMessage("価格は整数で入力してください");
            return null;
        }
    }

    // 購入日をDate型に変換する。
    private Date parsePurchaseDate() {
        String purchaseDateText = purchaseDateField.getText().trim();
        if (purchaseDateText.isEmpty()) {
            return null;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        try {
            return dateFormat.parse(purchaseDateText);
        } catch (ParseException e) {
            showErrorMessage("購入日はyyyy-MM-dd形式で入力してください");
            return null;
        }
    }

    // 入力欄を初期状態に戻す。
    private void clearFields() {
        isbnCodeField.setText("");
        bookNameField.setText("");
        writerNameField.setText("");
        statusComboBox.setSelectedIndex(0);
        priceField.setText("");
        registeredDateField.setText("");
        purchaseDateField.setText("");
        ratingComboBox.setSelectedIndex(0);
        reviewArea.setText("");
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "入力エラー", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BookRepository repository = new BookRepository();
            BookRegisterController controller = new BookRegisterController(repository);
            BookRegisterView registerView = new BookRegisterView(controller);
            registerView.setVisible(true);
        });
    }
}
