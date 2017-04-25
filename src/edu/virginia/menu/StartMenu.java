package edu.virginia.menu;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventListener;
import edu.virginia.engine.util.SoundManager;
import edu.virginia.lab1test.Conquest;
import edu.virginia.lab1test.Conquest2;
import edu.virginia.lab1test.Famine;
import edu.virginia.lab1test.MovementTest;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.security.Key;

/**
 * Created by austinchang on 4/23/17.
 */
public class StartMenu extends Application implements IEventListener {

    private static Stage theStage = null;
    private static StartMenu instance = null;
    private final static int width = 1280;
    private final static int height = 720;
    private static SoundManager soundManager = new SoundManager();
    private static final EventHandler<KeyEvent> startScreenHandle = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.ENTER) {
                if (theStage != null) {
                    theStage.setScene(getStartMenu());
                    theStage.removeEventHandler(KeyEvent.KEY_PRESSED, this);
                }
            }
        }
    };

    public StartMenu() {
        if (instance != null) {
            System.out.println("ERROR: Cannot re-initialize singleton class!");
        }
        instance = this;
    }

    public static StartMenu getInstance() {
        return instance;
    }

    @Override
    public void start(Stage primaryStage) {
        theStage = primaryStage;
//        primaryStage.setScene(getStartMenu());
        primaryStage.setScene(getStartMenu());
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
        primaryStage.show();
    }

    private static Scene getStartMenu() {
        GridPane gridPane = new GridPane();
        Image warImage = new Image("file:resources" + File.separator + "menuImage.png", 1280, 720, false, false);
        gridPane.setBackground(new Background(new BackgroundImage(warImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        formatGridPane(gridPane);
        addRows(gridPane);
        addColumns(gridPane);
        addButtons(gridPane);
        addText(gridPane);
        Scene startMenu = new Scene(gridPane);

        try {
            soundManager.stopAllMusic();
            soundManager.loadMusic("menuMusic", "menu.wav");
            soundManager.playMusic("menuMusic");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        theStage.addEventHandler(KeyEvent.KEY_PRESSED, startScreenHandle);
        try {
            soundManager.stopAllMusic();
            soundManager.loadMusic("gameOverMusic", "gameOver.wav");
            soundManager.playMusic("gameOverMusic");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Scene(stackPane);
    }

    private static Scene getWinScreen() {
        StackPane stackPane = new StackPane();
        stackPane.setBackground(new Background(new BackgroundFill(Color.GOLD, CornerRadii.EMPTY, Insets.EMPTY)));
        ImageView victoryImage = new ImageView(new Image("file:resources" + File.separator + "victory.png"));
        stackPane.getChildren().add(victoryImage);
        StackPane.setAlignment(victoryImage, Pos.CENTER);
        ScaleTransition st = new ScaleTransition(Duration.millis(2000), victoryImage);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.3);
        st.setToY(1.3);
        st.setCycleCount(Timeline.INDEFINITE);
        st.setAutoReverse(true);
        st.play();
        theStage.addEventHandler(KeyEvent.KEY_PRESSED, startScreenHandle);
        try {
            soundManager.stopAllMusic();
            soundManager.loadMusic("victoryMusic", "victory.wav");
            soundManager.playMusic("victoryMusic");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Scene(stackPane);
    }

    private static void formatGridPane(GridPane gridPane) {
//        gridPane.setVgap(VERTICAL_SPACE);
//        Border border = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
//        gridPane.setBorder(border);
        gridPane.setGridLinesVisible(true);
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getEventType().equals("gameOver")) {
            theStage.setScene(getGameOverScreen());
            theStage.setIconified(false);
        } else if (event.getEventType().equals("victory")) {
            theStage.setScene(getWinScreen());
            theStage.setIconified(false);
        }
    }

    private static void addText(GridPane gridPane) {
//        Text title = new Text("Four Horsemen");
//        title.setFont(new Font(100));
//        gridPane.add(title, 1, 0);
//        GridPane.setColumnSpan(title, 2);
        ImageView titleImage = new ImageView(new Image("file:resources" + File.separator + "title.png"));
        gridPane.add(titleImage, 1, 0);
        GridPane.setColumnSpan(titleImage, 2);
        ScaleTransition st = new ScaleTransition(Duration.millis(1500), titleImage);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.05);
        st.setToY(1.05);
        st.setCycleCount(Timeline.INDEFINITE);
        st.setAutoReverse(true);
        st.play();

        Text warText = new Text("War");
        warText.setFont(new Font(30));
        warText.setFill(Color.WHITE);
        GridPane.setValignment(warText, VPos.BOTTOM);
        gridPane.add(warText, 1, 1);

        Text famineText = new Text("Conquest");
        famineText.setFont(new Font(30));
        famineText.setFill(Color.WHITE);
        GridPane.setValignment(famineText, VPos.BOTTOM);
        gridPane.add(famineText, 2, 1);
    }

    private static void addRows(GridPane gridPane) {
        RowConstraints row0 = new RowConstraints();
        row0.setPercentHeight(30);
        row0.setValignment(VPos.CENTER);
        RowConstraints row1 = new RowConstraints();
        formatRow(row1);
        row1.setPercentHeight(40);
        RowConstraints row2 = new RowConstraints();
        formatRow(row2);
        row2.setPercentHeight(30);
        gridPane.getRowConstraints().addAll(row0, row1, row2);
    }

    private static void formatRow(RowConstraints row) {
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

        Button conquestBtn = new Button();
        formatConquestButton(conquestBtn);
        formatButton(conquestBtn);

        Button boss3 = new Button();
        formatButton(boss3);
        Button boss4 = new Button();
        formatButton(boss4);

        gridPane.add(warBtn, 1, 1);
        gridPane.add(conquestBtn, 2, 1);
//        gridPane.add(boss3, 1, 2);
//        gridPane.add(boss4, 2, 2);
    }

    private static void formatWarButton(Button warBtn) {
        Image warImage = new Image("file:resources" + File.separator + "warBtn.png", 200, 200, false, false);
        warBtn.setBackground(new Background(new BackgroundImage(warImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        warBtn.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        try {
                            soundManager.stopAllMusic();
                            soundManager.loadMusic("warMusic", "war.wav");
                            soundManager.playMusic("warMusic");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        MovementTest warBoss = new MovementTest();
                        warBoss.addEventListener(getInstance(), "gameOver");
                        warBoss.addEventListener(getInstance(), "victory");
                        warBoss.start();
                        theStage.setIconified(true);
                    }
                });
    }

    private static void formatConquestButton(Button famBtn) {
        famBtn.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        famBtn.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        try {
                            soundManager.stopAllMusic();
                            soundManager.loadMusic("conquestMusic", "conquest.wav");
                            soundManager.playMusic("conquestMusic");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Conquest2 conquestBoss = new Conquest2();
                        conquestBoss.addEventListener(getInstance(), "gameOver");
                        conquestBoss.addEventListener(getInstance(), "victory");
                        conquestBoss.start();
                        theStage.setIconified(true);
                    }
                });
    }

    private static void formatButton(Button button) {
        button.setPrefSize(200, 200);
        Border border = new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5.0)));
        button.setBorder(border);
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        button.setOpacity(.75);
                    }
                });
        button.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        button.setOpacity(1.0);
                    }
                });
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
