/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author darrylanthony
 */
public class FlooringMasteryCalculations {
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal tax;
    private BigDecimal total;

    public FlooringMasteryCalculations(BigDecimal area, BigDecimal costPerSquareFoot, BigDecimal taxRate, BigDecimal laborCostPerSquareFoot) {
        BigDecimal oneHundred = new BigDecimal("100");
        
        this.materialCost = area.multiply(costPerSquareFoot).setScale(2, RoundingMode.HALF_UP);
        this.laborCost = area.multiply(laborCostPerSquareFoot).setScale(2, RoundingMode.HALF_UP);
        this.tax = (materialCost.add(laborCost)).multiply((taxRate.divide(oneHundred))).setScale(2, RoundingMode.HALF_UP);
        this.total = materialCost.add(laborCost.add(this.tax)).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

     
}
