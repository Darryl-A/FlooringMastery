/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery;

import com.sg.flooringmastery.controller.FlooringMasteryController;
import com.sg.flooringmastery.dao.FlooringMasteryDao;
import com.sg.flooringmastery.dao.FlooringMasteryDaoFileImpl;
import com.sg.flooringmastery.service.FlooringMasteryServiceLayer;
import com.sg.flooringmastery.service.FlooringMasteryServiceLayerImpl;
import com.sg.flooringmastery.ui.FlooringMasteryView;
import com.sg.flooringmastery.ui.UserIO;
import com.sg.flooringmastery.ui.UserIOConsoleImpl;

/**
 *
 * @author darrylanthony
 */
public class App {
    
    public static void main(String[] args){
        UserIO myIo = new UserIOConsoleImpl();
        FlooringMasteryView myView = new FlooringMasteryView(myIo);
        FlooringMasteryDao myDao = new FlooringMasteryDaoFileImpl();
        FlooringMasteryServiceLayer myService = new FlooringMasteryServiceLayerImpl(myDao);
        FlooringMasteryController controller = new FlooringMasteryController(myService, myView);
        controller.run();
    }
    
}
