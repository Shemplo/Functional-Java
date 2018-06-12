# Functional library for Java

[![Language](https://img.shields.io/badge/language-java-red.svg)](https://github.com/Shemplo/Functional-Java/blob/master/)
[![Build Status](https://travis-ci.org/Shemplo/Functional-Java.svg?branch=master)](https://travis-ci.org/Shemplo/Functional-Java)
[![GitHub license](https://img.shields.io/github/license/Shemplo/Functional-Java.svg)](https://github.com/Shemplo/Functional-Java/blob/master/LICENSE)
[![GitHub stars](https://img.shields.io/github/stars/Shemplo/Functional-Java.svg)](https://github.com/Shemplo/Functional-Java/stargazers)


Functional programming is well of their types systems that allows 
to do many checks in compile time. Java also has same mechanism 
but in native use it's much weaker than analogues. This library
gives opportunities to do more checks in Java (mostly for generic types).

Also in functional programming is based on some mathematical theory
_Theory of Types_ or _Category Theory_. 
They have useful concepts that can decently help to avoid deep mistakes 
or they simply makes some problems easy. So, in this library are implemented
a part of them: **Functor**, **Applicative**, **Monad**, **Alternative**, etc;
and their instances such as **Maybe**, **Either**, **[]**, **NonEmpty**, etc.

## Building library

Project can be built with Maven:

> $ mvn clean packge

Packed `.jar` file will be in `target` directory.
In your project just add it to `classpath`.

Use it as a dependency in Maven or Graddle is impossible now (will be added in future).

## Examples of use

0. **Safe context**

   Everything is based on Java `Fucntion <In, Out>`s 
   which is default functional interface. In library it's
   extended by interface `F` that makes safe context for function.
   
   ```java
   // Imagine that we want to assign to String variable some value as
   // a result of execution of function :: a -> a.
   
   // The default Java variant
   Function <?, ?> $ = a -> a;
   String s = (String) $.apply ("abc"); // It's OK, no problem
                                        // But at least it needs an explicit cast Object to String
   // And another problem is when we do this
   String ss = (String) $.apply (2);    // Compiler will admit this
                                        // But in runtime it will cause ClassCastException
                                        
   // The solution in library - safe context
   String sss  = F.$$ (a -> a, "2");    // It's OK, no problem
   String ssss = F.$$ (a -> a, 2);      // During the compilation will be risen error:
                                        // Type mismatch: cannot convert from Object to String
   ```
   
   Why it's important and useful?
   
   Modern IDE for Java can intercept such errors and underline code with error.
   The programmer will be notified about error before the running code. 
   And it increases the reliability of program.
   
0. **Base functions**

   In function programming there is some set of functions that is commonly used.
   Here is example of use of some of them:
   
   ```java
   // This functions are declared in fp.core.Base class
   
   // ($) or id function :: a -> a
   Integer number = F.$$ (Base.$ (), 0x54); 
   // Result: number == 0x54
   
   // const :: a -> b -> a
   int [] array = F.$$ (Base.cst (), new int [4], "anything"); 
   // Result: array == [0, 0, 0, 0]
   
   // flip :: (a -> b -> c) -> b -> a -> c
   String concat = $$ (Base.flip (), a -> b -> "" + a + b, "A", 2); 
   // Result: concat == "2A"
   ```
   
   _P.S._ unfortunately in Java there is no way to avoid `()` for receiving functions
   and programmer should just take it as is.

0. **Functor**

   In functional programming there is a construction that named _Functor_ 
   ([haskell](http://hackage.haskell.org/package/base-4.11.1.0/docs/Data-Functor.html#t:Functor))
   that generalizes the map function on lists and uniforms action over a parameterized type.
   
   ```java
   // Functor is interface which is placed in fp.core.control.Functor
   
   // The simplest implementation can look as bellow
   public class FunctorImpl <T> implements Functor <T> {

      protected final T VALUE;
      
      public FunctorImpl (T value) {
          this.VALUE = value;
      }
      
      @Override
      public String toString () {
          String type = get ().getClass ().getSimpleName ();
          return "Functor <" + type + "> " + get ();
      }
      
      @Override
      public T get () { return VALUE; }
      
      @Override
      public <N> F <F <T, N>, ? extends Functor <N>> fmap () {
          return f -> new FunctorImpl <> (F.$$ (f, get ()));
      }
   
   }
   
   // And examples of operations on Functor
   
   import static ru.shemplo.fp.core.control.Control.*;
   import static ru.shemplo.fp.core.F.*;
   
   {
       Functor <Integer> base = new FunctorImpl <> (28);               // New instance of Functor
       Functor <String> str1  = $$ (ᐸ$ (), "New value", base);        // Replace value in Functor (1 option)
       Functor <String> str2  = $$ ($ᐳ (), base, "New value");        // Replace value in Functor (2 option)
       Functor <int []> array = $$ (base.ᐸ$ᐳ (), int []::new);        // Applying function on value in Functor
       Functor <String> str3  = $$ (ᐸՖᐳ (), base, Objects::toString); // Applying function in given Functor
       
       System.out.println (base);  // Functor <Integer> 28
       System.out.println (str1);  // Functor <String> New value
       System.out.println (str2);  // Functor <String> New value
       System.out.println (array); // Functor <int []> [I@...
       System.out.println (str3);  // Functor <String> 28
   }
   ```
   
   For using in user's structures programmer needs only implement a `fmap`
   ([haskell](http://hackage.haskell.org/package/base-4.11.1.0/docs/Data-Functor.html#v:fmap))
   and `get` methods (`get` is not provided in Haskell but it as feature in Java).
   
0. **Applicative**

   The next construction is _Applicative_ 
   ([haskell](http://hackage.haskell.org/package/base-4.11.1.0/docs/Control-Applicative.html#v:liftA2)).
   It has more complex structure and it's something between _Functor_ and _Monad_.
   _Applicative_ extends _Functor_ and inherit all his methods + adds some more:
   
   ```java
   // Applicative is interface which is placed in fp.core.control.Applicative
   
   // The simplest implementation can look as bellow
   public class ApplicativeImpl <T> extends JFunctor <T> implements Applicative <T> {

       public ApplicativeImpl (T value) {
           super (value);
       }
       
       @Override
       public String toString () {
           String type = get ().getClass ().getSimpleName ();
           return "Applicative <" + type + "> " + get ();
       }
       
       @Override
       public <N> F <F <T, N>, Applicative <N>> fmap () {
           return f -> new ApplicativeImpl <> (F.$$ (f, VALUE));
       }
       
       @Override
       public <B> F <B, ? extends Applicative <B>> pure () {
           return b -> new ApplicativeImpl <> (b);
       }
       
       @Override
       public <B> F <Applicative <F <T, B>>, ? extends Applicative <B>> ᐸⴲᐳ () {
           return ff -> F.$$ (pure (), ff.get ().apply (get()));
       }

   }
   
   // And examples of operations on Applicative
   
   import static ru.shemplo.fp.core.control.Control.*;
   import static ru.shemplo.fp.core.F.*;
   
   {
       // New instance of Applicative
       Applicative <Integer> base = new ApplicativeImpl (16);            
       // Instance from `pure` function 
       Applicative <F <Integer, Integer>> 
          pure = $$ (applicative.pure (), a -> a + 5); 
       // Applying function (not pure) on value in Applicative
       Applicative <Integer> int1 = $$ (ᐸⴲⴲᐳ (), applicative, pure);
       // Applying function (pure) on value in Applicative
       Applicative <Integer> int2 = $$ (liftA (), a -> a + 3, int1);
       
       System.out.println (base);  // Applicative <Integer> 16
       System.out.println (int1);  // Applicative <Integer> 21
       System.out.println (int2);  // Applicative <Integer> 24
   }
   ```
   
0. **Monad**

   The Monad class defines the basic operations over a monad, a concept from 
   a branch of mathematics known as category theory. Monad is an abstract datatype of actions 
   ([haskell](http://hackage.haskell.org/package/base-4.11.1.0/docs/Control-Monad.html)).
   
   ```java
   // Monad is interface which is placed in fp.core.control.Monad
   
   // The simplest implementation can look as bellow
   private static class MonadImpl <T> extends Ap <T> implements Monad <T> {

       public MonadImpl (T value) {
           super (value);
       }
       
       @Override
       public String toString () {
           String type = get ().getClass ().getSimpleName ();
           return "Monad <" + type + "> " + get ();
       }
       
       @Override
       @SuppressWarnings ({"unchecked", "rawtypes"})
       public <B, AB extends Applicative <B>> F <B, AB> pure () {
           return b -> (AB) new MonadImpl (b);
       }
       
       @Override
       public <B, MB extends Monad <B>> F <F <T, MB>, MB> bind () {
           return f -> F.$$ (f, get ());
       }
        
   }
   
   // And examples of operations on Applicative
   
   import static ru.shemplo.fp.core.control.Control.*;
   import static ru.shemplo.fp.core.F.*;
   
   {
       // New instance of Monad
       Monad <Integer> base = new Mo (44);
       // Applying bing operation
       Monad <String> str1  = $$ (base.bind (), a -> new Mo <> ("-" + a));
       // Applying liftM function with pure function
       Monad <int []> array = $$ (liftM (), int []::new, base);
       // Applying ap function with function in Monad
       Monad <String> str2  = $$ (ap (), $$ (base.ret (), Objects::toString), base);
       
       System.out.println (base);  // Monad <Integer> 44
       System.out.println (str1);  // Monad <String> -44
       System.out.println (str2);  // Monad <String> 44
       System.out.println (array); // Monad <int []> [I@...
   }
   ```

#### _{- done as inspiration of Haskell -}_
