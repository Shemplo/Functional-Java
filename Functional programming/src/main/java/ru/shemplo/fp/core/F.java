package ru.shemplo.fp.core;

import java.util.Objects;
import java.util.function.Function;

/**
 * <p>
 * Interface that allows to check types of given arguments
 * in compile time without any additions (up to 8 arguments).
 * </p>
 * 
 * <p>
 * In Java there is no templates like in C++ and code that is
 * bellow can be generated in compile time without hardcode
 * (actually can, but it's for <b>To Do</b>). But it will cause
 * compile time error whether given type doesn't match signature.
 * </p>
 * 
 * <p>For example:</p>
 * <code>
 * 	String s = $$ (a -> a, "abc");
 * </code>
 * 
 * <p>will works properly without any errors, whereas</p>
 * 
 * <code>
 * 	String s = $$ (a -> a, 2);
 * </code>
 * 
 * <p>will be throw 
 * 	<b>Type mismatch: cannot convert from Object to String</b> error.
 * </p>
 * 
 * <p>
 * If the number of arguments is bigger than 8 then should be used
 * unchecked application of arguments {@link #$ⵑ$(F, Object...) $ⵑ$}
 * </p>
 * 
 * @author Shemplo
 *
 * @param <I> type of input argument
 * @param <O> type of computation result
 * 
 */
public interface F <I, O> extends Function <I, O> {
	
	/**
	 * Context executor. 
	 * 
	 * This method creates context for execution of passed function.
	 * 
	 * @param f function to compute
	 * @param arg argument for the function of type I
	 * @param __ rest of arguments that will be ignored
	 * @return the result of appiccation argument to function
	 * 
	 * @param <R> required return value (pass by assignment or etc.)
	 * @param <In> required input type of argument
	 * 
	 */
	public static <R, In> 
			R $$ (F <In, R> f, 
				  In arg, Object... __) {
		return (R) f.apply (arg);
	}
	
	public static <R, In, In2> 
			R $$ (F <In, F <In2, R>> f, 
				  In in, In2 in2, Object... __) {
		return $$ ($$ (f, in), in2);
	}
	
	public static <R, In, In2, In3> 
			R $$ (F <In, F <In2, F <In3, R>>> f, 
				  In in, In2 in2, In3 in3, Object... __) {
		return $$ ($$ (f, in, in2), in3);
	}
	
	public static <R, In, In2, In3, In4> 
			R $$ (F <In, F <In2, F <In3, F <In4, R>>>> f, 
				  In in, In2 in2, In3 in3, In4 in4, Object... __) {
		return $$ ($$ (f, in, in2, in3), in4);
	}
	
	public static <R, In, In2, In3, In4, In5> 
			R $$ (F <In, F <In2, F <In3, F <In4, F <In5, R>>>>> f, 
				  In in, In2 in2, In3 in3, In4 in4,  In5 in5, Object... __) {
		return $$ ($$ (f, in, in2, in3, in4), in5);
	}
	
	public static <R, In, In2, In3, In4, In5, In6> 
			R $$ (F <In, F <In2, F <In3, F <In4, F <In5, F <In6, R>>>>>> f, 
				  In in, In2 in2, In3 in3, In4 in4, In5 in5, In6 in6, Object... __) {
		return $$ ($$ (f, in, in2, in3, in4, in5), in6);
	}
	
	public static <R, In, In2, In3, In4, In5, In6, In7> 
			R $$ (F <In, F <In2, F <In3, F <In4, F <In5, F <In6, F <In7, R>>>>>>> f, 
				  In in, In2 in2, In3 in3, In4 in4, In5 in5, In6 in6, In7 in7, Object... __) {
		return $$ ($$ (f, in, in2, in3, in4, in5, in6), in7);
	}
	
	public static <R, In, In2, In3, In4, In5, In6, In7, In8> 
			R $$ (F <In, F <In2, F <In3, F <In4, F <In5, F <In6, F <In7, F <In8, R>>>>>>>> f, 
				  In in, In2 in2, In3 in3, In4 in4, In5 in5, In6 in6, In7 in7, In8 in8, Object... __) {
		return $$ ($$ (f, in, in2, in3, in4, in5, in6, in7), in8);
	}
	
	public static <R> R $ⵑ$ (F <Object, ?> f, Object... args) {
		if (Objects.isNull (args) || args.length == 0) {
			@SuppressWarnings ("unchecked") // To avoid SW on method level
			R result = (R) f.apply (null);
			return result;
		}
		
		Object tmp = f.apply (args [0]);
		for (int i = 1; i < args.length; i++) {
			if (tmp instanceof F) {
				@SuppressWarnings ("unchecked")
				F <Object, ?> ff = (F <Object, ?>) tmp;
				tmp = ff.apply (args [i]); continue;
			}
			
			break;
		}
		
		@SuppressWarnings ("unchecked")
		R result = (R) tmp;
		return result;
	}
	
}
