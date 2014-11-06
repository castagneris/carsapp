package com.unrc.app;

import com.unrc.app.models.Point;

import org.javalite.activejdbc.Base;
import static org.javalite.test.jspec.JSpec.the;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class PointTest {
    @Before
    public void before(){
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/carsapp_test", "root", "root");
        System.out.println("PointTest setup");
        Base.openTransaction();
    }

    @After
    public void after(){
        System.out.println("PointTest tearDown");
        Base.rollbackTransaction();
        Base.close();
    }

    @Test
    public void shouldValidateMandatoryFields(){
        Point point = new Point();

        the(point).shouldNotBe("valid");
        the(point.errors().get("point")).shouldBeEqual("value is missing");
        
        
        point.set("point",5);

        the(point).shouldBe("valid");
    } 
}
