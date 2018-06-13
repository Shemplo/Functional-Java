package ru.shemplo.fp.data;

import static ru.shemplo.fp.core.F.*;
import static java.util.Objects.*;

import ru.shemplo.fp.core.F;
import ru.shemplo.fp.core.control.Applicative;
import ru.shemplo.fp.core.control.Functor;
import ru.shemplo.fp.core.control.Monad;

/**
 * <p>
 * Structure that has 2 constructors: <b>Just</b> and <b>Nothing</b>.
 * </p>
 * 
 * <p>
 * {@link Just Just} - is constructor which contains some value of type <i>T</i>.
 * Operations on this object will have some non-trivial effect.
 * </p>
 * 
 * <p>
 * {@link Nothing Nothing} - is constructor which contains no value. 
 * All operations on this object will return Nothing every time.
 * </p>
 * 
 * <p>
 * Other instances of Maybe is irrelevant and 
 * they are not provided by the Maybe convention.
 * </p>
 * 
 * @author Shemplo
 *
 * @param <T> type of value in the Maybe object
 * 
 */
public abstract class Maybe <T> implements Monad <T> {

	protected final T value;
	
	public Maybe (T value) {
		this.value = value;
	}
	
	@Override
	public T get () { return value; }
	
	public Boolean isNothing_ () {
		return $$ (isNothing (), this);
	}
	
	public static F <Maybe <?>, Boolean> isJust () {
		return ma -> !isNull (ma.get ());
	}
	
	public static F <Maybe <?>, Boolean> isNothing () {
		return ma -> isNull (ma.get ());
	}

	@Override
	public <N, FN extends Functor <N>> F <F <T, N>, FN> fmap () {
		return f -> isNothing_ ()
					? $$ (ret (), null) 
					: $$ (ret (), $$ (f, get ()));
	}
	
	@Override
	public <B, AB extends Applicative <B>> F <B, AB> pure () {
		return b -> {
			@SuppressWarnings ("unchecked") 
			AB pure = (AB) (isNull (b) 
					  ? new Nothing <> ( )
					  : new Just    <> (b));
			return pure;
		};
	}
	
	@Override
	public <B, MB extends Monad <B>> F <F <T, MB>, MB> bind () {
		return f -> isNothing_ ()
					? $$ (ret (), null) 
					: $$ (f, get ());
	}
	
	// Original maybe :: b -> (a -> b) -> Maybe a -> b
	public static <MA extends Maybe <A>, MB extends Maybe <B>, A, B>
			F <B, F <F <A, B>, F <MA, B>>> maybe () {
		return b -> f -> ma -> 
			ma.isNothing_ () ? b : $$ (f, ma.get ());
	}
	
	// Original fromJust :: Maybe a -> a
	public static <MA extends Maybe <A>, A>
			F <MA, A> fromJust () {
		return ma -> {
			if (ma.isNothing_ ()) {
				String message = "Method `fromJust` is requested on Nothing";
				throw new IllegalStateException (message);
			}
			
			return ma.get ();
		};
	}
	
	// Original fromMaybe :: a -> Maybe a -> a
	public static <MA extends Maybe <A>, A>
			F <A, F <MA, A>> fromMaybe () {
		return a -> ma -> 
			ma.isNothing_ () ? a : ma.get ();
	}
	
	public static final class Just <T> extends Maybe <T> {

		private Just (T value) { 
			super (value);
			
			if (isNull (value)) { // Checking Just constructor on valid state
				String message = "Constructor Just can't contain NULL value";
				throw new IllegalArgumentException (message);
			}
		}
		
		@Override
		public String toString () {
			return "Just " + get ();
		}
		
	}
	
	public static final class Nothing <T> extends Maybe <T> {
		
		private Nothing () { super (null); }
		
		@Override
		public String toString () {
			return "Nothing";
		}
		
	}
	
	public static <R> F <R, Just <R>> just () {
		return f -> new Just <> (f);
	}
	
	public static <R> Nothing <R> nothing () {
		return new Nothing <> ();
	}
	
}
