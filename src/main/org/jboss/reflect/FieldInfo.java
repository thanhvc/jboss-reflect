/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.reflect;

import java.lang.reflect.Field;

/**
 * A field info
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @author <a href="mailto:adrian@jboss.org">Adrian Brock</a>
 */
public interface FieldInfo extends AnnotatedInfo, MemberInfo
{
   // Constants -----------------------------------------------------

   // Public --------------------------------------------------------

   /**
    * Get the name
    * 
    * @return the field name
    */
   String getName();
   
   /**
    * Get the field
    * 
    * @return the field
    */
   Field getField();
   
   /**
    * Get the field type
    * 
    * @return the field type
    */
   TypeInfo getType();

   /**
    * Get the declaring class
    * 
    * @return the declaring class
    */
   ClassInfo getDeclaringClass();
   
   // Inner classes -------------------------------------------------
}
