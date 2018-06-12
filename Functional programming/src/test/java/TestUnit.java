import static org.junit.Assert.*;
import static ru.shemplo.fp.data.Maybe.*;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import ru.shemplo.fp.core.Base;
import ru.shemplo.fp.core.F;
import ru.shemplo.fp.core.control.Applicative;
import ru.shemplo.fp.core.control.Control;
import ru.shemplo.fp.core.control.Functor;
import ru.shemplo.fp.core.control.Monad;
import ru.shemplo.fp.data.Maybe;

public class TestUnit {

	private static class Fu <T> implements Functor <T> {

		protected final T value;
		
		public Fu (T value) { this.value = value; }
		
		@Override
		@SuppressWarnings ({"rawtypes", "unchecked"})
		public <N, FN extends Functor <N>> F <F <T, N>, FN> fmap () {
			return f -> (FN) new Fu (F.$$ (f, get ()));
		}

		@Override
		public T get () { return value; }
		
	}
	
	private static class Ap <T> extends Fu <T> implements Applicative <T> {

		public Ap (T value) {
			super (value);
		}
		
		@Override
		@SuppressWarnings ({"unchecked", "rawtypes"})
	    public <N, AN extends Functor <N>> F <F <T, N>, AN> fmap () {
	        return f -> (AN) new Ap (F.$$ (f, value));
	    }

		@Override
		@SuppressWarnings ({"unchecked", "rawtypes"})
		public <B, AB extends Applicative <B>> F <B, AB> pure () {
			return b -> (AB) new Ap (b);
		}

		@Override
		public <B, AB extends Applicative <B>> F <Applicative <F <T, B>>, AB> ᐸⴲᐳ () {
			return ff -> F.$$ (pure (), ff.get ().apply (get()));
		}
		
	}
	
	private static class Mo <T> extends Ap <T> implements Monad <T> {

		public Mo (T value) {
			super (value);
		}
		
		@Override
		@SuppressWarnings ({"unchecked", "rawtypes"})
		public <B, AB extends Applicative <B>> F <B, AB> pure () {
			return b -> (AB) new Mo (b);
		}

		@Override
		public <B, MB extends Monad <B>> F <F <T, MB>, MB> bind () {
			return f -> F.$$ (f, get ());
		}
		
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
	
	@Nested
	public class FunctorTestUnit {
		
		private Fu <Integer> base;
		private Integer value;
		
		@BeforeEach
		public void init () {
			value = RAND.nextInt (1000);
			base = new Fu <> (value);
		}

		@Test
		public void testGet () {
			assertEquals (value, base.get ());
		}
		
		@Test
		public void testFMap () {
			Integer number = RAND.nextInt (1000);
			F <Integer, Integer> addR = a -> a + number;
			
			Fu <Integer> functor = F.$$ (base.fmap (), addR);
			Integer expected = value + number;
			assertEquals (expected, functor.get ());
		}
		
		@Test
		public void testReplace () {
			Integer number = RAND.nextInt (1000);
			
			Fu <Integer> updated = F.$$ (Control.$ᐳ (), base, number);
			assertEquals (number, updated.get ());
			
			updated = F.$$ (Control.ᐸ$ (), number, base);
			assertEquals (number, updated.get ());
		}
		
		@Test
		public void testApply () {
			F <Integer, String> str = a -> "" + a;

			Functor <String> result = F.$$ (Control.ᐸՖᐳ (), base, str);
			String expected = "" + value;
			assertTrue (expected.equals (result.get ()));
			
			Functor <Integer> back = F.$$ (Control.ᐸՖᐳ (), result, Integer::parseInt);
			assertEquals (value, back.get ());
		}
		
	}
	
	@Nested
	public class ApplicativeTestUnit {
		
		private Ap <Integer> base;
		private Integer value;
		
		@BeforeEach
		public void init () {
			value = RAND.nextInt (1000);
			base = new Ap <> (value);
		}
		
		@Test
		public void testPure () {
			Integer next = 1000 + RAND.nextInt (1000);
			Ap <Integer> pure = F.$$ (base.pure (), next);
			assertTrue (next.equals (pure.get ()));
		}
		
		@Test
		public void testLiftA () {
			Integer number = 1000 + RAND.nextInt (1000);
			F <Integer, Integer> addR = a -> a + number;
			
			Ap <Integer> updated = F.$$ (Control.liftA (), addR, base);
			Integer expected = value + number;
			assertEquals (expected, updated.get ());
		}
		
		@Test
		public void testInternalApplication () {
			Ap <F <Integer, int []>> init = F.$$ (base.pure (), int []::new);
			Ap <int []> array = F.$$ (Control.ᐸⴲⴲᐳ (), base, init);
			Integer length = array.get ().length;
			assertEquals (value, length);
		}
		
		@Test
		public void testReplace () {
			String s = "string #" + RAND.nextInt (100);
			Ap <String> sap = F.$$ (base.pure (), s);
			
			Ap <Integer> firts = F.$$ (Control.ᐸⴲ (), base, sap);
			assertEquals (value, firts.get ());
			
			Ap <String> second = F.$$ (Control.ⴲᐳ (), base, sap);
			assertTrue (s.equals (second.get ()));
		}
		
	}
	
	@Nested
	public class MonadTestUnit {
		
		private Mo <Integer> base;
		private Integer value;
		
		@BeforeEach
		public void init () {
			value = RAND.nextInt (1000);
			base = new Mo <> (value);
		}
		
		@Test
		public void testBind () {
			F <Integer, Monad <String>> str = a -> new Mo <> ("" + a);
			Monad <String> updated = F.$$ (base.bind (), str);
			String expected = "" + value;
			assertTrue (expected.equals (updated.get ()));
		}
		
		@Test
		public void testReplace () {
			Integer number = 1000 + RAND.nextInt (1000);
			String expected = "string #" + number;
			Mo <String> next = F.$$ (base.ret (), expected);
			Mo <String> result = F.$$ (Control.ᐳᐳ (), base, next);
			assertTrue (expected.equals (result.get ()));
		}
		
		@Test
		public void testLift () {
			Monad <int []> result = F.$$ (Control.liftM (), int []::new, base);
			Integer length = result.get ().length;
			assertEquals (value, length);
		}
		
		@Test
		public void testLift2 () {
			String s = "string #" + RAND.nextInt (100);
			Monad <String> support = F.$$ (base.ret (), s);
			F <Integer, F <String, String>> str = a -> b -> a + b;
			
			Monad <String> result = F.$$ (Control.liftM2 (), str, base, support);
			String expected = value + s;
			assertTrue (expected.equals (result.get ()));
		}
		
		@Test
		public void testAp () {
			Monad <F <Integer, int []>> init  = F.$$ (base.ret (), int []::new);
			Monad <int []> result = F.$$ (Control.ap (), init, base);
			Integer length = result.get ().length;
			assertEquals (value, length);
		}
		
	}
	
	@Nested
	public class DataTestUnit {
		
		@Nested
		public class MaybeTestUnit {
			
			private Maybe <Integer> base;
			private Integer value;
			
			@BeforeEach
			public void init () {
				value = RAND.nextInt (10000);
				base = F.$$ (just (), value);
			}
			
			@Test
			public void testInstantiate () {
				Maybe <Integer> mb = nothing ();
				assertTrue (mb instanceof Maybe <?>);
				assertNull (mb.get ());
				
				mb = F.$$ (just (), value);
				assertTrue (mb instanceof Maybe <?>);
				assertEquals (value, mb.get ());
			}
			
			@Test
			public void testBind () {
				F <Integer, Maybe <String>> str = a -> F.$$ (just (), "" + a);
				Maybe <String> result = F.$$ (base.bind (), str);
				assertTrue (result instanceof Just <?>);
				
				String expected = "" + value;
				assertTrue (expected.equals (result.get ()));
				
				expected = "Just " + value;
				assertTrue (expected.equals (result.toString ()));
				
			}
			
		}
		
	}
	
}
