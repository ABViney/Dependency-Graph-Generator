cmake_minimum_required(VERSION 3.0)
project(DGG-Example)

add_executable(DGG-Example DGG-Example.cpp
	./src/SimpleObject.cpp
	./include/SimpleObject.h

    ./src/SimplerObject.cxx
    ./include/SimplerObject.hpp
)