#include <iostream>
#include "include/SimplerObject.hpp"

int main() {
    SimpleObject s1 = SimpleObject(2);
    SimpleObject s2 = SimpleObject(3);
    SimpleObject s3 = s1+s2;
    SimplerObject s10 = SimplerObject();
    std::cout << s10.printId() << std::endl;
    std::cout << s3.printId() << std::endl;
    s3 += s10;
    std::cout << s3.printId() << std::endl;
}