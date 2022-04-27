/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author darrylanthony
 */
public interface FlooringMasteryDao {
    Order addOrder(int orderNumber, Order order) throws PersistenceException;
    public Product getProduct(String productType) throws PersistenceException;
    public Tax getTax(String stateAbr) throws PersistenceException;
    public List<Product> getProducts() throws PersistenceException;
    public List<Tax> getTaxes() throws PersistenceException;
    public List<Order> getAllOrders();
    public Order getOrder(int orderNumber) throws PersistenceException;
    public List<Integer> getAllOrderNumber();
    public Order editOrder(Order order) throws PersistenceException;
    public List<Order> findOrdersFromDate(String aDate) throws PersistenceException;
    Order removeOrder(int orderNumber) throws PersistenceException;
}
