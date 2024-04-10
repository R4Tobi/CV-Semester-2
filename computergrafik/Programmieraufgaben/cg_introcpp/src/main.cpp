// BEGIN Hello World
//****************************** HELLO WORLD **********************************************

#include <iostream>  // <- this is an include. We use it to get functionality that is defined elsewhere
                     // In this case, we include something from the standard library
                     // "iostream" = input-output stream, we can use it to output text to the console
                     // useful for debugging :)

int main(int argc, char* argv[]){  // this is the definition for the "main" function. it is our program entry point

    std::cout << "Hello world!";   // here we use the standard library to output text
                                   // the "std::" is the prefix for the namespace of the standard library
                                   // "cout" is the standard output stream. It is NOT a function, but an object
                                   // "<<" is the stream operator. That, what is on the right side is streamed to the left side
                                   // in our case: "Hello world!" is streamed to standard output aka the console
    
    return 0;                      // the return value for our program. It is convention to return 0 to indicate
                                   // that the program did exit cleanly. We might as well return 42 
                                   // or not have a return statement at all.
}
// END Hello World

// BEGIN Variables and Functions
// ***************************** VARIABLES AND FUNCTIONS ****************************************

// #include <iostream>
// #include <string>

// A function looks something like this
/*
    <return_type> identifier(<parameter_type_1> parameter_name_1, ...){
        
        // DO STUFF
        
        return <whatever_we_want_to_return>;
    }
    
    if return type is "void" we do not need to return anything
    but we can use "return;" to exit the function
    
    we can call the function like 
       identifier();  // function without parameters
       identifier(foo, bar, ...);  // function with parameters
    where foo and bar are variables of the type of the parameters.
*/

// A variable is declared like this
/*
   <type> identifier;
   identifier = <something of type <type>>;
   
   <type> identifier = <something of type <type>>;
   
   <type> identifier = function_with_right_return_type();
*/

// the "auto" keyword
/*

auto can be used to let the compiler automatically determine the type of a variable.
For instance if a function returns an int, then the variable holding the return value can only be an int

int get_int(){
    return 0;
}
auto a = get_int();  // a is an integer

You might want to use the actual type to avoid mistakes and improve readability.
Especially in a large code base it might not be clear what the type is if you use auto
*/

// Examples
/*
void hello(){
    std::cout << "hello" << "\n";
}
 
void write_add(int a, int b){
    std::cout << a << " + " << b << " = " << a+b << "\n";
}
 
int get_best_number(){
    return 42;
}
 
int add(int a, int b){
    return a+b;
}

int main(int argc, char *argv[]){
    
    hello();
    
    int a;
    a = 333;
    int b = 29-234;
    
    write_add(a, b);
    int c = add(get_best_number(), a);
    std::cout << c << "\n";
    
    auto d = get_best_number();
    std::cout << "The best number is " << d << "\n";
    
    return 0;
}
*/
// END Variables and Functions

// BEGIN Headers and Source
//***************************** LINEAR COMPILATION OF THE PROGRAM *********************************

//#include <iostream>
//#include <string>

// THIS DOES NOT WORK!
/*
int main(int argc, char *argv[]){
    
    hello_func("dear students");  // compiler does not know hello_func
    
    return 0;
}

void hello_func(std::string who){
    std::cout << "Hello " << who << std::endl;
}

Why does it not work? 
  The C++ compiler needs to know that the function exists BEFORE it gets called.
  It does, however, not need to know what exactly it does.
  That is why we only need to know the DECLARATION of the function but not the DEFINITION.
  One often seperates declaration and definition into HEADER (.h, .hpp, ...) and SOURCE (.cpp) files respectively.
  Then we only need to include the header file, so the compiler knows that the function exists and can check,
  that the definition matches the way we call the function.
  
  See also https://stackoverflow.com/questions/1305947/why-does-c-need-a-separate-header-file
           https://stackoverflow.com/a/333964           
*/

//THIS WORKS
/*
void hello_func(std::string who){
    std::cout << "Hello " << who << std::endl;
}

int main(int argc, char *argv[]){
    
    hello_func("dear students.");  // hello_func is defined (and also declared at the same time)
    
    return 0;
}
*/

// THIS WORKS TOO
/*
//declaration
void hello_func(std::string who);

int main(int argc, char *argv[]){
    
    hello_func("dear students."); // hello_func is declared, but the definition is following after the call
    
    return 0;
}

//definition
void hello_func(std::string who){ 
    std::cout << "Hello " << who << std::endl;
}

// second definition gives us a compile error "error: redefinition of ‘void hello_func(std::string)"
//void hello_func(std::string who){ 
//    std::cout << "Hello " << who << " How is it going?" << std::endl;
//}

*/
// END Headers and Source

// BEGIN Objects
//********************************** OBJECTS, MEMORY AND POINTERS *********************************

// #include <iostream>
// #include <string>

/*
// In C++, like in Java, we have primitive data types like int, float, double, char, bool, ...
// and more complex ones, which are objects. Objects are instances of classes. For instance, to represent 
// some kind of text, we can use the "string" class from the standard library -> "std::string"

std::string text = "This is text";

// In this case, C++ automatically converts the right side of the = to an instance of std::string.
// This does not work for every type. Most times, we have to explicitly call the constructor of a class.
//E.g. :

std::string text = std::string("This is text");

// Note: we did not use the operator "new" in this case! This is different to Java, where we always use it if
// we do not have a primitive data type. "new" has another meaning in C++.

std::string* text_pointer = new std::string("This is text");
// std::cout << text_pointer;

// When you print "text_pointer" to console, you will get something like this: 0x55f797c84eb0
// This is the memory address of the string object we created with new.
// So "new" does not give us the object, but a literal POINTER to the place in memory where out object is.
// That this is a pointer can be seen by the "*" next to std::string*.
// This is called a "raw" pointer and in modern C++ we ususally do not use it anymore. We use "smart" pointers,
// but this is not relevant right now.

// To access the object at the end of this pointer, we need to DEREFERENCE it again. This can be done with the "*".
// std::cout << *text_pointer;

// As C++ does not manage memory usage like Java does. Everything that was once created with a new has to be deleted again
// when we are done with the object to avoid that our program hogs memory it does not use anymore.
// Java does this automatically and deletes unreferenced objects by running a garbage collection from time to time.

delete text_pointer;
text_pointer = nullptr;

// the nullptr is the so called null pointer. It points to an invalid memory address and is used to invalidate a pointer.
// Because even though we deleted the object at the end of the pointer, the pointer itself still points to the memory address
// Try this:
int main(int argc, char *argv[]){

    std::string* text_pointer = new std::string("This is text");

    std::cout << text_pointer << "\n";  // output: 0x559c67a9deb0
    
    delete text_pointer;                // delete string object
    
    std::cout << text_pointer;          // output is still: 0x559c67a9deb0

    return 0;
}

// We can see that the output is the same before and after the delete. In contrast, this will break:
int main(int argc, char *argv[]){

    std::string* text_pointer = new std::string("This is text");

    std::cout << *text_pointer << "\n";  // output: This is text

    delete text_pointer;                 // delete string object

    std::cout << *text_pointer;          // [1]    31776 segmentation fault (core dumped)

    return 0;
}

// As we can see, we could not access the actual object anymore.
// This managing of memory access in large programs is one of the things that are very difficult in C++.
// Smart pointers help with that because they can manage memory better.

// So why use C++ and pointers when it is so hard to manage the memory correctly?
//
// Mainly because this gives the programmer the freedom to decide what get created and deleted when and
// how data is passed around. Because of this, C++ programs are usually used when performance is critical.
// They can be optimized to the last bit, so to speak.
//
// The main advantage of pointers are that they can be passed to functions instead of the object.
// This means, we do not have to pass a potentially large object, which requires copying, which would negatively
// impact performance.



// Instead of using pointers, we can also use REFERENCES
// A reference can never be uninitialized and it can never be null. We also do not have to dereference it to use it.

// std::string& no_text; <- does not work

// We can basically use it like if we had the object itself. The benefit is, we can still edit the underlying object.

void populate_string(std::string* s_pointer){

    // This checks if the pointer is a nullptr.
    // If it is a nullpointer, we cannot assign a value to it
    if(s_pointer){
        *s_pointer = std::string("The pointer string");
    }
}
    
void populate_string(std::string& s_reference){
    s_reference = std::string("The reference string");
}
    
int main(int argc, char *argv[]){
    
    std::string text_p = std::string();
    std::string text_r = std::string();
    
    std::cout << "text_p: " << text_p << "\ntext_r: " << text_r << std::endl;
    
    std::string* text_pointer = &text_p; //the "&" takes the address of the variable
    std::string& text_ref = text_r;
    
    populate_string(text_pointer); // calls pointer variant
    populate_string(text_ref);     // calls reference variant, we do not need to take the address of the variable
    
    std::cout << "text_p: " << *text_pointer << "\ntext_r: " << text_ref;
    
    // no need to delete text_pointer because we did not use "new" to create it
     
    return 0;
}
*/
// END Objects

// BEGIN Constness
// *************************** CONST-NESS ************************************


// At times you will come across the keyword "const". "const" basically says that something is constant and 
// therefore it cannot be edited.
// A pointer can be const, meaning we cannot change the address it points to.
/*
int main(int argc, char *argv[]){
    
    const int i = 1;
    // i = 2;                    // the value of i is const, so we cannot change it
    
    int* const a = new int(1);
    // a = new int(2);           // cannot do this because the pointer is const
    *a = 2;                      // can do this because the value where a point to is not const
    
    const int* b = new int(1);   // same as int const * b = new int(1);
    // *b = 2;                   // cannot do this because the value where b points to is const
    b = new int(2);              // can do this because the pointer itself is not const
                                 // what is bad here? We changed the pointer but did not delete it before!
                                 // we cannot recover the location of the object "1" again -> cannot free this memory
                                 // anymore
    
    const int* const c = new int(1);
    // *c = 2;                   // value is const
    // c = new int(2);           // pointer is const
    
    const int& d = *a;
    // d = 3;                    // value of the reference is const -> cannot change value
    
    // int& const e = *a;        // cannot do this because the address a reference points to is const anyway
    
    return 0;
}

// Generally, we can read specifiers right to left, so in the case of
//  int const * const
// We have a constant pointer (* const) to a constant integer (int const)
// The last bit can also be written as "const int" instead. This is up to the programmer or code convention that is used.
*/
// END Constness

// BEGIN Classes
// ***************************** CLASSES ***************************************

// Check out vector.cpp and vector.h

/*
#include "vector2.h"
#include <iostream> 

using vec2 = Vector2;  // equivalent to  "typedef Vector2 vec2;"
                       // this just defines an alias for our class. So instead of always writing Vector2 we can also write vec2
                       // this makes it easy to abbreviate types that have a very long name or are complicated to type out every time

int main(int argc, char *argv[]){

    Vector2 v1{1, 2};          // could also write Vector2 v1(1, 2);
                               // there is a difference between intializing with {} and with ()
                               // see https://stackoverflow.com/questions/18222926/why-is-list-initialization-using-curly-braces-better-than-the-alternatives
    std::cout << v1 << "\n";
    
    v1.SetX(3);
    std::cout << v1 << "\n";
    
    Vector2 v2{1,1};
    
    Vector2 v3 = v1 + v2;
    std::cout << v3 << "\n";
    
    vec2 v4{0,3};
    std::cout << v4;
    
    Vector2* v_pointer = new Vector2{1, 0};
    bool is_unit = v_pointer->IsUnitLength();      // This arrow is used to access members of objects at the other end of pointers
                                                   // It is equivalent to dereferencing the pointer and then access its value
                                                   // (*v_pointer).IsUnitLength()
    if(is_unit){
        std::cout << "v_pointer is a unit vector" << "\n";
    }
    else{
        std::cout << "v_pointer is NOT a unit vector" << "\n";
    }
    
    delete v_pointer;
    
    return 0;
}
*/
// END Classes
