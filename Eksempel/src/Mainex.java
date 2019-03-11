/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sveinbra
 */
public class Mainex {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here


        RegMaalCtrlex maalCtrl = new RegMaalCtrlex();
        maalCtrl.connect();
        maalCtrl.startReg(123123);
        maalCtrl.regPost(0, 0, 70);
        maalCtrl.regPost(1, 31, 100);
        maalCtrl.regPost(2, 32, 120);
        maalCtrl.regPost(3, 33, 140);
        maalCtrl.regPost(4, 34, 160);
        maalCtrl.regPost(5, 35, 180);
        maalCtrl.regPost(6, 36, 200);
        maalCtrl.regPost(7, 37, 220);
        maalCtrl.regPost(8, 150, 230);
        maalCtrl.regPost(9, 175, 245);
        if (maalCtrl.sluttReg()) {
            System.out.println("Profit!!");
        }

       
        
        
        ResultatCtrlex resultatCtrlex = new ResultatCtrlex();
        resultatCtrlex.connect();
        resultatCtrlex.printKlasseResultat("H50");
       
    }

}
