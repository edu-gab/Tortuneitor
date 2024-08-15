/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tortuneitorpro;

import java.util.List;

/**
 *
 * @author Cristhian
 */
public class Animal {
    private String name;
    private List<String> responses;

    public Animal(String name, List<String> responses) {
        this.name = name;
        this.responses = responses;
    }

    public String getName() {
        return name;
    }

    public List<String> getResponses() {
        return responses;
    }

    public boolean matchesResponses(List<String> answers) {
        return responses.equals(answers);
    }
}
