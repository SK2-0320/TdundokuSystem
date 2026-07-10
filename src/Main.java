import javax.swing.SwingUtilities;

import controller.BookRegisterController;
import gui.MainView;
import storage.BookRepository;

public class Main {
    public static void main(String[] args) {
        // SwingのイベントディスパッチスレッドでGUIを起動する。
        SwingUtilities.invokeLater(() -> {
            // アプリケーション全体で共有するRepositoryを生成する。
            BookRepository repository = new BookRepository();
            BookRegisterController registerController =
                    new BookRegisterController(repository);

            MainView mainView = new MainView(registerController);
            mainView.setVisible(true);
        });
    }
}
