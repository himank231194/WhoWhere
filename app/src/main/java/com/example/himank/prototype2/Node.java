package com.example.himank.prototype2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Himank on 10/27/2017.
 */
public class Node {
    ArrayList<Integer> ans;
    char c;
    Map<Character, Node> child;
    boolean isLeaf;
    public Node(char c){
        this.c=c;
        isLeaf=false;
        ans = new ArrayList<>();
        child = new HashMap<>();
    }
    public void insert(String str,int actualIndex)
    {
        char[] strC = str.toCharArray();
        Node ptr = this;
        for(int i=0;i<strC.length;i++)
        {
            if(strC[i]==' '||strC[i]=='-') continue;
            if(!ptr.child.containsKey(strC[i]))
                ptr.child.put(strC[i],new Node(strC[i]));
            ptr=ptr.child.get(strC[i]);
        }
        ptr.ans.add(actualIndex);
        ptr.isLeaf=true;
    }
    public Set<Integer> find(String strS)
    {
        char[] str = strS.toCharArray();
        Node ptr=this;
        for(int i=0;i<str.length;i++)
        {
            if(str[i]==' '||str[i]=='-') continue;
            if(!ptr.child.containsKey(str[i]))
                return null;
            ptr=ptr.child.get(str[i]);
        }
        return bfs(ptr);
    }

    public Set<Integer> bfs(Node root)
    {
        Set<Integer> answer=new HashSet<>();
        Queue<Node> q = new LinkedBlockingQueue<Node>();
        q.add(root);
        while(!q.isEmpty())
        {
            Node temp=q.remove();
            if(temp.isLeaf)
                for(int c: temp.ans)
                    answer.add(c);
            // map ko directly iterate nhi kar skte isliye keys nikali saari
            // us keys k corresponding iterate kia and child values ko push kia
            Set<Character> keySet = temp.child.keySet();
            for(char ch:keySet)
            {
                q.add(temp.child.get(ch));
            }
        }
        return answer;
    }



}
