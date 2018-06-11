package ru.shemplo.fp.core.control;

import ru.shemplo.fp.core.F;

public interface Applicative <A> extends Functor <A> {
	
	// Original: a -> f a
	public <B> F <B, ? extends Applicative <B>> pure ();
	
	// Original (<*>) :: f (a -> b) -> f a -> f b
	public <B> F <Applicative <F <A, B>>, ? extends Applicative <B>> ᐸⴲᐳ ();
	
}
