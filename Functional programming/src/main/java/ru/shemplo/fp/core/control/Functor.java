package ru.shemplo.fp.core.control;

import static ru.shemplo.fp.core.F.*;

import ru.shemplo.fp.core.Base;
import ru.shemplo.fp.core.F;

public interface Functor <T> {

	// Original fmap:: (a -> b) -> f a -> f b
	// this :: f T -> (T -> N) -> Functor <N>
	public <N> F <F <T, N>, ? extends Functor <N>> fmap ();

	// Synonym of fmap: (a -> b) -> f a -> f b
	default public <N> F <F <T, N>, ? extends Functor <N>> ᐸ$ᐳ () {
		return fmap ();
	}
	
	// No original
	// T
	public T get ();

	// Original (<$):: a -> f b -> f a
	static public <A, B> F <A, F <Functor <B>, Functor <A>>> ᐸ$ () {
		return a -> fb -> $$ (fb.fmap (), $$ (Base.Ͼ (), a));
	}

	// Original ($>):: Functor f => f a -> b -> f b
	static public <A, B> F <Functor <A>, F <B, Functor <B>>> $ᐳ () {
		return fa -> b -> $$ (Base.flip (), ᐸ$ (), fa, b);
	}

	// Original (<&>):: (a -> b) -> f a -> f b
	// this :: f T -> (T -> N) -> Functor <N>
	static public <A, B> F <Functor <A>, F <F <A, B>, Functor <B>>> ᐸՖᐳ () {
		return fa -> f -> $$ (fa.fmap (), f);
	}
	
	// Original void:: f a -> f ()
	public static <A> F <Functor <A>, Functor <Object>> vd () {
		return fa -> $$ ($ᐳ (), fa, (F <?, ?>) (b -> $$ (vd (), null)));
	}

}
