package com.example.himank.prototype2;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Himank on 10/27/2017.
 */
public class NamesAdapter extends ArrayAdapter<String> {

    private final ArrayList<String> tempItems;
    Context context;
    int resource, textViewResourceId;
    List<String> items, suggestions;
    Node root;

    public NamesAdapter(Context context, int resource, int textViewResourceId, List<String> items, Node root) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        this.tempItems = new ArrayList<String>(items);
        this.root=root;
        //this.tempItems = ;
        suggestions = new ArrayList<String>();
        //cleanedItems = new ArrayList<String>();

       /* for(String s : tempItems)
            cleanedItems.add(cleanPat(s.toUpperCase()));
        for(int i=0; i<tempItems.size(); i++)
            tempItems.set(i, tempItems.get(i).toUpperCase());*/

        //Toast.makeText(context, tempItems.size()+ " "+suggestions.size(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, parent, false);
        }
        String names = items.get(position);
        if (names != null) {
            TextView lblName = (TextView) view.findViewById(textViewResourceId);
            if (lblName != null)
                lblName.setText(names);
        }

        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = (String) resultValue;
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null &&constraint.length()>0) {
                suggestions.clear();

                String pat = constraint.toString().trim().toUpperCase();
                if(pat.equals(""))
                    return new FilterResults();
                //String cleanedPat = cleanPat(pat);

                //int i=0;
                for(int i=0; i!=-1; i=pat.indexOf(" ",i+1)) {
                    Set<Integer> set1 = root.find(pat.substring(i));
                    //Toast.makeText(context, pat.substring(i),Toast.LENGTH_SHORT).show();
                    //while(i<tempItems.size() && tempItems.get(i).length()==0) i++; && tempItems.get(i).length()>0 && tempItems.get(i).charAt(0)<=pat.charAt(0)
                    if(set1==null)
                        continue;
                    for (int j : set1) {
                        suggestions.add(tempItems.get(j));
                    }
                }
                //tempItems.get(i).charAt(0)==pat.charAt(0) &&
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<String> filterList = (ArrayList<String>) results.values;
            if (results != null && results.count > 0) {
                clear();
                try {
                    for (String names : filterList) {
                        add(names);
                        notifyDataSetChanged();
                    }
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    };
    boolean seqSearch(String org,String pat)
    {
        //pat=cleanPat(pat);
        //Toast.makeText(context, pat, Toast.LENGTH_SHORT).show();
        char[] orgA = org.toCharArray();
        char[] patA = pat.toCharArray();

        int j=0;
        int nOrg=org.length();
        int nPat=pat.length();
        for(int i=0;i<nOrg && j<nPat;i++)
        {
            while(j<nPat && patA[j]==' ') j++;
            while(i<nOrg && orgA[i]==' ') i++;
            if(j>=nPat)
            {
                return true;
            }
            if(patA[j]==orgA[i])
            {
                j++;
            }
        }
        if(j>=nPat)
            return true;
        else
            return false;
    }


    String cleanPat(String pat)
    {
        String newPat="";
        int c=0;
        for(char ch: pat.toCharArray())
            if((ch>='A' && ch<='Z')||(ch>='1' && ch<='9'))
                newPat+=ch+"";
        return newPat;
    }
}