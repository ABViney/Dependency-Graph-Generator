#pragma once
#ifndef SIMPLE_H
#define SIMPLE_H

class SimpleObject {
    int id;

public:

    SimpleObject(int id);

    int printId();

    SimpleObject & operator += (const SimpleObject rhs);
    inline SimpleObject operator + (const SimpleObject & rhs) {
    return SimpleObject(this->id) += rhs;
}
};

#endif /* SimpleObject.h */