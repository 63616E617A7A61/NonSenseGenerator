package com.g63616e617a7a61.nonsensegenerator.view.components.syntaxTree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.g63616e617a7a61.nonsensegenerator.controller.ApiController;
import com.g63616e617a7a61.nonsensegenerator.model.SyntaxElement;
import javafx.scene.paint.Color;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Text;

public class SyntaxTreeController {

    @FXML 
    private StackPane syntaxTreeContainer; 

    public void generateTree(String sentence) {
        // Chiamata API (solo prima volta, dalla seconda riapertura della scheda del tree non chiama l'api)
        List<SyntaxElement> elements = null;
        if(elements == null){
            try {
                elements = ApiController.getSyntaxTree(sentence);
            } catch (IOException e) {
                e.printStackTrace();
            } 
        }

        // ordina gli archi in modo che i nodi collegati a sx del nodo attuale siano visualizzati dall'algoritmo di disegno in ordine descrescente
        // mentre quelli a dx in ordine crescente in modo che la crescita degli archi segua questa logica: il più grande è il più lontano e il più piccolo il più vicino
        SortEdges(elements);

        // Definizione parametri per le parole per il disegno dell'albero
        double baseY = 150; // Y da dove verranno scritte le parole 

        // Crea degli elementi text, ognuno dei quali contiene un SyntaxElement 
        ArrayList<Text> words = new ArrayList<>();
        for (SyntaxElement el : elements) {
            Text text = new Text(el.getValue());
            words.add(text);
        }

        // Calcola lo spazio necessario per disegnare l'albero sintattico (la width del canvas )
        double spacing = 20; // spaziatura tra le parole
        double totalWidth = 0; // variabile che contiene lo spazio totale occupato dall'albero
        for (Text word : words) {
            totalWidth += word.getLayoutBounds().getWidth() + spacing;
        }

        // Crea l'elemento canvas e il context su cui disegnare 
        Canvas canvas = new Canvas(totalWidth, 250);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Disegna ogni parola e calcolane il centro
        ArrayList<Point2D> wordCenters = new ArrayList<>(); // Variabile per memorizzare il centro di ogni parola
        double currentX = 0; // variabile di supporto per il calcolo del centro
        for (int i = 0; i < words.size(); i++) {
            Text word = words.get(i); // prendi la parola
            double wordWidth = word.getLayoutBounds().getWidth(); // prendi la sua larghezza

            gc.fillText(word.getText(), currentX, baseY); // disegnala nel canvas 

            double centerX = currentX + wordWidth / 2; // calcola il centro X
            double centerY = baseY - word.getLayoutBounds().getHeight() / 2; // calcola il centro Y
            wordCenters.add(new Point2D(centerX, centerY)); // aggiungi all'array

            currentX += wordWidth + spacing; // aggiorna la variabile di supporto
        }

        // Disegno degli archi
        gc.setStroke(Color.GRAY); // parametro di stile edges
        gc.setLineWidth(1.5); // parametro di stile edges

        // Mappa che tiene traccia del numero di archi disegnati per ogni parola (n. archi che partono da quella parola)
        Map<Integer, Integer> arcCountBySource = new HashMap<>();

        int count = 0; // variabile di supporto per determinare se disegnare l'arco sopra o sotto le parole 
        for (SyntaxElement el : elements) { // per ogni parola
            if (el.getEdges() != null) { // se ha degli archi
                count++; // aggiorno la variabile di supporto (disegna alternativamente gli archi sopra e gli archi sotto, considera solo parole con archi
                for (Integer targetIndex : el.getEdges()) {
                    // Se -1 significa che sto passando dal disegnare gli archi per i nodi di dx a quelli di sx, per questo motivo resetto la crescita delle altezze 
                    if(targetIndex == -1){
                        arcCountBySource.clear(); // posso pulire la mappa (gli elementi già contenuti non verranno più utilizzati e per il nodo corrente è proprio ciò che voglio: eliminare lo storico di archi già disegnati)
                        continue; // passa alla prossima iterazione (-1 è solo un marker, non un nodo vero)
                    }
                    Point2D from = wordCenters.get(el.getIndex()); // prendo le coordinato del nodo di partenza "from"
                    Point2D to = wordCenters.get(targetIndex); // prendo le coordinate del nodo di arrivo "to"

                    double arcX = Math.min(from.getX(), to.getX()); // prendo il punto minore (l'edge si disegna sempre da sx a dx)
                    double arcWidth = Math.abs(to.getX() - from.getX()); // calcolo la lunghezza dell'arco

                    boolean arcUp = (count % 2 == 0); // tramite la variabile di supporto count decido se disegnare sopra o sotto (arcup = true --> disegno sopra)
                    int arcCount = arcCountBySource.getOrDefault(el.getIndex(), 0); // cerca nella mappa l'elemento da cui partirà l'arco, se non lo trova vuol dire che ha 0 archi e lo inizializza a 0
                    arcCountBySource.put(el.getIndex(), arcCount + 1); // aggiorna la mappa, aggiungendo un arco al nodo corrente 

                    double distanceFactor = Math.abs(to.getX() - from.getX()) / 6; // rende l'altezza degli archi proporzionale alla distanza che devono percorrere
                    double arcHeight = 50 + arcCount * 15 + distanceFactor; // l'altezza è di 50 + 15 per ogni arco
                    double arcY = (arcUp ? from.getY() - arcHeight + distanceFactor * 0.4 + 7.5 * arcCount + 17 : from.getY() - 5 - 7.5 * arcCount - distanceFactor * 0.5); // posizione o sopra o sotto

                    // Disegna arco
                    gc.strokeArc(arcX, arcY, arcWidth, arcHeight, arcUp ? 0 : 180, 180, ArcType.OPEN); // disegna l'arco, se arcup = false lo ruota di 180 gradi (in modo da disegnarlo sotto)

                    // Calcola freccia
                    double arrowX = to.getX(); // calcola la posizione in x del nodo di arrivo 
                    double arrowY = arcY + arcHeight / 2; // calcola la posizione in y del nodo di arrivo 

                    double angle = arcUp ? Math.PI / 2 : -Math.PI / 2; // calcola l'angolo con cui orientare la freccia (se sopra avrà un certo angolo altrimenti un altro)

                    drawArrowHead(gc, arrowX, arrowY, angle, 5); // chiama il metodo ausiliario
                }
            }
        }
    
        syntaxTreeContainer.getChildren().clear(); // pulisce ogni volta il container
        syntaxTreeContainer.getChildren().add(canvas); // aggiunge a syntaxtreecontainer
    }

    // calcolo coordinate e punti della freccia 
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

    private void SortEdges(List<SyntaxElement> elements) {
        for (SyntaxElement el : elements) {
            if (el.getEdges() != null) {
                List<Integer> edges = el.getEdges();
                
                // Separa gli edge in "a sinistra" (indice minore) e "a destra" (indice maggiore)
                List<Integer> leftEdges = new ArrayList<>();
                List<Integer> rightEdges = new ArrayList<>();
                
                for (Integer targetIndex : edges) {
                    if (targetIndex < el.getIndex()) {
                        leftEdges.add(targetIndex);
                    } else {
                        rightEdges.add(targetIndex);
                    }
                }
                
                // Ordina:
                // - leftEdges in ordine DECRESCENTE (7, 6, 5...)
                // - rightEdges in ordine CRESCENTE (9, 10, 11...)
                leftEdges.sort((a, b) -> b - a);  // Ordine inverso
                rightEdges.sort((a, b) -> a - b);   // Ordine normale
                
                // Combina (prima destra, poi sinistra)
                List<Integer> sortedEdges = new ArrayList<>();
                sortedEdges.addAll(rightEdges);
                sortedEdges.add(-1); // per capire quando sto passando all'altro lato (da dx a sx)
                sortedEdges.addAll(leftEdges);
                
                // Sostituisce la lista originale con quella ordinata
                el.setEdges(new ArrayList<Integer>(sortedEdges));
            }
        }
    }
}