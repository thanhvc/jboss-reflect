/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.reflect;

/**
 * comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 */
public interface FieldData extends AnnotatedData
{
   String getName();

   ClassData getType();

   int getModifiers();

   ClassData getDeclaringClass();
}
