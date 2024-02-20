import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class TestClass {


    public static void main(String args[]){

        CustomDialog dial = new CustomDialog();
        int carotte = dial.show();

        System.out.println(carotte);
    }



}

