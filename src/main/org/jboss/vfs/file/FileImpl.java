/*
* JBoss, the OpenSource J2EE webOS
*
* Distributable under LGPL license.
* See terms of license at gnu.org.
*/
package org.jboss.vfs.file;

import org.jboss.vfs.spi.VirtualFile;

import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.ArrayList;

/** A java.io.File based implementation of VirtualFile
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class FileImpl
   implements VirtualFile
{
   private URL path;
   private InputStream contentIS;
   private File file;
   private VirtualFile[] children;

   public FileImpl(URL path)
   {
      this.path = path;
      this.file = new File(path.getPath());
   }
   public FileImpl(File file) throws MalformedURLException
   {
      this.path = file.toURL();
      this.file = file;
   }

   public String getName()
   {
      return path.getPath();
   }

   public VirtualFile[] getChildren()
      throws IOException
   {
      if( isDirectory() && children == null )
      {
         File[] listing = file.listFiles();
         ArrayList<VirtualFile> tmp = new ArrayList<VirtualFile>();
         for(File f : listing)
         {
            VirtualFile child = getChild(f);
            tmp.add(child);
         }
         children = new VirtualFile[tmp.size()];
         tmp.toArray(children);
      }
      return children;
   }

   public VirtualFile findChild(String name)
      throws IOException
   {
      VirtualFile child = null;
      File test = new File(file, name);
      if( test.exists() )
      {
         child = getChild(test);
      }
      else
      {
         throw new FileNotFoundException(test.getAbsolutePath()+" does not exist");
      }
      return child;
   }

   // Convience attribute accessors
   public long getLastModified()
   {
      return file.lastModified();
   }

   public long getSize()
   {
      return file.length();
   }

   public boolean isDirectory()
   {
      return file.isDirectory();
   }

   public boolean isFile()
   {
      return file.isFile();
   }

   public InputStream openStream()
      throws IOException
   {
      if( contentIS == null )
      {
         contentIS = path.openStream();
      }
      return contentIS;
   }

   public void close()
      throws IOException
   {
      if( contentIS != null )
         contentIS.close();
   }

   public URL toURL()
   {
      return path;
   }

   private VirtualFile getChild(File f)
      throws IOException
   {
      VirtualFile child;
      if( JarImpl.isJar(f.getName()) )
         child = new JarImpl(f.getAbsolutePath());
      else
         child = new FileImpl(f);
      return child;
   }
}
