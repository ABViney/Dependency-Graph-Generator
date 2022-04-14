#include "../include/SimpleObject.h"

SimpleObject::SimpleObject(int id):
                            id(id) {}

int SimpleObject::printId() { return id; }


SimpleObject & SimpleObject::operator += (const SimpleObject rhs) {
    this->id += rhs.id;
    return *this;
}
