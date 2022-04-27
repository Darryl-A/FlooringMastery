 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringMasteryDao;
import com.sg.flooringmastery.dao.PersistenceException;
import com.sg.flooringmastery.dto.Order;
import java.math.BigDecimal;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author danny
 */
public class FlooringMasteryServiceLayerImplTest {
    
    private FlooringMasteryServiceLayer service;
    
    public FlooringMasteryServiceLayerImplTest() {
        FlooringMasteryDao dao = new FlooringMasteryDaoStubImpl();
        service = new FlooringMasteryServiceLayerImpl(dao);
    }

    @Test
    public void testCreateValidOrder() {
        // ARRANGE
        Order order = new Order();
        order.setDate("08/23/2022");
        order.setCustomerName("Business1, Inc.");
        order.setState("TX");
        order.setProductType("Carpet");
        order.setArea(new BigDecimal("120"));
        
        // ACT
        try{
            service.createOrder(order);
        } catch(DataValidationException | ProductValidationException | StateValidationException | PersistenceException e){
        // ASSERT    
            fail("Order was valid. No exception should have been thrown.");
        }
    }
    
    @Test
    public void testCreateOrderInvalidData() throws Exception{
        // ARRANGE
        Order order = new Order();
        order.setDate("08/23/2022");
        order.setCustomerName("");
        order.setState("TX");
        order.setProductType("Carpet");
        order.setArea(new BigDecimal("120"));
        
        // ACT
        try{
            service.createOrder(order);
            fail("Expected DataValidationException was not thrown.");
        } catch(ProductValidationException | StateValidationException | PersistenceException e){
        // ASSERT    
            fail("Incorrect exception was thrown.");
        } catch(DataValidationException e) {
            return;
        }
    }
    
    @Test
    public void testCreateOrderInvalidProduct() throws Exception{
        // ARRANGE
        Order order = new Order();
        order.setDate("08/23/2022");
        order.setCustomerName("Business1, Inc.");
        order.setState("TX");
        order.setProductType("Door");
        order.setArea(new BigDecimal("120"));
        
        // ACT
        try{
            service.createOrder(order);
            fail("Expected ProductValidationException was not thrown.");
        } catch(DataValidationException | StateValidationException | PersistenceException e){
        // ASSERT    
            fail("Incorrect exception was thrown.");
        } catch(ProductValidationException e) {
            return;
        }
    }
    
    @Test
    public void testCreateOrderInvalidTax() throws Exception{
        // ARRANGE
        Order order = new Order();
        order.setDate("08/23/2022");
        order.setCustomerName("Business1, Inc.");
        order.setState("AA");
        order.setProductType("Carpet");
        order.setArea(new BigDecimal("120"));
        
        // ACT
        try{
            service.createOrder(order);
            fail("Expected StateValidationException was not thrown.");
        } catch(ProductValidationException | DataValidationException | PersistenceException e){
        // ASSERT    
            fail("Incorrect exception was thrown.");
        } catch(StateValidationException e) {
            return;
        }
    }
    
    // TODO: getAllOrders, removeOrder Tests
    
}
