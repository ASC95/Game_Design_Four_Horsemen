package edu.virginia.menu;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventListener;
import edu.virginia.lab1test.MovementTest;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

/**
 * Created by austinchang on 4/23/17.
 */
public class StartMenu extends Application implements IEventListener {

//    private static Stage theStage = null;
private static StartMenu instance;

    public StartMenu() {
        if(instance != null) {
            System.out.println("ERROR: Cannot re-initialize singleton class!");
        }
        instance = this;
    }

    public static StartMenu getInstance() {
        return instance;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(getStartMenu());
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private static Scene getStartMenu() {
        GridPane gridPane = new GridPane();
        formatGridPane(gridPane);
        addRows(gridPane);
        addColumns(gridPane);
        addButtons(gridPane);
        addText(gridPane);
        Scene startMenu = new Scene(gridPane);
        return startMenu;
    }

    private static Scene getGameOverScreen() {
        StackPane stackPane = new StackPane();
        stackPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        ImageView gameOverImage = new ImageView(new Image("file:resources" + File.separator + "gameOver.png"));
        stackPane.getChildren().add(gameOverImage);
        StackPane.setAlignment(gameOverImage, Pos.CENTER);
        FadeTransition ft = new FadeTransition(Duration.millis(3000), gameOverImage);
        ft.setFromValue(0.1);
        ft.setToValue(1.0);
        ft.setCycleCount(Timeline.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();
        return new Scene(stackPane);
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getEventType().equals("gameOver")) {
            Stage g = new Stage();
            g.setScene(getGameOverScreen());
            g.setMaximized(true);
            g.show();
        }
//        theStage.setScene(getGameOverScreen());
//        theStage.setMaximized(true);

    }

    private static void formatGridPane(GridPane gridPane) {
//        gridPane.setVgap(VERTICAL_SPACE);
//        Border border = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
//        gridPane.setBorder(border);
//        gridPane.setGridLinesVisible(true);
    }

    private static void addText(GridPane gridPane) {
        Text title = new Text("Four Horsemen");
        title.setFont(new Font(100));
        gridPane.add(title, 1, 0);
        GridPane.setColumnSpan(title, 2);
        Text warText = new Text("War");
        warText.setFont(new Font(30));
        GridPane.setValignment(warText, VPos.BOTTOM);
        gridPane.add(warText, 1, 1);
    }

    private static void addRows(GridPane gridPane) {
        RowConstraints row0 = new RowConstraints();
        formatRow(row0);
        RowConstraints row1 = new RowConstraints();
        formatRow(row1);
        RowConstraints row2 = new RowConstraints();
        formatRow(row2);
//        RowConstraints row3 = new RowConstraints();
//        formatRow(row3);
        gridPane.getRowConstraints().addAll(row0, row1, row2);
    }

    private static void formatRow(RowConstraints row) {
        row.setPercentHeight(33.3);
        row.setValignment(VPos.CENTER);
    }

    private static void addColumns(GridPane gridPane) {
        ColumnConstraints col0 = new ColumnConstraints();
        formatColumn(col0);
        ColumnConstraints col1 = new ColumnConstraints();
        formatColumn(col1);
        ColumnConstraints col2 = new ColumnConstraints();
        formatColumn(col2);
        ColumnConstraints col3 = new ColumnConstraints();
        formatColumn(col3);
        gridPane.getColumnConstraints().addAll(col0, col1, col2, col3);
    }

    private static void formatColumn(ColumnConstraints col) {
        col.setPercentWidth(25);
        col.setHalignment(HPos.CENTER);
    }

    //        Java starts in the root directory of the project. That's why I can start with "resources".
//        Most methods/constructors in JavaFX that take a string as a parameter in this way
//        (i.e. specifying a resource) do so via string URI's rather than just a plain file path or URL.
    private static void addButtons(GridPane gridPane) {
        Button warBtn = new Button();
        formatButton(warBtn);
        formatWarButton(warBtn);
        Button boss2 = new Button();
        formatButton(boss2);
        Button boss3 = new Button();
        formatButton(boss3);
        Button boss4 = new Button();
        formatButton(boss4);

        gridPane.add(warBtn, 1, 1);
        gridPane.add(boss2, 2, 1);
        gridPane.add(boss3, 1, 2);
        gridPane.add(boss4, 2, 2);
    }

    private static void formatWarButton(Button warBtn) {
        Image warImage = new Image("file:resources" + File.separator + "warBtn.png", 200, 200, false, false);
        warBtn.setBackground(new Background(new BackgroundImage(warImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        warBtn.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        MovementTest warBoss = new MovementTest();
                        warBoss.addEventListener(getInstance(), "gameOver");
                        warBoss.start();
                    }
                });
    }

    private static void formatButton(Button button) {
        button.setPrefSize(200, 200);
        Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5.0)));
        button.setBorder(border);
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
