/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.sna.generator;

import edu.uci.ics.jung.algorithms.generators.random.BarabasiAlbertGenerator;
import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGenerator;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import es.uam.eps.bmi.sna.structure.Edge;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import javafx.collections.transformation.SortedList;
import javax.swing.JFrame;
import org.apache.commons.collections15.Factory;

/**
 *
 * @author migal
 */
public class GraphGenerator {

    public static void main(String a[]) throws IOException {

        GraphFactory gf = new GraphFactory();
        UndirectedGraphFactory ungf = new UndirectedGraphFactory();
        VertexFactory vf = new VertexFactory();
        EdgeFactory ef = new EdgeFactory();
        double coefClusteringGlobal = 0;

        ///
        // Erdos Renyi Graph
        System.out.println("\nGenerando el grafo Erdos-Renyi . . .");
        ErdosRenyiGenerator erGenerator = new ErdosRenyiGenerator(ungf, vf, ef, 4000, 0.01);
        Graph erGraph = erGenerator.create();

        System.out.println("Generando ficheros para el grafo Erdos-Renyi . . .");
        writeToFile(erGraph, "graph/erdos.csv");
        writeDegreesToFile(erGraph, "degrees/er_degrees.csv");
        writeCountedDegreesToFile(erGraph, "degrees/er_counted_degrees.csv");

        System.out.println("Comprobando la paradoja de la amistad para el grafo Erdos-Renyi . . .");
        checkFriendshipParadox(erGraph);

        System.out.println("Calculando el coeficiente de clustering global del grafo Erdos-Renyi . . .");
        coefClusteringGlobal = calculateGlobalClustering(erGraph);
        System.out.println(String.format("%.2f", coefClusteringGlobal));
        
        System.out.println("Calculando el arraigo de varios pares de nodos aleatorios . . .");
        calculateRandomEmbeddedness(erGraph);
        
        System.out.println("Calculando la asortividad del grafo Erdos-Renyi . . .");
        System.out.println(String.format("%.2f", calculateAssortativity(erGraph)));
        // Erdos Renyi Graph
        /////

        /////
        // Barabasi Albert Graph
        System.out.println("\nGenerando el grafo Barabasi-Albert . . .");
        BarabasiAlbertGenerator baGenerator = new BarabasiAlbertGenerator(ungf, vf, ef, 40, 40, new HashSet());
        baGenerator.evolveGraph(3000);
        Graph baGraph = baGenerator.create();
        
        System.out.println("Generando ficheros para el grafo Barabasi-Albert . . .");
        writeToFile(baGraph, "graph/barabasi.csv");
        writeDegreesToFile(baGraph, "degrees/ba_degrees.csv");
        writeCountedDegreesToFile(baGraph, "degrees/ba_counted_degrees.csv");  
        
        System.out.println("Comprobando la paradoja de la amistad para el grafo Barabasi-Albert . . .");
        checkFriendshipParadox(baGraph);
        
        System.out.println("Calculando el coeficiente de clustering global del grafo Barabasi-Albert . . .");
        coefClusteringGlobal = calculateGlobalClustering(baGraph);
        System.out.println(String.format("%.2f", coefClusteringGlobal));
        System.out.println("Calculando el arraigo de varios nodos aleatorios . . .");
        calculateRandomEmbeddedness(baGraph);
        System.out.println("Calculando la asortividad del grafo Barabasi-Albert . . .");
        System.out.println(String.format("%.2f", calculateAssortativity(baGraph)));
        // Barabasi Albert Graph
        /////
        /////
        // Small 1 Graph
        System.out.println("\nGenerando el grafo Small1 . . .");
        Graph small1 = createGraphFromFile("graph/small1.csv");

        System.out.println("Generando ficheros para el grafo Small1 . . .");
        writeDegreesToFile(small1, "degrees/small1_degrees.csv");
        writeCountedDegreesToFile(small1, "degrees/small1_counted_degrees.csv");

        System.out.println("Comprobando la paradoja de la amistad para el grafo Small1 . . .");
        checkFriendshipParadox(small1);

        System.out.println("Calculando el coeficiente de clustering global del grafo Small1 . . .");
        coefClusteringGlobal = calculateGlobalClustering(small1);
        System.out.println(String.format("%.2f", coefClusteringGlobal));
        
        System.out.println("Calculando el arraigo de varios nodos aleatorios . . .");
        calculateRandomEmbeddedness(small1);
        
        System.out.println("Calculando la asortividad del grafo Small1 . . .");
        System.out.println(String.format("%.2f", calculateAssortativity(small1)));
        // Small 1 Graph
        /////

        /////
        // Small 2 Graph
        System.out.println("\nGenerando el grafo Small2 . . .");
        Graph small2 = createGraphFromFile("graph/small2.csv");

        System.out.println("Generando ficheros para el grafo Small2 . . .");
        writeDegreesToFile(small2, "degrees/small2_degrees.csv");
        writeCountedDegreesToFile(small2, "degrees/small2_counted_degrees.csv");

        System.out.println("Comprobando la paradoja de la amistad para el grafo Small2 . . .");
        checkFriendshipParadox(small2);

        System.out.println("Calculando el coeficiente de clustering global del grafo Small2 . . .");
        coefClusteringGlobal = calculateGlobalClustering(small2);
        System.out.println(String.format("%.2f", coefClusteringGlobal));
        
        System.out.println("Calculando el arraigo de varios nodos aleatorios . . .");
        calculateRandomEmbeddedness(small2);
        
        System.out.println("Calculando la asortividad del grafo Small2 . . .");
        System.out.println(String.format("%.2f", calculateAssortativity(small2)));
        // Small 2 Graph
        /////

        /////
        // Small 3 Graph
        System.out.println("\nGenerando el grafo Small3 . . .");
        Graph small3 = createGraphFromFile("graph/small3.csv");

        System.out.println("Generando ficheros para el grafo Small3 . . .");
        writeDegreesToFile(small3, "degrees/small3_degrees.csv");
        writeCountedDegreesToFile(small3, "degrees/small3_counted_degrees.csv");

        System.out.println("Comprobando la paradoja de la amistad para el grafo Small3 . . .");
        checkFriendshipParadox(small3);

        System.out.println("Calculando el coeficiente de clustering global del grafo Small3 . . .");
        coefClusteringGlobal = calculateGlobalClustering(small3);
        System.out.println(String.format("%.2f", coefClusteringGlobal));
        
        System.out.println("Calculando el arraigo de varios nodos aleatorios . . .");
        calculateRandomEmbeddedness(small3);
        
        System.out.println("Calculando la asortividad del grafo Small3 . . .");
        System.out.println(String.format("%.2f", calculateAssortativity(small3)));
        // Small 3 Graph
        /////

        /////
        // Twitter Graph
        System.out.println("\nGenerando el grafo de Twitter . . .");
        Graph twitter = createGraphFromFile("graph/twitter.csv");

        System.out.println("Generando ficheros para el grafo de Twitter . . .");
        writeDegreesToFile(twitter, "degrees/twitter_degrees.csv");
        writeCountedDegreesToFile(twitter, "degrees/twitter_counted_degrees.csv");

        System.out.println("Comprobando la paradoja de la amistad para el grafo de Twitter . . .");
        checkFriendshipParadox(twitter);

        System.out.println("Calculando el coeficiente de clustering global del grafo de Twitter . . .");
        coefClusteringGlobal = calculateGlobalClustering(twitter);
        System.out.println(String.format("%.2f", coefClusteringGlobal));
        
        System.out.println("Calculando el arraigo de varios nodos aleatorios . . .");
        calculateRandomEmbeddedness(twitter);
        
        System.out.println("Calculando la asortividad del grafo de Twitter . . .");
        System.out.println(String.format("%.2f", calculateAssortativity(twitter)));
        // Twitter Graph
        /////

        /////
        // Facebook Graph
        System.out.println("\nGenerando el grafo de Facebook . . .");
        Graph facebook = createGraphFromFile("graph/facebook_combined.txt");

        System.out.println("Generando ficheros para el grafo de Facebook . . .");
        writeDegreesToFile(facebook, "degrees/facebook_degrees.csv");
        writeCountedDegreesToFile(facebook, "degrees/facebook_counted_degrees.csv");

        System.out.println("Comprobando la paradoja de la amistad para el grafo de Facebook . . .");
        checkFriendshipParadox(facebook);

        System.out.println("Calculando el coeficiente de clustering global del grafo de Facebook . . .");
        coefClusteringGlobal = calculateGlobalClustering(facebook);
        System.out.println(String.format("%.2f", coefClusteringGlobal));
        
        System.out.println("Calculando el arraigo de varios nodos aleatorios . . .");
        calculateRandomEmbeddedness(facebook);
        
        System.out.println("Calculando la asortividad del grafo de Facebook . . .");
        System.out.println(String.format("%.2f", calculateAssortativity(facebook)));
        // Facebook Graph
        /////

    }

    /**
     * Función para escribir en un archivo los arcos de un grafo. Escribe en un
     * archivo nodo,nodo para cada arco de un grafo, separados, por salto de
     * linea.
     *
     * @param graph
     * @param fileName
     * @throws IOException
     */
    private static void writeToFile(Graph graph, String fileName) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        // Escribimos en el archivo cada arco
        graph.getEdges().forEach(edgeNum -> {
            Pair edge = graph.getEndpoints(edgeNum);
            printWriter.println(edge.getFirst() + "," + edge.getSecond());
        });
        printWriter.close();
    }

    private static void writeCountedDegreesToFile(Graph<String, Integer> graph, String fileName) throws IOException {
        Map<Integer, Integer> degrees = new TreeMap<>(Collections.reverseOrder());
        FileWriter fileWriter = new FileWriter(fileName);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        graph.getVertices().forEach(vertex -> {
            int degree = graph.getNeighborCount(vertex);
            int prevNum = 0;
            // Si ya existe nodos con ese grado, entonces le sumamos 1
            if (degrees.containsKey(degree)) {
                prevNum = degrees.get(degree);
            }
            // Le sumamos a la cantidad
            degrees.put(degree, prevNum + 1);
        });

        degrees.forEach((degree, num) -> {
            printWriter.println(degree + "," + num);
        });

        printWriter.close();
    }

    private static void writeDegreesToFile(Graph<String, Integer> graph, String fileName) throws IOException {
        List<Integer> degrees = new ArrayList<>();
        FileWriter fileWriter = new FileWriter(fileName);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        graph.getVertices().forEach(vertex
                -> degrees.add(graph.getNeighborCount(vertex)));

        degrees.sort(Collections.reverseOrder());

        degrees.forEach((degree) -> {
            printWriter.println(degree);
        });

        printWriter.close();
    }

    private static void showVisualGraph(Graph<String, Integer> graph, String title) {
        Layout<Integer, String> layout = new CircleLayout(graph);
        layout.setSize(new Dimension(1000, 1000));
        BasicVisualizationServer<Integer, String> vv
                = new BasicVisualizationServer<>(layout);
        vv.setPreferredSize(new Dimension(1050, 1050));

        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
    }

    private static Graph<String, Integer> createGraphFromFile(String fileName) throws IOException, FileNotFoundException {
        Graph graph = new GraphFactory().create();
        File file = new File(fileName);
        int edgeCount = 0;

        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;
        while ((line = br.readLine()) != null) {
            String[] tokens = new String[2];
            Edge<String> edge;

            tokens = line.split(",| ");

            edge = new Edge(tokens[0], tokens[1]);

            if (!graph.containsVertex(edge.getFirst())) {
                graph.addVertex(edge.getFirst());
            } else if (!graph.containsVertex(edge.getSecond())) {
                graph.addVertex(edge.getSecond());
            }

            graph.addEdge(++edgeCount, edge.getFirst(), edge.getSecond());
        }

        return graph;
    }

    private static void checkFriendshipParadox(Graph<String, Integer> graph) {
        double avgDegree = 0;
        Collection<String> vertices = graph.getVertices();
        int degreeSum = 0;
        double neighborsDegreeSum = 0;
        double method1 = 0.0;
        double method2 = 0.0;
        double avgDegreeNeighborsM1 = 0;
        double avgDegreeNeighborsM2 = 0;
        double avgDegreeVertex = 0;
        // Variable que cuenta usuarios que tienen más amigos que sus amigos
        int countGreater = 0;
        // Variable que cuenta usuarios que tienen menos amigos que sus amigos
        int countLess = 0;
        
        
        for (String vertex : vertices) {
            neighborsDegreeSum = 0.0;
            degreeSum += graph.getNeighborCount(vertex);
            Collection<String> neighbors = graph.getNeighbors(vertex);
            for (String neighbor : neighbors) {
                neighborsDegreeSum += graph.getNeighborCount(neighbor);
            }

            method1 = neighborsDegreeSum / graph.getNeighborCount(vertex);
            method2 = neighborsDegreeSum;
            
            if(graph.getNeighborCount(vertex) < neighborsDegreeSum)
                countLess++;
            else
                countGreater++;

            avgDegreeNeighborsM1 += method1;
            avgDegreeNeighborsM2 += method2;
        }

        // La media de los grados de los vertices
        avgDegreeVertex = degreeSum / vertices.size();

        // La media del grado de los vecinos metodo 1
        avgDegreeNeighborsM1 = avgDegreeNeighborsM1 / vertices.size();
        
        // La media del grado de los vecinos metodo 1
        avgDegreeNeighborsM2 = avgDegreeNeighborsM2 / graph.getEdgeCount();

        if (avgDegreeVertex <= avgDegreeNeighborsM1) {
            System.out.println("Método 1:");
            System.out.println("Grado medio de los usuarios " + String.format("%.2f", avgDegreeVertex) + " < " + String.format("%.2f", avgDegreeNeighborsM1) + " Grado medio de los amigos de los usuarios");
            System.out.println("Se cumple la paradoja de la amistad");
        }
        if(avgDegreeVertex <= avgDegreeNeighborsM2){
            System.out.println("Método 2:");
            System.out.println("Grado medio de los usuarios " + String.format("%.2f", avgDegreeVertex) + " < " + String.format("%.2f", avgDegreeNeighborsM2) + " Grado medio de los amigos de los usuarios");
            System.out.println("Se cumple la paradoja de la amistad");
        }
        if(countLess < countGreater){
            System.out.println("Método 3:");
            System.out.println("La mayoria de usuarios tienen más amigos que sus amigos. \nHay " + countGreater + " usuarios con más amigos que sus amigos y \n" + countLess + " usuarios con menos amigos que sus amigos");
            System.out.println("No se cumple la paradoja de la amistad");
        }else{
            System.out.println("Método 3:");
            System.out.println("La mayoria de usuarios tienen menos amigos que sus amigos. \nHay " + countGreater + " usuarios con más amigos que sus amigos y \n" + countLess + " usuarios con menos amigos que sus amigos");
            System.out.println("Se cumple la paradoja de la amistad");
        }
    }

    private static double calculateGlobalClustering(Graph<String, Integer> graph) {
        Collection<String> vertices = graph.getVertices();
        double localClusteringsSum = 0;
        for (String vertex : vertices) {
            localClusteringsSum += calculateLocalClustering(graph, vertex);
        }

        return localClusteringsSum / vertices.size();
    }

    private static double calculateLocalClustering(Graph<String, Integer> graph, String vertex) {
        String[] neighbors = graph.getNeighbors(vertex).toArray(new String[0]);
        // Numero de conexiones entre vecinos del vertice
        int connectionsBetweenNeighbors = 0;

        if (neighbors.length <= 1) {
            return 0.0;
        }

        // Calculamos el numero de conexiones entre vecinos
        for (int i = 0; i < neighbors.length; i++) {
            // Mientras no sea el último vecino de la lista
            if (i != neighbors.length - 1) {
                for (int j = i + 1; j < neighbors.length; j++) {
                    // Si existe un arco entre los vertices entonces sumamos una
                    if (graph.findEdge(neighbors[i], neighbors[j]) != null) {
                        connectionsBetweenNeighbors++;
                    }
                }
            }
        }

        // Numero de posibles conexiones entre los vecinos del vertice
        int pcbn = (neighbors.length * (neighbors.length - 1)) / 2;

        return (double) connectionsBetweenNeighbors / pcbn;
    }

    private static double calculateEmbeddedness(Graph<String, Integer> graph, Pair<String> pair) {
        if(pair == null)
                return 0.0;
        Collection<String> firstNeighbors = graph.getNeighbors(pair.getFirst());
        Collection<String> secondNeighbors = new ArrayList<String>(graph.getNeighbors(pair.getSecond()));
        // Nos quedamos todos los que coinciden
        secondNeighbors.retainAll(firstNeighbors);

        if (secondNeighbors.isEmpty()) {
            return 0.0;
        }

        return (double) secondNeighbors.size() / ((graph.getNeighborCount(pair.getFirst()) - 1) + (graph.getNeighborCount(pair.getSecond()) - 1) - secondNeighbors.size());
    }
    
    private static void calculateRandomEmbeddedness(Graph<String, Integer> graph){
        Random random = new Random();
        double embeddedness = 0.0;
        for(int i = 0; i < 3;){
            Pair<String> pair = graph.getEndpoints(random.nextInt(graph.getEdgeCount()));
            embeddedness = calculateEmbeddedness(graph, pair);
            if(embeddedness != 0.0){
                System.out.println("El arraigo del arco " + pair.toString() + " es: " + String.format("%.2f", embeddedness));
                i++;
            }
        }
    }
    
    private static double calculateAssortativity(Graph<String, Integer> graph){
        int m = graph.getEdgeCount();
        double degreeSum = 0;
        double result;
        double squared = 0.0, cubic = 0.0;
        // Calculamos el sumatorio de los arcos del grafo
        for(int edge : graph.getEdges()){
            Pair<String> pair = graph.getEndpoints(edge);
            degreeSum += graph.getNeighborCount(pair.getFirst()) * graph.getNeighborCount(pair.getSecond());
        }
        
        // 4 * m * Sumatorio de los arcos del grafo
        result = 4 * m * degreeSum;
        
        // Calculamos el sumatorio del grado de los nodos al cuadrado y al cubo
        for(String vertex : graph.getVertices()){
            squared += Math.pow(graph.getNeighborCount(vertex), 2);
            cubic += Math.pow(graph.getNeighborCount(vertex), 3);
        }
        // Sumatorio al cuadrado
        squared = Math.pow(squared, 2);
        
        // 2 * m * por el sumatorio de los grados al cubo
        degreeSum = 2 * m * cubic;

        // Aplicamos la formula
        result = (result - squared) / (degreeSum - squared);
        
        return result;
    }
}

class GraphFactory implements Factory<Graph<String, Integer>> {

    @Override
    public Graph<String, Integer> create() {
        return new UndirectedSparseGraph<>();
    }
}

class UndirectedGraphFactory implements Factory<UndirectedGraph<String, Integer>> {

    @Override
    public UndirectedGraph<String, Integer> create() {
        return new UndirectedSparseGraph<>();
    }
}

class VertexFactory implements Factory<String> {

    private Integer nVertices = 0;

    public String create() {
        nVertices++;
        return nVertices.toString();
    }
}

class EdgeFactory implements Factory<Integer> {

    private Integer nEdges = 0;

    public Integer create() {
        return nEdges++;
    }
}
