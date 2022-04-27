/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.ui;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author darrylanthony
 */
public class FlooringMasteryView {
    private UserIO io;
    
    public FlooringMasteryView(UserIO io)
    {
        this.io = io;
    }
    
    public int printMenuAndGetSelection() 
    {
        io.print("<<Flooring Program>>");
        io.print("1. Display Orders");
        io.print("2. Add an Order");
        io.print("3. Edit an Order");
        io.print("4. Remove an Order");
        io.print("5. Export All Data");
        io.print("6. Quit");

        return io.readInt("Please select from the above choices.", 1, 6);     
    }
    public String printDateSelection()
    {
        io.print("<<Display Orders>>");
        return io.readString("Please enter the date of the orders you wish to view.");
        
    }
    public void displayOrdersList(List<Order> orderList)
    {
        
        for(Order currentOrder : orderList)
        {
            String orderInfo = String.format(
            " Order Number: %s \n Customer Name: %s \n State: %s \n Tax Rate: %s \n"
          + " Product Type: %s \n Area: %s Cost Per Square Foot: %s \n"
          + " Labor Cost Per Square Foot: %s Material Cost: %s \n Labor Cost: %s \n"
          + " Tax: %s \n Total: %s \n \n\n",
            currentOrder.getOrderNumber(),
            currentOrder.getCustomerName(),
            currentOrder.getState(),
            currentOrder.getTaxRate(),
            currentOrder.getProductType(),
            currentOrder.getArea(),
            currentOrder.getCostPerSquareFoot(),
            currentOrder.getLaborCostPerSquareFoot(),
            currentOrder.getMaterialCost(),
            currentOrder.getLaborCost(),
            currentOrder.getTax(),
            currentOrder.getTotal()
            );
            io.print(orderInfo);
        }
        io.readString("Please hit enter to continue.");
    }
    
    public void displayRemoveBanner(){
        io.print("===== Removing =====");
    }
    
    public String getRemoveApproval(){
        return io.readString("Do you want to remove this order (Yes / No)");
    }
    
    public void displayRemovedOrder(Order orderRecord)
    {
        if(orderRecord != null)
            io.print("Order succesfully removed. ");
        else
            io.print("Order not removed.");
        io.readString("Please hit enter to continue.");
    }
    
    public void displaySuccessfullyRemovedBanner(){
        io.print("Order successfully removed!");
    }
    
    public void displayExportAllData()
    {
        io.print("== Exported All Data ===");
    }
    
    public void displayExitBanner() 
    {
        io.print("Exiting Program! Good Bye.");
    }
    public void displayUnknownCommandBanner() 
    {
        io.print("Error! Unknown Command.");
    }   
    public void displayErrorMessage(String errorMsg) 
    {
        io.print("=== ERROR ===");
        io.print(errorMsg);
    }
    
    public void displayProductList(List<Product> products){
        io.print("-----------------------------------------------------------------------------");
        io.print(String.format("%15s %10s %20s %5s %10s", "Product Type", "|", "Cost / Square ft.", "|", "Labor Cost / Square ft."));
        io.print("-----------------------------------------------------------------------------");
        for(Product currentProduct : products){
            String productInfo = String.format("%15s %10s %15s %10s %10s", currentProduct.getProductType(), "|", currentProduct.getCostPerSquareFoot(), "|", currentProduct.getLaborCostPerSquareFoot());
            io.print(productInfo);
        }
    }
    
    public Order getNewOrderInfo(List<Product> products){
        String orderDate = io.readString("Enter order date (MM/dd/yyyy):");
        String customerName = io.readString("Enter customer name:");
        String state = io.readString("Enter state abbreviation:").toUpperCase();
        
        displayProductList(products);
        
        String productType = io.readString("Select product type from list above:");
        BigDecimal area = io.readBigDecimal("Enter area:", "100");
        
        Order newOrder = new Order();
        newOrder.setDate(orderDate);
        newOrder.setCustomerName(customerName);
        newOrder.setState(state);
        newOrder.setProductType(productType.substring(0, 1).toUpperCase() + productType.substring(1).toLowerCase());
        newOrder.setArea(area);
        
        return newOrder;
    }
    
    public void displayOrder(Order currentOrder)
    {

        String orderInfo = String.format(
            " Order Number: %s \n Customer Name: %s \n State: %s \n Tax Rate: %s%% \n"
          + " Product Type: %s \n Area: %s \n Cost Per Square Foot: $%s \n"
          + " Labor Cost Per Square Foot: $%s \n Material Cost: $%s \n Labor Cost: $%s \n"
          + " Tax: $%s \n Total: $%s \n",
            currentOrder.getOrderNumber(),
            currentOrder.getCustomerName(),
            currentOrder.getState(),
            currentOrder.getTaxRate(),
            currentOrder.getProductType(),
            currentOrder.getArea(),
            currentOrder.getCostPerSquareFoot(),
            currentOrder.getLaborCostPerSquareFoot(),
            currentOrder.getMaterialCost(),
            currentOrder.getLaborCost(),
            currentOrder.getTax(),
            currentOrder.getTotal()
        );
        
        io.print(orderInfo);

    }
    
    public String displaySummary(Order newOrder){
        io.print("=== Summary ===");
        displayOrder(newOrder);
        Boolean keepGoing = true;
        String answer = "";
        do{
            answer = io.readString("Would you like to place the order? (y/n)").trim().toLowerCase();
            
            if(answer.equals("y") || answer.equals("n")){
                keepGoing = false;
            }
            
        }while(keepGoing);
        
        return answer;
    }
    
    public void displayEditBanner(){
        io.print("===== Editing Order ====");
    }
    
    public void displaySuccessfullEditBanner(){
        io.print("===== Order successfully updated! ====");
    }
    
    public String editCustomerDisplay(Order order){
        return io.readString("Enter customer name (" + order.getCustomerName() + "):");
    }
    
    public String editStateDisplay(Order order){
        return io.readString("Enter state name (" + order.getState() + "):");
    }
    
    public String editProductTypeDisplay(Order order){
        return io.readString("Enter product type (" + order.getProductType() + "):");
    }
    
    public String editAreaDisplay(Order order){
        return io.readString("Enter area (" + order.getArea() + "):");}
        
    public String saveEditDisplay(){
        return io.readString("Do you want to save the edit (Yes / No)");
    }

    public void displayViewOrdersBanner() 
    {
        io.print("=== Display Orders ===");
    }

    public String getOrderDateChoice() 
    {
        return io.readString("Please enter the Order date: ");
    }
    
    public int getOrderNumberChoice(){
        return io.readInt("Please enter the order number: ");
    }
}
