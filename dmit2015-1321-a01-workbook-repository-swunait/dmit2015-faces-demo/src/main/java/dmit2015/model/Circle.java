package dmit2015.model;


import lombok.Getter;
import lombok.Setter;

/**
 *This class models a Circle shape.
 *
 * @author Sam Wu
 * @version 2023.09.12
 */
public class Circle {

    @Getter @Setter
    private double radius;

//    public double getRadius() {
//        return radius;
//    }
//
//    public void setRadius(double radius) {
//        this.radius = radius;
//    }

    public Circle() {
        this.radius = 1;
    }

    public Circle(double radius) {
        this.radius = radius;
    }

    public double area() {
        return Math.PI * radius * radius;
    }

    public double circumference() {
        return 2 * Math.PI * radius;
    }
}

