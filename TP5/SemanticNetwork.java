package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;


public class SemanticNetwork {
    private static String IS_A = "is a";
    private HashMap<String, Node> nodes;
    private List<Edge> edges;

    public SemanticNetwork() {
        nodes = new HashMap<>();
        edges = new ArrayList<>();
    }

    public List<Node> getIncomingNodes(String targetNodeName) {
        List<Node> incomingNodes = new ArrayList<>();
        Node targetNode = nodes.get(targetNodeName);
        if (targetNode == null) {
            throw new IllegalArgumentException("Node not found: " + targetNodeName);
        }
        for (Node node : nodes.values()) {
            for (Edge edge : node.getEdges()) {
                if (edge.getTo() == targetNode) {
                    incomingNodes.add(node);
                    break;
                }
            }
        }
        return incomingNodes;
    }

    public void addNode(String nodeName) {
        if (!nodes.containsKey(nodeName)) {
            nodes.put(nodeName, new Node(nodeName));
        }
    }

    public void addEdge(String fromNode, String toNode, String edgeName) {
        Node from = nodes.get(fromNode);
        Node to = nodes.get(toNode);
        if (from != null && to != null) {
            Edge edge = new Edge(from, to, edgeName);
            edges.add(edge);
            from.addEdge(edge);
        }
    }

    public List<Node> getNodes() {
        return new ArrayList<>(nodes.values());
    }

    public Node getNode(String nodeName) {
        return nodes.get(nodeName);
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public List<Edge> getEdgesFromNode(String nodeName) {
        Node node = nodes.get(nodeName);
        if (node != null) {
            return node.getEdges();
        }
        return new ArrayList<>();
    }

    public static class Node {
        private String name;
        private List<Edge> edges;
        private int visited_from;
        public Node(String name) {
            this.name = name;
            this.edges = new ArrayList<>();
            this.visited_from = -1;
        }
        public String getName() {
            return name;
        }
        public void addEdge(Edge edge) {
            edges.add(edge);
        }
        public List<Edge> getEdges() {
            return edges;
        }
        public int getVisitedFrom() {
            return visited_from;
        }
        public void setVisitedFrom(int visited_from) {
            this.visited_from = visited_from;
        }
        public void resetVisitedFrom() {
            this.visited_from = -1;
        }
    }

    public static class Edge {
        private Node from;
        private Node to;
        private String name;
        public Edge(Node from, Node to, String name) {
            this.from = from;
            this.to = to;
            this.name = name;
        }
        public Node getFrom() {
            return from;
        }
        public Node getTo() {
            return to;
        }
        public String getName() {
            return name;
        }
    }

    public void displayNetwork()
    {
        List<Node> nodes = this.getNodes();
        for (Node node : nodes) {
            System.out.println(node.getName() + ":");
            List<Edge> edges = this.getEdgesFromNode(node.getName());
            for (Edge edge : edges) {
                System.out.println("- " + edge.getName() + " -> " + edge.getTo().getName());
            }
            System.out.println();
        }
    }

    public static SemanticNetwork example1 (){
        SemanticNetwork semanticNetwork = new SemanticNetwork();
        
        // Ajout de nœuds
        semanticNetwork.addNode("guerre");
        semanticNetwork.addNode("conflit");
        semanticNetwork.addNode("guerre conventionnelle");
        semanticNetwork.addNode("guerre nucleaire");
        semanticNetwork.addNode("1 guerre mondiale");
        semanticNetwork.addNode("2 guerre mondiale");
        semanticNetwork.addNode("guerre algerie");
        semanticNetwork.addNode("guerre froide");
        semanticNetwork.addNode("guerre technologique");
        semanticNetwork.addNode("usa russie");
        semanticNetwork.addNode("chouhada");
        semanticNetwork.addNode("poilus");
        semanticNetwork.addNode("pertes financieres");
        semanticNetwork.addNode("victime militaire");
        semanticNetwork.addNode("perte humaine");
        semanticNetwork.addNode("fln");
        semanticNetwork.addNode("parti politique");
        semanticNetwork.addNode("larbi benmhidi");
        semanticNetwork.addNode("hassiba benbouali");

    
        // Ajout de relations
        semanticNetwork.addEdge("guerre", "conflit", SemanticNetwork.IS_A);
        semanticNetwork.addEdge("guerre conventionnelle", "guerre", SemanticNetwork.IS_A);
        semanticNetwork.addEdge("guerre nucleaire", "guerre", SemanticNetwork.IS_A);
        semanticNetwork.addEdge("guerre technologique", "guerre", SemanticNetwork.IS_A);
        semanticNetwork.addEdge("guerre technologique", "pertes financieres", "genere");
        semanticNetwork.addEdge("usa russie","guerre froide", SemanticNetwork.IS_A);
        semanticNetwork.addEdge("guerre froide", "guerre", SemanticNetwork.IS_A);
        semanticNetwork.addEdge("guerre algerie", "guerre conventionnelle", SemanticNetwork.IS_A);
        semanticNetwork.addEdge("guerre algerie", "chouhada", "a genere");
        semanticNetwork.addEdge("1 guerre mondiale", "guerre conventionnelle", SemanticNetwork.IS_A);
        semanticNetwork.addEdge("1 guerre mondiale", "poilus", "a genere");
        semanticNetwork.addEdge("2 guerre mondiale", "guerre conventionnelle", SemanticNetwork.IS_A);
        semanticNetwork.addEdge("chouhada", "victime militaire", SemanticNetwork.IS_A);
        semanticNetwork.addEdge("chouhada", "fln", "adhere");
        semanticNetwork.addEdge("poilus", "victime militaire", SemanticNetwork.IS_A);
        semanticNetwork.addEdge("victime militaire","perte humaine", SemanticNetwork.IS_A);
        semanticNetwork.addEdge("victime militaire","parti politique", "adhere pas");
        semanticNetwork.addEdge("fln","parti politique", SemanticNetwork.IS_A);
        semanticNetwork.addEdge("larbi benmhidi","chouhada", SemanticNetwork.IS_A);
        semanticNetwork.addEdge("hassiba benbouali","chouhada", SemanticNetwork.IS_A);
        
        return semanticNetwork;
    }
    public List<Node> propagate(Node start,int mark1,Node end ,int mark2)
    {
        propagate(start, mark1);
        propagate(end, mark2);
        return getNodesWithVisitedFromOneAndNeighborsWithVisitedFromTwo(mark1, mark2);
    }

    private void propagate(Node start, int mark) {
        Set<Node> visitedNodes = new HashSet<>();
        dfs(start, visitedNodes, mark);
    }

    private void dfs(Node node, Set<Node> visitedNodes, int mark) {
        node.setVisitedFrom(mark); // mark node as visited
        visitedNodes.add(node); // add node to visited nodes
        for (Node prevNode : this.getIncomingNodes(node.getName())) { // for each node that has an edge to this node
            for (Edge edge : prevNode.getEdges()) { // for each edge that leads to this node
                if (edge.getTo() == node && edge.getName().equals(SemanticNetwork.IS_A) && !visitedNodes.contains(prevNode)) {
                    dfs(prevNode, visitedNodes, mark);
                }
            }
        }
    }
    
    
    public List<Node> getNodesWithVisitedFromOneAndNeighborsWithVisitedFromTwo(int mark1, int mark2) {
        List<Node> result = new ArrayList<>();
        for (Node node : nodes.values()) {
            if (node.getVisitedFrom() == mark1) {
                for (Edge edge : node.getEdges()) {
                    if (edge.getTo().getVisitedFrom() == mark2) {
                        result.add(node);
                        break;
                    }
                }
            }
        }
        return result;
    }
    
    
    public static void main(String[] args) {
        // Création du réseau sémantique
        SemanticNetwork semanticNetwork = new SemanticNetwork();
        // utilisation example (guerre)
        semanticNetwork = SemanticNetwork.example1();
        // Affichage du réseau sémantique

        Node n1  = semanticNetwork.getNode("guerre");
        Node n2  = semanticNetwork.getNode("victime militaire");

        // propager les marqueurs 1 et 2 (guerre et victime militaire)
        List<Node> nodes_res = semanticNetwork.propagate(n1, 1, n2, 2);

        // pour chaque noeud sans la liste nodes1
        for (Node node : nodes_res) {
            System.out.println(node.getName());
        }
    }
}
