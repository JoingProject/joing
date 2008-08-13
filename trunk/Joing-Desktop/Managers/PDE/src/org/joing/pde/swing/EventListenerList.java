package org.joing.pde.swing;

import java.io.*;
import java.util.*;
import java.lang.reflect.Array;

/**
 * A class that holds a list of EventListeners.  A single instance
 * can be used to hold all listeners (of all types).<br>
 * It is the responsiblity of the class using the
 * EventListenerList to provide type-safe API (preferably conforming
 * to the JavaBeans spec) and methods which dispatch event notification
 * methods to appropriate Event Listeners on the list.
 * <p>
 * The main benefits that this class provides are that it is relatively
 * cheap in the case of no listeners, and it provides serialization for 
 * event-listener lists in a single place, as well as a degree of MT safety
 * (when used correctly).
 * <p>
 * Usage example:
 * Say one is defining a class that sends out FooEvents, and one wants
 * to allow users of the class to register FooListeners and receive 
 * notification when FooEvents occur. The following code should be added
 * to the class definition:
 * <pre>
 * EventListenerList listenerList = new EventListenerList();
 * FooEvent          fooEvent     = null;
 *
 * public void addFooListener(FooListener l)
 * {
 *     listenerList.add(FooListener.class, l);
 * }
 *
 * public void removeFooListener(FooListener l)
 * {
 *     listenerList.remove(FooListener.class, l);
 * }
 *
 * // Notify all listeners that have registered interest for
 * // notification on this event type.  The event instance 
 * // is lazily created using the parameters passed into 
 * // the fire method.
 *
 * protected void fireFooXXX() {
 *     // Guaranteed to return a non-null array
 *     Object[] listeners = listenerList.getListenerList();
 *     // Process the listeners last to first, notifying
 *     // those that are interested in this event
 *     for (int i = listeners.length-2; i>=0; i-=2) {
 *         if (listeners[i]==FooListener.class) {
 *             // Lazily create the event:
 *             if (fooEvent == null)
 *                 fooEvent = new FooEvent(this);
 *             ((FooListener)listeners[i+1]).fooXXX(fooEvent);
 *         }
 *     }
 * }
 * </pre>
 * foo should be changed to the appropriate name, and fireFooXxx to the
 * appropriate method name. One fire method should exist for each
 * notification method in the FooListener interface.
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running
 * the same version of Swing.  As of 1.4, support for long term storage
 * of all JavaBeans<sup><font size="-2">TM</font></sup>
 * has been added to the <code>java.beans</code> package.
 * Please see {@link java.beans.XMLEncoder}.
 *
 * @version 1.37 11/17/05
 * @author Georges Saab
 * @author Hans Muller
 * @author James Gosling
 */
public class EventListenerList implements Serializable
{
    private Vector<Pair> vListeners;
    
    //------------------------------------------------------------------------//
    
    public EventListenerList()
    {
        vListeners = new Vector<Pair>();
    }

    public <T> void add( Class<T> clazz, T listener )
    {
        if( clazz.isInstance( listener ) )
            vListeners.add( new Pair( clazz, listener ) );
        else
            throw new IllegalArgumentException( "Listener " + listener + " is not of type " + clazz );
    }

    public void remove( Object listener )
    {
        for( Pair pair : vListeners )
        {
            if( pair.instance == listener )
            {
                vListeners.remove( pair );
                break;
            }
        }
    }

    public <T> T[] getListeners( Class<T> t )
    {
        int n = getListenerCount( t );
        T[] result = (T[]) Array.newInstance( t, n );
        
        n = 0;
        
        for( Pair pair : vListeners )
        {
            if( pair.clazz == t )
                result[n++] = (T) pair.instance;
        }
        
        return result;
    }
    
    public <T> int getListenerCount( Class<T> t )
    {
        int n = 0;
        
        for( Pair pair : vListeners )
        {
            if( pair.clazz == t )
                n++;
        }
        
        return n;
    }
    
    //------------------------------------------------------------------------//
    
    private final class Pair
    {
        private Class  clazz;
        private Object instance;

        <T> Pair( Class<T> clazz, T instance )
        {
            this.clazz    = clazz;
            this.instance = instance;
        }
    }
}