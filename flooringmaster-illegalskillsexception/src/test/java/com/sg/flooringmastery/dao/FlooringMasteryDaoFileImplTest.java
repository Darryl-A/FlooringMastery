/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import java.math.BigDecimal;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author darrylanthony
 */
public class FlooringMasteryDaoFileImplTest {
    FlooringMasteryDao testDao;
    
    public FlooringMasteryDaoFileImplTest() {
        
        testDao = new FlooringMasteryDaoFileImpl("TestFileData/TestOrders/");
    }

    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testAddGetOrder() throws Exception {
        // ARRANGE
//        Order order = new Order();
//        order.setOrderNumber(0);
//        order.setDate("09/03/2022");
//        order.setCustomerName("Business, Inc.");
//        order.setState("TX");
//        order.setProductType("Laminate");
//        order.setArea(new BigDecimal("120"));
//        
//        // ACT
//        testDao.addOrder(order.getOrderNumber(), order);
//        Order retrievedOrder = testDao.getOrder(order.getOrderNumber());
//        
//        // ASSERT
//        assertEquals("Checking Order Number", order.getOrderNumber(), retrievedOrder.getOrderNumber());
//        assertEquals("Checking Date", order.getDate(), retrievedOrder.getDate());
//        assertEquals("Checking Customer Name", order.getCustomerName(), retrievedOrder.getCustomerName());
//        assertEquals("Checking State", order.getState(), retrievedOrder.getState());
//        assertEquals("Checking Product Type", order.getProductType(), retrievedOrder.getProductType());
//        assertEquals("Checking Area", order.getArea(), retrievedOrder.getArea());
        
    }
    
    @Test
    public void testEdit() throws Exception{
        //Arrange
        Order order = new Order();
        order.setOrderNumber(0);
        order.setDate("09/03/2022");
        order.setCustomerName("Business, Inc.");
        order.setState("TX");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("120"));
        
        Order updatedOrder = testDao.editOrder(order);
        
        //ASSERT
        assertEquals("Order number should be same", order.getOrderNumber(),updatedOrder.getOrderNumber());
        assertEquals("Date should be same", order.getDate(),updatedOrder.getDate());
        assertEquals("Customer name should be same", order.getCustomerName(),updatedOrder.getCustomerName());
        assertEquals("State should be same", order.getState(),updatedOrder.getState());
        assertEquals("Product type should be same", order.getProductType(),updatedOrder.getProductType());
        assertEquals("Area should be same", order.getArea(),updatedOrder.getArea());
        
        
    }
    
    @Test
    public void testRemove() throws Exception{
        //Arrange - create and add 2 new orders
        Order order1 = new Order();
        order1.setOrderNumber(0);
        order1.setDate("09/03/2022");
        order1.setCustomerName("Business, Inc.");
        order1.setState("TX");
        order1.setProductType("Wood");
        order1.setArea(new BigDecimal("120"));
        
        Order order2 = new Order();
        order2.setOrderNumber(1);
        order2.setDate("02/13/2022");
        order2.setCustomerName("Bank, Inc.");
        order2.setState("VA");
        order2.setProductType("Laminate");
        order2.setArea(new BigDecimal("100"));
        
        //Add both
        testDao.addOrder(order1.getOrderNumber(), order1);
        testDao.addOrder(order2.getOrderNumber(), order2);
        
        //Remove first order
        Order removedOrder = testDao.removeOrder(order1.getOrderNumber());
        
        //Check if same
        assertEquals("Both should be same", removedOrder.getOrderNumber(), order1.getOrderNumber());
        
        //Get all orders and check the size (should be 1)
        List<Order> orderList = testDao.getAllOrders();
        assertNotNull("List should not be null", orderList);
        assertEquals("After removal, size should be 1", 1, orderList.size());
        
        
        //Ensure list doesn't contain order1
        assertFalse("List shouldn't have order1", orderList.contains(order1));
        
        //It should still contain order2
        assertTrue("List should have order2", orderList.contains(order2));
        
        //Now remove the second order
        Order removedOrder2 = testDao.removeOrder(order2.getOrderNumber());
        
        //This removed order should be same as order2
        assertEquals("Both should be same", removedOrder2.getOrderNumber(), order2.getOrderNumber());
        
        //Get the list again
        orderList = testDao.getAllOrders();
        
        //Now the list should be empty
        assertTrue("List should be empty", orderList.isEmpty());
    }
    
}
