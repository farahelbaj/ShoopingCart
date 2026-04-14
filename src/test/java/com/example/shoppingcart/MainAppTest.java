package com.example.shoppingcart;

import javafx.geometry.NodeOrientation;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainAppTest {

    @Test
    void shouldReturnRightToLeftForArabicLocale() {
        assertEquals(
                NodeOrientation.RIGHT_TO_LEFT,
                MainApp.getOrientationForLocale(new Locale("ar"))
        );
    }

    @Test
    void shouldReturnLeftToRightForEnglishLocale() {
        assertEquals(
                NodeOrientation.LEFT_TO_RIGHT,
                MainApp.getOrientationForLocale(Locale.US)
        );
    }

    @Test
    void shouldReturnLeftToRightForFinnishLocale() {
        assertEquals(
                NodeOrientation.LEFT_TO_RIGHT,
                MainApp.getOrientationForLocale(new Locale("fi"))
        );
    }
}