import java.lang.reflect.InvocationTargetException;

import io.scif.img.ImgIOException;
import net.imglib2.vigra.VigraWrapper;
import net.imglib2.vigra.exception.VigraContractException;
import net.imglib2.vigra.exception.VigraExceptionTestHelper;

public class ExceptionExample
{
	public static void catchVigraExceptions( ) 
	{

		try
		{
//			VigraWrapper.catchVigraViolationExample();
			
			VigraExceptionTestHelper.throwPrecondition();
//			VigraWrapper.throwPrecondition();
				
//			VigraWrapper.throwPostcondition();
//			VigraWrapper.throwInvariant();
		}
		catch(VigraContractException ve){
			System.out.println(" Vigra exception ");
			ve.printStackTrace();
		}
		catch(InvocationTargetException ex) // see if this ever comes up..
		{
			System.out.println("caught ITE");
			 Throwable cause = ex.getCause();
		        if(cause == null) {
		            throw new IllegalStateException(
		                "Got InvocationTargetException, but the cause is null.", ex);
		        } else if(cause instanceof RuntimeException) {
		            throw (RuntimeException) cause;
		        } else if(cause instanceof Exception) {
		        	System.out.println("Invocation failed with cause: " + cause);
		        } else {
		        	System.out.println("Invocation failed with error: " + cause);
		        }
		}
		catch(Exception e){
			System.out.println(" Other exception ");
			e.printStackTrace();
		}
		
		System.out.println(" If you see this, we successfully caught the vigra exception ");
		
	}

	public static void main( final String[] args )
	{
		catchVigraExceptions( );
	}


}
