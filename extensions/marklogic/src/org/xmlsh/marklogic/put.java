package org.xmlsh.marklogic;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlsh.core.CoreException;
import org.xmlsh.core.InputPort;
import org.xmlsh.core.Options;
import org.xmlsh.core.XValue;
import org.xmlsh.marklogic.util.MLCommand;
import org.xmlsh.util.Util;

import com.marklogic.xcc.AdhocQuery;
import com.marklogic.xcc.Content;
import com.marklogic.xcc.ContentCreateOptions;
import com.marklogic.xcc.ContentFactory;
import com.marklogic.xcc.ContentSource;
import com.marklogic.xcc.ResultSequence;
import com.marklogic.xcc.Session;
import com.marklogic.xcc.exceptions.RequestException;

public class put extends MLCommand {

	private Session session;

	private ContentCreateOptions options;

	@Override
	public int run(List<XValue> args) throws Exception {
		
		Options opts = new Options("c=connect:,uri:,baseuri:,m=maxfiles:,r=recurse");
		opts.parse(args);
		args = opts.getRemainingArgs();
		
		String uri = opts.getOptString("uri", null);
		String baseUri = opts.getOptString("baseuri", "");
		int  maxFiles = Util.parseInt(opts.getOptString("m", "1"),1);
		boolean bRecurse = opts.hasOpt("r");
		
		
		if(! baseUri.equals("") && ! baseUri.endsWith("/") )
			baseUri = baseUri + "/";
		
		ContentSource cs = getConnection(opts);
	
		// printErr("maxfiles is " + maxFiles );

		session = cs.newSession();
		
		if( args.size() == 0 ){
			InputPort in = null;
			in = getStdin();
			
			if( uri == null )
				uri = in.getSystemId();
			this.load(in, uri);
		}
		else {
			
			int start = 0 ;
			int end = args.size() ;
			
			while( start < end ){
				int last = start + maxFiles;
				if( last > end )
					last = end ;
				
				load( args.subList(start, last), baseUri , bRecurse );
				start += maxFiles ;
				
				
			}
			
			
		
		}

		session.close();
		
		
		return 0;
	}


	/**
	 * Load the provided {@link File}s, using the provided URIs, into
	 * the content server.
	 * @param uris An array of URIs (identifiers) that correspond to the
	 *  {@link File} instances given in the "files" parameter.
	 * @param files An array of {@link File} objects representing disk
	 *  files to be loaded.  The {@link ContentCreateOptions} object
	 *  set with {@link #setOptions(com.marklogic.xcc.ContentCreateOptions)},
	 *  if any, will be applied to all documents when they are loaded.
	 * @throws RequestException If there is an unrecoverable problem
	 *  with sending the data to the server.  If this exception is
	 *  thrown, none of the documents will have been committed to the
	 *  contentbase.
	 */
	public void load (File file , String uri ) throws RequestException
	{

		Content content= ContentFactory.newContent (uri, file, options);
		

		session.insertContent (content);
	}

	public void load (InputPort port , String uri ) throws CoreException, IOException, RequestException
	{

		Content content= ContentFactory.newUnBufferedContent (uri, port.asInputStream(getSerializeOpts()), options);
		

		session.insertContent (content);
	}

	public void load (List<XValue> files , String baseUri,  boolean bRecurse ) throws CoreException, IOException, RequestException
	{
		printErr("Putting " + files.size() + " files to " + baseUri );
		List<Content>	contents = new ArrayList<Content>(files.size());
		int i = 0;
		for( XValue v : files ){	
			
			String fname = v.toString();
			File file = getFile(fname);
			String uri = baseUri + file.getName() ;

			if( file.isDirectory() ){
				if( ! bRecurse ){
					printErr("Skipping directory: " + file.getName() );
					continue;
				}
				List<XValue> sub = new ArrayList<XValue>();
				for( String fn : file.list() ){
					sub.add(new XValue(fname + "/" + fn));
				}
				createDir( uri + "/" );
				if( ! sub.isEmpty() )
					load( sub , uri + "/" , bRecurse );
				continue ;
				
			}
			Content content= ContentFactory.newContent (uri, file, options);
			contents.add(content);
		}
		
		if( ! contents.isEmpty() )
			session.insertContent (contents.toArray(new Content[ contents.size()]));
	}


	private void createDir(String uri) throws RequestException {
		printErr("Making directory: " + uri );
		AdhocQuery request = session.newAdhocQuery ("xdmp:directory-create('" + uri + "')" );
		ResultSequence rs = session.submitRequest (request);

        // writeResult(rs, out, asText );
		
	}


}



//
//
//Copyright (C) 2008,2009 , David A. Lee.
//
//The contents of this file are subject to the "Simplified BSD License" (the "License");
//you may not use this file except in compliance with the License. You may obtain a copy of the
//License at http://www.opensource.org/licenses/bsd-license.php 
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied.
//See the License for the specific language governing rights and limitations under the License.
//
//The Original Code is: all this file.
//
//The Initial Developer of the Original Code is David A. Lee
//
//Portions created by (your name) are Copyright (C) (your legal entity). All Rights Reserved.
//
//Contributor(s): none.
//
