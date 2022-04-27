/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.PersistenceException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author darrylanthony
 */
public interface FlooringMasteryServiceLayer {
    
    Order createOrder(Order order) throws DataValidationException, ProductValidationException, StateValidationException, PersistenceException;
    void addOrder(Order order) throws PersistenceException;
    List<Product> getProducts() throws PersistenceException;
    Order updateOrder(Order order) throws PersistenceException;
    //public Order editOrder(String date, int orderNumber, Order order) throws DataValidationException, PersistenceException;
    List<Order> getOrdersFromDate(String aDate) throws DataValidationException, PersistenceException;
    public String isInputValid(String input) throws PersistenceException, DataValidationException;
    public BigDecimal isAreaInputValid(String input) throws PersistenceException, DataValidationException;
    boolean orderNumberExists(List<Order> orderList, int orderNumber) throws DataValidationException, PersistenceException;
    public Order updateCustomerName(String name, Order order) throws PersistenceException;
    public Order updateState(String state, Order updatedOrder,Order order) throws PersistenceException;
    public Order updateProductType(String productType, Order updatedOrder, Order order) throws PersistenceException,ProductValidationException;
    public Order updateArea(BigDecimal area, Order updatedOrder,Order order) throws PersistenceException;
    public Order saveEditOrder(String userChoice, Order order) throws PersistenceException;
    public Order getOrder(int orderNumber) throws DataValidationException, PersistenceException;
    public Order removeOrderConfirmation(String userChoice, int orderNumber) throws PersistenceException;
    public void checkProductExists(String userChoice) throws PersistenceException, ProductValidationException;
    public void checkStateExists(String userChoice) throws PersistenceException, StateValidationException;
}
