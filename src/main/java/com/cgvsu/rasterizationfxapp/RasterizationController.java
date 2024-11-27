package com.cgvsu.rasterizationfxapp;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;

import com.cgvsu.rasterization.*;
import javafx.scene.paint.Color;

import static java.lang.Math.PI;

public class RasterizationController {

    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));
        Rasterization.drawArc(canvas.getGraphicsContext2D().getPixelWriter(), 60, 51, 50,0, Color.BLACK, PI/2, Color.BLACK); // 0-90
        Rasterization.drawArc(canvas.getGraphicsContext2D().getPixelWriter(), 170, 51, 50,PI/2, Color.BLACK, PI, Color.BLACK); // 90-180
        Rasterization.drawArc(canvas.getGraphicsContext2D().getPixelWriter(), 280, 51, 50, PI, Color.BLACK, 3*PI/2, Color.BLACK); // 180-270
        Rasterization.drawArc(canvas.getGraphicsContext2D().getPixelWriter(), 390, 51, 50,3*PI/2, Color.BLACK, 2*PI, Color.BLACK); // 270-360

        Rasterization.drawArc(canvas.getGraphicsContext2D().getPixelWriter(), 60, 161, 50,0, Color.BLACK, PI, Color.BLACK); // 0-180
        Rasterization.drawArc(canvas.getGraphicsContext2D().getPixelWriter(), 170, 161, 50, PI, Color.BLACK, 2*PI, Color.BLACK); // 180-360

        int xc = 60;
        for (double i = 0; i <= 3*PI/2; i+=PI/2) {
            Rasterization.drawArc(canvas.getGraphicsContext2D().getPixelWriter(), xc, 271, 50, i, Color.BLACK, i+3*PI/2, Color.BLACK); // с разницей в 90
            xc+=110;
        }

        Rasterization.drawArc(canvas.getGraphicsContext2D().getPixelWriter(), 60, 381, 50,PI/2, Color.BLACK, 3*PI/2, Color.BLACK); // 90-270
        Rasterization.drawArc(canvas.getGraphicsContext2D().getPixelWriter(), 170, 381, 50, 3*PI/2, Color.BLACK, 5*PI/2, Color.BLACK); // 270-450

        Rasterization.drawArc(canvas.getGraphicsContext2D().getPixelWriter(), 650, 110, 100,0, Color.BLACK, 0.01, Color.BLACK); // маленький угол
        Rasterization.drawArc(canvas.getGraphicsContext2D().getPixelWriter(), 650, 220, 5,0, Color.BLACK, PI/2, Color.BLACK); // маленький радиус

        int xc1 = 60;
        for (double i = PI/6; i <= (2*PI)+(PI/6); i+=PI/3) {
            Rasterization.drawArc(canvas.getGraphicsContext2D().getPixelWriter(), xc1, 491, 50, i, Color.BLACK, i+((2*PI)-2*(PI/6)), Color.BLACK); // пакмен
            xc1+=110;
        }
    }
}