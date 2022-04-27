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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author darrylanthony
 */
public class FlooringMasteryServiceLayerImpl implements FlooringMasteryServiceLayer {
    FlooringMasteryDao dao;

    public FlooringMasteryServiceLayerImpl(FlooringMasteryDao dao) {
        this.dao = dao;
    }
    
    private void validateOrderData(Order order) throws DataValidationException {
        if(order.getCustomerName() == null
                || order.getCustomerName().isBlank()
                || order.getDate() == null
                || order.getState() == null
                || order.getState().isBlank()
                || order.getProductType()== null
                || order.getProductType().isBlank()
                || order.getArea() == null){
            throw new DataValidationException(
                    "ERROR: All fields [Order Date, Customer Name, State, Product Type, Area] are required.");
        }
        try{
            LocalDate newDate = LocalDate.parse(order.getDate(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            if(newDate.compareTo(LocalDate.now()) <= 0){
                throw new DataValidationException(
                    "ERROR: Date was not set to a future date.");
        }
        } catch(DateTimeParseException e){
            throw new DataValidationException(
                    "ERROR: Date was not entered in correct format.");
        }
        
        if(!order.getCustomerName().matches("^[a-z|A-Z|0-9|.|,| ]*$")){
            throw new DataValidationException(
                    "ERROR: Customer name can only contain letters, numbers, periods, and commas.");
        }
    }
    
    @Override
    public Order createOrder(Order order) throws DataValidationException, ProductValidationException, StateValidationException, PersistenceException {
        validateOrderData(order);
        
        Product product = dao.getProduct(order.getProductType());
        if(product == null){
            throw new ProductValidationException(
                    "ERROR: Product type was not found. Please select from the list.");
        }
        
        Tax tax = dao.getTax(order.getState());
        if(tax == null){
            throw new StateValidationException(
                    "ERROR: State abbreviation was not found.");
        }
        
        FlooringMasteryCalculations calc = new FlooringMasteryCalculations(order.getArea(), 
                product.getCostPerSquareFoot(), 
                tax.getTaxRate(), 
                product.getLaborCostPerSquareFoot());
        
        order.setCostPerSquareFoot(product.getCostPerSquareFoot());
        order.setTaxRate(tax.getTaxRate());
        order.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());
        order.setMaterialCost(calc.getMaterialCost());
        order.setLaborCost(calc.getLaborCost());
        order.setTax(calc.getTax());
        order.setTotal(calc.getTotal());
        
        List<Integer> orderNumbers = dao.getAllOrderNumber();
        
        if(orderNumbers.isEmpty()){
            order.setOrderNumber(1);
        } else {
            int max = Collections.max(orderNumbers) + 1;
            order.setOrderNumber(max);
        }
        
        return order;
    }

    @Override
    public void addOrder(Order order) throws PersistenceException{        
        dao.addOrder(order.getOrderNumber(), order);
    }

    @Override
    public List<Product> getProducts() throws PersistenceException {
        return dao.getProducts();
    }
    
    @Override 
    public Order updateOrder(Order order) throws PersistenceException{
        Product product = dao.getProduct(order.getProductType());
        Tax tax = dao.getTax(order.getState());
        if(product != null){
            FlooringMasteryCalculations calc = new FlooringMasteryCalculations(order.getArea(), 
                product.getCostPerSquareFoot(), 
                tax.getTaxRate(), 
                product.getLaborCostPerSquareFoot());
        
            order.setCostPerSquareFoot(product.getCostPerSquareFoot());
            order.setTaxRate(tax.getTaxRate());
            order.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());
            order.setMaterialCost(calc.getMaterialCost());
            order.setLaborCost(calc.getLaborCost());
            order.setTax(calc.getTax());
            order.setTotal(calc.getTotal());
        }
        return order;
    }
    
    //Function that validates the date
    public static boolean validateDate(String date) throws DataValidationException{
	if (date.trim().equals("")){
	    return true;
	}
	else
        {
	    SimpleDateFormat formatDate = new SimpleDateFormat("MM/dd/yyyy");
	    formatDate.setLenient(false);
	    try
	    {
	        Date javaDate = formatDate.parse(date);
	    }
	    /* Date format is invalid */
	    catch (ParseException e)
	    {
	        System.out.println(date +" is Invalid Date format");
	        return false;
	    }
	    /* Return true if date format is valid */
	    return true;
	}
    }
 
    @Override
    public boolean orderNumberExists(List<Order> orderList, int orderNumber) throws DataValidationException, PersistenceException{
        boolean exists = false;
        for(Order order: orderList){
            if(order.getOrderNumber() == orderNumber){
                exists = true;
                break;
            }
        }
        return exists;
    }

    @Override
    public List<Order> getOrdersFromDate(String aDate) throws DataValidationException, PersistenceException
    {
        try
        {
            LocalDate orderDate = LocalDate.parse(aDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            return dao.findOrdersFromDate(aDate);
        }
        catch(DateTimeParseException e)
        {
            throw new DataValidationException("ERROR: Date was not entered in correct format.");
        } catch (PersistenceException e) 
        {
            throw new PersistenceException(e.getMessage());
        }
    }
    
    //Checks empty input for customer name, product type, and state.
    @Override
    public String isInputValid(String input) throws PersistenceException{
        if (input == null || input.isBlank()|| input.trim().length()==0 || input.isEmpty()){
            return null;
        }
        else
            return input;   
    }
    
    @Override
    public BigDecimal isAreaInputValid(String input) throws PersistenceException{
        if (input == null || input.isBlank() || input.trim().length()==0 || input.isEmpty()){
            return null;
        }
        else
            return new BigDecimal(input);
    }
    
    //Updates the customer name
    @Override
    public Order updateCustomerName(String name,Order order) throws PersistenceException{
        if(name == null){
            return order;
        }
        else
            order.setCustomerName(name);
        return order;
    }

    //Updates the customer name
    @Override
    public Order updateState(String state, Order updatedOrder, Order order) throws PersistenceException{
        String oldState = order.getState();
        if(state != null){
            updatedOrder.setState(state);
        }
        else
            updatedOrder.setState(oldState);
        return updatedOrder;
    }
    
    //Updates the productType
    @Override
    public Order updateProductType(String productType, Order updatedOrder, Order order) throws 
            PersistenceException, ProductValidationException{
        
        String type = order.getProductType();
        Product product = dao.getProduct(order.getProductType());
        if(product == null){
            throw new ProductValidationException(
                    "ERROR: Product type was not found. Please select from the list.");
        }
        if(productType != null){
            updatedOrder.setProductType(productType);
        }
        else
            updatedOrder.setProductType(type);
        return updatedOrder;
    }
    
    //Updates the productType
    @Override
    public Order updateArea(BigDecimal area,Order updatedOrder, Order order) throws PersistenceException{
        BigDecimal oldArea = order.getArea();
        if(area != null){
            updatedOrder.setArea(area);
        }
        else
            updatedOrder.setArea(oldArea);
        return updatedOrder;
    }
    
    //Saves the order if user says yes
    @Override
    public Order saveEditOrder(String userChoice, Order order) throws PersistenceException{
        //If user wants to save
        Order updatedOrder = null;
        if(userChoice.equalsIgnoreCase("Yes")){
            updatedOrder = dao.editOrder(order);
        }
        else
            return null;
        return updatedOrder;
    }

    //Gets the order based on order number
    public Order getOrder(int orderNumber) throws DataValidationException, PersistenceException{
        return dao.getOrder(orderNumber);
    }
    
    //======= Removal Confirmation =======
    @Override
    public Order removeOrderConfirmation(String userChoice, int orderNumber)
            throws PersistenceException
    {
	//Order to be removed
	Order currentOrder  = null;
	if(userChoice.equalsIgnoreCase("Yes")){
            currentOrder = dao.removeOrder(orderNumber);
            return currentOrder;
	}
        return null;
    }
    
    @Override
    public void checkProductExists(String userChoice) 
            throws PersistenceException, ProductValidationException{
        //Store all the products in a list
        List<Product> products = dao.getProducts();
        
        //Store product name in a variable
        String productType = null;
        
        //Loop through the products and see if product type exists
        for(Product prod: products){
            if(prod.getProductType().equalsIgnoreCase(userChoice)){
                productType = prod.getProductType();
            }
        }
        if(productType == null){
            throw new ProductValidationException(
                    "ERROR: Product type was not found. Please select from the list.");
        }
    }

    @Override
    public void checkStateExists(String userChoice)
        throws PersistenceException, StateValidationException{
        //Store all the taxes in a list
        List<Tax> taxes = dao.getTaxes();
        
        //Store product name in a variable
        String stateName = null;
        
        //Loop through the products and see if product type exists
        for(Tax tax: taxes ){
            if(tax.getStateAbbr().equalsIgnoreCase(userChoice)){
                stateName = tax.getStateAbbr();
                if(stateName != null)
                    break;
            }
        }
        if(stateName == null){
            throw new StateValidationException(
                    "ERROR: State was not found. Please select from the list.");
        }
    }
}
