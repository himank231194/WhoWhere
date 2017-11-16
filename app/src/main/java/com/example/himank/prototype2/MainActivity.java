package com.example.himank.prototype2;

import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.himank.prototype2.NamesAdapter;
import com.example.himank.prototype2.Node;
import com.example.himank.prototype2.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String PATH_EXTRAS = "PATHEXTRA";
    AutoCompleteTextView actv1, actv2;

    Button bSubmit;
    TextView tvResult;
    private ArrayList<String> toBeDisplayed;
    GraphDikstra graphDikstra;

    boolean inputTaken;
    private ArrayList<String> toBeDisplayed3;
    private ArrayList<Integer> path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actv1 = (AutoCompleteTextView) findViewById(R.id.acType);
        actv2 = (AutoCompleteTextView) findViewById(R.id.acType2);
        bSubmit = (Button) findViewById(R.id.bSubmit);
        tvResult = (TextView) findViewById(R.id.tvResult);
        inputTaken = false;


        actv1.setThreshold(1);
        actv2.setThreshold(1);


        String[][] ar = new String[1500][];//(Arrays.asList("LT-01", "LT-02","IT-01","BOYS WASHROM"));
        int c = 0;
        InputStream inputStream = getResources().openRawResource(R.raw.aliasnew);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        try {
            line = reader.readLine();
            while (line != null) {
                if (!line.trim().equals(""))
                    ar[c++] = line.split(",");
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //String ar[] = ;
        //Arrays.sort(ar,0,c);
        ArrayList<ArrayList<String>> arr = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < c; i++) {
            ArrayList<String> s = new ArrayList<>();
            for (String p : ar[i])
                s.add(p);
            arr.add(s);
        }

        graphDikstra = new GraphDikstra(arr.size());

        Node root = new Node('0');
        for (int i = 0; i < arr.size(); i++)
            for (String s : arr.get(i))
                root.insert(s.toUpperCase(), i);

        toBeDisplayed = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            toBeDisplayed.add(arr.get(i).get(0).toUpperCase().trim());
            tvResult.setText(tvResult.getText()+arr.get(i).get(0).toUpperCase().trim()+"\n");
        }

        NamesAdapter namesAdapter = new NamesAdapter(
                this,
                R.layout.list_item,
                R.id.textView,
                toBeDisplayed,
                root
        );

        ArrayList<String> toBeDisplayed2 = new ArrayList<>(toBeDisplayed);
        toBeDisplayed3 = new ArrayList<>(toBeDisplayed);
        NamesAdapter namesAdapter2 = new NamesAdapter(
                this,
                R.layout.list_item,
                R.id.textView,
                toBeDisplayed2,
                root
        );
        actv1.setAdapter(namesAdapter);
        actv1.setInputType(InputType.TYPE_CLASS_TEXT);
        actv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actv1.showDropDown();
            }
        });
        actv2.setAdapter(namesAdapter2);
        actv2.setInputType(InputType.TYPE_CLASS_TEXT);
        actv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actv2.showDropDown();
            }
        });

        bSubmit.setOnClickListener(this);
        findViewById(R.id.bShowOnMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DisplayMapActivity.class);
                if(path==null)
                {
                    Toast.makeText(MainActivity.this,"You are there!",Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayList<Integer> temp = new ArrayList<Integer>(path);
                temp.set(0,graphDikstra.getSource());
                intent.putIntegerArrayListExtra(PATH_EXTRAS,temp);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bSubmit:
                hideKeyboard();
                Log.d("Himank", actv1.getText().toString().trim());
                Log.d("Himank", actv2.getText().toString().trim());

                int src = toBeDisplayed3.indexOf(actv1.getText().toString().trim());
                int dest = toBeDisplayed3.indexOf(actv2.getText().toString().trim());

                if(src==-1 || dest==-1)
                {
                    Toast.makeText(this, "Please Select a valid location! src:"+src+" dest:"+dest, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!inputTaken) {
                    ArrayList<ArrayList<Integer>> graph = new ArrayList<ArrayList<Integer>>();
                    Log.d("Himank", ""+graphDikstra.V);
                    //graph.ensureCapacity(graphDikstra.V);
                    for(int i=0; i<=graphDikstra.V; i++)
                        graph.add(new ArrayList<Integer>());
                    for(int i=0;i<=graphDikstra.V; i++)
                        for(int j=0;j<=graphDikstra.V; j++)
                            graph.get(i).add(0);

                    InputStream inputStream = getResources().openRawResource(R.raw.edgesnew);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    Scanner sc = new Scanner(reader);
                    while (sc.hasNextInt()) {
                        int a = sc.nextInt();
                        int b = sc.nextInt();
                        int w = sc.nextInt();
                        graph.get(a).set(b, w);
                        graph.get(b).set(a, w);
                    }
                    sc.close();
                    graphDikstra.setGraph(graph);
                    graphDikstra.setNames(toBeDisplayed3);
                    inputTaken = true;
                }
                graphDikstra.setSource(src);
                path = graphDikstra.dijkstra(dest);

                tvResult.setText(graphDikstra.solutionToString(path));
                graphDikstra.reset();
                break;
            default:
                return;
        }
    }
    void hideKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
