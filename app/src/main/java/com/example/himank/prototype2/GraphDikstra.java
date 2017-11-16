package com.example.himank.prototype2;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Himank on 10/27/2017.
 */
public class GraphDikstra {
    ArrayList<String> names;
    ArrayList<ArrayList<Integer>> graph;
    public int V;
    private int source;

    public int getSource() {
        return source;
    }

    public GraphDikstra(int V)
    {
        this.V = V;
    }

    public void setNames(ArrayList<String> names)
    {
        this.names = new ArrayList<>(names);
    }

    public void setGraph(ArrayList<ArrayList<Integer>> graph)
    {
        this.graph = graph;
    }

    public void setSource(int input)
    {
        this.source = input;
        swapRow(input, 0);
        swapCol(input, 0);
        String temp = names.get(0);
        names.set(0, names.get(input));
        names.set(input, temp);
    }

    private void swapCol(int x, int y) {
        for(int i=0;i<V;i++)
        {
            int temp=graph.get(i).get(x);
            graph.get(i).set(x,graph.get(i).get(y));
            graph.get(i).set(y,temp);
        }
    }

    private void swapRow(int x, int y) {
        ArrayList<Integer> temp = graph.get(x);
        graph.set(x, graph.get(y));
        graph.set(y, temp);
    }

    void reset()
    {
        swapRow(source, 0);
        swapCol(source, 0);
        String temp = names.get(0);
        names.set(0, names.get(source));
        names.set(source, temp);
    }

    public ArrayList<Integer> dijkstra(int dest)
    {
        if(dest==source)
        {
            dest=source=0;
        }
        if(dest == 0)
            dest = source;
        int[] dist = new int[V];
        boolean[] sptSet=new boolean[V];
        int[] parent=new int[V];

        for (int i = 0; i < V; i++)
        {
            parent[0] = -1;
            dist[i] = Integer.MAX_VALUE;
            sptSet[i] = false;
        }

        dist[0] = 0;

        for (int count = 0; count < V-1; count++)
        {
           int u = minDistance(dist, sptSet);

            sptSet[u] = true;

            for (int v = 0; v < V; v++)
                if (!sptSet[v] && graph.get(u).get(v)!=0 &&
                        dist[u] + graph.get(u).get(v) < dist[v])
                {
                    parent[v]  = u;
                    dist[v] = dist[u] + graph.get(u).get(v);
                }
        }
        return getSolution(dist, parent, dest);
    }

    public String solutionToString(ArrayList<Integer> path)
    {
        if(path==null)
            return "You are there";
        String res = "";
        res += path.get(0)+"\n";
        for(int i=1; i<path.size(); i++)
            res+="-> "+names.get(path.get(i)) + "\n ";
        return res;
    }

    ArrayList<Integer> getSolution(int dist[], int parent[], int dest)
    {
        ArrayList<Integer> path = new ArrayList<>();
        String res="";
        int src = 0,t;
        path.add(dist[dest]);

        // Base Case : If j is source
        if (parent[dest]==-1)
            return null;
//            return "You are already there!";

        Stack<Integer> s = new Stack<>();
        while(dest>=0 && dest<V && parent[dest]!=-1){
            s.push(dest);
            dest=parent[dest];
        }
        while(!s.empty()){
            path.add(s.pop());
        }
        return path;
    }


    int minDistance(int dist[], boolean sptSet[])
    {
        // Initialize min value
        int min = Integer.MAX_VALUE, min_index=0;

        for (int v = 0; v < V; v++)
            if (sptSet[v] == false && dist[v] <= min)
            {
                min = dist[v];
                min_index = v;
            }

        return min_index;
    }
}
