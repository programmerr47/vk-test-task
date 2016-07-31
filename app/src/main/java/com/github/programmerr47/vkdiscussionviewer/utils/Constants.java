package com.github.programmerr47.vkdiscussionviewer.utils;

/**
 * @author Michael Spitsin
 * @since 2016-07-31
 */

public class Constants {
    public enum Font {
        ROBOTO_REGULAR("Roboto-Regular.ttf", 0),
        ROBOTO_MEDIUM("Roboto-Medium.ttf", 1);

        private final String fontName;
        private final int id;

        Font(String fontName, int id) {
            this.fontName = fontName;
            this.id = id;
        }

        public String getFontName() {
            return fontName;
        }

        public int getId() {
            return id;
        }

        public static Font fromId(int id) {
            if (id == ROBOTO_REGULAR.id) {
                return ROBOTO_REGULAR;
            } else if (id == ROBOTO_MEDIUM.id) {
                return ROBOTO_MEDIUM;
            } else {
                return null;
            }
        }
    }

    private Constants() {}

    public static final String ASSETS_FONTS_DIR = "fonts/";
}
