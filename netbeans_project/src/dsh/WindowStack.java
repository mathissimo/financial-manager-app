/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dsh;

import javax.swing.JFrame;

/**
 *
 * @author maak
 */
public class WindowStack {
    private JFrame[] windowStack;
    private int windowStackCounter=0; //countr ist fängt bei 1 an!
    private int maxWindows=10;

    public WindowStack () {
        windowStackCounter=0;
        windowStack= new JFrame [maxWindows];
    }
    public void pushOnWindowStack (JFrame newWindow) {        
        if (windowStackCounter<10) {
            windowStackCounter++;
            windowStack [windowStackCounter-1]=newWindow;            
        }
    }
    
    public JFrame popWindowStack () {
        if (windowStackCounter>0) {
            windowStackCounter--;
            return windowStack [windowStackCounter];
        }
        return windowStack [windowStackCounter];
    }
    
    public JFrame getTopWindow () {
        // System.out.println ("getTopWindow: "+windowStackCounter);
        return windowStack [windowStackCounter-1];
    }
}
