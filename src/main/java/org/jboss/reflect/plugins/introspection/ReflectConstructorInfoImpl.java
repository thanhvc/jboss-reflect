/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jboss.reflect.plugins.introspection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Constructor;

import org.jboss.reflect.plugins.ConstructorInfoImpl;
import org.jboss.reflect.spi.AnnotationValue;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.ModifierInfo;
import org.jboss.reflect.spi.ParameterInfo;
import org.jboss.reflect.spi.TypeInfo;

/**
 * Constructor info
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @author <a href="mailto:adrian@jboss.org">Adrian Brock</a>
 */
public class ReflectConstructorInfoImpl extends ConstructorInfoImpl
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 2;
   
   /** The constructor */
   protected transient Constructor<?> constructor;

   /**
    * Create a new ConstructorInfo.
    */   
   public ReflectConstructorInfoImpl()
   {
   }

   /**
    * Create a new ConstructorInfo.
    * 
    * @param annotations the annotations
    * @param parameterTypes the parameter types
    * @param parameterAnnotations the parameter annotations
    * @param exceptionTypes the exception types
    * @param modifiers the modifiers
    * @param declaring the declaring class
    */
   public ReflectConstructorInfoImpl(AnnotationValue[] annotations, TypeInfo[] parameterTypes, AnnotationValue[][] parameterAnnotations, ClassInfo[] exceptionTypes, ModifierInfo modifiers, ClassInfo declaring)
   {
      super(annotations, parameterTypes, parameterAnnotations, exceptionTypes, modifiers, declaring);
   }

   /**
    * Create a new ConstructorInfo.
    * 
    * @param annotations the annotations
    * @param parameters the parameters
    * @param exceptionTypes the exception types
    * @param modifiers the modifiers
    * @param declaring the declaring class
    */
   public ReflectConstructorInfoImpl(AnnotationValue[] annotations, ParameterInfo[] parameters, ClassInfo[] exceptionTypes, ModifierInfo modifiers, ClassInfo declaring)
   {
      super(annotations, parameters, exceptionTypes, modifiers, declaring);
   }

   /**
    * Set the constructor
    * 
    * @param constructor the constructor
    */
   public void setConstructor(Constructor<?> constructor)
   {
      this.constructor = constructor;
   }

   /**
    * Get the constructor
    * 
    * @return the constructor
    */
   public Constructor<?> getConstructor()
   {
      return constructor;
   }

   @Override
   public Object newInstance(Object[] args) throws Throwable
   {
      return ReflectionUtils.newInstance(constructor, args);
   }

   /**
    * Read the object, handling constructor read.
    *
    * @param oistream the stream
    * @throws IOException io error
    * @throws ClassNotFoundException cnf error
    * @throws NoSuchMethodException no such method error
    */
   @SuppressWarnings("deprecation")
   private void readObject(ObjectInputStream oistream)
         throws IOException, ClassNotFoundException, NoSuchMethodException
   {
      oistream.defaultReadObject();
      int length = parameterTypes != null ? parameterTypes.length : 0;
      Class<?>[] classes = new Class<?>[length];
      for(int i = 0; i < length; i++)
         classes[i] = parameterTypes[i].getType();
      constructor = ReflectionUtils.findExactConstructor(getDeclaringClass().getType(), classes);
   }
}
