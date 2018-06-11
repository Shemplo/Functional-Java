package ru.shemplo.fp.core;

import static ru.shemplo.fp.core.F.*;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Objects;

public final class Base {
	
	// +-------------------------+
	// | Miscellaneous functions |
	// +-------------------------+
	
	// Original:: a -> a
	// A -> a
	private static final <A> F <A, A> _id () { return a -> a; }
	
	public static final <A> F <A, A>
		id () { return _id (); }
	
	public static final <A> F <A, A>
		$  () { return _id (); }
	
	// Original const:: a -> b -> a
	// A -> B -> A
	private static final <A, B> F <A, F <B, A>> _const () {
		return a -> b -> a;
	}
	
	public static final <A, B> F <A, F <B, A>> 
		cst () { return _const (); }
	
	public static final <A, B> F <A, F <B, A>> 
		Ͼ   () { return _const (); }
	
	// Original seq:: a -> b -> b
	// A -> B -> B
	private static final <A, B> F <A, F <B, B>> _nconst () {
		return a -> b -> b;
	}
	
	public static final <A, B> F <A, F <B, B>> 
		ncst () { return _nconst (); }

	public static final <A, B> F <A, F <B, B>> 
		Ͽ    () { return _nconst (); }
	
	// Original .:: (b -> c) -> (a -> b) -> a -> c
	// (B -> C) -> (A -> B) -> A -> C
	private static final <A, B, C> F <F <B, C>, F <F <A, B>, F <A, C>>> _dot () {
		return fc -> fb -> a -> $$ (fc, $$ (fb, a));
	}
	
	public static final <A, B, C> F <F <B, C>, F <F <A, B>, F <A, C>>>
		dot () { return _dot (); }
	
	public static final <A, B, C> F <F <B, C>, F <F <A, B>, F <A, C>>>
		ⵙ   () { return _dot (); }
	
	// Original flip:: (a -> b -> c) -> b -> a -> c
	// (A -> B -> C) -> B -> A -> C
	public static final <A, B, C> F <F <A, F <B, C>>, F <B, F <A, C>>> 
		flip () { return f -> b -> a -> $$ (f, a, b); }
	
	// No original
	// A -> B -> String
	public static final <A, B> F <A, F <B, String>> concAsStr () {
		return a -> b -> "" + a + b;
	}
	
	// +-----------------+
	// | List operations |
	// +-----------------+
	
	// Original map:: (a -> b) -> [a] -> [b]
	// (A -> B) -> [A] -> [B]
	public static final <A, B> F <F <A, B>, F <A [], B []>> amap () { 
		return f -> a -> {
			try {
				Class <?> type = a.getClass ();
				A nonNull = null;
				for (int i = 0; i < a.length; i++) {
					if (!Objects.isNull (a [i])) {
						nonNull = a [i]; break;
					}
				}
				
				if (Objects.isNull (nonNull)) {
					throw new NullPointerException ();
				}
				
				type = $$ (f, nonNull).getClass ();
				@SuppressWarnings ("unchecked")
				B [] updated = (B []) Array.newInstance (type, a.length);
				for (int i = 0; i < a.length; i++) {
					updated [i] = $$ (f, a [i]);
				}
				
				return updated;
			} catch (Exception e) { e.printStackTrace(); }
			
			return null;
		};
	}
	
	// Original map:: (a -> b) -> [a] -> [b]
	// Iterative it, Collector col => (A -> B) -> (it & col) A -> col B
	public static final <A, B, Aa extends Iterable <A> & Collection <A>> F <F <A, B>, 
		F <Aa, Collection <B>>> cmap () { 
		return f -> a -> {
			try {
				Class <?> type = a.getClass ();
				@SuppressWarnings ("unchecked") 
				Collection <B> updated = (Collection <B>) 
					type.getConstructor ().newInstance ();
				for (A value : a) { updated.add ($$ (f, value)); }

				return updated;
			} catch (Exception e) { e.printStackTrace(); }
			
			return null;
		};
	}
	
}
