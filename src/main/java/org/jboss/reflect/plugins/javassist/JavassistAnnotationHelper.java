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
package org.jboss.reflect.plugins.javassist;

import org.jboss.reflect.plugins.AnnotationHelper;
import org.jboss.reflect.spi.AnnotationInfo;
import org.jboss.reflect.spi.AnnotationValue;

/**
 * An javassist annotation helper that uses a static delegate.<p>
 *
 * This avoids recalculating things everytime a helper is
 * constructed inside the same classloader.
 *
 * Extends JavassistTypeInfo to get access to delegate +
 * simplifies usage when TIF must also be used as AnnotationHelper.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class JavassistAnnotationHelper extends JavassistTypeInfoFactory implements AnnotationHelper
{
   public AnnotationValue[] getAnnotations(Object object)
   {
      return delegate.getAnnotations(object);
   }

   public AnnotationValue createAnnotationValue(AnnotationInfo info, Object ann)
   {
      return delegate.createAnnotationValue(info, ann);
   }
}