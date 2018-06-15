package ru.shemplo.fp.data;

import static java.util.Objects.*;
import static ru.shemplo.fp.core.F.*;

import ru.shemplo.fp.core.F;
import ru.shemplo.fp.core.control.Applicative;
import ru.shemplo.fp.core.control.Functor;
import ru.shemplo.fp.core.control.Monad;

public abstract class Either <L, R> implements Monad <R> {
	
	protected final R RIGHT;
	protected final L LEFT;
	
	private Either (L left, R right) {
		this.RIGHT = right;
		this.LEFT = left;
	}
	
	@Override
	public R get () { return RIGHT; }
	
	public Boolean isRight_ () {
		return $$ (isRight (), this);
	}
	
	public Boolean isLeft_ () {
		return $$ (isleft (), this);
	}
	
	public static F <Either <?, ?>, Boolean> isRight () {
		return ea -> !isNull (ea.get ());
	}
	
	public static F <Either <?, ?>, Boolean> isleft () {
		return ea -> isNull (ea.get ());
	}
	
	@Override
	public <N, FN extends Functor <N>> F <F <R, N>, FN> fmap () {
		return f -> {
			FN result = null;
			if (isLeft_ ()) {
				@SuppressWarnings ("unchecked")
				FN tmp = (FN) $$ (left (), LEFT);
				result = tmp;
			} else {
				@SuppressWarnings ("unchecked")
				FN tmp = (FN) $$ (right (), $$ (f, RIGHT));
				result = tmp;
			}
			
			return result;
		};
	}
	
	public static final class Left <L, R> extends Either <L, R> {
		
		public Left (L value) {
			super (value, null);
		}
		
	}
	
	@Override
	public <B, AB extends Applicative <B>> F <B, AB> pure () {
		return v -> {
			if (isNull (v)) {
				String message = "NULL given to Right constructor";
				throw new IllegalArgumentException (message);
			}
			
			@SuppressWarnings ("unchecked")
			AB result = (AB) $$ (right (), v);
			return result;
		};
	}
	
	@Override
	public <B, MB extends Monad <B>> F <F <R, MB>, MB> bind () {
		return f -> {
			if (isLeft_ ()) {
				@SuppressWarnings ("unchecked")
				MB result = (MB) $$ (left (), LEFT);
				return result;
			} else {
				return $$ (f, get ());
			}
		};
	}
	
	// Original either :: (a -> c) -> (b -> c) -> Either a b -> c
	public static <E extends Either <A, B>, A, B, C>
			F <F <A, C>, F <F <B, C>, F <E, C>>> either () {
		return fa -> fb -> e -> 
			e.isLeft_ () ? $$ (fa, e.LEFT) : $$ (fb, e.RIGHT);
	}
	
	// Original fromLeft :: a -> Either a b -> a
	public static <E extends Either <A, B>, A, B>
			F <A, F <E, A>> fromLeft () {
		return a -> e -> (e.isLeft_ () ? e.LEFT : a);
	}
	
	// Original fromRight :: b -> Either a b -> b
	public static <E extends Either <A, B>, A, B>
			F <B, F <E, B>> fromRight () {
		return b -> e -> (e.isRight_ () ? e.RIGHT : b);
	}
	
	public static <L, R> F <L, Left <L, R>> left () {
		return l -> new Left <> (l);
	}
	
	public static final class Right <L, R> extends Either <L, R> {
		
		public Right (R value) {
			super (null, value);
		}
		
	}
	
	public static <L, R> F <R, Right <L, R>> right () {
		return r -> new Right <> (r);
	}
	
}
