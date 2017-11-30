package com.mycompany.app;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        // Generate some findbugs perhaps
        String a = "hello";
		String b = "goodbye";

		if( a = b )
		{
			System.out.println( "Hello World!" );
		}

		

		String aString = "bob";
		  b.replace('b', 'p');
		  if(b.equals("pop"))
		  	{
			System.out.println( "Hello World!" );
		}


    }
}
