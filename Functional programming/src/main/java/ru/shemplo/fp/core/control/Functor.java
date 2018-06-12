package ru.shemplo.fp.core.control;

import ru.shemplo.fp.core.F;

public interface Functor <T> {

	// Original fmap:: (a -> b) -> f a -> f b
	// this :: f T -> (T -> N) -> Functor <N>
	public <N, FN extends Functor <N>> F <F <T, N>, FN> fmap ();

	// Synonym of fmap: (a -> b) -> f a -> f b
	default public <N, FN extends Functor <N>> F <F <T, N>, FN> ᐸ$ᐳ () {
		return fmap ();
	}
	
	// No original
	// T
	public T get ();

}
