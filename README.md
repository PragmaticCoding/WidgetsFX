# WidgetsFX

JavaFX Widget Tools - A toolkit to create JavaFX applications with highly efficient layout code.

## Purpose

The best way to build JavaFX screens is to hand-code them using an MVC structure, and to avoid the use of Screen Builder
and FXML.

However, attempting to implement DRY when designing JavaFX layouts can be challenging as many of the Controls require
fairly simple configuration steps to be applied to each element over and over. This library contains a number of
convience methods and classes which encapsulate a number of common operations (such as applying a StyleClass) used to
instantiate and configre a Control into a single call.

Additionally, this library is integrated with a Cascading Style Sheet which supports it. This results in consistency
across an application in look and feel, in both styling and colours. For instance, the method TextWidgets.headingText("
Some text") will return a Text object with the "heading-text" StyleClass applied to it.

A framework for applying Model-View-Controller integrated with Jacobson's Entity-Boundary-Controller is supplied.

There is nothing complicated or magical about the elements in this library, however, utilizing it will allow you to
isolate the *layout* portion of your application to be strictly layout. It will also eliminate huge amounts of the
repetitive boilerplate involved in JavaFX and allow you to insert fully configured controls into your layout directly
without instantiating them as variables. The result should be layout code which is a tiny fraction of what you would
otherwise write, and an even tiny fraction of the amount of gobble-de-gook required with FXML.

## Features

Elements of this library are built on the assumption that the correct way to design JavaFX layouts is to bind the value
properties of the various JavaFX controls to properties defined outside of the layout. Then the other elements of the
application interact with these externally defined properties, often defined within a Model. Besides minimizing coupling
between the layout and the rest of the system, this avoids the need to create any load or save routines that visit each
screen element and access its value property.

Ideally, this enables a "set it and forget it" design approach where each Node is instantiated, inserted into its parent
screen container and never referenced programmatically again.

However, JavaFX Nodes typically require some amount of styling to be applied to them, and will then need to have their
value properties bound to the external properties. JavaFX Nodes do not have constructors which allow styling and binding
to be applied, and this means that each Node generally requires four lines of code; one to instantiate it, one to bind
it, one to style it and then one to add it to a parent. In screens with many components, this quickly adds up to many,
many lines of repetitive boiler-plate code.

The core of this library consists of convenience methods to instantiate, style and bind JavaFX Nodes in a single call.
There are also methods to apply onAction() event handlers when instantiating Controls which might need them. The result
is that all of the boiler-plate is removed from the layout code, and most often, these methods can be called as
parameters to container methods to add child Nodes. This means that Nodes only need to be instantiated as variables or
fields when some particular behaviour needs to be applied to them, which is usually a very small percentage of the Nodes
on a screen.

The result is a **massive** decrease in the size and complexity of layout building classes which now contain only code
directly related to a particular screen. In addition, opportunities for programmer error are reduced, and code for
instantiation of Nodes is now removed from the need for layout testing. Styling is applied in a consistent manner
throughout the entire application.

#### TextWidgets

Text has been found to be somewhat more versatile than Label for most screen display purposes. This static class
contains a number of creation methods to instatiate bound Text objects with various stylings. It also contains creation
methods to create bound TextFields.

#### Two-Column GridPane

A common GUI pattern is to have a display, or portion of a display, consisting of a column of labels, and a column of
data/inputs. The TwoColumnGridPane simplifies this by implementing a GridPane with two columns. The left column is
right-justified, and the right column is left-justified. Methods in the class allow for easy creation of new rows
containing various kinds of right column data Nodes. These methods are implemented as decorators, providing a Fluent
stype API which would allow a complete TwoColumnGridPane to be created in a single line of layout code.

#### Model-View-Controller-Interactor

JavaFX works best with the GUI implemented using a Model-View-Controller structure. In order to integrate with external
API's and other non-GUI parts of an application, such as the persistence layer, a new class called Interactor has been
created. Technically, this can be thought of as an extention of the Controller function, but it contains all of the
business logic and interfaces to external elements of the system. By splitting this out, the Controller now contains
only functionality directly related to the operation of the GUI, and isolates the GUI from the business logic.

This structure allows the Model-View-Controller to become the Boundary in Entity-Boundary-Controller. The Interactor
becomes the Controller in EBC, and contains the Entities as Domain Objects held in variables and fields. In this way,
the MVC is no longer exposed to Domain Objects, and is completely independant of any business logic.

This functionality is defined by two abstract classes generic based on the Model, which are to be extended to create the
Controller and the Interactor. The ScreenController class contains code to invoke an implementation of the Builder
interface, which will construct a View which is an instatiation of a subclass of Region.

#### Decimal and Integer TextFields

Numeric input is a common use case for TextFields, yet they are fairly complicated to implement correctly with JavaFX.
These classes do the heavy lifting for you.

#### Buttons

A static library of methods to created Buttons with defined Event Handlers in a single call. A ButtonAction class to
automate background action combined with FXAT based screen activities.

#### Text Subclass for Data Display

A special Text subclass and styling has been created for on-screen display of data values. This class also supports an "
error" property which is linked to a Pseudo-class which will automatically apply a special error styling to the Text.

## Planned Enhancements

Classes and methods for TableView and TableColumn creation.
