/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dto;

import java.math.BigDecimal;

/**
 *
 * @author darrylanthony
 */
public class Tax {
    
    private String stateAbbr;
    private String stateName;
    private BigDecimal taxRate;
    
    public Tax(String stateAbbr){
        this.stateAbbr = stateAbbr;
    }

    public String getStateAbbr() {
        return stateAbbr;
    }

    public void setStateAbbr(String stateAbbr) {
        this.stateAbbr = stateAbbr;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }    

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }
    
}
