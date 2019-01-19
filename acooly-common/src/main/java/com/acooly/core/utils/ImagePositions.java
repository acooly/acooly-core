package com.acooly.core.utils;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.geometry.Position;

import java.awt.*;

/**
 * @author zhangpu
 * @date 2019-01-19 18:46
 */
@Slf4j
public enum ImagePositions implements Position {

    /**
     * Calculates the {@link Point} at which an enclosed image should be placed
     * if it is to be placed at the top left-hand corner of the enclosing
     * image.
     */
    TOP_LEFT() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight,
                               int width, int height, int insetLeft, int insetRight,
                               int insetTop, int insetBottom) {
            int x = insetLeft;
            int y = insetTop;
            return new Point(x, y);
        }
    },

    /**
     * 顶部1/4位置
     */
    TOP_ONE_QUARTER() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight,
                               int width, int height, int insetLeft, int insetRight,
                               int insetTop, int insetBottom) {
            int x = (enclosingWidth / 4) - (width / 4);
            int y = insetTop;
            return new Point(x, y);
        }
    },

    /**
     * Calculates the {@link Point} at which an enclosed image should be placed
     * if it is to be horizontally centered at the top of the enclosing image.
     */
    TOP_CENTER() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight,
                               int width, int height, int insetLeft, int insetRight,
                               int insetTop, int insetBottom) {
            int x = (enclosingWidth / 2) - (width / 2);
            int y = insetTop;
            return new Point(x, y);
        }
    },

    /**
     * 顶部3/4位置
     */
    TOP_THREE_QUARTER() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight,
                               int width, int height, int insetLeft, int insetRight,
                               int insetTop, int insetBottom) {
            int x = (enclosingWidth * 3 / 4) - (width * 3 / 4);
            int y = insetTop;
            return new Point(x, y);
        }
    },

    /**
     * Calculates the {@link Point} at which an enclosed image should be placed
     * if it is to be placed at the top right-hand corner of the enclosing
     * image.
     */
    TOP_RIGHT() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight,
                               int width, int height, int insetLeft, int insetRight,
                               int insetTop, int insetBottom) {
            int x = enclosingWidth - width - insetRight;
            int y = insetTop;
            return new Point(x, y);
        }
    },


    /**
     * 1/4TOP-LEFT
     */
    ONE_QUARTER_LEFT() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight,
                               int width, int height, int insetLeft, int insetRight,
                               int insetTop, int insetBottom) {
            int x = insetLeft;
            int y = (enclosingHeight / 4) - (height / 4);
            return new Point(x, y);
        }
    },

    /**
     * 1/4top-1/4left
     */
    ONE_QUARTER_ONE_QUARTER() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight,
                               int width, int height, int insetLeft, int insetRight,
                               int insetTop, int insetBottom) {
            int x = (enclosingWidth / 4) - (width / 4);
            int y = (enclosingHeight / 4) - (height / 4);
            return new Point(x, y);
        }
    },

    /**
     * 1/4top-CENTER
     */
    ONE_QUARTER_CENTER() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight,
                               int width, int height, int insetLeft, int insetRight,
                               int insetTop, int insetBottom) {
            int x = (enclosingWidth / 2) - (width / 2);
            int y = (enclosingHeight / 4) - (height / 4);
            return new Point(x, y);
        }
    },

    /**
     * 1/4top-3/4LEFT
     */
    ONE_QUARTER_THREE_QUARTER() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight,
                               int width, int height, int insetLeft, int insetRight,
                               int insetTop, int insetBottom) {
            int x = (enclosingWidth * 3 / 4) - (width * 3 / 4);
            int y = (enclosingHeight / 4) - (height / 4);
            return new Point(x, y);
        }
    },

    ONE_QUARTER_RIGHT() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight,
                               int width, int height, int insetLeft, int insetRight,
                               int insetTop, int insetBottom) {
            int x = enclosingWidth - width - insetRight;
            int y = (enclosingHeight / 4) - (height / 4);
            return new Point(x, y);
        }
    },

    /**
     * Calculates the {@link Point} at which an enclosed image should be placed
     * if it is to be placed vertically centered at the left-hand corner of
     * the enclosing image.
     */
    CENTER_LEFT() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight,
                               int width, int height, int insetLeft, int insetRight,
                               int insetTop, int insetBottom) {
            int x = insetLeft;
            int y = (enclosingHeight / 2) - (height / 2);
            return new Point(x, y);
        }
    },

    /**
     * 中1/4位置
     */
    CENTER_ONE_QUARTER() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight,
                               int width, int height, int insetLeft, int insetRight,
                               int insetTop, int insetBottom) {
            int x = (enclosingWidth / 4) - (width / 4);
            int y = (enclosingHeight / 2) - (height / 2);
            return new Point(x, y);
        }
    },

    /**
     * Calculates the {@link Point} at which an enclosed image should be placed
     * horizontally and vertically centered in the enclosing image.
     */
    CENTER() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight,
                               int width, int height, int insetLeft, int insetRight,
                               int insetTop, int insetBottom) {
            int x = (enclosingWidth / 2) - (width / 2);
            int y = (enclosingHeight / 2) - (height / 2);
            return new Point(x, y);
        }
    },

    /**
     * 中3/4位置
     */
    CENTER_THREE_QUARTER() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight,
                               int width, int height, int insetLeft, int insetRight,
                               int insetTop, int insetBottom) {
            int x = (enclosingWidth * 3 / 4) - (width * 3 / 4);
            int y = (enclosingHeight / 2) - (height / 2);
            return new Point(x, y);
        }
    },

    /**
     * Calculates the {@link Point} at which an enclosed image should be placed
     * if it is to be placed vertically centered at the right-hand corner of
     * the enclosing image.
     */
    CENTER_RIGHT() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight,
                               int width, int height, int insetLeft, int insetRight,
                               int insetTop, int insetBottom) {
            int x = enclosingWidth - width - insetRight;
            int y = (enclosingHeight / 2) - (height / 2);
            return new Point(x, y);
        }
    },


    /**
     * 3/4TOP-LEFT
     */
    THREE_QUARTER_LEFT() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight,
                               int width, int height, int insetLeft, int insetRight,
                               int insetTop, int insetBottom) {
            int x = insetLeft;
            int y = (enclosingHeight * 3 / 4) - (height * 3 / 4);
            return new Point(x, y);
        }
    },


    /**
     * 3/4top-1/4left
     */
    THREE_QUARTER_ONE_QUARTER() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight,
                               int width, int height, int insetLeft, int insetRight,
                               int insetTop, int insetBottom) {
            int x = (enclosingWidth / 4) - (width / 4);
            int y = (enclosingHeight * 3 / 4) - (height * 3 / 4);
            return new Point(x, y);
        }
    },

    /**
     * 3/4top-CENTER
     */
    THREE_QUARTER_CENTER() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight,
                               int width, int height, int insetLeft, int insetRight,
                               int insetTop, int insetBottom) {
            int x = (enclosingWidth / 2) - (width / 2);
            int y = (enclosingHeight * 3 / 4) - (height * 3 / 4);
            return new Point(x, y);
        }
    },

    /**
     * 3/4top-3/4LEFT
     */
    THREE_QUARTER_THREE_QUARTER() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight,
                               int width, int height, int insetLeft, int insetRight,
                               int insetTop, int insetBottom) {
            int x = (enclosingWidth * 3 / 4) - (width * 3 / 4);
            int y = (enclosingHeight * 3 / 4) - (height * 3 / 4);
            return new Point(x, y);
        }
    },

    THREE_QUARTER_RIGHT() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight,
                               int width, int height, int insetLeft, int insetRight,
                               int insetTop, int insetBottom) {
            int x = enclosingWidth - width - insetRight;
            int y = (enclosingHeight * 3 / 4) - (height * 3 / 4);
            return new Point(x, y);
        }
    },


    /**
     * Calculates the {@link Point} at which an enclosed image should be placed
     * if it is to be placed at the bottom left-hand corner of the enclosing
     * image.
     */
    BOTTOM_LEFT() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight,
                               int width, int height, int insetLeft, int insetRight,
                               int insetTop, int insetBottom) {
            int x = insetLeft;
            int y = enclosingHeight - height - insetBottom;
            return new Point(x, y);
        }
    },

    /**
     * BOTTOM-1/4left
     */
    BOTTOM_ONE_QUARTER() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight,
                               int width, int height, int insetLeft, int insetRight,
                               int insetTop, int insetBottom) {
            int x = (enclosingWidth / 4) - (width / 4);
            int y = enclosingHeight - height - insetBottom;
            return new Point(x, y);
        }
    },

    /**
     * Calculates the {@link Point} at which an enclosed image should be placed
     * if it is to be horizontally centered at the bottom of the enclosing
     * image.
     */
    BOTTOM_CENTER() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight,
                               int width, int height, int insetLeft, int insetRight,
                               int insetTop, int insetBottom) {
            int x = (enclosingWidth / 2) - (width / 2);
            int y = enclosingHeight - height - insetBottom;
            return new Point(x, y);
        }
    },

    /**
     * BOTTOM-3/4left
     */
    BOTTOM_THREE_QUARTER() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight,
                               int width, int height, int insetLeft, int insetRight,
                               int insetTop, int insetBottom) {
            int x = (enclosingWidth * 3 / 4) - (width * 3 / 4);
            int y = enclosingHeight - height - insetBottom;
            return new Point(x, y);
        }
    },

    /**
     * Calculates the {@link Point} at which an enclosed image should be placed
     * if it is to be placed at the bottom right-hand corner of the enclosing
     * image.
     */
    BOTTOM_RIGHT() {
        @Override
        public Point calculate(int enclosingWidth, int enclosingHeight,
                               int width, int height, int insetLeft, int insetRight,
                               int insetTop, int insetBottom) {
            int x = enclosingWidth - width - insetRight;
            int y = enclosingHeight - height - insetBottom;
            return new Point(x, y);
        }
    },;
}
