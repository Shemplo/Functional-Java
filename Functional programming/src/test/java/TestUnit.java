import static org.junit.Assert.*;

import java.util.Random;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import ru.shemplo.fp.core.Base;
import ru.shemplo.fp.core.F;
import ru.shemplo.fp.core.control.Functor;

public class TestUnit {

	private static class Fu <T> implements Functor <T> {

		private final T value;
		
		public Fu (T value) { this.value = value; }
		
		@Override
		@SuppressWarnings ({"rawtypes", "unchecked"})
		public <N, FN extends Functor <N>> F <F <T, N>, FN> fmap () {
			return f -> (FN) new Fu (F.$$ (f, get ()));
		}

		@Override
		public T get () { return value; }
		
	}
	
	private final Random RAND = new Random ();
	
	@Nested
	public class BaseTestUnit {
	
		@Test
		public void testId () {
			Integer number = RAND.nextInt (1000);
			Integer result = F.$$ (Base.$ (), number);
			assertTrue (number.equals (result));
		}
		
		@Test
		public void testConst () {
			String s = "test string #" + RAND.nextInt (10);
			Integer number = RAND.nextInt (1000);
			
			String result = F.$$ (Base.cst (), s, number);
			assertTrue (s.equals (result));
		}
		
		
		@Test
		public void testDot () {
			Integer number = RAND.nextInt (1000);
			F <Integer, Integer> addR = a -> a + number;
			F <Integer, String> str   = a -> "" + a;
			
			Integer a = 1000 + RAND.nextInt (1000);
			// Here is also check for partial application
			String result = F.$$ (F.$$ (Base.dot (), str, addR), a);
			String expected = "" + (a + number);
			assertTrue (expected.equals (result));
		}
		
		@Test
		public void testFlip () {
			String s = "test string #" + (char) ('a' + RAND.nextInt (10));
			Integer number = RAND.nextInt (1000);
			
			String result = F.$$ (Base.flip (), Base.concAsStr (), number, s);
			String expected = s + number;
			assertTrue (expected.equals (result));
		}
		
	}
	
}
