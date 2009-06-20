package org.xmlsh.commands.util;

import java.util.ArrayList;

/**
 * 
 * 
 * @author David A. Lee
 * @version $Revision$
 */
public class CSVParser
{
	private char mDelim = ','; // csv
	private char mQuote = '"';

	
	
	
	
	
	public CSVParser() {
		
		
	}
	
	public CSVParser( char delim , char quote )
	{
		mDelim = delim ;
		mQuote = quote ;
	}
	
	
	/**
     * Parse a single line into String[] each string is 1 csv field
     */
     
     public CSVRecord parseLine( String line ){
        ArrayList<String>v = new ArrayList<String>();

        int len = line.length();
        char c;
        int i;
        
        StringBuffer	buf = new StringBuffer();
        for( i = 0 ; i < len ; )
        {
        	c = line.charAt(i++);
        	
        	if( c == mDelim ){
        		v.add( buf.toString());
        		buf = new StringBuffer();
        		continue;
        	}
        	if( c == mQuote ){
        		while ( i < len ){
        			c = line.charAt(i++);
        			if( c == mQuote ){
        				if( i == len || (i < len && line.charAt(i) == mDelim ) )
        					break;
        				c = line.charAt(i++);
        			}
        			buf.append(c);
        		}
        	}
        	else
        		buf.append(c);
        }
        
        if( i>0 )
        	v.add( buf.toString());
        
        
        
        

        return new CSVRecord( (String[]) v.toArray( new String[ v.size() ] )  );
        
    }


    

}
