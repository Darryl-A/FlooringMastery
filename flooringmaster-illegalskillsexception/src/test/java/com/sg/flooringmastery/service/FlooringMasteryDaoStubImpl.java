/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringMasteryDao;
import com.sg.flooringmastery.dao.PersistenceException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author darrylanthony
 */
public class FlooringMasteryDaoStubImpl implements FlooringMasteryDao {
    
    public Order onlyOrder;
    public Product onlyProduct;
    public Tax onlyTax;

    public FlooringMasteryDaoStubImpl() {
        onlyOrder = new Order();
        onlyOrder.setOrderNumber(0);
        onlyOrder.setDate("09/03/2022");
        onlyOrder.setCustomerName("Business, Inc.");
        onlyOrder.setState("TX");
        onlyOrder.setProductType("Laminate");
        onlyOrder.setArea(new BigDecimal("120"));
        
        onlyProduct = new Product("Carpet");
        onlyProduct.setCostPerSquareFoot(new BigDecimal("2.25"));
        onlyProduct.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        
        onlyTax = new Tax("TX");
        onlyTax.setStateName("Texas");
        onlyTax.setTaxRate(new BigDecimal("45"));
        
    }

    public FlooringMasteryDaoStubImpl(Order testOrder) {
        this.onlyOrder = testOrder;
    }

    @Override
    public Order addOrder(int orderNumber, Order order) throws PersistenceException {
        if(orderNumber == onlyOrder.getOrderNumber()){
            return onlyOrder;
        } else {
            return null;
        }
    }

    @Override
    public Product getProduct(String productType) {
        if(productType.equals(onlyProduct.getProductType())){
            return onlyProduct;
        } else {
            return null;
        }
    }

    @Override
    public Tax getTax(String stateAbr) throws PersistenceException {
        if(stateAbr.equals(onlyTax.getStateAbbr())){
            return onlyTax;
        } else {
            return null;
        }
    }
    
    @Override
    public List<Tax> getTaxes() throws PersistenceException{
        List<Tax> taxList = new ArrayList<>();
        taxList.add(onlyTax);
        return taxList;
    }

    @Override
    public List<Product> getProducts() throws PersistenceException {
        List<Product> productList = new ArrayList<>();
        productList.add(onlyProduct);
        return productList;
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<>();
        orderList.add(onlyOrder);
        return orderList;
    }

    @Override
    public List<Integer> getAllOrderNumber() {
        List<Integer> orderNumberList = new ArrayList<>();
        orderNumberList.add(onlyOrder.getOrderNumber());
        return orderNumberList;
    }

    @Override
    public List<Order> findOrdersFromDate(String aDate) throws PersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Order getOrder(int orderNumber) throws PersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Order editOrder(Order order) throws PersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Order removeOrder(int orderNumber) throws PersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
