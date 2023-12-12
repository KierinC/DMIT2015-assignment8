package dmit2015.faces;

import dmit2015.model.Circle;
import lombok.Getter;
import org.omnifaces.util.Messages;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.annotation.PostConstruct;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("currentCircleViewScopedView")
@ViewScoped // create this object for one HTTP request and keep in memory if the next is for the same page
// class must implement Serializable
public class CircleViewScopedView implements Serializable {

    // Declare read/write properties (field + getter + setter) for each form
    @Getter
    private Circle currentCircle = new Circle();

    // Declare read only properties (field + getter) for data sources
    @Getter
    private List<Circle> circles = new ArrayList<>();

    // Declare private fields for internal usage only objects

    @PostConstruct // This method is executed after DI is completed (fields with @Inject now have values)
    public void init() { // Use this method to initialized fields instead of a constructor
        // Code to access fields annotated with @Inject

    }

    public void onAddCircle() {
        // add the currentCircle to the list
        circles.add(currentCircle);
        // create a new Circle
        currentCircle = new Circle();
        // Add a feedback message
        Messages.addGlobalInfo("Successfully added circle to list.");
    }

    public void onCalculateArea() {
        Messages.addGlobalInfo("The area of the circle is {0}", currentCircle.area());
    }

    public void onCalculateCircumference() {
        Messages.addGlobalInfo("The circumference of the circle is {0}", currentCircle.circumference());
    }

    public void onClear() {
        // Set all fields to default values
        currentCircle = new Circle();
    }
}