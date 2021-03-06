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
package org.jboss.test.beaninfo.support;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class NestedBean implements SetGetHook<NestedBean>
{
   private NestedBean bean;
   private String string;

   public NestedBean()
   {
   }

   public NestedBean getBean()
   {
      return bean;
   }

   public void doSetHook(NestedBean child)
   {
      bean = child;
   }

   public NestedBean doGetHook()
   {
      return bean;
   }

   public boolean valid()
   {
      return bean != null;
   }

   public void setBean(NestedBean bean)
   {
      this.bean = bean;
   }

   public NestedBean getDifferentGetter()
   {
      return null;
   }

   public NestedBean getOtherBean()
   {
      NestedBean other = new NestedBean();
      other.setString(string);
      return other;
   }

   public String getString()
   {
      return string;
   }

   public void setString(String string)
   {
      this.string = string;
   }
}
