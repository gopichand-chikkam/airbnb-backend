package com.gopi.airbnb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class test {
   public static boolean vertical(char[][] chess,int j){
        int n=chess.length;
        for(int i=0;i<n;i++){
            if(chess[i][j]=='Q')return false;
        }
        return true;
    }

    public static boolean horizontal(char[][] chess,int i){
        int n=chess.length;
        for(int j=0;j<n;j++){
            if(chess[i][j]=='Q')return false;
        }
        return true;
    }
   public static boolean diagnol(char[][] chess,int ind,int jnd){
        int n=chess.length;
        int i=ind;
        int j=jnd;
        while(i<n && j<n){
            if(chess[i][j]=='Q')return false;
            i=i+1;
            j=j+1;
        }
        i=ind;
        j=jnd;
        while(i>=0 && j>=0){
            if(chess[i][j]=='Q')return false;
            i=i-1;
            j=j-1;
        }

        i=ind;
        j=jnd;
        while(i>=0 && j<n){
            if(chess[i][j]=='Q')return false;
            i=i-1;
            j=j+1;
        }
        i=ind;
        j=jnd;
        while(i<n && j>=0){
            if(chess[i][j]=='Q')return false;
            i=i+1;
            j=j-1;
        }

        return true;
    }

    public static  List<String>getList(char[][] chess){
        List<String> temp=new ArrayList<>();
        for(int i=0;i<chess.length;i++){
            StringBuilder st= new StringBuilder();
            for(int j=0;j<chess.length;j++){
                st.append(chess[i][j]);
            }
            temp.add(st.toString());
        }
        return temp;
    }

    public static  void arrangeQueens(int n,char[][] chess,HashMap<List<String>,Integer>ans,int i,int j,int count){
        if(count==n){
            ans.put(getList(chess),ans.getOrDefault(getList(chess),0)+1);
            return;
        }
        if(i==n){
            return;
        }
//        for(int i=ind;i<n;i++){
//            for(int j=jnd;j<n;j++){
                if(vertical(chess,j) && horizontal(chess,i) && diagnol(chess,i,j)){
                    chess[i][j]='Q';
                    if(j<n-1)arrangeQueens(n,chess,ans,i,j+1,count+1);
                    else arrangeQueens(n,chess,ans,i+1,0,count+1);
                    chess[i][j]='.';
                }

                if(j<n-1)arrangeQueens(n,chess,ans,i,j+1,count);
                else arrangeQueens(n,chess,ans,i+1,0,count);

           // }
        //}
    }


    public static void main(String[] args){
       int n=5;

        char[][] chess= new char[n][n];
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                chess[i][j]='.';
            }
        }
        List<List<String>> finalAns = new ArrayList<>();
        HashMap<List<String>,Integer> ans= new HashMap<>();
       // for(int i=0;i<n;i++){
          // chess[0][i]='Q';
            arrangeQueens(n,chess,ans,0,0,0);
            //chess[0][i]='.';
       // }

       for(Map.Entry<List<String>, Integer> m:ans.entrySet()){
           finalAns.add(m.getKey());
       }
        System.out.println(ans);


    }
}
