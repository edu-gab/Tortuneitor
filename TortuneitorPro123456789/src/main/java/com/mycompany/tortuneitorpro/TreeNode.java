/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tortuneitorpro;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Cristhian
 */
public class TreeNode {
    private String pregunta;
    private TreeNode si;
    private TreeNode no;
    private List<String> animales; // Lista de animales en el nodo hoja

    public TreeNode(String pregunta) {
        this.pregunta = pregunta;
        this.si = null;
        this.no = null;
        this.animales = new ArrayList<>();
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public TreeNode getSi() {
        return si;
    }

    public void setSi(TreeNode si) {
        this.si = si;
    }

    public TreeNode getNo() {
        return no;
    }

    public void setNo(TreeNode no) {
        this.no = no;
    }

    public List<String> getAnimales() {
        return animales;
    }

    public void setAnimales(List<String> animales) {
        this.animales = animales;
    }
}
