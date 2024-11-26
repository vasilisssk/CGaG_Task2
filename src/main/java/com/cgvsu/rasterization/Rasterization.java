package com.cgvsu.rasterization;

import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import static java.lang.Math.PI;

public class Rasterization {

    public static void drawArc(PixelWriter pixelWriter, int xc, int yc, int r, double ang1, Color color1, double ang2, Color color2) {
        int[] multiplier = new int[4];
        int xz = (int) Math.ceil(ang2/(PI/2));
        for (int i = multiplier.length-1; i >= 0; i--) {
            multiplier[i] = (xz-4 > 0) ? 1 : 0;
            xz-=1;
        }
        drawAuxilaryAxis(pixelWriter,xc,yc,r-2);
        double dist = ang2 - ang1;
        int x = -r; int y = 0; int error = 1-2*r;
        Color mainColor;
        do {
            double angle = angle(xc,yc,xc+y,yc+x);
            if (angle + multiplier[0] * 2 * PI >= ang1 && angle + multiplier[0] * 2 * PI <= ang2) {
                double diff = angle - ang1;
                mainColor = color1.interpolate(color2, diff/dist);
                pixelWriter.setColor(xc+y, yc+x, mainColor);
            }
            if ((angle+PI/2) + multiplier[1] * 2 * PI >= ang1 && (angle+PI/2) + multiplier[1] * 2 * PI <= ang2) {
                double diff = angle+PI/2 - ang1;
                mainColor = color1.interpolate(color2, diff/dist);
                pixelWriter.setColor(xc+x, yc-y, mainColor);
            }
            if ((angle+PI) + multiplier[2] * 2 * PI >= ang1 && (angle+PI) + multiplier[2] * 2 * PI <= ang2) {
                double diff = angle+PI - ang1;
                mainColor = color1.interpolate(color2, diff/dist);
                pixelWriter.setColor(xc-y, yc-x, mainColor);
            }
            if ((angle+3*PI/2) + multiplier[3] * 2 * PI >= ang1 && (angle+3*PI/2) + multiplier[3] * 2 * PI <= ang2) {
                double diff = angle+3*PI/2 - ang1;
                mainColor = color1.interpolate(color2, diff/dist);
                pixelWriter.setColor(xc-x, yc+y, mainColor);
            }
            r = error;
            if (r <= y) error += ++y*2+1;
            if (r > x || error > y) error += ++x*2+1;
        } while (x <= 0);
    }

    public static double angle(int xc, int yc, int x, int y) {
        int dx = x - xc; int dy = yc - y;
        if (dx >= 0 && dy >= 0) {
            return Math.atan((double)dy/dx);
        }
        else if (dx <= 0 && dy >= 0) {
            return Math.toRadians(180)+Math.atan((double)dy/dx);
        }
        else if (dx <= 0 && dy <= 0) {
            return (dx == 0) ? Math.toRadians(270) : Math.toRadians(180)+Math.atan((double)dy/dx);
        }
        else if (dx >= 0 && dy <= 0) {
            return Math.toRadians(360)+Math.atan((double)dy/dx);
        }
        return 0;
    }

    public static void drawAuxilaryAxis(PixelWriter pixelWriter, int xc, int yc, int r) {
        for (int i = xc; i <= xc+r; i++) {
            pixelWriter.setColor(i,yc,Color.RED);
        }
        for (int i = xc-r; i <= xc; i++) {
            pixelWriter.setColor(i,yc,Color.BLUE);
        }
        for (int i = yc; i <= yc+r; i++) {
            pixelWriter.setColor(xc,i,Color.GREEN);
        }
        for (int i = yc-r; i <= yc; i++) {
            pixelWriter.setColor(xc,i,Color.DARKGRAY);
        }
        pixelWriter.setColor(xc, yc, Color.ORANGE);
    }

    public static void bresenhamCircleAlgorithm(PixelWriter pixelWriter, int xc, int yc, int r) {
        drawAuxilaryAxis(pixelWriter,xc,yc,r-2);
        int x = -r; int y = 0; int error = 1-2*r;
        do {
            pixelWriter.setColor(xc+x, yc-y, Color.RED);
            pixelWriter.setColor(xc-x, yc+y, Color.RED);
            pixelWriter.setColor(xc+y, yc+x, Color.RED);
            pixelWriter.setColor(xc-y, yc-x, Color.RED);
            r = error;
            if (r <= y) error += ++y*2+1;
            if (r > x || error > y) error += ++x*2+1;
        } while (x < 0);
    }

    public static void bresenhamLineAlgorithm(PixelWriter pixelWriter, int x1, int y1, int x2, int y2) {
        int dx = x2-x1; int dy = y2-y1;
        int y = y1;
        int diry = y2-y1;
        if (diry > 0) diry = 1;
        if (diry < 0) diry = -1;
        double error = 0.5;
        double deltaerr = (double) (dy)/(dx);
        for (int i = x1; i < x2+1; i++) {
            pixelWriter.setColor(i,y, Color.RED);
            error+=deltaerr;
            if (error >= 1.0) {
                y+=diry;
                error-= 1.0;
            }
        }
//        int dx = x2 - x1; int dy = y2 - y1; int y = y1; int delta = 2*dy-dx;
//        for (int i = x1; i < x2+1; i++) {
//            pixelWriter.setColor(i, y, Color.RED);
//            if (delta >= 0) {
//                y+=1;
//                delta+=2*(dy-dx);
//            } else {
//                delta+=2*dy;
//            }
//        }
    }

//    public static void drawArc(PixelWriter pixelWriter, int xc, int yc, int r, double ang1, Color color1, double ang2, Color color2) {
//        int flag = 0; // для отслеживания четверти
//        int counter = 0; // если мы нарисовали всю окружность, то угол вновь становится 0 => будет бесконечный цикл
//        int colorCounter = 0; // счетчик итераций, нужен для интерполяции цвета
//        drawAuxilaryAxis(pixelWriter, xc,yc,r-2);
//        int approximatePixelsNumber = (int) (Math.floor(PI * (2*r+1) - ((PI * (2*r+1))/10)) * (ang2-ang1)/(2*PI)) + 1; // приблизительное кол-во пикселей для интерполяции
//        int x = (int) (xc+r*Math.cos(ang1/(PI/2) * (PI/2))); int y = (int) (yc-r*Math.sin(ang1/(PI/2) * (PI/2))); // начинаем с начала четверти
//        while (true) {
//            Color mainColor = color1.interpolate(color2, (float) colorCounter/approximatePixelsNumber);
//            double angle = angle(xc,yc,x,y) + counter * 360; // хранит значение угла на каком-то шаге
//            if (angle == 0 && flag > 1) { // проверка на то, что мы прошли круг, нужно, если мы указывает ang2 360 или близкий к нему
//                counter++;
//            }
//            if (angle >= ang1 && angle <= ang2) { // проверка, что мы находимся в нужном угловом диапазоне
//                pixelWriter.setColor(x,y,mainColor);
//                colorCounter++;
//            } else if (angle > ang2){
//                break;
//            }
//            if (angle >= 0 && angle < PI/2) {
//                flag = 1;
//                if (isCenterInCircle(xc, yc, r, x, y - 1)) {
//                    y -= 1;
//                } else if (isCenterInCircle(xc, yc, r, x - 1, y - 1)) {
//                    x -= 1;
//                    y -= 1;
//                } else {
//                    x -= 1;
//                }
//            }
//            else if (angle >= PI/2 && angle < PI) {
//                flag = 2;
//                if (isCenterInCircle(xc, yc, r, x - 1, y)) {
//                    x -= 1;
//                } else if (isCenterInCircle(xc, yc, r, x - 1, y + 1)) {
//                    y += 1;
//                    x -= 1;
//                } else {
//                    y += 1;
//                }
//            }
//            else if (angle >= PI && angle < 3*PI/2) {
//                flag = 3;
//                if (isCenterInCircle(xc, yc, r, x, y + 1)) {
//                    y += 1;
//                } else if (isCenterInCircle(xc, yc, r, x + 1, y + 1)) {
//                    x += 1;
//                    y += 1;
//                } else {
//                    x += 1;
//                }
//            }
//            else if (angle >= 3*PI/2 && angle < 2*PI) {
//                flag = 4;
//                if (isCenterInCircle(xc, yc, r, x + 1, y)) {
//                    x += 1;
//                } else if (isCenterInCircle(xc, yc, r, x + 1, y - 1)) {
//                    y -= 1;
//                    x += 1;
//                } else {
//                    y -= 1;
//                }
//            }
//        }
//    }

//    public static boolean isCenterInCircle(int xc, int yc, int r, int x, int y) {
//        return Math.pow((x-0.5) - (xc-0.5), 2) + Math.pow((y-0.5) - (yc-0.5), 2) - ((r+0.5) * (r+0.5)) <= 0;
//    }
}
