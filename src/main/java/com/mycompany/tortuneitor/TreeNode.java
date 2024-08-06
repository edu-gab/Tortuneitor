/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tortuneitor;

/**
 *
 * @author edu-g
 */
public class TreeNode<String> {
    private String pregunta;
    private DecisionTree<String> si;
    private DecisionTree<String> no;
    private boolean esPregunta;

    public TreeNode(String pregunta, boolean esPregunta) {
        this.pregunta = pregunta;
        this.si = null;
        this.no = null;
        this.esPregunta = esPregunta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public DecisionTree<String> getSi() {
        return si;
    }

    public void setSi(DecisionTree<String> si) {
        this.si = si;
    }

    public DecisionTree<String> getNo() {
        return no;
    }

    public void setNo(DecisionTree<String> no) {
        this.no = no;
    }

    public boolean esPregunta() {
        return esPregunta;
    }

    public void setPregunta(boolean esPregunta) {
        this.esPregunta = esPregunta;
    }
    
    
}
