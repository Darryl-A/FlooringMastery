/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.dao.PersistenceException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.service.DataValidationException;
import com.sg.flooringmastery.service.FlooringMasteryServiceLayer;
import com.sg.flooringmastery.service.IncorrectOrderNumberException;
import com.sg.flooringmastery.service.ProductValidationException;
import com.sg.flooringmastery.service.StateValidationException;
import com.sg.flooringmastery.ui.FlooringMasteryView;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author darrylanthony
 */
public class FlooringMasteryController 
{
    private FlooringMasteryView view;
    private FlooringMasteryServiceLayer service;
    
    public FlooringMasteryController(FlooringMasteryServiceLayer service, FlooringMasteryView view)
    {
        this.service = service;
        this.view = view;
    }     
    public void run()
    {
        boolean keepGoing = true;
        int menuSelection;
        try
        {
            while(keepGoing)
            {
                menuSelection = getMenuSelection();
                switch (menuSelection) 
                {
                   case 1:
                        displayOrders();
                        break;
                    case 2:
                        addOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        exportAllData();
                        break;
                    case 6:
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();
                }
            }
            exitMessage();
        }
        catch (PersistenceException 
                | DataValidationException 
                | ProductValidationException
                | StateValidationException e)
        {
            view.displayErrorMessage(e.getMessage());
        }
    }

    private int getMenuSelection() throws DataValidationException
    {
        return view.printMenuAndGetSelection();
    }
    private void displayOrders() throws PersistenceException, DataValidationException
    {
        view.displayViewOrdersBanner();
        String orderDate = view.getOrderDateChoice();
        List<Order> orderDateList = service.getOrdersFromDate(orderDate);
    }

    private void addOrder() throws PersistenceException
    {
        boolean hasErrors = true;
        do{
            Order newOrder = view.getNewOrderInfo(service.getProducts());
            
            try{
                Order fullOrder = service.createOrder(newOrder);
                String placeOrder = view.displaySummary(fullOrder);
                
                if(placeOrder.equals("y")){
                    service.addOrder(fullOrder);
                }
                
                hasErrors = false;
                
            } catch(DataValidationException | ProductValidationException | StateValidationException e) {
                view.displayErrorMessage(e.getMessage());
                hasErrors = true;
            }

        }while(hasErrors);

    }

    private void editOrder() throws 
            PersistenceException,
            DataValidationException,
            ProductValidationException,
            StateValidationException
    {
        //Order to be edited
        Order currentOrder = null;
        Order updatedOrder = null;
        
        //declare the boolean variable
        boolean hasErrors = false;
        //Display the edit banner
        view.displayEditBanner();
        
        do{
            //Get the date from the user
            String userDate = view.getOrderDateChoice();
            int userOrderNumber = view.getOrderNumberChoice();
        
            try{
                //Get the list of all the orders from this date
                List<Order> orderList = service.getOrdersFromDate(userDate);
            
                //Loop through and check if the order exists
                if (service.orderNumberExists(orderList, userOrderNumber)){
                    currentOrder = service.getOrder(userOrderNumber);
                }
            
                //Update the customer name
                String customerName = view.editCustomerDisplay(currentOrder);
                customerName = service.isInputValid(customerName);
                updatedOrder = service.updateCustomerName(customerName,currentOrder);
            
                //Update the productType
                String productType = view.editProductTypeDisplay(currentOrder);
                productType = service.isInputValid(productType);
                
                //To make sure empty values (user presses enter) is alllowed
                if(productType != null)
                    service.checkProductExists(productType);
                updatedOrder = service.updateProductType(productType,updatedOrder, currentOrder);
                
            
                //Update the state
                String state = view.editStateDisplay(currentOrder);
                state = service.isInputValid(state);
                
                //To make sure empty values (user presses enter) is alllowed
                if(state!= null)
                    service.checkStateExists(state);
                updatedOrder = service.updateState(state, updatedOrder, currentOrder);
            
                //Update the area
                String inputArea = view.editAreaDisplay(currentOrder);
                BigDecimal newArea = service.isAreaInputValid(inputArea);
                updatedOrder = service.updateArea(newArea, updatedOrder, currentOrder);
            
                //Update the order based on new values
                updatedOrder = service.updateOrder(updatedOrder);
                
                
                //Display the order
                view.displayOrder(updatedOrder);
            
                //Ask user if they want to save the edit
                String saveEdit = view.saveEditDisplay();
            
                //If yes, save the edit
                Order confirmedOrder = service.saveEditOrder(saveEdit, updatedOrder);
            
                //Display success banner
                if(confirmedOrder != null)
                    view.displaySuccessfullEditBanner();
                
                hasErrors = false;
            } 
            catch(PersistenceException 
                    | ProductValidationException 
                    | DataValidationException
                    | StateValidationException e){
                //hasErrors = true;
                view.displayErrorMessage(e.getMessage());
                hasErrors = true;
            
            }
        }while(hasErrors);
    }

    private void removeOrder() throws 
            DataValidationException,
            PersistenceException
            
    {
        boolean hasErrors = false;
        do{
            //Order to be removed
            Order currentOrder = null;
            
            //Display the remove banner
            view.displayRemoveBanner();
        
            //Get the date from the user
            String userDate = view.getOrderDateChoice();
            int userOrderNumber = view.getOrderNumberChoice();
            
            try{
                //Get the list of all the orders from this date
                List<Order> orderList = service.getOrdersFromDate(userDate);
            
                //Loop through and check if the order exists
                if (service.orderNumberExists(orderList, userOrderNumber)){
                    currentOrder = service.getOrder(userOrderNumber);
                }
                
                //Display the order
                view.displayOrder(currentOrder);
                
                //Ask user if they want to remove and remove if user wants to
                String userChoice = view.getRemoveApproval();
                //service.removeFromMap(userDate, userOrderNumber);
                currentOrder = service.removeOrderConfirmation(userChoice, userOrderNumber);
                
                //Display banner
                //view.displaySuccessfullyRemovedBanner();
                view.displayRemovedOrder(currentOrder);
                hasErrors = false;
                
            } catch(DataValidationException 
                    | PersistenceException e) {
                view.displayErrorMessage(e.getMessage());
                hasErrors = true;
            }

        }while(hasErrors);
    }

    private void exportAllData() 
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
 
    
    private void unknownCommand() 
    {
        view.displayUnknownCommandBanner();
    }

    private void exitMessage() {
        view.displayExitBanner();
    }
}    