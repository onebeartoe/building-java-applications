
package org.onebeartoe.building.applications.parent.child.second.grandchild;

/**
 * @author Roberto Marquez (http://electronics.onebeartoe.org/)
 */
public class HelloWorldWithLambdas
{
    /**
     * This is 'Hello World' implementation using Java 8 Lambdas and Streams
     *
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        String message = "Hello Lambdas and Streams World!";

        // not so functional implementation with long notation (the curly braces)
        message.chars()
                .forEach( i ->
                {
                    System.out.print( (char) i  );
                } );
        System.out.println();
        
        // using mapping with long notation and a method reference to print() in System.out
        message.chars()
                .mapToObj( i -> { return (char) i; } )
                .forEach(System.out::print);        
        System.out.println();

        // using mapping, short notation, and the method reference again
        message.chars()
                .mapToObj( i ->  (char) i  )
                .forEach(System.out::print);        
        System.out.println();
    }
}
