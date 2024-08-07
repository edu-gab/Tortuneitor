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

    public TreeNode(String pregunta) {
        this.pregunta = pregunta;
        this.si = null;
        this.no = null;
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
    
}
