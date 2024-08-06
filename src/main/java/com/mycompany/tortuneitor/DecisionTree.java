/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tortuneitor;

import java.util.Scanner;

/**
 *
 * @author edu-g
 */
public class DecisionTree<String> {
    
    private TreeNode<String> root;

    public DecisionTree() {
        this.root = null;
    }
    
    public DecisionTree(String pregunta, boolean esPregunta){
        this.root = new TreeNode<>(pregunta, esPregunta);
    }
    
    public String getPregunta(){
        return this.root.getPregunta();
    }

    public TreeNode<String> getRoot() {
        return this.root;
    }
    
    public void preguntar(){
        Scanner scanner = new Scanner(System.in);
        DecisionTree<String> actual = this;
        
        while(actual.getRoot().esPregunta()){
            System.out.println(actual.getPregunta() + "(si/no)");
            
            String respuesta = (String) scanner.nextLine().trim().toLowerCase();
            
            if(respuesta.equals("si")){
                actual = actual.getRoot().getSi();
            } else if(respuesta.equals("no")){
                actual = actual.getRoot().getNo();
            } else{
                System.out.println("Digite solo si o no");
            }
        }
        
        System.out.println("Respuesta: " + actual.getPregunta());
    }
}
