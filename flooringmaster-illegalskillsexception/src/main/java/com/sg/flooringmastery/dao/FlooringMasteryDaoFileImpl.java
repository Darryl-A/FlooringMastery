/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 *
 * @author darrylanthony
 */
public class FlooringMasteryDaoFileImpl implements FlooringMasteryDao {
    public static final String DELIMITER = ",";
    public static final String FILE_DELIMITER = "_";
    private final String DIRECTORY;
    private Map<String, Tax> taxes = new HashMap();
    private Map<String, Product> products = new HashMap();
    private Map<Integer, Order> orders = new HashMap<>();
    private Map<String, ArrayList<Integer>> dateOrderNumber = new HashMap<>();

    public FlooringMasteryDaoFileImpl(){
        this.DIRECTORY = "FileData/Orders/";
        
    }
    
    public FlooringMasteryDaoFileImpl(String dir){
        this.DIRECTORY = dir;
    }

    private void loadTaxes() throws PersistenceException{
        Scanner scanner;
        
        try{
            // Create Scanner for reading the file
            scanner = new Scanner(
                new BufferedReader(
                        new FileReader("FileData/Data/Taxes.txt")));
        } catch (FileNotFoundException e) {
            throw new PersistenceException( "-_- Could not load data into memory. " + e);
        }
        
        String currentLine;
        
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            
            String[] taxTokens = currentLine.split(DELIMITER);
            String taxStateAbr = taxTokens[0];

            Tax taxFromFile = new Tax(taxStateAbr);
            taxFromFile.setStateName(taxTokens[1]);
            taxFromFile.setTaxRate(new BigDecimal(taxTokens[2]));
            
            taxes.put(taxFromFile.getStateAbbr(), taxFromFile);
        }
        // close scanner
        scanner.close();
    }
    
    private void loadProducts() throws PersistenceException {
        Scanner scanner;
        
        try{
            // Create Scanner for reading the file
            scanner = new Scanner(
                new BufferedReader(
                        new FileReader("FileData/Data/Products.txt")));
        } catch (FileNotFoundException e) {
            throw new PersistenceException( "-_- Could not load data into memory. " + e);
        }
        
        String currentLine;
        
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            
            String[] productTokens = currentLine.split(DELIMITER);
            String productType = productTokens[0];

            Product productFromFile = new Product(productType);
            productFromFile.setCostPerSquareFoot(new BigDecimal(productTokens[1]));
            productFromFile.setLaborCostPerSquareFoot(new BigDecimal(productTokens[2]));
            
            products.put(productFromFile.getProductType(), productFromFile);
        }
        // close scanner
        scanner.close();
    }
    
    @Override
    public Order addOrder(int orderNumber, Order order) throws PersistenceException {
        loadOrders();
        Order newOrder = orders.put(orderNumber, order);
        writeOrders();
        return newOrder;
    }
    
    @Override
    public List<Product> getProducts() throws PersistenceException{
        loadProducts();
        return new ArrayList<Product>(products.values());
    }
    
    @Override
    public List<Tax> getTaxes() throws PersistenceException{
        loadTaxes();
        return new ArrayList<Tax>(taxes.values());
    }
    
    @Override
    public Product getProduct(String productType) throws PersistenceException{
        loadProducts();
        return products.get(productType);
    }
    
    @Override
    public Tax getTax(String stateAbr) throws PersistenceException{
        loadTaxes();
        return taxes.get(stateAbr);
    }
    
    private Order unmarshallOrder(String stringOrder){
        String[] orderTokens = stringOrder.split(DELIMITER);
        int orderNum = Integer.valueOf(orderTokens[0]);

        Order orderFromFile = new Order();
        orderFromFile.setOrderNumber(orderNum);
        orderFromFile.setCustomerName(orderTokens[1]);
        orderFromFile.setState(orderTokens[2]);
        orderFromFile.setTaxRate(new BigDecimal(orderTokens[3]));
        orderFromFile.setProductType(orderTokens[4]);
        orderFromFile.setArea(new BigDecimal(orderTokens[5]));
        orderFromFile.setCostPerSquareFoot(new BigDecimal(orderTokens[6]));
        orderFromFile.setLaborCostPerSquareFoot(new BigDecimal(orderTokens[7]));
        orderFromFile.setMaterialCost(new BigDecimal(orderTokens[8]));
        orderFromFile.setLaborCost(new BigDecimal(orderTokens[9]));
        orderFromFile.setTax(new BigDecimal(orderTokens[10]));
        orderFromFile.setTotal(new BigDecimal(orderTokens[11]));
        
        return orderFromFile;
    }
    
    private void loadOrders() throws PersistenceException{
        File dir = new File(DIRECTORY);
        File[] files = dir.listFiles();
        
        for(File file : files){
            Scanner scanner;

            try{
                // Create Scanner for reading the file
                scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(file)));
            } catch (FileNotFoundException e) {
                throw new PersistenceException( "-_- Could not load data into memory. " + e);
            }
            
            String currentLine;
            Order currentOrder;
        
            while (scanner.hasNextLine()) {
                currentLine = scanner.nextLine();
                currentOrder = unmarshallOrder(currentLine);
                
                String date = file.toString().split(FILE_DELIMITER)[1];
                currentOrder.setDate(date);
                //System.out.println(currentOrder);
                orders.put(currentOrder.getOrderNumber(), currentOrder);
            }
            // close scanner
            scanner.close();
        }
    }
    
    private String marshallOrder(Order order){
        //System.out.println(order);
        String orderAsString = String.valueOf(order.getOrderNumber()) + DELIMITER;
        orderAsString += order.getCustomerName() + DELIMITER;
        orderAsString += order.getState() + DELIMITER;
        orderAsString += order.getTaxRate().toString() + DELIMITER;
        orderAsString += order.getProductType() + DELIMITER;
        orderAsString += order.getArea().toString() + DELIMITER;
        orderAsString += order.getCostPerSquareFoot().toString() + DELIMITER;
        orderAsString += order.getLaborCostPerSquareFoot().toString() + DELIMITER;
        orderAsString += order.getMaterialCost().toString() + DELIMITER;
        orderAsString += order.getLaborCost().toString() + DELIMITER;
        orderAsString += order.getTax().toString() + DELIMITER;
        orderAsString += order.getTotal().toString();
                
        return orderAsString;
    }
    
    private void writeOrders() throws PersistenceException{
        
        getDateOrderNumber();
        
        for(String date : dateOrderNumber.keySet()){
            String[] dateTokens = date.split("/");
            String dateWithoutSlashes = dateTokens[0] + dateTokens[1] + dateTokens[2];
            
            String fileName = "Orders_" + dateWithoutSlashes;
            File dir = new File (DIRECTORY);
            File actualFile = new File (dir, fileName);
            
            PrintWriter out;
        
            try {
                out = new PrintWriter(new FileWriter(actualFile));
            } catch (IOException e) {
                throw new PersistenceException(
                        "Could not save data.", e);
            }

            this.dateOrderNumber.get(date).stream()
                    .forEach(currentOrder -> {String orderAsText = marshallOrder(orders.get(currentOrder));
                        out.println(orderAsText);
                        out.flush();
                    });
            // Clean up
            out.close();
            
        }

    }
    
    @Override
    public List<Order> getAllOrders(){
        return new ArrayList(orders.values());
    }
    
    @Override
    public List<Integer> getAllOrderNumber(){
        return new ArrayList(orders.keySet());
    }
    
    private void getDateOrderNumber(){
        for (Map.Entry<Integer, Order> order : orders.entrySet()) {
            
            String date = order.getValue().getDate().substring(0,2) + "/" 
                    + order.getValue().getDate().substring(2,4) + "/"
                    + order.getValue().getDate().substring(4,8);
            if (!dateOrderNumber.containsKey(date)) {
                dateOrderNumber.put(date, new ArrayList<>());
            }
            ArrayList<Integer> keys = dateOrderNumber.get(date);
            if(!keys.contains(order.getKey())){
                keys.add(order.getKey());
                dateOrderNumber.put(date, keys);
            }
        }
        
    }

    @Override
    public List<Order> findOrdersFromDate(String orderDate) throws PersistenceException 
    {
        loadOrders();
        getDateOrderNumber();
        //System.out.println(dateOrderNumber);
        //System.out.println(orders);
        List<Order> aList = new ArrayList();
        List<Integer> temp;
        if(!dateOrderNumber.containsKey(orderDate))
            throw new PersistenceException("There are no orders for the date you have entered!");
        else
        {
            temp = dateOrderNumber.get(orderDate);
            for(int orderNumber : temp)
            {
                aList.add(orders.get(orderNumber));
            }
            return aList;
        }
    }
    
    @Override
    public Order editOrder(Order order) throws PersistenceException{
        loadOrders();

        Order updatedOrder = orders.get(order.getOrderNumber());
        updatedOrder.setCustomerName(order.getCustomerName());
        updatedOrder.setState(order.getState());
        updatedOrder.setTaxRate(order.getTaxRate());
        updatedOrder.setProductType(order.getProductType());
        updatedOrder.setArea(order.getArea());
        updatedOrder.setCostPerSquareFoot(order.getCostPerSquareFoot());
        updatedOrder.setLaborCostPerSquareFoot(order.getLaborCostPerSquareFoot());
        updatedOrder.setMaterialCost(order.getMaterialCost());
        updatedOrder.setLaborCost(order.getLaborCost());
        updatedOrder.setTax(order.getTax());
        updatedOrder.setTotal(order.getTotal());
        
        orders.replace(updatedOrder.getOrderNumber(), updatedOrder);

        writeOrders();
        return updatedOrder;
    }
    
    @Override
    public Order getOrder(int orderNumber) throws PersistenceException{
        loadOrders();
        Order desiredOrder = orders.get(orderNumber);
        return desiredOrder;
    }
    
    public void removeFromMap (String date, int orderNumber){
        String dateWithSlashes = date.substring(0,2) + "/" 
                    + date.substring(2,4) + "/"
                    + date.substring(4,8);
        ArrayList<Integer> orderNumbers = dateOrderNumber.get(dateWithSlashes);
        for(Integer num: orderNumbers){
            if (num == orderNumber){
                orderNumbers.remove(num);
                break;
            }
        }
        dateOrderNumber.replace(dateWithSlashes, orderNumbers);
    }
    
    @Override
    public Order removeOrder(int orderNumber) throws PersistenceException{
        loadOrders();
        
        Order order = orders.get(orderNumber);
        String date = order.getDate();
        removeFromMap(date, orderNumber);
        Order removedOrder = orders.remove(orderNumber);
        writeOrders();
        return removedOrder;
    }
    
}
