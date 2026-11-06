package dev.reveles.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;

public class EightOffView
{
    private VBox root;

    private static final double CARD_WIDTH = 75;
    private static final double CARD_HEIGHT = 105;
    private static final double COMPACT_HEIGHT = 32;

    // ------------------------------

    public EightOffView()
    {
        createLayout();
    }

    // ------------------------------

    private void createLayout()
    {
        root = new VBox(75);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #24543D;");

        HBox gameArea = new HBox(18);
        gameArea.setAlignment(Pos.TOP_CENTER);

        VBox foundations = createFoundations();
        VBox gameBoard = createGameBoard();

        gameArea.getChildren().addAll(foundations, gameBoard);

        HBox buttons = createButtons();

        root.getChildren().addAll(gameArea, buttons);
    }

    private VBox createGameBoard()
    {
        VBox gameBoard = new VBox(10);
        gameBoard.setAlignment(Pos.TOP_CENTER);

        HBox reserve = createReserve();
        HBox columns = createColumns();

        gameBoard.getChildren().addAll(reserve, columns);

        return gameBoard;
    }

    private VBox createFoundations()
    {
        VBox foundations = new VBox(10);
        foundations.setAlignment(Pos.TOP_CENTER);

        for(int i = 0; i < 4; i ++)
        {
            foundations.getChildren().add(createEmptySlot());
        }

        return foundations;
    }

    private HBox createReserve()
    {
        HBox reserve = new HBox(10);
        reserve.setAlignment(Pos.TOP_CENTER);

        for(int i = 0; i < 8; i ++)
        {
            reserve.getChildren().add(createEmptySlot());
        }

        return reserve;
    }

    private HBox createColumns()
    {
        HBox columns = new HBox(10);
        columns.setAlignment(Pos.TOP_CENTER);

        for(int i = 0; i < 8; i ++)
        {
            VBox column = new VBox(0);
            column.setAlignment(Pos.TOP_CENTER);

            column.getChildren().add(createEmptySlot());

            columns.getChildren().add(column);
        }

        return columns;
    }

    private StackPane createCard(boolean compact)
    {
        StackPane card = new StackPane();

        double height = compact ? COMPACT_HEIGHT : CARD_HEIGHT;

        card.setPrefSize(CARD_WIDTH, height);
        card.setMinSize(CARD_WIDTH, height);
        card.setMaxSize(CARD_WIDTH, height);

        card.setStyle
        (
            "-fx-background-color: #F5F1E8;" +
            "-fx-border-color: #252525;" +
            "-fx-border-width: 1.2;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;"
        );

        Label value = new Label("");

        value.setStyle
        (
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #222222;"
        );

        card.getChildren().add(value);
        return card;
    }

    private StackPane createEmptySlot()
    {
        StackPane slot = new StackPane();

        slot.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        slot.setMinSize(CARD_WIDTH, CARD_HEIGHT);
        slot.setMaxSize(CARD_WIDTH, CARD_HEIGHT);

        slot.setStyle
        (
            "-fx-background-color: #193D2D;" +
            "-fx-border-color: #102B20;" +
            "-fx-border-width: 1.2;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;"
        );

        return slot;
    }

    private HBox createButtons()
    {
        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);

        Button hintButton = new Button("Pista");
        Button undoButton = new Button("Deshacer");
        Button restartButton = new Button("Reiniciar");

        hintButton.setDisable(true);
        undoButton.setDisable(true);
        restartButton.setDisable(true);

        hintButton.setPrefWidth(110);
        undoButton.setPrefWidth(110);
        restartButton.setPrefWidth(110);

        buttons.getChildren().addAll
        (
            hintButton,
            undoButton,
            restartButton
        );

        return buttons;
    }

    private Label createSectionTitle(String text)
    {
        Label title = new Label(text);

        title.setStyle
        (
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #FFFFFF;"
        );

        return title;
    }

    public Parent getRoot()
    {
        return root;
    }
}