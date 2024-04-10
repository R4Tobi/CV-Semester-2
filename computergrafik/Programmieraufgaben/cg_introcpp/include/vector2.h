# pragma once // this is a preprocessor macro. It protects us from including headers multiple time.
              // E.g. iostream is included here but we also included it in the main.cpp

#include <iostream>

// The class definition
class Vector2{

// Functions and Variables that are public, aka accessible from the outside
public:
    
    // the constructor (basically same as below.)
    Vector2(double x, double y) : m_x{x}, m_y{y} {};
    //Vector2(double x, double y) { m_x = x; m_y = y;};
    
    // the destructor
    ~Vector2() { /*nothing to be done because our class does not hold anything created with new */ };
    
    // These are only declarations. The definition is done in the .cpp file
    // You can see that sometimes, there is a "const" after the declaration.
    // This means that this function will not modify the object when it is called.
    // We need this because sometimes we want to pass an object to a function, eg as const reference
    // then we are not allowed to modify the object and the compiler needs to know if we can safely call
    // a member function
    
    double GetX() const;
    double GetY() const;
    
    void SetX(double x); // here is no const because we modify the object
    void SetY(double y);
    
    bool IsUnitLength() const;
    
    // this is the output operator of the vector class
    // This is so we can easily std::cout objects of our class
    // "friend" means that this function is not a member function
    // but it is allowed to access the private members of our class
    friend std::ostream& operator<<( std::ostream& output, const Vector2& V ) { 
        output << "x : " << V.m_x << " y : " << V.m_y;
        return output;            
    }
    
    // we define addition for our vector
    Vector2 operator+(const Vector2& rhs) const {
        return add(*this, rhs);
    }
    
    // if we define +, we normally would also define other operators
    // +=. -, -=, *, *=, /, /=, ...
    
// Functions and Variables that are private, aka only accessible from within the class
private:
    double m_x;
    double m_y;

    static Vector2 add(const Vector2& v1, const Vector2& v2){
        return Vector2{ v1.GetX() + v2.GetX(), v1.GetY() + v2.GetY() };
    };
    
};

// This is where we would usually put the operator definition for the << operator
// Note that it is not inside the class and we have to use getters and setters
/*
std::ostream& operator<<( std::ostream& output, const Vector2& V ) { 
    output << "x : " << V.GetX() << " y : " << V.GetY();
    return output;            
}
*/
