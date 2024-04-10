#include "vector2.h"

#include <cmath>

// Here are the definitions of the class member functions
// Note the "Vector2::" in front. This specifies that we want to define
// the functions of that class and not define a completely new function
// with a similar name

double Vector2::GetX() const{
    return m_x;
};

double Vector2::GetY() const{
    return m_y;
};

void Vector2::SetX(double x){
    m_x = x;
};

void Vector2::SetY(double y){
    m_y = y;
};

bool Vector2::IsUnitLength() const{
    return std::abs(m_x*m_x + m_y*m_y - 1) < 1e-15; 
};
