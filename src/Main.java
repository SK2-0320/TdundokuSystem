import javax.swing.SwingUtilities;

import controller.BookEditDeleteController;
import controller.BookRegisterController;
import controller.BookSearchController;
import controller.StatisticsController;
import controller.StatusChangeController;
import controller.TsundokuPeriodController;
import domain.Book;
import gui.MainView;
import storage.BookApiService;
import storage.BookRepository;

public class Main {
    public static void main(String[] args) {
        // SwingのイベントディスパッチスレッドでGUIを起動する。
        SwingUtilities.invokeLater(() -> {
            // アプリケーション全体で共有するRepositoryを生成する。
            BookRepository repository = new BookRepository();

            BookApiService bookApiService = new BookApiService();

            // 同じRepositoryを使って各Controllerを生成する。
            BookRegisterController registerController =
                    new BookRegisterController(repository,bookApiService);
            BookSearchController searchController =
                    new BookSearchController(repository);
            BookEditDeleteController editDeleteController =
                    new BookEditDeleteController(repository);
            StatusChangeController statusChangeController =
                    new StatusChangeController(repository);
            StatisticsController statisticsController =
                    new StatisticsController();
            TsundokuPeriodController tsundokuPeriodController =
                    new TsundokuPeriodController();

            MainView mainView = new MainView(
                    registerController,
                    searchController,
                    editDeleteController,
                    statusChangeController,
                    statisticsController,
                    repository,
                    tsundokuPeriodController);
            mainView.setVisible(true);
        });
    }
}
