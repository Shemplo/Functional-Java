# Functional library for Java

Function programming is well of their types systems that allows 
to do many checks in compile time. Java also has same mechanism 
but in native use it's much weaker than analogues. This library
gives opportunities to do more checks in Java (mostly for generic types).

Also here will be included main concepts of functional programming:
**Functors**, **Applicatives**, **Monads** and etc.

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
   int [] array = F.$$ (Base.cst (), new int [4], "anyting"); 
   // Result: array == [0, 0, 0, 0]
   
   // flip :: (a -> b -> c) -> b -> a -> c
   String concat = $$ (Base.flip (), a -> b -> "" + a + b, "A", "B"); 
   // Result: concat == "BA"
   ```
   
   _P.S._ unfortunately in Java there is no way to avoid `()` for receiving functions
   and programmer should just take it as is.

#### _{- done as inspiration of Haskell -}_
