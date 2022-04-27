/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

/**
 *
 * @author darrylanthony
 */
public class IncorrectOrderNumberException extends Exception{
    public IncorrectOrderNumberException(String message) {
        super(message);
    }

    public IncorrectOrderNumberException(String message, Throwable cause) {
        super(message, cause);
    }
}
