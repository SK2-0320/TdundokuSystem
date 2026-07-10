package controller;

import java.util.Date;

import domain.Book;

public class TsundokuPeriodController {
    public long calculateTsundokuPeriod(Book book) {
        if (book == null || book.getPurchaseDate() == null) {
            return 0;
        }

        Date currentDate = new Date();
        long differenceMillis = currentDate.getTime() - book.getPurchaseDate().getTime();

        return differenceMillis / (1000L * 60L * 60L * 24L);
    }
}
