package ru.shemplo.fp.core.control;

import ru.shemplo.fp.core.F;

public interface Monad <T> extends Applicative <T> {

	// Original (>>=) :: forall a b. m a -> (a -> m b) -> m b
	public <B, MB extends Monad <B>> F <F <T, MB>, MB> bind ();
	
	default public <B, MB extends Monad <B>> F <F <T, MB>, MB> ᐳᐳᆖ () {
		return bind ();
	}
	
	default public <B, MB extends Monad <B>> F <B, MB> ret () {
		return pure ();
	}
	
}
