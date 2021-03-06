package ru.shemplo.fp.core.control;

import ru.shemplo.fp.core.F;

public interface Applicative <T> extends Functor <T> {
	
	// Original: a -> f a
	public <B, AB extends Applicative <B>> F <B, AB> pure ();
	
	// Original (<*>) :: f (a -> b) -> f a -> f b
	// f <*> fa = fmap f fa
	default public <B, AB extends Applicative <B>> F <Applicative <F <T, B>>, AB> ᐸⴲᐳ () {
		return ff -> F.$$ (fmap (), ff.get (), get ());
	}

}
