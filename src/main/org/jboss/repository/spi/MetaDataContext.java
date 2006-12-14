/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors. 
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
package org.jboss.repository.spi;

import java.lang.annotation.Annotation;
import java.util.List;

import org.jboss.metadata.spi.repository.MetaDataRepository;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 58766 $
 */
public interface MetaDataContext
{

   public abstract <T extends Annotation> boolean hasAnnotation(Class<T> ann);

   public abstract <T extends Annotation> Annotation getAnnotation(Class<T> ann);

   public abstract <T extends Annotation> boolean hasAnnotationForMethod(long methodHash, Class<T> ann);

   public abstract <T extends Annotation> Annotation getAnnotationForMethod(long methodHash, Class<T> ann);

   public abstract List<Annotation> getAnnotations();

   public abstract List<Annotation> getAnnotationsForMethod(long methodHash);

   public abstract List<Annotation> getAnnotationsForMethods(long[] methodHashes);

   public abstract MetaDataRepository getRepository();

}