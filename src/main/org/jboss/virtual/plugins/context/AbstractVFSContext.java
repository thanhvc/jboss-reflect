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
package org.jboss.virtual.plugins.context;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.jboss.virtual.VFS;
import org.jboss.virtual.VFSUtils;
import org.jboss.virtual.VisitorAttributes;
import org.jboss.virtual.spi.VFSContext;
import org.jboss.virtual.spi.VirtualFileHandler;
import org.jboss.virtual.spi.VirtualFileHandlerVisitor;

/**
 * AbstractVFSContext.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractVFSContext implements VFSContext
{
   /** The log */
   protected final Logger log = Logger.getLogger(getClass());
   
   /** The VFS wrapper */
   private VFS vfs;
   
   /** The root url */
   private final URI rootURI;
   /** Options associated with the root URL */
   private Map<String, String> rootOptions;

   /**
    * Create a new AbstractVFSContext.
    * 
    * @param rootURI the root url
    * @throws IllegalArgumentException if rootURI is null
    */
   protected AbstractVFSContext(URI rootURI)
   {
      if (rootURI == null)
         throw new IllegalArgumentException("Null rootURI");
      this.rootURI = rootURI;
      String query = rootURI.getQuery();
      rootOptions = VFSUtils.parseURLQuery(query);
   }
   /**
    * Create a new AbstractVFSContext.
    * 
    * @param rootURL the root url
    * @throws URISyntaxException 
    * @throws IllegalArgumentException if rootURI is null
    */
   protected AbstractVFSContext(URL rootURL)
      throws URISyntaxException
   {
      this(rootURL.toURI());
   }

   public VFS getVFS()
   {
      if (vfs == null)
         vfs = new VFS(this);
      return vfs;
   }

   public URI getRootURI()
   {
      return rootURI;
   }

   public Map<String, String> getOptions()
   {
      return rootOptions;
   }

   public List<VirtualFileHandler> getChildren(VirtualFileHandler parent, boolean ignoreErrors) throws IOException
   {
      if (parent == null)
         throw new IllegalArgumentException("Null parent");
      return parent.getChildren(ignoreErrors);
   }

   public VirtualFileHandler findChild(VirtualFileHandler parent, String path) throws IOException
   {
      if (parent == null)
         throw new IllegalArgumentException("Null parent");
      if (path == null)
         throw new IllegalArgumentException("Null path");
      return parent.findChild(path);
   }

   public void visit(VirtualFileHandler handler, VirtualFileHandlerVisitor visitor) throws IOException
   {
      if (handler == null)
         throw new IllegalArgumentException("Null handler");
      if (visitor == null)
         throw new IllegalArgumentException("Null visitor");
      
      VisitorAttributes attributes = visitor.getAttributes();
      boolean includeRoot = attributes.isIncludeRoot();
      boolean includeDirs = attributes.isIncludeDirectories();
      boolean recurseDirs = attributes.isRecurseDirectories();
      boolean ignoreErrors = attributes.isIgnoreErrors();
      boolean ignoreHidden = attributes.isIgnoreHidden();

      visit(handler, visitor, includeRoot, includeDirs, recurseDirs, ignoreErrors, ignoreHidden);
   }

   /**
    * Visit. the file system, recursive death checking is left to the visitor
    * or otherwise a stack overflow.
    * 
    * @param handler the reference handler
    * @param visitor the visitor
    * @param includeRoot whether to visit the root
    * @param includeDirs whether to visit directories
    * @param recurseDirs whether to recurse into directories
    * @param ignoreErrors whether to ignore errors
    * @param ignoreHidden whether to ignore hidden files
    * @throws IOException for any problem accessing the virtual file system
    */
   protected void visit(VirtualFileHandler handler, VirtualFileHandlerVisitor visitor, boolean includeRoot, boolean includeDirs, boolean recurseDirs, boolean ignoreErrors, boolean ignoreHidden) throws IOException
   {
      // Visit the root when asked
      if (includeRoot)
         visitor.visit(handler);
      
      // Visit the children
      List<VirtualFileHandler> children;
      try
      {
          children = getChildren(handler, ignoreErrors);
      }
      catch (IOException e)
      {
         if (ignoreErrors == false)
            throw e;
         log.trace("Ignored: " + e);
         return;
      }
      
      // Look through each child
      for (VirtualFileHandler child : children)
      {
         // Ignore hidden if asked
         if (ignoreHidden && child.isHidden())
            continue;
         
         // Exclude directories if asked
         boolean isDirectory = child.isDirectory();
         if (includeDirs || isDirectory == false)
            visitor.visit(child);
         
         // Recurse into directories when asked
         if (recurseDirs && isDirectory)
         {
            try
            {
               visit(child, visitor, false, includeDirs, recurseDirs, ignoreErrors, ignoreHidden);
            }
            catch (StackOverflowError e)
            {
               log.debug("Original: " + child, e);
               throw new IOException("Stack overflow, the file system is too complicated? " + child);
            }
         }
      }
   }
   
   @Override
   public String toString()
   {
      StringBuilder buffer = new StringBuilder();
      buffer.append(getClass().getSimpleName());
      buffer.append('@');
      buffer.append(System.identityHashCode(this));
      buffer.append('[');
      buffer.append(rootURI);
      buffer.append(']');
      return buffer.toString();
   }
   
   @Override
   public int hashCode()
   {
      return rootURI.hashCode();
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null || obj instanceof VFSContext == false)
         return false;
      VFSContext other = (VFSContext) obj;
      return rootURI.equals(other.getRootURI());
   }
}
