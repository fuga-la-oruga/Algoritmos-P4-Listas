package dev.reveles.view;

import dev.reveles.controller.EightOff;
import dev.reveles.controller.Move;
import dev.reveles.model.Card;
import dev.reveles.model.Suit;
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
    private EightOff game;

    private VBox[] columns = new VBox[8];
    private StackPane[] reserveSlots = new StackPane[8];
    private StackPane[] foundationSlots = new StackPane[4];

    private VBox root;

    private static final double CARD_WIDTH = 75;
    private static final double CARD_HEIGHT = 105;
    private static final double COMPACT_HEIGHT = 32;

    private Move.Location selectedSrc;
    private int selectedSrcI = -1;
    private int selectedCardI = -1;

    // ------------------------------

    public EightOffView()
    {
        game = new EightOff();

        createLayout();
        refreshBoard();
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
            foundationSlots[i] = createEmptySlot(Move.Location.FOUNDATION, i);
            foundations.getChildren().add(foundationSlots[i]);
        }

        return foundations;
    }

    private HBox createReserve()
    {
        HBox reserve = new HBox(10);
        reserve.setAlignment(Pos.TOP_CENTER);

        for(int i = 0; i < 8; i ++)
        {
            reserveSlots[i] = createEmptySlot(Move.Location.RESERVE, i);
            reserve.getChildren().add(reserveSlots[i]);
        }

        return reserve;
    }

    private HBox createColumns()
    {
        HBox columnsContainer = new HBox(10);
        columnsContainer.setAlignment(Pos.TOP_CENTER);

        for(int i = 0; i < 8; i ++)
        {
            columns[i] = new VBox(0);
            columns[i].setAlignment(Pos.TOP_CENTER);

            columnsContainer.getChildren().add(columns[i]);
        }

        return columnsContainer;
    }

    private StackPane createCard(Card card, boolean compact, int col, int cardI)
    {
        StackPane pane = new StackPane();

        double height = compact ? COMPACT_HEIGHT : CARD_HEIGHT;

        pane.setPrefSize(CARD_WIDTH, height);
        pane.setMinSize(CARD_WIDTH, height);
        pane.setMaxSize(CARD_WIDTH, height);

        String borderColor = isSelectedTableCard(col, cardI) ? "#FFD54F" : "#252525";
        double borderWidth = isSelectedTableCard(col, cardI) ? 3 : 1.2;

        pane.setStyle
        (
            "-fx-background-color: #F5F1E8;" +
            "-fx-border-color: " + borderColor + ";" +
            "-fx-border-width: " + borderWidth + ";" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;"
        );

        Label value = new Label(card.toString());

        value.setStyle
        (
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + getCardColor(card) + ";"
        );

        pane.getChildren().add(value);

        pane.setOnMouseClicked(event ->
        {
            if(selectedSrc == null)
            {
                if(game.isSelectable(col, cardI))
                {
                    selectCard(Move.Location.TABLE, col, cardI);
                }
            }
            else if
            (
                selectedSrc == Move.Location.TABLE &&
                selectedSrcI == col &&
                selectedCardI == cardI
            )
            {
                clearSelection();
            }
            else
            {
                handleCardClick(Move.Location.TABLE, col, cardI);
            }
        });

        return pane;
    }

    private StackPane createEmptySlot(Move.Location location, int index)
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

        slot.setOnMouseClicked(event ->
        {
            if(selectedSrc != null)
            {
                handleCardClick(location, index, -1);
            }
        });

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

    private void refreshBoard()
    {
        for(int i = 0; i < 4; i ++)
        {
            Card card = game.getFoundationTop(i);

            if(card != null)
            {
                updateSlot(foundationSlots[i], card, Move.Location.FOUNDATION, i);
            }
            else
            {
                resetEmptySlot(foundationSlots[i], Move.Location.FOUNDATION, i);
            }
        }

        for(int i = 0; i < 8; i ++)
        {
            Card card = game.getReserveCard(i);

            if(card != null)
            {
                updateSlot(reserveSlots[i], card, Move.Location.RESERVE, i);
            }
            else
            {
                resetEmptySlot(reserveSlots[i], Move.Location.RESERVE, i);
            }
        }

        for(int i = 0; i < 8; i ++)
        {
            columns[i].getChildren().clear();

            int size = game.getTableSize(i);

            if(size == 0)
            {
                columns[i].getChildren().add(createEmptySlot(Move.Location.TABLE, i));
                continue;
            }

            for(int j = 0; j < size; j ++)
            {
                Card card = game.getTableCard(i, j);

                boolean compact = j < size - 1;

                columns[i].getChildren().add(createCard(card, compact, i, j));
            }
        }
    }

    private void resetEmptySlot(StackPane slot, Move.Location location, int index)
    {
        slot.getChildren().clear();

        slot.setStyle
        (
            "-fx-background-color: #193D2D;" +
            "-fx-border-color: #102B20;" +
            "-fx-border-width: 1.2;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;"
        );

        slot.setOnMouseClicked(event ->
        {
            if(selectedSrc != null)
            {
                handleCardClick(location, index, -1);
            }
        });
    }

    private void updateSlot(StackPane slot, Card card, Move.Location location, int i)
    {
        slot.getChildren().clear();

        Label value = new Label(card.toString());

        value.setStyle
        (
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + getCardColor(card) + ";"
        );

        slot.getChildren().add(value);

        boolean selected =
            location == Move.Location.RESERVE &&
            selectedSrc == Move.Location.RESERVE &&
            selectedSrcI == i;

        String borderColor = selected ? "#FFD54F" : "#252525";
        double borderWidth = selected ? 3 : 1.2;

        slot.setStyle
        (
            "-fx-background-color: #F5F1E8;" +
            "-fx-border-color: " + borderColor + ";" +
            "-fx-border-width: " + borderWidth + ";" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;"
        );

        slot.setOnMouseClicked(event ->
        {
            if(selectedSrc != null)
            {
                handleCardClick(location, i, -1);
            }
            else if(location == Move.Location.RESERVE)
            {
                if(game.getReserveCard(i) != null)
                {
                    selectCard(Move.Location.RESERVE, i, -1);
                }
            }
        });
    }

    private void selectCard(Move.Location src, int srcI, int cardI)
    {
        selectedSrc = src;
        selectedSrcI = srcI;
        selectedCardI = cardI;

        refreshBoard();
    }

    private void clearSelection()
    {
        selectedSrc = null;
        selectedSrcI = -1;
        selectedCardI = -1;

        refreshBoard();
    }

    private boolean isSelectedTableCard(int col, int cardI)
    {
        return
            selectedSrc == Move.Location.TABLE &&
            selectedSrcI == col &&
            cardI >= selectedCardI;
    }

    private void handleCardClick(Move.Location dest, int destI, int cardI)
    {
        if(selectedSrc == null)
        {
            return;
        }

        if(selectedSrc == dest && selectedSrcI == destI && selectedCardI == cardI)
        {
            clearSelection();
            return;
        }

        boolean moved = false;

        if(selectedSrc == Move.Location.TABLE)
        {
            if(dest == Move.Location.TABLE)
            {
                moved = game.moveSequence
                (
                    selectedSrcI,
                    selectedCardI,
                    destI
                );
            }
            else if(selectedCardI == game.getTableSize(selectedSrcI) - 1)
            {
                moved = game.moveCard
                (
                    Move.Location.TABLE,
                    selectedSrcI,
                    dest,
                    destI
                );
            }
        }
        else
        {
            moved = game.moveCard
            (
                selectedSrc,
                selectedSrcI,
                dest,
                destI
            );
        }

        if(moved)
        {
            selectedSrc = null;
            selectedSrcI = -1;
            selectedCardI = -1;

            refreshBoard();
        }
        else
        {
            clearSelection();
        }
    }

    private String getCardColor(Card card)
    {
        if(card.getSuit() == Suit.HEARTS || card.getSuit() == Suit.DIAMONDS)
        {
            return "#D32F2F";
        }

        return "#222222";
    }

    public Parent getRoot()
    {
        return root;
    }
}