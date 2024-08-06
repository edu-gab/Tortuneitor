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
    
    public boolean isEmpty(){
        return this.root == null;
    }
    
    public boolean isLeaf(){
        if(!this.isEmpty()){
            return this.root.getSi() == null && this.root.getNo() == null;
        }
        
        return false;
    }
}