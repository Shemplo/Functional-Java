package ru.shemplo.fp.core.control;

import static ru.shemplo.fp.core.F.*;

import ru.shemplo.fp.core.Base;
import ru.shemplo.fp.core.F;

public class Control {

	// +-------------------+
	// | Functor functions |
	// +-------------------+
	
	public static <FA extends Functor <A>, FB extends Functor <B>, A, B>
			F <F <A, B>, F <FA, FB>> fmap () {
		return f -> fa -> F.$$ (fa.fmap (), f);
	}
	
	// Original (<$):: a -> f b -> f a
	static public <FA extends Functor <A>, FB extends Functor <B>, A, B> 
			F <A, F <FB, FA>> ᐸ$ () {
		return a -> fb -> F.$$ (fb.fmap (), $$ (Base.Ͼ (), a));
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
	
	public static <AA extends Applicative <A>, AB extends Applicative <B>, A, B>
			F <AA, F <B, AB>> pure () {
		return fa -> b -> $$ (fa.pure (), b);
	}
	
	// Original liftA2 :: (a -> b -> c) -> fa -> fb -> fc
	// f fa fb = (fmap f fa) <*> fb
	public static <AA extends Applicative <A>, AB extends Applicative <B>, AC extends Applicative <C>, A, B, C> 
			F <F <A, F <B, C>>, F <AA, F <AB, AC>>> liftA2 () {
		return f -> fa -> fb -> $$ (fb.ᐸⴲᐳ (), $$ (fa.fmap (), f));
	}
	
	// Original (*>) :: f a -> f b -> f b
	// fa *> fb = (id <$ fa) <*> fb
	public static <AA extends Applicative <A>, AB extends Applicative <B>, A, B> 
			F <AA, F <AB, AB>> ⴲᐳ  () {
		return fa -> fb -> $$ (fb.ᐸⴲᐳ (), $$ (ᐸ$ (), Base.$ (), fa));
	}
	
	// Original (<*) :: f a -> f b -> f a
	// fa <* fb = liftA2 const fa fb
	public static <AA extends Applicative <A>, AB extends Applicative <B>, A, B> 
			F <AA, F <AB, AA>> ᐸⴲ () {
		return fa -> fb -> $$ (liftA2 (), Base.cst (), fa, fb);
	}
	
	// Original (<**>) :: Applicative f => f a -> f (a -> b) -> f b
	// fa (<**>) f = liftA2 (f fa)
	public static <AA extends Applicative <A>, AB extends Applicative <B>, A, B> 
			F <AA, F <Applicative <F <A, B>>, AB>> ᐸⴲⴲᐳ () {
		return fa -> f -> $$ (liftA2 (), a -> b -> $$ (b, a), fa, f);
	}
	
	// Original liftA :: Applicative f => (a -> b) -> f a -> f b
	public static <AA extends Applicative <A>, AB extends Applicative <B>, A, B> 
			F <F <A, B>, F <AA, AB>> liftA () {
		return f -> fa -> $$ (fa.ᐸⴲᐳ (), $$ (fa.pure (), f));
	}
	
	// Original liftA3 :: Applicative f => (a -> b -> c -> d) -> f a -> f b -> f c -> f d
	public static <AA extends Applicative <A>, AB extends Applicative <B>, AC extends Applicative <C>,
					AD extends Applicative <D>, A, B, C, D> 
			F <F <A, F <B, F <C, D>>>, F <AA, F <AB, F <AC, AD>>>> liftA3 () {
		return f -> fa -> fb -> fc -> $$ (fc.ᐸⴲᐳ (), $$ (liftA2 (), f, fa, fb));
	}
	
	// +-----------------+
	// | Monad functions |
	// +-----------------+
	
	public static <MA extends Monad <A>, MB extends Monad <B>, A, B>
			F <MA, F <F <A, MB>, MB>> bind () {
		return ma -> f -> $$ (ma.bind (), f);
	}
	
	// Original (>>) :: forall a b. m a -> m b -> m b
	// m >> k = m >>= \_ -> k
	public static <MA extends Monad <A>, MB extends Monad <B>, A, B>
			F <MA, F <MB, MB>> ᐳᐳ () {
		return ma -> mb -> $$ (ma.bind (), __ -> mb);
	}
	
	// Original (=<<) :: Monad m => (a -> m b) -> m a -> m b
	// f =<< x = x >>= f
	public static <MA extends Monad <A>, MB extends Monad <B>, A, B>
			F <F <A, MB>, F <MA, MB>> ᆖᐸᐸ () {
		return f -> ma -> $$ (ma.bind (), f);
	}
	
	// Original liftM :: (Monad m) => (a1 -> r) -> m a1 -> m r
	// liftM f m1 = do { x1 <- m1; return (f x1) }
	public static <MA extends Monad <A>, MB extends Monad <B>, A, B>
			F <F <A, B>, F <MA, MB>> liftM () {
		return f -> ma -> $$ (ma.ret (), $$ (f, ma.get ()));
	}
	
	// Original liftM2 :: (Monad m) => (a1 -> a2 -> r) -> m a1 -> m a2 -> m r
	public static <MA extends Monad <A>, MB extends Monad <B>, MC extends Monad <C>, A, B, C>
			F <F <A, F <B, C>>, F <MA, F <MB, MC>>> liftM2 () {
		return f -> ma -> mb -> $$ (liftM (), $$ (liftM (), f, ma).get (), mb);
	}
	
	// Original liftM3 :: (Monad m) => (a1 -> a2 -> a3 -> r) -> m a1 -> m a2 -> m a3 -> m r
	public static <MA extends Monad <A>, MB extends Monad <B>, MC extends Monad <C>, 
					MD extends Monad <D>, A, B, C, D>
			F <F <A, F <B, F <C, D>>>, F <MA, F <MB, F <MC, MD>>>> liftM3 () {
		return f -> ma -> mb -> mc -> $$ (liftM2 (), $$ (liftM (), f, ma).get (), mb, mc);
	}
	
	// Original ap :: (Monad m) => m (a -> b) -> m a -> m b
	// ap m1 m2 = do { x1 <- m1; x2 <- m2; return (x1 x2) }
	public static <MA extends Monad <A>, MB extends Monad <B>, A, B>
			F <Monad <F <A, B>>, F <MA, MB>> ap () {
		return f -> ma -> $$ (liftM (), f.get (), ma);
	}
	
}
