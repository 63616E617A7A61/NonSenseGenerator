package com.g63616e617a7a61.nonsensegenerator.view.components.syntaxTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.g63616e617a7a61.nonsensegenerator.controller.SentenceController;
import com.g63616e617a7a61.nonsensegenerator.model.SyntaxElement;

import javafx.scene.paint.Color;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Controller responsible for rendering the syntactic structure of a generated sentence.
 * It dynamically builds a visual syntax tree based on edges between syntax elements.
 * 
 * <p>It handles:
 * <ul>
 *   <li>Loading the syntax tree from a {@link SentenceController}</li>
 *   <li>Drawing labeled arcs to represent syntactic relationships</li>
 *   <li>Using cached tree data on repeated access</li>
 *   <li>Handling scroll behavior inside a {@link ScrollPane}</li>
 * </ul>
 */
public class SyntaxTreeController {

    @FXML 
    private ScrollPane syntaxTreeContainer; 

    /**
     * Initializes the syntax tree container.
     * Propagates vertical scroll events to the parent node.
     */
    @FXML 
    public void initialize() {
        // Block the vertical scroll of the syntax tree container and propagate it to the parent
        syntaxTreeContainer.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() != 0) {
                event.consume(); 
                if (syntaxTreeContainer.getParent() != null) {
                    syntaxTreeContainer.getParent().fireEvent(event.copyFor(syntaxTreeContainer.getParent(), syntaxTreeContainer.getParent()));
                }
            }
        });
    }

    List<SyntaxElement> elements = null; // cached elements of the syntax tree

    /**
     * Generates the syntax tree canvas from a given {@link SentenceController}.
     * This method:
     * <ol>
     *   <li>Fetches the syntax elements</li>
     *   <li>Sorts edges into left/right</li>
     *   <li>Draws words and arcs representing relationships</li>
     * </ol>
     * 
     * @param sc The controller used to generate the sentence and its syntax tree
     */
    public void generateTree(SentenceController sc) {
        // Call the API to get the syntax tree elements (only the first time, after that it uses the cached elements)
        if(elements == null){
            elements = sc.getSyntaxTree(); // get the syntax tree elements 
        }

        /* Order the edges so that the nodes connected to the left of the current node are displayed in descending order by the drawing algorithm 
         * while those on the right are displayed in ascending order so that the growth of the edges follows this logic: 
         * the largest is the farthest and the smallest is the closest
        */
        SortEdges(elements);

        // Parameters for the drawing
        double baseY = 125; // Y coordinate of the base line for the words
        double spacing = 20; // spacing between words

        // Create a list of Text objects for each word in the syntax tree
        ArrayList<Text> words = new ArrayList<>();
        for (SyntaxElement el : elements) {
            Text text = new Text(el.getValue());
            words.add(text);
        }

        // calculate the total width of the tree
        double totalWidth = 0; // container for the total width of the tree
        for (Text word : words) {
            totalWidth += word.getLayoutBounds().getWidth() + spacing;
        }

        // Create a canvas to draw the syntax tree
        Canvas canvas = new Canvas(totalWidth, baseY*2);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // load the custom font
        Font customFont = Font.loadFont(getClass().getResourceAsStream("../../res/fonts/font.ttf"), 15);
        if (customFont != null) {
            gc.setFont(customFont);
        } else {
            gc.setFont(new Font("System", 15)); // fallback se fallisce il caricamento
        }

        // Draw each word in the syntax tree and calculate its center
        ArrayList<Point2D> wordCenters = new ArrayList<>();  // List of centers of the words
        double currentX = 5; // variable of support for the x coordinate
        for (int i = 0; i < words.size(); i++) {
            Text word = words.get(i); 
            double wordWidth = word.getLayoutBounds().getWidth(); // take the width of the word

            gc.setFill(Color.WHITE); // style of the words
            
            gc.fillText(word.getText(), currentX, baseY); // draw the word

            double centerX = currentX + wordWidth / 2; // calculate the center X
            double centerY = baseY - word.getLayoutBounds().getHeight() / 2; // calculate the center Y
            wordCenters.add(new Point2D(centerX, centerY)); // add to the list of centers

            currentX += wordWidth + spacing; // update the x coordinate support variable
        }

        // Edges drawing
        gc.setStroke(Color.WHITE); // style of the edges
        gc.setLineWidth(1.5); // style of the edges

        // Maps to keep track of the number of arcs drawn from each source node
        Map<Integer, Integer> arcCountBySource = new HashMap<>();

        int count = 0; // variable of support to alternate the drawing of the arcs above and below the words
        for (SyntaxElement el : elements) { 
            if (el.getEdges() != null) { // if the node has edges
                count++; // increment the count of edges (to alternate the drawing of the arcs above and below the words)
                for (Integer targetIndex : el.getEdges()) {
                    // if it means that i'm passing from the right side to the left side of the node so I have to reset the map (in order to reset the height of the arcs)
                    if(targetIndex == -1){
                        arcCountBySource.clear(); 
                        continue; 
                    }
                    Point2D from = wordCenters.get(el.getIndex()); // take the coordinates of the starting node "from"
                    Point2D to = wordCenters.get(targetIndex); // take the coordinates of the ending node "to"

                    double arcX = Math.min(from.getX(), to.getX()); // take the minimum x coordinate between the two nodes (the arcs are drawn from the left to the right)
                    double arcWidth = Math.abs(to.getX() - from.getX()); // calculate the width of the arc (the distance between the two nodes)

                    boolean arcUp = (count % 2 == 0); // if arcUp is true, draw the arc above the word, otherwise below
                    int arcCount = arcCountBySource.getOrDefault(el.getIndex(), 0); // get the number of arcs already drawn from the source node (if it doesn't exist, set it to 0)
                    arcCountBySource.put(el.getIndex(), arcCount + 1); // update the number of arcs drawn from the source node

                    double distanceFactor = Math.abs(to.getX() - from.getX()) / 6; // makes the arcs height proportional to the distance between the two nodes
                    double arcHeight = 50 + arcCount * 15 + distanceFactor; // height is 50 + 15 * arcCount + distanceFactor (the more arcs are drawn, the higher the arc is)
                    double arcY = (arcUp ? from.getY() - arcHeight + distanceFactor * 0.4 + 7.5 * arcCount + 17 : from.getY() - 5 - 7.5 * arcCount - distanceFactor * 0.5); // draw the arc above or below the word 

                    // Draw the arc
                    gc.strokeArc(arcX, arcY, arcWidth, arcHeight, arcUp ? 0 : 180, 180, ArcType.OPEN); // draw the arc (if arcUp is true, draw it above the word, otherwise below rotate it 180 degrees)

                    // Draw the arrow head
                    double arrowX = to.getX(); // calculate the position in x of the node of arrival
                    double arrowY = arcY + arcHeight / 2; // calculate the position in y of the node of arrival 

                    double angle = arcUp ? Math.PI / 2 : -Math.PI / 2; // calculate the angle of the arrow head 

                    drawArrowHead(gc, arrowX, arrowY, angle, 5); 
                }
            }
        }
    
        syntaxTreeContainer.setContent(canvas); // add the canvas to the container
    }

    /**
     * Draws an arrowhead at the end of an arc.
     *
     * @param gc    The graphics context to draw on
     * @param x     X coordinate of the arrow point
     * @param y     Y coordinate of the arrow point
     * @param angle Rotation angle of the arrow
     * @param size  Size of the arrow head
     */
    private void drawArrowHead(GraphicsContext gc, double x, double y, double angle, double size) {
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        double x1 = x - size * cos + size * sin / 2;
        double y1 = y - size * sin - size * cos / 2;

        double x2 = x - size * cos - size * sin / 2;
        double y2 = y - size * sin + size * cos / 2;

        gc.strokeLine(x, y, x1, y1);
        gc.strokeLine(x, y, x2, y2);
    }




    /**
     * Sorts the edges of each syntax element into:
     * <ul>
     *   <li>Right edges in ascending order</li>
     *   <li>Left edges in descending order</li>
     * </ul>
     * Adds -1 as a separator between the two groups.
     *
     * @param elements The syntax tree elements to process
     */
    private void SortEdges(List<SyntaxElement> elements) {
        for (SyntaxElement el : elements) {
            if (el.getEdges() != null) {
                List<Integer> edges = el.getEdges();
                
                List<Integer> leftEdges = new ArrayList<>();
                List<Integer> rightEdges = new ArrayList<>();
                
                for (Integer targetIndex : edges) {
                    if (targetIndex < el.getIndex()) {
                        leftEdges.add(targetIndex);
                    } else {
                        rightEdges.add(targetIndex);
                    }
                }
                
                leftEdges.sort((a, b) -> b - a);  
                rightEdges.sort((a, b) -> a - b);  
                
                List<Integer> sortedEdges = new ArrayList<>();
                sortedEdges.addAll(rightEdges);
                sortedEdges.add(-1); // Separator for left and right edges
                sortedEdges.addAll(leftEdges);
                
                el.setEdges(new ArrayList<Integer>(sortedEdges));
            }
        }
    }
    
}
