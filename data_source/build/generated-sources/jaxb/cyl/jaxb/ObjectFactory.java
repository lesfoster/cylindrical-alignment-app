//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.03.18 at 09:14:14 PM EDT 
//


package cyl.jaxb;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cyl.jaxb package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cyl.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Cylinder }
     * 
     */
    public Cylinder createCylinder() {
        return new Cylinder();
    }

    /**
     * Create an instance of {@link Cylinder.Entity }
     * 
     */
    public Cylinder.Entity createCylinderEntity() {
        return new Cylinder.Entity();
    }

    /**
     * Create an instance of {@link Cylinder.Entity.Subentity }
     * 
     */
    public Cylinder.Entity.Subentity createCylinderEntitySubentity() {
        return new Cylinder.Entity.Subentity();
    }

    /**
     * Create an instance of {@link Cylinder.Entity.Subentity.P }
     * 
     */
    public Cylinder.Entity.Subentity.P createCylinderEntitySubentityP() {
        return new Cylinder.Entity.Subentity.P();
    }

}
