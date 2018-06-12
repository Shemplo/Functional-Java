import static org.junit.Assert.*;

import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import ru.shemplo.fp.core.Base;
import ru.shemplo.fp.core.F;

public class TestUnitClass {

	@Nested
	@DisplayName ("Base functions test")
	public class BaseTests {
		
		private final Random r = new Random ();
		
		@Test
		@DisplayName ("($) :: a -> a")
		public void testId () {
			final String STR = "test string";
			String result = F.$$ (Base.$ (), STR);
			assertTrue (STR.equals (result));
		}
		
		@Test
		@DisplayName ("const :: a -> b -> a")
		public void testConst () {
			Integer value = r.nextInt (1000);
			Integer other = 1000 + r.nextInt (1000);
			
			Integer result = F.$$ (Base.cst (), value, other);
			assertTrue (value.equals (result));
		}
		
		@Test
		@DisplayName ("(.) :: (b -> c) -> (a -> b) -> a -> c")
		public void testDot () {
			int rand = r.nextInt (100);
			F <Integer, Integer> addR = a -> a + rand;
			F <Integer, String> str = a -> "" + a;
			
			Integer a = 1 + r.nextInt (10);
			String expected = "" + (a + rand);
			// Here is also test on partial application of function
			String result = F.$$ (F.$$ (Base.dot (), str, addR), a);
			assertTrue (expected.equals (result));
		}
		
	}
	
}
