/**
 * $Id: $
 * $Date: $
 *
 */

package org.xmlsh.commands.internal;

import java.util.ArrayList;
import java.util.List;

import org.xmlsh.core.CommandFactory;
import org.xmlsh.core.ICommand;
import org.xmlsh.core.InputPort;
import org.xmlsh.core.Options;
import org.xmlsh.core.XCommand;
import org.xmlsh.core.XValue;
import org.xmlsh.sh.shell.SerializeOpts;
import org.xmlsh.sh.shell.Shell;
import org.xmlsh.util.DTDValidator;
import org.xmlsh.util.Util;
import org.xmlsh.util.XSDValidator;


public class xsdvalidate extends XCommand {

	
	@Override
	public int run( List<XValue> args )
	throws Exception 
	{

		
		Options opts = new Options( "psvi" , SerializeOpts.getOptionDefs() );
		opts.parse(args);
		

		
		String schema = null;
	

		
		SerializeOpts sopts = getSerializeOpts(opts);
		args= opts.getRemainingArgs();
		InputPort in = null;
		
		
		if( args.size() < 1 ){
			usage("XSD file expected");
			return -1;
			
		}
		
		schema = args.remove(0).toString();
		
		
		if( args.size() > 0 )
			in = getInput(args.get(0));
		else
			in = getStdin();
		
		if( schema != null ){
			XSDValidator v = null ; 
				
			if( schema.indexOf(' ') < 0 )
				v = new XSDValidator( getEnv().getShell().getURL(schema).toString() );
			else
				v = new XSDValidator( Util.toList( schema.split(" ") ) );
				
			v.validate( in.asInputStream(sopts));
		} 
		
		
		
		in.close();
		
		return 0;

	}



	


	

}

//
//
//Copyright (C) 2008,2009,2010 , David A. Lee.
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
