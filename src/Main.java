import javax.swing.SwingUtilities;

import controller.BookRegisterController;
import controller.BookSearchController;
import gui.MainView;
import storage.BookRepository;

public class Main {
    public static void main(String[] args) {
        // SwingのイベントディスパッチスレッドでGUIを起動する。
        SwingUtilities.invokeLater(() -> {
            // アプリケーション全体で共有するRepositoryを生成する。
            BookRepository repository = new BookRepository();

            // 同じRepositoryを使って各Controllerを生成する。
            BookRegisterController registerController =
                    new BookRegisterController(repository);
            BookSearchController searchController =
                    new BookSearchController(repository);

            MainView mainView =
                    new MainView(registerController, searchController);
            mainView.setVisible(true);
        });
    }
}
