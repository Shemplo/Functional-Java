package ru.shemplo.fp.core.control;

import static ru.shemplo.fp.core.F.*;

import ru.shemplo.fp.core.Base;
import ru.shemplo.fp.core.F;

public class Control {

	// +-------------------+
	// | Functor functions |
	// +-------------------+
	
	// Original (<$):: a -> f b -> f a
	static public <FA extends Functor <A>, FB extends Functor <B>, A, B> 
			F <A, F <FB, FA>> ᐸ$ () {
		return a -> fb -> $$ (fb.fmap (), $$ (Base.Ͼ (), a));
	}

	// Original ($>):: Functor f => f a -> b -> f b
	static public <FA extends Functor <A>, FB extends Functor <B>, A, B> 
			F <FA, F <B, FB>> $ᐳ () {
		return fa -> b -> $$ (Base.flip (), ᐸ$ (), fa, b);
	}

	// Original (<&>):: (a -> b) -> f a -> f b
	// this :: f T -> (T -> N) -> Functor <N>
	static public <FA extends Functor <A>, FB extends Functor <B>, A, B> 
			F <FA, F <F <A, B>, FB>> ᐸՖᐳ () {
		return fa -> f -> $$ (fa.fmap (), f);
	}
	
	// Original void:: f a -> f ()
	public static <FA extends Functor <A>, A> F <FA, Functor <Object>> vd () {
		return fa -> $$ ($ᐳ (), fa, (Object) null);
	}
	
	// +-----------------------+
	// | Applicative functions |
	// +-----------------------+
	
	// Original liftA2 :: (a -> b -> c) -> fa -> fb -> fc
	// f fa fb = (fmap f fa) <*> fb
	public static <AA extends Applicative <A>, AB extends Applicative <B>, AC extends Applicative <C>, A, B, C> 
			F <F <A, F <B, C>>, F <AA, F <AB, AC>>> liftA2 () {
		return f -> fa -> fb -> $$ (fb.ᐸⴲᐳ (), $$ (fa.fmap (), f));
	}
	
	// fa *> fb = (id <$ fa) <*> fb
	public static <AA extends Applicative <A>, AB extends Applicative <B>, A, B> 
			F <AA, F <AB, AB>> ⴲᐳ  () {
		return fa -> fb -> $$ (fb.ᐸⴲᐳ (), $$ (ᐸ$ (), Base.$ (), fa));
	}
	
}
